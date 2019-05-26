package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.routine.impl.FreecamRoutine;
import org.bitbucket.lanius.routine.impl.WaypointsRoutine;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public final class WaypointsCommand extends ModCommand {

	public WaypointsCommand() {
		super("waypoints", "way", "wp");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<add|remove|clear> [x|spawn|freecam|here|name] [y|z|name] [z|name] [name]";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length <= 0, sender);
		final WaypointsRoutine waypointsRoutine = (WaypointsRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Waypoints");
		if (args[0].equalsIgnoreCase("clear")) {
			waypointsRoutine.clearWaypoints();
			CommandUtils.addText(sender, "Removed all waypoints.");
			return;
		}
		assertUsage(
				args.length == 2
						&& (args[1].equalsIgnoreCase("spawn") || args[1].equalsIgnoreCase("freecam")
								|| args[1].equalsIgnoreCase("here"))
						|| args.length <= 1 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")),
				sender);
		final String name;
		final BlockPos pos;
		if (args[1].equalsIgnoreCase("spawn")) {
			name = argumentStr(args, 2);
			pos = new BlockPos(Lanius.mc.world.getWorldInfo().getSpawnX(), Lanius.mc.world.getWorldInfo().getSpawnY(),
					Lanius.mc.world.getWorldInfo().getSpawnZ());
		} else if (args[1].equalsIgnoreCase("freecam")) {
			name = argumentStr(args, 2);
			final FreecamRoutine freecamRoutine = (FreecamRoutine) Lanius.getInstance().getRoutineRegistry()
					.get("Freecam");
			pos = new BlockPos(Lanius.mc.player.posX + freecamRoutine.getPosX(),
					Lanius.mc.player.posY + freecamRoutine.getPosY(), Lanius.mc.player.posZ + freecamRoutine.getPosZ());
		} else if (args[1].equalsIgnoreCase("here")) {
			name = argumentStr(args, 2);
			pos = new BlockPos(Lanius.mc.player.posX, Lanius.mc.player.posY, Lanius.mc.player.posZ);
		} else {
			assertUsage(args.length <= (args[0].equalsIgnoreCase("remove") ? 1 : 3), sender);
			name = argumentStr(args, args[0].equalsIgnoreCase("remove") ? 1 : args.length >= 5 ? 4 : 3);
			pos = args[0].equalsIgnoreCase("remove") ? null
					: new BlockPos(Integer.parseInt(args[1]),
							args.length == 4 ? Lanius.mc.player.posY : Integer.parseInt(args[2]),
							Integer.parseInt(args[args.length == 4 ? 2 : 3]));
		}
		if (args[0].equalsIgnoreCase("add")) {
			waypointsRoutine.putWaypoint(name, pos);
			CommandUtils.addText(sender, "Added a waypoint at (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()
					+ ") with name \"" + name + ".\"");
		} else {
			waypointsRoutine.removeWaypoint(name);
			CommandUtils.addText(sender, "Removed waypoint \"" + name + ".\"");
		}
	}

}
