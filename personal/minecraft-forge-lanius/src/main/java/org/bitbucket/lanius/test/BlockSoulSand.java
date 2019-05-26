package org.bitbucket.lanius.test;

import javax.annotation.Nullable;

import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.SoulSandData;
import org.bitbucket.lanius.util.Phase;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSoulSand extends Block {
	protected static final AxisAlignedBB SOUL_SAND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

	public BlockSoulSand() {
		super(Material.SAND, MapColor.BROWN);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return SOUL_SAND_AABB;
	}

	/**
	 * Called When an Entity Collided with the Block
	 */
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		final double hMult = HookManager.soulSandManager.execute(
				new SoulSandData((net.minecraft.block.BlockSoulSand) Blocks.SOUL_SAND, entityIn, 0.4D), Phase.START);
		entityIn.motionX *= hMult;
		entityIn.motionZ *= hMult;
	}
}