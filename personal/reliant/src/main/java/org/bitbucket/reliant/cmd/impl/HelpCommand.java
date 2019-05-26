package org.bitbucket.reliant.cmd.impl;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cmd.BaseCommand;
import org.bitbucket.reliant.cmd.Command;
import org.bitbucket.reliant.cmd.CommandException;
import org.bitbucket.reliant.routine.Routine;

public final class HelpCommand extends BaseCommand {

	public HelpCommand() {
		super("help", "?");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String[] args, final List<String> output) throws CommandException {
		// TODO Auto-generated method stub
		if (args.length <= 0 || args.length > 1 && args[0].equalsIgnoreCase("list") && args[1].equalsIgnoreCase("commands")) {
			final List<String> cmdNames = new ArrayList<String>();
			for (final Command cmd : Reliant.instance.getCmdRegistry().objects()) {
				String cmdPrefix = "";
				final String[] names = cmd.names();
				for (int nameIdx = 0; nameIdx < names.length; nameIdx++) {
					cmdPrefix += names[nameIdx];
					if (nameIdx < names.length - 1) {
						cmdPrefix += "|";
					}
				}
				cmdNames.add(cmdPrefix);
			}
			output.add(Reliant.instance.getI18n().format("help.list.cmds", Reliant.instance.getCmdRegistry().objects().size(), StringUtils.list(cmdNames.toArray(new String[0]), 0)));
			if (args.length <= 0) {
				output.add(Reliant.instance.getI18n().format("help.display.usage", names()[0]));
			}
		} else if (args.length > 1 && args[0].equalsIgnoreCase("list") && args[1].equalsIgnoreCase("routines")) {
			final List<String> routineNames = new ArrayList<String>();
			for (final Routine routine : Reliant.instance.getRoutineRegistry().objects()) {
				routineNames.add(StringUtils.configName(routine.name()));
			}
			output.add("Routines (" + Reliant.instance.getRoutineRegistry().objects().size() + "): " + StringUtils.list(routineNames.toArray(new String[0]), 0));
		} else if (args.length > 1 && args[0].equalsIgnoreCase("feature")) {
			for (int argIdx = 1; argIdx < args.length; argIdx++) {
				boolean hasFeature = false;
				final Routine foundRoutine = Reliant.instance.getRoutineRegistry().get(args[argIdx]);
				if (foundRoutine != null) {
					output.add(foundRoutine.name() + ": " + foundRoutine.description());
					hasFeature = true;
				}
				final Command foundCmd = Reliant.instance.getCmdRegistry().get(args[argIdx]);
				if (foundCmd != null) {
					output.add(foundCmd.usage());
					hasFeature = true;
				}
				if (!hasFeature) {
					output.add("Could not find a feature with name " + args[argIdx]);
				}
			}
		} else {
			assertUsage();
		}
	}

	@Override
	public String paramUsage() {
		// TODO Auto-generated method stub
		return "[feature|list] [name|commands|routines] [name2]";
	}

}
