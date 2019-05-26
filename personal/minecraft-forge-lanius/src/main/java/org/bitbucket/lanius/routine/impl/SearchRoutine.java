package org.bitbucket.lanius.routine.impl;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_WIDTH;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glIsEnabled;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.cfg.Configurable;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.concurrent.LoopThread;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class SearchRoutine extends TabbedRoutine implements Configurable {

	private final class LookupThread extends LoopThread {

		private final ConcurrentMap<BlockPos, Block> posMap = new ConcurrentHashMap<BlockPos, Block>();

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			return 50L;
		}

		private Block equivalentBlock(final Block block) {
			if (block == AntiCactusRoutine.cactus) {
				return Blocks.CACTUS;
			} else if (block == JesusRoutine.flowingLava) {
				return Blocks.FLOWING_LAVA;
			} else if (block == JesusRoutine.flowingWater) {
				return Blocks.FLOWING_WATER;
			} else if (block == JesusRoutine.lava) {
				return Blocks.LAVA;
			} else if (block == JesusRoutine.water) {
				return Blocks.WATER;
			} else if (block == ViaVersionRoutine.farmland) {
				return Blocks.FARMLAND;
			} else if (block == ViaVersionRoutine.waterlily) {
				return Blocks.WATERLILY;
			}
			return block;
		}

		@Override
		protected void onExecute(final Phase phase) {
			// TODO Auto-generated method stub
			if (!phase.equals(Phase.START)) {
				return;
			}
			final Iterator<Entry<BlockPos, Block>> posIt = posMap.entrySet().iterator();
			final int blockDist = (Lanius.mc.gameSettings.renderDistanceChunks * 2 + 1) * 16 / 2;
			while (posIt.hasNext()) {
				final Entry<BlockPos, Block> posEntry = posIt.next();
				final BlockPos blockPos = posEntry.getKey();
				final Block block = equivalentBlock(posEntry.getValue());
				final double deltaX = blockPos.getX() - Lanius.mc.player.posX,
						deltaZ = blockPos.getZ() - Lanius.mc.player.posZ;
				if (!blockMap.containsValue(block) || deltaX * deltaX + deltaZ * deltaZ > blockDist * blockDist
						|| equivalentBlock(Lanius.mc.world.getBlockState(blockPos).getBlock()) != block) {
					posIt.remove();
				}
			}
			final int playerX = MathHelper.floor(Lanius.mc.player.posX),
					playerZ = MathHelper.floor(Lanius.mc.player.posZ);
			for (int blockY = 0; blockY < 256; blockY++) {
				for (int blockX = playerX - blockDist; blockX < playerX + blockDist; blockX++) {
					for (int blockZ = playerZ - blockDist; blockZ < playerZ + blockDist; blockZ++) {
						final BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);
						if (posMap.containsKey(blockPos)) {
							continue;
						}
						final double deltaX = blockPos.getX() - Lanius.mc.player.posX,
								deltaZ = blockPos.getZ() - Lanius.mc.player.posZ;
						final Block block = equivalentBlock(Lanius.mc.world.getBlockState(blockPos).getBlock());
						if (deltaX * deltaX + deltaZ * deltaZ <= blockDist * blockDist) {
							for (final Block searchBlock : blockMap.values()) {
								if (searchBlock == block) {
									posMap.put(blockPos, block);
									break;
								}
							}
						}
					}
				}
			}
		}

		@Override
		protected boolean running() {
			// TODO Auto-generated method stub
			return isEnabled() && this == lookupThread;
		}

	}

	private static final File searchFile = new File(Lanius.dataDir, "search.cfg");

	static boolean doubleChest(final World worldIn, final BlockPos pos) {
		if (worldIn.getBlockState(pos).getBlock() != Blocks.CHEST) {
			return false;
		} else {
			for (final EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
				if (worldIn.getBlockState(pos.offset(facing)).getBlock() == Blocks.CHEST) {
					return true;
				}
			}
			return false;
		}
	}

	static void drawOutline(final AxisAlignedBB outlineBox, final float partialTicks, final float red,
			final float green, final float blue, final float alpha, final ConfigContainer cfgContainer,
			boolean drawLines) {
		final AxisAlignedBB renderBox = outlineBox
				.grow(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
				.offset(-org.bitbucket.lanius.util.math.MathHelper.interpolate(Lanius.mc.player.posX,
						Lanius.mc.player.lastTickPosX, partialTicks),
						-org.bitbucket.lanius.util.math.MathHelper.interpolate(Lanius.mc.player.posY,
								Lanius.mc.player.lastTickPosY, partialTicks),
						-org.bitbucket.lanius.util.math.MathHelper.interpolate(Lanius.mc.player.posZ,
								Lanius.mc.player.lastTickPosZ, partialTicks));
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBuffer();
		final boolean culling = glIsEnabled(GL_CULL_FACE);
		if (culling) {
			glDisable(GL_CULL_FACE);
		}
		final boolean blend = glIsEnabled(GL_BLEND);
		if (!blend) {
			GlStateManager.enableBlend();
		}
		final boolean alphaTest = glIsEnabled(GL_ALPHA_TEST);
		if (alphaTest) {
			GlStateManager.disableAlpha();
		}
		buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		final float quadAlpha;
		final float MAX_QUAD_ALPHA = 0.375F;
		if (cfgContainer.getBoolean("Fade") && drawLines) {
			final FreecamRoutine freecam = (FreecamRoutine) Lanius.getInstance().getRoutineRegistry().get("Freecam");
			final double BOX_VEC = 0.5D;
			float distPercent = (float) (org.bitbucket.lanius.util.math.MathHelper.distance(
					Lanius.mc.player.posX + freecam.getPosX(), Lanius.mc.player.posY + freecam.getPosY(),
					Lanius.mc.player.posZ + freecam.getPosZ(), outlineBox.minX + BOX_VEC, outlineBox.minY + BOX_VEC,
					outlineBox.minZ + BOX_VEC) / ((Lanius.mc.gameSettings.renderDistanceChunks * 2 + 1) * 16 / 2));
			if (distPercent > MAX_QUAD_ALPHA) {
				distPercent = MAX_QUAD_ALPHA;
			}
			quadAlpha = distPercent;
		} else {
			quadAlpha = MAX_QUAD_ALPHA;
		}
		buffer.pos(renderBox.minX, renderBox.minY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.maxY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.minY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.minY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.maxY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.minY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.minY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.maxY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.maxY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.minY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.minY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.minY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.minY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.minY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.minY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.minY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.maxY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.minX, renderBox.maxY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.maxZ).color(red, green, blue, quadAlpha).endVertex();
		buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.minZ).color(red, green, blue, quadAlpha).endVertex();
		tessellator.draw();
		if (alphaTest) {
			GlStateManager.enableAlpha();
		}
		if (!blend) {
			GlStateManager.disableBlend();
		}
		if (culling) {
			glEnable(GL_CULL_FACE);
		}
		if (drawLines) {
			RenderGlobal.drawSelectionBoundingBox(renderBox, red, green, blue, alpha);
			buffer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(renderBox.minX, renderBox.minY, renderBox.minZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.minZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.minX, renderBox.minY, renderBox.maxZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.maxZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.minX, renderBox.minY, renderBox.minZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.minX, renderBox.maxY, renderBox.maxZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.maxX, renderBox.minY, renderBox.minZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.maxZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.minX, renderBox.minY, renderBox.minZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.maxX, renderBox.minY, renderBox.maxZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.minX, renderBox.maxY, renderBox.minZ).color(red, green, blue, alpha).endVertex();
			buffer.pos(renderBox.maxX, renderBox.maxY, renderBox.maxZ).color(red, green, blue, alpha).endVertex();
			tessellator.draw();
		}
	}

	private final ConcurrentMap<String, Block> blockMap = new ConcurrentHashMap<String, Block>(); // Eric:
	// using
	// a
	// map
	// instead
	// of
	// a
	// list
	// to
	// prevent
	// duplicates

	private LookupThread lookupThread;

	private Thread orebThread;

	public SearchRoutine() {
		super(Keyboard.KEY_H, false, Tab.RENDER);
		// TODO Auto-generated constructor stub
	}

	public void clearBlocks() {
		blockMap.clear();
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Highlights the selected blocks.";
	}

	@Override
	public String displayData() {
		return String.valueOf(lookupThread == null ? 0 : lookupThread.posMap.size());
	}

	@Override
	public void init() {
		lookupThread = null;
		orebThread = null;
	}

	@Override
	public void load() {
		blockMap.clear();
		if (!searchFile.exists()) {
			putBlock("chest");
			putBlock("diamond_ore");
			return;
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(searchFile));
			String line;
			try {
				while ((line = in.readLine()) != null) {
					putBlock(line);
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
		return "Search";
	}

	/**
	 * Handles LivingUpdateEvents. The livingUpdateEv argument is the instance of
	 * LivingUpdateEvent to handle. TODO(Eric) Make the Orebfuscator bypass
	 * concurrent.
	 * 
	 * @param livingUpdateEv the LivingUpdateEvent to handle
	 */
	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP
				|| !getBoolean("Orebfuscator") || orebThread != null) {
			return;
		}
		orebThread = new LoopThread() {

			private final Map<BlockPos, Block> orebPosMap = new HashMap<BlockPos, Block>();

			@Override
			protected long delay() {
				// TODO Auto-generated method stub
				return 50L;
			}

			@Override
			protected void onExecute(final Phase phase) {
				// TODO Auto-generated method stub
				if (!phase.equals(Phase.START)) {
					return;
				}
				final Iterator<Entry<BlockPos, Block>> orebIt = orebPosMap.entrySet().iterator();
				while (orebIt.hasNext()) {
					final Entry<BlockPos, Block> orebEntry = orebIt.next();
					final BlockPos orebPos = orebEntry.getKey();
					if (orebEntry.getValue() != Lanius.mc.world.getBlockState(orebPos).getBlock()
							|| entityLiving.getDistanceSq(orebPos) >= 64.0D) {
						orebIt.remove();
					}
				}
				final int playerX = MathHelper.floor(Lanius.mc.player.posX),
						playerY = MathHelper.floor(Lanius.mc.player.posY),
						playerZ = MathHelper.floor(Lanius.mc.player.posZ), RADIUS = 8;
				for (int blockY = playerY - RADIUS; blockY < playerY + RADIUS; blockY++) {
					for (int blockX = playerX - RADIUS; blockX < playerX + RADIUS; blockX++) {
						for (int blockZ = playerZ - RADIUS; blockZ < playerZ + RADIUS; blockZ++) {
							final BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);
							if (orebPosMap.containsKey(blockPos)) {
								continue;
							}
							final IBlockState blockState = Lanius.mc.world.getBlockState(blockPos);
							final Block block = blockState.getBlock();
							if (blockState.getMaterial() != Material.AIR
									&& entityLiving.getDistanceSq(blockPos) < 64.0D) {
								Lanius.mc.player.connection.sendPacket(new CPacketPlayerDigging(
										CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
								orebPosMap.put(blockPos, block);
							}
						}
					}
				}
			}

			@Override
			protected boolean running() {
				// TODO Auto-generated method stub
				return isEnabled() && this == orebThread;
			}

		};
		orebThread.start();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onRenderWorldLastLow(final RenderWorldLastEvent renderWorldLastEv) {
		if (lookupThread == null) {
			lookupThread = new LookupThread();
			lookupThread.start();
		}
		final boolean textureEnabled = glIsEnabled(GL_TEXTURE_2D), lightEnabled = glIsEnabled(GL_LIGHTING),
				depthEnabled = glIsEnabled(GL_DEPTH_TEST);
		if (textureEnabled) {
			GlStateManager.disableTexture2D();
		}
		if (lightEnabled) {
			GlStateManager.disableLighting();
		}
		if (depthEnabled) {
			GlStateManager.disableDepth();
		}
		final float prevLineWidth = glGetFloat(GL_LINE_WIDTH);
		GlStateManager.glLineWidth(getFloat("Line Width").floatValue());
		final ICamera camera = new Frustum();
		final Entity viewEntity = Lanius.mc.getRenderViewEntity();
		final float partialTicks = renderWorldLastEv.getPartialTicks();
		camera.setPosition(
				org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX,
						partialTicks),
				org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posY, viewEntity.lastTickPosY,
						partialTicks),
				org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posZ, viewEntity.lastTickPosZ,
						partialTicks));
		boolean drawLines = !getBoolean("Optimize") || lookupThread.posMap.size() <= getInt("Threshold").intValue();
		for (final BlockPos blockPos : lookupThread.posMap.keySet()) {
			final IBlockState blockState = Lanius.mc.world.getBlockState(blockPos);
			final Color blockColor = new Color(blockState.getMapColor(Lanius.mc.world, blockPos).colorValue);
			final float red = blockColor.getRed() / 255.0F, green = blockColor.getGreen() / 255.0F,
					blue = blockColor.getBlue() / 255.0F, alpha = blockColor.getAlpha() / 255.0F;
			if (doubleChest(Lanius.mc.world, blockPos)) {
				for (final EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
					final BlockPos offPos = blockPos.offset(facing);
					if (Lanius.mc.world.getBlockState(offPos).getBlock() == Blocks.CHEST) {
						final AxisAlignedBB blockBox = blockState.getSelectedBoundingBox(Lanius.mc.world, blockPos),
								offBox = blockState.getSelectedBoundingBox(Lanius.mc.world, offPos),
								outlineBox = blockBox.expand(offBox.minX - blockBox.minX, offBox.minY - blockBox.minY,
										offBox.minZ - blockBox.minZ);
						if (camera.isBoundingBoxInFrustum(outlineBox)) {
							drawOutline(outlineBox, partialTicks, red, green, blue, alpha, this, drawLines);
						}
					}
				}
			} else {
				final AxisAlignedBB outlineBox = blockState.getSelectedBoundingBox(Lanius.mc.world, blockPos);
				if (camera.isBoundingBoxInFrustum(outlineBox)) {
					drawOutline(outlineBox, partialTicks, red, green, blue, alpha, this, drawLines);
				}
			}
		}
		GlStateManager.glLineWidth(prevLineWidth);
		if (textureEnabled) {
			GlStateManager.enableTexture2D();
		}
		if (lightEnabled) {
			GlStateManager.enableLighting();
		}
		if (depthEnabled) {
			GlStateManager.enableDepth();
		}
	}

	public Block putBlock(final String blockName) {
		return blockMap.put(blockName, Block.getBlockFromName(blockName));
	}

	@Override
	public void registerValues() {
		registerValue("Optimize", true, "Specifies whether or not to optimize the outlines.");
		registerValue("Threshold", 1024, 1, 8192,
				"Specifies the amount of blocks before it starts optimizing the outlines.");
		registerValue("Orebfuscator", false, "Determines whether or not " + name() + " will bypass Orebfuscator.");
		registerValue("Line Width", 1.5F, 1.0F, 3.0F, "Specifies the thickness of the outlines' lines.");
		registerValue("Fade", true, "Determines whether or not to make the outlines fade out.");
	}

	public Block removeBlock(final String blockName) {
		return blockMap.remove(blockName);
	}

	@Override
	public void save() {
		searchFile.delete();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter((new FileWriter(searchFile))));
			for (final String blockName : blockMap.keySet()) {
				out.println(blockName);
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
