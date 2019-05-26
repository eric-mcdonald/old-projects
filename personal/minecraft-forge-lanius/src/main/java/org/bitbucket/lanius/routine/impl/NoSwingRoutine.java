package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.network.play.client.CPacketAnimation;

public final class NoSwingRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	public NoSwingRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.PLAYER);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Prevents the player from swinging their arm on the server-side.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "No Swing";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (isEnabled() && phase.equals(Phase.START) && data.retVal instanceof CPacketAnimation
				&& (!RoutineUtils.enabled("Retard")
						|| ((RetardRoutine) Lanius.getInstance().getRoutineRegistry().get("Retard")).isCancelSwing())) {
			data.retVal = null;
		}
	}

}
