package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookData;

import net.minecraft.client.renderer.RenderItem;

public class RenderEffectData extends HookData<RenderItem, Boolean> {

	public RenderEffectData(final RenderItem source) {
		super(source);
		retVal = false;
		// TODO Auto-generated constructor stub
	}

}
