package org.bitbucket.reliant.routine.impl;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.GameCache.ParseMapThread;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.TextureOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.PlayerRoutine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.SdkUtils;
import org.bitbucket.valve_file_parsing.bsp.Brush;

public final class EspRoutine extends PlayerRoutine {
	private final Map<Integer, Long> playerDamageMap = new HashMap<Integer, Long>();
	private final Map<Integer, Short> prevHealthMap = new HashMap<Integer, Short>();

	public EspRoutine() {
		super("ESP", "Highlights a player by drawing a box on them.", true, 1003, new BoolOption("Flashed", "Specifies whether or not to execute while flashed.", true), new BoolOption("Visibility", "Specifies whether or not to only execute if a player is visible.", false), new BoolOption("Bomb", "Specifies whether or not to display if a player has the bomb.", true), new FloatOption("Line Width", "Specifies the width of a line.", new ClampedNumber<Float>(1.0F, 0.1F, 10.0F), 0.1F), new BoolOption("Trim", "Specifies whether or not to draw outlines.", true), new BoolOption("Ray Trace", "Specifies whether or not to ray trace for visibility.", false), new TextureOption("Texture", "Specifies the image file to use as a texture.", ""), new IntOption("View", "Specifies the mode to draw a player's view vector with. Set to 0 for no view line.", new ClampedNumber<Integer>(1, 0, 2), 1), new BoolOption("Fade", "Specifies whether or not to make the box fade out as you move closer to a highlighted player.", true), new BoolOption("Name", "Specifies whether or not to display a player's name.", true), new BoolOption("All Teams", "Specifies whether or not to highlight all teams as enemies.", false), new BoolOption("Health", "Specifies whether or not to display a player's health.", true), new IntOption("Armor Color", "Specifies the color to use when displaying a player's armor.", new ClampedNumber<Integer>(0x00BFFF, 0x0, 0xFFFFFF), 16000000), new BoolOption("Weapon", "Specifies whether or not to display a player's weapon.", true), new BoolOption("Damage", "Specifies whether or not to flash from green to red when a player gets damaged.", true), new FloatOption("Ray Trace Step", "Specifies the perferred value to step by whilst tracing a ray.", new ClampedNumber<Float>(1.0F, 1.0F, 100.0F), 10.0F), new IntOption("Mode", "Specifies the condition to draw on.", new ClampedNumber<Integer>(0, 0, 2), 1));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFF00FF00;
	}

