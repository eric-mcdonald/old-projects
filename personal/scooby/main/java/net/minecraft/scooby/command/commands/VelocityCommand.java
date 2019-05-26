package net.minecraft.scooby.command.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.scooby.Scooby;
import net.minecraft.scooby.command.Command;
import net.minecraft.scooby.command.Configurable;
import net.minecraft.scooby.mode.Mode;

public class VelocityCommand extends Command implements Configurable {

	private double minFactor = 0.5D, maxFactor = 0.75D;
	public VelocityCommand(Scooby scooby) {
		super(scooby, "-velocity", "-velocity [min|max|reset] [factor]");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(ICommandSender sender, String[] args)
			throws CommandException {
		// TODO Auto-generated method stub
		if (args.length == 0) {
			Mode velocityMode = scooby.modeUtils.getModeByName("Velocity");
			velocityMode.setEnabled(!velocityMode.isEnabled());
			addCommandMessage("mode " + velocityMode.getName() + " has been " + (velocityMode.isEnabled() ? "enabled" : "disabled"));
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
			minFactor = 0.5D;
			maxFactor = 0.75D;
			addCommandMessage("velocity parameters have been reset to the default values");
		}
		else if (args.length == 2 && (args[0].equalsIgnoreCase("min") || args[0].equalsIgnoreCase("max"))) {
			try {
				double factor = Double.parseDouble(args[1]), prevFactor = factor;
				if (factor < 0.1D) {
					factor = 0.1D;
				}
				if (factor > 0.9D) {
					factor = 0.9D;
				}
				String argLowerCase = args[0].toLowerCase();
				if ((factor >= maxFactor && args[0].equalsIgnoreCase("min")) || (factor <= minFactor && args[0].equalsIgnoreCase("max"))) {
					addCommandMessage("cannot set velocity parameter " + argLowerCase + " because it cannot be " + (factor >= maxFactor && args[0].equalsIgnoreCase("min") ? "greater" : "less") + " than or equal to velocity parameter " + (factor == maxFactor && args[0].equalsIgnoreCase("min") ? "max" : "min"));
					return;
				}
				if (factor != prevFactor) {
					addCommandMessage("changing velocity parameter from " + prevFactor + " to " + factor);
				}
				if (args[0].equalsIgnoreCase("min")) {
					minFactor = factor;
				}
				else {
					maxFactor = factor;
				}
				addCommandMessage("velocity parameter " + argLowerCase + " has been set to " + factor);
			}
			catch (NumberFormatException e) {
				addCommandMessage("usage: " + getCommandUsage());
				return;
			}
		}
		else {
			addCommandMessage("usage: " + getCommandUsage());
		}
	}

	public double getMaxFactor() {
		return maxFactor;
	}
	public double getMinFactor() {
		return minFactor;
	}

	@Override
	public void load(BufferedReader reader) throws IOException {
		// TODO Auto-generated method stub
		String modeConfigName = scooby.modeUtils.getModeByName("Velocity").getConfigName(), line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith(modeConfigName)) {
				String[] valueSetting = line.split(":");
				double factor = Double.parseDouble(valueSetting[1]);
				if (factor < 0.1D) {
					factor = 0.1D;
				}
				if (factor > 0.9D) {
					factor = 0.9D;
				}
				if ((factor >= maxFactor && valueSetting[0].equals(modeConfigName + "_min")) || (factor <= minFactor && valueSetting[0].equals(modeConfigName + "_max"))) {
					continue;
				}
				if (valueSetting[0].equals(modeConfigName + "_min")) {
					minFactor = factor;
				}
				else {
					maxFactor = factor;
				}
			}
		}
	}

	@Override
	public void save(PrintWriter writer) {
		// TODO Auto-generated method stub
		String modeConfigName = scooby.modeUtils.getModeByName("Velocity").getConfigName();
		writer.println(modeConfigName + "_min:" + minFactor);
		writer.println(modeConfigName + "_max:" + maxFactor);
	}
}
