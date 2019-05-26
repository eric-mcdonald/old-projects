package org.bitbucket.reliant.cmd.impl;

import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cmd.BaseCommand;
import org.bitbucket.reliant.cmd.CommandException;
import org.bitbucket.reliant.routine.impl.CmdSpamRoutine;

public final class CmdSpamCommand extends BaseCommand {

	public CmdSpamCommand() {
		super("commandspammer", "cmdspammer", "cmdspam");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String[] args, final List<String> output) throws CommandException {
		// TODO Auto-generated method stub
		final CmdSpamRoutine cmdSpamRoutine = (CmdSpamRoutine) Reliant.instance.getRoutineRegistry().get("Command Spammer");
		if (args.length <= 0) {
			ToggleCommand.toggleRoutine(cmdSpamRoutine, output);
			return;
		}
		if (args[0].equalsIgnoreCase("clear")) {
			cmdSpamRoutine.commands.clear();
			output.add(Reliant.instance.getI18n().format("cmd_spam.cleared.list"));
		} else {
			assertUsage(args.length > 1);
			final String command = StringUtils.arguments(args, 1);
			if (args[0].equalsIgnoreCase("add")) {
				cmdSpamRoutine.commands.add(command);
				output.add(Reliant.instance.getI18n().format("cmd_spam.added.cmd", command));
			} else if (args[0].equalsIgnoreCase("remove")) {
				cmdSpamRoutine.commands.remove(command);
				output.add(Reliant.instance.getI18n().format("cmd_spam.removed.cmd", command));
			} else {
				assertUsage();
			}
		}
	}

	@Override
	public String paramUsage() {
		// TODO Auto-generated method stub
		return "[add|remove|clear] [" + Reliant.instance.getI18n().format("cmd_spam.usage.value") + "]";
	}

}
