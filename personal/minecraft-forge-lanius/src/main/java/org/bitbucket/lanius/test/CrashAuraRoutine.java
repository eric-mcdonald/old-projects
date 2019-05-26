package org.bitbucket.lanius.test;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrashAuraRoutine extends TabbedRoutine {

	public CrashAuraRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.WORLD);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
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
		return "Test routine to see if attacking a falling block will crash the server.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Crash Aura Test";
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		final EntityLivingBase entityLiving = event.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP) {
			return;
		}
		for (Entity entity : Lanius.mc.world.loadedEntityList) {
			if (Lanius.mc.player.getDistance(entity) > (Lanius.mc.player.canEntityBeSeen(entity) ? 6.0F : 3.0F)
					|| !(entity instanceof EntityLeashKnot)) {
				continue;
			}
			Lanius.mc.playerController.attackEntity(Lanius.mc.player, entity);
			Lanius.mc.player.swingArm(EnumHand.MAIN_HAND);
		}
	}

}
