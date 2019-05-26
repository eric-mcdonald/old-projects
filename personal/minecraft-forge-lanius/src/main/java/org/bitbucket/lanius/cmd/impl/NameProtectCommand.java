package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.routine.impl.NameProtectRoutine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class NameProtectCommand extends ModCommand {

	public NameProtectCommand() {
		super("nameprotect", "protect");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<add|remove|clear> <name> [alias]";
	}

	@Override
	public void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length <= 0, sender);
		final boolean addParam = args[0].equalsIgnoreCase("add");
		assertUsage(!addParam && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("clear"), sender);
		final NameProtectRoutine tagsRoutine = (NameProtectRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Name Protect");
		if (addParam) {
			assertUsage(args.length <= 2, sender);
			tagsRoutine.putTag(args[1], args[2]);
			CommandUtils.addText(sender, "Added \"" + args[1] + "\" with alias " + "\"" + args[2] + ".\"");
		} else if (args[0].equalsIgnoreCase("remove")) {
			assertUsage(args.length <= 1, sender);
			tagsRoutine.removeAlias(args[1]);
			CommandUtils.addText(sender, "Removed \"" + args[1] + "'s\" alias.");
		} else {
			tagsRoutine.clearTags();
			CommandUtils.addText(sender, "Removed all name tags.");
		}
	}

}
