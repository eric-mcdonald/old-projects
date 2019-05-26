package net.hackforums.pootpoot.monumental.command.commands;

import java.util.ArrayList;
import java.util.List;

import net.hackforums.pootpoot.monumental.command.BaseCommand;
import net.hackforums.pootpoot.monumental.mode.Mode;
import net.hackforums.pootpoot.monumental.util.MessageType;
import net.hackforums.pootpoot.monumental.util.MessageUtils;
import net.hackforums.pootpoot.monumental.util.StringUtils;

public class ToggleCommand extends BaseCommand {

	public ToggleCommand() {
		super("Toggle", "Enables/disables modes", ".toggle <mode> [mode2]");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean modExecute(String[] arguments) {
		// TODO Auto-generated method stub
		List<Mode> toggledModes = new ArrayList<Mode>();
		for (String argument : arguments) {
			Mode mode = StringUtils.getModeFromString(argument);
			if (mode != null && !toggledModes.contains(mode)) {
				mode.setEnabled(!mode.isEnabled());
				MessageUtils.addMessage(getModName().appendText(": " + (mode.isEnabled() ? "enabled" : "disabled") + " mode ").appendSibling(mode.getName()), MessageType.INFO);
				toggledModes.add(mode);
			}
			else if (mode != null && toggledModes.contains(mode)) {
				MessageUtils.addMessage(getModName().appendText(": cannot " + (mode.isEnabled() ? "disable" : "enable") + " mode ").appendSibling(mode.getName()).appendText(" because it was already toggled"), MessageType.ERROR);
			}
			else {
				MessageUtils.addMessage(getModName().appendText(": cannot enable/disable mode " + argument + " because it is invalid"), MessageType.ERROR);
			}
		}
		return arguments.length > 0 ? true : false;
	}

}
