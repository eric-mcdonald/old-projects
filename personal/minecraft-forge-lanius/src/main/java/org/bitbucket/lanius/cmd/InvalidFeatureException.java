package org.bitbucket.lanius.cmd;

import net.minecraft.command.CommandException;

public final class InvalidFeatureException extends CommandException {

	public InvalidFeatureException(String featName) {
		super("Feature: " + featName + " is invalid");
		// TODO Auto-generated constructor stub
	}

}
