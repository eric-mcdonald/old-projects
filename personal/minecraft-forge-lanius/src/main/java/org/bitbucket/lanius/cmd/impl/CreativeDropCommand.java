package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.InvalidItemException;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.routine.impl.CreativeDropRoutine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

public final class CreativeDropCommand extends ModCommand {

	public CreativeDropCommand() {
		super("creativedrop", "drop");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<add|remove> <item> [item2]";
	}

	@Override
	public void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length <= 0, sender);
		final boolean addParam = args[0].equalsIgnoreCase("add");
		assertUsage(!addParam && !args[0].equalsIgnoreCase("remove") || args.length <= 1, sender);
		final CreativeDropRoutine dropRoutine = (CreativeDropRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Creative Drop");
		for (int argIdx = 1; argIdx < args.length; argIdx++) {
			args[argIdx] = args[argIdx].toLowerCase();
			if (Item.getByNameOrId(args[argIdx]) == null) {
				throw new InvalidItemException(args[argIdx]);
			}
			if (addParam) {
				dropRoutine.putExempt(args[argIdx]);
			} else {
				dropRoutine.removeExempt(args[argIdx]);
			}
			CommandUtils.addText(sender, (addParam ? "Added" : "Removed") + " " + args[argIdx] + " "
					+ (addParam ? "to" : "from") + " the list of exempt items.");
		}
	}

}
