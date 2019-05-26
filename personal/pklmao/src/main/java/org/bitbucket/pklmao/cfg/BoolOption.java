package org.bitbucket.pklmao.cfg;

public class BoolOption extends BaseOption<Boolean> {
	public BoolOption(String id, String nameKey, String descKey, boolean value, boolean requiresRestart) {
		super(id, nameKey, descKey, value, requiresRestart);
	}

	@Override
	public Boolean parseValue(String valueStr) {
		return Boolean.parseBoolean(valueStr);
	}
}
