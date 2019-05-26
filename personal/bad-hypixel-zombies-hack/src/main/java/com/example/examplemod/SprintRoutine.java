package com.example.examplemod;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public final class SprintRoutine extends Routine {

	SprintRoutine() {
		super("Sprint", Keyboard.KEY_R, true);
		// TODO Auto-generated constructor stub
	}

	void preUpdate() {
		if (!Minecraft.getMinecraft().thePlayer.isSprinting() && Minecraft.getMinecraft().thePlayer.movementInput.moveForward > 0.0F && Minecraft.getMinecraft().thePlayer.getFoodStats().getFoodLevel() > 6.0F) {
			Minecraft.getMinecraft().thePlayer.setSprinting(true);
		}
	}
}
