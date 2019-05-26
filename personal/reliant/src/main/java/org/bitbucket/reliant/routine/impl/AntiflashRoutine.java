package org.bitbucket.reliant.routine.impl;

import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class AntiflashRoutine extends DisplayRoutine {

	public AntiflashRoutine() {
		super("Anti-flash", "Modifies the player's flash effect.", true, true, -1, new FloatOption("Duration Threshold", "Specifies the threshold to wait for before removing the flash effect.", new ClampedNumber<Float>(6.0F, 0.1F, 6.0F), 0.1F), new FloatOption("Maximum Alpha", "Specifies the maximum alpha of the flash effect.", new ClampedNumber<Float>(0.0F, 0.0F, 255.0F), 10.0F));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFF8000;
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post || !SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return;
		}
		if (Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flFlashDuration")) <= getFloat("Duration Threshold")) {
			Reliant.instance.getProcessStream().write(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flFlashDuration"), 0.0F);
		}
		final float maxAlpha = getFloat("Maximum Alpha");
		if (Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flFlashMaxAlpha")) != maxAlpha) {
			Reliant.instance.getProcessStream().write(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flFlashMaxAlpha"), maxAlpha);
		}
	}

}
