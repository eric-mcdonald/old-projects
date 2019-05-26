package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.routine.impl.SpamRoutine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class SpamCommand extends ModCommand {

	public SpamCommand() {
		super("spam");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<add|remove|clear> [message|index]";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length <= 0 || !args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove")
				&& !args[0].equalsIgnoreCase("clear"), sender);
		final SpamRoutine spamRoutine = (SpamRoutine) Lanius.getInstance().getRoutineRegistry().get("Spam");
		if (args[0].equalsIgnoreCase("add")) {
			final String message = argumentStr(args, 1);
			spamRoutine.addLine(message);
			CommandUtils.addText(sender, "Added '" + message + "' to the list of spam.");
		} else if (args[0].equalsIgnoreCase("remove")) {
			CommandUtils.addText(sender,
					"Removed '" + spamRoutine.removeLine(Integer.parseInt(args[1])) + "' from the list of spam.");
		} else {
			spamRoutine.clearLines();
			CommandUtils.addText(sender, "Removed all spam from the list.");
		}
	}

}
