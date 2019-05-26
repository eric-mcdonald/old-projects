package org.bitbucket.reliant.routine.impl;

import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.memory.WriteMemoryException;
import org.bitbucket.reliant.routine.PlayerRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class GlowRoutine extends PlayerRoutine {
	public GlowRoutine() {
		super("Glow", "Highlights players by drawing the glow effect on them.", true, 1005, new BoolOption("Visibility", "Specifies whether or not to only execute if a player is visible.", false), new IntOption("Mode", "Specifies the condition to draw on.", new ClampedNumber<Integer>(0, 0, 2), 1), new BoolOption("Ray Trace", "Specifies whether or not to ray trace for visibility.", false), new BoolOption("Fade", "Specifies whether or not to make the glow effect fade out as you move closer to a highlighted player.", true), new BoolOption("All Teams", "Specifies whether or not to highlight all teams as enemies.", false), new BoolOption("Unoccluded", "Specifies whether or not to render the glow effect as unoccluded.", false), new BoolOption("Vulnerability", "Specifies whether or not to change color based on how vulnerable the player is.", true), new FloatOption("Ray Trace Step", "Specifies the perferred value to step by whilst tracing a ray.", new ClampedNumber<Float>(1.0F, 1.0F, 100.0F), 10.0F));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFF80FF00;
	}
	@Override
	public boolean handle(final int player, final int entityIdx) {
		if (!super.handle(player, entityIdx) || !SdkUtils.entityValid(player)) {
			return false;
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		final int glowObj = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwGlowObject"));
		if (glowObj == MemoryStream.NULL) {
			throw new InvalidDataException("glow_obj");
		}
		final int localPlayer = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwLocalPlayer"));
		if (!SdkUtils.entityAlive(localPlayer)) {
			return false;
		}
		if (player == localPlayer) {
			return false;
		}
		final int mode = getInt("Mode");
		final short playerTeam = Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")), localTeam = Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"));
		if (mode == 1 && playerTeam == localTeam || mode == 2 && playerTeam != localTeam || playerTeam != localTeam && getBoolean("Visibility") && !SdkUtils.entityVisible(player, true, SdkUtils.VisAggressiveness.BASIC, true, 1.0F)) {
			return false;
		}
		final int glowAddr = glowObj + Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iGlowIndex")) * 0x38;
		int glowEntity = Reliant.instance.getProcessStream().readInt(glowAddr);
		if (glowEntity == MemoryStream.NULL) {
			if (!Reliant.instance.getProcessStream().write(glowAddr, player)) {
				throw new WriteMemoryException("glow_entity", player, glowAddr);
			}
			glowEntity = player;
		}
		if (glowEntity != player) {
			return false;
		}
		final float[] colors = new float[4];
		final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
		final String playerName = SdkUtils.readRadarName(entityIdx);
		if (StringUtils.empty(playerName)) {
			return false;
		}
		final float rayTraceStep = getFloat("Ray Trace Step");
		final boolean visible = getBoolean("Ray Trace") && SdkUtils.entityVisTrace(glowEntity, true, rayTraceStep);
		if (nameProtect.isEnabled() && nameProtect.getBoolean("Allies") && nameProtect.containsAlias(playerName)) {
			colors[0] = 0.0F;
			colors[1] = 0.5F;
			colors[2] = 1.0F;
		} else if (!getBoolean("All Teams") && playerTeam == localTeam) {
			colors[0] = 0.0F;
			colors[1] = 0.0F;
			colors[2] = 1.0F;
		} else if (getBoolean("Vulnerability")) {
			if (Reliant.instance.getProcessStream().readFloat(glowEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flFlashDuration")) > 0.0F) {
				colors[0] = 1.0F;
				colors[1] = 0.0F;
				colors[2] = 1.0F;
			} else if (visible || SdkUtils.entityVisible(glowEntity, true, SdkUtils.VisAggressiveness.BASIC, true, rayTraceStep)) {
				colors[0] = 0.0F;
				colors[1] = 1.0F;
				colors[2] = 1.0F;
			} else {
				final float healthPercent = Reliant.instance.getProcessStream().readShort(glowEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iHealth")) / 100.0F;
				colors[0] = 1.0F - healthPercent;
				colors[1] = healthPercent;
				colors[2] = 0.0F;
			}
		} else {
			colors[0] = 1.0F;
			colors[1] = 0.0F;
			colors[2] = 0.0F;
		}
		final float[] playerAbsOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * playerAbsOrigin.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getCustomOffsetsRegistry().get("m_vecAbsOrigin"), MemoryStream.FLOAT_SZ * playerAbsOrigin.length)).rewind()).asFloatBuffer().get(playerAbsOrigin);
		final float[] localAbsOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localAbsOrigin.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getCustomOffsetsRegistry().get("m_vecAbsOrigin"), MemoryStream.FLOAT_SZ * localAbsOrigin.length)).rewind()).asFloatBuffer().get(localAbsOrigin);
		float alpha = !getBoolean("Fade") || !(visible || SdkUtils.entityVisible(glowEntity, true, SdkUtils.VisAggressiveness.BASIC, true, rayTraceStep)) ? 1.0F : org.bitbucket.eric_generic.math.MathHelper.distance(localAbsOrigin[0], localAbsOrigin[1], localAbsOrigin[2], playerAbsOrigin[0], playerAbsOrigin[1], playerAbsOrigin[2]) / (SdkUtils.MAX_DIST / 3.0F);
		if (alpha > 1.0F) {
			alpha = 1.0F;
		}
		colors[3] = alpha;
		if (!Reliant.instance.getProcessStream().write(glowAddr + MemoryStream.INT_SZ, NioUtils.createBuffer(MemoryStream.FLOAT_SZ * colors.length).putFloat(colors[0]).putFloat(colors[1]).putFloat(colors[2]).putFloat(colors[3]).array()) || !Reliant.instance.isLegacyToolchain() && !Reliant.instance.getProcessStream().write(glowAddr + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * colors.length + 0x8, colors[3])) {
			throw new WriteMemoryException("glow_colors", colors, glowAddr + MemoryStream.INT_SZ);
		}
		if (!Reliant.instance.getProcessStream().write(glowAddr + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * colors.length + 16, (short) (SdkUtils.entityAlive(glowEntity) ? MemoryStream.TRUE : MemoryStream.FALSE))) {
			throw new WriteMemoryException("glow_bool0", SdkUtils.entityAlive(glowEntity) ? MemoryStream.TRUE : MemoryStream.FALSE, glowAddr + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * colors.length + 16);
		}
		Reliant.instance.getProcessStream().write(glowAddr + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * colors.length + 16 + MemoryStream.SHORT_SZ, (short) (getBoolean("Unoccluded") ? MemoryStream.TRUE : MemoryStream.FALSE));
		return true;
	}
}
