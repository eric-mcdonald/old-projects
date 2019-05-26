package org.bitbucket.reliant.memory;

public final class OffsetNotFoundException extends RuntimeException {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	public OffsetNotFoundException(String message) {
		super(message);
	}
}
