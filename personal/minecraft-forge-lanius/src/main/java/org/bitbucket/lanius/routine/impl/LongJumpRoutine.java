package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class LongJumpRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private boolean jumped;

	private float receivedVel;

	public LongJumpRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.MOVEMENT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Makes the player jump far.";
	}

	@Override
	public String displayData() {
		final int flooredVel = MathHelper.floor(receivedVel);
		return flooredVel == receivedVel ? String.valueOf(flooredVel) : String.format("%,.1f", receivedVel);
	}

	@Override
	public void init() {
		receivedVel = 0.0F;
		jumped = false;
	}

	boolean isJumped() {
		return jumped;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Long Jump";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (data.retVal instanceof SPacketRespawn) {
			init();
		}
		if (!isEnabled() || !RoutineUtils.ncpEnabled() || !phase.equals(Phase.START)
				|| !(data.retVal instanceof SPacketEntityVelocity || data.retVal instanceof SPacketExplosion)) {
			return;
		}
		double velocityX = 0.0F, velocityZ = 0.0F;
		if (data.retVal instanceof SPacketEntityVelocity) {
			final SPacketEntityVelocity entityVelPacket = (SPacketEntityVelocity) data.retVal;
			final Entity entity = Lanius.mc.world.getEntityByID(entityVelPacket.getEntityID());
			if (entity == null || !entity.equals(Lanius.mc.player)) {
				return;
			}
			velocityX = entityVelPacket.getMotionX() / 8000.0F;
			velocityZ = entityVelPacket.getMotionZ() / 8000.0F;

		} else if (data.retVal instanceof SPacketExplosion) {
			final SPacketExplosion explosionPacket = (SPacketExplosion) data.retVal;
			velocityX = explosionPacket.getMotionX();
			velocityZ = explosionPacket.getMotionZ();
		}
		receivedVel += MathHelper.sqrt(velocityX * velocityX + velocityZ * velocityZ);
		data.retVal = null;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingUpdateHighest(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP
				|| Lanius.mc.player.movementInput == null) {
			return;
		}
		if (entityLiving.onGround) {
			jumped = false;
		}
		final Material predictMat = entityLiving.world
				.getBlockState(new BlockPos(entityLiving.posX + entityLiving.motionX,
						entityLiving.posY - JesusRoutine.BLOCK_OFF + entityLiving.motionY,
						entityLiving.posZ + entityLiving.motionZ))
				.getMaterial(),
				posMat = entityLiving.world.getBlockState(
						new BlockPos(entityLiving.posX, entityLiving.posY - JesusRoutine.BLOCK_OFF, entityLiving.posZ))
						.getMaterial();
		final float multiplier = getFloat("Multiplier").floatValue();
		if (Lanius.mc.player.movementInput.moveForward > 0.0F && entityLiving.onGround && !entityLiving.isInWater()
				&& !entityLiving.isInLava()
				&& !(Boolean) ObfuscationReflectionHelper.getPrivateValue(Entity.class, entityLiving, "field_70134_J",
						"isInWeb")
				&& (!RoutineUtils.ncpEnabled() || !(RoutineUtils.enabled("Jesus") && (posMat == Material.WATER
						|| posMat == Material.LAVA || predictMat == Material.WATER || predictMat == Material.LAVA))
						&& receivedVel >= multiplier - 1.0F)) {
			entityLiving.motionX = entityLiving.motionZ = 0.0D;
			entityLiving.setSprinting(true);
			entityLiving.moveRelative(entityLiving.moveStrafing, 0.0F, entityLiving.moveForward,
					(float) ((SpeedRoutine) Lanius.getInstance().getRoutineRegistry().get("Speed"))
							.baseSpeed(entityLiving));
			Lanius.mc.player.jump();
			entityLiving.motionX *= multiplier;
			entityLiving.motionZ *= multiplier;
			receivedVel = 0.0F;
			jumped = true;
		}
	}

	@Override
	public void registerValues() {
		registerValue("Multiplier", 9.0F, 1.1F, 9.0F, "Specifies the multiplier for the player's velocity.");
	}

}
