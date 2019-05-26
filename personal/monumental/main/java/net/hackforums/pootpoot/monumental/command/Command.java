package net.hackforums.pootpoot.monumental.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.util.IChatComponent;

public interface Command extends ICommand {

	List<Command> COMMANDS = new ArrayList<Command>();
	IChatComponent getDescription();
	IChatComponent getModName();
	IChatComponent getModUsage();
	boolean modExecute(String[] arguments);
}
