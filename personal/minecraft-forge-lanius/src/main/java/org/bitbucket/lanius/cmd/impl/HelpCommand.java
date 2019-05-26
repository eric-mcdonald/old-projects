package org.bitbucket.lanius.cmd.impl;

import java.util.List;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.InvalidFeatureException;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.RoutineUtils;
import org.bitbucket.lanius.util.registry.Registry;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class HelpCommand extends ModCommand {

	public HelpCommand() {
		super("help", "?");
		// TODO Auto-generated constructor stub
	}

	private String cmdList(final Registry<ModCommand> cmdRegistry) {
		final List<ModCommand> cmds = cmdRegistry.objects();
		String message = "Commands " + "(" + cmds.size() + "): ";
		for (int cmdIdx = 0; cmdIdx < cmds.size(); cmdIdx++) {
			message += cmds.get(cmdIdx).names()[0];
			if (cmdIdx < cmds.size() - 1) {
				message += ", ";
			}
		}
		return message;
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "[feature|list] [name|commands|routines]";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(
				args.length > 0 && (!args[0].equalsIgnoreCase("feature") || args.length <= 1)
						&& (!args[0].equalsIgnoreCase("list") || args.length <= 1
								|| !args[1].equalsIgnoreCase("commands") && !args[1].equalsIgnoreCase("routines")),
				sender);
		final Lanius instance = Lanius.getInstance();
		final Registry<ModCommand> cmdRegistry = instance.getCmdRegistry();
		final Registry<Routine> routineRegistry = instance.getRoutineRegistry();
		if (args.length <= 0) {
			CommandUtils.addText(sender, cmdList(cmdRegistry));
			return;
		}
		if (args[0].equalsIgnoreCase("feature")) {
			args[1] = args[1].toLowerCase();
			final ModCommand foundCmd = cmdRegistry.get(args[1]);
			if (foundCmd != null) {
				CommandUtils.addText(sender, foundCmd.getUsage(sender));
			}
			final Routine foundRoutine = routineRegistry.get(args[1]);
			if (foundRoutine != null) {
				CommandUtils.addText(sender, foundRoutine.name() + ": " + foundRoutine.description());
			}
			if (foundCmd == null && foundRoutine == null) {
				throw new InvalidFeatureException(args[1]);
			}
		} else if (args[0].equalsIgnoreCase("list")) {
			String message;
			if (args[1].equalsIgnoreCase("commands")) {
				message = cmdList(cmdRegistry);
			} else {
				final List<Routine> routines = routineRegistry.objects();
				message = "Routines " + "(" + routines.size() + "): ";
				for (int routineIdx = 0; routineIdx < routines.size(); routineIdx++) {
					message += "\247" + (routines.get(routineIdx).isEnabled() ? "a" : "c")
							+ RoutineUtils.configName(routines.get(routineIdx).name()) + "\247r";
					if (routineIdx < routines.size() - 1) {
						message += ", ";
					}
				}
			}
			CommandUtils.addText(sender, message);
		}
	}

}
