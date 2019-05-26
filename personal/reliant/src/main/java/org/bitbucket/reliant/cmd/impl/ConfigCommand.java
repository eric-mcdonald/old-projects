package org.bitbucket.reliant.cmd.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.Configuration;
import org.bitbucket.reliant.cfg.Option;
import org.bitbucket.reliant.cmd.BaseCommand;
import org.bitbucket.reliant.cmd.CommandException;

public final class ConfigCommand extends BaseCommand {
	public ConfigCommand() {
		super("configuration", "config", "cfg");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String[] args, final List<String> output) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length > 0 && (args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("description") || args[0].equalsIgnoreCase("list")));
		if (args[0].equalsIgnoreCase("load")) {
			if (args.length < 2) {
				Reliant.instance.loadConfigs(Reliant.instance.getConfigsDir(), false);
			} else {
				final String dirArgs = StringUtils.arguments(args, 1);
				final int firstIdx = dirArgs.indexOf(Main.DIR_SEPARATOR), lastIdx = dirArgs.lastIndexOf(Main.DIR_SEPARATOR);
				Reliant.instance.loadConfigs(new File(firstIdx != -1 && lastIdx != -1 && firstIdx != lastIdx ? dirArgs.substring(Main.DIR_SEPARATOR.length(), lastIdx) : dirArgs), false);
			}
			output.add(Reliant.instance.getI18n().format("config.loaded.cfg", Reliant.instance.getConfigsDir()));
		} else if (args[0].equalsIgnoreCase("list")) {
			assertUsage(args.length > 1);
			final Configuration config = findCategory(args[1], output);
			if (config == null) {
				return;
			}
			final List<Option<?>> options = config.getOptions();
			final List<String> optionNames = new ArrayList<String>();
			for (final Option<?> option : options) {
				optionNames.add(StringUtils.configName(option.name()));
			}
			output.add(Reliant.instance.getI18n().format("config.displayed.list", config.name(), options.size(), StringUtils.list(optionNames.toArray(new String[0]), 0)));
		} else {
			assertUsage(args.length > 2);
			final Configuration config = findCategory(args[1], output);
			if (config == null) {
				return;
			}
			final Option<?> option = config.getOptionByName(args[2]);
			if (option == null) {
				output.add(Reliant.instance.getI18n().format("config.act.find_err", args[2], config.name()));
				return;
			}
			if (args[0].equalsIgnoreCase("set")) {
				assertUsage(args.length > 3);
				String value = "";
				for (int argIdx = 3; argIdx < args.length; argIdx++) {
					value += args[argIdx];
					if (argIdx < args.length - 1) {
						value += " ";
					}
				}
				option.setValue(option.parseValue(value));
				output.add(Reliant.instance.getI18n().format("config.act.set", option.name(), config.name(), value));
			} else {
				output.add(Reliant.instance.getI18n().format("config.act.desc", option.name(), config.name(), option.description()));
			}
		}
	}

	private Configuration findCategory(final String name, final List<String> output) {
		final Configuration config = Reliant.instance.getConfigRegistry().get(name);
		if (config == null) {
			output.add(Reliant.instance.getI18n().format("config.find_err", name));
		}
		return config;
	}
	@Override
	public String paramUsage() {
		// TODO Auto-generated method stub
		return "<description|list|load|set> [" + Reliant.instance.getI18n().format("config.usage.category") + "|" + Main.DIR_SEPARATOR + Reliant.instance.getI18n().format("config.usage.dir") + Main.DIR_SEPARATOR + "] [" + Reliant.instance.getI18n().format("config.usage.option") + "] [" + Reliant.instance.getI18n().format("config.usage.value") + "]";
	}
}
