package net.minecraft.scooby.util;

import net.minecraft.scooby.Scooby;
import net.minecraft.scooby.mode.Mode;

public class ModeUtils {

	private Scooby scooby;
	public ModeUtils(Scooby scooby) {
		this.scooby = scooby;
	}
	public Mode getModeByName(String modeName) {
		for (Mode mode : scooby.modeHandler.getModes()) {
			if (mode.getName().equals(modeName)) {
				return mode;
			}
		}
		return null;
	}
}
