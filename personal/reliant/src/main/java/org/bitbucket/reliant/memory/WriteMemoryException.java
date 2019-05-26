package org.bitbucket.reliant.memory;

import org.bitbucket.reliant.CheatException;

public final class WriteMemoryException extends CheatException {

	/**
	 * The serial version UID of this class.
	 */
	private static final long serialVersionUID = 1L;
	public static final long NO_ADDR = 0xFFFFFFFF;
	private final Object writeVal;
	private final long writeAddr;

	public WriteMemoryException(String message, final Object writeVal, final long writeAddr) {
		super(new IllegalArgumentException(message));
		this.writeVal = writeVal;
		this.writeAddr = writeAddr;
		// TODO Auto-generated constructor stub
	}
	
	public long getWriteAddr() {
		return writeAddr;
	}
	public Object getWriteVal() {
		return writeVal;
	}
}
