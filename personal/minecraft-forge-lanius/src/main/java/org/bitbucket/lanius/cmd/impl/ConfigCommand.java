package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.cmd.InvalidFeatureException;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class ConfigCommand extends ModCommand {

	public ConfigCommand() {
		super("configuration", "config", "cfg");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<set|reload> ['category'] ['name'] [value]";
	}

	@Override
	public void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length <= 0 || !args[0].equalsIgnoreCase("set") && !args[0].equalsIgnoreCase("reload")
				|| args[0].equalsIgnoreCase("set") && args.length < 4, sender);
		final Lanius instance = Lanius.getInstance();
		if (args[0].equalsIgnoreCase("reload")) {
			instance.getModCfg().load();
			instance.loadCfgContainers();
			instance.loadStandaloneCfgs();
			CommandUtils.addText(sender, "Reloaded the configuration.");
		} else {
			final String arguments = argumentStr(args, 1);
			final String CATEGORY_BOUND = "'";
			int boundCount = 0;
			for (int charIdx = 0; charIdx < arguments.length(); charIdx++) {
				if (Character.toString(arguments.charAt(charIdx)).equals(CATEGORY_BOUND)) {
					++boundCount;
				}
			}
			assertUsage(boundCount < 4, sender);
			final String parsedCategory = arguments.substring(arguments.indexOf(CATEGORY_BOUND) + 1,
					arguments.substring(arguments.indexOf(CATEGORY_BOUND) + 1).indexOf(CATEGORY_BOUND) + 1);
			final ConfigContainer parsedCfg = instance.getCfgContainerRegistry().get(parsedCategory);
			final String parsedName = arguments.substring(
					arguments.indexOf(parsedCategory) + parsedCategory.length() + CATEGORY_BOUND.length() + " ".length()
							+ CATEGORY_BOUND.length(),
					arguments
							.substring(arguments.indexOf(parsedCategory) + parsedCategory.length()
									+ CATEGORY_BOUND.length() + " ".length() + CATEGORY_BOUND.length())
							.indexOf(CATEGORY_BOUND) + arguments.indexOf(parsedCategory) + parsedCategory.length()
							+ CATEGORY_BOUND.length() + " ".length() + CATEGORY_BOUND.length()),
					parsedVal = arguments.substring(arguments.indexOf(parsedName) + parsedName.length()
							+ CATEGORY_BOUND.length() + " ".length());
			if (parsedCfg == null) {
				throw new InvalidFeatureException(parsedCategory);
			} else if (parsedCfg.getBoolean(parsedName) != null) {
				parsedCfg.putValue(parsedName, Boolean.parseBoolean(parsedVal));
				CommandUtils.addText(sender, "Set the value of \"" + parsedName + "\" in category \"" + parsedCategory
						+ "\" to " + parsedCfg.getBoolean(parsedName) + ".");
			} else if (parsedCfg.getFloat(parsedName) != null) {
				parsedCfg.getFloat(parsedName).setValue(Float.parseFloat(parsedVal));
				CommandUtils.addText(sender, "Set the value of \"" + parsedName + "\" in category \"" + parsedCategory
						+ "\" to " + parsedCfg.getFloat(parsedName) + ".");
			} else if (parsedCfg.getInt(parsedName) != null) {
				parsedCfg.getInt(parsedName).setValue(Integer.parseInt(parsedVal));
				CommandUtils.addText(sender, "Set the value of \"" + parsedName + "\" in category \"" + parsedCategory
						+ "\" to " + parsedCfg.getInt(parsedName) + ".");
			} else if (parsedCfg.getString(parsedName) != null) {
				parsedCfg.putValue(parsedName, parsedVal);
				CommandUtils.addText(sender, "Set the value of \"" + parsedName + "\" in category \"" + parsedCategory
						+ "\" to \"" + parsedCfg.getString(parsedName) + "\".");
			} else {
				throw new InvalidFeatureException(parsedName);
			}
		}
	}

}
