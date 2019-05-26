package org.bitbucket.lanius.gui;

public interface Component {
	void click(final int mouseX, final int mouseY, final int mouseBtn);

	int getHeight();

	FrameComponent getParent();

	/**
	 * Retrieves the width of this component. The unused parameter is there because
	 * GuiTextField already has a getWidth() method.
	 * 
	 * @param unused the unused parameter
	 * @return the width of this component
	 */
	int getWidth(final byte unused);

	int getX();

	int getY();

	void move(int motionX, int motionY);

	void render(float partialTicks);

	void render(final int mouseX, final int mouseY, final float partialTicks);

	String text();
}
