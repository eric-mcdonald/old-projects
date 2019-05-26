package net.hackforums.pootpoot.monumental.mode.modes;

import net.hackforums.pootpoot.monumental.Monumental;
import net.hackforums.pootpoot.monumental.init.Commands;
import net.hackforums.pootpoot.monumental.mode.BaseMode;
import net.hackforums.pootpoot.monumental.mode.DisplayText;
import net.hackforums.pootpoot.monumental.mode.Displayable;
import net.hackforums.pootpoot.monumental.mode.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import org.lwjgl.input.Keyboard;

public class BrightnessMode extends BaseMode implements Displayable, EventHandler {

	private DisplayText displayText = new DisplayText(getName(), new ChatComponentText(String.format("%,.1f", 10.0F)), 0xFFFF00);
	private float prevGammaSetting;
	public BrightnessMode() {
		super("Brightness", "Enables the player to be able to easily see in the dark", new KeyBinding("Brightness", Keyboard.KEY_B, Monumental.NAME));
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean bypassesNoCheatPlus() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public DisplayText getDisplayText() {
		// TODO Auto-generated method stub
		return displayText;
	}
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() {
		super.onEnable();
		prevGammaSetting = Minecraft.getMinecraft().gameSettings.gammaSetting;
	}

	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		if (event instanceof TickEvent.RenderTickEvent && ((TickEvent.RenderTickEvent) event).phase.equals(Phase.START)) {
			GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
			if (isEnabled()) {
				float gammaSetting = Commands.BRIGHTNESS_COMMAND.getGammaSetting(), deltaGammaSetting = gammaSetting - gameSettings.gammaSetting;
				displayText.setValue(new ChatComponentText(String.format("%,.1f", gammaSetting)));
				gameSettings.gammaSetting += gameSettings.gammaSetting < gammaSetting ? deltaGammaSetting < 0.1F ? deltaGammaSetting : 0.1F : deltaGammaSetting > -0.1F ? deltaGammaSetting : -0.1F;
			}
			else {
				float deltaGammaSetting = gameSettings.gammaSetting - prevGammaSetting;
				gameSettings.gammaSetting -= prevGammaSetting < gameSettings.gammaSetting ? deltaGammaSetting < 0.1F ? deltaGammaSetting : 0.1F : deltaGammaSetting > -0.1F ? deltaGammaSetting : -0.1F;
			}
		}
	}

}
