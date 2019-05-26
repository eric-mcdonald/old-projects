package net.minecraft.scooby.command.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.scooby.Scooby;
import net.minecraft.scooby.command.Command;
import net.minecraft.scooby.mode.Mode;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class BindCommand extends Command {

	public BindCommand(Scooby scooby) {
		super(scooby, "-bind", "-bind <set|del|clear> [mode] [key]");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(ICommandSender sender, String[] args)
			throws CommandException {
		// TODO Auto-generated method stub
		if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {
			for (Mode mode : scooby.modeHandler.getModes()) {
				if (mode.getToggleKey() != -1) {
					for (Mode mode1 : scooby.modeHandler.getModes()) {
						mode1.setToggleKey(-1);
					}
					addCommandMessage("removed all key binds");
					return;
				}
			}
			addCommandMessage("cannot remove all key binds because there are no key binds");
		}
		else if (args.length == 2 && args[0].equalsIgnoreCase("del")) {
			for (Mode mode : scooby.modeHandler.getModes()) {
				if (mode.getConfigName().equalsIgnoreCase(args[1])) {
					String name = mode.getName();
					if (mode.getToggleKey() == -1) {
						addCommandMessage("cannot unbind mode " + name + " because it does not have a key bind");
						return;
					}
					mode.setToggleKey(-1);
					addCommandMessage("mode " + name + " has been unbound");
					return;
				}
			}
			addCommandMessage("cannot unbind mode " + args[1] + " because it does not exist");
		}
		else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
			String keyName = args[2].toUpperCase();
			for (Mode mode : scooby.modeHandler.getModes()) {
				if (mode.getConfigName().equalsIgnoreCase(args[1])) {
					String name = mode.getName();
					int toggleKey = Keyboard.getKeyIndex(keyName);
					if (toggleKey == Keyboard.KEY_NONE) {
						toggleKey = Mouse.getButtonIndex(keyName);
						if (toggleKey == -1) {
							addCommandMessage("usage: " + getCommandUsage());
							return;
						}
					}
					if (toggleKey == mode.getToggleKey()) {
						addCommandMessage("cannot bind mode " + name + " to key " + keyName + " because it is already bound to that key");
						return;
					}
					mode.setToggleKey(toggleKey);
					addCommandMessage("mode " + name + " has been bound to key " + keyName);
					return;
				}
			}
			addCommandMessage("cannot bind mode " + args[1] + " to key " + keyName + " because it does not exist");
		}
		else {
			addCommandMessage("usage: " + getCommandUsage());
		}
	}

}
