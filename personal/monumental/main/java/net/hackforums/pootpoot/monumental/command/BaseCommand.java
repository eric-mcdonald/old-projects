package net.hackforums.pootpoot.monumental.command;

import net.hackforums.pootpoot.monumental.Monumental;
import net.hackforums.pootpoot.monumental.util.MessageType;
import net.hackforums.pootpoot.monumental.util.MessageUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public abstract class BaseCommand extends CommandBase implements Command {

	private IChatComponent name, description, usage;
	public BaseCommand(IChatComponent name, IChatComponent description, IChatComponent usage) {
		if (name == null) {
			throw new NullPointerException(Monumental.NAME + ": name cannot be null");
		}
		if (description == null) {
			throw new NullPointerException(Monumental.NAME + ": description cannot be null");
		}
		if (usage == null) {
			throw new NullPointerException(Monumental.NAME + ": usage cannot be null");
		}
		this.name = name;
		this.description = description;
		this.usage = usage;
		if (COMMANDS.contains(this)) {
			throw new CommandException(Monumental.NAME + ": duplicate Command instantiated");
		}
		COMMANDS.add(this);
	}
	public BaseCommand(String name, String description, String usage) {
		this(new ChatComponentText(name), new ChatComponentText(description), new ChatComponentText(usage));
	}
	@Override
	public boolean equals(Object obj) {
		return obj != null ? obj.toString().equals(toString()) : false;
	}
	@Override
	public void execute(ICommandSender sender, String[] args)
			throws net.minecraft.command.CommandException {
		// TODO Auto-generated method stub
		if (!modExecute(args)) {
			MessageUtils.addMessage(getModName().appendText(": usage: ").appendSibling(getModUsage()), MessageType.ERROR);
		}
	}
	@Override
	public String getCommandUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return usage.getUnformattedTextForChat();
	}
	@Override
	public IChatComponent getDescription() {
		// TODO Auto-generated method stub
		return description.createCopy();
	}
	@Override
	public IChatComponent getModName() {
		// TODO Auto-generated method stub
		return name.createCopy();
	}
	@Override
	public IChatComponent getModUsage() {
		// TODO Auto-generated method stub
		return usage.createCopy();
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return usage.getUnformattedTextForChat().split(" ")[0];
	}
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
	@Override
	public String toString() {
		return "BaseCommand{name=" + name + ", description=" + description + ", usage=" + usage + "}";
	}
}
