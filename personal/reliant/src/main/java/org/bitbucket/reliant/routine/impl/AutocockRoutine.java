package org.bitbucket.reliant.routine.impl;

import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.eric_generic.math.MathHelper;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.SdkUtils;

// TODO(Eric) Add Immunity Saver-like configuration options.
public final class AutocockRoutine extends DisplayRoutine {
	private enum Condition {
		ALWAYS,
		ENEMIES_NEARBY,
		TEAMMATES_NEARBY,
		EITHER_NEARBY,
	}
	private boolean executed;
	private final Timer timer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			return getInt("Time");
		}

	}, delayTimer = new Timer() {
		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			return getBoolean("Keep") ? Math.min(getInt("Time"), SdkUtils.TICK_TIME) : getInt("Time");
		}
	};

	public AutocockRoutine() {
		super("Auto-cock", "Automatically cocks a revolver for attacking.", true, true, false, 1, new BoolOption("Visibility", "Specifies whether or not to only execute if a player is visible.", false), new FloatOption("Maximum Distance", "Specifies the maximum distance between the local player and another player.", new ClampedNumber<Float>(SdkUtils.MAX_DIST, 1.0F, (float) SdkUtils.MAX_VIS_RANGE), 100.0F), new IntOption("Condition", "Specifies the condition to execute on.", new ClampedNumber<Integer>(Condition.ENEMIES_NEARBY.ordinal(), 0, Condition.values().length - 1), 1), new BoolOption("Keep", "Specifies whether or not to keep a revolver cocked.", true), new IntOption("Time", "Specifies the amount of time to cock a revolver for.", new ClampedNumber<Integer>(200 - SdkUtils.TICK_TIME, 1, 200 - SdkUtils.TICK_TIME), 10));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFF8000;
	}

	boolean isExecuted() {
		return isEnabled() && executed;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		// Eric: Not necessary to check for Immunity Saver.
		if (executed) {
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr != MemoryStream.NULL && !((TriggerbotRoutine) Reliant.instance.getRoutineRegistry().get("Triggerbot")).executed()) {
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceAttack"), ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Attack Key")).keyDown(true) ? MemoryStream.TRUE : MemoryStream.FALSE);
			}
			executed = false;
		}
		timer.reset();
		if (shutdown) {
			delayTimer.reset();
		}
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
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer()) || Main.mouseEnabled()) {
			reset(false);
			return;
		}
		final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
		if (weaponEntity == MemoryStream.NULL) {
			throw new InvalidDataException("weapon_entity");
		}
		if (Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex")) != Item.R8_REVOLVER.index) {
			reset(false);
			return;
		}
		final long modAddr = Reliant.instance.isLegacyToolchain() ? clientAddr : Reliant.instance.getProcessStream().moduleAddress("engine.dll");
		if (modAddr == MemoryStream.NULL) {
			throw new InvalidDataException("mod_addr");
		}
		final long globalVars = Reliant.instance.isLegacyToolchain() ? Reliant.instance.getProcessStream().readInt(modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars")) : modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars");
		if (globalVars == MemoryStream.NULL) {
			return;
		}
		if (Reliant.instance.getProcessStream().readFloat(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flNextPrimaryAttack")) - Reliant.instance.getProcessStream().readFloat(globalVars + MemoryStream.FLOAT_SZ + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * 2) > 0.0F || ((KeyOption) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Reload Key")).keyPressed(true) || GameCache.isReloading() || Reliant.instance.getProcessStream().readShort(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iClip1")) <= 0) {
			reset(false);
			return;
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
				if (playerTeam != localTeam && visibility && !SdkUtils.entityVisible(player, true, SdkUtils.VisAggressiveness.BASIC, true, 1.0F)) {
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
		if (!hasPlayer) {
			reset(false);
			return;
		}
		if (!executed && delayTimer.delayPassed()) {
			timer.setStartTime();
		}
		if (timer.delayPassed()) {
			reset(false);
			return;
		}
		if (!((TriggerbotRoutine) Reliant.instance.getRoutineRegistry().get("Triggerbot")).executed() && Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwForceAttack"), MemoryStream.TRUE)) {
			delayTimer.setStartTime();
			executed = true;
		}
	}
}
