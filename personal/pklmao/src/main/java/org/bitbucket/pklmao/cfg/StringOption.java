package org.bitbucket.pklmao.cfg;

public class StringOption extends BaseOption<Object> {
	public StringOption(String id, String nameKey, String descKey, String value, boolean requiresRestart) {
		super(id, nameKey, descKey, value, requiresRestart);
	}

	@Override
	public Object parseValue(String valueStr) {
		return valueStr;
	}
}
