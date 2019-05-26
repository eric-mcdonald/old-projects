package net.minecraft.scooby.mode;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.scooby.Scooby;
import net.minecraft.scooby.handlers.Handler;
import net.minecraft.scooby.mode.modes.AimbotMode;
import net.minecraft.scooby.mode.modes.CriticalsMode;
import net.minecraft.scooby.mode.modes.SprintMode;
import net.minecraft.scooby.mode.modes.TriggerBotMode;
import net.minecraft.scooby.mode.modes.VelocityMode;

/**
 * @author b
 * @since 3:50 PM on 3/15/2015
 */
public class ModeHandler implements Handler {

	/* The list of mods.  This will allow us to loop through the mods to call stuff rather than individually. */
	private final List<Mode> modes = new CopyOnWriteArrayList<Mode>();

	/**
	 * @return	The mod list, rather than having it be public.  Basic getter.
	 */
	public List<Mode> getModes() {
		return this.modes;
	}

	/**
	 * 'Registers'/loads the mods.  Basically just loading them into the game by making instances of them.
	 */
	@Override
	public void init(Scooby scooby) {
		registerMode(new TriggerBotMode(scooby));
		registerMode(new SprintMode(scooby));
		registerMode(new AimbotMode(scooby));
		registerMode(new VelocityMode(scooby));
		registerMode(new CriticalsMode(scooby));
	}

	/**
	 * Adds the specified Mod instance to the mods list.  Since this will only be used in this class, it's private.
	 * @param mode	The specified Mod to 'register'.
	 */
	private void registerMode(Mode mode) {
		this.modes.add(mode);
	}
}
