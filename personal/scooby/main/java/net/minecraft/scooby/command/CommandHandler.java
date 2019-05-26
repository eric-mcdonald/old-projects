package net.minecraft.scooby.command;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.scooby.Scooby;
import net.minecraft.scooby.command.commands.BindCommand;
import net.minecraft.scooby.command.commands.HelpCommand;
import net.minecraft.scooby.command.commands.VelocityCommand;
import net.minecraft.scooby.handlers.Handler;
import net.minecraftforge.client.ClientCommandHandler;

/**
 * Literally just took brudin's ModeHandler and fixed shit cus I'm lazy and for consistency code-wise :^O
 * @author pootPoot
 * @since I started adding the command system :^)))
 */
public class CommandHandler implements Handler {

	private final List<Command> commands = new CopyOnWriteArrayList<Command>();

	public List<Command> getCommands() {
		return this.commands;
	}

	@Override
	public void init(Scooby scooby) {
		registerCommand(new BindCommand(scooby));
		registerCommand(new HelpCommand(scooby));
		registerCommand(new VelocityCommand(scooby));
	}

	private void registerCommand(Command command) {
		this.commands.add(command);
		ClientCommandHandler.instance.registerCommand(command);
	}
}
