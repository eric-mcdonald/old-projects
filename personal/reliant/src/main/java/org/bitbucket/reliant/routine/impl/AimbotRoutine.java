package org.bitbucket.reliant.routine.impl;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.lang.time.BasicTimer;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.GameCache.ParseMapThread;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.handler.CrosshairHandler;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.routine.Routine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.Player;
import org.bitbucket.reliant.util.SdkUtils;
import org.bitbucket.reliant.util.math.MathHelper;

public final class AimbotRoutine extends DisplayRoutine {
	private final class Bone {
		private int boneId;
		private Vector bonePos;

		private Bone(final int boneId, final Vector bonePos) {
			this.boneId = boneId;
			this.bonePos = bonePos;
		}
	}

	private enum CrosshairMode {
		NO_OVERRIDE,
		OVERRIDE_SAFE,
		OVERRIDE_NORMAL,
		OVERRIDE_AGGRESSIVE
	}
	private int targetEntity = MemoryStream.NULL, visEntity = MemoryStream.NULL;
	private final Timer switchTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			return getInt("Switch Delay");
		}

	};
	private final Timer visTimer = new BasicTimer(SdkUtils.TICK_TIME * 4);
	private boolean prevDucking;
	private Vector prevLocalPos; // Eric: Origin + View Offset (x, z, y)
	private final Map<Integer, Bone> entityBoneMap = new HashMap<Integer, Bone>();
	private int baseFov = 90;

	public AimbotRoutine() {
		super("Aimbot", "Automatically aims at players.", true, 0, new BoolOption("Body Aim AWP", "Specifies whether or not to aim at the body if the player is holding an AWP.", true), new BoolOption("Flashed", "Specifies whether or not to execute while flashed.", true), new BoolOption("Scale FOV", "Specifies whether or not to scale the FOV based on certain conditions.", false), new BoolOption("Grenades", "Specifies whether or not to aim at an enemy while the player is holding a grenade.", false), new BoolOption("Local on Ground", "Specifies whether or not to require the player to be on the ground.", false), new BoolOption("Target on Ground", "Specifies whether or not to require the target to be on the ground.", false), new BoolOption("All Teams", "Specifies whether or not to aim at all teams.", false), new IntOption("Override Crosshair Mode", "Specifies the mode to override the crosshair entity with.", new ClampedNumber<Integer>(CrosshairMode.OVERRIDE_NORMAL.ordinal(), 0, CrosshairMode.values().length - 1), 1), new BoolOption("Next Attack", "Specifies whether or not to wait until the player can attack again.", false), new IntOption("Visibility Aggressiveness", "Specifies how aggressive the visibility checking is. Set to 0 for no visibility checking.", new ClampedNumber<Integer>(2, 0, 5), 1), new FloatOption("FOV", "Specifies the maximum rotation difference to rotate by.", new ClampedNumber<Float>(360.0F, 0.1F, 360.0F), 10.0F), new IntOption("Bone ID", "Specifies the bone that this routine will aim at.", new ClampedNumber<Integer>(SdkUtils.headBoneId(), 0, 127), 1), new FloatOption("Melee Distance", "Specifies the maximum distance between your player and another while holding a melee weapon.", new ClampedNumber<Float>(100.0F, 0.0F, SdkUtils.MAX_DIST), 100.0F), new FloatOption("Shotgun Distance", "Specifies the maximum distance between your player and another while holding a shotgun.", new ClampedNumber<Float>(1000.0F, 0.0F, SdkUtils.MAX_DIST), 100.0F), new IntOption("Switch Delay", "Specifies the delay to wait for before switching targets.", new ClampedNumber<Integer>(0, 0, 1000), 100), new FloatOption("Yaw Offset", "Specifies the amount to offset the target yaw by.", new ClampedNumber<Float>(0.0F, -org.bitbucket.eric_generic.math.MathHelper.MAX_FOV, org.bitbucket.eric_generic.math.MathHelper.MAX_FOV), 10.0F), new FloatOption("Pitch Offset", "Specifies the amount to offset the target pitch by.", new ClampedNumber<Float>(0.0F, -SdkUtils.MAX_PLAYER_PITCH, SdkUtils.MAX_PLAYER_PITCH), 10.0F), new FloatOption("Yaw Increment", "Specifies the amount to increment the yaw by.", new ClampedNumber<Float>(org.bitbucket.eric_generic.math.MathHelper.MAX_FOV, 0.1F, org.bitbucket.eric_generic.math.MathHelper.MAX_FOV), 10.0F), new FloatOption("Pitch Increment", "Specifies the amount to increment the pitch by.", new ClampedNumber<Float>(SdkUtils.MAX_PLAYER_PITCH, 0.1F, SdkUtils.MAX_PLAYER_PITCH), 10.0F), new FloatOption("Ray Trace Step", "Specifies the perferred value to step by whilst tracing a ray.", new ClampedNumber<Float>(1.0F, 1.0F, 100.0F), 10.0F));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFF0000;
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return String.valueOf(SdkUtils.sortedPlayers(GameCache.getLocalPlayer(), getBoolean("All Teams"), false, true).size());
	}
	
	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		targetEntity = visEntity = MemoryStream.NULL;
		switchTimer.reset();
		visTimer.reset();
		prevDucking = false;
		prevLocalPos = null;
		entityBoneMap.clear();
		baseFov = 90;
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post) {
			return;
		}
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return;
		}
		final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
		if (weaponEntity == MemoryStream.NULL) {
			throw new InvalidDataException("weapon_entity");
		}
		final int weaponIdx = Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"));
		final boolean allTeams = getBoolean("All Teams");
		final List<Player> players = SdkUtils.sortedPlayers(GameCache.getLocalPlayer(), allTeams, false, true);
		SdkUtils.setLocalOrigin();
		final float[] localOrigin = SdkUtils.getLocalOrigin();
		final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
		final boolean allies = nameProtect.getBoolean("Allies");
		final int overrideCrossMode = getInt("Override Crosshair Mode");
		int visAggressive = getInt("Visibility Aggressiveness");
		final ParseMapThread mapParser = GameCache.getMapDir() == null ? null : GameCache.getMapParser(GameCache.getMapDir());
		if (visAggressive > 1 && (mapParser == null || mapParser.isAlive())) {
			visAggressive = 1;
		}
		final SdkUtils.VisAggressiveness visibilityMode = SdkUtils.visValues[visAggressive];
		final int visMode = visibilityMode.ordinal();
		final boolean localOnGround = !getBoolean("Local on Ground") || (Reliant.instance.getProcessStream().readByte(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_fFlags")) & SdkUtils.ON_GROUND_FLAG) != 0, targetOnGround = getBoolean("Target on Ground");
		final float meleeDist = getFloat("Melee Distance"), shotgunDist = getFloat("Shotgun Distance");
		final long modAddr = Reliant.instance.getProcessStream().moduleAddress(Reliant.instance.isLegacyToolchain() ? "client.dll" : "engine.dll");
		if (modAddr == MemoryStream.NULL) {
			throw new InvalidDataException("mod_addr");
		}
		final long globalVars = Reliant.instance.isLegacyToolchain() ? Reliant.instance.getProcessStream().readInt(modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars")) : modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars");
		if (globalVars == MemoryStream.NULL) {
			throw new InvalidDataException("global_vars");
		}
		final boolean grenade = Item.grenade(weaponIdx);
		final boolean melee = Item.melee(weaponIdx), automatic = Item.automatic(weaponIdx), nextAttack = getBoolean("Next Attack"), grenades = getBoolean("Grenades") || !grenade;
		boolean targeted = false;
		final Routine rcs = Reliant.instance.getRoutineRegistry().get("RCS");
		final TriggerbotRoutine triggerbot = (TriggerbotRoutine) Reliant.instance.getRoutineRegistry().get("Triggerbot");
		final int boneId = getBoolean("Body Aim AWP") && weaponIdx == Item.AWP.index ? SdkUtils.bodyBoneId() : getInt("Bone ID");
		final float[] vecOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * vecOrigin.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * vecOrigin.length)).rewind()).asFloatBuffer().get(vecOrigin);
		final float[] viewOffset = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * viewOffset.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecViewOffset[0]"), MemoryStream.FLOAT_SZ * viewOffset.length)).rewind()).asFloatBuffer().get(viewOffset);
		final Vector localPosVec = Vector.wrap(vecOrigin);
		localPosVec.add(Vector.wrap(viewOffset));
		final boolean ducking = (Reliant.instance.getProcessStream().readByte(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_fFlags")) & SdkUtils.DUCKING_FLAG) != 0;
		final float rayTraceStep = getFloat("Ray Trace Step");
		float fov = getFloat("FOV");
		if (getBoolean("Scale FOV")) {
			final int currentFov = Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iFOVStart") - 0x4);
			if (currentFov > 0) {
				if (Reliant.instance.getProcessStream().readShort(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_zoomLevel")) <= 0) {
					baseFov = currentFov;
				} else if (currentFov < baseFov) { // Eric: Makes sure that the scaled FOV is never higher than the desired maximum FOV.
					fov *= (float) currentFov / baseFov;
				}
			}
		}
		for (final Player player : players) {
			final String playerName = SdkUtils.readRadarName(player.entityIdx);
			if (StringUtils.empty(playerName)) {
				continue;
			}
			final float[] playerOrigin = new float[3];
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * playerOrigin.length).put(Reliant.instance.getProcessStream().read(player.entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * playerOrigin.length)).rewind()).asFloatBuffer().get(playerOrigin);
			final float originDist = org.bitbucket.eric_generic.math.MathHelper.distance(localOrigin[0], localOrigin[1], localOrigin[2], playerOrigin[0], playerOrigin[1], playerOrigin[2]);
			final boolean visibleGuess = visMode == 4 ? SdkUtils.entityVisTrace(player.entity, false, rayTraceStep) : SdkUtils.entityVisible(player.entity, false, visibilityMode, true, rayTraceStep), visible = visMode == 4 ? visibleGuess : SdkUtils.entityVisible(player.entity, false, SdkUtils.VisAggressiveness.BASIC, true, rayTraceStep);
			if (visibleGuess && visibleGuess != visible && visTimer.getStartTime() == 0L) {
				visTimer.setStartTime();
			}
			final boolean timedOut = !visible && visMode == 3 && visTimer.delayPassed();
			if (timedOut) {
				visTimer.reset();
			}
			final Bone prevBone = entityBoneMap.get(player.entity);
			final float[] playerBone = SdkUtils.entityBone(player.entity, boneId);
			if (playerBone == null) {
				throw new InvalidDataException("player_bone");
			}
			final boolean rcsVis = rcs.isEnabled() && triggerbot.isEnabled() && triggerbot.getPrevCrossEntity() == player.entity && prevDucking == ducking && !melee && !grenade && (prevLocalPos == null || SdkUtils.vectorsEqualTrace(prevLocalPos, localPosVec)) && (prevBone == null || prevBone.boneId == boneId && SdkUtils.vectorsEqualTrace(prevBone.bonePos, Vector.wrap(playerBone)));
			entityBoneMap.put(player.entity, new Bone(boneId, Vector.wrap(playerBone)));
			if (!(nameProtect.isEnabled() && allies && nameProtect.containsAlias(playerName)) && (localOnGround || melee && weaponIdx != Item.ZEUS_X27.index) && grenades && (!targetOnGround || (Reliant.instance.getProcessStream().readByte(player.entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_fFlags")) & SdkUtils.ON_GROUND_FLAG) != 0 || melee && weaponIdx != Item.ZEUS_X27.index) && (visMode <= 0 || visMode == 2 && visibleGuess || visible || visMode == 5 || rcsVis || visMode == 3 && !timedOut && (visEntity == MemoryStream.NULL || visEntity == player.entity) && visibleGuess) && (!nextAttack || automatic && !GameCache.isReloading() || Reliant.instance.getProcessStream().readFloat(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flNextPrimaryAttack")) - Reliant.instance.getProcessStream().readFloat(globalVars + MemoryStream.FLOAT_SZ + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * 2) <= 0.0F) && (!melee || originDist <= meleeDist) && (!Item.shotgun(weaponIdx) || originDist <= shotgunDist) && (getBoolean("Flashed") || Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flFlashDuration")) <= 0.0F) && switchTimer.delayPassed() && MathHelper.faceEntity(player.entity, boneId, fov, getFloat("Yaw Offset"), getFloat("Pitch Offset"), getFloat("Yaw Increment"), getFloat("Pitch Increment"))) {
				// Eric: Hotfix for an RCS conflict
				if (overrideCrossMode != CrosshairMode.NO_OVERRIDE.ordinal() && (visMode <= 0 || visMode == 4 || (overrideCrossMode == CrosshairMode.OVERRIDE_AGGRESSIVE.ordinal() || overrideCrossMode == CrosshairMode.OVERRIDE_NORMAL.ordinal()) && SdkUtils.entityAlive(GameCache.getCrosshairEntity()) && (overrideCrossMode != CrosshairMode.OVERRIDE_NORMAL.ordinal() || allTeams || Reliant.instance.getProcessStream().readShort(GameCache.getCrosshairEntity() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) != Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"))) || SdkUtils.entityVisible(player.entity, false, SdkUtils.VisAggressiveness.BASIC, false, rayTraceStep) || rcsVis)) {
					for (final CrosshairHandler crosshairHandler : Reliant.instance.getCrosshairHandlerRegistry().objects()) {
						crosshairHandler.setCrosshairEntity(player.entity);
					}
				}
				if (targetEntity == MemoryStream.NULL) {
					targetEntity = player.entity;
				}
				visEntity = player.entity;
				if (targetEntity != player.entity) {
					targetEntity = player.entity;
					switchTimer.setStartTime();
				}
				targeted = true;
				break;
			}
		}
		prevLocalPos = localPosVec;
		prevDucking = ducking;
		if (!targeted) {
			visEntity = MemoryStream.NULL;
		}
	}
}
