package org.bitbucket.reliant.cmd;

import java.util.List;

public interface Command {
	void execute(String[] args, final List<String> output) throws CommandException;
	String[] names();
	String paramUsage();
	String usage();
}
