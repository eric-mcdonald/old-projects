package org.bitbucket.reliant.cfg;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;

public final class BoolOption extends BaseOption<Boolean> implements ItemListener {
	public BoolOption(final String name, final String description, final Boolean defaultVal) {
		super(name, description, defaultVal, new JCheckBox(name));
		final JCheckBox guiComponent = (JCheckBox) guiComponent();
		guiComponent.setToolTipText(description);
		guiComponent.setSelected(defaultVal);
		guiComponent.addItemListener(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		setValue(e.getStateChange() == ItemEvent.SELECTED);
	}
	
	@Override
	public Boolean parseValue(final String value) {
		// TODO Auto-generated method stub
		return Boolean.valueOf(value);
	}

	@Override
	protected void refreshComponent() {
		((AbstractButton) guiComponent()).setSelected(getValue());
	}
}
