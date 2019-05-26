package org.bitbucket.reliant.cmd.impl;

import java.nio.ByteBuffer;
import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cmd.BaseCommand;
import org.bitbucket.reliant.cmd.CommandException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.impl.WaypointsRoutine;
import org.bitbucket.reliant.util.Command;
import org.bitbucket.reliant.util.Point;

public final class WaypointsCommand extends BaseCommand {
	private static final String TXT_SEPARATOR = "\"";

	public WaypointsCommand() {
		super("waypoints", "waypoint", "way", "wp");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String[] args, final List<String> output) throws CommandException {
		// TODO Auto-generated method stub
		final WaypointsRoutine waypoints = (WaypointsRoutine) Reliant.instance.getRoutineRegistry().get("Waypoints");
		if (args.length <= 0) {
			ToggleCommand.toggleRoutine(waypoints, output);
			return;
		}
		assertUsage(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("clear"));
		if (args[0].equalsIgnoreCase("clear")) {
			waypoints.clearWaypoints();
			output.add("Removed all entries from the waypoint map.");
		} else {
			try {
				assertUsage(args.length > 1);
				final String nameArgs = StringUtils.arguments(args, 1);
				String nameParam = nameArgs.substring(TXT_SEPARATOR.length());
				nameParam = nameParam.substring(0, nameParam.indexOf(TXT_SEPARATOR));
				if (args[0].equalsIgnoreCase("remove")) {
					waypoints.removeWaypoint(nameParam);
					output.add("Removed \"" + nameParam + "\" from the waypoint map.");
				} else {
					String pointData = nameArgs.substring(nameArgs.indexOf(TXT_SEPARATOR + nameParam + TXT_SEPARATOR) + TXT_SEPARATOR.length() + nameParam.length() + TXT_SEPARATOR.length() + " ".length());
					final int cmdBeginIdx = pointData.indexOf(" " + TXT_SEPARATOR);
					if (cmdBeginIdx != -1) {
						pointData = pointData.substring(0, cmdBeginIdx);
					}
					final String[] pointArgs = pointData.split(" ");
					// Eric: detect if a command was specified
					int maxArgs = 3;
					try {
						Float.parseFloat(pointArgs[pointArgs.length - 1]);
					} catch (final NumberFormatException numFormatEx) {
						maxArgs = 4;
					}
					final float x = Float.parseFloat(pointArgs[0]), z = Float.parseFloat(pointArgs[pointArgs.length == maxArgs ? 2 : 1]);
					final float[] localOrigin = new float[3];
					((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localOrigin.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * localOrigin.length)).rewind()).asFloatBuffer().get(localOrigin);
					final float y = pointArgs.length == maxArgs ? Float.parseFloat(pointArgs[1]) : localOrigin[2];
					final Point point = new Point(new Vector(x, z, y));
					waypoints.putWaypoint(nameParam, point);
					if (maxArgs == 4) {
						pointData = nameArgs.substring(nameArgs.indexOf(TXT_SEPARATOR + nameParam + TXT_SEPARATOR) + TXT_SEPARATOR.length() + nameParam.length() + TXT_SEPARATOR.length() + " ".length());
						pointData = pointData.substring(pointData.indexOf(TXT_SEPARATOR) + TXT_SEPARATOR.length(), pointData.lastIndexOf(TXT_SEPARATOR));
						point.cmd = new Command(Command.Type.typeByKey(pointArgs[pointArgs.length - 1]), pointData);
						output.add("Added \"" + nameParam + "\" with command type " + pointArgs[pointArgs.length - 1] + " and data \"" + pointData + "\" at location " + point.pos + " to the waypoint map.");
					} else {
						output.add("Added \"" + nameParam + "\" at location " + point.pos + " to the waypoint map.");
					}
				}
			} catch (final IndexOutOfBoundsException idxOutOfBoundsEx) {
				assertUsage();
			}
		}
	}

	@Override
	public String paramUsage() {
		// TODO Auto-generated method stub
		return "[add|remove|clear] [" + TXT_SEPARATOR + "name" + TXT_SEPARATOR + "] [x] [y|z] [z|type] [type] [" + TXT_SEPARATOR + "cmd" + TXT_SEPARATOR + "]";
	}

}
