package org.bitbucket.reliant.cmd.impl;

import java.util.List;

import org.bitbucket.reliant.cmd.RoutineCommand;
import org.bitbucket.reliant.routine.Routine;

public final class StateCommand extends RoutineCommand {

	public StateCommand() {
		super("state", "s");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleRoutine(final Routine routine, final List<String> output) {
		// TODO Auto-generated method stub
		output.add("Routine " + routine.name() + " is " + (routine.isEnabled() ? "enabled" : "disabled"));
	}

}
