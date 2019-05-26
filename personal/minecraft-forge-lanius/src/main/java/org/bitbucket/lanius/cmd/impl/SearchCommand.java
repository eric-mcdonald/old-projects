package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.InvalidBlockException;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.routine.impl.SearchRoutine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class SearchCommand extends ModCommand {

	public SearchCommand() {
		super("search");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<add|remove|clear> [block] [block2]";
	}

	@Override
	public void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length <= 0, sender);
		SearchRoutine searchRoutine = (SearchRoutine) Lanius.getInstance().getRoutineRegistry().get("Search");
		if (args[0].equalsIgnoreCase("clear")) {
			CommandUtils.addText(sender, "Removed all blocks from the search list.");
			searchRoutine.clearBlocks();
			return;
		}
		final boolean addParam = args[0].equalsIgnoreCase("add");
		assertUsage(!addParam && !args[0].equalsIgnoreCase("remove") || args.length <= 1, sender);
		for (int argIdx = 1; argIdx < args.length; argIdx++) {
			args[argIdx] = args[argIdx].toLowerCase();
			if (Block.getBlockFromName(args[argIdx]) == null) {
				throw new InvalidBlockException(args[argIdx]);
			}
			if (addParam) {
				searchRoutine.putBlock(args[argIdx]);
			} else {
				searchRoutine.removeBlock(args[argIdx]);
			}
			CommandUtils.addText(sender, (addParam ? "Added" : "Removed") + " " + args[argIdx] + " "
					+ (addParam ? "to" : "from") + " the list of selected blocks.");
		}
	}

}
