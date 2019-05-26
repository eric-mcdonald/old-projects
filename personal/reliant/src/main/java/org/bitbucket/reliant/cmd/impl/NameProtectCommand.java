package org.bitbucket.reliant.cmd.impl;

import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cmd.BaseCommand;
import org.bitbucket.reliant.cmd.CommandException;
import org.bitbucket.reliant.cmd.PlayerNotApplicableException;
import org.bitbucket.reliant.routine.impl.NameProtectRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class NameProtectCommand extends BaseCommand {
	private static final String NAME_SEPARATOR = "\"";

	public NameProtectCommand() {
		super("nameprotect", "protect", "np");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String[] args, final List<String> output) throws CommandException {
		// TODO Auto-generated method stub
		final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
		if (args.length <= 0) {
			ToggleCommand.toggleRoutine(nameProtect, output);
			return;
		}
		assertUsage(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("clear"));
		if (args[0].equalsIgnoreCase("clear")) {
			nameProtect.clearNameMap();
			output.add("Removed all entries from the name map.");
		} else {
			try {
				assertUsage(args.length > 1);
				final String nameArgs = StringUtils.arguments(args, 1);
				final String nameParam = nameArgs.substring(NAME_SEPARATOR.length(), nameArgs.lastIndexOf(NAME_SEPARATOR));
				if (args[0].equalsIgnoreCase("add")) {
					assertUsage(args.length > 2);
					final String aliasParam = nameArgs.substring(nameArgs.indexOf(nameParam) + nameParam.length() + " ".length() + NAME_SEPARATOR.length());
					if (nameParam.equals(SdkUtils.readRadarName(SdkUtils.localPlayerIdx()))) {
						throw new PlayerNotApplicableException(nameParam);
					}
					nameProtect.putAlias(nameParam, aliasParam);
					output.add("Added \"" + nameParam + "\" as \"" + aliasParam + "\" to the name map.");
				} else {
					nameProtect.removeAlias(nameParam);
					output.add("Removed \"" + nameParam + "\" and their alias from the name map.");
				}
			} catch (final IndexOutOfBoundsException idxOutOfBoundsEx) {
				assertUsage();
			}
		}
	}

	@Override
	public String paramUsage() {
		// TODO Auto-generated method stub
		return "[add|remove|clear] " + "[" + NAME_SEPARATOR + "name" + NAME_SEPARATOR + "]" + " [alias]";
	}

}
