package org.bitbucket.reliant.cfg;

import java.awt.Component;

public interface Option<V> {
	String description();
	Object getCfgDefault();
	Object getCfgValue();
	V getDefault();
	V getValue();
	Component guiComponent();
	String name();
	V parseValue(final String value);
	void setValue(final Object value);
}
