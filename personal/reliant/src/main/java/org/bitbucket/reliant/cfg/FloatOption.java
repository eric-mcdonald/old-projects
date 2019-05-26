package org.bitbucket.reliant.cfg;

import java.util.Enumeration;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public final class FloatOption extends NumberOption<Float> {
	private static final int PRECISION = 10;
	private boolean cancelChange;
	
	public FloatOption(final String name, final String description, final ClampedNumber<Float> defaultVal, final float increment) {
		super(name, description, defaultVal, defaultVal.getMin().intValue() * PRECISION, defaultVal.getMax().intValue() * PRECISION, defaultVal.getValue().intValue() * PRECISION);
		final JSlider guiComponent = (JSlider) guiComponent();
		guiComponent.setMajorTickSpacing((int) (Math.abs(defaultVal.getMax() - defaultVal.getMin()) / 5.0F * PRECISION));
		guiComponent.setMinorTickSpacing((int) (increment * PRECISION));
		guiComponent.setPaintTicks(true);
		guiComponent.setPaintLabels(true);
		final Enumeration<?> labelEnum = guiComponent.getLabelTable().elements();
		while (labelEnum.hasMoreElements()) {
			final Object labelObj = labelEnum.nextElement();
			if (labelObj instanceof JLabel) {
				final JLabel label = (JLabel) labelObj;
				label.setText(String.valueOf((int) (Float.parseFloat(label.getText()) / PRECISION)));
			}
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	public ClampedNumber<Float> parseValue(final String value) {
		// TODO Auto-generated method stub
		return createValue(Float.valueOf(value));
	}

	@Override
	protected void refreshComponent() {
		cancelChange = true;
		((JSlider) guiComponent()).setValue(getValue().intValue() * PRECISION);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		final JSlider guiComponent = (JSlider) e.getSource();
		if (guiComponent.getValueIsAdjusting()) {
			return;
		}
		if (cancelChange) {
			cancelChange = false;
			return;
		}
		setValue(createValue((float) guiComponent.getValue() / PRECISION));
	}
}
