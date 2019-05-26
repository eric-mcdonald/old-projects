package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.InvalidMatchTypeException;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.routine.impl.AutoRespondRoutine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class AutoRespondCommand extends ModCommand {

	public AutoRespondCommand() {
		super("autorespond", "respond");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<add|remove|clear> ['key'|index] ['response'] [matchType] [ignoreCase]";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length == 0 || !args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove")
				&& !args[0].equalsIgnoreCase("clear"), sender);
		AutoRespondRoutine respondRoutine = (AutoRespondRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Auto-respond");
		if (args[0].equalsIgnoreCase("add")) {
			assertUsage(args.length < 5, sender);
			String[] argFields = argumentStr(args, 1).split("'");
			assertUsage(argFields.length < 4, sender);
			AutoRespondRoutine.ResponseEntry.MatchType matchType = AutoRespondRoutine.ResponseEntry.MatchType
					.getById(args[args.length - 2]);
			if (matchType == null) {
				throw new InvalidMatchTypeException(args[args.length - 2]);
			}
			respondRoutine.getResponses().add(new AutoRespondRoutine.ResponseEntry(argFields[1], argFields[3],
					matchType, Boolean.parseBoolean(args[args.length - 1])));
			CommandUtils.addText(sender, "Added a response entry with key \"" + argFields[1] + "\" and match type "
					+ matchType.getId() + ".");
		} else if (args[0].equalsIgnoreCase("remove")) {
			assertUsage(args.length < 2, sender);
			int index = Integer.parseInt(args[1]);
			try {
				respondRoutine.getResponses().remove(index);
			} catch (IndexOutOfBoundsException ex) {
				// Empty stub implementation
			}
			CommandUtils.addText(sender, "Removed the response entry at index " + index + ".");
		} else {
			respondRoutine.getResponses().clear();
			CommandUtils.addText(sender, "Removed all response entries.");
		}
	}

}
