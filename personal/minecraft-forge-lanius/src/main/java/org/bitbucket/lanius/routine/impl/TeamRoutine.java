package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public final class TeamRoutine extends TabbedRoutine {

	public TeamRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.MISCELLANEOUS);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Allows " + Lanius.NAME + " to identify teammates.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Team";
	}

	@Override
	public void registerValues() {
		registerValue("Format", "c", "Specifies the chat formatting code that identifies teammates.");
		registerValue("Auto-identify", true,
				"Determines whether or not to automatically identify what team the player is on.");
		registerValue("Only Players", false, "Determines whether or not to only consider players as teammates.");
	}

	boolean teammate(final Entity entity) {
		if (!isEnabled() || getBoolean("Only Players") && !(entity instanceof EntityPlayer)) {
			return false;
		}
		final String entityName = entity.getDisplayName().getFormattedText();
		final String FORMAT_PREFIX = "\247";
		if (getBoolean("Auto-identify")) {
			final String playerName = Lanius.mc.player.getDisplayName().getFormattedText();
			for (int cIdx = playerName.indexOf(FORMAT_PREFIX); cIdx != -1 && cIdx < playerName.length() - 1; cIdx++) {
				final String formatCode = playerName.substring(cIdx, cIdx + 2);
				if (!formatCode.startsWith(FORMAT_PREFIX) || formatCode.equals(FORMAT_PREFIX + "r")) {
					continue;
				}
				return entityName.contains(formatCode);
			}
			return false;
		}
		return entityName.contains(FORMAT_PREFIX + getString("Format"));
	}

}
