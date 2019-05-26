package org.bitbucket.valve_file_parsing;

public final class InvalidDataException extends RuntimeException {
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidDataException(String message) {
		super(message);
	}
}
