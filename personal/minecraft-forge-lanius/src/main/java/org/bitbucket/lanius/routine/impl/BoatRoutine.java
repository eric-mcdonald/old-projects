package org.bitbucket.lanius.routine.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class BoatRoutine extends TabbedRoutine {

	// Eric: the flight bypass was patched in 1.9...
	// private double oldX, oldY, oldZ;

	public BoatRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.MOVEMENT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Modifies boat movement while the player is riding one.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Boat";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.START) || Lanius.mc.player == null || Lanius.mc.isGamePaused()) {
			return;
		}
		final Entity ridingEntity = Lanius.mc.player.getRidingEntity();
		if (!(ridingEntity instanceof EntityBoat)) {
			return;
		}
		final boolean ncpEnabled = RoutineUtils.ncpEnabled(),
				keyDown = Keyboard.isKeyDown(Keyboard.getKeyIndex(getString("Down Key")));
		ridingEntity.motionX = ridingEntity.motionZ = 0.0D;
		if (Lanius.mc.player.movementInput.jump && !ncpEnabled || keyDown && !ncpEnabled || !ncpEnabled) {
			ridingEntity.motionY = 0.0D;
		}
		final double prevMotY = ridingEntity.motionY;
		try {
			ReflectionHelper.findMethod(EntityBoat.class, "updateMotion", "func_184450_w").invoke(ridingEntity);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ridingEntity.motionX = -ridingEntity.motionX;
		ridingEntity.motionY = Lanius.mc.player.movementInput.jump && !ncpEnabled || keyDown && !ncpEnabled
				|| !ncpEnabled ? -ridingEntity.motionY : prevMotY;
		ridingEntity.motionZ = -ridingEntity.motionZ;
		final float speed = getFloat("Speed").floatValue();
		ridingEntity.moveRelative(Lanius.mc.player.movementInput.moveStrafe, 0.0F,
				Lanius.mc.player.movementInput.moveForward, speed);
		if (Lanius.mc.player.movementInput.jump && !ncpEnabled) {
			ridingEntity.motionY += speed;
		} else if (keyDown && !ncpEnabled) {
			ridingEntity.motionY -= speed;
		}
	}

	@Override
	public void registerValues() {
		registerValue("Speed", 0.6F, 0.1F, 6.0F, "Specifies the speed at which the player will move while in a boat.");
		registerValue("Down Key", Keyboard.getKeyName(Keyboard.KEY_LMENU),
				"Specifies the key that should be used to make the player go down while boat flying.");
	}

}
