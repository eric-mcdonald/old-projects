package net.minecraft.scooby.mode;

import net.minecraft.scooby.Scooby;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author b
 * @since 3:40 PM on 3/15/2015
 */
public abstract class Mode {

	/* Instance of the Scooby class */
	protected final Scooby scooby;

	/* The key code that is used to toggle the mod */
	private int toggleKey = -1;

	/* Whether or not the mod is currently enabled */
	private boolean enabled = false;
	private String name, configName;

	public Mode(Scooby scooby, String name, int toggleKey) {
		this.scooby = scooby;
		this.name = name;
		configName = name.toLowerCase().replace(" ", "_");
		this.toggleKey = toggleKey;
	}

	public String getConfigName() {
		return configName;
	}

	public String getName() {
		return name;
	}
	/**
	 * This is the key code used to toggle the mod.
	 */
	public int getToggleKey() {
		return this.toggleKey;
	}

	/**
	 * This method returns <code>true</code> if the mod is enabled.
	 *
	 * @return	<code>true</code> if the mod is currently enabled, else <code>false</code>.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	public abstract void onEvent(Event event);
	/**
	 * This method toggles the mod based on the specified state.
	 *
	 * @param	enabled	<code>true</code> if you want the mod enabled, else <code>false</code>.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void setToggleKey(int toggleKey) {
		this.toggleKey = toggleKey;
	}
}
