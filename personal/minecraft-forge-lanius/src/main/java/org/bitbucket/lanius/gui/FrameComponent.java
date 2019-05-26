package org.bitbucket.lanius.gui;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bitbucket.lanius.Lanius;
import org.lwjgl.input.Mouse;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;

public final class FrameComponent extends BaseComponent
		implements UpdatableComponent, DraggableComponent, TypeableComponent {
	static boolean playSound;
	public final List<Component> children = new ArrayList<Component>();
	private final Dimension closedBounds, openBounds = new Dimension(0, 0);
	private boolean open, disableOpen;
	final Dimension openBtnBounds;
	Dimension pinBtnBounds;
	private int scrollIdx;
	private boolean visible;
	private boolean pinned;

	public FrameComponent(final String text, int x, int y, int width, int height) {
		super(text, x, y, width, height, null);
		openBtnBounds = new Dimension(getWidth((byte) 0) / 10, 1 + Lanius.mc.fontRenderer.FONT_HEIGHT);
		pinBtnBounds = new Dimension(openBtnBounds);
		closedBounds = new Dimension(getWidth((byte) 0), getHeight());
	}

	boolean addChild(final Component child) {
		return children.add(child);
	}

	@Override
	public void click(final int mouseX, final int mouseY, final int mouseBtn) {
		// TODO Auto-generated method stub
		final int x = getX(), y = getY();
		if (mouseX >= x + getWidth((byte) 0) - openBtnBounds.width - 1 && mouseX <= x + getWidth((byte) 0) - 2
				&& mouseY >= y + 2 && mouseY <= y + openBtnBounds.height && !disableOpen && isVisible()) {
			playSound = true; // Eric: Fixes an issue with sounds not playing with a fresh config.
			open();
		}
		if (mouseX >= x + getWidth((byte) 0) - openBtnBounds.width - 1 - pinBtnBounds.width - 1
				&& mouseX <= x + getWidth((byte) 0) - 2 - pinBtnBounds.width - 1 && mouseY >= y + 2
				&& mouseY <= y + openBtnBounds.height && !disableOpen && isVisible()) {
			playSound = true;
			togglePinned();
		}
		if (open && !disableOpen && isVisible()) {
			for (int childIdx = children.size() - 1; childIdx >= scrollIdx; childIdx--) {
				final Component child = children.get(childIdx);
				final int childX = child.getX(), childY = child.getY();
				if (mouseX >= childX && mouseX <= childX + child.getWidth((byte) 0) && mouseY >= childY
						&& mouseY <= childY + child.getHeight() && !(child instanceof TextFieldComponent)
						&& (child instanceof RoutineButton || mouseBtn == 0)) {
					child.click(mouseX, mouseY, mouseBtn);
					break;
				}
			}
		}
	}

	void clickTextFields(final int mouseX, final int mouseY, final int mouseBtn) {
		if (open && !disableOpen && isVisible()) {
			for (int childIdx = children.size() - 1; childIdx >= scrollIdx; childIdx--) {
				final Component child = children.get(childIdx);
				if (!(child instanceof TextFieldComponent)) {
					continue;
				}
				child.click(mouseX, mouseY, mouseBtn);
			}
		}
	}

	@Override
	public void drag(final int mouseX, final int mouseY) {
		// TODO Auto-generated method stub
		for (int childIdx = scrollIdx, childCount = 0; childIdx < children.size()
				&& childCount < ClickGui.MAX_CHILDREN; childIdx++, childCount++) {
			final Component child = children.get(childIdx);
			if (!(child instanceof DraggableComponent)) {
				continue;
			}
			((DraggableComponent) child).drag(mouseX, mouseY);
		}
	}

	Dimension getClosedBounds() {
		return closedBounds;
	}

	boolean isDisableOpen() {
		return disableOpen;
	}

	boolean isOpen() {
		return open;
	}

	public boolean isPinned() {
		return pinned;
	}

	public boolean isVisible() {
		return visible;
	}

	@Override
	public void keyTyped(final char typedChar, final int keyCode) throws IOException {
		// TODO Auto-generated method stub
		if (!isVisible() || !open || disableOpen) {
			return;
		}
		for (int childIdx = scrollIdx, childCount = 0; childIdx < children.size()
				&& childCount < ClickGui.MAX_CHILDREN; childIdx++, childCount++) {
			final Component child = children.get(childIdx);
			if (!(child instanceof TypeableComponent)) {
				continue;
			}
			((TypeableComponent) child).keyTyped(typedChar, keyCode);
		}
	}

	@Override
	public void move(int motionX, int motionY) {
		// TODO Auto-generated method stub
		if (getX() + motionX < 0) {
			motionX = 0;
		}
		if (getX() + getWidth((byte) 0) + motionX > ClickGui.instance.width) {
			motionX = ClickGui.instance.width - getX() - getWidth((byte) 0);
		}
		if (getY() + motionY < 0) {
			motionY = 0;
		}
		if (getY() + closedBounds.height + motionY > ClickGui.instance.height) {
			motionY = ClickGui.instance.height - getY() - closedBounds.height;
		}
		moveUnchecked(motionX, motionY);
	}

	void moveUnchecked(final int motionX, final int motionY) {
		super.move(motionX, motionY);
		for (final Component child : children) {
			child.move(motionX, motionY);
		}
		final ScaledResolution resolution = new ScaledResolution(Lanius.mc);
		final int width = resolution.getScaledWidth(), height = resolution.getScaledHeight(), y1 = height - 29;
		if (getX() >= 0 && getX() <= width && getY() >= y1 && getY() <= height && !disableOpen) {
			setDisableOpen();
		} else if ((getX() < 0 || getX() > width || getY() < y1 || getY() > height) && disableOpen) {
			setDisableOpen();
		}
	}

	void open() {
		if (!isVisible()) {
			return;
		}
		if (playSound) {
			Lanius.mc.getSoundHandler()
					.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}
		toggleOpen();
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		// TODO Auto-generated method stub
		if (!isVisible()) {
			return;
		}
		Gui.drawRect(getX(), getY(), getX() + getWidth((byte) 0), getY() + getHeight(), 0x80000000);
		final int openBtnX = getX() + getWidth((byte) 0) - openBtnBounds.width - 1, openBtnY = getY() + 2,
				openBtnRight = getX() + getWidth((byte) 0) - 2, openBtnBottom = getY() + openBtnBounds.height;
		if (!disableOpen) {
			Gui.drawRect(openBtnX, openBtnY, openBtnRight, openBtnBottom, 0x80808080);
			if (mouseX >= openBtnX && mouseX <= openBtnRight && mouseY >= openBtnY && mouseY <= openBtnBottom
					&& ClickGui.instance.frontmostFrame(mouseX, mouseY) == this) {
				Gui.drawRect(openBtnX, openBtnY, openBtnRight, openBtnBottom, 0x80202020);
			}
			Lanius.mc.fontRenderer.drawStringWithShadow(open ? "-" : "+",
					getX() + getWidth((byte) 0) - openBtnBounds.width + 1.5F, getY() + 2.5F, 16777215);

			int pinBtnX = openBtnX - pinBtnBounds.width - 1, pinBtnY = openBtnY, pinBtnRight = openBtnX - 1,
					pinBtnBottom = openBtnBottom;
			Gui.drawRect(pinBtnX, pinBtnY, pinBtnRight, pinBtnBottom, 0x80808080);
			if (mouseX >= pinBtnX && mouseX <= pinBtnRight && mouseY >= pinBtnY && mouseY <= pinBtnBottom
					&& ClickGui.instance.frontmostFrame(mouseX, mouseY) == this) {
				Gui.drawRect(pinBtnX, pinBtnY, pinBtnRight, pinBtnBottom, 0x80202020);
			}
			Lanius.mc.fontRenderer.drawStringWithShadow(pinned ? "o" : "x",
					getX() + getWidth((byte) 0) - pinBtnBounds.width * 2 + 1.0F, getY() + 1.5F, 16777215);
		}
		Lanius.mc.fontRenderer.drawStringWithShadow(text(), getX() + 2, getY() + 2, 16777215);
		if (open && !disableOpen) {
			int childCount = 0;
			for (int childIdx = scrollIdx; childIdx < children.size()
					&& childCount < ClickGui.MAX_CHILDREN; childIdx++, childCount++) {
				children.get(childIdx).render(mouseX, mouseY, partialTicks);
			}
			int maxChildHeight = 0;
			for (final Component child : children) {
				final int childHeight = child.getHeight();
				if (childHeight > maxChildHeight) {
					maxChildHeight = childHeight;
				}
			}
			final int scrollFactor = childCount * maxChildHeight + childCount;
			final int scrollTop = openBtnBottom + scrollIdx * scrollFactor / children.size() + 2;
			Gui.drawRect(getX() + getWidth((byte) 0) - 2, scrollTop, getX() + getWidth((byte) 0) - 1,
					Math.min(scrollTop
							+ scrollFactor * scrollFactor / (children.size() * maxChildHeight + children.size()),
							getY() + getHeight() - 1),
					0x80808080);
		}
	}

	boolean renderTooltips(final int mouseX, final int mouseY, final float partialTicks) {
		for (int childIdx = children.size() - 1; childIdx >= scrollIdx; childIdx--) {
			final Component child = children.get(childIdx);
			if (!(child instanceof TooltipButton)) {
				continue;
			}
			if (((TooltipButton) child).renderTooltip(mouseX, mouseY, partialTicks)) {
				return true;
			}
		}
		return false;
	}

	void scroll() {
		if (!open || disableOpen || !isVisible()) {
			return;
		}
		int dWheel = Mouse.getEventDWheel() * -1;
		if (dWheel == 0) {
			return;
		}
		if (dWheel > 1) {
			dWheel = 1;
		}
		if (dWheel < -1) {
			dWheel = -1;
		}
		final int prevScrollIdx = scrollIdx;
		scrollIdx += dWheel;
		final int maxIdx = children.size() - ClickGui.MAX_CHILDREN;
		if (scrollIdx > maxIdx) {
			scrollIdx = maxIdx;
		}
		if (scrollIdx < 0) {
			scrollIdx = 0;
		}
		if (prevScrollIdx != scrollIdx) {
			final int scrollHeight = children.get(prevScrollIdx).getHeight();
			for (final Component child : children) {
				child.move(0, (prevScrollIdx < scrollIdx ? -scrollHeight : scrollHeight) - dWheel);
			}
		}
	}

	void setBtnBounds() {
		openBtnBounds.setSize(getWidth((byte) 0) / 10, 1 + Lanius.mc.fontRenderer.FONT_HEIGHT);
		pinBtnBounds.setSize(openBtnBounds);
	}

	private void setDisableOpen() {
		disableOpen = !disableOpen;
		if (disableOpen) {
			setMinimizedBounds();
		} else {
			if (open) {
				setSize(openBounds.width, openBounds.height);
			} else {
				setSize(closedBounds.width, closedBounds.height);
			}
		}
	}

	private void setMinimizedBounds() {
		setSize(Lanius.mc.fontRenderer.getStringWidth(text()) + 4, closedBounds.height);
	}

	void setOpenBounds(final int width, final int height) {
		openBounds.setSize(width, height);
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	void toggleOpen() {
		open = !open;
		if (open && !disableOpen) {
			setSize(openBounds.width, openBounds.height);

		} else if (!open && !disableOpen) {
			setSize(closedBounds.width, closedBounds.height);
		} else {
			setMinimizedBounds();
		}
	}

	void togglePinned() {
		if (!isVisible()) {
			return;
		}
		if (playSound) {
			Lanius.mc.getSoundHandler()
					.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}
		setPinned(!isPinned());
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		for (final Component child : children) {
			if (child instanceof UpdatableComponent) {
				((UpdatableComponent) child).update();
			}
		}
	}

	void updateTooltips() {
		for (int childIdx = children.size() - 1; childIdx >= 0; childIdx--) {
			final Component child = children.get(childIdx);
			if (!(child instanceof TooltipButton)) {
				continue;
			}
			((TooltipButton) child).setTooltipCount();
		}
	}
}
