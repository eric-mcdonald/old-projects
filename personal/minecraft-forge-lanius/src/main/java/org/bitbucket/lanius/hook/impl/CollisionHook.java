package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.util.CollisionUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.RoutineUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;

public final class CollisionHook implements Hook<CollisionData> {

	private static final AxisAlignedBB VIAVERSION_FARMLAND = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

	@Override
	public void onExecute(final CollisionData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START) || Lanius.mc.player == null) {
			return;
		}
		final boolean blockBoxes = RoutineUtils.viaVersionEnabled()
				&& Lanius.getInstance().getRoutineRegistry().get("ViaVersion").getBoolean("Block Boxes");
		if (data.source instanceof BlockLiquid) {
			final double H_VEC = 0.5D;
			data.retVal = RoutineUtils.enabled("Jesus") && !Lanius.mc.player.isInWater() && !Lanius.mc.player.isInLava()
					&& !CollisionUtils.collides(Lanius.mc.player, BlockStaticLiquid.class, H_VEC)
					&& !CollisionUtils.collides(Lanius.mc.player, BlockDynamicLiquid.class, H_VEC)
					&& (Lanius.mc.player.onGround || Lanius.mc.player.fallDistance <= 0.1F
							|| !RoutineUtils.ncpEnabled()
									&& (Lanius.mc.player.fallDistance <= 3.0F || RoutineUtils.enabled("No Fall"))
							|| Lanius.mc.player.capabilities.allowFlying)
									? Block.FULL_BLOCK_AABB.setMaxY(Block.FULL_BLOCK_AABB.maxY * (1.0F
											- BlockLiquid.getLiquidHeightPercent(data.state.getValue(BlockLiquid.LEVEL))
											+ 0.11111111F))
									: Block.NULL_AABB;
		} else if (data.source instanceof BlockCactus) {
			final AxisAlignedBB cactusBB = Blocks.CACTUS.getCollisionBoundingBox(null, null, null);
			data.retVal = RoutineUtils.enabled("Anti-cactus")
					? RoutineUtils.ncpEnabled() && !Lanius.mc.player.capabilities.allowFlying
							? Block.FULL_BLOCK_AABB.setMaxY(cactusBB.maxY)
							: Block.FULL_BLOCK_AABB
					: cactusBB;
		} else if (blockBoxes && data.source instanceof BlockLilyPad) {
			float xOffset = 0.5F, yMax = 0.015625F;
			data.retVal = new AxisAlignedBB(0.5F - xOffset, 0.0F, 0.5F - xOffset, 0.5F + xOffset, yMax, 0.5F + xOffset);
		} else if (blockBoxes && data.source instanceof BlockFarmland) {
			data.retVal = VIAVERSION_FARMLAND;
		}
	}

}
