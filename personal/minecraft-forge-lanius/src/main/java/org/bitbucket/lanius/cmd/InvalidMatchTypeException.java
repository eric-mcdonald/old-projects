package org.bitbucket.lanius.cmd;

import net.minecraft.command.CommandException;

public class InvalidMatchTypeException extends CommandException {
	public InvalidMatchTypeException(String typeName) {
		super("Match type: " + typeName + " is invalid");
	}
}
