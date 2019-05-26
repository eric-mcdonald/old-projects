package org.bitbucket.reliant.cmd;

public final class WrongUsageException extends CommandException {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	public WrongUsageException(String cmdUsage) {
		super("Proper usage: " + cmdUsage);
		// TODO Auto-generated constructor stub
	}

}
