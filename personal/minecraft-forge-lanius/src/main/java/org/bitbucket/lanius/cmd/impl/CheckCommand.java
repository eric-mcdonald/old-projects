package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.cmd.RoutineCommand;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.RoutineUtils;

import net.minecraft.command.ICommandSender;

public final class CheckCommand extends RoutineCommand {

	public CheckCommand() {
		super("check", "c");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleRoutine(final Routine foundRoutine, ICommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		CommandUtils.addText(sender, foundRoutine + " is " + RoutineUtils.stateText(foundRoutine, false) + ".");
	}

}
