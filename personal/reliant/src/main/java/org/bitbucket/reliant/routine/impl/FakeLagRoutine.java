package org.bitbucket.reliant.routine.impl;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class FakeLagRoutine extends DisplayRoutine {
	private byte prevSendPacket = -1;
	private final Timer lagTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			/*long retVal = getInt("Time");
			if (Reliant.instance.antiOverwatch()) {
				retVal += Reliant.instance.getOverwatchRand().nextInt(Reliant.OVERWATCH_MAX_TIME);
			}*/
			return /*retVal*/getInt("Time");
		}

	};

	public FakeLagRoutine() {
		super("Fake Lag", "Simulates network lag.", true, -998, new BoolOption("Only Spotted", "Specifies whether or not to only execute if the player has been spotted by an enemy.", false), new BoolOption("All Teams", "Specifies whether or not to execute on all teams.", false), new BoolOption("Deactivate on Ground", "Specifies whether or not to deactivate this routine on ground.", false), new BoolOption("Deactivate on Attack", "Specifies whether or not to deactivate this routine on attack.", true), new BoolOption("Deactivate on Attack2", "Specifies whether or not to deactivate this routine on attack2.", true), new IntOption("Time", "Specifies how long each lag pulse is. Set to -1 to lag until this routine is disabled.", new ClampedNumber<Integer>(200, -1, 1000), 100));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFBFFF00;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		if (prevSendPacket != -1) {
			final long engineAddr = Reliant.instance.getProcessStream().moduleAddress("engine.dll");
			if (engineAddr != MemoryStream.NULL) {
				Reliant.instance.getProcessStream().write(engineAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwbSendPackets"), prevSendPacket);
			}
			prevSendPacket = -1;
		}
		lagTimer.reset();
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post) {
			return;
		}
		final long engineAddr = Reliant.instance.getProcessStream().moduleAddress("engine.dll");
		if (engineAddr == MemoryStream.NULL) {
			throw new InvalidDataException("engine_addr");
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		if (prevSendPacket == -1) {
			prevSendPacket = Reliant.instance.getProcessStream().readByte(engineAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwbSendPackets"));
			lagTimer.setStartTime();
		}
		final boolean onlySpotted = getBoolean("Only Spotted"), allTeams = getBoolean("All Teams");
		boolean spotted = false;
		if (onlySpotted && SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
			for (int playerIdx = 0; playerIdx <= SdkUtils.PLAYERS_SZ; playerIdx++) {
				final int player = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + playerIdx * SdkUtils.NEXT_ENTITY_SZ);
				if (player == MemoryStream.NULL || !SdkUtils.entityAlive(player) || player == GameCache.getLocalPlayer() || StringUtils.empty(SdkUtils.readRadarName(playerIdx)) && playerIdx == 64) {
					continue;
				}
				final String playerName = SdkUtils.readRadarName(playerIdx);
				if (StringUtils.empty(playerName)) {
					return;
				}
				if (!allTeams && Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) == Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) || nameProtect.isEnabled() && nameProtect.getBoolean("Allies") && nameProtect.containsAlias(playerName)) {
					continue;
				}
				if ((Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_bSpottedByMask")) & (1 << playerIdx)) != 0) {
					spotted = true;
					break;
				}
			}
		}
		if ((spotted || !onlySpotted) && SdkUtils.entityAlive(GameCache.getLocalPlayer()) && (!getBoolean("Deactivate on Ground") || (Reliant.instance.getProcessStream().readByte(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_fFlags")) & SdkUtils.ON_GROUND_FLAG) == 0) && (!getBoolean("Deactivate on Attack") || !SdkUtils.cmdDown(Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceAttack")))) && (!getBoolean("Deactivate on Attack2") || !SdkUtils.cmdDown(Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2")))) && (getInt("Time") == -1 || !lagTimer.delayPassed())) {
			Reliant.instance.getProcessStream().write(engineAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwbSendPackets"), (byte) MemoryStream.FALSE);
		} else if (prevSendPacket != -1) {
			Reliant.instance.getProcessStream().write(engineAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwbSendPackets"), prevSendPacket);
			lagTimer.setStartTime();
		}
	}
}
