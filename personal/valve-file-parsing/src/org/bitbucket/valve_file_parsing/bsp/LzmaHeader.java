package org.bitbucket.valve_file_parsing.bsp;

public class LzmaHeader {
	public final int id;
	public final int actualSize, lzmaSize;
	public final byte[] properties;
	
	public LzmaHeader(final int id, final int actualSize, final int lzmaSize, final byte[] properties) {
		this.id = id;
		this.actualSize = actualSize;
		this.lzmaSize = lzmaSize;
		this.properties = properties;
	}
}
