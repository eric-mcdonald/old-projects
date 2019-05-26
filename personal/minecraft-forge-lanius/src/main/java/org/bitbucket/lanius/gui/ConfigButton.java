package org.bitbucket.lanius.gui;

import org.bitbucket.lanius.cfg.ConfigContainer;

public final class ConfigButton extends TooltipButton {
	private final ConfigContainer cfgContainer;

	public ConfigButton(final String text, int x, int y, int width, int height, FrameComponent parent,
			final ConfigContainer cfgContainer) {
		super(text, x, y, width, height, parent, cfgContainer.getBoolean(text));
		this.cfgContainer = cfgContainer;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void click(final int mouseX, final int mouseY, final int mouseBtn) {
		// TODO Auto-generated method stub
		super.click(mouseX, mouseY, mouseBtn);
		cfgContainer.putValue(text(), !isSelected());
	}

	@Override
	protected boolean isSelected() {
		return cfgContainer.getBoolean(text());
	}

	@Override
	protected String tooltip() {
		// TODO Auto-generated method stub
		return cfgContainer.getComment(text());
	}
}
