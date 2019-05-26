package org.bitbucket.valve_file_parsing.bsp;

public class Lump {
	public final int fileOffs, fileLen, version;
	public final byte[] fourCc;
	
	public Lump(final int fileOffs, final int fileLen, final int version, final byte[] fourCc) {
		this.fileOffs = fileOffs;
		this.fileLen = fileLen;
		this.version = version;
		this.fourCc = fourCc;
	}
}
