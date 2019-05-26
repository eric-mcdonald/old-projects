package org.bitbucket.reliant.cfg;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

public abstract class NumberOption<N extends Number & Comparable<N>> extends BaseOption<ClampedNumber<N>> implements ChangeListener {
	public NumberOption(final String name, final String description, final ClampedNumber<N> defaultVal) {
		this(name, description, defaultVal, defaultVal.getMin().intValue(), defaultVal.getMax().intValue(), defaultVal.getValue().intValue());
		// TODO Auto-generated constructor stub
	}
	
	public NumberOption(final String name, final String description, final ClampedNumber<N> defaultVal, final int min, final int max, final int value) {
		super(name, description, defaultVal, new JSlider(min, max, value));
		final JSlider guiComponent = (JSlider) guiComponent();
		guiComponent.setValue(value);
		guiComponent.addChangeListener(this);
	}
	
	public final ClampedNumber<N> createValue(final N value) {
		return new ClampedNumber<N>(value, getValue().getMin(), getValue().getMax());
	}
	
	@Override
	protected void refreshComponent() {
		((JSlider) guiComponent()).setValue(getValue().intValue());
	}
}
