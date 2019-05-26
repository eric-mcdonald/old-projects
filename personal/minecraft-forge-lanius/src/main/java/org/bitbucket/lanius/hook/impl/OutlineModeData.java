package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookData;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;

public final class OutlineModeData extends HookData<RenderLivingBase, Integer> {

	public final Entity entity;

	public OutlineModeData(final RenderLivingBase source, final int outlineMode, final Entity entity) {
		super(source);
		retVal = outlineMode;
		this.entity = entity;
		// TODO Auto-generated constructor stub
	}

}
