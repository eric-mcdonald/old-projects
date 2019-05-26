package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookData;

import net.minecraft.client.entity.EntityPlayerSP;

public final class SlowdownData extends HookData<EntityPlayerSP, float[]> {

	public SlowdownData(final EntityPlayerSP source, final float[] moveVecs) {
		super(source);
		retVal = moveVecs;
		// TODO Auto-generated constructor stub
	}

}
