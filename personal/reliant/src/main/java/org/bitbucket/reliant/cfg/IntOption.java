package org.bitbucket.reliant.cfg;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public final class IntOption extends NumberOption<Integer> {
	public IntOption(final String name, final String description, final ClampedNumber<Integer> defaultVal, final int increment) {
		super(name, description, defaultVal);
		final JSlider guiComponent = (JSlider) guiComponent();
		guiComponent.setMajorTickSpacing(Math.max(Math.abs(defaultVal.getMax() - defaultVal.getMin()) / 5, 1));
		guiComponent.setMinorTickSpacing(increment);
		guiComponent.setPaintTicks(true);
		guiComponent.setPaintLabels(true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ClampedNumber<Integer> parseValue(final String value) {
		// TODO Auto-generated method stub
		return createValue(Integer.valueOf(value));
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		final JSlider guiComponent = (JSlider) e.getSource();
		if (guiComponent.getValueIsAdjusting()) {
			return;
		}
		setValue(createValue(guiComponent.getValue()));
	}
}
