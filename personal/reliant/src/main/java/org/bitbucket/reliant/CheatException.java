package org.bitbucket.reliant;

public class CheatException extends RuntimeException {
	/**
	 * The default serial UID.
	 */
	private static final long serialVersionUID = 1L;

	public CheatException(final String message) {
		super(message);
	}
	public CheatException(final Throwable error) {
		super(error);
	}
}