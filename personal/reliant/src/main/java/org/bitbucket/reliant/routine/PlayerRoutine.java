package org.bitbucket.reliant.routine;

import org.bitbucket.reliant.cfg.Option;
import org.bitbucket.reliant.handler.PlayerHandler;

public abstract class PlayerRoutine extends DisplayRoutine implements PlayerHandler {
	private boolean updated;
	
	public PlayerRoutine(final String name, final String description, final boolean inGameOnly, final boolean ignoresMouse, final boolean defaultEnabled, final int priority, final Option<?>... options) {
		super(name, description, defaultEnabled, ignoresMouse, inGameOnly, priority, options); // TODO(Eric) Uhhh parameters are out of order?
		// TODO Auto-generated constructor stub
	}
	
	public PlayerRoutine(final String name, final String description, final boolean inGameOnly, final boolean defaultEnabled, final int defaultKey, final int priority, final Option<?>... options) {
		super(name, description, inGameOnly, defaultEnabled, defaultKey, priority, options);
	}
	
	public PlayerRoutine(final String name, final String description, final boolean inGameOnly, final boolean defaultEnabled, final int priority, final Option<?>... options) {
		super(name, description, defaultEnabled, inGameOnly, priority, options);
		// TODO Auto-generated constructor stub
	}
	
	public PlayerRoutine(final String name, final String description, final boolean inGameOnly, final int priority, final Option<?>... options) {
		super(name, description, inGameOnly, priority, options);
	}

	@Override
	public boolean handle(final int player, final int entityIdx) {
		// TODO Auto-generated method stub
		return updated;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		updated = false;
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post) {
			return;
		}
		updated = true;
	}
}
