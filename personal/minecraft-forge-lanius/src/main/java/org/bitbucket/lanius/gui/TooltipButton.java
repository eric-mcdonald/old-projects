package org.bitbucket.lanius.gui;

import org.bitbucket.lanius.Lanius;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.Gui;

public abstract class TooltipButton extends ButtonComponent implements UpdatableComponent {
	private static TooltipButton tooltipBtn;

	static void setTooltipBtn() {
		tooltipBtn = null;
	}

	private int tooltipCount = 20;

	public TooltipButton(final String text, int x, int y, int width, int height, final FrameComponent parent,
			final boolean selected) {
		super(text, x, y, width, height, parent, selected);
		// TODO Auto-generated constructor stub
	}

	final boolean renderTooltip(final int mouseX, final int mouseY, final float partialTicks) {
		if (tooltipCount > 0 || !getParent().isOpen() || getParent().isDisableOpen() || !getParent().isVisible()
				|| SliderComponent.changingSlider != null || tooltipBtn != this
				|| ClickGui.instance.frontmostFrame(mouseX, mouseY) != getParent()) {
			return false;
		}
		final int MAX_LEN = 120;
		int width = 0;
		final String tooltipStr = tooltip();
		for (final String formattedStr : Lanius.mc.fontRenderer.listFormattedStringToWidth(tooltipStr, MAX_LEN)) {
			if (Lanius.mc.fontRenderer.getStringWidth(formattedStr) > width) {
				width = Lanius.mc.fontRenderer.getStringWidth(formattedStr);
			}
		}
		Gui.drawRect(mouseX, mouseY, mouseX + width + 3,
				mouseY + Lanius.mc.fontRenderer.getWordWrappedHeight(tooltipStr, MAX_LEN) + 3, 0x80000000);
		Lanius.mc.fontRenderer.drawSplitString(tooltipStr, mouseX + 2, mouseY + 2, MAX_LEN, 16777215);
		return true;
	}

	final void setTooltipCount() {
		if (tooltipBtn != this) {
			tooltipCount = 20;
		}
	}

	protected abstract String tooltip();

	@Override
	public void update() {
		// TODO Auto-generated method stub
		final int mouseX = Mouse.getEventX() * ClickGui.instance.width / ClickGui.instance.mc.displayWidth,
				mouseY = ClickGui.instance.height
						- Mouse.getEventY() * ClickGui.instance.height / ClickGui.instance.mc.displayHeight - 1;
		final int x = getX(), y = getY();
		if (mouseX >= x && mouseX <= x + getWidth((byte) 0) && mouseY >= y && mouseY <= y + getHeight()
				&& getParent().isOpen() && !getParent().isDisableOpen() && getParent().isVisible()
				&& SliderComponent.changingSlider == null
				&& ClickGui.instance.frontmostFrame(mouseX, mouseY) == getParent()) {
			tooltipBtn = this;
			if (tooltipCount > 0) {
				--tooltipCount;
			}
		} else {
			tooltipCount = 20;
		}
	}
}
