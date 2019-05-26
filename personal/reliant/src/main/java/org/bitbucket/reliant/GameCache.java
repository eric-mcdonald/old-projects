package org.bitbucket.reliant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.log.Logger;
import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.util.SdkUtils;
import org.bitbucket.valve_file_parsing.BspParser;
import org.bitbucket.valve_file_parsing.MdlParser;
import org.bitbucket.valve_file_parsing.VpkParser;
import org.bitbucket.valve_file_parsing.bsp.StaticPropLump;
import org.bitbucket.valve_file_parsing.mdl.MsStudioBbox;
import org.bitbucket.valve_file_parsing.mdl.MsStudioHitboxSet;
import org.bitbucket.valve_file_parsing.mdl.StudioHdr;
import org.bitbucket.valve_file_parsing.vpk.VpkDirectoryEntry;

public final class GameCache {
	public static class ParseMapThread extends Thread {
		public static final class MdlBox {
			public final Vector mins, maxs;
			
			public MdlBox(final Vector mins, final Vector maxs) {
				this.mins = mins;
				this.maxs = maxs;
			}
			
			@Override
			public boolean equals(Object obj) {
				if (!(obj instanceof MdlBox)) {
					return false;
				}
				final MdlBox mdlBox = (MdlBox) obj;
				return SdkUtils.vectorsEqualTrace(mdlBox.mins, mins) && SdkUtils.vectorsEqualTrace(mdlBox.maxs, maxs);
			}
			@Override
			public int hashCode() {
				int hashCode = mins.hashCode();
				hashCode = 31 * hashCode + maxs.hashCode();
				return hashCode;
			}
		}
		public final BspParser bspParser = new BspParser();
		public final Set<MdlBox> mdlBoxes = new HashSet<MdlBox>();

