package org.bitbucket.lanius.gui;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.lanius.Lanius;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public final class LaniusGuiConfig extends GuiConfig {

	private static List<IConfigElement> configElements() {
		final Configuration modCfg = Lanius.getInstance().getModCfg();
		final List<IConfigElement> cfgElements = new ArrayList<IConfigElement>();
		for (final String categoryName : modCfg.getCategoryNames()) {
			cfgElements.add(new ConfigElement(modCfg.getCategory(categoryName)));
		}
		return cfgElements;
	}

	public LaniusGuiConfig(GuiScreen parentScreen) {
		super(parentScreen, configElements(), Lanius.MODID, false, false, Lanius.NAME + " Configuration");
		// TODO Auto-generated constructor stub
	}
}
