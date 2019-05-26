package org.bitbucket.pklmao.test;

import java.util.List;

import org.bitbucket.pklmao.cmd.BaseCommand;
import org.bitbucket.pklmao.cmd.CommandException;

public class TestCommand extends BaseCommand {

	public TestCommand() {
		super("test", "test.cmd.test_cmd.desc", null, "test_alias");
	}

	@Override
	public void execute(String[] args, List<String> output) throws CommandException {
		assertUsage(args.length == 0);
		output.add("Test output.");
	}
}
