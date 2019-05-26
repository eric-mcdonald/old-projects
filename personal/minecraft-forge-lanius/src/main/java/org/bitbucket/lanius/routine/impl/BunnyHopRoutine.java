package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.CollisionUtils;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BunnyHopRoutine extends TabbedRoutine {

	public BunnyHopRoutine() {
		super(Keyboard.KEY_U, false, Tab.MOVEMENT);
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
		return "Makes the player jump to move faster.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Bunny Hop";
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent livingUpdateEv) {
		if (Lanius.mc.player == null || Lanius.mc.player.movementInput == null || RoutineUtils.enabled("Freecam")
				|| RoutineUtils.enabled("Speed")) {
			return;
		}
		EntityLivingBase entity = livingUpdateEv.getEntityLiving();
		if (!entity.equals(Lanius.mc.player) || entity instanceof EntityPlayerMP) {
			return;
		}
		Material predictMat = entity.world
				.getBlockState(new BlockPos(entity.posX + entity.motionX,
						entity.posY - JesusRoutine.BLOCK_OFF + entity.motionY, entity.posZ + entity.motionZ))
				.getMaterial(),
				posMat = entity.world
						.getBlockState(new BlockPos(entity.posX, entity.posY - JesusRoutine.BLOCK_OFF, entity.posZ))
						.getMaterial();
		if (!entity.collidedHorizontally && !Lanius.mc.player.capabilities.isFlying && !RoutineUtils.flyEnabled()
				&& (entity.onGround || !Lanius.mc.player.world.getCollisionBoxes(Lanius.mc.player,
						Lanius.mc.player.getEntityBoundingBox().offset(SpeedRoutine.NO_MOVE, SpeedRoutine.GROUND_OFF,
								SpeedRoutine.NO_MOVE))
						.isEmpty())
				&& entity.motionY < 0.0D && !CollisionUtils.collidesWall(entity, false) && !entity.isInWater()
				&& !entity.isInLava()
				&& !((Boolean) ObfuscationReflectionHelper.getPrivateValue(Entity.class, entity, "field_70134_J",
						"isInWeb"))
				&& entity.moveForward > 0.0F
				&& (!RoutineUtils.ncpEnabled() || Lanius.mc.player.capabilities.allowFlying
						|| !(RoutineUtils.enabled("Jesus") && (posMat == Material.WATER || posMat == Material.LAVA
								|| predictMat == Material.WATER || predictMat == Material.LAVA)))) {
			if (getBoolean("Sprint") && !entity.isSprinting() && (Lanius.mc.player.getFoodStats().getFoodLevel() > 6.0F
					|| Lanius.mc.player.capabilities.allowFlying)) {
				entity.setSprinting(true);
			}
			if (Lanius.mc.gameSettings.keyBindJump.isKeyDown()) {
				ReflectHelper.setValue(EntityLivingBase.class, entity, 0, "field_70773_bE", "jumpTicks");
			} else {
				((EntityPlayer) entity).jump();
			}
		}
	}

	@Override
	public void registerValues() {
		super.registerValues();
		registerValue("Sprint", true, "Determines whether or not to sprint.");
	}

}
