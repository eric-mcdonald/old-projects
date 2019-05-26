package org.bitbucket.reliant.util.math;

import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.impl.RcsRoutine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.SdkUtils;

public final class MathHelper extends org.bitbucket.eric_generic.math.MathHelper {
	public static final float UNITS_TO_METERS = 0.01905F;
	public static float[] angleDiff(final int entity1, final int entity2) {
		final float[] entityBone = SdkUtils.entityBone(entity2, SdkUtils.headBoneId());
		if (entityBone == null || !SdkUtils.entityAlive(entity1)) {
			return null;
		}
		entityBone[2] -= Reliant.instance.getProcessStream().readFloat(entity1 + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecViewOffset[0]") + MemoryStream.FLOAT_SZ * 2);
		final float[] localOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localOrigin.length).put(Reliant.instance.getProcessStream().read(entity1 + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * localOrigin.length)).rewind()).asFloatBuffer().get(localOrigin);
		final float hypotenuse = distance(localOrigin[0], localOrigin[1], localOrigin[2], entityBone[0], entityBone[1], entityBone[2]);
		final float deltaX = localOrigin[0] - entityBone[0], deltaZ = localOrigin[1] - entityBone[1];
		double sinVal = (localOrigin[2] - entityBone[2]) / (hypotenuse == 0.0F ? 1.0F : hypotenuse);
		if (Math.abs(sinVal) > 1.0D) {
			sinVal = 0.0D;
		}
		final float[] viewAngles = new float[2], newAngles = new float[] {(float) Math.toDegrees(Math.asin(sinVal)), (float) Math.toDegrees(deltaX == 0.0F ? 0.0F : Math.atan(deltaZ / deltaX))};
		if (GameCache.getClientState() == MemoryStream.NULL) {
			throw new InvalidDataException("client_state");
		}
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * viewAngles.length).put(Reliant.instance.getProcessStream().read(GameCache.getClientState() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwViewAngles"), MemoryStream.FLOAT_SZ * viewAngles.length)).rewind()).asFloatBuffer().get(viewAngles);
		if (deltaX >= 0.0F) {
			newAngles[1] += MAX_FOV;
		}
		clampAngles(newAngles, SdkUtils.MAX_PLAYER_PITCH);
		float[] viewDiff = new float[] {newAngles[0] - viewAngles[0], newAngles[1] - viewAngles[1]};
		clampAngles(viewDiff, SdkUtils.MAX_PLAYER_PITCH);
		return viewDiff;
	}
	public static float clamp360Degrees(final float angle) {
		return ((angle % 360.0F) + 360.0F) % 360.0F;
	}
	/*public static Vector anglesVecTranspose(final float[] angles) {
		final float sinPitch = (float) Math.sin(Math.toRadians(angles[0])), sinYaw = (float) Math.sin(Math.toRadians(angles[1])), sinRoll = (float) Math.sin(Math.toRadians(angles[2])), cosPitch = (float) Math.cos(Math.toRadians(angles[0])), cosYaw = (float) Math.cos(Math.toRadians(angles[1])), cosRoll = (float) Math.cos(Math.toRadians(angles[2]));
		return new Vector(cosPitch * cosYaw, sinRoll * sinPitch * cosYaw + cosRoll * -sinYaw, cosRoll * sinPitch * cosYaw + -sinRoll * -sinYaw);
	}*/
	public static boolean faceEntity(final int entity, final int boneId) {
		return faceEntity(entity, boneId, MAX_FOV * 2, 0.0F, 0.0F, MAX_FOV, SdkUtils.MAX_PLAYER_PITCH);
	}
	public static boolean faceEntity(final int entity, final int boneId, final float fov, final float yawOffset, final float pitchOffset, final float yawIncrement, final float pitchIncrement) {
		if (!SdkUtils.entityAttackable(entity)) {
			return false;
		}
		final float[] entityBone = SdkUtils.entityBone(entity, boneId);
		if (entityBone == null || !SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return false;
		}
		entityBone[2] -= Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecViewOffset[0]") + MemoryStream.FLOAT_SZ * 2);
		final float[] localOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localOrigin.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * localOrigin.length)).rewind()).asFloatBuffer().get(localOrigin);
		final float hypotenuse = distance(localOrigin[0], localOrigin[1], localOrigin[2], entityBone[0], entityBone[1], entityBone[2]);
		final float deltaX = localOrigin[0] - entityBone[0], deltaZ = localOrigin[1] - entityBone[1];
		double sinVal = (localOrigin[2] - entityBone[2]) / (hypotenuse == 0.0F ? 1.0F : hypotenuse);
		if (Math.abs(sinVal) > 1.0D) {
			sinVal = 0.0D;
		}
		final float[] viewAngles = new float[2], newAngles = new float[] {(float) Math.toDegrees(Math.asin(sinVal)), (float) Math.toDegrees(deltaX == 0.0F ? 0.0F : Math.atan(deltaZ / deltaX))};
		if (GameCache.getClientState() == MemoryStream.NULL) {
			throw new InvalidDataException("client_state");
		}
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * viewAngles.length).put(Reliant.instance.getProcessStream().read(GameCache.getClientState() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwViewAngles"), MemoryStream.FLOAT_SZ * viewAngles.length)).rewind()).asFloatBuffer().get(viewAngles);
		if (deltaX >= 0.0F) {
			newAngles[1] += MAX_FOV;
		}
		newAngles[0] += pitchOffset;
		newAngles[1] += yawOffset;
		/*if (Reliant.instance.antiOverwatch()) {
			final float RAND_MULT = 1.0F / 2.0F;
			newAngles[0] += (Reliant.instance.getOverwatchRand().nextBoolean() ? Reliant.instance.getOverwatchRand().nextFloat() : -Reliant.instance.getOverwatchRand().nextFloat()) * RAND_MULT;
			newAngles[1] += (Reliant.instance.getOverwatchRand().nextBoolean() ? Reliant.instance.getOverwatchRand().nextFloat() : -Reliant.instance.getOverwatchRand().nextFloat()) * RAND_MULT;
		}*/
		clampAngles(newAngles, SdkUtils.MAX_PLAYER_PITCH);
		float[] viewDiff = new float[] {newAngles[0] - viewAngles[0], newAngles[1] - viewAngles[1]};
		clampAngles(viewDiff, SdkUtils.MAX_PLAYER_PITCH);
		//final float MAX_OVERWATCH_DIFF = 29.0F;
		final float actualPitchDiff = /*Reliant.instance.antiOverwatch() ? (Math.abs(viewDiff[0]) > MAX_OVERWATCH_DIFF ? viewDiff[0] < 0.0F ? -MAX_OVERWATCH_DIFF : MAX_OVERWATCH_DIFF : viewDiff[0]) * Reliant.instance.getOverwatchRand().nextFloat() : */Math.abs(viewDiff[0]) > pitchIncrement ? viewDiff[0] < 0.0F ? -pitchIncrement : pitchIncrement : viewDiff[0];
		viewAngles[0] += actualPitchDiff;
		final float maxYawDiff = /*Reliant.instance.antiOverwatch() ? MAX_OVERWATCH_DIFF : */yawIncrement; // Eric: Actual number is 45 degrees. But, we are going to use 44 just for in case.
		final float actualYawDiff = /*((*//* || Reliant.instance.antiOverwatch()) && */Math.abs(viewDiff[1]) > maxYawDiff ? viewDiff[1] < 0.0F ? -maxYawDiff : maxYawDiff : viewDiff[1]/*)/* * (Reliant.instance.antiOverwatch() ? Reliant.instance.getOverwatchRand().nextFloat() : 1.0F)*/;
		viewAngles[1] += actualYawDiff;
		final RcsRoutine rcs = (RcsRoutine) Reliant.instance.getRoutineRegistry().get("RCS");
		final float[] modPunch = rcs.modPunch();
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return false;
		}
		final float[] vecPunch = new float[2];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * vecPunch.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecPunch"), MemoryStream.FLOAT_SZ * vecPunch.length)).rewind()).asFloatBuffer().get(vecPunch);
		// Eric: Fixes an RCS conflict that prevents aim smoothing from working correctly
		final float deltaPitch = viewDiff[0] - actualPitchDiff, deltaYaw = viewDiff[1] - actualYawDiff;
		final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
		if (weaponEntity == MemoryStream.NULL) {
			throw new InvalidDataException("weapon_entity");
		}
		final int weaponIdx = Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"));
		final long modAddr = Reliant.instance.getProcessStream().moduleAddress(Reliant.instance.isLegacyToolchain() ? "client.dll" : "engine.dll");
		if (modAddr == MemoryStream.NULL) {
			throw new InvalidDataException("mod_addr");
		}
		final long globalVars = Reliant.instance.isLegacyToolchain() ? Reliant.instance.getProcessStream().readInt(modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars")) : modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars");
		if (globalVars == MemoryStream.NULL) {
			throw new InvalidDataException("global_vars");
		}
		if (Math.sqrt(viewDiff[0] * viewDiff[0] + viewDiff[1] * viewDiff[1]) > fov && !(rcs.isEnabled() && ((vecPunch[0] != 0.0F || vecPunch[1] != 0.0F) && (Math.abs(deltaPitch) < EQUALS_PRECISION || Math.abs(viewDiff[0]) <= Math.abs(vecPunch[0]) + EQUALS_PRECISION) && (Math.abs(deltaYaw) < EQUALS_PRECISION || Math.abs(viewDiff[1]) <= Math.abs(vecPunch[1]) + EQUALS_PRECISION) && Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iShotsFired")) >= 0 && (!rcs.getBoolean("Next Attack") || (Item.automatic(weaponIdx) || !Item.boltAction(weaponIdx) && !Item.shotgun(weaponIdx)) && !GameCache.isReloading() || Reliant.instance.getProcessStream().readFloat(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flNextPrimaryAttack")) - Reliant.instance.getProcessStream().readFloat(globalVars + MemoryStream.FLOAT_SZ + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * 2) <= 0.0F)))) {
			return false;
		}
		if (rcs.isEnabled()) {
			if ((modPunch[0] != 0.0F || modPunch[1] != 0.0F) && (Math.abs(deltaPitch) < EQUALS_PRECISION || Math.abs(viewDiff[0]) <= Math.abs(modPunch[0]) + SdkUtils.VEC_EQUALS_DIST) && (Math.abs(deltaYaw) < EQUALS_PRECISION || Math.abs(viewDiff[1]) <= Math.abs(modPunch[1]) + SdkUtils.VEC_EQUALS_DIST) && Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iShotsFired")) >= 0 && (!rcs.getBoolean("Next Attack") || (Item.automatic(weaponIdx) || !Item.boltAction(weaponIdx) && !Item.shotgun(weaponIdx)) && !GameCache.isReloading() || Reliant.instance.getProcessStream().readFloat(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flNextPrimaryAttack")) - Reliant.instance.getProcessStream().readFloat(globalVars + MemoryStream.FLOAT_SZ + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * 2) <= 0.0F)) {
				if (modPunch[0] != 0.0F) {
					viewAngles[0] -= modPunch[0] - deltaPitch;
				}
				if (modPunch[1] != 0.0F) {
					viewAngles[1] -= modPunch[1] - deltaYaw;
				}
			}
			rcs.setPrevPunch(modPunch);
		}
		/*if (Reliant.instance.getProcessStream().write(GameCache.getClientState() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwViewAngles"), NioUtils.createBuffer(MemoryStream.FLOAT_SZ * 2).putFloat(viewAngles[0]).putFloat(viewAngles[1]).array())) {
			if ((Boolean) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Disable Mouse").getValue()) {
				Reliant.instance.getProcessStream().disableMouse();
			}
			return true;
		}
		return false;*/
		return SdkUtils.writeViewAngles(viewAngles);
	}
	/*public static float[] faceAngles(final int entity) {
		final int boneMatrix = Reliant.instance.getProcessStream().readInt(entity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwBoneMatrix"));
		if (boneMatrix == MemoryStream.NULL) {
			return null;
		}
		float[][] entityBone = new float[3][4];
		for (int i = 0; i < entityBone.length; i++) {
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * entityBone[i].length).put(Reliant.instance.getProcessStream().read(boneMatrix + 0x30 * 8 + MemoryStream.FLOAT_SZ * entityBone[i].length * i, MemoryStream.FLOAT_SZ * entityBone[i].length)).rewind()).asFloatBuffer().get(entityBone[i]);
		}
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return null;
		}
		entityBone[2][3] -= Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecViewOffset[0]") + MemoryStream.FLOAT_SZ * 2);
		final float[] localOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localOrigin.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * localOrigin.length)).rewind()).asFloatBuffer().get(localOrigin);
		final float hypotenuse = distance(localOrigin[0], localOrigin[1], localOrigin[2], entityBone[0][3], entityBone[1][3], entityBone[2][3]);
		final float deltaX = localOrigin[0] - entityBone[0][3], deltaZ = localOrigin[1] - entityBone[1][3];
		double sinVal = (localOrigin[2] - entityBone[2][3]) / (hypotenuse == 0.0F ? 1.0F : hypotenuse);
		if (Math.abs(sinVal) > 1.0D) {
			sinVal = 0.0D;
		}
		final float[] newAngles = new float[] {(float) Math.toDegrees(Math.asin(sinVal)), (float) Math.toDegrees(deltaX == 0.0F ? 0.0F : Math.atan(deltaZ / deltaX))};
		if (deltaX >= 0.0F) {
			newAngles[1] += MAX_FOV;
		}
		clampAngles(newAngles);
		return newAngles;
	}*/
	public static float findShortestArc(float src, float dst) {
		src = clamp360Degrees(src);
		dst = clamp360Degrees(dst);
	    if (Math.abs(dst - src) < MAX_FOV)
	        return dst - src;
	    if (dst > src)
	        return dst - src - MAX_FOV * 2.0F;
	    return dst - src + MAX_FOV * 2.0F;
	}
}
