package org.bitbucket.lanius.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public final class LaniusGuiFactory implements IModGuiFactory {

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		// TODO Auto-generated method stub
		return new LaniusGuiConfig(parentScreen);
	}

	@Override
	public boolean hasConfigGui() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void initialize(Minecraft minecraftInstance) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		// TODO Auto-generated method stub
		return null;
	}

}
