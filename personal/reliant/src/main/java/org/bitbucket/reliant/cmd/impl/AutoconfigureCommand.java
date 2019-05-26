package org.bitbucket.reliant.cmd.impl;

import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cmd.BaseCommand;
import org.bitbucket.reliant.cmd.CommandException;
import org.bitbucket.reliant.routine.impl.AutoconfigureRoutine;

public final class AutoconfigureCommand extends BaseCommand {

	public AutoconfigureCommand() {
		super("autoconfigure", "autoconfig", "autocfg");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String[] args, final List<String> output) throws CommandException {
		// TODO Auto-generated method stub
		final AutoconfigureRoutine autoCfgRoutine = (AutoconfigureRoutine) Reliant.instance.getRoutineRegistry().get("Auto-configure");
		if (args.length <= 0) {
			ToggleCommand.toggleRoutine(autoCfgRoutine, output);
			return;
		}
		if (args[0].equalsIgnoreCase("clear")) {
			autoCfgRoutine.names.clear();
			output.add(Reliant.instance.getI18n().format("auto-cfg.cleared.list"));
		} else {
			assertUsage(args.length > 1);
			final String player = StringUtils.arguments(args, 1);
			if (args[0].equalsIgnoreCase("add")) {
				autoCfgRoutine.names.add(player);
				output.add(Reliant.instance.getI18n().format("auto-cfg.added.player", player));
			} else if (args[0].equalsIgnoreCase("remove")) {
				autoCfgRoutine.names.remove(player);
				output.add(Reliant.instance.getI18n().format("auto-cfg.removed.player", player));
			} else {
				assertUsage();
			}
		}
	}

	@Override
	public String paramUsage() {
		// TODO Auto-generated method stub
		return "[add|remove|clear] [" + Reliant.instance.getI18n().format("auto-cfg.usage.value") + "]";
	}

}
