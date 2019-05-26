package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class TabOverlaySub extends GuiPlayerTabOverlay {

	/**
	 * Creates an instance of TabOverlaySub for handling Minecraft Forge events.
	 */
	public TabOverlaySub() {
		this(null, null);
	}

	public TabOverlaySub(Minecraft mcIn, GuiIngame p_i45529_2_) {
		super(mcIn, p_i45529_2_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getPlayerName(NetworkPlayerInfo p_175243_1_) {
		final String playerName = HookManager.tabManager.execute(new TabOverlayData(this, p_175243_1_), Phase.START);
		return playerName == null ? super.getPlayerName(p_175243_1_) : playerName;
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final String[] tabOverlayMappings = new String[] { "field_175196_v", "overlayPlayerList" };
		if (livingUpdateEv.getEntityLiving().equals(Lanius.mc.player) && !(ObfuscationReflectionHelper
				.getPrivateValue(GuiIngame.class, Lanius.mc.ingameGUI, tabOverlayMappings) instanceof TabOverlaySub)) {
			ReflectHelper.setValue(GuiIngame.class, Lanius.mc.ingameGUI,
					new TabOverlaySub(Lanius.mc, Lanius.mc.ingameGUI), tabOverlayMappings);
		}
	}

}
