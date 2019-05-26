package net.hackforums.pootpoot.monumental.command.commands;

import net.hackforums.pootpoot.monumental.command.BaseCommand;
import net.hackforums.pootpoot.monumental.command.Command;
import net.hackforums.pootpoot.monumental.mode.Mode;
import net.hackforums.pootpoot.monumental.util.MessageType;
import net.hackforums.pootpoot.monumental.util.MessageUtils;
import net.hackforums.pootpoot.monumental.util.StringUtils;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

public class HelpCommand extends BaseCommand {

	public HelpCommand() {
		super("Help", "Prints useful information about the commands and modes", ".help [list|feature] [commands|modes|component]");
		// TODO Auto-generated constructor stub
	}

	private void addCommandsMessages() {
		MessageUtils.addMessage(getModName().appendText(": commands: "), MessageType.INFO);
		for (Command command : COMMANDS) {
			MessageUtils.addMessage(getModName().appendText(": - ").appendSibling(command.getModUsage()), MessageType.INFO);
		}
	}

	@Override
	public boolean modExecute(String[] arguments) {
		// TODO Auto-generated method stub
		if (arguments.length == 0) {
			addCommandsMessages();
			return true;
		}
		else if (arguments.length == 2) {
			if (arguments[0].equals("list")) {
				if (arguments[1].equals("commands")) {
					addCommandsMessages();
					return true;
				}
				else if (arguments[1].equals("modes")) {
					MessageUtils.addMessage(getModName().appendText(": modes: "), MessageType.INFO);
					for (Mode mode : Mode.MODES) {
						MessageUtils.addMessage(getModName().appendText(": - ").appendSibling(mode.getName()), MessageType.INFO);
					}
					return true;
				}
			}
			else if (arguments[0].equals("feature")) {
				Command command = StringUtils.getCommandFromString(arguments[1]);
				Mode mode = StringUtils.getModeFromString(arguments[1]);
				if (command != null) {
					MessageUtils.addMessage(getModName().appendText(": ").appendSibling(command.getModName()).appendText(mode != null ? " (command): " : ": "), MessageType.INFO);
					MessageUtils.addMessage(getModName().appendText(": - Description: ").appendSibling(command.getDescription()), MessageType.INFO);
					MessageUtils.addMessage(getModName().appendText(": - Usage: ").appendSibling(command.getModUsage()), MessageType.INFO);
				}
				if (mode != null) {
					MessageUtils.addMessage(getModName().appendText(": ").appendSibling(mode.getName()).appendText(command != null ? " (mode): " : ": "), MessageType.INFO);
					MessageUtils.addMessage(getModName().appendText(": - Description: ").appendSibling(mode.getDescription()), MessageType.INFO);
					KeyBinding keyBind = mode.getKeyBind();
					MessageUtils.addMessage(getModName().appendText(": - Key bind: " + Keyboard.getKeyName(keyBind != null ? keyBind.getKeyCode() : Keyboard.KEY_NONE)), MessageType.INFO);
				}
				if (command == null && mode == null) {
					MessageUtils.addMessage(getModName().appendText(": cannot display information about feature " + arguments[1] + " because it is invalid"), MessageType.ERROR);
				}
				return true;
			}
		}
		return false;
	}

}
