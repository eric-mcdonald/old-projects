package net.hackforums.pootpoot.monumental.util;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public enum MessageType {

	INFO(new ChatComponentText("\2479INFO\247r")),
	WARNING(new ChatComponentText("\247eWARNING\247r")),
	ERROR(new ChatComponentText("\247cERROR\247r"));
	private IChatComponent name;
	private MessageType(IChatComponent name) {
		this.name = name;
	}
	public IChatComponent getName() {
		return name;
	}
	@Override
	public String toString() {
		return "CommandMessageType{name=" + name + "}";
	}
}
