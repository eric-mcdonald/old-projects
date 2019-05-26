package net.hackforums.pootpoot.monumental.util;

import net.hackforums.pootpoot.monumental.Monumental;
import net.hackforums.pootpoot.monumental.command.Command;
import net.hackforums.pootpoot.monumental.mode.Mode;

public class StringUtils {

	public static String convertToSettingsName(String name) {
		if (name == null) {
			throw new NullPointerException(Monumental.NAME + ": name cannot be null");
		}
		return name.toLowerCase().replace(" ", "_");
	}
	public static Command getCommandFromString(String str) {
		if (str == null) {
			throw new NullPointerException(Monumental.NAME + ": string cannot be null");
		}
		for (Command command : Command.COMMANDS) {
			if (convertToSettingsName(command.getModName().getUnformattedTextForChat()).equals(str)) {
				return command;
			}
		}
		return null;
	}
	public static Mode getModeFromString(String str) {
		if (str == null) {
			throw new NullPointerException(Monumental.NAME + ": string cannot be null");
		}
		for (Mode mode : Mode.MODES) {
			if (convertToSettingsName(mode.getName().getUnformattedTextForChat()).equals(str)) {
				return mode;
			}
		}
		return null;
	}
}
