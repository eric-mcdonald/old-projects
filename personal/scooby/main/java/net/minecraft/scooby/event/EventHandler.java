package net.minecraft.scooby.event;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.scooby.Scooby;
import net.minecraft.scooby.handlers.Handler;
import net.minecraft.scooby.mode.Mode;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Basic event manager using the existing events provided by Forge.  Mainly to keep all of this out of the main
 * Scooby class, keeping things clean.
 *
 * @author b
 * @since 3:37 PM on 3/15/2015
 */
public class EventHandler implements Handler {

	private Scooby scooby;

	@Override
	public void init(Scooby scooby) {
		this.scooby = scooby;
	}

	@SubscribeEvent
	public void onButtonPressed(InputEvent.MouseInputEvent event) {
		if (!Mouse.getEventButtonState())
			return;
		int keyCode = Mouse.getEventButton();
		for (Mode mode : scooby.modeHandler.getModes()) {
			if (mode.getToggleKey() == keyCode) {
				mode.setEnabled(!mode.isEnabled());
			}
		}
	}

	@SubscribeEvent
	public void onCommand(CommandEvent event) {
		ServerData currentServerData = scooby.mc.getCurrentServerData();
		if (scooby.commandHandler.getCommands().contains(event.command) && event.sender == scooby.mc.thePlayer && currentServerData != null && !currentServerData.serverIP.split(":")[0].equals("127.0.0.1") && !currentServerData.serverIP.split(":")[0].equals("localhost")) {
			event.setCanceled(true);
		}
	}
	@SubscribeEvent
	public void onEvent(Event event) {
		for (Mode mode : scooby.modeHandler.getModes()) {
			if (mode.isEnabled()) {
				mode.onEvent(event);
			}
		}
	}
	/**
	 * @see net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
	 */
	@SubscribeEvent
	public void onKeyPressed(InputEvent.KeyInputEvent event) {
		if (!Keyboard.getEventKeyState())
			return;
		int keyCode = Keyboard.getEventKey();
		for (Mode mode : scooby.modeHandler.getModes()) {
			if (mode.getToggleKey() == keyCode) {
				mode.setEnabled(!mode.isEnabled());
			}
		}
	}
}
