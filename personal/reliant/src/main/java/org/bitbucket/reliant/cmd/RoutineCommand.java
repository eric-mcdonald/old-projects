package org.bitbucket.reliant.cmd;

import java.util.List;

import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.routine.Routine;

public abstract class RoutineCommand extends BaseCommand {

	public RoutineCommand(String name, String... aliases) {
		super(name, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public final void execute(String[] args, final List<String> output) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length > 0);
		for (final String argument : args) {
			final Routine foundRoutine = Reliant.instance.getRoutineRegistry().get(argument);
			if (foundRoutine == null) {
				output.add("Could not find a routine with name " + argument);
			} else {
				handleRoutine(foundRoutine, output);
			}
		}
	}

	protected abstract void handleRoutine(final Routine routine, final List<String> output);

	@Override
	public final String paramUsage() {
		// TODO Auto-generated method stub
		return "<routine> [routine2]";
	}
}
