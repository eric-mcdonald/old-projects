package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookData;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public final class CollisionData extends HookData<Block, AxisAlignedBB> {

	public final BlockPos pos;
	public final IBlockState state;
	public final IBlockAccess world;

	public CollisionData(final Block source, final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		super(source);
		this.state = state;
		this.world = world;
		this.pos = pos;
		// TODO Auto-generated constructor stub
	}

}
