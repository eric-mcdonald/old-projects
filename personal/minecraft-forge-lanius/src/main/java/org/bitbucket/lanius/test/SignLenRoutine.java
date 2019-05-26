package org.bitbucket.lanius.test;

import java.util.HashSet;

import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.Phase;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.network.play.client.CPacketUpdateSign;

public final class SignLenRoutine extends Routine implements Hook<NetHandlerData> {
	public SignLenRoutine() {
		super(Keyboard.KEY_NONE, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return TEST_COLOR;
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Tests if you can make a sign with more than 15 characters per line.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Sign Length";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!isEnabled() || !phase.equals(Phase.START) || !(data.retVal instanceof CPacketUpdateSign)) {
			return;
		}
		final String[] signLines = ((CPacketUpdateSign) data.retVal).getLines();
		for (int line = 0; line < signLines.length; line++) {
			signLines[line] = "";
			for (int charCount = 0; charCount < 384; charCount++) {
				signLines[line] += "a";
			}
		}
	}
}
