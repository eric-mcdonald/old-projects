package net.hackforums.pootpoot.monumental.util;

import java.util.ArrayList;
import java.util.List;

import net.hackforums.pootpoot.monumental.Monumental;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindingUtils {

	public static void clearKeyBinds() {
		List<KeyBinding> keyBindings = new ArrayList<KeyBinding>();
		for (KeyBinding keyBinding : Minecraft.getMinecraft().gameSettings.keyBindings) {
			if (!keyBinding.getKeyCategory().equals(Monumental.NAME)) {
				keyBindings.add(keyBinding);
			}
		}
		Minecraft.getMinecraft().gameSettings.keyBindings = keyBindings.toArray(new KeyBinding[0]);
	}
}
