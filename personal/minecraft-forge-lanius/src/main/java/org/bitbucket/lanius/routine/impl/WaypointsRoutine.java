package org.bitbucket.lanius.routine.impl;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST_FUNC;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST_REF;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_LINE_WIDTH;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.GL_PROJECTION_MATRIX;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL11.glNormal3f;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.Configurable;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.GLU;

import com.google.common.collect.Sets;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class WaypointsRoutine extends TabbedRoutine implements Configurable, Hook<NetHandlerData> {

	private static final double BOX_SZ = 3.0D;

	private static final File waypointsFile = new File(Lanius.dataDir, "waypoints.cfg");

	private final FloatBuffer modelviewMatrix = GLAllocation.createDirectFloatBuffer(16),
			projectionMatrix = GLAllocation.createDirectFloatBuffer(16),
			windowPos = GLAllocation.createDirectFloatBuffer(3);

	private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);

	private final Map<String, BlockPos> waypointMap = new HashMap<String, BlockPos>();

	public WaypointsRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.RENDER);
		// TODO Auto-generated constructor stub
	}

	public void clearWaypoints() {
		waypointMap.clear();
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Highlights the specified locations.";
	}

	@Override
	public String displayData() {
		return String.valueOf(waypointMap.size());
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		if (!waypointsFile.exists()) {
			return;
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(waypointsFile));
			String line;
			try {
				while ((line = in.readLine()) != null) {
					final int colonIdx = line.indexOf(":");
					final String[] position = line.substring(colonIdx + 1).split(",");
					waypointMap.put(line.substring(0, colonIdx), new BlockPos(Integer.parseInt(position[0]),
							Integer.parseInt(position[1]), Integer.parseInt(position[2])));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Waypoints";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START) || !(data.retVal instanceof SPacketSpawnPosition)) {
			return;
		}
		for (final Map.Entry<String, BlockPos> waypointEntry : waypointMap.entrySet()) {
			if (waypointEntry.getValue().equals(new BlockPos(Lanius.mc.world.getWorldInfo().getSpawnX(),
					Lanius.mc.world.getWorldInfo().getSpawnY(), Lanius.mc.world.getWorldInfo().getSpawnZ()))) {
				waypointEntry.setValue(((SPacketSpawnPosition) data.retVal).getSpawnPos());
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdateLowest(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP) {
			return;
		}
		if (getBoolean("Remove Reached")) {
			final Iterator<Map.Entry<String, BlockPos>> posIt = waypointMap.entrySet().iterator();
			while (posIt.hasNext()) {
				final Map.Entry<String, BlockPos> waypointEntry = posIt.next();
				final BlockPos waypointPos = waypointEntry.getValue();
				if (!Lanius.mc.player.getEntityBoundingBox()
						.intersects(new AxisAlignedBB(waypointPos.getX() - BOX_SZ / 2.0D, waypointPos.getY(),
								waypointPos.getZ() - BOX_SZ / 2.0D, waypointPos.getX() + BOX_SZ / 2.0D,
								waypointPos.getY() + BOX_SZ, waypointPos.getZ() + BOX_SZ / 2.0D))) {
					continue;
				}
				CommandUtils.addText(livingEntity, "Reached waypoint \"" + waypointEntry.getKey() + "\" at ("
						+ waypointPos.getX() + ", " + waypointPos.getY() + ", " + waypointPos.getZ() + ")");
				posIt.remove();
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onRenderWorldLastLowest(final RenderWorldLastEvent renderWorldLastEv) {
		if (waypointMap.isEmpty()) {
			return;
		}
		final RenderManager renderManager = Lanius.mc.getRenderManager();
		if (renderManager.renderViewEntity == null) {
			return; // Eric: hotfix for a crash when trying to connect to
			// play.mcgamer.net
		}
		final ICamera camera = new Frustum();
		final Entity viewEntity = Lanius.mc.getRenderViewEntity();
		final float partialTicks = renderWorldLastEv.getPartialTicks();
		camera.setPosition(MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX, partialTicks),
				MathHelper.interpolate(viewEntity.posY, viewEntity.lastTickPosY, partialTicks),
				MathHelper.interpolate(viewEntity.posZ, viewEntity.lastTickPosZ, partialTicks));
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
			GlStateManager.depthMask(false);
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
		RenderHelper.disableStandardItemLighting();
		Lanius.mc.entityRenderer.disableLightmap();
		final int prevFunc = glGetInteger(GL_ALPHA_TEST_FUNC);
		final float prevRef = glGetFloat(GL_ALPHA_TEST_REF);
		GlStateManager.alphaFunc(516, 0.1F);
		final float prevLineWidth = glGetFloat(GL_LINE_WIDTH);
		GlStateManager.glLineWidth(getFloat("Line Width").floatValue());
		final Color selectedCol = new Color(0xAA00AA);
		final float red = selectedCol.getRed() / 255.0F, green = selectedCol.getGreen() / 255.0F,
				blue = selectedCol.getBlue() / 255.0F, alpha = selectedCol.getAlpha() / 255.0F;
		for (final Map.Entry<String, BlockPos> waypointEntry : waypointMap.entrySet()) {
			final BlockPos waypointPos = waypointEntry.getValue();
			final AxisAlignedBB outlineBox = new AxisAlignedBB(waypointPos.getX() - BOX_SZ / 2.0D, waypointPos.getY(),
					waypointPos.getZ() - BOX_SZ / 2.0D, waypointPos.getX() + BOX_SZ / 2.0D, waypointPos.getY() + BOX_SZ,
					waypointPos.getZ() + BOX_SZ / 2.0D);
			float renderPercent = (float) (MathHelper.distance(Lanius.mc.player.posX + freecamRoutine.getPosX(),
					Lanius.mc.player.posY + freecamRoutine.getPosY(), Lanius.mc.player.posZ + freecamRoutine.getPosZ(),
					waypointPos.getX(), waypointPos.getY(), waypointPos.getZ()) / 64.0D);
			if (renderPercent > 1.0F) {
				renderPercent = 1.0F;
			}
			final boolean boxInFrustum = camera.isBoundingBoxInFrustum(outlineBox);
			if (boxInFrustum) {
				SearchRoutine.drawOutline(outlineBox, partialTicks, red, green, blue, alpha, this, true);
			}
			final Tessellator tessellator = Tessellator.getInstance();
			final BufferBuilder worldRenderer = tessellator.getBuffer();
			final double renderX = -MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX, partialTicks)
					+ waypointPos.getX(),
					renderY = -MathHelper.interpolate(viewEntity.posY, viewEntity.lastTickPosY, partialTicks)
							+ waypointPos.getY(),
					renderZ = -MathHelper.interpolate(viewEntity.posZ, viewEntity.lastTickPosZ, partialTicks)
							+ waypointPos.getZ();
			worldRenderer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
			worldRenderer.pos(viewX, viewY, viewZ).color(red, green, blue, boxInFrustum ? renderPercent : alpha)
					.endVertex();
			worldRenderer.pos(renderX, renderY, renderZ).color(red, green, blue, boxInFrustum ? renderPercent : alpha)
					.endVertex();
			tessellator.draw();
			final int X1 = 1, X2 = 1, Y1 = -1, y2 = 9;
			float scaleFactor = 1.0F;
			try {
				final int midAverage = Lanius.mc.fontRenderer.getStringWidth("0123456789") / 2; // TODO(Eric)
				// This
				// literally
				// makes
				// no
				// sense.
				// Better
				// implementation?
				scaleFactor = (float) (org.bitbucket.lanius.util.math.MathHelper.distance(
						viewEntity.posX + freecamRoutine.getPosX(),
						viewEntity.posY + viewEntity.getEyeHeight() + freecamRoutine.getPosY(),
						viewEntity.posZ + freecamRoutine.getPosZ(), waypointPos.getX(), waypointPos.getY(), waypointPos
								.getZ())
						* Math.sin((Float) ReflectionHelper.findMethod(EntityRenderer.class, "getFOVModifier",
								"func_78481_a", Float.TYPE, Boolean.TYPE)
								.invoke(Lanius.mc.entityRenderer, partialTicks, true) * Math.PI / 180.0D / 2.0D)
						/ (org.bitbucket.lanius.util.math.MathHelper.distance(-midAverage - X1, Y1, 0.0D,
								midAverage + X2, y2, 0.0D) / 2.0D));
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
			final float MIN_SCALE = -0.025F, cfgScale = getFloat("Scale").floatValue(),
					multiplier = -MIN_SCALE * cfgScale;
			final boolean scaleFocused = getBoolean("Scale Focused");
			final float HEIGHT = (float) BOX_SZ;
			// Eric: check for code efficiency
			if (scaleFocused) {
				modelviewMatrix.rewind();
				projectionMatrix.rewind();
				viewport.rewind();
				GlStateManager.pushMatrix();
				GlStateManager.translate(renderX, renderY, renderZ);
				glNormal3f(0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(-renderX, -renderY, -renderZ);
				glGetFloat(GL_MODELVIEW_MATRIX, modelviewMatrix);
				glGetFloat(GL_PROJECTION_MATRIX, projectionMatrix);
				glGetInteger(GL_VIEWPORT, viewport);
				GlStateManager.popMatrix();
				project((float) (waypointPos.getX()
						- MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX, partialTicks)),
						(float) (waypointPos.getY() + HEIGHT / 2.0F
								- MathHelper.interpolate(viewEntity.posY, viewEntity.lastTickPosY, partialTicks)),
						(float) (waypointPos.getZ()
								- MathHelper.interpolate(viewEntity.posZ, viewEntity.lastTickPosZ, partialTicks)));
			}
			final float centerX = windowPos.get(0), centerY = windowPos.get(1);
			final ScaledResolution resolution = new ScaledResolution(Lanius.mc);
			final int scaleFac = resolution.getScaleFactor();
			final float MID_WIDTH = (float) (BOX_SZ / 2.0D);
			if (scaleFocused) {
				project((float) (waypointPos.getX() - MID_WIDTH
						- MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX, partialTicks)),
						(float) (waypointPos.getY()
								- MathHelper.interpolate(viewEntity.posY, viewEntity.lastTickPosY, partialTicks)),
						(float) (waypointPos.getZ()
								- MathHelper.interpolate(viewEntity.posZ, viewEntity.lastTickPosZ, partialTicks)));
			}
			final float negX = windowPos.get(0), negY = windowPos.get(1);
			if (scaleFocused) {
				project((float) (waypointPos.getX() + MID_WIDTH
						- MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX, partialTicks)),
						(float) (waypointPos.getY() + HEIGHT
								- MathHelper.interpolate(viewEntity.posY, viewEntity.lastTickPosY, partialTicks)),
						(float) (waypointPos.getZ()
								- MathHelper.interpolate(viewEntity.posZ, viewEntity.lastTickPosZ, partialTicks)));
			}
			final boolean scaleEffects = cfgScale > 0.0F && (!scaleFocused || MathHelper.distance(
					resolution.getScaledWidth_double() / 2.0D * scaleFac, 0.0D, 0.0D, centerX, 0.0D,
					0.0D) <= MathHelper.distance(negX, 0.0D, 0.0D, windowPos.get(0), 0.0D, 0.0D) / 2.0D
					&& MathHelper.distance(0.0D, resolution.getScaledHeight_double() / 2.0D * scaleFac, 0.0D, 0.0D,
							centerY,
							0.0D) <= MathHelper.distance(0.0D, negY, 0.0D, 0.0D, windowPos.get(1), 0.0D) / 2.0D);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) renderX + 0.0F, (float) renderY + BOX_SZ, (float) renderZ);
			glNormal3f(0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			final float scale = scaleEffects && multiplier * -scaleFactor < MIN_SCALE ? multiplier * -scaleFactor
					: MIN_SCALE;
			GlStateManager.scale(scale, scale, -scale);
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			final float rootedDist = net.minecraft.util.math.MathHelper
					.sqrt(MathHelper.distance(viewEntity.posX + freecamRoutine.getPosX(),
							viewEntity.posY + freecamRoutine.getPosY(), viewEntity.posZ + freecamRoutine.getPosZ(),
							waypointPos.getX(), waypointPos.getY(), waypointPos.getZ()));
			byte yOffset = (byte) (-Math
					.round(scaleEffects && multiplier * -scaleFactor < MIN_SCALE ? rootedDist : 0.0F));
			GlStateManager.disableTexture2D();
			worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			final int midName = Math.round(Lanius.mc.fontRenderer.getStringWidth(waypointEntry.getKey()) / 2.0F);
			worldRenderer.pos(-midName - X1, Y1 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldRenderer.pos(-midName - X1, y2 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldRenderer.pos(midName + X2, y2 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldRenderer.pos(midName + X2, Y1 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			Lanius.mc.fontRenderer.drawStringWithShadow(waypointEntry.getKey(), -midName, yOffset, 16777215);
			GlStateManager.disableTexture2D();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
		GlStateManager.glLineWidth(prevLineWidth);
		GlStateManager.alphaFunc(prevFunc, prevRef);
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
			GlStateManager.depthMask(true);
		}
		if (Lanius.mc.gameSettings.viewBobbing) {
			GlStateManager.popMatrix();
		}
	}

	private void project(final float objX, final float objY, final float objZ) {
		windowPos.rewind();
		GLU.gluProject(objX, objY, objZ, modelviewMatrix, projectionMatrix, viewport, windowPos);
	}

	public BlockPos putWaypoint(final String name, final BlockPos pos) {
		return waypointMap.put(name, pos);
	}

	@Override
	public void registerValues() {
		registerValue("Anti-aliasing", true, "Determines whether or not to enable line smoothing.");
		registerValue("Line Width", 1.5F, 1.0F, 3.0F, "Determines the thickness of the tracer lines.");
		registerValue("Fade", true, "Determines whether or not to make the outlines fade out.");
		registerValue("Scale", 9.0F, 0.0F, 10.0F, "Specifies the value to scale the name tags by.");
		registerValue("Scale Focused", true,
				"Determines whether or not to only scale the name tags of waypoints that are over the cursor.");
		registerValue("Remove Reached", true, "Determines whether or not to remove a waypoint when you reach it.");
	}

	public BlockPos removeWaypoint(final String name) {
		return waypointMap.remove(name);
	}

	@Override
	public void save() {
		waypointsFile.delete();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter((new FileWriter(waypointsFile))));
			for (final Map.Entry<String, BlockPos> waypointEntry : waypointMap.entrySet()) {
				final BlockPos value = waypointEntry.getValue();
				out.println(waypointEntry.getKey() + ":" + value.getX() + "," + value.getY() + "," + value.getZ());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
