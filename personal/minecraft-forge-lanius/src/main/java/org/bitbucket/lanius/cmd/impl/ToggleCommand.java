package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.RoutineCommand;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.ICommandSender;

public final class ToggleCommand extends RoutineCommand {

	public ToggleCommand() {
		super("toggle", "t");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleRoutine(final Routine foundRoutine, ICommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		foundRoutine.setEnabled();
		if (!Lanius.getInstance().getModCfg().getBoolean("Toggle Messages", Routine.CFG_CATEGORY, false,
				"Determines whether or not to display a toggle message when a routine is toggled.")) {
			CommandUtils.addEnabledMsg(sender, foundRoutine);
		}
	}

}
