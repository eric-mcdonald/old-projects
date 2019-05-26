package org.bitbucket.reliant.memory;

import java.io.IOException;

public final class OpenProcessException extends IOException {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	public OpenProcessException(final String process) {
		super(process);
	}
}
