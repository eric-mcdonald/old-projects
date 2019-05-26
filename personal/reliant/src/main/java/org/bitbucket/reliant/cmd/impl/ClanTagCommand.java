package org.bitbucket.reliant.cmd.impl;

import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cmd.BaseCommand;
import org.bitbucket.reliant.cmd.CommandException;
import org.bitbucket.reliant.routine.impl.ClanTagRoutine;

public final class ClanTagCommand extends BaseCommand {

	public ClanTagCommand() {
		super("clantag", "clan");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String[] args, final List<String> output) throws CommandException {
		// TODO Auto-generated method stub
		final ClanTagRoutine clanTagRoutine = (ClanTagRoutine) Reliant.instance.getRoutineRegistry().get("Clan Tag");
		if (args.length <= 0) {
			ToggleCommand.toggleRoutine(clanTagRoutine, output);
			return;
		}
		if (args[0].equalsIgnoreCase("clear")) {
			clanTagRoutine.clearClanTags();
			output.add(Reliant.instance.getI18n().format("clan_tag.cleared.list"));
		} else {
			assertUsage(args.length > 1);
			final String tag = StringUtils.arguments(args, 1);
			if (args[0].equalsIgnoreCase("add")) {
				clanTagRoutine.addClanTag(tag);
				output.add(Reliant.instance.getI18n().format("clan_tag.added.tag", tag));
			} else if (args[0].equalsIgnoreCase("remove")) {
				clanTagRoutine.removeClanTag(tag);
				output.add(Reliant.instance.getI18n().format("clan_tag.removed.tag", tag));
			} else {
				assertUsage();
			}
		}
	}

	@Override
	public String paramUsage() {
		// TODO Auto-generated method stub
		return "[add|remove|clear] [" + Reliant.instance.getI18n().format("clan_tag.usage.value") + "]";
	}

}
