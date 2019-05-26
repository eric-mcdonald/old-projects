package org.bitbucket.lanius.test;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.ReflectHelper;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class TestRoutine extends Routine {

	private static final double INIT_SPEED = 0.0D;

	private static final int INIT_STATE = 0;

	private double moveSpeed;

	private int state;

	public TestRoutine() {
		super(Keyboard.KEY_NONE, false);
		// TODO Auto-generated constructor stub
	}

	private double baseSpeed(final EntityLivingBase entityLiving) {
		double speed = 0.221D;
		if (entityLiving.isSprinting()) {
			speed *= 1.30000002D;
		}
		if (entityLiving.isPotionActive(MobEffects.SPEED)) {
			speed *= 1.0D + 0.2D * (entityLiving.getActivePotionEffect(MobEffects.SPEED).getAmplifier() + 1);
		}
		return speed;
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return TEST_COLOR;
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "This is a routine for testing code. The user should not be able to use this.";
	}

	@Override
	public void init() {
		moveSpeed = INIT_SPEED;
		setState();
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Test";
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP) {
			return;
		}
		Lanius.mc.player.movementInput.updatePlayerMoveState(); // Eric:
																// EntityLivingBase#moveForward
																// and
																// EntityLivingBase#moveStrafing
																// only go
																// up to
																// 0.98
		final double NO_MOVE = 0.0D;
		final boolean hMove = Lanius.mc.player.movementInput.moveForward != NO_MOVE
				|| Lanius.mc.player.movementInput.moveStrafe != NO_MOVE;
		entityLiving.setSprinting(hMove && Lanius.mc.player.getFoodStats().getFoodLevel() > 6.0F);
		final double baseSpeed = baseSpeed(entityLiving);
		if (moveSpeed == INIT_SPEED) {
			moveSpeed = baseSpeed;
		}
		if (hMove && entityLiving.onGround && state == INIT_STATE) {
			if (Lanius.mc.gameSettings.keyBindJump.isKeyDown()) {
				ReflectHelper.setValue(EntityLivingBase.class, entityLiving, 0, "field_70773_bE", "jumpTicks");
			} else {
				final double prevMotX = entityLiving.motionX, prevMotZ = entityLiving.motionZ;
				Lanius.mc.player.jump();
				entityLiving.motionY -= 0.02D;
				entityLiving.motionX = prevMotX;
				entityLiving.motionZ = prevMotZ;
				moveSpeed = 2.14D * baseSpeed;
			}
			++state;
		} else if (!entityLiving.onGround && state == 1) {
			final double newSpeed = -(0.66D * (moveSpeed - baseSpeed) - moveSpeed);
			if (newSpeed > baseSpeed) {
				moveSpeed = newSpeed;
			}
			++state;
		} else if (!entityLiving.onGround && entityLiving.motionY < 0.0D
				&& !entityLiving.world
						.getCollisionBoxes(entityLiving,
								entityLiving.getEntityBoundingBox().offset(NO_MOVE, entityLiving.motionY, NO_MOVE))
						.isEmpty()
				&& state == 2) {
			moveSpeed = 1.34D * baseSpeed;
			setState();
		} else if (!entityLiving.onGround && state == 2) {
			moveSpeed /= 1.09D;
		}
		entityLiving.motionX = entityLiving.motionZ = NO_MOVE;
		entityLiving.moveRelative(Lanius.mc.player.movementInput.moveStrafe, 0.0F,
				Lanius.mc.player.movementInput.moveForward, (float) moveSpeed);
	}

	private void setState() {
		state = INIT_STATE;
	}

}
