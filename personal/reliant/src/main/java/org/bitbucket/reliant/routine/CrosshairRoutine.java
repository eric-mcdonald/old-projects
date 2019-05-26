package org.bitbucket.reliant.routine;

import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.cfg.Option;
import org.bitbucket.reliant.handler.CrosshairHandler;
import org.bitbucket.reliant.memory.MemoryStream;

public abstract class CrosshairRoutine extends DisplayRoutine implements CrosshairHandler {
	private int crosshairEntity = MemoryStream.NULL;
	
	public CrosshairRoutine(final String name, final String description, final boolean inGameOnly, final boolean defaultEnabled, final int defaultKey, final int priority, final Option<?>... options) {
		super(name, description, inGameOnly, defaultEnabled, defaultKey, priority, options);
	}
	
	public CrosshairRoutine(final String name, final String description, final boolean inGameOnly, final boolean defaultEnabled, final int priority, final Option<?>... options) {
		super(name, description, inGameOnly, defaultEnabled, priority, options);
	}
	
	public CrosshairRoutine(final String name, final String description, final boolean inGameOnly, final int priority, final Option<?>... options) {
		super(name, description, inGameOnly, priority, options);
	}

	@Override
	public final int getCrosshairEntity() {
		// TODO Auto-generated method stub
		return crosshairEntity == MemoryStream.NULL ? GameCache.getCrosshairEntity() : crosshairEntity;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		crosshairEntity = MemoryStream.NULL;
	}

	@Override
	public final void setCrosshairEntity(final int crosshairEntity) {
		// TODO Auto-generated method stub
		this.crosshairEntity = crosshairEntity;
	}
}
