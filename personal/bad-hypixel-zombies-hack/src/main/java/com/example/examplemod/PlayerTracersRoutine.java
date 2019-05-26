package com.example.examplemod;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import static org.lwjgl.opengl.GL11.*;

public final class PlayerTracersRoutine extends Routine {

	PlayerTracersRoutine() {
		super("Player Tracers", Keyboard.KEY_BACK, false);
		// TODO Auto-generated constructor stub
	}

	private static double interpolate(final double value, final double prevValue, final float partialTicks) {
		return prevValue + (value - prevValue) * partialTicks;
	}
	void render3d(final float partialTicks) {
		Minecraft.getMinecraft().gameSettings.viewBobbing = false;
		final boolean lighting = glIsEnabled(GL_LIGHTING), texture2d = glIsEnabled(GL_TEXTURE_2D), alphaTest = glIsEnabled(GL_ALPHA_TEST), depthTest = glIsEnabled(GL_DEPTH_TEST), blend = glIsEnabled(GL_BLEND);
		if (lighting)
			glDisable(GL_LIGHTING);
		if (texture2d)
			glDisable(GL_TEXTURE_2D);
		if (alphaTest)
			glDisable(GL_ALPHA_TEST);
		if (depthTest)
			glDisable(GL_DEPTH_TEST);
		if (blend)
			glDisable(GL_BLEND);
		glLineWidth(1.5F);
		glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
		final double localX = interpolate(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.lastTickPosX, partialTicks), localY = interpolate(Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.lastTickPosY, partialTicks), localZ = interpolate(Minecraft.getMinecraft().thePlayer.posZ, Minecraft.getMinecraft().thePlayer.lastTickPosZ, partialTicks);
		for (final EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {
			if (player instanceof EntityPlayerSP || player.isInvisible()) {
				continue;
			}
			glBegin(GL_LINE_STRIP);
			glVertex3f(0.0F, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0F);
			final double playerX = interpolate(player.posX, player.lastTickPosX, partialTicks), playerY = interpolate(player.posY, player.lastTickPosY, partialTicks), playerZ = interpolate(player.posZ, player.lastTickPosZ, partialTicks);
			glVertex3d(-localX + playerX, -localY + playerY, -localZ + playerZ);
			glVertex3d(-localX + playerX, -localY + playerY + player.getEyeHeight(), -localZ + playerZ);
			glEnd();
		}
		if (blend)
			glEnable(GL_BLEND);
		if (depthTest)
			glEnable(GL_DEPTH_TEST);
		if (alphaTest)
			glEnable(GL_ALPHA_TEST);
		if (texture2d)
			glEnable(GL_TEXTURE_2D);
		if (lighting)
			glEnable(GL_LIGHTING);
	}
}
