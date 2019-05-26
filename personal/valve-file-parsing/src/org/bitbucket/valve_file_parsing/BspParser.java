package org.bitbucket.valve_file_parsing;

import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.valve_file_parsing.bsp.GameLump;
import org.bitbucket.valve_file_parsing.bsp.GameLumpHeader;
import org.bitbucket.valve_file_parsing.bsp.Header;
import org.bitbucket.valve_file_parsing.bsp.Leaf;
import org.bitbucket.valve_file_parsing.bsp.Lump;
import org.bitbucket.valve_file_parsing.bsp.Node;
import org.bitbucket.valve_file_parsing.bsp.Plane;
import org.bitbucket.valve_file_parsing.bsp.StaticPropDictLump;
import org.bitbucket.valve_file_parsing.bsp.StaticPropLeafLump;
import org.bitbucket.valve_file_parsing.bsp.StaticPropLump;
import org.bitbucket.valve_file_parsing.math.Color32;
import org.bitbucket.valve_file_parsing.math.ColorRgbExp32;
import org.bitbucket.valve_file_parsing.math.CompressedLightCube;

public final class BspParser extends FileParser {
	private Header header;
	// TODO(Eric) Make getters for the different lumps
	//private Entities entityLump;
	private Plane[] planeLump;
	//private TexData[] texDataLump;
	//private Vector[] vertexLump;
	//private List<Vis> visLump;
	private Node[] nodeLump;
	private Leaf[] leafLump;
	private GameLumpHeader gameLumpHeader;
	private StaticPropDictLump staticPropDictLump;
	private StaticPropLeafLump staticPropLeafLump;
	private StaticPropLump[] staticPropLump;
	
