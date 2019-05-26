package org.bitbucket.reliant.routine;

import java.util.Comparator;

import javax.swing.JCheckBox;

public interface Routine {
	Comparator<Routine> priorityCmp = new Comparator<Routine>() {

		@Override
		public int compare(Routine o1, Routine o2) {
			// TODO Auto-generated method stub
			return o1.getPriority() == o2.getPriority() ? 0 : o1.getPriority() < o2.getPriority() ? -1 : 1;
		}
		
	};
	
	String description();
	int getPriority();
	JCheckBox guiComponent();
	boolean ignoresMouse();
	boolean inGameOnly();
	boolean isEnabled();
	String name();
	void reset(final boolean shutdown);
	void setEnabled();
	void update(final boolean post);
}
