package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class ReviveRoutine extends TabbedRoutine {

	private boolean sentRespawn;

	public ReviveRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.PLAYER);
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
		return "Automatically respawns the player.";
	}

	@Override
	public void init() {
		sentRespawn = false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Revive";
	}

	@SubscribeEvent
	public void onClientTick(
			final TickEvent.ClientTickEvent clientTickEv /*
															 * Eric: LivingDeathEvent doesn 't work for the player
															 */) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || Lanius.mc.isGamePaused()) {
			return;
		}
		if (Lanius.mc.player.isEntityAlive()) {
			init();
			return;
		}
		final boolean tpHome = getBoolean("Teleport Home");
		if (!sentRespawn) {
			if (tpHome) {
				Lanius.mc.player.sendChatMessage(getString("Set Home"));
			}
			Lanius.mc.player.respawnPlayer();
			if (tpHome) {
				Lanius.mc.player.sendChatMessage(getString("Home"));
			}
			sentRespawn = true;
		}
	}

	@Override
	public void registerValues() {
		registerValue("Teleport Home", false,
				"Determines whether or not to respawn the player at their death location using the specified commands.");
		registerValue("Set Home", "/sethome", "Specifies the chat message to send to set your home.");
		registerValue("Home", "/home", "Specifies the chat message to send to teleport to your home.");
	}

}
