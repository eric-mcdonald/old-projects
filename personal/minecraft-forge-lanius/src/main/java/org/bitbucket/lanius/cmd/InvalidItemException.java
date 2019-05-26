package org.bitbucket.lanius.cmd;

import net.minecraft.command.CommandException;

public final class InvalidItemException extends CommandException {

	public InvalidItemException(String itemName) {
		super("Item: " + itemName + " is invalid");
		// TODO Auto-generated constructor stub
	}

}