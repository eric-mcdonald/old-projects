package org.bitbucket.lanius.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bitbucket.lanius.routine.Routine;

public enum Tab {
	COMBAT("Combat", 0xFE2E2E), MISCELLANEOUS("Miscellaneous", 0xFE9A2E), MOVEMENT("Movement", 0xFFFF00),
	PLAYER("Player", 0x00FF00), RENDER("Render", 0x2E9AFE), WORLD("World", 0xDA81F5);

	public final int color;
	public final String name;
	public boolean open;
	public int routineIdx;
	private final List<Routine> routines = new ArrayList<Routine>();

	private Tab(final String name, final int color) {
		this.name = name;
		this.color = color;
	}

	public boolean addRoutine(final Routine routine) {
		final boolean success = routines.add(routine);
		if (success) {
			Collections.sort(routines, new Comparator<Routine>() {

				@Override
				public int compare(Routine arg0, Routine arg1) {
					// TODO Auto-generated method stub
					return arg0.name().compareTo(arg1.name());
				}

			});
		}
		return success;
	}

	public Routine[] routines() {
		return routines.toArray(new Routine[0]);
	}
}
