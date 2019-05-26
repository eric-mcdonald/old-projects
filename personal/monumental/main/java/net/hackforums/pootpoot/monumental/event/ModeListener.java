package net.hackforums.pootpoot.monumental.event;

import net.hackforums.pootpoot.monumental.mode.DisplayText;
import net.hackforums.pootpoot.monumental.mode.Displayable;
import net.hackforums.pootpoot.monumental.mode.EventHandler;
import net.hackforums.pootpoot.monumental.mode.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ModeListener {

	@SubscribeEvent
	public void onEvent(Event event) {
		for (Mode mode : Mode.MODES) {
			if (mode instanceof EventHandler) {
				((EventHandler) mode).onEvent(event);
			}
		}
	}
	@SubscribeEvent
	public void onInput(InputEvent event) {
		if (event instanceof InputEvent.KeyInputEvent) {
			for (Mode mode : Mode.MODES) {
				KeyBinding keyBind = mode.getKeyBind();
				if (keyBind != null && keyBind.isPressed()) {
					mode.setEnabled(!mode.isEnabled());
				}
			}
		}
	}
	@SubscribeEvent
	public void onRenderGameOverlayText(RenderGameOverlayEvent.Text event) {
		if (event.left.isEmpty() && event.right.isEmpty()) {
			int height = 2;
			for (Mode mode : Mode.MODES) {
				if (!(mode instanceof Displayable) || !mode.isEnabled()) {
					continue;
				}
				DisplayText displayText = ((Displayable) mode).getDisplayText();
				String formattedText = displayText.getDisplayComponent().getFormattedText();
				FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
				fontRenderer.drawStringWithShadow(formattedText, event.resolution.getScaledWidth() - fontRenderer.getStringWidth(formattedText) - 2, height, displayText.getColor());
				height += fontRenderer.FONT_HEIGHT + 1;
			}
		}
	}
}
