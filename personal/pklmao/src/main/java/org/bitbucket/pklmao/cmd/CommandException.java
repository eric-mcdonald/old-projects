package org.bitbucket.pklmao.cmd;

import org.bitbucket.pklmao.ReportedException;

public class CommandException extends ReportedException {
	private static final long serialVersionUID = 1L;
	private Command cmd;

	public CommandException(String msgKey, Object... formatArgs) {
		super(msgKey, formatArgs);
	}
	
	public Command getCmd() {
		return cmd;
	}
}
