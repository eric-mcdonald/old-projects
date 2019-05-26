package org.bitbucket.reliant.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.lang.time.BasicTimer;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.GameCache.ParseMapThread;
import org.bitbucket.reliant.GameCache.ParseMapThread.MdlBox;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.valve_file_parsing.bsp.Brush;
import org.bitbucket.valve_file_parsing.bsp.Leaf;
import org.bitbucket.valve_file_parsing.bsp.Node;
import org.bitbucket.valve_file_parsing.bsp.Plane;

public final class SdkUtils {
	public static enum VisAggressiveness {
		NONE,
		BASIC,
		ADVANCED,
		EXTREME,
		RAY_TRACE,
		EFFICIENT
	}
	private static class VisPos extends Vector {
		private final boolean visible;

		private VisPos(final Vector pos, final boolean visible) {
			super(pos.x, pos.z, pos.y);
			this.visible = visible;
			// TODO Auto-generated constructor stub
		}
	}
	private static final Comparator<Player> distanceCmp = new Comparator<Player>() {

		@Override
		public int compare(Player o1, Player o2) {
			// TODO Auto-generated method stub
			if (localOrigin == null) {
				setLocalOrigin();
			}
			return Float.compare(org.bitbucket.eric_generic.math.MathHelper.distance(o1.vecOrigin[0], o1.vecOrigin[1], o1.vecOrigin[2], localOrigin[0], localOrigin[1], localOrigin[2]), org.bitbucket.eric_generic.math.MathHelper.distance(o2.vecOrigin[0], o2.vecOrigin[1], o2.vecOrigin[2], localOrigin[0], localOrigin[1], localOrigin[2]));
		}

	}, healthCmp = new Comparator<Player>() {

		@Override
		public int compare(Player o1, Player o2) {
			// TODO Auto-generated method stub
			return Float.compare(o1.health, o2.health);
		}

	}, visCmp = new Comparator<Player>() {

		@Override
		public int compare(Player o1, Player o2) {
			// TODO Auto-generated method stub
			final boolean vis1 = entityVisible(o1.entity, false, VisAggressiveness.BASIC, true, 1.0F), vis2 = entityVisible(o2.entity, false, VisAggressiveness.BASIC, true, 1.0F);
			return vis1 == vis2 ? 0 : vis1 && !vis2 ? -1 : 1;
		}
		
	};
	private static float[] localOrigin;
	public static final int PLAYERS_SZ = 64, NEXT_ENTITY_SZ = 0x10, INVALID_ENTITY_HANDLE = 0xFFFFFFFF, INVALID_PLAYER_IDX = -1;
	public static final int MAX_COORD_RANGE = 16384, MAX_VIS_RANGE = 4096;
	public static final int IMMUNITY_FALSE = 256, IMMUNITY_NO_MOVE = 0, FORCE_TRUE = 5, FORCE_FALSE = 4, SIGNONSTATE_NONE = 0, SIGNONSTATE_FULL = 6, MASK_SHOT_HULL = Brush.CONTENTS_SOLID | Brush.CONTENTS_MOVEABLE | Brush.CONTENTS_MONSTER | Brush.CONTENTS_WINDOW | Brush.CONTENTS_DEBRIS | Brush.CONTENTS_GRATE;
	public static final byte LIFE_ALIVE = 0, LIFE_DYING = 1;
	public static final float MAX_DIST = 3000.0F, MAX_KNIFE_DIST = 65.0F, GRID_RECT_UNITS = 256.0F, MAX_PLAYER_PITCH = 89.0F, VEC_EQUALS_DIST = 1.0F;
	public static final int PLAYER_NAME_SZ = 32;
	private static final String PLAYER_NAME_CHARSET = "UTF-16LE";
	public static final int TICK_TIME = 64;
	private static final Timer crosshairTimer = new BasicTimer(TICK_TIME * 2);
	public static final int ON_GROUND_FLAG = 1 << 0, DUCKING_FLAG = 1 << 1, FAKE_CLIENT_FLAG = 1 << 8;
	public static final VisAggressiveness[] visValues = VisAggressiveness.values();
	private static int prevCrossEntity = MemoryStream.NULL;

