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

public final class AutoduckRoutine extends DisplayRoutine {
	private enum Mode {
		ALWAYS, // Eric: while toggled/key pressed
		ATTACK, // Eric: execute on attack
		STANDING, // Eric: not moving horizontally; could be jumping
		IDLE; // Eric: not moving horizontally; no jumping
	}
	private static final Mode[] modes = Mode.values();
	
	private int prevDuck = -1;
	
	private final Timer duckTimer = new Timer() {

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
	
	public AutoduckRoutine() {
		super("Auto-duck", "Makes the player duck while they are attacking.", true, 2, new BoolOption("Deactivate on Reload", "Specifies whether or not to deactivate this routine if the player is reloading.", true), new IntOption("Mode", "Specifies the condition to duck on.", new ClampedNumber<Integer>(Mode.ATTACK.ordinal(), 0, modes.length - 1), 1), new IntOption("Delay", "Specifies the minimum delay to wait before resetting.", new ClampedNumber<Integer>(SdkUtils.TICK_TIME * 8, 0, 10000), 1000));
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
		if (prevDuck != -1) {
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr != MemoryStream.NULL) {
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getCustomOffsetsRegistry().get("m_dwForceDuck"), prevDuck == MemoryStream.TRUE && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Duck Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
			}
			prevDuck = -1;
		}
		duckTimer.reset();
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post) {
			return;
		}
		final Mode mode = modes[getInt("Mode")];
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			reset(false);
			return;
		}
		final boolean forward = SdkUtils.cmdDown(Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceForward"))), backward = SdkUtils.cmdDown(Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceBackward"))), right = SdkUtils.cmdDown(Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceRight"))), left = SdkUtils.cmdDown(Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceLeft")));
		final boolean inWalk = (forward || backward || right|| left) && (!forward || !backward || !right || !left); // Eric: Fixes a conflict between this routine and Auto-stop
		final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
		if (weaponEntity == MemoryStream.NULL) {
			throw new InvalidDataException("weapon_entity");
		}
		final int weaponIdx = Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"));
		if ((mode.equals(Mode.ALWAYS) || (!mode.equals(Mode.ATTACK) || !((AutocockRoutine) Reliant.instance.getRoutineRegistry().get("Auto-cock")).isExecuted() && SdkUtils.cmdDown(Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceAttack")))) && (!mode.equals(Mode.STANDING) || !inWalk) && (!mode.equals(Mode.IDLE) || !inWalk && !SdkUtils.cmdDown(Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceJump"))))) && (!Item.melee(weaponIdx) || weaponIdx == Item.ZEUS_X27.index) && !Item.grenade(weaponIdx) && Reliant.instance.getProcessStream().readShort(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iClip1")) > 0 && (!getBoolean("Deactivate on Reload") || !GameCache.isReloading())) {
			prevDuck = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getCustomOffsetsRegistry().get("m_dwForceDuck"));
			Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getCustomOffsetsRegistry().get("m_dwForceDuck"), MemoryStream.TRUE);
			duckTimer.setStartTime();
		} else if (duckTimer.delayPassed()) {
			reset(false);
		} else {
			Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getCustomOffsetsRegistry().get("m_dwForceDuck"), MemoryStream.TRUE);
		}
	}
}