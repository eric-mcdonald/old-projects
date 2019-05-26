package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.cmd.RoutineCommand;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.ICommandSender;

public final class DescCommand extends RoutineCommand {

	public DescCommand() {
		super("description", "desc", "d");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleRoutine(final Routine foundRoutine, ICommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		CommandUtils.addText(sender, "Description of " + foundRoutine + ": \"" + foundRoutine.description() + "\"");
	}

}
