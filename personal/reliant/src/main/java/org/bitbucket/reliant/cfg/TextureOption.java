package org.bitbucket.reliant.cfg;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.bitbucket.reliant.Reliant;

public final class TextureOption extends TextOption<Long> {

	public TextureOption(final String name, final String description, final String defaultVal) {
		super(name, description, Reliant.instance.getRenderer().createTexture(defaultVal), DirOption.MAX_PATH);
		cfgValue = cfgDefault = defaultVal;
		((JTextComponent) guiComponent()).setText(defaultVal);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Long parseValue(String value) {
		// TODO Auto-generated method stub
		cfgValue = value;
		return Reliant.instance.getRenderer().createTexture(value);
	}
	@Override
	protected void refreshComponent() {
		// TODO Auto-generated method stub
		((JTextField) guiComponent()).setText(getCfgValue().toString());
	}
	@Override
	public void setValue(final Object value) {
		final Object cfgValue = this.cfgValue;
		super.setValue(value);
		this.cfgValue = cfgValue;
		refreshComponent();
	}
}
