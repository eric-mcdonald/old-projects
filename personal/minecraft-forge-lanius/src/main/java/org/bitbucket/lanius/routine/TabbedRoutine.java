package org.bitbucket.lanius.routine;

import org.bitbucket.lanius.gui.Tab;

public abstract class TabbedRoutine extends Routine {
	private final Tab guiTab;

	public TabbedRoutine(final int defaultKey, boolean hidden, final Tab guiTab) {
		super(defaultKey, hidden);
		this.guiTab = guiTab;
		guiTab.addRoutine(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return guiTab.color;
	}

	public final Tab getGuiTab() {
		return guiTab;
	}
}
