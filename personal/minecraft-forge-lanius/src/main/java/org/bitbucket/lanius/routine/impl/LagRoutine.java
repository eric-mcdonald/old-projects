package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.NetworkUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

public final class LagRoutine extends TabbedRoutine {

	public LagRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.MISCELLANEOUS);
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
		return "Puts " + Lanius.NAME + " into its lag compensation mode.";
	}

	@Override
	public String displayData() {
		return String.valueOf(NetworkUtils.lagTime());
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Lag Compensation";
	}

}
