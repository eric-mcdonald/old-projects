package org.bitbucket.pklmao.cmd;

import java.util.List;

public interface Command {
	String name();
	String[] aliases();
	String desc();
	void execute(String[] args, List<String> output) throws CommandException;
	String usage();
}
