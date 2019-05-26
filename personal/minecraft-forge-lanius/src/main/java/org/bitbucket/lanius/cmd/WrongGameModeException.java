package org.bitbucket.lanius.cmd;

import net.minecraft.command.CommandException;

public final class WrongGameModeException extends CommandException {

	public WrongGameModeException(String validMode) {
		super("The player is not in game mode: " + validMode);
		// TODO Auto-generated constructor stub
	}

}
