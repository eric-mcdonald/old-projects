package org.bitbucket.reliant.routine.impl;

import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.memory.WriteMemoryException;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.SdkUtils;

public final class RcsRoutine extends DisplayRoutine {
	//private static final float MAX_RAND = 1.0F, MIN_RAND = 0.8F;
	private float[] prevPunch = new float[2];
	
	public RcsRoutine() {
		super("RCS", "Controls your weapon's recoil.", true, true, 7, new BoolOption("Next Attack", "Specifies whether or not to wait until the player can attack again.", false), new FloatOption("X-multiplier", "Specifies how much to multiply the recoil's x-coordinate by.", new ClampedNumber<Float>(2.0F, -10.0F, 10.0F), 1.0F), new FloatOption("Y-multiplier", "Specifies how much to multiply the recoil's y-coordinate by.", new ClampedNumber<Float>(2.0F, -10.0F, 10.0F), 1.0F));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFFBF00;
	}
	public float[] modPunch() {
		final float[] vecPunch = new float[2];
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return vecPunch;
		}
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * vecPunch.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecPunch"), MemoryStream.FLOAT_SZ * vecPunch.length)).rewind()).asFloatBuffer().get(vecPunch);
		/*float xMult = getFloat("X-multiplier"), yMult = getFloat("Y-multiplier");
		if (Reliant.instance.antiOverwatch()) {
			xMult *= Reliant.instance.getOverwatchRand().nextFloat() * (MAX_RAND - MIN_RAND) + MIN_RAND;
			yMult *= Reliant.instance.getOverwatchRand().nextFloat() * (MAX_RAND - MIN_RAND) + MIN_RAND;
		}*/
		/*vecPunch[0] *= xMult;
		vecPunch[1] *= yMult;*/
		vecPunch[0] *= getFloat("X-multiplier");
		vecPunch[1] *= getFloat("Y-multiplier");
		return vecPunch;
	}
	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		prevPunch = new float[2];
	}

	public void setPrevPunch(final float[] prevPunch) {
		this.prevPunch = prevPunch;
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post || !SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return;
		}
		float xMult = getFloat("X-multiplier"), yMult = getFloat("Y-multiplier");
		if (xMult == 1.0F && yMult == 1.0F) {
			reset(false);
			return; // Eric: Exit for efficiency
		}
		/*if (Reliant.instance.antiOverwatch()) {
			xMult *= Reliant.instance.getOverwatchRand().nextFloat() * (MAX_RAND - MIN_RAND) + MIN_RAND;
			yMult *= Reliant.instance.getOverwatchRand().nextFloat() * (MAX_RAND - MIN_RAND) + MIN_RAND;
		}*/
		final float[] vecPunch = new float[2];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * vecPunch.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecPunch"), MemoryStream.FLOAT_SZ * vecPunch.length)).rewind()).asFloatBuffer().get(vecPunch);
		vecPunch[0] *= xMult;
		vecPunch[1] *= yMult;
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
		if (Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iShotsFired")) >= 0 && (!getBoolean("Next Attack") || (Item.automatic(weaponIdx) || !Item.boltAction(weaponIdx) && !Item.shotgun(weaponIdx)) && !GameCache.isReloading() || Reliant.instance.getProcessStream().readFloat(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flNextPrimaryAttack")) - Reliant.instance.getProcessStream().readFloat(globalVars + MemoryStream.FLOAT_SZ + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * 2) <= 0.0F)) {
			final float[] viewAngles = new float[2];
			if (GameCache.getClientState() == MemoryStream.NULL) {
				throw new InvalidDataException("client_state");
			}
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * viewAngles.length).put(Reliant.instance.getProcessStream().read(GameCache.getClientState() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwViewAngles"), MemoryStream.FLOAT_SZ * viewAngles.length)).rewind()).asFloatBuffer().get(viewAngles);
			final float deltaX = vecPunch[0] - prevPunch[0], deltaY = vecPunch[1] - prevPunch[1];
			if (deltaX == 0.0F && deltaY == 0.0F) {
				return;
			}
			viewAngles[0] -= deltaX;
			viewAngles[1] -= deltaY;
			if (!SdkUtils.writeViewAngles(viewAngles)) {
				throw new WriteMemoryException("view_angles", viewAngles, WriteMemoryException.NO_ADDR);
			}
		}
		prevPunch = vecPunch;
	}
}
