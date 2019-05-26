package net.hackforums.pootpoot.monumental.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import net.hackforums.pootpoot.monumental.Monumental;
import net.hackforums.pootpoot.monumental.command.Command;
import net.hackforums.pootpoot.monumental.command.Configurable;
import net.hackforums.pootpoot.monumental.mode.Mode;
import net.hackforums.pootpoot.monumental.util.KeyBindingUtils;
import net.hackforums.pootpoot.monumental.util.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class ModSettings {

	private static final File SETTINGS_DIRECTORY = new File(Minecraft.getMinecraft().mcDataDir, Monumental.MODID);
	private File modesFile = new File(SETTINGS_DIRECTORY, "modes.cfg"), keyBindsFile = new File(SETTINGS_DIRECTORY, "key_binds.cfg"), valuesFile = new File(SETTINGS_DIRECTORY, "values.cfg");
	private static final Logger LOGGER = LogManager.getLogger();
	public void loadKeyBinds() {
		if (!keyBindsFile.exists()) {
			return;
		}
		KeyBindingUtils.clearKeyBinds();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(keyBindsFile));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				String[] keyBindSetting = line.split(":");
				try {
					Mode mode = StringUtils.getModeFromString(keyBindSetting[0]);
					int keyCode = Keyboard.getKeyIndex(keyBindSetting[1]);
					mode.setKeyBind(keyCode == Keyboard.KEY_NONE ? null : new KeyBinding(mode.getName().getUnformattedTextForChat(), keyCode, Monumental.NAME));
				}
				catch (Exception e) {
					LOGGER.warn(Monumental.NAME + ": skipping bad key bind: " + line);
				}
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(Monumental.NAME + ": failed to load key binds", e);
		}
	}
	public void loadModes() {
		if (!modesFile.exists()) {
			return;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(modesFile));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				String[] modeSetting = line.split(":");
				try {
					StringUtils.getModeFromString(modeSetting[0]).setEnabled(Boolean.parseBoolean(modeSetting[1]));
				}
				catch (Exception e) {
					LOGGER.warn(Monumental.NAME + ": skipping bad mode: " + line);
				}
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(Monumental.NAME + ": failed to load modes", e);
		}
	}
	public void loadValues() {
		if (!valuesFile.exists()) {
			return;
		}
		try {
			for (Command command : Command.COMMANDS) {
				if (!(command instanceof Configurable)) {
					continue;
				}
				BufferedReader reader = new BufferedReader(new FileReader(valuesFile));
				((Configurable) command).load(reader);
				reader.close();
			}
		} catch (Exception e) {
			LOGGER.error(Monumental.NAME + ": failed to load values", e);
		}
	}
	public void saveKeyBinds() {
		try {
			SETTINGS_DIRECTORY.mkdir();
			PrintWriter writer = new PrintWriter(new FileWriter(keyBindsFile));
			writer.println("# This file contains the key binds.");
			for (Mode mode : Mode.MODES) {
				KeyBinding keyBind = mode.getKeyBind();
				writer.println(StringUtils.convertToSettingsName(mode.getName().getUnformattedTextForChat()) + ":" + (keyBind != null ? Keyboard.getKeyName(keyBind.getKeyCode()) : Keyboard.getKeyName(Keyboard.KEY_NONE)));
			}
			writer.close();
		}
		catch (Exception e) {
			LOGGER.error(Monumental.NAME + ": failed to save key binds", e);
		}
	}
	public void saveModes() {
		try {
			SETTINGS_DIRECTORY.mkdir();
			PrintWriter writer = new PrintWriter(new FileWriter(modesFile));
			writer.println("# This file contains the modes and their states.");
			for (Mode mode : Mode.MODES) {
				writer.println(StringUtils.convertToSettingsName(mode.getName().getUnformattedTextForChat()) + ":" + mode.isEnabled());
			}
			writer.close();
		}
		catch (Exception e) {
			LOGGER.error(Monumental.NAME + ": failed to save modes", e);
		}
	}
	public void saveValues() {
		try {
			SETTINGS_DIRECTORY.mkdir();
			PrintWriter writer = new PrintWriter(new FileWriter(valuesFile));
			writer.println("# This file contains the values.");
			for (Command command : Command.COMMANDS) {
				if (command instanceof Configurable) {
					((Configurable) command).save(writer);
				}
			}
			writer.close();
		}
		catch (Exception e) {
			LOGGER.error(Monumental.NAME + ": failed to save values", e);
		}
	}
}
