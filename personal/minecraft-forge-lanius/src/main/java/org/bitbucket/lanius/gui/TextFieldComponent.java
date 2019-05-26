package org.bitbucket.lanius.gui;

import java.io.IOException;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public final class TextFieldComponent extends GuiTextField implements UpdatableComponent, TypeableComponent {

	public final ConfigContainer cfgContainer;
	private final FrameComponent parent;
	private final String text;

	public TextFieldComponent(int componentId, FontRenderer fontRenderer, int x, int y, int width, int height,
			final FrameComponent parent, final String text, final ConfigContainer cfgContainer) {
		super(componentId, fontRenderer, x, y, width, height);
		this.parent = parent;
		this.text = text;
		this.cfgContainer = cfgContainer;
		setText(cfgContainer.getString(text));
		setEnableBackgroundDrawing(false);
		setTextColor(16777215);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void click(final int mouseX, final int mouseY, final int mouseBtn) {
		// TODO Auto-generated method stub
		mouseClicked(mouseX, mouseY, mouseBtn);
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	@Override
	public FrameComponent getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public int getWidth(final byte unused) {
		return getWidth();
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public void keyTyped(final char typedChar, final int keyCode) throws IOException {
		textboxKeyTyped(typedChar, keyCode);
		if (keyCode == 15) {
			setFocused(false);
		}
	}

	@Override
	public void move(int motionX, int motionY) {
		// TODO Auto-generated method stub
		x += motionX;
		y += motionY;
	}

	@Override
	public void render(float partialTicks) {
		// TODO Auto-generated method stub
		render(-1, -1, partialTicks);
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		// TODO Auto-generated method stub
		drawRect(getX(), getY(), getX() + getWidth((byte) 0), getY() + getHeight(), 0x80808080);
		move(2, 2);
		drawTextBox();
		move(-2, -2);
	}

	@Override
	public String text() {
		// TODO Auto-generated method stub
		return text;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		updateCursorCounter();
		final String name = text(), value = getText();
		if (!value.equals(cfgContainer.getString(name))) {
			cfgContainer.putValue(name, value);
		}
		if (Lanius.mc.currentScreen == null && getParent() != null && getParent().isPinned()
				&& !getParent().isDisableOpen()) {
			setFocused(false);
		}
	}

}
