package org.bitbucket.lanius.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public final class CollisionUtils {
	public static boolean collides(final Entity entity, final Class<?> blockClass, final double hVec) {
		for (final double[] hPos : new double[][] { { hVec, hVec }, { -hVec, hVec }, { hVec, -hVec },
				{ -hVec, -hVec } }) {
			final Block predictHBlock = entity.world
					.getBlockState(new BlockPos(entity.posX + hPos[0], entity.posY, entity.posZ + hPos[1])).getBlock(),
					predictYBlock = entity.world
							.getBlockState(
									new BlockPos(entity.posX + hPos[0], entity.posY + 1.0D, entity.posZ + hPos[1]))
							.getBlock();
			if (blockClass.isInstance(predictHBlock) || blockClass.isInstance(predictYBlock)) {
				return true;
			}
		}
		return false;
	}

	public static boolean collidesWall(final Entity entity, boolean checkAbove) {
		final double H_VEC = 0.03125D;
		boolean collidesHighBlock = false;
		for (final Field blockField : Blocks.class.getFields()) {
			Object block = null;
			try {
				block = blockField.get(null);
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			final boolean wallBlock = BlockWall.class.isInstance(block);
			if (BlockFence.class.isInstance(block) || BlockFenceGate.class.isInstance(block) || wallBlock) {
				collidesHighBlock |= collides(entity, block.getClass(), wallBlock ? 0.25D : H_VEC);
				if (collidesHighBlock) {
					break;
				}
			}
		}
		final AxisAlignedBB entityBB = entity.getEntityBoundingBox();
		boolean collidesBox = collidesHighBlock;
		for (final Object aabbObj : entity.world.getCollisionBoxes(entity,
				entityBB.grow(H_VEC, 0.0D, H_VEC).expand(0.0D, checkAbove ? 1.0D : 0.0D, 0.0D))) {
			final AxisAlignedBB aabb = (AxisAlignedBB) aabbObj;
			if (aabb.minY > entityBB.minY && aabb.minY - entityBB.minY > 0.5D && aabb.maxY - aabb.minY > 0.2D) {
				collidesBox = true;
				break;
			}
		}
		return collidesBox;
	}

	public static IBlockState[] collidingStates(final Entity entity, final Class<?>[] blockClasses, final double radius,
			final boolean groundCollides) {
		final List<IBlockState> collidingStates = new ArrayList<IBlockState>();
		for (final double[] hPos : new double[][] { { radius, radius }, { -radius, radius }, { radius, -radius },
				{ -radius, -radius } }) {
			for (double x = hPos[0] < 0.0D ? hPos[0] : 0.0D, z = hPos[1] < 0.0D ? hPos[1]
					: 0.0D; (hPos[0] < 0.0D ? x <= 0.0D : x <= hPos[0])
							&& (hPos[1] < 0.0D ? z <= 0.0D : z <= hPos[1]); x++, z++) {
				final IBlockState predictHState = entity.world
						.getBlockState(new BlockPos(entity.posX + x, entity.posY, entity.posZ + z)),
						predictYState = entity.world
								.getBlockState(new BlockPos(entity.posX + x, entity.posY + 1.0D, entity.posZ + z));
				for (final Class<?> blockClass : blockClasses) {
					boolean addedBlock = false;
					if (blockClass.isInstance(predictHState.getBlock())) {
						collidingStates.add(predictHState);
						addedBlock = true;
					}
					if (blockClass.isInstance(predictYState.getBlock())) {
						collidingStates.add(predictYState);
						addedBlock = true;
					}
					if (addedBlock) {
						break;
					}
				}
				if (groundCollides) {
					for (double y = 0.0D; y <= radius; y++) {
						final IBlockState groundState = entity.world
								.getBlockState(new BlockPos(entity.posX + x, entity.posY - y, entity.posZ + z));
						for (final Class<?> blockClass : blockClasses) {
							if (blockClass.isInstance(groundState.getBlock())) {
								collidingStates.add(groundState);
								break;
							}
						}
					}
				}
			}
		}
		return collidingStates.toArray(new IBlockState[0]);
	}

}
