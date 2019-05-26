package org.bitbucket.reliant.test;

import java.util.List;

import org.bitbucket.reliant.cmd.BaseCommand;
import org.bitbucket.reliant.cmd.CommandException;

public final class TestCommand extends BaseCommand {

	public TestCommand() {
		super("test_command", "test_cmd", "test");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String[] args, final List<String> output) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length > 0);
		output.add("Executed the test command.");
	}

	@Override
	public String paramUsage() {
		// TODO Auto-generated method stub
		return "<arg> [arg2]";
	}

}
