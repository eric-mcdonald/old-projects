package org.bitbucket.reliant.cmd;

public class CommandException extends RuntimeException {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	public CommandException(String message) {
		super(message);
	}

}
