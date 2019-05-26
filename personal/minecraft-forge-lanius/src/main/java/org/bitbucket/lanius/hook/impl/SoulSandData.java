package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookData;

import net.minecraft.block.BlockSoulSand;
import net.minecraft.entity.Entity;

public final class SoulSandData extends HookData<BlockSoulSand, Double> {

	public final Entity entity;

	public SoulSandData(final BlockSoulSand source, final Entity entity, final double hMult) {
		super(source);
		this.entity = entity;
		retVal = hMult;
		// TODO Auto-generated constructor stub
	}

}
