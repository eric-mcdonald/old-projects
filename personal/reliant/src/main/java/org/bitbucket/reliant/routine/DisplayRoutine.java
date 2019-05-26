package org.bitbucket.reliant.routine;

import org.bitbucket.reliant.cfg.Option;

public abstract class DisplayRoutine extends BaseRoutine implements Displayable {
	public DisplayRoutine(final String name, final String description, final boolean inGameOnly, final boolean ignoresMouse, final boolean defaultEnabled, final int defaultKey, final int priority, final Option<?>... options) {
		super(name, description, inGameOnly, ignoresMouse, defaultEnabled, defaultKey, priority, options);
		// TODO Auto-generated constructor stub
	}
	
	public DisplayRoutine(final String name, final String description, final boolean inGameOnly, final boolean ignoresMouse, final boolean defaultEnabled, final int priority, final Option<?>... options) {
		super(name, description, inGameOnly, ignoresMouse, defaultEnabled, priority, options);
		// TODO Auto-generated constructor stub
	}
	
	public DisplayRoutine(final String name, final String description, final boolean inGameOnly, final boolean defaultEnabled, final int defaultKey, final int priority, final Option<?>... options) {
		super(name, description, inGameOnly, false, defaultEnabled, defaultKey, priority, options);
	}
	
	public DisplayRoutine(final String name, final String description, final boolean inGameOnly, final boolean defaultEnabled, final int priority, final Option<?>... options) {
		super(name, description, inGameOnly, false, defaultEnabled, priority, options);
		// TODO Auto-generated constructor stub
	}

	public DisplayRoutine(final String name, final String description, final boolean inGameOnly, final int priority, final Option<?>... options) {
		super(name, description, inGameOnly, false, false, priority, options);
	}
	
	@Override
	public String baseTxt() {
		// TODO Auto-generated method stub
		return name();
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}
}
