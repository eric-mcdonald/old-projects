package org.bitbucket.valve_file_parsing.bsp;

public class GameLump {
	public final int id;
	public final char flags, version;
	public final int fileOfs, fileLen;
	
	public GameLump(final int id, final char flags, final char version, final int fileOfs, final int fileLen) {
		this.id = id;
		this.flags = flags;
		this.version = version;
		this.fileOfs = fileOfs;
		this.fileLen = fileLen;
	}
}
