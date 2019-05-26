package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookData;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;

public final class TabOverlayData extends HookData<GuiPlayerTabOverlay, String> {

	public NetworkPlayerInfo playerInfo;

	public TabOverlayData(final GuiPlayerTabOverlay source, final NetworkPlayerInfo playerInfo) {
		super(source);
		this.playerInfo = playerInfo;
		// TODO Auto-generated constructor stub
	}

}
