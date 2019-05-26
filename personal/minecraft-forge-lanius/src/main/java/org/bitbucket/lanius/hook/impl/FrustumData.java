package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookData;

import net.minecraft.client.renderer.culling.Frustum;

public final class FrustumData extends HookData<Frustum, Boolean> {

	public FrustumData(final Frustum source, final boolean originVal) {
		super(source);
		retVal = originVal;
		// TODO Auto-generated constructor stub
	}

}
