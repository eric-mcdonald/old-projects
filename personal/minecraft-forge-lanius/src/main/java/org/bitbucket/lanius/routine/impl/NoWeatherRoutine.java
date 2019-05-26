package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class NoWeatherRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private boolean prevRaining;

	private float prevRainStrength, prevThunderStrength;

	public NoWeatherRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.WORLD);
		// TODO Auto-generated constructor stub
	}

	private void changeRain(final boolean raining, final float rainStrength) {
		Lanius.mc.world.getWorldInfo().setRaining(raining);
		Lanius.mc.world.setRainStrength(rainStrength);
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Removes weather from displaying.";
	}

	@Override
	public void init() {
		if (Lanius.mc.world != null) {
			changeRain(prevRaining, prevRainStrength);
			Lanius.mc.world.setThunderStrength(prevThunderStrength);
		}
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "No Weather";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.world == null) {
			return;
		}
		changeRain(true, 0.0F);
		Lanius.mc.world.setThunderStrength(0.0F);
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		final boolean enabled = isEnabled();
		if (phase.equals(Phase.END) && data.retVal instanceof SPacketChangeGameState) {
			final SPacketChangeGameState gameStatePacket = (SPacketChangeGameState) data.retVal;
			final int gameState = gameStatePacket.getGameState();
			if (gameState == 1) {
				prevRaining = true;
				prevRainStrength = 0.0F;
			} else if (gameState == 2) {
				prevRaining = false;
				prevRainStrength = 1.0F;
				if (enabled) {
					changeRain(true, 0.0F);
				}
			} else {
				final float value = gameStatePacket.getValue();
				if (gameState == 7) {
					prevRainStrength = value;
					if (enabled) {
						Lanius.mc.world.setRainStrength(0.0F);
					}
				} else if (gameState == 8) {
					prevThunderStrength = value;
					if (enabled) {
						Lanius.mc.world.setThunderStrength(0.0F);
					}
				}
			}
		} else if (enabled && phase.equals(Phase.START)
				&& (data.retVal instanceof SPacketSoundEffect
						&& ((SPacketSoundEffect) data.retVal).getCategory().equals(SoundCategory.WEATHER)
						|| data.retVal instanceof SPacketCustomSound
								&& ((SPacketCustomSound) data.retVal).getCategory().equals(SoundCategory.WEATHER)
						|| data.retVal instanceof SPacketSpawnGlobalEntity
								&& ((SPacketSpawnGlobalEntity) data.retVal).getType() == 1)) {
			data.retVal = null;
		}
	}

	@Override
	@SubscribeEvent
	public void onUnload(final WorldEvent.Unload unloadEv) {
		super.onUnload(unloadEv);
		prevRaining = false;
		prevRainStrength = prevThunderStrength = 0.0F;
	}

}
