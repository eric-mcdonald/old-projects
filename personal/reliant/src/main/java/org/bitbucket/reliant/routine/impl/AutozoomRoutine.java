package org.bitbucket.reliant.routine.impl;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.lang.time.BasicTimer;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.CrosshairRoutine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.SdkUtils;

public final class AutozoomRoutine extends CrosshairRoutine {
	private int prevAttack2 = -1;
	private short prevZoom = -1;
	private final Timer crosshairTimer = new BasicTimer(SdkUtils.TICK_TIME * 2);
	private String prevCrosshairName;
	private final Timer zoomTimer = new Timer() {

		@Override
		protected long delay() {
			return getInt("Delay");
		}
		
	};

	public AutozoomRoutine() {
		super("Auto-zoom", "Zooms the player's weapon in.", true, true, 4, new IntOption("Delay", "Specifies the minimum delay to wait before resetting.", new ClampedNumber<Integer>(SdkUtils.TICK_TIME * 8, 0, 10000), 1000), new BoolOption("Local on Ground", "Specifies whether or not to require the player to be on the ground.", false), new BoolOption("Target on Ground", "Specifies whether or not to require the target to be on the ground.", false), new BoolOption("All Teams", "Specifies whether or not to execute on all teams.", false));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFFFF00;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		if (prevAttack2 != -1) {
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr != MemoryStream.NULL) {
				if (shutdown) {
					// TODO(Eric) Fix dwForceAttack2: It's actually +forward right now...
					Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2"), SdkUtils.cmdDown(prevAttack2) && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack 2 Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
					prevAttack2 = prevZoom = -1;
					crosshairTimer.reset();
					zoomTimer.reset();
				} else {
					Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2"), MemoryStream.TRUE);
					final int localPlayer = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwLocalPlayer"));
					if (SdkUtils.entityValid(localPlayer)) {
						final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(localPlayer + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
						if (weaponEntity != MemoryStream.NULL && (!Item.scoped(Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"))) || Reliant.instance.getProcessStream().readShort(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_zoomLevel")) <= prevZoom)) {
							Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2"), SdkUtils.cmdDown(prevAttack2) && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack 2 Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
							prevAttack2 = prevZoom = -1;
							crosshairTimer.reset();
							zoomTimer.reset();
						}
					} else {
						Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2"), SdkUtils.cmdDown(prevAttack2) && ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack 2 Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
						prevAttack2 = prevZoom = -1;
						crosshairTimer.reset();
						zoomTimer.reset();
					}
				}
			} else {
				prevAttack2 = prevZoom = -1;
				crosshairTimer.reset();
				zoomTimer.reset();
			}
		}
		prevCrosshairName = null;
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post) {
			return;
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL || !SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			reset(false);
			return;
		}
		final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
		if (weaponEntity == MemoryStream.NULL) {
			throw new InvalidDataException("weapon_entity");
		}
		final int weaponIdx = Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"));
		final int crosshairEntity = getCrosshairEntity();
		final boolean melee = Item.melee(weaponIdx);
		final boolean crosshairValid = SdkUtils.entityValid(crosshairEntity) && (getBoolean("All Teams") || Reliant.instance.getProcessStream().readShort(crosshairEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) != Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"))) && (!getBoolean("Local on Ground") || (Reliant.instance.getProcessStream().readByte(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_fFlags")) & SdkUtils.ON_GROUND_FLAG) != 0 || melee && weaponIdx != Item.ZEUS_X27.index) && (!getBoolean("Target on Ground") || (Reliant.instance.getProcessStream().readByte(crosshairEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_fFlags")) & SdkUtils.ON_GROUND_FLAG) != 0 || melee && weaponIdx != Item.ZEUS_X27.index) && SdkUtils.entityAlive(crosshairEntity) && SdkUtils.entityAlive(GameCache.getLocalPlayer()) && SdkUtils.entityAttackable(crosshairEntity);
		final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
		final String playerName = crosshairValid ? GameCache.getCrosshairName() : prevCrosshairName;
		final boolean emptyName = StringUtils.empty(playerName);
		if (crosshairValid && !emptyName) {
			prevCrosshairName = playerName;
		}
		if (crosshairValid && !(nameProtect.isEnabled() && nameProtect.getBoolean("Allies") && !emptyName && playerName != null && nameProtect.containsAlias(playerName)) && Item.scoped(weaponIdx)) {
			final short zoomLevel = Reliant.instance.getProcessStream().readShort(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_zoomLevel"));
			if (zoomLevel <= 0) {
				if (prevAttack2 == -1) {
					prevAttack2 = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2"));
				}
				if (prevZoom == -1) {
					prevZoom = zoomLevel;
				}
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2"), MemoryStream.TRUE);
			} else {
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack2"), MemoryStream.FALSE);
			}
			crosshairTimer.setStartTime();
			zoomTimer.setStartTime();
		} else if (crosshairTimer.delayPassed() && zoomTimer.delayPassed()) {
			reset(false);
		}
		setCrosshairEntity(MemoryStream.NULL);
	}
}