		@Override
		public void run() {
			String dirPrefix = Reliant.instance.getProcessStream().moduleFileName(MemoryStream.NULL);
			if (dirPrefix != null) {
				final int commonIdx = dirPrefix.indexOf(File.separator + "common");
				if (commonIdx != -1) {
					dirPrefix = (Main.csco() ? dirPrefix.substring(0, commonIdx) + File.separator + "sourcemods" + File.separator : dirPrefix.substring(0, dirPrefix.lastIndexOf(File.separator) + File.separator.length())) + Main.getTargetDataDir();
					final String mapDir = GameCache.mapDir;
					Reliant.instance.getLogger().log("Parsing map \"" + mapDir + "\"", Logger.Type.INFO);
					try {
						bspParser.parse(new File(dirPrefix + File.separator + mapDir));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Reliant.instance.getLogger().logError(e);
					}
					final VpkParser pakParser = new VpkParser();
					final File pakFile = new File(dirPrefix + File.separator + "pak01_dir.vpk");
					Reliant.instance.getLogger().log("Parsing static prop hitboxes.", Logger.Type.INFO);
					try {
						pakParser.parse(pakFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Reliant.instance.getLogger().logError(e);
					}
					final HashMap<String, MdlParser> mdlParserMap = new HashMap<String, MdlParser>();
					for (final StaticPropLump staticProp : bspParser.getStaticPropLump()) {
						try {
							final String propMdl = StringUtils.crop(new String(bspParser.getStaticPropDictLump().name[staticProp.propType], "US-ASCII"));
							if (mdlParserMap.containsKey(propMdl)) {
								continue;
							}
							final VpkDirectoryEntry dirEntry = pakParser.getDirEntryMap().get(propMdl);
							if (dirEntry != null) {
								if (dirEntry.archiveIndex == 0x7fff) {
									mdlParserMap.put(propMdl, new MdlParser());
									try {
										final FileInputStream fileIn = new FileInputStream(pakFile);
										final ByteBuffer dataBuf = NioUtils.createBuffer((int) pakFile.length());
										fileIn.read(dataBuf.array());
										fileIn.close();
										dataBuf.position((4 + 4 + 4) + (pakParser.getVpkHeader1() == null ? pakParser.getVpkHeader2().treeSize : pakParser.getVpkHeader1().treeSize) + dirEntry.entryOffset);
										if (dirEntry.entryLength != 0) {
											mdlParserMap.get(propMdl).parse(dataBuf);
										}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										Reliant.instance.getLogger().logError(e);
									}
								} else {
									File entryPak = new File(dirPrefix + File.separator + "pak01_" + String.format("%03d", (short) dirEntry.archiveIndex) + ".vpk");
									if (!entryPak.exists()) {
										entryPak = new File(dirPrefix + File.separator + "pak02_" + String.format("%03d", (short) dirEntry.archiveIndex) + ".vpk");
									}
									mdlParserMap.put(propMdl, new MdlParser());
									if (dirEntry.entryLength == 0) {
										continue;
									}
									try {
										final FileInputStream fileIn = new FileInputStream(entryPak);
										fileIn.skip(dirEntry.entryOffset); // Eric: Reading the entire file into a buffer can throw OutOfMemoryError; so, we skip unneeded bytes instead
										final ByteBuffer fileDataBuf = NioUtils.createBuffer(dirEntry.entryLength);
										fileIn.read(fileDataBuf.array());
										fileIn.close();
										if (dirEntry.entryLength != 0) {
											mdlParserMap.get(propMdl).parse(fileDataBuf);
										}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										Reliant.instance.getLogger().logError(e);
									}
								}
							}
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							Reliant.instance.getLogger().logError(e);
						}
					}
					for (final StaticPropLump staticProp : bspParser.getStaticPropLump()) {
						try {
							final String propMdl = StringUtils.crop(new String(bspParser.getStaticPropDictLump().name[staticProp.propType], "US-ASCII"));
							if (!mdlParserMap.containsKey(propMdl)) {
								continue;
							}
							final MdlParser mdlParser = mdlParserMap.get(propMdl);
							if ((mdlParser.getStudioHdr().flags & StudioHdr.STUDIOHDR_FLAGS_AUTOGENERATED_HITBOX) != 0) {
								continue;
							}
							for (final MsStudioHitboxSet studioHitboxSet : mdlParser.getHitboxSets()) {
								for (final MsStudioBbox studioBbox : studioHitboxSet.bboxes) {
									Vector bbMinCpy = studioBbox.bbMin.copy(), bbMaxCpy = studioBbox.bbMax.copy();
									bbMinCpy.minimum(bbMaxCpy);
									bbMaxCpy.maximum(bbMinCpy);
									final float[][] angleMatrix = org.bitbucket.eric_generic.math.MathHelper.angleMatrix(staticProp.angles);
									final Vector localCenter = bbMinCpy.copy();
									localCenter.add(bbMaxCpy);
									final float MID_DIV = 2.0F;
									localCenter.divide(new Vector(MID_DIV, MID_DIV, MID_DIV));
									final Vector localExtents = bbMaxCpy.copy();
									localExtents.subtract(localCenter);
									final Vector newCenter = localCenter.copy();
									newCenter.rotate(angleMatrix);
									final Vector newExtents = localExtents.copy();
									newExtents.x = newExtents.dotProductAbs(Vector.wrap(angleMatrix[0]));
									newExtents.z = newExtents.dotProductAbs(Vector.wrap(angleMatrix[1]));
									newExtents.y = newExtents.dotProductAbs(Vector.wrap(angleMatrix[2]));
									bbMinCpy = new Vector(newCenter.x - newExtents.x, newCenter.z - newExtents.z, newCenter.y - newExtents.y);
									bbMaxCpy = new Vector(newCenter.x + newExtents.x, newCenter.z + newExtents.z, newCenter.y + newExtents.y);
									bbMinCpy.add(staticProp.origin);
									bbMaxCpy.add(staticProp.origin);
									mdlBoxes.add(new MdlBox(bbMinCpy, bbMaxCpy));
								}
							}
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							Reliant.instance.getLogger().logError(e);
						}
					}
				}
			}
		}
	}
	private static int localPlayer;
	private static int clientState;
	private static int radarBasePtr;
	private static int crosshairEntity;
	private static int inGame;
	private static String crosshairName;
	private static boolean reloading;
	private static String mapDir; // Eric: The map directory is actually both the directory and filename of the map, relative to the game's directory
	private static final Map<String, ParseMapThread> mapParsers = new HashMap<String, ParseMapThread>();

