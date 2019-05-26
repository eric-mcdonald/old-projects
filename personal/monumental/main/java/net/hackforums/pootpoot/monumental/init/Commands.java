package net.hackforums.pootpoot.monumental.init;

import net.hackforums.pootpoot.monumental.command.commands.BindCommand;
import net.hackforums.pootpoot.monumental.command.commands.BrightnessCommand;
import net.hackforums.pootpoot.monumental.command.commands.HelpCommand;
import net.hackforums.pootpoot.monumental.command.commands.SpeedyGonzalesCommand;
import net.hackforums.pootpoot.monumental.command.commands.ToggleCommand;
import net.hackforums.pootpoot.monumental.command.commands.TracerCommand;

public class Commands {

	public static void init() {
		
	}
	public static final BindCommand BIND_COMMAND = new BindCommand();
	public static final BrightnessCommand BRIGHTNESS_COMMAND = new BrightnessCommand();
	public static final HelpCommand HELP_COMMAND = new HelpCommand();
	public static final SpeedyGonzalesCommand SPEEDY_GONZALES_COMMAND = new SpeedyGonzalesCommand();
	public static final ToggleCommand TOGGLE_COMMAND = new ToggleCommand();
	public static final TracerCommand TRACER_COMMAND = new TracerCommand();
}
