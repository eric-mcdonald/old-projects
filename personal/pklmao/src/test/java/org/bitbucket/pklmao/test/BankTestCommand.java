package org.bitbucket.pklmao.test;

import java.util.List;

import org.bitbucket.pklmao.cmd.BaseCommand;
import org.bitbucket.pklmao.cmd.CommandException;

public class BankTestCommand extends BaseCommand {
	public BankTestCommand() {
		super("banktest", "", "", "bank");
	}

	@Override
	public void execute(String[] args, List<String> output) throws CommandException {
		// TODO Implement this.
	}
}
