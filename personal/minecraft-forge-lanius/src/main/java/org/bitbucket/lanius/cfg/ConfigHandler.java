package org.bitbucket.lanius.cfg;

import org.bitbucket.lanius.Lanius;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ConfigHandler {
	@SubscribeEvent
	public void onPostConfigChanged(final PostConfigChangedEvent postCfgChangedEv) {
		if (!postCfgChangedEv.getModID().equals(Lanius.MODID)) {
			return;
		}
		Lanius.getInstance().loadCfgContainers();
	}
}
