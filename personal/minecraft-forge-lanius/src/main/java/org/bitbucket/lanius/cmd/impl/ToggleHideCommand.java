package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.cmd.RoutineCommand;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.ICommandSender;

public class ToggleHideCommand extends RoutineCommand {

	public ToggleHideCommand() {
		super("togglehideroutine", "togglehide", "hide", "h");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleRoutine(Routine foundRoutine, ICommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		foundRoutine.setHidden();
		if (foundRoutine.isHidden()) {
			CommandUtils.addText(sender, "Started hiding " + foundRoutine + " from the enabled list.");
		} else {
			CommandUtils.addText(sender, "Started showing " + foundRoutine + " in the enabled list.");
		}
	}

}
