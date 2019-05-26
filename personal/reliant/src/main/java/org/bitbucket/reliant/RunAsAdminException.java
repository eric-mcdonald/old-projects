package org.bitbucket.reliant;

public final class RunAsAdminException extends RuntimeException {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	public RunAsAdminException(String file) {
		super(file);
	}
}
