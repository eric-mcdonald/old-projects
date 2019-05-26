package org.bitbucket.lanius.routine.impl;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
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
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.OutlineModeData;
import org.bitbucket.lanius.routine.Renderable;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.RoutineUtils;
import org.bitbucket.lanius.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class TracersRoutine extends TabbedRoutine implements Hook<OutlineModeData>, Renderable {

	private static final float ALIAS_COLOR = 0.66666666666666666666666666666667F;
	private static final int TEAM_COLOR = 0x5882FA;

	public TracersRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.RENDER);
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
		return "Highlights loaded entities in memory.";
	}

	@Override
	public String displayData() {
		int entityCount = 0;
		for (final Entity entity : Lanius.mc.world.loadedEntityList) {
			if (validEntity(entity)) {
				++entityCount;
			}
		}
		return String.valueOf(entityCount);
	}

	@Override
	public void init() {
		if (Lanius.mc.player == null) {
			return;
		}
		final boolean outlines = Lanius.mc.player.isSpectator()
				&& Lanius.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown();
		for (final Entity entity : Lanius.mc.world.loadedEntityList) {
			if ((!outlines || !(entity instanceof EntityPlayer)) && entity.isGlowing()) {
				entity.setGlowing(false);
			}
		}
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Tracers";
	}

	@Override
	public void onExecute(final OutlineModeData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!isEnabled() || !getBoolean("Use Glow") || !validEntity(data.entity)
				|| !(data.entity instanceof EntityLivingBase)) {
			return;
		}
		final Color outlineCol;
		final EntityLivingBase entityLiving = (EntityLivingBase) data.entity;
		if (RoutineUtils.enabled("Name Protect")
				&& ((NameProtectRoutine) Lanius.getInstance().getRoutineRegistry().get("Name Protect"))
						.getAlias(data.entity.getName()) != null) {
			outlineCol = new Color(0.0F, ALIAS_COLOR, ALIAS_COLOR);
		} else if (((TeamRoutine) Lanius.getInstance().getRoutineRegistry().get("Team")).teammate(data.entity)) {
			outlineCol = new Color(TEAM_COLOR);
		} else if (entityLiving.hurtTime > 0 || entityLiving.deathTime > 0) {
			outlineCol = new Color(255, 0, 0);
		} else {
			outlineCol = new Color(0, 255, 0);
		}
		data.retVal = outlineCol.getRGB();
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.world.isRemote) {
			return;
		}
		final boolean glowing = livingEntity.isGlowing();
		if (!validEntity(livingEntity)) {
			if ((!Lanius.mc.player.isSpectator() || !Lanius.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown()
					|| !(livingEntity instanceof EntityPlayer)) && glowing) {
				livingEntity.setGlowing(false);
			}
			return;
		}
		final boolean useGlow = getBoolean("Use Glow");
		if (useGlow && !glowing) {
			livingEntity.setGlowing(true);
		} else if (!useGlow && glowing) {
			livingEntity.setGlowing(false);
		}
	}

	@Override
	public void registerValues() {
		registerValue("Use Glow", true, "Determines whether or not Minecraft's glow ESP will be used.");
		registerValue("Anti-aliasing", true, "Determines whether or not to enable line smoothing.");
		registerValue("Line Width", 1.5F, 1.0F, 3.0F, "Determines the thickness of the tracer lines.");
		final String displayName = name();
		registerValue("Entity Age", 0, 0, 200, "Specifies how long " + displayName
				+ " will wait before rendering a tracer for a recently spawned entity.");
		registerValue("Players", true, "Determines whether or not " + displayName + " should highlight players.");
		registerValue("Mobs", false, "Determines whether or not " + displayName + " should highlight mobs.");
		registerValue("Animals", false, "Determines whether or not " + displayName + " should highlight animals.");
		registerValue("Invisible Entities", true,
				"Determines whether or not " + displayName + " should highlight invisible entities.");
		registerValue("Fade", true, "Determines whether or not to make the tracer lines fade out.");
		registerValue("Armor", false,
				"Determines whether or not to only render a tracer on a player that has armor equipped.");
		registerValue("Held Item", false,
				"Determines whether or not to only render a tracer on a player that is holding an item.");
	}

	@Override
	public void renderEsp(final Entity entity, final float partialTicks) {
		final Entity viewEntity = Lanius.mc.getRenderViewEntity();
		if (!(viewEntity instanceof EntityLivingBase) || !validEntity(entity)) {
			return;
		}
		if (Lanius.mc.gameSettings.viewBobbing) {
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			Lanius.mc.gameSettings.viewBobbing = false;
			try {
				ReflectionHelper.findMethod(EntityRenderer.class, "setupCameraTransform", "func_78479_a", Float.TYPE,
						Integer.TYPE).invoke(Lanius.mc.entityRenderer, partialTicks, 2);
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Lanius.mc.gameSettings.viewBobbing = true;
		}
		final boolean textureEnabled = glIsEnabled(GL_TEXTURE_2D), lightEnabled = glIsEnabled(GL_LIGHTING),
				depthEnabled = glIsEnabled(GL_DEPTH_TEST), smoothEnabled = glIsEnabled(GL_LINE_SMOOTH),
				antiAliasing = getBoolean("Anti-aliasing"), blend = glIsEnabled(GL_BLEND),
				alphaTest = glIsEnabled(GL_ALPHA_TEST);
		if (textureEnabled) {
			GlStateManager.disableTexture2D();
		}
		if (lightEnabled) {
			GlStateManager.disableLighting();
		}
		if (depthEnabled) {
			GlStateManager.disableDepth();
		}
		if (!smoothEnabled && antiAliasing) {
			glEnable(GL_LINE_SMOOTH);
		}
		if (!blend) {
			GlStateManager.enableBlend();
		}
		if (alphaTest) {
			GlStateManager.disableAlpha();
		}
		final float prevLineWidth = glGetFloat(GL_LINE_WIDTH);
		GlStateManager.glLineWidth(getFloat("Line Width").floatValue());
		final float BLUE = 0.0F;
		final EntityLivingBase viewLiving = (EntityLivingBase) viewEntity;
		final float savedHeadYaw = viewLiving.rotationYawHead, savedPrevHead = viewLiving.prevRotationYawHead;
		viewLiving.rotationYawHead = viewEntity.rotationYaw;
		viewLiving.prevRotationYawHead = viewEntity.prevRotationYaw;
		final Vec3d lookVec = viewEntity.getLook(partialTicks);
		viewLiving.rotationYawHead = savedHeadYaw;
		viewLiving.prevRotationYawHead = savedPrevHead;
		float fovPercent = 1.0F;
		try {
			fovPercent = (Float) ReflectionHelper
					.findMethod(EntityRenderer.class, "getFOVModifier", "func_78481_a", Float.TYPE, Boolean.TYPE)
					.invoke(Lanius.mc.entityRenderer, partialTicks, true) / 180.0F;
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
		final FreecamRoutine freecamRoutine = (FreecamRoutine) Lanius.getInstance().getRoutineRegistry().get("Freecam");
		final SpeedRoutine speedRoutine = (SpeedRoutine) Lanius.getInstance().getRoutineRegistry().get("Speed");
		final double VIEW_MULT = Lanius.mc.gameSettings.thirdPersonView == 2 ? -1.0D : 1.0D,
				viewX = lookVec.x / fovPercent * VIEW_MULT + freecamRoutine.interpolatedX(partialTicks),
				viewY = viewEntity.getEyeHeight() + lookVec.y / fovPercent * VIEW_MULT
						+ freecamRoutine.interpolatedY(partialTicks)
						- (speedRoutine.execSilent() ? speedRoutine.distToGround(Lanius.mc.player, true) : 0.0D),
				viewZ = lookVec.z / fovPercent * VIEW_MULT + freecamRoutine.interpolatedZ(partialTicks);
		final NameProtectRoutine protectRoutine = (NameProtectRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Name Protect");
		final boolean protectEnabled = protectRoutine.isEnabled(), useGlow = getBoolean("Use Glow");
		float distPercent = entity.getDistance(Lanius.mc.player) / 64.0F;
		if (distPercent > 1.0F) {
			distPercent = 1.0F;
		}
		final ICamera camera = new Frustum();
		camera.setPosition(
				org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX,
						partialTicks),
				org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posY, viewEntity.lastTickPosY,
						partialTicks),
				org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posZ, viewEntity.lastTickPosZ,
						partialTicks));
		float renderPercent = (float) (org.bitbucket.lanius.util.math.MathHelper.distance(
				Lanius.mc.player.posX + freecamRoutine.getPosX(), Lanius.mc.player.posY + freecamRoutine.getPosY(),
				Lanius.mc.player.posZ + freecamRoutine.getPosZ(), entity.posX, entity.posY, entity.posZ) / 64.0D);
		if (renderPercent > 1.0F) {
			renderPercent = 1.0F;
		}
		final float alpha = getBoolean("Fade") && camera.isBoundingBoxInFrustum(entity.getEntityBoundingBox())
				? renderPercent
				: 1.0F;
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder worldRenderer = tessellator.getBuffer();
		final boolean hasAlias = protectEnabled && protectRoutine.getAlias(entity.getName()) != null,
				hasTeam = ((TeamRoutine) Lanius.getInstance().getRoutineRegistry().get("Team")).teammate(entity);
		final Color teamCol = new Color(TEAM_COLOR);
		final float red = hasAlias ? 0.0F : hasTeam ? teamCol.getRed() / 255.0F : 1.0F - distPercent;
		final double renderX = -MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX, partialTicks)
				+ MathHelper.interpolate(entity.posX, entity.lastTickPosX, partialTicks),
				renderY = -MathHelper.interpolate(viewEntity.posY, viewEntity.lastTickPosY, partialTicks)
						+ MathHelper.interpolate(entity.posY, entity.lastTickPosY, partialTicks),
				renderZ = -MathHelper.interpolate(viewEntity.posZ, viewEntity.lastTickPosZ, partialTicks)
						+ MathHelper.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
		worldRenderer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(viewX, viewY, viewZ)
				.color(red, hasAlias ? ALIAS_COLOR : hasTeam ? teamCol.getGreen() / 255.0F : distPercent,
						hasAlias ? ALIAS_COLOR : hasTeam ? teamCol.getBlue() / 255.0F : BLUE, alpha)
				.endVertex();
		worldRenderer.pos(renderX, renderY, renderZ)
				.color(red, hasAlias ? ALIAS_COLOR : hasTeam ? teamCol.getGreen() / 255.0F : distPercent,
						hasAlias ? ALIAS_COLOR : hasTeam ? teamCol.getBlue() / 255.0F : BLUE, alpha)
				.endVertex();
		if (!useGlow && !entity.isGlowing()) {
			worldRenderer.pos(renderX, renderY + entity.height, renderZ)
					.color(red, hasAlias ? ALIAS_COLOR : hasTeam ? teamCol.getGreen() / 255.0F : distPercent,
							hasAlias ? ALIAS_COLOR : hasTeam ? teamCol.getBlue() / 255.0F : BLUE, 1.0F)
					.endVertex();
		}
		tessellator.draw();
		GlStateManager.glLineWidth(prevLineWidth);
		if (alphaTest) {
			GlStateManager.enableAlpha();
		}
		if (!blend) {
			GlStateManager.disableBlend();
		}
		if (!smoothEnabled && antiAliasing) {
			glDisable(GL_LINE_SMOOTH);
		}
		if (textureEnabled) {
			GlStateManager.enableTexture2D();
		}
		if (lightEnabled) {
			GlStateManager.enableLighting();
		}
		if (depthEnabled) {
			GlStateManager.enableDepth();
		}
		if (Lanius.mc.gameSettings.viewBobbing) {
			GlStateManager.popMatrix();
		}
	}

	private boolean validEntity(final Entity entity) {
		final int entityAge = getInt("Entity Age").intValue();
		return !entity.equals(Lanius.mc.player)
				&& ((getBoolean("Players") && entity instanceof EntityOtherPlayerMP
						&& !entity.equals(((FreecamRoutine) Lanius.getInstance().getRoutineRegistry().get("Freecam"))
								.getRenderEntity())
						&& !entity.equals(
								((BlinkRoutine) Lanius.getInstance().getRoutineRegistry().get("Blink")).getPosEntity())
						&& (!getBoolean("Armor") || InventoryUtils.hasArmor((EntityPlayer) entity))
						&& (!getBoolean("Held Item") || InventoryUtils.hasHeldItem((EntityPlayer) entity))
						|| getBoolean("Mobs") && entity instanceof EntityMob
						|| getBoolean("Animals") && entity instanceof EntityAnimal)
						&& (!entity.isInvisible() || getBoolean("Invisible Entities")))
				&& entity.isEntityAlive() && Lanius.getInstance().getRoutineHandler().entityAge(entity) >= entityAge
						+ (entityAge <= 0 ? 0 : NetworkUtils.lagTicks());
	}

}
