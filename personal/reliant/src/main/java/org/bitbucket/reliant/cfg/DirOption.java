package org.bitbucket.reliant.cfg;

public final class DirOption extends TextOption<String> {
	static final int MAX_PATH = 260;

	public DirOption(final String name, final String description, final String defaultVal) {
		super(name, description, defaultVal, MAX_PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String parseValue(final String value) {
		// TODO Auto-generated method stub
		return value;
	}

}
