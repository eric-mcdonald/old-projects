package org.bitbucket.reliant.cfg;

import java.awt.Component;

import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.ui.ConfigPanel;

public abstract class BaseOption<V> implements Option<V> {
	private final String name, description;
	private V value, defaultVal;
	private final Component guiComponent;
	protected Object cfgValue, cfgDefault;
	
	public BaseOption(final String name, final String description, final V defaultVal, final Component guiComponent) {
		this.name = name;
		this.description = description;
		value = this.defaultVal = defaultVal;
		this.guiComponent = guiComponent;
		cfgValue = value;
		cfgDefault = defaultVal;
	}
	
	@Override
	public final String description() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public final Object getCfgDefault() {
		return cfgDefault;
	}
	
	@Override
	public final Object getCfgValue() {
		return cfgValue;
	}

	@Override
	public final V getDefault() {
		// TODO Auto-generated method stub
		return defaultVal;
	}
	
	@Override
	public final V getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public final Component guiComponent() {
		return guiComponent;
	}
	
	@Override
	public final String name() {
		// TODO Auto-generated method stub
		return name;
	}
	
	private Configuration parent() {
		for (final Configuration config : Reliant.instance.getConfigRegistry().objects()) {
			for (final Option<?> option : config.getOptions()) {
				if (option.equals(this)) {
					return config;
				}
			}
		}
		return null;
	}
	
	protected abstract void refreshComponent();
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(final Object value) {
		// TODO Auto-generated method stub
		if (value.getClass() != this.value.getClass() || value.equals(this.value)) {
			return;
		}
		this.value = (V) value;
		cfgValue = value;
		refreshComponent();
		final ConfigPanel gui = Reliant.instance.getConfigGui();
		if (this instanceof NumberOption<?> && gui != null) {
			gui.updateNumLabel((NumberOption<?>) this);
		}
		if (gui != null) {
			if (value.equals(getDefault())) {
				gui.disableResetBtn(parent());
			} else {
				gui.toggleResetBtn(parent(), true);
			}
		}
	}
}