	public static final float[][] viewMatrix = new float[4][4];

	public static int getClientState() {
		return clientState;
	}
	public static int getCrosshairEntity() {
		return crosshairEntity;
	}
	public static String getCrosshairName() {
		return crosshairName;
	}
	public static int getInGame() {
		return inGame;
	}
	public static int getLocalPlayer() {
		return localPlayer;
	}
	public static String getMapDir() {
		return mapDir;
	}
	public static ParseMapThread getMapParser(final String map) {
		return mapParsers.get(map);
	}
	public static int getRadarBasePtr() {
		return radarBasePtr;
	}
	public static boolean isReloading() {
		return reloading;
	}
	static void update() {
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr != MemoryStream.NULL) {
			localPlayer = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwLocalPlayer"));
			// Eric: Do not remove the following line! It fixes a race condition between crosshairEntity and crosshairName.
			final short crosshairId = Reliant.instance.getProcessStream().readShort(localPlayer + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iCrossHairID"));
			crosshairEntity = localPlayer != MemoryStream.NULL ? SdkUtils.entityById(crosshairId) : MemoryStream.NULL;
			crosshairName = crosshairEntity != MemoryStream.NULL ? SdkUtils.readRadarName(crosshairId - 1) : null;
			final int radarBase = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwRadarBase"));
			if (radarBase != MemoryStream.NULL) {
				radarBasePtr = Reliant.instance.getProcessStream().readInt(radarBase + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwRadarBasePointer"));
			}
		} else {
			localPlayer = radarBasePtr = crosshairEntity = MemoryStream.NULL;
			crosshairName = null;
		}
		for (int i = 0; i < viewMatrix.length; i++) {
			if (clientAddr != MemoryStream.NULL) {
				((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * viewMatrix[i].length).put(Reliant.instance.getProcessStream().read(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwViewMatrix") + MemoryStream.FLOAT_SZ * viewMatrix[i].length * i, MemoryStream.FLOAT_SZ * viewMatrix[i].length)).rewind()).asFloatBuffer().get(viewMatrix[i]);
			} else {
				viewMatrix[i] = new float[viewMatrix[i].length];
			}
		}
		final long engineAddr = Reliant.instance.getProcessStream().moduleAddress("engine.dll");
		clientState = engineAddr != MemoryStream.NULL ? Reliant.instance.getProcessStream().readInt(engineAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwClientState")) : MemoryStream.NULL;
		final String prevMapDir = mapDir;
		if (clientState != MemoryStream.NULL) {
			inGame = Reliant.instance.getProcessStream().readInt(clientState + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwInGame"));
			if (inGame == SdkUtils.SIGNONSTATE_FULL) {
				mapDir = Reliant.instance.getProcessStream().read(clientState + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwMapDirectory"), 256, "US-ASCII");
			}
		} else {
			inGame = SdkUtils.SIGNONSTATE_NONE;
			mapDir = null;
		}
		if (!StringUtils.empty(mapDir) && !mapDir.equals(prevMapDir) && !mapParsers.containsKey(mapDir) && inGame == SdkUtils.SIGNONSTATE_FULL) {
			mapParsers.put(mapDir, new ParseMapThread());
			mapParsers.get(mapDir).start();
		}
		if (SdkUtils.entityAlive(localPlayer)) {
			final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(localPlayer + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
			reloading = weaponEntity != MemoryStream.NULL && Reliant.instance.getProcessStream().readByte(weaponEntity + Reliant.instance.getOffsetsRegistry().get("haze").offset("m_bInReload")) != MemoryStream.FALSE; // TODO(Eric) This is not good enough for flNextAttackTime - globalVars->curtime
		} else {
			reloading = false;
		}
	}
}
