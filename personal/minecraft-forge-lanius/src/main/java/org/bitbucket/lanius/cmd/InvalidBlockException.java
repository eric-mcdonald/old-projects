package org.bitbucket.lanius.cmd;

import net.minecraft.command.CommandException;

public final class InvalidBlockException extends CommandException {

	public InvalidBlockException(String blockName) {
		super("Block: " + blockName + " is invalid");
		// TODO Auto-generated constructor stub
	}

}