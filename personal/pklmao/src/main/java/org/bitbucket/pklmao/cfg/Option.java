package org.bitbucket.pklmao.cfg;

public interface Option<T> {
	String id();
	String name();
	String desc();
	T getValue();
	void setValue(T value, boolean fromCfg);
	void setValue(String valueStr, boolean fromCfg);
	T parseValue(String valueStr);
}
