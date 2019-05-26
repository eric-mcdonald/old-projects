package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookData;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public final class DoRenderData extends HookData<Render<? extends Entity>, Boolean> {

	public DoRenderData(final Render<? extends Entity> source) {
		super(source);
		retVal = false;
		// TODO Auto-generated constructor stub
	}

}