	private static final Map<Integer, VisPos> entityPosMap = new HashMap<Integer, VisPos>();

	private static VisPos prevOriginPos;
	public static int bodyBoneId() {
		return Main.csco() ? 7 : 6;
	}
	public static boolean cmdDown(final int cmd) {
		return cmd == FORCE_TRUE || cmd == MemoryStream.TRUE;
	}
	public static boolean entityAlive(final int entity) {
		return entityValid(entity) && Reliant.instance.getProcessStream().readShort(entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iHealth")) > 0 && Reliant.instance.getProcessStream().readByte(entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_lifeState")) == LIFE_ALIVE;
	}
	public static boolean entityAttackable(final int entity) {
		if (!entityAlive(entity)) {
			return false;
		}
		final int immunity = Reliant.instance.getProcessStream().readInt(entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_bGunGameImmunity"));
		return immunity == IMMUNITY_FALSE || immunity == IMMUNITY_NO_MOVE;
	}
	public static float[] entityBone(final int entity, final int boneId) {
		final int boneMatrix = Reliant.instance.getProcessStream().readInt(entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwBoneMatrix"));
		if (boneMatrix == MemoryStream.NULL) {
			throw new InvalidDataException("bone_matrix");
		}
		final float[][] entityBone = new float[3][4];
		for (int i = 0; i < entityBone.length; i++) {
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * entityBone[i].length).put(Reliant.instance.getProcessStream().read(boneMatrix + 0x30 * boneId + MemoryStream.FLOAT_SZ * entityBone[i].length * i, MemoryStream.FLOAT_SZ * entityBone[i].length)).rewind()).asFloatBuffer().get(entityBone[i]);
		}
		return new float[] {entityBone[0][3], entityBone[1][3], entityBone[2][3]};
	}
	public static int entityByHandle(int entityHandle) {
		if (entityHandle == MemoryStream.NULL || entityHandle == INVALID_ENTITY_HANDLE) {
			return MemoryStream.NULL;
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		entityHandle &= 0xFFF;
		return entityHandle == MemoryStream.NULL ? entityHandle : Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + (entityHandle - 1) * NEXT_ENTITY_SZ);
	}
	public static int entityById(final short entityId) {
		if (entityId == 0) {
			return MemoryStream.NULL;
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		return Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + (entityId - 1) * NEXT_ENTITY_SZ);
	}
	public static boolean entityValid(final int entity) {
		return entity != MemoryStream.NULL && Reliant.instance.getProcessStream().readInt(entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_bDormant")) == MemoryStream.FALSE;
	}
	public static boolean entityVisible(final int target, final boolean useTimer, final VisAggressiveness visMode, final boolean useSpotted, final float stepAmount) {
		if (!entityAlive(target) || !entityAlive(GameCache.getLocalPlayer())) {
			return false;
		}
		if (visMode.equals(VisAggressiveness.NONE)) {
			return true;
		}
		final int crosshairEntity = GameCache.getCrosshairEntity();
		if (crosshairEntity == target) {
			prevCrossEntity = crosshairEntity;
			crosshairTimer.setStartTime();
			return true;
		}
		if (crosshairTimer.delayPassed()) {
			prevCrossEntity = MemoryStream.NULL;
		} else if (useTimer && !entityValid(crosshairEntity) && prevCrossEntity == target) {
			return true;
		}
		final float[] playerBone = entityBone(target, headBoneId());
		if (playerBone == null) {
			throw new InvalidDataException("player_bone");
		}
		final Vector playerBoneVec = Vector.wrap(playerBone);
		setLocalOrigin();
		final float[] localOrigin = getLocalOrigin();
		final Vector localOriginVec = Vector.wrap(localOrigin);
		localOriginVec.y += Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecViewOffset[0]") + MemoryStream.FLOAT_SZ * 2);
		if (useSpotted) {
			int targetIdx = INVALID_PLAYER_IDX;
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr == MemoryStream.NULL) {
				throw new InvalidDataException("client_addr");
			}
			for (int playerIdx = 0; playerIdx <= PLAYERS_SZ; playerIdx++) {
				final int entity = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + playerIdx * NEXT_ENTITY_SZ);
				if (entity == MemoryStream.NULL || entity == GameCache.getLocalPlayer() || StringUtils.empty(SdkUtils.readRadarName(playerIdx)) && playerIdx == 64) {
					continue;
				}
				if (entity == target) {
					targetIdx = playerIdx;
					break;
				}
			}
			if (GameCache.getClientState() == MemoryStream.NULL) {
				throw new InvalidDataException("client_state");
			}
			final int localIdx = localPlayerIdx();
			if (localIdx == INVALID_PLAYER_IDX) {
				return false;
			}
			if ((Reliant.instance.getProcessStream().readInt(target + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_bSpottedByMask")) & (1 << localIdx)) != 0 || targetIdx != INVALID_PLAYER_IDX && (Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_bSpottedByMask")) & (1 << targetIdx)) != 0) {
				return true;
			}
			final VisPos entityVisPos;
			// Eric: Hotfix for an issue in which the spotted check is invalid.
			if ((entityVisPos = entityPosMap.get(target)) != null && vectorsEqualTrace(entityVisPos, playerBoneVec) && prevOriginPos != null && vectorsEqualTrace(prevOriginPos, localOriginVec)) {
				return entityVisPos.visible; // Eric: prevOriginPos.visible does not match up with the entity's visibility state
			}
		}
		if (visMode.equals(VisAggressiveness.BASIC) || visMode.equals(VisAggressiveness.EFFICIENT)) {
			return false;
		}
		final float percentVis = traceRay(localOriginVec, playerBoneVec, stepAmount, MASK_SHOT_HULL, visMode.equals(VisAggressiveness.ADVANCED));
		return percentVis != -999.0F && (useSpotted ? putVisPos(target, playerBoneVec, localOriginVec, percentVis == 1.0F) : percentVis == 1.0F);
	}
	public static boolean entityVisTrace(final int target, final boolean useTimer, final float stepAmount) {
		return entityVisible(target, useTimer, VisAggressiveness.ADVANCED, false, stepAmount);
	}

	public static float[] getLocalOrigin() {
		return localOrigin;
	}
	public static int headBoneId() {
		return Main.csco() ? 6 : 8;
	}
	private static Leaf leafByPoint(final Vector point) {
		if (GameCache.getMapDir() == null) {
			return null;
		}
		final ParseMapThread mapParser = GameCache.getMapParser(GameCache.getMapDir());
		if (mapParser == null || mapParser.isAlive()) {
			return null;
		}
		int nodeIdx;
		for (nodeIdx = 0; nodeIdx >= 0;) {
			final Node currentNode = mapParser.bspParser.getNodeLump()[nodeIdx];
			final Plane currentPlane = mapParser.bspParser.getPlaneLump()[currentNode.planeNum];
			nodeIdx = point.dotProduct(currentPlane.normal) - currentPlane.dist > 0.0F ? currentNode.children[0] : currentNode.children[1];
		}
		return mapParser.bspParser.getLeafLump()[-(nodeIdx + 1)];
	}
	public static int localPlayerIdx() {
		return GameCache.getClientState() == MemoryStream.NULL ? INVALID_PLAYER_IDX : Reliant.instance.isLegacyToolchain() ? Reliant.instance.getProcessStream().readInt(GameCache.getClientState() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwLocalPlayerIndex")) : playerIdx(GameCache.getLocalPlayer());
	}
	public static int playerIdx(final int player) {
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		for (int playerIdx = 0; playerIdx <= PLAYERS_SZ; playerIdx++) {
			final int currentPlayer = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + playerIdx * NEXT_ENTITY_SZ);
			if (currentPlayer == MemoryStream.NULL || StringUtils.empty(SdkUtils.readRadarName(playerIdx)) && playerIdx == 64) {
				continue;
			}
			if (currentPlayer == player) {
				return playerIdx;
			}
		}
		return INVALID_PLAYER_IDX;
	}
	private static boolean putVisPos(final int player, final Vector playerOriginVec, final Vector localOriginVec, final boolean visible) {
		entityPosMap.put(player, new VisPos(playerOriginVec, visible));
		prevOriginPos = new VisPos(localOriginVec, visible);
		return visible;
	}
	public static String readRadarName(final int entityIdx) {
		// Eric: PLAYER_NAME_SZ * 2 for unicode
		return entityIdx == INVALID_PLAYER_IDX ? "" : Reliant.instance.getProcessStream().read(Reliant.instance.isLegacyToolchain() ? GameCache.getRadarBasePtr() + 0x1EC * (entityIdx + 1) + 0x3C : GameCache.getRadarBasePtr() + 0x1E0 * (entityIdx + 1) + 0x24, PLAYER_NAME_SZ * 2, PLAYER_NAME_CHARSET);
	}
	public static void reset() {
		crosshairTimer.reset();
		prevCrossEntity = MemoryStream.NULL;
		entityPosMap.clear();
		prevOriginPos = null;
	}
	private static void resetTrace(Map<Thread, Integer> prevPriorities, final int prevCheatPriority, final int prevGamePriority) {
		for (final Thread thread : Thread.getAllStackTraces().keySet()) {
			final Integer priority = prevPriorities.get(thread);
			if (priority != null) {
				thread.setPriority(priority);
			}
		}
		Reliant.instance.getProcessStream().setPriorityClass(prevGamePriority);
		Reliant.instance.setPriorityClass(prevCheatPriority);
	}
	public static void setLocalOrigin() {
		setLocalOrigin(GameCache.getLocalPlayer());
	}
	private static void setLocalOrigin(final int originEntity) {
		if (originEntity == GameCache.getLocalPlayer() && !entityValid(originEntity)) {
			return;
		}
		final float[] vecOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * vecOrigin.length).put(Reliant.instance.getProcessStream().read(originEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * vecOrigin.length)).rewind()).asFloatBuffer().get(vecOrigin);
		localOrigin = vecOrigin;
	}
	public static List<Player> sortedPlayers(final int originEntity, final boolean allTeams, final boolean deadPlayers, final boolean localCheck) {
		final List<Player> players = new ArrayList<Player>();
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			return players;
		}
		if (localCheck && !entityValid(originEntity)) {
			return players;
		}
		for (int playerIdx = 0; playerIdx <= PLAYERS_SZ; playerIdx++) {
			final int entity = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + playerIdx * NEXT_ENTITY_SZ);
			if (!entityValid(entity) || localCheck && entity == originEntity || StringUtils.empty(SdkUtils.readRadarName(playerIdx)) && playerIdx == 64) {
				continue;
			}
			if ((allTeams || Reliant.instance.getProcessStream().readShort(entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) != Reliant.instance.getProcessStream().readShort(originEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"))) && (deadPlayers || entityAlive(entity) && (!localCheck || entityAlive(originEntity)))) {
				float[] vecOrigin = new float[3];
				((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * vecOrigin.length).put(Reliant.instance.getProcessStream().read(entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * vecOrigin.length)).rewind()).asFloatBuffer().get(vecOrigin);
				players.add(new Player(entity, playerIdx, vecOrigin, Reliant.instance.getProcessStream().readShort(entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iHealth"))));
			}
		}
		setLocalOrigin(originEntity);
		Collections.sort(players, distanceCmp);
		if (originEntity == GameCache.getLocalPlayer()) {
			Collections.sort(players, healthCmp);
			Collections.sort(players, visCmp);
		}
		return players;
	}
	public static float traceRay(final Vector startVec, final Vector endVec, final float stepAmount, final int contentsMask, final boolean useMdls) {
		final int prevCheatPriority = Reliant.instance.getCheatPriority(), prevGamePriority = Reliant.instance.getProcessStream().getPriorityClass();
		Reliant.instance.setPriorityClass(0x00000100);
		Reliant.instance.getProcessStream().setPriorityClass(0x00000040);
		final Map<Thread, Integer> prevPriorities = new HashMap<Thread, Integer>();
		prevPriorities.put(Thread.currentThread(), Thread.currentThread().getPriority());
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		for (final Thread thread : Thread.getAllStackTraces().keySet()) {
			if (thread != Thread.currentThread()) {
				prevPriorities.put(thread, thread.getPriority());
				thread.setPriority(Thread.MIN_PRIORITY);
			}
		}
		if (GameCache.getMapDir() == null) {
			resetTrace(prevPriorities, prevCheatPriority, prevGamePriority);
			return -999.0F;
		}
		final ParseMapThread mapParser = GameCache.getMapParser(GameCache.getMapDir());
		if (mapParser == null || mapParser.isAlive()) {
			resetTrace(prevPriorities, prevCheatPriority, prevGamePriority);
			return -999.0F;
		}
		final Vector dirVec = endVec.copy();
		dirVec.subtract(startVec);
		float dirSteps = (float) Math.floor(dirVec.length());
		dirVec.divide(new Vector(dirSteps, dirSteps, dirSteps));
		dirVec.multiply(new Vector(stepAmount, stepAmount, stepAmount));
		final Vector currentVec = startVec.copy();
		final Set<MdlBox> mdls = new HashSet<MdlBox>(mapParser.mdlBoxes);
		Iterator<MdlBox> validMdls = mdls.iterator();
		//int i = mapParser.mdlBoxes.size();
		//System.out.println("Before: " + mdls.size());
		for (; dirSteps > 0.0F; dirSteps -= stepAmount) {
			currentVec.add(dirVec);
			final Leaf currentLeaf = leafByPoint(currentVec);
			if (currentLeaf != null && (currentLeaf.contents & contentsMask) != 0) {
				final float totalDist = startVec.distance(endVec);
				resetTrace(prevPriorities, prevCheatPriority, prevGamePriority);
				return startVec.distance(currentVec) / (totalDist == 0.0F ? 1.0F : totalDist);
			}
			if (useMdls) {
				while (validMdls.hasNext()) {
					final MdlBox mdlBox = validMdls.next();
					final float totalDist = startVec.distance(endVec);
					if (!(startVec.x > mdlBox.mins.x && startVec.x < mdlBox.maxs.x && startVec.z > mdlBox.mins.z && startVec.z < mdlBox.maxs.z && startVec.y > mdlBox.mins.y && startVec.y < mdlBox.maxs.y) && currentVec.x > mdlBox.mins.x && currentVec.x < mdlBox.maxs.x && currentVec.z > mdlBox.mins.z && currentVec.z < mdlBox.maxs.z && currentVec.y > mdlBox.mins.y && currentVec.y < mdlBox.maxs.y) {
						resetTrace(prevPriorities, prevCheatPriority, prevGamePriority);
						return startVec.distance(currentVec) / (totalDist == 0.0F ? 1.0F : totalDist);
					} else if (startVec.distance(mdlBox.mins) > totalDist && startVec.distance(mdlBox.maxs) > totalDist) {
						validMdls.remove();
						//System.out.println("During: " + i);
						//--i;
					}
				}
				validMdls = mdls.iterator();
				//System.out.println("During: " + mdls.size());
			}
		}
		//System.out.println("After: " + mdls.size());
		//if (i != mapParser.mdlBoxes.size())
		//System.out.println("After: " + i);
		resetTrace(prevPriorities, prevCheatPriority, prevGamePriority);
		return 1.0F;
	}
	public static boolean vectorsEqualTrace(final Vector vecOne, final Vector vecTwo) {
		return vecOne.distance(vecTwo) < VEC_EQUALS_DIST;
	}
	public static boolean writeRadarName(final String name, final int entityIdx) {
		return Reliant.instance.getProcessStream().write(Reliant.instance.isLegacyToolchain() ? GameCache.getRadarBasePtr() + 0x1EC * (entityIdx + 1) + 0x3C : GameCache.getRadarBasePtr() + 0x1E0 * (entityIdx + 1) + 0x24, name, PLAYER_NAME_SZ * 2, PLAYER_NAME_CHARSET);
	}
	public static boolean writeViewAngles(final float[] viewAngles) {
		org.bitbucket.eric_generic.math.MathHelper.clampAngles(viewAngles, MAX_PLAYER_PITCH);
		return Reliant.instance.getProcessStream().write(GameCache.getClientState() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwViewAngles"), NioUtils.createBuffer(MemoryStream.FLOAT_SZ * 2).putFloat(viewAngles[0]).putFloat(viewAngles[1]).array());
	}
}
