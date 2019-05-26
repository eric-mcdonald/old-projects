package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.routine.impl.BreadcrumbsRoutine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class BreadcrumbsCommand extends ModCommand {

	public BreadcrumbsCommand() {
		super("breadcrumbs", "crumbs", "bc");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<clear|positions>";
	}

	@Override
	public void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length <= 0, sender);
		final BreadcrumbsRoutine breadcrumbsRoutine = (BreadcrumbsRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Breadcrumbs");
		if (args[0].equalsIgnoreCase("clear")) {
			breadcrumbsRoutine.clearPositions();
			CommandUtils.addText(sender, "Removed all positions from the trail.");
		} else {
			final int posRemoved = breadcrumbsRoutine.removePositions(Integer.parseInt(args[0]));
			CommandUtils.addText(sender,
					"Removed " + posRemoved + " " + (posRemoved == 1 ? "position" : "positions") + " from the trail.");
		}
	}

}