	@Override
	public boolean handle(final int player, final int entityIdx) {
		if (!super.handle(player, entityIdx) || !Reliant.instance.getRenderer().shouldDraw()) {
			return false;
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return false;
		}
		if (!SdkUtils.entityAlive(player) || player == GameCache.getLocalPlayer()) {
			return false;
		}
		if (!getBoolean("Flashed") && Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flFlashDuration")) > 0.0F) {
			return false;
		}
		final int mode = getInt("Mode");
		final short playerTeam = Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")), localTeam = Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"));
		if (mode == 1 && playerTeam == localTeam || mode == 2 && playerTeam != localTeam || playerTeam != localTeam && getBoolean("Visibility") && !SdkUtils.entityVisible(player, true, SdkUtils.VisAggressiveness.BASIC, true, 1.0F)) {
			return false;
		}
		final short fullHealth = (short) (Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iHealth")) + Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_ArmorValue")));
		if (!prevHealthMap.containsKey(player) || fullHealth > prevHealthMap.get(player)) {
			prevHealthMap.put(player, fullHealth);
		}
		if (fullHealth < prevHealthMap.get(player)) {
			playerDamageMap.put(player, System.currentTimeMillis());
			prevHealthMap.remove(player);
		}
		final float[] mins = new float[3], maxs = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * mins.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_vecMins"), MemoryStream.FLOAT_SZ * mins.length)).rewind()).asFloatBuffer().get(mins);
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * maxs.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_vecMaxs"), MemoryStream.FLOAT_SZ * maxs.length)).rewind()).asFloatBuffer().get(maxs);
		final float[] playerAbsOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * playerAbsOrigin.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getCustomOffsetsRegistry().get("m_vecAbsOrigin"), MemoryStream.FLOAT_SZ * playerAbsOrigin.length)).rewind()).asFloatBuffer().get(playerAbsOrigin);
		final Vector minsVec = Vector.wrap(mins), maxsVec = Vector.wrap(maxs);
		final Vector absOriginVec = Vector.wrap(playerAbsOrigin);
		minsVec.add(absOriginVec);
		maxsVec.add(absOriginVec);
		final float[][] screenPos = new float[][] {Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, minsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, maxsVec.z, maxsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, minsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, maxsVec.z, maxsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, maxsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, maxsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, minsVec.z, maxsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, minsVec.z, maxsVec.y})};
		for (final float[] pos : screenPos) {
			if (pos == null || pos[3] < org.bitbucket.eric_generic.math.MathHelper.EQUALS_PRECISION) {
				return false;
			}
		}
		float left = screenPos[0][0], top = screenPos[0][1], right = screenPos[0][0], bottom = screenPos[0][1];
		for (int posIdx = 1; posIdx < screenPos.length; posIdx++) {
			if (left > screenPos[posIdx][0]) {
				left = screenPos[posIdx][0];
			}
			if (top > screenPos[posIdx][1]) {
				top = screenPos[posIdx][1];
			}
			if (right < screenPos[posIdx][0]) {
				right = screenPos[posIdx][0];
			}
			if (bottom < screenPos[posIdx][1]) {
				bottom = screenPos[posIdx][1];
			}
		}
		// Eric: Hotfix for the health bar being in the camera when it shouldn't be.
		boolean notInScreen = true;
		for (final float[] screenPoint : new float[][] {new float[] {left, top}, new float[] {left, bottom}, new float[] {right, top}, new float[] {right, bottom}}) {
			if (Reliant.instance.getRenderer().inScreen(screenPoint)) {
				notInScreen = false;
				break;
			}
		}
		if (notInScreen) {
			return false;
		}
		final float boxSz = org.bitbucket.eric_generic.math.MathHelper.distance(left, top, 0.0F, right, bottom, 0.0F);
		final float lineWidth = getFloat("Line Width");
		final float lineOffset = Math.min(lineWidth - 1.0F, 0.0F);
		final float barWidth = boxSz / 24.0F + lineOffset, xOffset = boxSz / 48.0F + lineOffset;
		final boolean outlines = getBoolean("Trim");
		final float[] localAbsOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localAbsOrigin.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getCustomOffsetsRegistry().get("m_vecAbsOrigin"), MemoryStream.FLOAT_SZ * localAbsOrigin.length)).rewind()).asFloatBuffer().get(localAbsOrigin);
		final float playerDist = org.bitbucket.eric_generic.math.MathHelper.distance(localAbsOrigin[0], localAbsOrigin[1], localAbsOrigin[2], playerAbsOrigin[0], playerAbsOrigin[1], playerAbsOrigin[2]);
		final int OUTLINE_COL = 0xFF000000;
		final float outlineWidth = lineWidth - Math.min(playerDist / SdkUtils.MAX_VIS_RANGE, lineWidth);
		// Eric: Extends the bars' height to look normal when Outlines is enabled.
		final float outlinedTop = outlines ? top - outlineWidth : top, outlinedBottom = outlines ? bottom + outlineWidth : bottom;
		if (getBoolean("Health")) {
			Reliant.instance.getRenderer().drawQuad(left - barWidth - xOffset, outlinedTop, left - xOffset, outlinedBottom, 0xFF000000, MemoryStream.NULL);
			float healthPercent = Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iHealth")) / 100.0F;
			final float MAX_HEALTH_PERCENT = 1.0F;
			if (healthPercent > MAX_HEALTH_PERCENT) {
				healthPercent = MAX_HEALTH_PERCENT;
			}
			if (healthPercent > 0.0F) {
				Reliant.instance.getRenderer().drawQuad(left - barWidth - xOffset, outlinedTop + (outlinedBottom - outlinedTop) * (MAX_HEALTH_PERCENT - healthPercent), left - xOffset, outlinedBottom, new Color(1.0F - healthPercent, healthPercent, 0.0F, 1.0F).getRGB(), MemoryStream.NULL);
				if (outlines) {
					Reliant.instance.getRenderer().drawOutline(left - barWidth - xOffset, outlinedTop, left - xOffset - (left - barWidth - xOffset), outlinedBottom - outlinedTop, OUTLINE_COL, outlineWidth);
				}
			}
		}
		final int armorCol = getInt("Armor Color");
		if (armorCol > 0x0) {
			Reliant.instance.getRenderer().drawQuad(right + xOffset, outlinedTop, right + xOffset + barWidth, outlinedBottom, 0xFF000000, MemoryStream.NULL);
			float armorPercent = Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_ArmorValue")) / 100.0F;
			final float MAX_ARMOR_PERCENT = 1.0F;
			if (armorPercent > MAX_ARMOR_PERCENT) {
				armorPercent = MAX_ARMOR_PERCENT;
			}
			if (armorPercent > 0.0F) {
				Reliant.instance.getRenderer().drawQuad(right + xOffset, outlinedTop + (outlinedBottom - outlinedTop) * (MAX_ARMOR_PERCENT - armorPercent), right + xOffset + barWidth, outlinedBottom, new Color(armorCol, false).getRGB(), MemoryStream.NULL);
				if (outlines) {
					Reliant.instance.getRenderer().drawOutline(right + xOffset, outlinedTop, right + xOffset + barWidth - (right + xOffset), outlinedBottom - outlinedTop, OUTLINE_COL, outlineWidth);
				}
			}
		}
		final float rayTraceStep = getFloat("Ray Trace Step");
		float alpha = !getBoolean("Fade") || (getBoolean("Ray Trace") ? !SdkUtils.entityVisTrace(player, true, rayTraceStep) : !SdkUtils.entityVisible(player, true, SdkUtils.VisAggressiveness.BASIC, true, rayTraceStep)) ? 1.0F : playerDist / (SdkUtils.MAX_DIST / 3.0F);
		if (alpha > 1.0F) {
			alpha = 1.0F;
		}
		final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
		final String playerName = SdkUtils.readRadarName(entityIdx);
		if (StringUtils.empty(playerName)) {
			return false;
		}
		final int color = (nameProtect.isEnabled() && nameProtect.getBoolean("Allies") && nameProtect.containsAlias(playerName) ? new Color(0.0F, 0.5F, 1.0F, alpha) : getBoolean("All Teams") || playerTeam != localTeam ? !getBoolean("Damage") || playerDamageMap.containsKey(player) && System.currentTimeMillis() - playerDamageMap.get(player) <= 500L ? new Color(1.0F, 0.0F, 0.0F, alpha) : new Color(0.0F, 1.0F, 0.0F, alpha) : new Color(0.0F, 0.0F, 1.0F, alpha)).getRGB();
		final long texture = getTexture("Texture");
		if (texture != MemoryStream.NULL) {
			Reliant.instance.getRenderer().drawQuad(left, top, right, bottom, color, texture);
		}
		Reliant.instance.getRenderer().drawOutline(left, top, right - left, bottom - top, color, lineWidth);
		final int view = getInt("View");
		if (view > 0) {
			float[] eyeAngles = new float[2];
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * eyeAngles.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_angEyeAngles"), MemoryStream.FLOAT_SZ * eyeAngles.length)).rewind()).asFloatBuffer().get(eyeAngles);
			final Vector anglesVec = org.bitbucket.eric_generic.math.MathHelper.anglesVec(eyeAngles);
			final ParseMapThread mapParser = GameCache.getMapDir() == null ? null : GameCache.getMapParser(GameCache.getMapDir());
			final boolean traceVis = view == 2 && mapParser != null && !mapParser.isAlive();
			final float lineDist = traceVis ? SdkUtils.MAX_DIST : 100.0F;
			anglesVec.x *= lineDist;
			anglesVec.y *= lineDist;
			anglesVec.z *= lineDist;
			final float[] playerBone = SdkUtils.entityBone(player, SdkUtils.headBoneId());
			if (playerBone == null) {
				throw new InvalidDataException("player_bone");
			}
			final Vector playerBoneVec = Vector.wrap(playerBone);
			if (traceVis) {
				final Vector traceEndVec = anglesVec.copy();
				traceEndVec.add(playerBoneVec);
				final float viewTrace = SdkUtils.traceRay(playerBoneVec, traceEndVec, rayTraceStep, Brush.CONTENTS_SOLID, true);
				anglesVec.x *= viewTrace;
				anglesVec.y *= viewTrace;
				anglesVec.z *= viewTrace;
			}
			anglesVec.add(playerBoneVec);
			final float[] screenAngles = Reliant.instance.getRenderer().screenPos(anglesVec.data()), screenBone = Reliant.instance.getRenderer().screenPos(playerBone);
			Reliant.instance.getRenderer().drawLine(screenBone[0], screenBone[1], screenAngles[0], screenAngles[1], new Color(1.0F, 0.0F, 1.0F, 1.0F).getRGB(), lineWidth);
		}
		final String font = Reliant.instance.getWorldFont();
		final int FONT_SZ = 12;
		final boolean SHADOW = true;
		final int SPACING = 2;
		if (getBoolean("Name")) {
			final int[] nameSz = Reliant.instance.getRenderer().textSize(playerName, font, FONT_SZ, SHADOW);
			if (nameSz != null) {
				final int midWidth = nameSz[0] / 2;
				final int yOffset = nameSz[1] + 4;
				final float outlinedX1 = outlines ? (left + right) / 2.0F - midWidth - SPACING - outlineWidth : (left + right) / 2.0F - midWidth - SPACING, outlinedX2 = outlines ? (left + right) / 2.0F + midWidth + SPACING - 1 + outlineWidth : (left + right) / 2.0F + midWidth + SPACING - 1;
				final float outlinedY1 = outlines ? top - yOffset - outlineWidth : top - yOffset, outlinedY2 = outlines ? top - 4.0F + outlineWidth - 1.0F : top - 4.0F - 1.0F;
				if (Reliant.instance.getRenderer().drawQuad(outlinedX1, outlinedY1, outlinedX2, outlinedY2, 0x80000000, MemoryStream.NULL)) {
					Reliant.instance.getRenderer().drawText(playerName, (int) ((left + right) / 2.0F - midWidth), (int) top - yOffset, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
					if (outlines) {
						Reliant.instance.getRenderer().drawOutline(outlinedX1, outlinedY1, outlinedX2 - outlinedX1, outlinedY2 - outlinedY1, OUTLINE_COL, outlineWidth);
					}
				}
			}
		}
		boolean drawnWeapon = false;
		int[] nameSz = null;
		if (getBoolean("Weapon")) {
			final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
			if (weaponEntity != MemoryStream.NULL) {
				final int weaponIdx = Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"));
				final String name = Item.name(weaponIdx);
				if (!StringUtils.empty(name)) {
					nameSz = Reliant.instance.getRenderer().textSize(name, font, FONT_SZ, SHADOW);
					if (nameSz != null) {
						final int midWidth = nameSz[0] / 2;
						final float outlinedX1 = outlines ? (left + right) / 2.0F - midWidth - SPACING - outlineWidth : (left + right) / 2.0F - midWidth - SPACING, outlinedX2 = outlines ? (left + right) / 2.0F + midWidth + SPACING - 1 + outlineWidth : (left + right) / 2.0F + midWidth + SPACING - 1;
						final float outlinedY1 = outlines ? bottom + 4.0F - outlineWidth : bottom + 4.0F, outlinedY2 = outlines ? bottom + 4.0F + nameSz[1] + outlineWidth - 1.0F : bottom + 4.0F + nameSz[1] - 1.0F;
						if (Reliant.instance.getRenderer().drawQuad(outlinedX1, outlinedY1, outlinedX2, outlinedY2, 0x80000000, MemoryStream.NULL)) {
							Reliant.instance.getRenderer().drawText(name, (int) ((left + right) / 2.0F - midWidth), (int) bottom + 4, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
							if (outlines) {
								Reliant.instance.getRenderer().drawOutline(outlinedX1, outlinedY1, outlinedX2 - outlinedX1, outlinedY2 - outlinedY1, OUTLINE_COL, outlineWidth);
							}
							drawnWeapon = true;
						}
					}
				}
			}
		}
		if (getBoolean("Bomb")) {
			final int playerRes = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("CSPlayerResource"));
			if (playerRes == MemoryStream.NULL) {
				throw new InvalidDataException("player_res");
			}
			if (Reliant.instance.getProcessStream().readInt(playerRes + Reliant.instance.getCustomOffsetsRegistry().get("m_iPlayerC4")) == entityIdx + 0x1) {
				final String TEXT = "Bomb Carrier";
				final int[] txtSz = Reliant.instance.getRenderer().textSize(TEXT, font, FONT_SZ, SHADOW);
				if (txtSz != null) {
					final int midWidth = txtSz[0] / 2;
					final float outlinedX1 = outlines ? (left + right) / 2.0F - midWidth - SPACING - outlineWidth : (left + right) / 2.0F - midWidth - SPACING, outlinedX2 = outlines ? (left + right) / 2.0F + midWidth + SPACING - 1 + outlineWidth : (left + right) / 2.0F + midWidth + SPACING - 1;
					final float outlinedY1 = outlines ? bottom + 4.0F + (drawnWeapon ? nameSz[1] + 4.0F : 0) - outlineWidth : bottom + 4.0F + (drawnWeapon ? nameSz[1] + 4.0F : 0), outlinedY2 = outlines ? bottom + 4.0F + (drawnWeapon ? nameSz[1] + 4.0F : 0) + outlineWidth - 1.0F + txtSz[1] : bottom + 4.0F + (drawnWeapon ? nameSz[1] + 4.0F : 0) - 1.0F + txtSz[1];
					if (Reliant.instance.getRenderer().drawQuad(outlinedX1, outlinedY1, outlinedX2, outlinedY2, 0x80000000, MemoryStream.NULL)) {
						Reliant.instance.getRenderer().drawText(TEXT, (int) ((left + right) / 2.0F - midWidth), (int) bottom + 4 + (drawnWeapon ? nameSz[1] + 4 : 0), 0xFFFFFFFF, font, FONT_SZ, SHADOW);
						if (outlines) {
							Reliant.instance.getRenderer().drawOutline(outlinedX1, outlinedY1, outlinedX2 - outlinedX1, outlinedY2 - outlinedY1, OUTLINE_COL, outlineWidth);
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		playerDamageMap.clear();
		prevHealthMap.clear();
	}
}
