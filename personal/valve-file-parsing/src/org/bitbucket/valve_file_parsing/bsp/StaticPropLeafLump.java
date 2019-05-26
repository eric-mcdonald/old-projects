package org.bitbucket.valve_file_parsing.bsp;

public class StaticPropLeafLump {
	public final int leafEntries;
	public final char[] leaf;
	
	public StaticPropLeafLump(final int leafEntries, final char[] leaf) {
		this.leafEntries = leafEntries;
		this.leaf = leaf;
	}
}
