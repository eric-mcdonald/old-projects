package net.hackforums.pootpoot.monumental.command.commands;

import net.hackforums.pootpoot.monumental.Monumental;
import net.hackforums.pootpoot.monumental.command.BaseCommand;
import net.hackforums.pootpoot.monumental.mode.Mode;
import net.hackforums.pootpoot.monumental.util.KeyBindingUtils;
import net.hackforums.pootpoot.monumental.util.MessageType;
import net.hackforums.pootpoot.monumental.util.MessageUtils;
import net.hackforums.pootpoot.monumental.util.StringUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.IChatComponent;

import org.lwjgl.input.Keyboard;

public class BindCommand extends BaseCommand {

	public BindCommand() {
		super("Bind", "Allows modes to be toggled when a key is pressed", ".bind <add|remove|clear> [mode] [key]");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean modExecute(String[] arguments) {
		// TODO Auto-generated method stub
		if (arguments.length == 1 && arguments[0].equals("clear")) {
			KeyBindingUtils.clearKeyBinds();
			MessageUtils.addMessage(getModName().appendText(": cleared all key binds"), MessageType.INFO);
			return true;
		}
		else if (arguments.length == 3 && arguments[0].equals("add")) {
			Mode mode = StringUtils.getModeFromString(arguments[1]);
			arguments[2] = arguments[2].toUpperCase();
			int keyCode = Keyboard.getKeyIndex(arguments[2]);
			if (mode == null) {
				MessageUtils.addMessage(getModName().appendText(": cannot bind mode " + arguments[1] + " to key " + arguments[2] + " because the mode is invalid"), MessageType.ERROR);
			}
			else if (keyCode == Keyboard.KEY_NONE) {
				MessageUtils.addMessage(getModName().appendText(": cannot bind mode ").appendSibling(mode.getName()).appendText(" to key " + arguments[2] + " because the key is invalid"), MessageType.ERROR);
			}
			else {
				IChatComponent name = mode.getName();
				mode.setKeyBind(new KeyBinding(name.getUnformattedTextForChat(), keyCode, Monumental.NAME));
				MessageUtils.addMessage(getModName().appendText(": bound mode ").appendSibling(name).appendText(" to key " + arguments[2]), MessageType.INFO);
			}
			return true;
		}
		else if (arguments.length == 2 && arguments[0].equals("remove")) {
			Mode mode = StringUtils.getModeFromString(arguments[1]);
			if (mode == null) {
				MessageUtils.addMessage(getModName().appendText(": cannot unbind mode " + arguments[1] + " because it is invalid"), MessageType.ERROR);
			}
			else {
				mode.setKeyBind(null);
				MessageUtils.addMessage(getModName().appendText(": unbound mode ").appendSibling(mode.getName()), MessageType.INFO);
			}
			return true;
		}
		return false;
	}

}
