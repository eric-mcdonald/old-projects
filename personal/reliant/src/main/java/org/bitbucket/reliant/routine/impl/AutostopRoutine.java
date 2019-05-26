package org.bitbucket.reliant.routine.impl;

import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.SdkUtils;

public final class AutostopRoutine extends DisplayRoutine {
	private static void stopMovement(final long clientAddr) {
		if ((((ImmunitySaverRoutine) Reliant.instance.getRoutineRegistry().get("Immunity Saver")).getBlockFlags() & ImmunitySaverRoutine.BLOCK_MOVEMENT_FLAG) != 0) {
			return;
		}
		Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceForward"), MemoryStream.TRUE);
		Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceBackward"), MemoryStream.TRUE);
		Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceRight"), MemoryStream.TRUE);
		Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceLeft"), MemoryStream.TRUE);
	}
	private int prevForward = -1, prevBackward = -1, prevRight = -1, prevLeft = -1;
	
	private final Timer stopTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			/*long retVal = getInt("Delay");
			if (Reliant.instance.antiOverwatch()) {
				retVal += Reliant.instance.getOverwatchRand().nextInt(Reliant.OVERWATCH_MAX_TIME);
			}*/
			return /*retVal*/getInt("Delay");
		}
		
	};

	public AutostopRoutine() {
		super("Auto-stop", "Stops the player from moving while they are attacking.", true, 3, new BoolOption("Deactivate on Reload", "Specifies whether or not to deactivate this routine if the player is reloading.", true), new IntOption("Delay", "Specifies the minimum delay to wait before resetting.", new ClampedNumber<Integer>(SdkUtils.TICK_TIME * 8, 0, 10000), 1000));
		// TODO Auto-generated constructor stub
	}
	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFF8000;
	}
	
	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		if (prevForward != -1 || prevBackward != -1 || prevRight != -1 || prevLeft != -1) {
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr != MemoryStream.NULL && (((ImmunitySaverRoutine) Reliant.instance.getRoutineRegistry().get("Immunity Saver")).getBlockFlags() & ImmunitySaverRoutine.BLOCK_MOVEMENT_FLAG) == 0) {
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceForward"), prevForward == MemoryStream.TRUE && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Forward Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceBackward"), prevBackward == MemoryStream.TRUE && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Backward Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceRight"), prevRight == MemoryStream.TRUE && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Right Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceLeft"), prevLeft == MemoryStream.TRUE && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Left Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
			}
			prevForward = prevBackward = prevRight = prevLeft = -1;
		}
		stopTimer.reset();
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post) {
			return;
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			reset(false);
			return;
		}
		final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
		if (weaponEntity == MemoryStream.NULL) {
			throw new InvalidDataException("weapon_entity");
		}
		final int weaponIdx = Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"));
		if (!((AutocockRoutine) Reliant.instance.getRoutineRegistry().get("Auto-cock")).isExecuted() && SdkUtils.cmdDown(Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceAttack"))) && (!Item.melee(weaponIdx) || weaponIdx == Item.ZEUS_X27.index) && !Item.grenade(weaponIdx) && Reliant.instance.getProcessStream().readShort(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iClip1")) > 0 && (((ImmunitySaverRoutine) Reliant.instance.getRoutineRegistry().get("Immunity Saver")).getBlockFlags() & ImmunitySaverRoutine.BLOCK_MOVEMENT_FLAG) == 0 && (!getBoolean("Deactivate on Reload") || !GameCache.isReloading())) {
			prevForward = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceForward"));
			prevBackward = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceBackward"));
			prevRight = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceRight"));
			prevLeft = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceLeft"));
			stopMovement(clientAddr);
			stopTimer.setStartTime();
		} else if (stopTimer.delayPassed()) {
			reset(false);
		} else {
			stopMovement(clientAddr);
		}
	}
}
