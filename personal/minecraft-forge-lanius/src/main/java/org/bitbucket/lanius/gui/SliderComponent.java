package org.bitbucket.lanius.gui;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ClampedNumber;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;

public abstract class SliderComponent<N extends Number & Comparable<N>> extends BaseComponent
		implements DraggableComponent {
	protected static SliderComponent changingSlider;
	protected final ClampedNumber<N> clampedVal;

	public SliderComponent(final String text, int x, int y, int width, int height, final FrameComponent parent,
			final ClampedNumber<N> value) {
		super(text, x, y, width, height, parent);
		this.clampedVal = value;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void click(final int mouseX, final int mouseY, final int mouseBtn) {
		// TODO Auto-generated method stub
		if (mouseBtn != 0 || mouseX < getX() || mouseX > getX() + getWidth((byte) 0) || mouseY < getY()
				|| mouseY > getY() + getHeight()) {
			return;
		}
		drag(mouseX, mouseY);
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		// TODO Auto-generated method stub
		// Eric: prevents overlapping of non-opaque alphas
		final double sliderPercent = (clampedVal.getValue().doubleValue() - clampedVal.getMin().doubleValue())
				/ (clampedVal.getMax().doubleValue() - clampedVal.getMin().doubleValue());
		final int sliderEnd = (int) (getX() + getWidth((byte) 0) * sliderPercent);
		Gui.drawRect(sliderEnd, getY(), MathHelper.ceil(sliderEnd + getWidth((byte) 0) * (1.0D - sliderPercent)),
				getY() + getHeight(), 0x80808080);
		Gui.drawRect(getX(), getY(), sliderEnd, getY() + getHeight(), 0x80FFFFFF);
		Lanius.mc.fontRenderer.drawStringWithShadow(text(), getX() + 2, getY() + 2, 16777215);
		Lanius.mc.fontRenderer.drawStringWithShadow(valueTxt(),
				getX() + getWidth((byte) 0) - Lanius.mc.fontRenderer.getStringWidth(valueTxt()) - 2, getY() + 2,
				16777215);
	}

	protected abstract String valueTxt();
}
