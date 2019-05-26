package org.bitbucket.reliant.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;

public abstract class BaseCommand implements Command {
	private final String[] names;
	
	public BaseCommand(String name, String... aliases) {
		name = StringUtils.configName(name);
		final List<String> names = new ArrayList<String>();
		names.add(name);
		for (String alias : aliases) {
			alias = StringUtils.configName(alias);
			names.add(alias);
		}
		this.names = names.toArray(new String[0]);
	}
	
	protected final void assertUsage(final boolean validCond) {
		if (!validCond) {
			throw new WrongUsageException(usage());
		}
	}
	protected final void assertUsage() {
		assertUsage(false);
	}
	@Override
	public final String[] names() {
		// TODO Auto-generated method stub
		return names;
	}
	@Override
	public final String usage() {
		String usage = "";
		for (int nameIdx = 0; nameIdx < names.length; nameIdx++) {
			usage += names[nameIdx];
			if (nameIdx < names.length - 1) {
				usage += "|";
			}
		}
		usage += " " + paramUsage();
		return usage;
	}
}
