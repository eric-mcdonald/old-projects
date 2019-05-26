package org.bitbucket.lanius.cmd.impl;

import java.util.HashSet;
import java.util.Set;

import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.concurrent.Timer;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;

public final class TimersCommand extends ModCommand {

	public final Set<Timer> timers = new HashSet<Timer>();

	public TimersCommand() {
		super("timers", "timer", "time");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<add|remove|clear> [duration|up|name] [name]";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length <= 0, sender);
		if (args[0].equalsIgnoreCase("clear")) {
			for (final Timer timer : timers) {
				timer.setTiming(false);
			}
			timers.clear();
			CommandUtils.addText(sender, "Removed all timers.");
		} else if (args[0].equalsIgnoreCase("remove")) {
			assertUsage(args.length <= 1, sender);
			final Timer timer = new Timer(argumentStr(args, 1));
			timer.setTiming(false);
			timers.remove(timer);
			CommandUtils.addText(sender, "Removed timer \"" + timer.name + ".\"");
		} else if (args[0].equalsIgnoreCase("add")) {
			assertUsage(args.length <= 2, sender);
			if (args[1].equalsIgnoreCase("up")) {
				final Timer timer = new Timer(argumentStr(args, 2));
				timers.add(timer);
				timer.start();
				CommandUtils.addText(sender, "Added an upwards-counting timer with name \"" + timer.name + ".\"");
			} else {
				final String[] duration = args[1].split(":");
				int hours = duration.length == 3 ? Integer.parseInt(duration[0]) : 0,
						minutes = duration.length > 1 ? Integer.parseInt(duration[1]) : 0,
						seconds = Integer.parseInt(duration[0]);
				if (seconds > 59) {
					final float addMinutes = seconds / 60.0F;
					minutes += addMinutes;
					seconds = Math.round((addMinutes - MathHelper.floor(addMinutes)) * 60.0F);
				}
				if (minutes > 59) {
					final float addHours = minutes / 60.0F;
					hours += addHours;
					minutes = Math.round((addHours - MathHelper.floor(addHours)) * 60.0F);
				}
				final Timer timer = new Timer(argumentStr(args, 2), hours, minutes, seconds);
				timers.add(timer);
				timer.start();
				CommandUtils.addText(sender,
						"Added a timer starting from " + (hours < 10 ? "0" + hours : hours) + ":"
								+ (minutes < 10 ? "0" + minutes : minutes) + ":"
								+ (seconds < 10 ? "0" + seconds : seconds) + " with name \"" + timer.name + ".\"");
			}
		}
	}

}
