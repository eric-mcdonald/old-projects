package net.hackforums.pootpoot.monumental.init;

import net.hackforums.pootpoot.monumental.mode.modes.BrightnessMode;
import net.hackforums.pootpoot.monumental.mode.modes.NoCheatPlusMode;
import net.hackforums.pootpoot.monumental.mode.modes.SneakMode;
import net.hackforums.pootpoot.monumental.mode.modes.SpeedyGonzalesMode;
import net.hackforums.pootpoot.monumental.mode.modes.TracerMode;

public class Modes {

	public static void init() {
		
	}
	public static final BrightnessMode BRIGHTNESS_MODE = new BrightnessMode();
	public static final NoCheatPlusMode NOCHEATPLUS_MODE = new NoCheatPlusMode();
	public static final SneakMode SNEAK_MODE = new SneakMode();
	public static final SpeedyGonzalesMode SPEEDY_GONZALES_MODE = new SpeedyGonzalesMode();
	public static final TracerMode TRACER_MODE = new TracerMode();
}
