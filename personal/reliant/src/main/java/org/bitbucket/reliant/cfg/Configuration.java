package org.bitbucket.reliant.cfg;

import java.util.List;

public interface Configuration extends Configurable {
	Option<?> getOptionByName(final String name);
	List<Option<?>> getOptions();
	void loadDefaults();
	String name();
}
