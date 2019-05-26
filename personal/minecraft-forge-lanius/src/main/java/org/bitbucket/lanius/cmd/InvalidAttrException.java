package org.bitbucket.lanius.cmd;

import net.minecraft.command.CommandException;

public class InvalidAttrException extends CommandException {
	public InvalidAttrException(String attrName) {
		super("Attribute: " + attrName + " is invalid");
		// TODO Auto-generated constructor stub
	}
}
