package org.bitbucket.reliant.routine.impl;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.TextureOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.PlayerRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class RadarRoutine extends PlayerRoutine {
	private enum Mode {
		MODIFY_SPOTTED_ONLY,
		OVERLAY_ONLY,
		OVERLAY_TEAMMATES,
		BOTH,
		BOTH_TEAMMATES
	}

	public RadarRoutine() {
		super("Radar", "Shows all enemies.", true, true, true, 1008, new BoolOption("Flashed", "Specifies whether or not to execute while flashed.", true), new BoolOption("Visibility", "Specifies whether or not to only execute if a player is visible.", false), new BoolOption("Bomb", "Specifies whether or not to label the bomb carrier.", true), new BoolOption("Axes", "Specifies whether or not to display the axes.", true), new FloatOption("Line Width", "Specifies the width of a line.", new ClampedNumber<Float>(1.0F, 0.1F, 10.0F), 0.1F), new IntOption("Alpha", "Specifies the background's alpha.", new ClampedNumber<Integer>(128, 1, 255), 10), new IntOption("Red", "Specifies the background's red.", new ClampedNumber<Integer>(0, 0, 255), 10), new IntOption("Green", "Specifies the background's green.", new ClampedNumber<Integer>(0, 0, 255), 10), new IntOption("Blue", "Specifies the background's blue.", new ClampedNumber<Integer>(0, 0, 255), 10), new BoolOption("View", "Specifies whether or not to draw a player's view vector.", true), new FloatOption("Zoom", "Specifies how much to zoom in by.", new ClampedNumber<Float>(10.0F, 1.0F, 100.0F), 10.0F), new BoolOption("Trim", "Specifies whether or not to draw outlines.", true), new BoolOption("Distance", "Specifies whether or not to change color based on distance.", true), new FloatOption("Player Size", "Specifies the dimensions to use whilst drawing a player.", new ClampedNumber<Float>(10.0F, 1.0F, 100.0F), 10.0F), new TextureOption("Texture", "Specifies the image file to use as a texture.", ""), new IntOption("X-coordinate", "Specifies the x-coordinate on your screen.", new ClampedNumber<Integer>(1920 / 2 - 250 / 2, 0, 10000), 100), new IntOption("Y-coordinate", "Specifies the y-coordinate on your screen.", new ClampedNumber<Integer>(1080 - (250 + 2 + 24 + Renderer.SHADOW_OFF + 2), 0, 10000), 100), new IntOption("Radar Size", "Specifies the size of the radar.", new ClampedNumber<Integer>(250, 0, 10000), 100), new IntOption("Mode", "Specifies the condition to execute on.", new ClampedNumber<Integer>(Mode.BOTH_TEAMMATES.ordinal(), 0, Mode.values().length - 1), 1), new BoolOption("All Teams", "Specifies whether or not to highlight all teams.", false));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFF80FF00;
	}
	@Override
	public boolean handle(final int player, final int entityIdx) {
		final int mode = getInt("Mode");
		if (!super.handle(player, entityIdx) || !SdkUtils.entityAlive(player) || mode == Mode.OVERLAY_ONLY.ordinal() || mode == Mode.OVERLAY_TEAMMATES.ordinal()) {
			return false;
		}
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer()) || player == GameCache.getLocalPlayer() || !getBoolean("All Teams") && Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) == Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"))) {
			return false;
		}
		if (!getBoolean("Flashed") && Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flFlashDuration")) > 0.0F) {
			return false;
		}
		Reliant.instance.getProcessStream().write(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_bSpotted"), MemoryStream.TRUE);
		return true;
	}
	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		super.update(post);
		final int mode = getInt("Mode");
		if (!post || mode == Mode.MODIFY_SPOTTED_ONLY.ordinal()) {
			return;
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL || !SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return;
		}
		final Renderer overlay = Reliant.instance.getRenderer();
		final int radarSz = getInt("Radar Size");
		final int x1 = getInt("X-coordinate"), y1 = getInt("Y-coordinate"), x2 = x1 + radarSz, y2 = y1 + radarSz;
		final float midX = (x1 + x2) / 2.0F, midY = (y1 + y2) / 2.0F;
		final float playerSz = getFloat("Player Size");
		final float zoom = getFloat("Zoom");
		final boolean allTeams = getBoolean("All Teams"), renderDistance = getBoolean("Distance"), outlines = getBoolean("Trim"), view = getBoolean("View");
		final long texture = getTexture("Texture");
		if (texture != MemoryStream.NULL) {
			overlay.drawQuad(x1, y1, x2, y2, 0xFFFFFFFF, texture);
		}
		overlay.drawQuad(x1, y1, x2, y2, new Color(getInt("Red"), getInt("Green"), getInt("Blue"), getInt("Alpha")).getRGB(), MemoryStream.NULL);
		final float lineWidth = getFloat("Line Width");
		overlay.drawLine(midX, y1, midX, y2, 0xFF000000, lineWidth);
		overlay.drawLine(x1, midY, x2, midY, 0xFF000000, lineWidth);
		if (view) {
			final float[] viewAngles = new float[2];
			if (GameCache.getClientState() == MemoryStream.NULL) {
				throw new InvalidDataException("client_state");
			}
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * viewAngles.length).put(Reliant.instance.getProcessStream().read(GameCache.getClientState() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwViewAngles"), MemoryStream.FLOAT_SZ * viewAngles.length)).rewind()).asFloatBuffer().get(viewAngles);
			final Vector localAnglesVec = org.bitbucket.eric_generic.math.MathHelper.anglesVec(viewAngles);
			final float anglesLen = localAnglesVec.length();
			if (anglesLen != 0.0F) {
				localAnglesVec.divide(new Vector(anglesLen, anglesLen, 1.0F));
			}
			overlay.drawLine(midX, midY, midX + -localAnglesVec.x * playerSz * 2, midY + -localAnglesVec.z * playerSz * 2, 0xFFFF00FF, lineWidth);
		}
		overlay.drawQuad(midX - playerSz / 2.0F, midY - playerSz / 2.0F, midX + playerSz / 2.0F, midY + playerSz / 2.0F, 0xFFFFFFFF, MemoryStream.NULL);
		if (outlines) {
			overlay.drawOutline(midX - playerSz / 2.0F, midY - playerSz / 2.0F, midX + playerSz / 2.0F - (midX - playerSz / 2.0F), midY + playerSz / 2.0F - (midY - playerSz / 2.0F), 0xFF000000, lineWidth);
		}
		final boolean visibility = getBoolean("Visibility");
		for (int playerIdx = 0; playerIdx <= SdkUtils.PLAYERS_SZ; playerIdx++) {
			if (!getBoolean("Flashed") && Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flFlashDuration")) > 0.0F) {
				break;
			}
			final int player = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + playerIdx * SdkUtils.NEXT_ENTITY_SZ);
			if (player == MemoryStream.NULL || !SdkUtils.entityAlive(player) || player == GameCache.getLocalPlayer() || StringUtils.empty(SdkUtils.readRadarName(playerIdx)) && playerIdx == 64) {
				continue;
			}
			final short playerTeam = Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")), localTeam = Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"));
			if (!allTeams && playerTeam == localTeam && mode != Mode.OVERLAY_TEAMMATES.ordinal() && mode != Mode.BOTH_TEAMMATES.ordinal() || playerTeam != localTeam && visibility && !SdkUtils.entityVisible(player, true, SdkUtils.VisAggressiveness.BASIC, true, 1.0F)) {
				continue;
			}
			final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
			final String playerName = SdkUtils.readRadarName(playerIdx);
			if (StringUtils.empty(playerName)) {
				continue;
			}
			float[] localAbsOrigin = new float[3];
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localAbsOrigin.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getCustomOffsetsRegistry().get("m_vecAbsOrigin"), MemoryStream.FLOAT_SZ * localAbsOrigin.length)).rewind()).asFloatBuffer().get(localAbsOrigin);
			float[] playerAbsOrigin = new float[3];
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * playerAbsOrigin.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getCustomOffsetsRegistry().get("m_vecAbsOrigin"), MemoryStream.FLOAT_SZ * playerAbsOrigin.length)).rewind()).asFloatBuffer().get(playerAbsOrigin);
			final float absDist = org.bitbucket.eric_generic.math.MathHelper.distance(localAbsOrigin[0], localAbsOrigin[1], 0.0F, playerAbsOrigin[0], playerAbsOrigin[1], 0.0F);
			float distPercent = absDist / SdkUtils.MAX_DIST;
			if (distPercent > 1.0F) {
				distPercent = 1.0F;
			}
			SdkUtils.setLocalOrigin();
			final float[] localOrigin = SdkUtils.getLocalOrigin(), playerOrigin = new float[3];
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * playerOrigin.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * playerOrigin.length)).rewind()).asFloatBuffer().get(playerOrigin);
			final Vector drawVec = new Vector(localOrigin[0] - playerOrigin[0], localOrigin[1] - playerOrigin[1], 0.0F);
			final float scale = zoom / 100.0F;
			drawVec.multiply(new Vector(scale, scale, 1.0F));
			drawVec.add(new Vector(midX, midY, 0.0F));
			float alpha = 1.0F;
			if (drawVec.x < x1) {
				drawVec.x += x1 - drawVec.x;
				alpha = 0.5F;
			}
			if (drawVec.z < y1) {
				drawVec.z += y1 - drawVec.z;
				alpha = 0.5F;
			}
			if (drawVec.x + playerSz > x2) {
				drawVec.x -= drawVec.x + playerSz - x2;
				alpha = 0.5F;
			}
			if (drawVec.z + playerSz > y2) {
				drawVec.z -= drawVec.z + playerSz - y2;
				alpha = 0.5F;
			}
			if (view) {
				float[] eyeAngles = new float[2];
				((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * eyeAngles.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_angEyeAngles"), MemoryStream.FLOAT_SZ * eyeAngles.length)).rewind()).asFloatBuffer().get(eyeAngles);
				final Vector playerAnglesVec = org.bitbucket.eric_generic.math.MathHelper.anglesVec(eyeAngles);
				final float anglesLen = playerAnglesVec.length();
				if (anglesLen != 0.0F) {
					playerAnglesVec.divide(new Vector(anglesLen, anglesLen, 1.0F));
				}
				overlay.drawLine(Math.min(Math.max(drawVec.x + playerSz / 2.0F, x1), x2), Math.min(Math.max(drawVec.z + playerSz / 2.0F, y1), y2), Math.min(Math.max(drawVec.x + playerSz / 2.0F + -playerAnglesVec.x * playerSz * 2, x1), x2), Math.min(Math.max(drawVec.z + playerSz / 2.0F + -playerAnglesVec.z * playerSz * 2, y1), y2), new Color(1.0F, 0.0F, 1.0F, alpha).getRGB(), lineWidth);
			}
			final int playerRes = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("CSPlayerResource"));
			if (playerRes == MemoryStream.NULL) {
				throw new InvalidDataException("player_res");
			}
			overlay.drawQuad(drawVec.x, drawVec.z, drawVec.x + playerSz, drawVec.z + playerSz, (nameProtect.isEnabled() && nameProtect.getBoolean("Allies") && nameProtect.containsAlias(playerName) ? new Color(0.0F, 0.5F, 1.0F, alpha) : getBoolean("Bomb") && Reliant.instance.getProcessStream().readInt(playerRes + Reliant.instance.getCustomOffsetsRegistry().get("m_iPlayerC4")) == playerIdx + 0x1 ? new Color(0.0F, 1.0F, 1.0F, alpha) : playerTeam == localTeam && !allTeams ? new Color(0.0F, 0.0F, 1.0F, alpha) : renderDistance ? new Color(1.0F - distPercent, distPercent, 0.0F, alpha) : new Color(1.0F, 0.0F, 0.0F, alpha)).getRGB(), MemoryStream.NULL);
			if (outlines) {
				overlay.drawOutline(drawVec.x, drawVec.z, playerSz, playerSz, new Color(0.0F, 0.0F, 0.0F, alpha).getRGB(), lineWidth);
			}
		}
		overlay.drawOutline(x1, y1, x2 - x1, y2 - y1, 0xFF000000, lineWidth);
		if (getBoolean("Axes")) {
			final String font = Reliant.instance.getGuiFont();
			final int FONT_SZ = 24;
			final boolean SHADOW = true;
			final int SPACING = 2;
			String text = "+Z";
			int[] textSz = Reliant.instance.getRenderer().textSize(text, font, FONT_SZ, SHADOW);
			if (textSz != null) {
				overlay.drawText(text, (int) (midX - textSz[0] / 2), y1 - textSz[1] - SPACING, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
			}
			text = "-Z";
			textSz = Reliant.instance.getRenderer().textSize(text, font, FONT_SZ, SHADOW);
			if (textSz != null) {
				overlay.drawText(text, (int) (midX - textSz[0] / 2), y2 + SPACING, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
			}
			text = "+X";
			textSz = Reliant.instance.getRenderer().textSize(text, font, FONT_SZ, SHADOW);
			if (textSz != null) {
				overlay.drawText(text, x1 - textSz[0] - SPACING, (int) midY, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
			}
			text = "-X";
			textSz = Reliant.instance.getRenderer().textSize(text, font, FONT_SZ, SHADOW);
			if (textSz != null) {
				overlay.drawText(text, x2 + SPACING, (int) midY, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
			}
		}
	}
}
