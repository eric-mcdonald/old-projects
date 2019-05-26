package org.bitbucket.reliant.routine.impl;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.log.Logger;
import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.CheatException;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.util.Player;
import org.bitbucket.reliant.util.SdkUtils;
import org.bitbucket.reliant.util.math.MathHelper;

public final class CalloutsRoutine extends DisplayRoutine {
	private enum Mode {
		RELATIVE,
		ABSOLUTE,
		PROXIMITY,
	}
	
	public CalloutsRoutine() {
		super("Callouts", "Tells information about a player.", true, 999, new BoolOption("Names", "Specifies whether or not to provide the name of an enemy player.", true), new BoolOption("All Teams", "Specifies whether or not to execute on all teams.", false), new IntOption("Mode", "Specifies the mode of this routine.", new ClampedNumber<Integer>(Mode.PROXIMITY.ordinal(), 0, Mode.values().length - 1), 1), new BoolOption("Timestamps", "Specifies whether or not to provide a timestamp.", false));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFF0000;
	}
	@Override
	protected void onEnabled() {
		try {
			super.onEnabled();
		} catch (final CheatException cheatEx) {
			final String message = "Failed to execute CalloutsRoutine#onEnabled: " + cheatEx;
			Reliant.instance.clientCmdRate.execute(message, 0);
			Reliant.instance.getLogger().log(message, Logger.Type.ERROR);
			Reliant.instance.getLogger().logError(cheatEx);
		}
	}
	@Override
	public void reset(final boolean shutdown) {
		try {
			super.reset(shutdown);
		} catch (final CheatException cheatEx) {
			final String message = "Failed to execute CalloutsRoutine#reset: " + cheatEx;
			Reliant.instance.clientCmdRate.execute(message, 0);
			Reliant.instance.getLogger().log(message, Logger.Type.ERROR);
			Reliant.instance.getLogger().logError(cheatEx);
		}
	}
	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		try {
			if (post) {
				return;
			}
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr == MemoryStream.NULL) {
				throw new InvalidDataException("client_addr");
			}
			final boolean names = getBoolean("Names"), timestamps = getBoolean("Timestamps"), allTeams = getBoolean("All Teams");
			final int mode = getInt("Mode");
			if (GameCache.getLocalPlayer() == MemoryStream.NULL || StringUtils.empty(SdkUtils.readRadarName(GameCache.getLocalPlayer())) && GameCache.getLocalPlayer() == 64) {
				return;
			}
			int observedEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_hObserverTarget")));
			if (!SdkUtils.entityAlive(observedEntity)) {
				return;
			}
			final String observedName = SdkUtils.readRadarName(SdkUtils.playerIdx(observedEntity));
			if (StringUtils.empty(observedName)) {
				return;
			}
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
			final float centerX = resolution[0] / 2.0F + borderX / 2.0F, centerY = resolution[1] / 2.0F + borderY;
			String clientCmd = allTeams ? "say " : "say_team ";
			for (final Player playerObj : SdkUtils.sortedPlayers(observedEntity, allTeams, false, false)) {
				final int player = playerObj.entity;
				final String playerName = SdkUtils.readRadarName(playerObj.entityIdx);
				if (player == MemoryStream.NULL || !SdkUtils.entityValid(player) || observedEntity == player || StringUtils.empty(playerName) && playerObj.entityIdx == 64 || StringUtils.empty(playerName)) {
					continue;
				}
				if (!allTeams && Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) == Reliant.instance.getProcessStream().readShort(observedEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"))) {
					continue;
				}
				clientCmd += (names ? playerName : "Enemy") + "@" + observedName + "=";
				if (mode == Mode.ABSOLUTE.ordinal()) {
					final float[] angleDiff = MathHelper.angleDiff(player, observedEntity);
					if (angleDiff == null) {
						throw new InvalidDataException("angle_diff");
					}
					clientCmd += String.valueOf(Math.round(angleDiff[1])) + "deg, " + String.valueOf(Math.round(angleDiff[0])) + "deg";
				} else if (mode == Mode.RELATIVE.ordinal()) {
					final float[] entityBone = SdkUtils.entityBone(playerObj.entity, SdkUtils.headBoneId());
					if (entityBone == null) {
						throw new InvalidDataException("entity_bone");
					}
					final Vector boneVec = Vector.wrap(entityBone);
					final float[] screenPos = Reliant.instance.getRenderer().screenPos(boneVec.data());
					if (screenPos == null) {
						throw new InvalidDataException("screen_pos");
					}
					clientCmd += String.valueOf(Math.round(Math.abs(screenPos[0] - centerX))) + "px " + (screenPos[0] - centerX < 0 ? "<" : ">") + ", " + String.valueOf(Math.round(Math.abs(screenPos[1] - centerY))) + "px " + (screenPos[1] - centerY < 0 ? "^" : "v") + " (" + Math.round(resolution[0]) + " x " + Math.round(resolution[1]) + ")";
				} else if (mode == Mode.PROXIMITY.ordinal()) {
					final float[] playerOrigin = new float[3];
					((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * playerOrigin.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * playerOrigin.length)).rewind()).asFloatBuffer().get(playerOrigin);
					final float[] observedOrigin = new float[3];
					((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * observedOrigin.length).put(Reliant.instance.getProcessStream().read(observedEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * observedOrigin.length)).rewind()).asFloatBuffer().get(observedOrigin);
					clientCmd += String.valueOf(Math.round(org.bitbucket.eric_generic.math.MathHelper.distance(observedOrigin[0], observedOrigin[1], observedOrigin[2], playerOrigin[0], playerOrigin[1], playerOrigin[2]) * MathHelper.UNITS_TO_METERS)) + "m";
				}
				if (timestamps) {
					clientCmd += " " + new SimpleDateFormat("mm:ss").format(new Date());
				}
				break;
			}
			Reliant.instance.clientCmdRate.execute(clientCmd, 0);
		} catch (final CheatException cheatEx) {
			final String message = "Failed to execute CalloutsRoutine#handle: " + cheatEx;
			Reliant.instance.clientCmdRate.execute("say_team " + message, 0);
			Reliant.instance.getLogger().log(message, Logger.Type.ERROR);
			Reliant.instance.getLogger().logError(cheatEx);
		}
	}
}
