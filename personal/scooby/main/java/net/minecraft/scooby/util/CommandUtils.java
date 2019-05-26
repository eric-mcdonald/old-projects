package net.minecraft.scooby.util;

import net.minecraft.scooby.Scooby;
import net.minecraft.scooby.command.Command;

public class CommandUtils {

	private Scooby scooby;
	public CommandUtils(Scooby scooby) {
		this.scooby = scooby;
	}
	public Command getCommandByName(String commandName) {
		for (Command command : scooby.commandHandler.getCommands()) {
			if (command.getName().equals(commandName)) {
				return command;
			}
		}
		return null;
	}
}
