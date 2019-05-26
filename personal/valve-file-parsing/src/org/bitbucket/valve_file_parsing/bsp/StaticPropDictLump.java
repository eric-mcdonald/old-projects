package org.bitbucket.valve_file_parsing.bsp;

public class StaticPropDictLump {
	public final int dictEntries;
	public final byte[][] name;
	
	public StaticPropDictLump(final int dictEntries, final byte[][] name) {
		this.dictEntries = dictEntries;
		this.name = name;
	}
}
