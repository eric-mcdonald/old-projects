package org.bitbucket.lanius.gui;

import org.bitbucket.lanius.Lanius;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;

public class ButtonComponent extends BaseComponent {
	private boolean selected;
	private FrameComponent childFrame;

	public ButtonComponent(final String text, int x, int y, int width, int height, final FrameComponent parent,
			final boolean selected) {
		super(text, x, y, width, height, parent);
		this.selected = selected;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void click(final int mouseX, final int mouseY, final int mouseBtn) {
		// TODO Auto-generated method stub
		Lanius.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		selected = !selected;
	}

	public FrameComponent getChildFrame() {
		return childFrame;
	}

	protected boolean isSelected() {
		return selected;
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		// TODO Auto-generated method stub
		final int x = getX(), y = getY(), right = x + getWidth((byte) 0), bottom = y + getHeight();
		Gui.drawRect(x, y, right, bottom, isSelected() ? 0x80FFFFFF : 0x80808080);
		if (mouseX >= x && mouseX <= right && mouseY >= y && mouseY <= bottom
				&& ClickGui.instance.frontmostFrame(mouseX, mouseY) == getParent()
				&& SliderComponent.changingSlider == null) {
			Gui.drawRect(x, y, right, bottom, 0x80202020);
		}
		final String text = text();
		Lanius.mc.fontRenderer.drawStringWithShadow(text,
				getX() + getWidth((byte) 0) / 2 - Lanius.mc.fontRenderer.getStringWidth(text) / 2, getY() + 2,
				16777215);
	}

	public void setChildFrame(FrameComponent childFrame) {
		this.childFrame = childFrame;
	}
}
