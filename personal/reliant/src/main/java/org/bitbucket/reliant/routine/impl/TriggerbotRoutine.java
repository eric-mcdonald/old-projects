package org.bitbucket.reliant.routine.impl;

import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.lang.time.BasicTimer;
import org.bitbucket.eric_generic.lang.time.Timer;
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
import org.bitbucket.reliant.routine.CrosshairRoutine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.SdkUtils;

public final class TriggerbotRoutine extends CrosshairRoutine {
	private final Timer attackTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			if (!SdkUtils.entityValid(GameCache.getLocalPlayer())) {
				return 0;
			}
			final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
			if (weaponEntity == MemoryStream.NULL) {
				throw new InvalidDataException("weapon_entity");
			}
			long delay = Item.minDelay(Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex")));
			final int cfgDelay = getInt("Attack Delay");
			if (delay < cfgDelay) {
				delay = cfgDelay;
			}
			//if (Reliant.instance.antiOverwatch()) {
				//delay += Reliant.instance.getOverwatchRand().nextInt(Reliant.OVERWATCH_MAX_TIME);
			//}
			return delay;
		}

	}, semiAutoSniperTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			/*long retVal = getInt("Sniper Delay");
			if (Reliant.instance.antiOverwatch()) {
				retVal += Reliant.instance.getOverwatchRand().nextInt(Reliant.OVERWATCH_MAX_TIME);
			}*/
			return /*retVal*/getInt("Semi-automatic Sniper Delay");
		}

	}, autoSniperTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			/*long retVal = getInt("Sniper Delay");
			if (Reliant.instance.antiOverwatch()) {
				retVal += Reliant.instance.getOverwatchRand().nextInt(Reliant.OVERWATCH_MAX_TIME);
			}*/
			return /*retVal*/getInt("Automatic Sniper Delay");
		}

	}, deactivateTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			return getInt("Deactivate Delay");
		}
		
	}, activateTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			return getInt("Activate Delay");
		}
		
	};
	private final Timer crosshairTimer = new BasicTimer(SdkUtils.TICK_TIME * 2), weaponSwitchTimer = new BasicTimer(SdkUtils.TICK_TIME * 1);
	private boolean executed, executedPower;
	private short prevZoom;
	private String prevCrosshairName;
	private int prevWeaponIdx = -1;
	private float prevAccuracyPenalty = -999.0F;
	private boolean prevDucking;
	private int prevCrossEntity = MemoryStream.NULL;

	public TriggerbotRoutine() {
		super("Triggerbot", "Automatically attacks enemies that are in your crosshair.", true, 6, new BoolOption("Flashed", "Specifies whether or not to execute while flashed.", true), new IntOption("Automatic Sniper Delay", "Specifies the minimum delay to wait before attacking while the player's automatic sniper is zoomed again.", new ClampedNumber<Integer>(0, 0, 1000), 100), new IntOption("Activate Delay", "Specifies the delay to wait for before activating.", new ClampedNumber<Integer>(0, 0, 1000), 100), new IntOption("Deactivate Delay", "Specifies the delay to wait for before deactivating.", new ClampedNumber<Integer>(0, 0, 1000), 100), new BoolOption("Grenades", "Specifies whether or not grenades should be thrown at an enemy.", false), new BoolOption("Power Attack", "Specifies whether or not to make the player power attack with applicable weapons.", true), new BoolOption("Local on Ground", "Specifies whether or not to require the player to be on the ground.", false), new BoolOption("Target on Ground", "Specifies whether or not to require the target to be on the ground.", false), new BoolOption("Next Attack", "Specifies whether or not to wait until the player can attack again.", false), new BoolOption("All Teams", "Specifies whether or not to attack all teams.", false), new BoolOption("Only Zoomed", "Specifies whether or not to only shoot when the player's weapon is zoomed.", true), new FloatOption("Melee Distance", "Specifies the maximum distance between your player and another while holding a melee weapon.", new ClampedNumber<Float>(100.0F, 0.0F, SdkUtils.MAX_DIST), 100.0F), new FloatOption("Shotgun Distance", "Specifies the maximum distance between your player and another while holding a shotgun.", new ClampedNumber<Float>(1000.0F, 0.0F, SdkUtils.MAX_DIST), 100.0F), new FloatOption("Automatic Accuracy Distance", "Specifies the threshold distance between your player and another before compensating for your automatic weapon's accuracy penalty. Set to -1 for no automatic weapon accuracy compensation.", new ClampedNumber<Float>(2000.0F, -1.0F, SdkUtils.MAX_DIST), 100.0F), new FloatOption("Semi-automatic Accuracy Distance", "Specifies the threshold distance between your player and another before compensating for your semi-automatic weapon's accuracy penalty. Set to -1 for no semi-automatic weapon accuracy compensation.", new ClampedNumber<Float>(2000.0F, -1.0F, SdkUtils.MAX_DIST), 100.0F), new IntOption("Attack Delay", "Specifies the minimum delay to wait before attacking again.", new ClampedNumber<Integer>(0, 0, 1000), 100), new IntOption("Semi-automatic Sniper Delay", "Specifies the minimum delay to wait before attacking while the player's semi-automatic sniper is zoomed again.", new ClampedNumber<Integer>(SdkUtils.TICK_TIME * 12, 0, 1000), 100));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFF0000;
	}
	
	boolean executed() {
		return isEnabled() && (executed || executedPower);
	}
	
	int getPrevCrossEntity() {
		return prevCrossEntity;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr != MemoryStream.NULL) {
			final boolean canAttack = (((ImmunitySaverRoutine) Reliant.instance.getRoutineRegistry().get("Immunity Saver")).getBlockFlags() & ImmunitySaverRoutine.BLOCK_ATTACK_FLAG) == 0;
			if (executed && canAttack && !((AutocockRoutine) Reliant.instance.getRoutineRegistry().get("Auto-cock")).isExecuted()) {
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceAttack"), ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
			}
			if (executedPower && canAttack) {
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2"), ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack 2 Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
			}
		}
		executed = executedPower = false;
		attackTimer.reset();
		activateTimer.reset();
		deactivateTimer.reset();
		semiAutoSniperTimer.reset();
		autoSniperTimer.reset();
		prevZoom = 0;
		crosshairTimer.reset();
		prevCrosshairName = null;
		prevWeaponIdx = -1;
		prevAccuracyPenalty = -999.0F;
		prevDucking = false;
		weaponSwitchTimer.reset();
		prevCrossEntity = MemoryStream.NULL;
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
			reset(false); // Eric: Fixes an issue in which the player is still attacking whilst dead.
			return;
		}
		final int crosshairEntity = getCrosshairEntity();
		prevCrossEntity = crosshairEntity;
		final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
		if (weaponEntity == MemoryStream.NULL) {
			throw new InvalidDataException("weapon_entity");
		}
		final int weaponIdx = Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"));
		final boolean melee = Item.melee(weaponIdx);
		SdkUtils.setLocalOrigin();
		final float[] localOrigin = SdkUtils.getLocalOrigin(), crosshairOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * crosshairOrigin.length).put(Reliant.instance.getProcessStream().read(crosshairEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * crosshairOrigin.length)).rewind()).asFloatBuffer().get(crosshairOrigin);
		final boolean sniper = Item.sniper(weaponIdx);
		final short zoomLevel = Reliant.instance.getProcessStream().readShort(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_zoomLevel"));
		final long modAddr = Reliant.instance.isLegacyToolchain() ? clientAddr : Reliant.instance.getProcessStream().moduleAddress("engine.dll");
		if (modAddr == MemoryStream.NULL) {
			throw new InvalidDataException("mod_addr");
		}
		final long globalVars = Reliant.instance.isLegacyToolchain() ? Reliant.instance.getProcessStream().readInt(modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars")) : modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars");
		if (globalVars == MemoryStream.NULL) {
			throw new InvalidDataException("global_vars");
		}
		final boolean zoomChanged = zoomLevel != prevZoom;
		final boolean canAttack = Reliant.instance.getProcessStream().readFloat(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flNextPrimaryAttack")) - Reliant.instance.getProcessStream().readFloat(globalVars + MemoryStream.FLOAT_SZ + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * 2) <= 0.0F;
		final boolean automatic = Item.automatic(weaponIdx);
		if (sniper && (zoomLevel > 0 && zoomChanged || Item.boltAction(weaponIdx) && zoomLevel > 0 && !canAttack)) {
			if (automatic) {
				autoSniperTimer.setStartTime();
			} else {
				semiAutoSniperTimer.setStartTime();
			}
		}
		prevZoom = sniper ? zoomLevel : 0;
		final boolean idxMatchesPrev = prevWeaponIdx == -1 || weaponIdx == prevWeaponIdx;
		if (idxMatchesPrev) {
			weaponSwitchTimer.setStartTime(); // Eric: Experimental hotfix for CS:GO not seeing that the bind was let go
		}
		if (prevWeaponIdx == -1 || weaponSwitchTimer.delayPassed()) {
			prevWeaponIdx = weaponIdx;
		}
		final byte flags = Reliant.instance.getProcessStream().readByte(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_fFlags"));
		final boolean localAlive = SdkUtils.entityAlive(GameCache.getLocalPlayer()), crosshairValid = SdkUtils.entityValid(crosshairEntity) && (getBoolean("All Teams") || Reliant.instance.getProcessStream().readShort(crosshairEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) != Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"))) && SdkUtils.entityAlive(crosshairEntity) && (!getBoolean("Local on Ground") || (flags & SdkUtils.ON_GROUND_FLAG) != 0 || melee && weaponIdx != Item.ZEUS_X27.index) && (!getBoolean("Target on Ground") || (Reliant.instance.getProcessStream().readByte(crosshairEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_fFlags")) & SdkUtils.ON_GROUND_FLAG) != 0 || melee && weaponIdx != Item.ZEUS_X27.index) && SdkUtils.entityAttackable(crosshairEntity) && localAlive;
		final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
		final String playerName = crosshairValid ? GameCache.getCrosshairName() : prevCrosshairName;
		final boolean emptyName = StringUtils.empty(playerName);
		if (crosshairValid && !emptyName) {
			prevCrosshairName = playerName;
			crosshairTimer.setStartTime();
		}
		final float crosshairDist = org.bitbucket.eric_generic.math.MathHelper.distance(localOrigin[0], localOrigin[1], localOrigin[2], crosshairOrigin[0], crosshairOrigin[1], crosshairOrigin[2]);
		final float accuracyCompDist = automatic ? getFloat("Automatic Accuracy Distance") : getFloat("Semi-automatic Accuracy Distance");
		final float accuracyPenalty = Reliant.instance.getProcessStream().readFloat(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_fAccuracyPenalty"));
		final boolean ducking = (flags & SdkUtils.DUCKING_FLAG) != 0;
		if (accuracyCompDist == -1.0F || crosshairValid && crosshairDist <= accuracyCompDist) {
			prevAccuracyPenalty = -999.0F;
		} else if (prevAccuracyPenalty == -999.0F || !idxMatchesPrev || prevAccuracyPenalty == 0.0F && accuracyPenalty != 0.0F && !(melee && weaponIdx != Item.ZEUS_X27.index) || prevDucking != ducking || melee && weaponIdx != Item.ZEUS_X27.index || weaponIdx == Item.R8_REVOLVER.index || zoomChanged) {
			prevAccuracyPenalty = accuracyPenalty;
		}
		prevDucking = ducking;
		final boolean grenade = Item.grenade(weaponIdx), resetTimer = executed && (crosshairValid || crosshairTimer.delayPassed());
		final float[] vecPunch = new float[2];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * vecPunch.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecPunch"), MemoryStream.FLOAT_SZ * vecPunch.length)).rewind()).asFloatBuffer().get(vecPunch);
		if (!crosshairValid && crosshairTimer.delayPassed() && vecPunch[0] == 0.0F && vecPunch[1] == 0.0F && Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iShotsFired")) <= 0) {
			activateTimer.setStartTime();
		}
		final boolean activate = !(nameProtect.isEnabled() && nameProtect.getBoolean("Allies") && !emptyName && playerName != null && nameProtect.containsAlias(playerName)) && idxMatchesPrev && (prevAccuracyPenalty == -999.0F || prevAccuracyPenalty >= accuracyPenalty || Math.abs(prevAccuracyPenalty - accuracyPenalty) < 0.0001F) && (melee && weaponIdx != Item.ZEUS_X27.index || Reliant.instance.getProcessStream().readShort(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iClip1")) > 0 || grenade) && (!getBoolean("Next Attack") || automatic && !GameCache.isReloading() || canAttack) && (!getBoolean("Only Zoomed") || !sniper || zoomLevel > 0) && (!melee || crosshairDist <= getFloat("Melee Distance")) && (!Item.shotgun(weaponIdx) || crosshairDist <= getFloat("Shotgun Distance")) && (getBoolean("Grenades") || !grenade) && (getBoolean("Flashed") || Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flFlashDuration")) <= 0.0F) && attackTimer.delayPassed() && semiAutoSniperTimer.delayPassed() && autoSniperTimer.delayPassed();
		if ((crosshairValid || !deactivateTimer.delayPassed()) && activate && activateTimer.delayPassed()) {
			if (getBoolean("Power Attack") && melee && weaponIdx != Item.ZEUS_X27.index) {
				if (crosshairDist > SdkUtils.MAX_KNIFE_DIST || (((ImmunitySaverRoutine) Reliant.instance.getRoutineRegistry().get("Immunity Saver")).getBlockFlags() & ImmunitySaverRoutine.BLOCK_ATTACK_FLAG) == 0 && !Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2"), MemoryStream.TRUE)) {
					return;
				}
				executedPower = true;
			} else if ((((ImmunitySaverRoutine) Reliant.instance.getRoutineRegistry().get("Immunity Saver")).getBlockFlags() & ImmunitySaverRoutine.BLOCK_ATTACK_FLAG) == 0 && !Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceAttack"), MemoryStream.TRUE)) {
				return;
			}
			attackTimer.setStartTime();
			if (crosshairValid) {
				deactivateTimer.setStartTime();
			}
			executed = true;
			prevAccuracyPenalty = accuracyPenalty;
		} else if ((!idxMatchesPrev || resetTimer) && (!(activate && activateTimer.delayPassed()) || deactivateTimer.delayPassed()) || !localAlive) {
			if (executedPower && (((ImmunitySaverRoutine) Reliant.instance.getRoutineRegistry().get("Immunity Saver")).getBlockFlags() & ImmunitySaverRoutine.BLOCK_ATTACK_FLAG) == 0 && !Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2"), ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack 2 Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE) || !executedPower && (((ImmunitySaverRoutine) Reliant.instance.getRoutineRegistry().get("Immunity Saver")).getBlockFlags() & ImmunitySaverRoutine.BLOCK_ATTACK_FLAG) == 0 && !((AutocockRoutine) Reliant.instance.getRoutineRegistry().get("Auto-cock")).isExecuted() && !Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceAttack"), ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE)) {
				return;
			}
			executedPower = false;
			if (resetTimer || !localAlive) {
				crosshairTimer.reset();
				executed = false;
			}
		}
		setCrosshairEntity(MemoryStream.NULL);
	}
}
