package org.bitbucket.lanius.routine;

import java.util.HashMap;
import java.util.Map;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.util.registry.Registry;

public abstract class ConfigRoutine extends TabbedRoutine {
	private Map<String[], Boolean> changedBoolMap;
	private Map<String[], Float> changedFloatMap;
	private Map<String[], Integer> changedIntMap;
	private Map<String[], String> changedStrMap;

	public ConfigRoutine(final int defaultKey, boolean hidden, final Tab guiTab) {
		super(defaultKey, hidden, guiTab);
		// TODO Auto-generated constructor stub
	}

	protected abstract void configureValues(final Registry<Routine> routineRegistry,
			final Registry<ConfigContainer> cfgContainerRegistry);

	protected final void putChangedBool(final String[] names, final boolean value, final boolean newVal) {
		changedBoolMap.put(names, value);
		Lanius.getInstance().getCfgContainerRegistry().get(names[0]).putValue(names[1], newVal);
	}

	protected final void putChangedFloat(final String[] names, final float value, final float newVal) {
		changedFloatMap.put(names, value);
		Lanius.getInstance().getCfgContainerRegistry().get(names[0]).getFloat(names[1]).setValue(newVal);
	}

	protected final void putChangedInt(final String[] names, final int value, final int newVal) {
		changedIntMap.put(names, value);
		Lanius.getInstance().getCfgContainerRegistry().get(names[0]).getInt(names[1]).setValue(newVal);
	}

	protected final void putChangedStr(final String[] names, final String value, final String newVal) {
		changedStrMap.put(names, value);
		Lanius.getInstance().getCfgContainerRegistry().get(names[0]).putValue(names[1], newVal);
	}

	@Override
	public void registerValues() {
		registerValue("Configuration", true,
				"Determines whether or not to change the configuration to the optimal values.");
	}

	@Override
	public void setEnabled() {
		super.setEnabled();
		if (getBoolean("Configuration")) {
			// Eric: initialize the maps before usage in this method for the
			// possibility of it being invoked before the constructor
			if (changedBoolMap == null) {
				changedBoolMap = new HashMap<String[], Boolean>();
				changedStrMap = new HashMap<String[], String>();
				changedIntMap = new HashMap<String[], Integer>();
				changedFloatMap = new HashMap<String[], Float>();
			}
			final Registry<ConfigContainer> cfgContainerRegistry = Lanius.getInstance().getCfgContainerRegistry();
			if (isEnabled()) {
				configureValues(Lanius.getInstance().getRoutineRegistry(), cfgContainerRegistry);
			} else {
				for (final Map.Entry<String[], Boolean> boolEntry : changedBoolMap.entrySet()) {
					cfgContainerRegistry.get(boolEntry.getKey()[0]).putValue(boolEntry.getKey()[1],
							boolEntry.getValue());
				}
				for (final Map.Entry<String[], String> strEntry : changedStrMap.entrySet()) {
					cfgContainerRegistry.get(strEntry.getKey()[0]).putValue(strEntry.getKey()[1], strEntry.getValue());
				}
				for (final Map.Entry<String[], Integer> intEntry : changedIntMap.entrySet()) {
					cfgContainerRegistry.get(intEntry.getKey()[0]).getInt(intEntry.getKey()[1])
							.setValue(intEntry.getValue());
				}
				for (final Map.Entry<String[], Float> floatEntry : changedFloatMap.entrySet()) {
					cfgContainerRegistry.get(floatEntry.getKey()[0]).getFloat(floatEntry.getKey()[1])
							.setValue(floatEntry.getValue());
				}
				changedBoolMap.clear();
				changedStrMap.clear();
				changedIntMap.clear();
				changedFloatMap.clear();
			}
		}
	}
}
