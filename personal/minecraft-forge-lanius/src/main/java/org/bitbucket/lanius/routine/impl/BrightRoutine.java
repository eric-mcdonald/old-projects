package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class BrightRoutine extends TabbedRoutine {

	private float currentGamma, prevGamma;

	private boolean firstTick;

	public BrightRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.RENDER);
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
		return "Modifies the gamma to the specified value.";
	}

	@Override
	public void init() {
		prevGamma = -1.0F;
		firstTick = true;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Brightness";
	}

	@SubscribeEvent
	public void onRenderTick(final TickEvent.RenderTickEvent renderTickEv) {
		if (Lanius.mc.isGamePaused()) {
			return;
		}
		if (renderTickEv.phase.equals(TickEvent.Phase.START)) {
			if (firstTick) {
				currentGamma = Lanius.mc.gameSettings.gammaSetting;
				firstTick = false;
			}
			final float gamma = getFloat("Gamma").floatValue();
			final float PRECISION = 0.0001F;
			if (MathHelper.abs(gamma - currentGamma) > PRECISION) {
				if (gamma == 0.0F) {
					currentGamma = gamma;
				} else {
					final int fadeTicks = getInt("Fade Ticks").intValue();
					final float deltaGamma = gamma - Lanius.mc.gameSettings.gammaSetting;
					currentGamma += deltaGamma / (fadeTicks == 0 ? 1.0F : fadeTicks);
					// Eric: limit the gamma for in case the user changes it
					// while it is incrementing
					if (currentGamma < gamma && deltaGamma < 0.0F || currentGamma > gamma && deltaGamma > 0.0F) {
						currentGamma = gamma;
					}
				}
			}
			prevGamma = Lanius.mc.gameSettings.gammaSetting;
			Lanius.mc.gameSettings.gammaSetting = currentGamma;
		} else if (renderTickEv.phase.equals(TickEvent.Phase.END) && prevGamma != -1.0F) {
			Lanius.mc.gameSettings.gammaSetting = prevGamma;
			prevGamma = -1.0F;
		}
	}

	@Override
	public void registerValues() {
		registerValue("Gamma", 8.0F, 0.0F, 10.0F, "Specifies the gamma to use for the lightmap.");
		registerValue("Fade Ticks", 40, 0, 200, "Specifies the amount of time to increment the gamma over.");
	}

}
