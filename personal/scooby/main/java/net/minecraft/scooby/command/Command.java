package net.minecraft.scooby.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.scooby.Scooby;
import net.minecraft.util.ChatComponentText;

public abstract class Command extends CommandBase {

	protected final Scooby scooby;
	private String name, usage;
	public Command(Scooby scooby, String name, String usage) {
		this.scooby = scooby;
		this.name = name;
		this.usage = usage;
	}
	protected void addCommandMessage(String message) {
		scooby.mc.thePlayer.addChatMessage(new ChatComponentText("\247c[" + Scooby.NAME + "]:\247r " + message));
	}

	/**
	 * Convenience method.
	 * @return
	 */
	public String getCommandUsage() {
		return getCommandUsage(scooby.mc.thePlayer);
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return usage;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
}
