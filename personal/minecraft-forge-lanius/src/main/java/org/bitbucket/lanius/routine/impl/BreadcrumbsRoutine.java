package org.bitbucket.lanius.routine.impl;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_LINE_WIDTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glIsEnabled;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Timer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class BreadcrumbsRoutine extends TabbedRoutine {

	private final List<double[]> positions = new ArrayList<double[]>();

	public BreadcrumbsRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.RENDER);
		// TODO Auto-generated constructor stub
	}

	public void clearPositions() {
		positions.clear();
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Draws a line from the player.";
	}

	@Override
	public String displayData() {
		return String.valueOf(positions.size());
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Breadcrumbs";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || Lanius.mc.isGamePaused()) {
			return;
		}
		final float partialTicks = ((Timer) ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Lanius.mc,
				"field_71428_T", "timer")).renderPartialTicks;
		final double[] position = new double[] {
				MathHelper.interpolate(Lanius.mc.player.posX, Lanius.mc.player.lastTickPosX, partialTicks),
				MathHelper.interpolate(Lanius.mc.player.posY, Lanius.mc.player.lastTickPosY, partialTicks),
				MathHelper.interpolate(Lanius.mc.player.posZ, Lanius.mc.player.lastTickPosZ, partialTicks) };
		if (positions.isEmpty()) {
			positions.add(position);
		} else {
			final double[] lastPos = positions.get(positions.size() - 1);
			if (MathHelper.distance(lastPos[0], lastPos[1], lastPos[2], position[0], position[1],
					position[2]) >= 3.0D) {
				positions.add(position);
			}
		}
	}

	@SubscribeEvent
	public void onRenderWorldLast(final RenderWorldLastEvent renderWorldLastEv) {
		final Entity viewEntity = Lanius.mc.getRenderViewEntity();
		if (!isEnabled() || !(viewEntity instanceof EntityLivingBase)) {
			return;
		}
		final boolean textureEnabled = glIsEnabled(GL_TEXTURE_2D), lightEnabled = glIsEnabled(GL_LIGHTING),
				depthEnabled = glIsEnabled(GL_DEPTH_TEST), disableDepth = getBoolean("Disable Depth"),
				smoothEnabled = glIsEnabled(GL_LINE_SMOOTH), antiAliasing = getBoolean("Anti-aliasing");
		if (textureEnabled) {
			GlStateManager.disableTexture2D();
		}
		if (lightEnabled) {
			GlStateManager.disableLighting();
		}
		if (depthEnabled && disableDepth) {
			GlStateManager.disableDepth();
		}
		if (!smoothEnabled && antiAliasing) {
			glEnable(GL_LINE_SMOOTH);
		}
		final float prevLineWidth = glGetFloat(GL_LINE_WIDTH);
		GlStateManager.glLineWidth(getFloat("Line Width").floatValue());
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder worldRenderer = tessellator.getBuffer();
		final Color renderColor = new Color(0x5882FA);
		final float red = renderColor.getRed() / 255.0F, green = renderColor.getGreen() / 255.0F,
				blue = renderColor.getBlue() / 255.0F;
		worldRenderer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		final float partialTicks = renderWorldLastEv.getPartialTicks();
		final double viewX = MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX, partialTicks),
				viewY = MathHelper.interpolate(viewEntity.posY, viewEntity.lastTickPosY, partialTicks),
				viewZ = MathHelper.interpolate(viewEntity.posZ, viewEntity.lastTickPosZ, partialTicks);
		final double Y_OFF = 0.1D;
		for (final double[] position : positions) {
			if (viewEntity.getDistance(position[0], position[1], position[2]) > 24.0D) {
				continue;
			}
			worldRenderer.pos(position[0] - viewX, position[1] - viewY + Y_OFF, position[2] - viewZ)
					.color(red, green, blue, 1.0F).endVertex();
		}
		worldRenderer.pos(0.0D, Y_OFF, 0.0D).color(red, green, blue, 1.0F).endVertex();
		tessellator.draw();
		GlStateManager.glLineWidth(prevLineWidth);
		if (!smoothEnabled && antiAliasing) {
			glDisable(GL_LINE_SMOOTH);
		}
		if (depthEnabled && disableDepth) {
			GlStateManager.enableDepth();
		}
		if (lightEnabled) {
			GlStateManager.enableLighting();
		}
		if (textureEnabled) {
			GlStateManager.enableTexture2D();
		}
	}

	@Override
	@SubscribeEvent
	public void onUnload(final WorldEvent.Unload unloadEv) {
		super.onUnload(unloadEv);
		clearPositions();
	}

	@Override
	public void registerValues() {
		registerValue("Anti-aliasing", true, "Determines whether or not to enable line smoothing.");
		registerValue("Line Width", 1.0F, 0.5F, 3.0F, "Determines the thickness of the breadcrumb lines.");
		registerValue("Disable Depth", true, "Determines whether or not to disable depth testing.");
	}

	public int removePositions(int amount) {
		amount = Math.min(amount, positions.size());
		for (int posIdx = 0; posIdx < amount; posIdx++) {
			positions.remove(0);
		}
		return amount;
	}

	@Override
	public void setEnabled() {
		super.setEnabled();
		if (!isEnabled()) {
			MinecraftForge.EVENT_BUS.register(this); // Eric: re-add the
														// listener to continue
														// tracking positions
		}
	}

}
