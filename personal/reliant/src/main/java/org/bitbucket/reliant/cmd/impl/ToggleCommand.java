package org.bitbucket.reliant.cmd.impl;

import java.util.List;

import org.bitbucket.reliant.cmd.RoutineCommand;
import org.bitbucket.reliant.routine.Routine;

public final class ToggleCommand extends RoutineCommand {

	public static String toggleRoutine(final Routine routine, final List<String> output) {
		routine.setEnabled();
		final String message;
		output.add(message = (routine.isEnabled() ? "Enabled" : "Disabled") + " routine " + routine.name());
		return message;
	}

	public ToggleCommand() {
		super("toggle", "t");
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void handleRoutine(final Routine routine, final List<String> output) {
		// TODO Auto-generated method stub
		toggleRoutine(routine, output);
	}
}
