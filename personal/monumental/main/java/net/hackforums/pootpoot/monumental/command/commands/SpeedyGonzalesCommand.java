package net.hackforums.pootpoot.monumental.command.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.hackforums.pootpoot.monumental.command.BaseCommand;
import net.hackforums.pootpoot.monumental.command.NoCheatPlusCompatible;
import net.hackforums.pootpoot.monumental.init.Modes;
import net.hackforums.pootpoot.monumental.util.MessageType;
import net.hackforums.pootpoot.monumental.util.MessageUtils;
import net.hackforums.pootpoot.monumental.util.StringUtils;
import net.minecraft.util.ChatComponentText;

public class SpeedyGonzalesCommand extends BaseCommand implements NoCheatPlusCompatible {

	private float breakThreshold = 0.7F, prevBreakThreshold = breakThreshold;
	private int placeSpeed, prevPlaceSpeed = placeSpeed;
	public SpeedyGonzalesCommand() {
		super(Modes.SPEEDY_GONZALES_MODE.getName(), Modes.SPEEDY_GONZALES_MODE.getDescription(), new ChatComponentText(".sg [break|place|reset] [threshold|speed]"));
		// TODO Auto-generated constructor stub
	}

	public float getBreakThreshold() {
		return breakThreshold;
	}

	public int getPlaceSpeed() {
		return placeSpeed;
	}
	@Override
	public void load(BufferedReader reader) throws IOException {
		// TODO Auto-generated method stub
		String settingsName = StringUtils.convertToSettingsName(getModName().getUnformattedTextForChat()), line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith(settingsName)) {
				String[] valueSetting = line.split(":");
				if (valueSetting[0].equals(settingsName + "_break_threshold")) {
					breakThreshold = Float.parseFloat(valueSetting[1]);
					if (breakThreshold < 0.7F) {
						breakThreshold = 0.7F;
					}
					else if (breakThreshold > 0.9F) {
						breakThreshold = 0.9F;
					}
				}
				if (valueSetting[0].equals(settingsName + "_place_speed")) {
					placeSpeed = Integer.parseInt(valueSetting[1]);
					if (placeSpeed < 0) {
						placeSpeed = 0;
					}
					else if (placeSpeed > 3) {
						placeSpeed = 3;
					}
				}
			}
		}
	}
	@Override
	public boolean modExecute(String[] arguments) {
		// TODO Auto-generated method stub
		if (arguments.length == 0) {
			Modes.SPEEDY_GONZALES_MODE.setEnabled(!Modes.SPEEDY_GONZALES_MODE.isEnabled());
			MessageUtils.addMessage(getModName().appendText(": mode has been " + (Modes.SPEEDY_GONZALES_MODE.isEnabled() ? "enabled" : "disabled")), MessageType.INFO);
			return true;
		}
		else if (arguments.length == 1 && arguments[0].equals("reset")) {
			breakThreshold = Modes.NOCHEATPLUS_MODE.isEnabled() ? 0.8F : 0.7F;
			placeSpeed = Modes.NOCHEATPLUS_MODE.isEnabled() ? 2 : 0;
			MessageUtils.addMessage(getModName().appendText(": settings have been reset to the default values"), MessageType.INFO);
			return true;
		}
		else if (arguments.length == 2) {
			if (arguments[0].equals("break")) {
				try {
					float breakThreshold = Float.parseFloat(arguments[1]);
					if (breakThreshold < 0.7F) {
						MessageUtils.addMessage(getModName().appendText(": changing threshold from " + breakThreshold + " to 0.7"), MessageType.WARNING);
						breakThreshold = 0.7F;
					}
					else if (breakThreshold > 0.9F) {
						MessageUtils.addMessage(getModName().appendText(": changing threshold from " + breakThreshold + " to 0.9"), MessageType.WARNING);
						breakThreshold = 0.9F;
					}
					this.breakThreshold = breakThreshold;
					MessageUtils.addMessage(getModName().appendText(": threshold has been set to " + breakThreshold), MessageType.INFO);
				}
				catch (NumberFormatException e) {
					return false;
				}
				return true;
			}
			else if (arguments[0].equals("place")) {
				try {
					int placeSpeed = Integer.parseInt(arguments[1]);
					if (placeSpeed < 0) {
						MessageUtils.addMessage(getModName().appendText(": changing speed from " + placeSpeed + " to 0"), MessageType.WARNING);
						placeSpeed = 0;
					}
					else if (placeSpeed > 3) {
						MessageUtils.addMessage(getModName().appendText(": changing speed from " + placeSpeed + " to 3"), MessageType.WARNING);
						placeSpeed = 3;
					}
					this.placeSpeed = placeSpeed;
					MessageUtils.addMessage(getModName().appendText(": speed has been set to " + placeSpeed), MessageType.INFO);
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
	public void onNCPDisable() {
		// TODO Auto-generated method stub
		breakThreshold = prevBreakThreshold;
		placeSpeed = prevPlaceSpeed;
	}

	@Override
	public void onNCPEnable() {
		// TODO Auto-generated method stub
		if (breakThreshold < 0.8F) {
			prevBreakThreshold = breakThreshold;
			breakThreshold = 0.8F;
		}
		if (placeSpeed < 2) {
			prevPlaceSpeed = placeSpeed;
			placeSpeed = 2;
		}
	}

	@Override
	public void save(PrintWriter writer) {
		// TODO Auto-generated method stub
		String settingsName = StringUtils.convertToSettingsName(getModName().getUnformattedTextForChat());
		writer.println(settingsName + "_break_threshold:" + breakThreshold);
		writer.println(settingsName + "_place_speed:" + placeSpeed);
	}

}
