package org.bitbucket.reliant.routine.impl;

import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.math.MathHelper;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class ImmunitySaverRoutine extends DisplayRoutine {
	private enum Condition {
		ALWAYS,
		ENEMIES_NEARBY,
		TEAMMATES_NEARBY,
		EITHER_NEARBY,
	}
	private enum Mode {
		BLOCK_MOVEMENT,
		BLOCK_ATTACK,
		BLOCK_BOTH
	}
	public static final int BLOCK_MOVEMENT_FLAG = 1 << 0, BLOCK_ATTACK_FLAG = 1 << 1;
	private int blockFlags;
	private int prevForward = -1, prevBackward = -1, prevRight = -1, prevLeft = -1;
	private boolean moddedAttack;

	public ImmunitySaverRoutine() {
		super("Immunity Saver", "Attempts to preserve the player's immunity state after spawning.", true, -997, new BoolOption("Visibility", "Specifies whether or not to only execute if a player is visible.", true), new FloatOption("Maximum Distance", "Specifies the maximum distance between the local player and another player.", new ClampedNumber<Float>(SdkUtils.MAX_DIST, 1.0F, (float) SdkUtils.MAX_VIS_RANGE), 100.0F), new IntOption("Condition", "Specifies the condition to execute on.", new ClampedNumber<Integer>(Condition.ENEMIES_NEARBY.ordinal(), 0, Condition.values().length - 1), 1), new IntOption("Mode", "Specifies the mode of this routine.", new ClampedNumber<Integer>(Mode.BLOCK_MOVEMENT.ordinal(), 0, Mode.values().length - 1), 1));
		// TODO Auto-generated constructor stub
	}

	private void blockAttack(final long clientAddr) {
		((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack Key")).pressKey(true, true);
		Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceAttack"), MemoryStream.FALSE);
		moddedAttack = true;
	}

	private void blockMovement(final long clientAddr) {
		prevForward = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceForward"));
		prevBackward = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceBackward"));
		prevRight = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceRight"));
		prevLeft = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceLeft"));
		((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Forward Key")).pressKey(false, true);
		((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Backward Key")).pressKey(false, true);
		((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Right Key")).pressKey(false, true);
		((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Left Key")).pressKey(false, true);
		Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceForward"), MemoryStream.FALSE);
		Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceBackward"), MemoryStream.FALSE);
		Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceRight"), MemoryStream.FALSE);
		Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceLeft"), MemoryStream.FALSE);
	}
	
	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFFFF00;
	}
	
	public int getBlockFlags() {
		return isEnabled() ? blockFlags : 0;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		if (prevForward != -1 || prevBackward != -1 || prevRight != -1 || prevLeft != -1) {
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr != MemoryStream.NULL) {
				((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Forward Key")).pressKey(true, true);
				((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Backward Key")).pressKey(true, true);
				((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Right Key")).pressKey(true, true);
				((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Left Key")).pressKey(true, true);
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceForward"), prevForward == MemoryStream.TRUE && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Forward Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceBackward"), prevBackward == MemoryStream.TRUE && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Backward Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceRight"), prevRight == MemoryStream.TRUE && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Right Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceLeft"), prevLeft == MemoryStream.TRUE && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Left Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
			}
			prevForward = prevBackward = prevRight = prevLeft = -1;
		}
		if (moddedAttack) {
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr != MemoryStream.NULL) {
				((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack Key")).pressKey(true, true);
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceAttack"), ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
			}
			moddedAttack = false;
		}
		blockFlags = 0;
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		// Eric: SdkUtils#entityAlive can cause this routine to fail.
		if (post || !SdkUtils.entityValid(GameCache.getLocalPlayer())) {
			return;
		}
		if (!SdkUtils.entityAttackable(GameCache.getLocalPlayer())) {
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr == MemoryStream.NULL) {
				throw new InvalidDataException("client_addr");
			}
			final boolean visibility = getBoolean("Visibility");
			final int cond = getInt("Condition");
			boolean hasPlayer = cond == Condition.ALWAYS.ordinal();
			if (!hasPlayer) {
				final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
				final float maxDist = getFloat("Maximum Distance");
				for (int playerIdx = 0; playerIdx <= SdkUtils.PLAYERS_SZ; playerIdx++) {
					final int player = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + playerIdx * SdkUtils.NEXT_ENTITY_SZ);
					if (player == MemoryStream.NULL || StringUtils.empty(SdkUtils.readRadarName(playerIdx)) && playerIdx == 64 || player == GameCache.getLocalPlayer() || !SdkUtils.entityAlive(player)) {
						continue;
					}
					final String playerName = SdkUtils.readRadarName(playerIdx);
					if (StringUtils.empty(playerName)) {
						continue;
					}
					final boolean protectedPlayer = nameProtect.isEnabled() && nameProtect.getBoolean("Allies") && nameProtect.containsAlias(playerName);
					final short playerTeam = Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")), localTeam = Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"));
					if (playerTeam != localTeam && visibility && !SdkUtils.entityVisible(player, false, SdkUtils.VisAggressiveness.BASIC, true, 1.0F)) {
						continue;
					}
					if ((cond == Condition.ENEMIES_NEARBY.ordinal() || cond == Condition.EITHER_NEARBY.ordinal()) && !protectedPlayer && playerTeam != localTeam || (cond == Condition.TEAMMATES_NEARBY.ordinal() || cond == Condition.EITHER_NEARBY.ordinal()) && playerTeam == localTeam) {
						SdkUtils.setLocalOrigin();
						final float[] localOrigin = SdkUtils.getLocalOrigin(), playerOrigin = new float[3];
						((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * playerOrigin.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * playerOrigin.length)).rewind()).asFloatBuffer().get(playerOrigin);
						if (MathHelper.distance(localOrigin[0], localOrigin[1], localOrigin[2], playerOrigin[0], playerOrigin[1], playerOrigin[2]) <= maxDist) {
							hasPlayer = true;
							break;
						}
					}
				}
			}
			if (hasPlayer) {
				switch (getInt("Mode")) {
				case 0:
					blockMovement(clientAddr);
					blockFlags = BLOCK_MOVEMENT_FLAG;
					break;
				case 1:
					blockAttack(clientAddr);
					blockFlags = BLOCK_ATTACK_FLAG;
					break;
				case 2:
					blockMovement(clientAddr);
					blockAttack(clientAddr);
					blockFlags = BLOCK_MOVEMENT_FLAG | BLOCK_ATTACK_FLAG;
					break;
				}
			} else {
				reset(false);
			}
		} else {
			reset(false);
		}
	}

}
