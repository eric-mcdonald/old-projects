package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class SpiderRoutine extends TabbedRoutine {

	private int prevNoGravity = -1;

	public SpiderRoutine() {
		super(Keyboard.KEY_P, false, Tab.MOVEMENT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Allows the player to climb up walls.";
	}

	@Override
	public void init() {
		if (prevNoGravity != -1 && Lanius.mc.player != null) {
			Lanius.mc.player.setNoGravity(prevNoGravity == 1);
		}
		prevNoGravity = -1;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Spider";
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onLivingUpdateLow(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP) {
			return;
		}
		final double H_VEC = 0.0625D;
		if (!entityLiving.isElytraFlying() && !Lanius.mc.player.capabilities.isFlying && !RoutineUtils.flyEnabled()
				&& !RoutineUtils.noclipEnabled() && !entityLiving.isOnLadder()
				&& !entityLiving.world
						.getCollisionBoxes(entityLiving, entityLiving.getEntityBoundingBox().grow(H_VEC, 0.0D, H_VEC))
						.isEmpty()) {
			final float speed = getFloat("Speed").floatValue();
			entityLiving.motionY = entityLiving.collidedHorizontally ? speed : -speed;
			if (entityLiving.isSneaking() && entityLiving.motionY < 0.0D) {
				entityLiving.motionY = 0.0D;
			}
			if (prevNoGravity == -1) {
				prevNoGravity = entityLiving.hasNoGravity() ? 1 : 0;
			}
			entityLiving.setNoGravity(true);
		} else if (prevNoGravity != -1) {
			entityLiving.setNoGravity(prevNoGravity == 1);
			prevNoGravity = -1;
		}
	}

	@Override
	public void registerValues() {
		registerValue("Speed", 0.2F, 0.1F, 2.0F, "Specifies the speed at which the player will climb.");
	}

}
