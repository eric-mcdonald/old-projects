package net.hackforums.pootpoot.monumental.command.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.hackforums.pootpoot.monumental.command.BaseCommand;
import net.hackforums.pootpoot.monumental.command.Configurable;
import net.hackforums.pootpoot.monumental.init.Modes;
import net.hackforums.pootpoot.monumental.util.MessageType;
import net.hackforums.pootpoot.monumental.util.MessageUtils;
import net.hackforums.pootpoot.monumental.util.StringUtils;
import net.minecraft.util.ChatComponentText;

public class TracerCommand extends BaseCommand implements Configurable {

	private boolean highlightAnimals, highlightMobs = true, highlightPlayers = true;
	public TracerCommand() {
		super(Modes.TRACER_MODE.getName(), Modes.TRACER_MODE.getDescription(), new ChatComponentText(".tracer [selection|reset] [selection2]"));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void load(BufferedReader reader) throws IOException {
		// TODO Auto-generated method stub
		String settingsName = StringUtils.convertToSettingsName(getModName().getUnformattedTextForChat()), line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith(settingsName)) {
				String[] tracerSetting = line.split(":");
				if (tracerSetting[0].equals(settingsName + "_animals")) {
					highlightAnimals = Boolean.parseBoolean(tracerSetting[1]);
				}
				else if (tracerSetting[0].equals(settingsName + "_mobs")) {
					highlightMobs = Boolean.parseBoolean(tracerSetting[1]);
				}
				else if (tracerSetting[0].equals(settingsName + "_players")) {
					highlightPlayers = Boolean.parseBoolean(tracerSetting[1]);
				}
			}
		}
	}

	@Override
	public boolean modExecute(String[] arguments) {
		// TODO Auto-generated method stub
		if (arguments.length == 0) {
			Modes.TRACER_MODE.setEnabled(!Modes.TRACER_MODE.isEnabled());
			MessageUtils.addMessage(getModName().appendText(": mode has been " + (Modes.TRACER_MODE.isEnabled() ? "enabled" : "disabled")), MessageType.INFO);
			return true;
		}
		else if (arguments.length >= 1) {
			if (arguments.length == 1 && arguments[0].equals("reset")) {
				highlightAnimals = false;
				highlightMobs = true;
				highlightPlayers = true;
				MessageUtils.addMessage(getModName().appendText(": settings have been reset to the default values"), MessageType.INFO);
				return true;
			}
			else if (arguments.length >= 1) {
				for (String argument : arguments) {
					List<String> toggledSelection = new ArrayList<String>();
					if (argument.equals("animals") || argument.equals("players") || argument.equals("mobs")) {
						if (toggledSelection.contains(argument)) {
							MessageUtils.addMessage(getModName().appendText(": cannot toggle selection " + argument + " because it was already toggled"), MessageType.ERROR);
							continue;
						}
						if (argument.equals("animals")) {
							highlightAnimals = !highlightAnimals;
							MessageUtils.addMessage(getModName().appendText(": selection " + argument + " has been " + (highlightAnimals ? "enabled" : "disabled")), MessageType.INFO);
						}
						else if (argument.equals("mobs")) {
							highlightMobs = !highlightMobs;
							MessageUtils.addMessage(getModName().appendText(": selection " + argument + " has been " + (highlightMobs ? "enabled" : "disabled")), MessageType.INFO);
						}
						else {
							highlightPlayers = !highlightPlayers;
							MessageUtils.addMessage(getModName().appendText(": selection " + argument + " has been " + (highlightPlayers ? "enabled" : "disabled")), MessageType.INFO);
						}
						toggledSelection.add(argument);
					}
					else {
						MessageUtils.addMessage(getModName().appendText(": cannot toggle selection " + argument + " because it is invalid"), MessageType.ERROR);
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void save(PrintWriter writer) {
		// TODO Auto-generated method stub
		String settingsName = StringUtils.convertToSettingsName(getModName().getUnformattedTextForChat());
		writer.println(settingsName + "_animals:" + highlightAnimals);
		writer.println(settingsName + "_mobs:" + highlightMobs);
		writer.println(settingsName + "_players:" + highlightPlayers);
	}

	public boolean willHighlightAnimals() {
		return highlightAnimals;
	}
	public boolean willHighlightMobs() {
		return highlightMobs;
	}
	public boolean willHighlightPlayers() {
		return highlightPlayers;
	}
}
