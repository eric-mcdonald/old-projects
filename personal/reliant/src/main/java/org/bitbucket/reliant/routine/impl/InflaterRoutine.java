package org.bitbucket.reliant.routine.impl;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.lang.time.BasicTimer;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.memory.WriteMemoryException;
import org.bitbucket.reliant.routine.PlayerRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class InflaterRoutine extends PlayerRoutine {
	private static final float BOUND_DIV = 10.0F;
	private boolean executed, canReset;
	private float currentScale = 1.0F;
	private boolean scaleMin;
	private final Timer scaleTimer = new BasicTimer(SdkUtils.TICK_TIME * 1);

	public InflaterRoutine() {
		super("Inflater", "Inflates and deflates player models.", true, 2001, new BoolOption("All Teams", "Specifies whether or not to scale all teams.", false), new BoolOption("Smooth", "Specifies whether or not to change a model scale smoothly.", true), new FloatOption("Minimum", "Specifies the minimum model scale.", new ClampedNumber<Float>(0.5F * BOUND_DIV, 0.1F * BOUND_DIV, 1.0F * BOUND_DIV), 0.1F), new FloatOption("Maximum", "Specifies the maximum model scale.", new ClampedNumber<Float>(1.1F * BOUND_DIV, 1.1F * BOUND_DIV, 10.0F * BOUND_DIV), 10.0F));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFF00FF00;
	}
	@Override
	public boolean handle(final int player, final int entityIdx) {
		if (!super.handle(player, entityIdx) || player == GameCache.getLocalPlayer() || !SdkUtils.entityAlive(player) || !SdkUtils.entityAlive(GameCache.getLocalPlayer()) || !scaleTimer.delayPassed()) {
			return false;
		}
		if (!getBoolean("All Teams") && Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) == Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum"))) {
			Reliant.instance.getProcessStream().write(player + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_flModelScale"), 1.0F);
			return false;
		}
		if (!Reliant.instance.getProcessStream().write(player + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_flModelScale"), currentScale)) {
			throw new WriteMemoryException("model_scale", currentScale, player + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_flModelScale"));
		}
		executed = canReset = true;
		return true;
	}
	@Override
	public String info() {
		// TODO Auto-generated method stub
		return StringUtils.formatNum(currentScale, "%,.1f");
	}
	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		if (canReset) {
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr != MemoryStream.NULL && GameCache.getInGame() == SdkUtils.SIGNONSTATE_FULL) {
				for (int playerIdx = 0; playerIdx <= SdkUtils.PLAYERS_SZ; playerIdx++) {
					final int player = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + playerIdx * SdkUtils.NEXT_ENTITY_SZ);
					if (player == MemoryStream.NULL || player == GameCache.getLocalPlayer() || StringUtils.empty(SdkUtils.readRadarName(playerIdx)) && playerIdx == 64) {
						continue;
					}
					Reliant.instance.getProcessStream().write(player + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_flModelScale"), 1.0F);
				}
			}
			canReset = false;
		}
		executed = false;
		currentScale = 1.0F;
		scaleMin = false;
		scaleTimer.reset();
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		super.update(post);
		if (!post) {
			if (scaleTimer.delayPassed()) {
				final float SCALE_INC = 0.1F;
				final boolean smooth = getBoolean("Smooth");
				final float minimum = getFloat("Minimum") / BOUND_DIV, maximum = getFloat("Maximum") / BOUND_DIV;
				currentScale = smooth ? scaleMin ? Math.max(currentScale - SCALE_INC, minimum) : Math.min(currentScale + SCALE_INC, maximum) : scaleMin ? minimum : maximum;
				if (!smooth || smooth && (currentScale == minimum || currentScale == maximum)) {
					scaleMin = !scaleMin;
				}
			}
		} else if (executed) {
			scaleTimer.setStartTime();
			executed = false;
		}
	}
}
