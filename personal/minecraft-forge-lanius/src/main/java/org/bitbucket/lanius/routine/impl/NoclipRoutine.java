package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class NoclipRoutine extends TabbedRoutine {

	private boolean phased;

	private int state;

	public NoclipRoutine() {
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
		return "Allows the player to move freely through blocks.";
	}

	public boolean executing() {
		return state > 0 || phased;
	}

	@Override
	public void init() {
		state = 0;
		if (Lanius.mc.player != null) {
			Lanius.mc.player.noClip = false;
		}
		phased = false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Noclip";
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP
				|| Lanius.mc.player.movementInput == null) {
			return;
		}
		phased = false;
		final boolean ncpEnabled = RoutineUtils.ncpEnabled(),
				inBlock = entityLiving.isEntityInsideOpaqueBlock() && !ncpEnabled;
		final long worldTime = entityLiving.world.getWorldTime();
		final MovementInput movementIn = Lanius.mc.player.movementInput;
		final float prevStrafe = movementIn.moveStrafe, prevForward = movementIn.moveForward;
		final boolean prevForwardDown = movementIn.forwardKeyDown, prevBackDown = movementIn.backKeyDown,
				prevLeftDown = movementIn.leftKeyDown, prevRightDown = movementIn.rightKeyDown,
				prevJump = movementIn.jump, prevSneak = movementIn.sneak;
		movementIn.updatePlayerMoveState();
		final boolean threeDim = getBoolean("3D");
		final Vec3d lookVec = entityLiving.getLookVec();
		if (inBlock) {
			state = 1;
		} else if (entityLiving.isPlayerSleeping()) {
			wakeNoPacket();
			state = 2;
		} else if (state == 1 && !inBlock || state == 2 && (worldTime < 13000L || worldTime >= 24000L)) {
			init();
		} else if (state == 0
				&& MathHelper.sqrt(movementIn.moveForward * movementIn.moveForward
						+ movementIn.moveStrafe * movementIn.moveStrafe) > 0.0F
				&& Lanius.mc.player.capabilities.isCreativeMode && !ncpEnabled) {
			boolean cancel = true;
			final AxisAlignedBB entityBox = entityLiving.getEntityBoundingBox();
			final double prevMotX = entityLiving.motionX, prevMotY = entityLiving.motionY,
					prevMotZ = entityLiving.motionZ;
			final float INCREMENT = 0.05F;
			// Eric: recalculate the player's speed for block collision
			entityLiving.motionX = entityLiving.motionZ = 0.0D;
			final double baseSpeed = ((SpeedRoutine) Lanius.getInstance().getRoutineRegistry().get("Speed"))
					.baseSpeed(entityLiving);
			entityLiving.moveRelative(movementIn.moveStrafe, 0.0F, movementIn.moveForward, (float) baseSpeed);
			boolean firstLoop = true;
			while (!entityLiving.world
					.getCollisionBoxes(entityLiving,
							entityBox.offset(entityLiving.motionX, entityLiving.motionY, entityLiving.motionZ))
					.isEmpty()
					&& (!firstLoop
							|| !entityLiving.world
									.getCollisionBoxes(entityLiving,
											entityBox.offset(entityLiving.motionX, 0.0D, entityLiving.motionZ))
									.isEmpty())
					|| !firstLoop && Math.sqrt(entityLiving.motionX * entityLiving.motionX
							+ entityLiving.motionZ * entityLiving.motionZ) < baseSpeed) {
				if (cancel) {
					entityLiving.motionX = entityLiving.motionY = entityLiving.motionZ = 0.0D;
					cancel = false;
				} else if (MathHelper
						.sqrt(entityLiving.motionX * entityLiving.motionX + entityLiving.motionY * entityLiving.motionY
								+ entityLiving.motionZ * entityLiving.motionZ) > getFloat("Distance").floatValue()) {
					cancel = true;
					break;
				}
				entityLiving.moveRelative(movementIn.moveStrafe, 0.0F, movementIn.moveForward, INCREMENT);
				if (threeDim) {
					if (movementIn.moveForward > 0.0F) {
						entityLiving.motionY += lookVec.y * INCREMENT;
					} else if (movementIn.moveForward < 0.0F) {
						entityLiving.motionY += -lookVec.y * INCREMENT;
					}
				} else {
					entityLiving.motionY += movementIn.jump ? INCREMENT : movementIn.sneak ? -INCREMENT : 0.0D;
				}
				firstLoop = false;
			}
			if (cancel) {
				entityLiving.motionX = prevMotX;
				entityLiving.motionY = prevMotY;
				entityLiving.motionZ = prevMotZ;
				entityLiving.noClip = false;
			} else {
				entityLiving.noClip = true;
				entityLiving.move(MoverType.SELF, entityLiving.motionX, entityLiving.motionY, entityLiving.motionZ);
				entityLiving.motionX = entityLiving.motionY = entityLiving.motionZ = 0.0D;
				phased = true;
			}
		}
		if (executing() && !phased) {
			entityLiving.noClip = true;
			Lanius.mc.player.capabilities.isFlying = false;
			entityLiving.motionX = entityLiving.motionY = entityLiving.motionZ = 0.0D;
			if (movementIn.sneak) {
				movementIn.moveStrafe /= 0.3F;
				movementIn.moveForward /= 0.3F;
			}
			final float speed = getFloat("Speed").floatValue();
			entityLiving.moveRelative(movementIn.moveStrafe, 0.0F, movementIn.moveForward, speed);
			if (entityLiving.isSprinting()) {
				final double SPRINT_MULT = 1.30000001192092896D;
				entityLiving.motionX *= SPRINT_MULT;
				entityLiving.motionZ *= SPRINT_MULT;
			}
			if (threeDim) {
				if (movementIn.moveForward > 0.0F) {
					entityLiving.motionY = lookVec.y * speed;
				} else if (movementIn.moveForward < 0.0F) {
					entityLiving.motionY = -lookVec.y * speed;
				}
			} else {
				entityLiving.motionY = movementIn.jump ? speed : movementIn.sneak ? -speed : 0.0D;
			}
		}
		// Eric: hotfixes for double-tap issues
		movementIn.moveStrafe = prevStrafe;
		movementIn.moveForward = prevForward;
		movementIn.forwardKeyDown = prevForwardDown;
		movementIn.backKeyDown = prevBackDown;
		movementIn.leftKeyDown = prevLeftDown;
		movementIn.rightKeyDown = prevRightDown;
		movementIn.jump = prevJump;
		movementIn.sneak = prevSneak;
	}

	@Override
	public void registerValues() {
		registerValue("3D", false, "Determines whether or not to move the player based on their rotations.");
		registerValue("Speed", 0.6F, 0.1F, 6.0F, "Specifies the speed the player will noclip at.");
		registerValue("Distance", 10.0F, 0.1F, 10.0F,
				"Specifies the maximum distance that the player can noclip through in one move.");
	}

	void wakeNoPacket() {
		ReflectHelper.setValue(EntityPlayer.class, Lanius.mc.player, false, "field_71083_bS", "sleeping");
		ReflectHelper.setValue(EntityPlayer.class, Lanius.mc.player, 0, "field_71076_b", "sleepTimer");
	}
}
