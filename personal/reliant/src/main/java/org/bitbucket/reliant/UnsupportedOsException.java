package org.bitbucket.reliant;

public final class UnsupportedOsException extends RuntimeException {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedOsException(String osName) {
		super(osName);
	}
}
