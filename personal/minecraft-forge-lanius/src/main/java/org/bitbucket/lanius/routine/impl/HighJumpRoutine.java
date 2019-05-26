package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class HighJumpRoutine extends TabbedRoutine {

	public HighJumpRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.MOVEMENT);
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
		return "Makes the player jump higher.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "High Jump";
	}

	@SubscribeEvent
	public void onLivingJump(final LivingJumpEvent livingJumpEv) {
		final EntityLivingBase entityLiving = livingJumpEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP) {
			return;
		}
		entityLiving.motionY *= getFloat("Multiplier").floatValue();
	}

	@Override
	public void registerValues() {
		registerValue("Multiplier", 1.5F, 1.1F, 9.0F, "Specifies how much to multiply the player's jump height by.");
	}

}
