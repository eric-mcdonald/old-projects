package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.gui.GuiHandler;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class AllOffCommand extends ModCommand {

	public AllOffCommand() {
		super("togglealloff", "alloff", "off");
	}

	@Override
	protected String getParamUsage() {
		return "<hud>";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		for (Routine routine : Lanius.getInstance().getRoutineRegistry().objects()) {
			if (routine.isEnabled()) {
				routine.setEnabled();
			}
		}
		if (args.length > 0 && Boolean.parseBoolean(args[0])) {
			GuiHandler hud = Lanius.getInstance().getGuiHandler();
			for (String boolName : hud.boolNames()) {
				hud.putValue(boolName, false);
			}
			CommandUtils.addText(sender,
					!Lanius.getInstance().getModCfg().getBoolean("Toggle Messages", Routine.CFG_CATEGORY, false,
							"Determines whether or not to display a toggle message when a routine is toggled.")
									? "Disabled all routines and HUD options."
									: "Disabled all HUD options.");
		} else if (!Lanius.getInstance().getModCfg().getBoolean("Toggle Messages", Routine.CFG_CATEGORY, false,
				"Determines whether or not to display a toggle message when a routine is toggled.")) {
			CommandUtils.addText(sender, "Disabled all routines.");
		}
	}

}