	@Override
	public byte[] data() {
		// TODO Auto-generated method stub
		return null;
	}
	/*public Entities getEntityLump() {
		return entityLump;
	}*/
	public GameLumpHeader getGameLumpHeader() {
		return gameLumpHeader;
	}
	public Header getHeader() {
		return header;
	}
	public Leaf[] getLeafLump() {
		return leafLump;
	}
	public Node[] getNodeLump() {
		return nodeLump;
	}
	public Plane[] getPlaneLump() {
		return planeLump;
	}
	public StaticPropDictLump getStaticPropDictLump() {
		return staticPropDictLump;
	}
	public StaticPropLeafLump getStaticPropLeafLump() {
		return staticPropLeafLump;
	}
	public StaticPropLump[] getStaticPropLump() {
		return staticPropLump;
	}
	/*public TexData[] getTexDataLump() {
		return texDataLump;
	}
	public Vector[] getVertexLump() {
		return vertexLump;
	}
	public List<Vis> getVisLump() {
		return visLump;
	}*/
	private boolean lumpUsed(final Lump lump) {
		for (final byte b : lump.fourCc) {
			if (b != 0) {
				return true;
			}
		}
		return lump.fileOffs != 0 || lump.fileLen != 0 || lump.version != 0;
	}
	@Override
	public void parse(final ByteBuffer dataBuf) throws InvalidDataException {
		// TODO Auto-generated method stub
		dataBuf.mark();
		final int identifier = dataBuf.getInt();
		if (identifier != 0x50534256) {
			throw new InvalidDataException("Invalid BSP identifier: " + identifier);
		}
		final int version = dataBuf.getInt();
		final Lump[] lumps = new Lump[Header.HEADER_LUMPS];
		for (int i = 0; i < lumps.length; i++) {
			lumps[i] = new Lump(dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt(), new byte[] {dataBuf.get(), dataBuf.get(), dataBuf.get(), dataBuf.get()});
		}
		final int mapRevision = dataBuf.getInt();
		header = new Header(identifier, version, lumps, mapRevision);
		final boolean[] parsedLumps = new boolean[4]; // Eric: This way, we can simply break the loop for efficiency
		for (int i = 0; i < lumps.length; i++) {
			if (!lumpUsed(lumps[i])) {
				continue;
			}
			final byte[] lumpData = new byte[lumps[i].fileLen];
			dataBuf.reset();
			dataBuf.position(lumps[i].fileOffs);
			switch (i) {
			/*case Header.LUMP_ENTITIES:
				dataBuf.get(lumpData);
				try {
					entityLump = new Entities(new String(lumpData, "US-ASCII"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				parsedLumps[0] = true;
				break;*/
			case Header.LUMP_PLANES:
				planeLump = new Plane[lumpData.length / (4 * 3 + 4 + 4)]; // TODO(Eric) Better way of getting the amount of planes?
				for (int j = 0; j < planeLump.length; j++) {
					planeLump[j] = new Plane(new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat()), dataBuf.getFloat(), dataBuf.getInt());
				}
				parsedLumps[0] = true;
				break;/*
			case Header.LUMP_TEXDATA:
				texDataLump = new TexData[lumpData.length / (4 * 3 + 4 + 4 * 2 + 4 * 2)];
				for (int j = 0; j < texDataLump.length; j++) {
					texDataLump[j] = new TexData(new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat()), dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt());
				}
				parsedLumps[2] = true;
				break;
			case Header.LUMP_VERTEXES:
				vertexLump = new Vector[lumpData.length / (4 * 3)];
				for (int j = 0; j < vertexLump.length; j++) {
					vertexLump[j] = new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat());
				}
				parsedLumps[3] = true;
				break;
			case Header.LUMP_VISIBILITY:
				visLump = new ArrayList<Vis>();
				for (int j = 0; j < lumpData.length;) {
					final int numClusters = dataBuf.getInt();
					j += 4;
					final int OFS_DIM_LEN = 2;
					final int[][] byteOfs = new int[8][OFS_DIM_LEN];
					for (int k = 0; k < byteOfs.length; k++) {
						for (int l = 0; l < byteOfs[k].length; l++) {
							byteOfs[k][l] = dataBuf.getInt();
						}
					}
					visLump.add(new Vis(numClusters, byteOfs));
					j += byteOfs.length * OFS_DIM_LEN;
				}
				parsedLumps[4] = true;
				break;*/
			case Header.LUMP_NODES:
				nodeLump = new Node[lumpData.length / (4 + 4 * 2 + 2 * 3 + 2 * 3 + 2 + 2 + 2 + 2)];
				for (int j = 0; j < nodeLump.length; j++) {
					nodeLump[j] = new Node(dataBuf.getInt(), new int[] {dataBuf.getInt(), dataBuf.getInt()}, new short[] {dataBuf.getShort(), dataBuf.getShort(), dataBuf.getShort()}, new short[] {dataBuf.getShort(), dataBuf.getShort(), dataBuf.getShort()}, dataBuf.getChar(), dataBuf.getChar(), dataBuf.getShort(), dataBuf.getShort());
				}
				parsedLumps[1] = true;
				break;
			case Header.LUMP_LEAFS:
				leafLump = new Leaf[lumpData.length / (4 + 2 + (9 + 7) / 8 * 2 + 2 * 3 + 2 * 3 + 2 + 2 + 2 + 2 + 2 + (version <= 19 ? 6 * (1 * 4) + 2 : 0))];
				for (int j = 0; j < leafLump.length; j++) {
					leafLump[j] = new Leaf(dataBuf.getInt(), dataBuf.getShort(), dataBuf.getInt(), new short[] {dataBuf.getShort(), dataBuf.getShort(), dataBuf.getShort()}, new short[] {dataBuf.getShort(), dataBuf.getShort(), dataBuf.getShort()}, dataBuf.getChar(), dataBuf.getChar(), dataBuf.getChar(), dataBuf.getChar(), dataBuf.getShort());
					if (version <= 19) {
						final ColorRgbExp32[] colors = new ColorRgbExp32[6];
						for (int k = 0; k < colors.length; k++) {
							colors[k] = new ColorRgbExp32(dataBuf.get(), dataBuf.get(), dataBuf.get(), dataBuf.get());
						}
						leafLump[j].ambientLighting = new CompressedLightCube(colors);
						leafLump[j].padding = dataBuf.getShort();
					}
				}
				parsedLumps[2] = true;
				break;
			case Header.LUMP_GAME_LUMP:
				final int lumpCount = dataBuf.getInt();
				final GameLump[] gameLumps = new GameLump[lumpCount];
				for (int j = 0; j < gameLumps.length; j++) {
					gameLumps[j] = new GameLump(dataBuf.getInt(), dataBuf.getChar(), dataBuf.getChar(), dataBuf.getInt(), dataBuf.getInt());
					if (gameLumps[j].id == 1936749168) {
						final int prevPos = dataBuf.position();
						dataBuf.position(gameLumps[j].fileOfs);
						final int dictEntries = dataBuf.getInt();
						final byte[][] name = new byte[dictEntries][128];
						for (int k = 0; k < name.length; k++) {
							dataBuf.get(name[k]);
						}
						staticPropDictLump = new StaticPropDictLump(dictEntries, name);
						final int leafEntries = dataBuf.getInt();
						final char[] leaf = new char[leafEntries];
						for (int k = 0; k < leaf.length; k++) {
							leaf[k] = dataBuf.getChar();
						}
						staticPropLeafLump = new StaticPropLeafLump(leafEntries, leaf);
						final int lumpSz = dataBuf.getInt();
						staticPropLump = new StaticPropLump[lumpSz];
						for (int k = 0; k < staticPropLump.length; k++) {
							staticPropLump[k] = new StaticPropLump(new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat()), new float[] {dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat()}, dataBuf.getChar(), dataBuf.getChar(), dataBuf.getChar(), dataBuf.get(), dataBuf.get(), dataBuf.getInt(), dataBuf.getFloat(), dataBuf.getFloat(), new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat()));
							if (version >= 5) {
								staticPropLump[k].forcedFadeScale = dataBuf.getFloat();
							}
							if (version == 6 || version == 7) {
								staticPropLump[k].minDXLevel = dataBuf.getChar();
								staticPropLump[k].maxDXLevel = dataBuf.getChar();
							}
							if (version >= 8) {
								staticPropLump[k].minCPULevel = dataBuf.get();
								staticPropLump[k].maxCPULevel = dataBuf.get();
								staticPropLump[k].minGPULevel = dataBuf.get();
								staticPropLump[k].maxGPULevel = dataBuf.get();
							}
							if (version >= 7) {
								staticPropLump[k].diffuseModulation = new Color32(dataBuf.get(), dataBuf.get(), dataBuf.get(), dataBuf.get());
							}
							if (version >= 10) {
								staticPropLump[k].unknown = dataBuf.getFloat();
							}
							if (version >= 9) {
								staticPropLump[k].disableX360 = dataBuf.get() != 0;
							}
							dataBuf.get(new byte[3]);
						}
						dataBuf.position(prevPos);
					}
				}
				gameLumpHeader = new GameLumpHeader(lumpCount, gameLumps);
				parsedLumps[3] = true;
				break;
			default:
				break;
			}
			boolean parsedAll = true;
			for (final boolean parsedLump : parsedLumps) {
				parsedAll &= parsedLump;
			}
			if (parsedAll) {
				break;
			}
		}
	}
}
