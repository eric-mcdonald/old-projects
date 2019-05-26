package org.bitbucket.reliant.routine.impl;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.PlayerRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class TracersRoutine extends PlayerRoutine {
	public TracersRoutine() {
		super("Tracers", "Draws a line to each player.", true, 1009, new BoolOption("Flashed", "Specifies whether or not to execute while flashed.", true), new BoolOption("Visibility", "Specifies whether or not to only execute if a player is visible.", false), new FloatOption("Line Width", "Specifies the width of a line.", new ClampedNumber<Float>(1.0F, 0.1F, 10.0F), 0.1F), new IntOption("Mode", "Specifies the condition to draw on.", new ClampedNumber<Integer>(0, 0, 2), 1), new BoolOption("Ray Trace", "Specifies whether or not to ray trace for visibility.", false), new BoolOption("Bottom", "Specifies whether or not to place the first vertex of a line at the bottom of the screen, rather than in the center.", false), new BoolOption("All Teams", "Specifies whether or not to highlight all teams as enemies.", false), new BoolOption("Fade", "Specifies whether or not to make the line fade out as you move closer to a highlighted player.", true), new BoolOption("Distance", "Specifies whether or not to change color based on distance.", true), new FloatOption("Ray Trace Step", "Specifies the perferred value to step by whilst tracing a ray.", new ClampedNumber<Float>(1.0F, 1.0F, 100.0F), 10.0F));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFF00FF00;
	}

	@Override
	public boolean handle(final int player, final int entityIdx) {
		if (!super.handle(player, entityIdx) || !Reliant.instance.getRenderer().shouldDraw() || !SdkUtils.entityAlive(player)) {
			return false;
		}
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer()) || player == GameCache.getLocalPlayer()) {
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
		float[] playerAbsOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * playerAbsOrigin.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getCustomOffsetsRegistry().get("m_vecAbsOrigin"), MemoryStream.FLOAT_SZ * playerAbsOrigin.length)).rewind()).asFloatBuffer().get(playerAbsOrigin);
		float[] screenPos = Reliant.instance.getRenderer().screenPos(playerAbsOrigin);
		final boolean bottom = getBoolean("Bottom");
		if (screenPos == null || screenPos[3] < org.bitbucket.eric_generic.math.MathHelper.EQUALS_PRECISION && bottom) {
			return false;
		}
		float[] localAbsOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localAbsOrigin.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getCustomOffsetsRegistry().get("m_vecAbsOrigin"), MemoryStream.FLOAT_SZ * localAbsOrigin.length)).rewind()).asFloatBuffer().get(localAbsOrigin);
		final float absDist = org.bitbucket.eric_generic.math.MathHelper.distance(localAbsOrigin[0], localAbsOrigin[1], localAbsOrigin[2], playerAbsOrigin[0], playerAbsOrigin[1], playerAbsOrigin[2]);
		float distPercent = absDist / SdkUtils.MAX_DIST;
		if (distPercent > 1.0F) {
			distPercent = 1.0F;
		}
		float alpha = !getBoolean("Fade") || screenPos[3] < org.bitbucket.eric_generic.math.MathHelper.EQUALS_PRECISION || getBoolean("Ray Trace") && !SdkUtils.entityVisTrace(player, true, getFloat("Ray Trace Step")) || !SdkUtils.entityVisible(player, true, SdkUtils.VisAggressiveness.BASIC, true, getFloat("Ray Trace Step")) ? 1.0F : absDist / (SdkUtils.MAX_DIST / 3.0F);
		if (alpha > 1.0F) {
			alpha = 1.0F;
		}
		final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
		final String playerName = SdkUtils.readRadarName(entityIdx);
		if (StringUtils.empty(playerName)) {
			return false;
		}
		final int color = (nameProtect.isEnabled() && nameProtect.getBoolean("Allies") && nameProtect.containsAlias(playerName) ? new Color(0.0F, 0.5F, 1.0F, alpha) : playerTeam == localTeam && !getBoolean("All Teams") ? new Color(0.0F, 0.0F, 1.0F, alpha) : getBoolean("Distance") ? new Color(1.0F - distPercent, distPercent, 0.0F, alpha) : new Color(1.0F, 0.0F, 0.0F, alpha)).getRGB();
		final int[] resolution = Reliant.instance.getRenderer().resolution();
		if (resolution == null) {
			throw new InvalidDataException("resolution");
		}
		final float borderX, borderY;
		if (Reliant.instance.getRenderer().hasBorder(Main.TARGET_WINDOW_CLASS)) {
			borderX = Renderer.BORDER_WIDTH;
			borderY = Renderer.BORDER_HEIGHT;
		} else {
			borderX = borderY = 0.0F;
		}
		final float centerX = resolution[0] / 2.0F + borderX / 2.0F, y1 = bottom ? resolution[1] : resolution[1] / 2.0F + borderY;
		final float lineWidth = getFloat("Line Width");
		Reliant.instance.getRenderer().drawLine(centerX, y1, screenPos[0], screenPos[1], color, lineWidth);
		final GlowRoutine glow = (GlowRoutine) Reliant.instance.getRoutineRegistry().get("Glow");
		final int glowMode = glow.getInt("Mode");
		final EspRoutine esp = (EspRoutine) Reliant.instance.getRoutineRegistry().get("ESP");
		final int espMode = esp.getInt("Mode");
		if ((!glow.isEnabled() || (glowMode == 1 && playerTeam == localTeam || glowMode == 2 && playerTeam != localTeam)) && (!esp.isEnabled() || (espMode == 1 && playerTeam == localTeam || espMode == 2 && playerTeam != localTeam)) && screenPos[3] >= org.bitbucket.eric_generic.math.MathHelper.EQUALS_PRECISION) {
			final float[] prevScreenPos = screenPos;
			final float[] playerBone = SdkUtils.entityBone(player, SdkUtils.headBoneId()); // Eric: We use the entity's head bone instead of its vecViewOffset because vecViewOffset is sometimes not in sync
			if (playerBone == null) {
				throw new InvalidDataException("player_bone");
			}
			playerAbsOrigin[2] = playerBone[2];
			screenPos = Reliant.instance.getRenderer().screenPos(playerAbsOrigin);
			if (screenPos == null || screenPos[3] < org.bitbucket.eric_generic.math.MathHelper.EQUALS_PRECISION && bottom) {
				return false;
			}
			Reliant.instance.getRenderer().drawLine(prevScreenPos[0], prevScreenPos[1], prevScreenPos[0], screenPos[1], color, lineWidth);
		}
		return true;
	}
}
