package org.bitbucket.lanius.gui;

import java.awt.Color;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.routine.TabbedRoutine;

import net.minecraft.client.gui.Gui;

public final class RoutineButton extends TooltipButton {
	private final TabbedRoutine routine;

	public RoutineButton(final TabbedRoutine routine, int x, int y, int width, int height,
			final FrameComponent parent) {
		super(routine.name(), x, y, width, height, parent, routine.isEnabled());
		this.routine = routine;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void click(final int mouseX, final int mouseY, final int mouseBtn) {
		// TODO Auto-generated method stub
		if (mouseBtn == 0) {
			super.click(mouseX, mouseY, mouseBtn);
			routine.setEnabled();
		} else if (mouseBtn == 1 && getChildFrame() != null) {
			super.click(mouseX, mouseY, mouseBtn);
			getChildFrame().setVisible(!getChildFrame().isVisible());
			if (getChildFrame().isVisible()) {
				getChildFrame().move(mouseX - getChildFrame().getX(), mouseY - getChildFrame().getY());
				ClickGui.instance.bringToFront(getChildFrame());
			}
		}
	}

	@Override
	protected boolean isSelected() {
		return routine.isEnabled();
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		// TODO Auto-generated method stub
		final Color color = new Color(routine.getGuiTab().color),
				tabCol = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
		final int x = getX(), y = getY(), right = x + getWidth((byte) 0), bottom = y + getHeight();
		Gui.drawRect(x, y, right, bottom, isSelected() ? tabCol.getRGB() : 0x80808080);
		if (mouseX >= x && mouseX <= right && mouseY >= y && mouseY <= bottom
				&& ClickGui.instance.frontmostFrame(mouseX, mouseY) == getParent()
				&& SliderComponent.changingSlider == null) {
			Gui.drawRect(x, y, right, bottom, 0x80202020);
		}
		String text = text();
		Lanius.mc.fontRenderer.drawStringWithShadow(text,
				getX() + getWidth((byte) 0) / 2 - Lanius.mc.fontRenderer.getStringWidth(text) / 2, getY() + 2,
				16777215);
		if (getChildFrame() != null) {
			text = getChildFrame().isVisible() ? "-" : "+";
			Lanius.mc.fontRenderer.drawStringWithShadow(text,
					getX() + getWidth((byte) 0) - (Lanius.mc.fontRenderer.getStringWidth(text) + 1), getY() + 3,
					16777215);
		}
	}

	@Override
	protected String tooltip() {
		// TODO Auto-generated method stub
		return routine.description();
	}
}
