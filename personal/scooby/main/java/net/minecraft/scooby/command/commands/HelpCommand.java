package net.minecraft.scooby.command.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.scooby.Scooby;
import net.minecraft.scooby.command.Command;
import net.minecraft.scooby.mode.Mode;

public class HelpCommand extends Command {

	public HelpCommand(Scooby scooby) {
		super(scooby, "-help", "-help [commands|modes]");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(ICommandSender sender, String[] args)
			throws CommandException {
		// TODO Auto-generated method stub
		if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("commands"))) {
			addCommandMessage("commands: ");
			for (Command command : scooby.commandHandler.getCommands()) {
				addCommandMessage(command.getCommandUsage());
			}
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("modes")) {
			addCommandMessage("modes: ");
			for (Mode mode : scooby.modeHandler.getModes()) {
				addCommandMessage(mode.getName());
			}
		}
		else {
			addCommandMessage("usage: " + getCommandUsage());
		}
	}

}
