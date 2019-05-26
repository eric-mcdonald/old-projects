package net.hackforums.pootpoot.monumental.mode.modes;

import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.glDepthRange;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glNormal3f;

import java.lang.reflect.InvocationTargetException;

import net.hackforums.pootpoot.monumental.Monumental;
import net.hackforums.pootpoot.monumental.init.Commands;
import net.hackforums.pootpoot.monumental.mode.BaseMode;
import net.hackforums.pootpoot.monumental.mode.DisplayText;
import net.hackforums.pootpoot.monumental.mode.Displayable;
import net.hackforums.pootpoot.monumental.mode.EventHandler;
import net.hackforums.pootpoot.monumental.util.ObfuscationReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class TracerMode extends BaseMode implements Displayable, EventHandler {

	private DisplayText displayText = new DisplayText(getName(), 0xFF0000);
	private static final Logger LOGGER = LogManager.getLogger();
	public TracerMode() {
		super("Tracer", "Highlights entities by drawing a line and their bounding box", new KeyBinding("Tracer", Keyboard.KEY_BACK, Monumental.NAME));
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean bypassesNoCheatPlus() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public DisplayText getDisplayText() {
		// TODO Auto-generated method stub
		return displayText;
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		if (!isEnabled()) {
			return;
		}
		if (event instanceof RenderLivingEvent) {
			Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity(), entity = ((RenderLivingEvent) event).entity;
			if ((Commands.TRACER_COMMAND.willHighlightAnimals() && entity instanceof EntityAnimal) || (Commands.TRACER_COMMAND.willHighlightPlayers() && entity instanceof EntityOtherPlayerMP) || (Commands.TRACER_COMMAND.willHighlightMobs() && entity instanceof EntityMob)) {
				EntityRenderer entityRenderer = Minecraft.getMinecraft().entityRenderer;
				if (event instanceof RenderLivingEvent.Pre) {
					entityRenderer.disableLightmap();
				}
				else if (event instanceof RenderLivingEvent.Post) {
					entityRenderer.enableLightmap();
				}
				if (renderViewEntity instanceof EntityLivingBase && !((EntityLivingBase) renderViewEntity).canEntityBeSeen(entity)) {
					if (event instanceof RenderLivingEvent.Pre) {
						glDepthRange(0.0D, 0.9D);
					}
					else if (event instanceof RenderLivingEvent.Post) {
						glDepthRange(0.1D, 1.0D);
					}
				}
			}
		}
		else if (event instanceof RenderHandEvent) {
			RenderHandEvent renderTickEvent = (RenderHandEvent) event;
			if (renderTickEvent.renderPass == 2) {
				EntityRenderer entityRenderer = Minecraft.getMinecraft().entityRenderer;
				RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
				Tessellator tessellator = Tessellator.getInstance();
				WorldRenderer worldRenderer = tessellator.getWorldRenderer();
				for (Object obj : Minecraft.getMinecraft().theWorld.loadedEntityList) {
					if ((!Commands.TRACER_COMMAND.willHighlightAnimals() || !(obj instanceof EntityAnimal)) && (!Commands.TRACER_COMMAND.willHighlightPlayers() || !(obj instanceof EntityOtherPlayerMP)) && (!Commands.TRACER_COMMAND.willHighlightMobs() || !(obj instanceof EntityMob))) {
						continue;
					}
					GlStateManager.pushMatrix();
					GlStateManager.disableAlpha();
					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
					GlStateManager.disableTexture2D();
					GlStateManager.depthMask(false);
					GlStateManager.disableDepth();
					glLineWidth(2.0F);
					glEnable(GL_LINE_SMOOTH);
					GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
					boolean prevViewBobbing = gameSettings.viewBobbing;
					gameSettings.viewBobbing = false;
					try {
						ReflectionHelper.findMethod(EntityRenderer.class, entityRenderer, ObfuscationReflectionHelper.remapMethodNames(EntityRenderer.class.getName(), "(FI)V", "a", "setupCameraTransform"), Float.TYPE, Integer.TYPE).invoke(entityRenderer, renderTickEvent.partialTicks, renderTickEvent.renderPass);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						LOGGER.error(Monumental.NAME + ": could not access the method", e);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						LOGGER.error(Monumental.NAME + ": parameter is invalid", e);
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						LOGGER.error(Monumental.NAME + ": an Exception was thrown in the method", e);
					}
					gameSettings.viewBobbing = prevViewBobbing;
					Entity entity = (Entity) obj;
					double renderX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * renderTickEvent.partialTicks - renderManager.viewerPosX, renderY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * renderTickEvent.partialTicks - renderManager.viewerPosY, renderZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * renderTickEvent.partialTicks - renderManager.viewerPosZ;
					GlStateManager.translate(renderX, renderY, renderZ);
					worldRenderer.startDrawing(1);
					int distance = (int) Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity), red = 4 * (64 - distance), green = 4 * distance;
					worldRenderer.setColorOpaque(red, green, 0);
					worldRenderer.addVertex(-renderX, -renderY + Minecraft.getMinecraft().getRenderViewEntity().getEyeHeight(), -renderZ);
					worldRenderer.addVertex(0.0D, 0.0D, 0.0D);
					tessellator.draw();
					glNormal3f(0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
					worldRenderer.startDrawing(3);
					EntityLivingBase entityLiving = (EntityLivingBase) entity;
					if (entityLiving.hurtTime > 0 || entityLiving.deathTime > 0) {
						red = 255;
						green = 0;
					}
					else {
						red = 0;
						green = 255;
					}
					worldRenderer.setColorOpaque(red, green, 0);
					float halfWidth = entity.width / 2.0F;
					worldRenderer.addVertex(-halfWidth, 0.0D, -halfWidth);
					worldRenderer.addVertex(halfWidth, 0.0D, -halfWidth);
					worldRenderer.addVertex(halfWidth, 0.0D, halfWidth);
					worldRenderer.addVertex(-halfWidth, 0.0D, halfWidth);
					worldRenderer.addVertex(-halfWidth, 0.0D, -halfWidth);
					tessellator.draw();
					worldRenderer.startDrawing(3);
					worldRenderer.setColorOpaque(red, green, 0);
					double height = entity.height - (entity.isSneaking() ? 0.125D : 0.0D) - (((EntityLivingBase) entity).isPlayerSleeping() ? 1.5D : 0.0D);
					worldRenderer.addVertex(-halfWidth, height, -halfWidth);
					worldRenderer.addVertex(halfWidth, height, -halfWidth);
					worldRenderer.addVertex(halfWidth, height, halfWidth);
					worldRenderer.addVertex(-halfWidth, height, halfWidth);
					worldRenderer.addVertex(-halfWidth, height, -halfWidth);
					tessellator.draw();
					worldRenderer.startDrawing(1);
					worldRenderer.setColorOpaque(red, green, 0);
					worldRenderer.addVertex(-halfWidth, 0.0D, -halfWidth);
					worldRenderer.addVertex(-halfWidth, height, -halfWidth);
					worldRenderer.addVertex(halfWidth, 0.0D, -halfWidth);
					worldRenderer.addVertex(halfWidth, height, -halfWidth);
					worldRenderer.addVertex(halfWidth, 0.0D, halfWidth);
					worldRenderer.addVertex(halfWidth, height, halfWidth);
					worldRenderer.addVertex(-halfWidth, 0.0D, halfWidth);
					worldRenderer.addVertex(-halfWidth, height, halfWidth);
					tessellator.draw();
					worldRenderer.startDrawing(1);
					worldRenderer.setColorOpaque(red, green, 0);
					worldRenderer.addVertex(-halfWidth, 0.0D, -halfWidth);
					worldRenderer.addVertex(halfWidth, height, -halfWidth);
					worldRenderer.addVertex(-halfWidth, 0.0D, halfWidth);
					worldRenderer.addVertex(halfWidth, height, halfWidth);
					worldRenderer.addVertex(-halfWidth, 0.0D, -halfWidth);
					worldRenderer.addVertex(-halfWidth, height, halfWidth);
					worldRenderer.addVertex(halfWidth, 0.0D, -halfWidth);
					worldRenderer.addVertex(halfWidth, height, halfWidth);
					worldRenderer.addVertex(-halfWidth, 0.0D, -halfWidth);
					worldRenderer.addVertex(halfWidth, 0.0D, halfWidth);
					worldRenderer.addVertex(-halfWidth, height, -halfWidth);
					worldRenderer.addVertex(halfWidth, height, halfWidth);
					tessellator.draw();
					glDisable(GL_LINE_SMOOTH);
					GlStateManager.enableDepth();
					GlStateManager.depthMask(true);
					GlStateManager.enableTexture2D();
					GlStateManager.disableBlend();
					GlStateManager.enableAlpha();
					GlStateManager.popMatrix();
				}
			}
		}
	}

}
