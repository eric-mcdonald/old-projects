package org.bitbucket.lanius.gui;

public abstract class BaseComponent implements Component {
	private final FrameComponent parent;
	private final String text;
	private int x, y, width, height;

	public BaseComponent(final String text, int x, int y, int width, int height, final FrameComponent parent) {
		this.text = text;
		// Eric: Cannot use BaseComponent#move because it references
		// ClickGui#instance
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.parent = parent;
	}

	@Override
	public final int getHeight() {
		return height;
	}

	@Override
	public FrameComponent getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public final int getWidth(final byte unused) {
		return width;
	}

	@Override
	public final int getX() {
		return x;
	}

	@Override
	public final int getY() {
		return y;
	}

	@Override
	public void move(int motionX, int motionY) {
		// TODO Auto-generated method stub
		x += motionX;
		y += motionY;
	}

	@Override
	public void render(float partialTicks) {
		render(-1, -1, partialTicks);
	}

	void setSize(final int width, final int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public String text() {
		// TODO Auto-generated method stub
		return text;
	}

	@Override
	public final String toString() {
		return text();
	}
}
