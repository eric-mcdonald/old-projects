package org.bitbucket.pklmao.cmd;

public class InvalidUsageException extends CommandException {
	private static final long serialVersionUID = 1L;

	public InvalidUsageException(Command cmd) {
		super("cmd.err.invalid_usage", cmd.name(), cmd.usage());
	}
}
