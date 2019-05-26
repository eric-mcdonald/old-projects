package org.bitbucket.lanius.cfg;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bitbucket.lanius.Lanius;

import net.minecraftforge.common.config.Configuration;

public abstract class ConfigContainer {
	private static final Configuration modCfg = Lanius.getInstance().getModCfg();
	private final Map<String, Boolean> boolValMap = new HashMap<String, Boolean>();
	private final Map<String, String> commentMap = new HashMap<String, String>();
	private final Map<String, ClampedNumber<Float>> floatValMap = new HashMap<String, ClampedNumber<Float>>();
	private final Map<String, ClampedNumber<Integer>> intValMap = new HashMap<String, ClampedNumber<Integer>>();
	private final Map<String, String> strValMap = new HashMap<String, String>();

	public final Set<String> boolNames() {
		return boolValMap.keySet();
	}

	public abstract String category();

	public final Set<String> floatNames() {
		return floatValMap.keySet();
	}

	public final Boolean getBoolean(final String name) {
		return boolValMap.get(name);
	}

	public final String getComment(final String name) {
		return commentMap.get(name);
	}

	public final ClampedNumber<Float> getFloat(final String name) {
		return floatValMap.get(name);
	}

	public final ClampedNumber<Integer> getInt(final String name) {
		return intValMap.get(name);
	}

	public final String getString(final String name) {
		return strValMap.get(name);
	}

	public final Set<String> intNames() {
		return intValMap.keySet();
	}

	public final boolean putValue(final String name, final boolean value) {
		return boolValMap.put(name, value);
	}

	public final String putValue(final String name, final String value) {
		return strValMap.put(name, value);
	}

	protected final void registerValue(final String name, boolean defaultVal, final String comment) {
		validateValue(name, boolValMap);
		defaultVal = modCfg.getBoolean(name, category(), defaultVal, comment);
		commentMap.put(name, comment);
		boolValMap.put(name, defaultVal);
	}

	protected final void registerValue(final String name, float defaultVal, final float minVal, final float maxVal,
			final String comment) {
		validateValue(name, floatValMap);
		defaultVal = modCfg.getFloat(name, category(), defaultVal, minVal, maxVal, comment);
		commentMap.put(name, comment);
		final ClampedNumber<Float> registeredVal = getFloat(name);
		if (registeredVal == null) {
			floatValMap.put(name, new ClampedNumber<Float>(defaultVal, minVal, maxVal));
		} else {
			// Eric: the maximum and minimum values should never change anyway
			registeredVal.setValue(defaultVal);
		}
	}

	protected final void registerValue(final String name, int defaultVal, final int minVal, final int maxVal,
			final String comment) {
		validateValue(name, intValMap);
		defaultVal = modCfg.getInt(name, category(), defaultVal, minVal, maxVal, comment);
		commentMap.put(name, comment);
		final ClampedNumber<Integer> registeredVal = getInt(name);
		if (registeredVal == null) {
			intValMap.put(name, new ClampedNumber<Integer>(defaultVal, minVal, maxVal));
		} else {
			registeredVal.setValue(defaultVal);
		}
	}

	protected final void registerValue(final String name, String defaultVal, final String comment) {
		validateValue(name, strValMap);
		defaultVal = modCfg.getString(name, category(), defaultVal, comment);
		commentMap.put(name, comment);
		strValMap.put(name, defaultVal);
	}

	public abstract void registerValues();

	public final Set<String> strNames() {
		return strValMap.keySet();
	}

	private void validateValue(final String name, final Map<?, ?> againstMap) {
		if (!againstMap.equals(boolValMap) && boolValMap.containsKey(name)
				|| !againstMap.equals(floatValMap) && floatValMap.containsKey(name)
				|| !againstMap.equals(intValMap) && intValMap.containsKey(name)
				|| !againstMap.equals(strValMap) && strValMap.containsKey(name)) {
			throw new ValueAlreadyRegisteredException(name, category());
		}
	}
}
