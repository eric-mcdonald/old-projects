package net.hackforums.pootpoot.monumental.command.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.hackforums.pootpoot.monumental.command.BaseCommand;
import net.hackforums.pootpoot.monumental.command.Configurable;
import net.hackforums.pootpoot.monumental.init.Modes;
import net.hackforums.pootpoot.monumental.util.MessageType;
import net.hackforums.pootpoot.monumental.util.MessageUtils;
import net.hackforums.pootpoot.monumental.util.StringUtils;
import net.minecraft.util.ChatComponentText;

public class BrightnessCommand extends BaseCommand implements Configurable {

	private float gammaSetting = 10.0F;
	public BrightnessCommand() {
		super(Modes.BRIGHTNESS_MODE.getName(), Modes.BRIGHTNESS_MODE.getDescription(), new ChatComponentText(".bright [gamma|reset]"));
		// TODO Auto-generated constructor stub
	}

	public float getGammaSetting() {
		return gammaSetting;
	}

	@Override
	public void load(BufferedReader reader) throws IOException {
		// TODO Auto-generated method stub
		String settingsName = StringUtils.convertToSettingsName(getModName().getUnformattedTextForChat()), line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith(settingsName)) {
				String[] valueSetting = line.split(":");
				if (valueSetting[0].equals(settingsName + "_gamma")) {
					gammaSetting = Float.parseFloat(valueSetting[1]);
					if (gammaSetting < 0.0F) {
						gammaSetting = 0.0F;
					}
					else if (gammaSetting > 10.0F) {
						gammaSetting = 10.0F;
					}
					break;
				}
			}
		}
	}

	@Override
	public boolean modExecute(String[] arguments) {
		// TODO Auto-generated method stub
		if (arguments.length == 0) {
			Modes.BRIGHTNESS_MODE.setEnabled(!Modes.BRIGHTNESS_MODE.isEnabled());
			MessageUtils.addMessage(getModName().appendText(": mode has been " + (Modes.BRIGHTNESS_MODE.isEnabled() ? "enabled" : "disabled")), MessageType.INFO);
			return true;
		}
		else if (arguments.length == 1) {
			if (arguments[0].equals("reset")) {
				gammaSetting = 10.0F;
				MessageUtils.addMessage(getModName().appendText(": gamma has been reset to the default value"), MessageType.INFO);
				return true;
			}
			else {
				try {
					float gammaSetting = Float.parseFloat(arguments[0]);
					if (gammaSetting < 0.0F) {
						MessageUtils.addMessage(getModName().appendText(": changing gamma from " + gammaSetting + " to 0.0"), MessageType.WARNING);
						gammaSetting = 0.0F;
					}
					else if (gammaSetting > 10.0F) {
						MessageUtils.addMessage(getModName().appendText(": changing gamma from " + gammaSetting + " to 10.0"), MessageType.WARNING);
						gammaSetting = 10.0F;
					}
					this.gammaSetting = gammaSetting;
					MessageUtils.addMessage(getModName().appendText(": gamma has been set to " + gammaSetting), MessageType.INFO);
				}
				catch (NumberFormatException e) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void save(PrintWriter writer) {
		// TODO Auto-generated method stub
		writer.println(StringUtils.convertToSettingsName(getModName().getUnformattedTextForChat()) + "_gamma:" + gammaSetting);
	}
}
