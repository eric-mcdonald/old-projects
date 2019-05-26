package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.ConfigRoutine;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.registry.Registry;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

public final class NcpRoutine extends ConfigRoutine {
	public NcpRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.MISCELLANEOUS);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	protected void configureValues(final Registry<Routine> routineRegistry,
			final Registry<ConfigContainer> cfgContainerRegistry) {
		// TODO Auto-generated method stub
		for (final Routine routine : routineRegistry.objects()) {
			if (routine.isEnabled() && !routine.compatibleWith().contains(Compatibility.NOCHEATPLUS)) {
				routine.setEnabled();
			}
		}
		String container = "Creative Drop";
		String cfgVal = "Delay";
		if (cfgContainerRegistry.get(container).getInt(cfgVal).intValue() < 50) {
			putChangedInt(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getInt(cfgVal).intValue(), 50);
		}
		cfgVal = "Legitimate Stacks";
		if (!cfgContainerRegistry.get(container).getBoolean(cfgVal)) {
			putChangedBool(new String[] { container, cfgVal }, cfgContainerRegistry.get(container).getBoolean(cfgVal),
					true);
		}
		container = "Speedy Gonzales";
		cfgVal = "Click Delay";
		if (cfgContainerRegistry.get(container).getInt(cfgVal).intValue() < 2) {
			putChangedInt(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getInt(cfgVal).intValue(), 2);
		}
		cfgVal = "Hit Delay";
		if (cfgContainerRegistry.get(container).getInt(cfgVal).intValue() < 1) {
			putChangedInt(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getInt(cfgVal).intValue(), 1);
		}
		cfgVal = "Threshold";
		if (cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue() < 0.8F) {
			putChangedFloat(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue(), 0.8F);
		}
		container = "Kill Aura";
		cfgVal = "Delay";
		if (cfgContainerRegistry.get(container).getInt(cfgVal).intValue() < 167) {
			putChangedInt(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getInt(cfgVal).intValue(), 167);
		}
		cfgVal = "Radius";
		if (cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue() > 4.2F) {
			putChangedFloat(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue(), 4.2F);
		}
		cfgVal = "Swing";
		if (!cfgContainerRegistry.get(container).getBoolean(cfgVal)) {
			putChangedBool(new String[] { container, cfgVal }, cfgContainerRegistry.get(container).getBoolean(cfgVal),
					true);
		}
		container = "Spam";
		cfgVal = "Delay";
		if (cfgContainerRegistry.get(container).getInt(cfgVal).intValue() < 2000) {
			putChangedInt(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getInt(cfgVal).intValue(), 2000);
		}
		container = "Speed";
		cfgVal = "H-acc";
		if (!cfgContainerRegistry.get(container).getBoolean(cfgVal)) {
			putChangedBool(new String[] { container, cfgVal }, cfgContainerRegistry.get(container).getBoolean(cfgVal),
					true);
		}
		cfgVal = "Multiplier";
		if (cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue() > 1.7F) {
			putChangedFloat(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue(), 1.7F);
		}
		container = "Nuker";
		cfgVal = "Swing";
		if (!cfgContainerRegistry.get(container).getBoolean(cfgVal)) {
			putChangedBool(new String[] { container, cfgVal }, cfgContainerRegistry.get(container).getBoolean(cfgVal),
					true);
		}
		cfgVal = "Delay";
		if (cfgContainerRegistry.get(container).getInt(cfgVal).intValue() < 100) {
			putChangedInt(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getInt(cfgVal).intValue(), 100);
		}
		cfgVal = "Nuke Radius";
		if (cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue() > 5.2F) {
			putChangedFloat(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue(), 5.2F);
		}
		container = "Entity Launcher";
		cfgVal = "Swing";
		if (!cfgContainerRegistry.get(container).getBoolean(cfgVal)) {
			putChangedBool(new String[] { container, cfgVal }, cfgContainerRegistry.get(container).getBoolean(cfgVal),
					true);
		}
		cfgVal = "Delay";
		if (cfgContainerRegistry.get(container).getInt(cfgVal).intValue() < 100) {
			putChangedInt(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getInt(cfgVal).intValue(), 100);
		}
		container = "Boat";
		cfgVal = "Speed";
		if (cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue() > 3.2F) {
			putChangedFloat(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue(), 3.2F);
		}
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Puts " + Lanius.NAME + " into its NoCheatPlus bypass mode.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "NoCheatPlus";
	}

}
