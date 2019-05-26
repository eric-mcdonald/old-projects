package com.example.examplemod;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public class FullbrightRoutine extends Routine {
	private float prevGamma = -999.0F;
	
	FullbrightRoutine() {
		super("Fullbright", Keyboard.KEY_B, true);
		// TODO Auto-generated constructor stub
	}

	void preUpdate() {
		if (prevGamma == -999.0F) {
			prevGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
		}
		Minecraft.getMinecraft().gameSettings.gammaSetting = 10.0F;
	}
	void cleanup() {
		if (prevGamma != -999.0F) {
			Minecraft.getMinecraft().gameSettings.gammaSetting = prevGamma;
			prevGamma = -999.0F;
		}
	}
}
