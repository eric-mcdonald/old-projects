package com.example.examplemod;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public final class BunnyhopRoutine extends Routine {
	BunnyhopRoutine() {
		super("Bunny-hop", Keyboard.KEY_J, true);
		// TODO Auto-generated constructor stub
	}

	void preUpdate() {
		if (Minecraft.getMinecraft().thePlayer.moveForward > 0.0F && Minecraft.getMinecraft().thePlayer.onGround && !Minecraft.getMinecraft().thePlayer.isCollidedHorizontally && Minecraft.getMinecraft().thePlayer.motionY < 0.0F && !Minecraft.getMinecraft().thePlayer.isInLava() && !Minecraft.getMinecraft().thePlayer.isInWater() && Minecraft.getMinecraft().thePlayer.worldObj.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().addCoord(Minecraft.getMinecraft().thePlayer.motionX, 0.42D, Minecraft.getMinecraft().thePlayer.motionZ)).isEmpty() && !Minecraft.getMinecraft().thePlayer.worldObj.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(Minecraft.getMinecraft().thePlayer.motionX, 0.42D, Minecraft.getMinecraft().thePlayer.motionZ).addCoord(0.0D, -Minecraft.getMinecraft().thePlayer.getMaxFallHeight(), 0.0D)).isEmpty() && !GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump)) {
			Minecraft.getMinecraft().thePlayer.jump();
		}
	}
}
