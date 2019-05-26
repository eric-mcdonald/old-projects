package org.bitbucket.valve_file_parsing.vpk;

public class VpkHeaderV1 {
	public final int signature = 0x55aa1234, version = 1;
	public final int treeSize;
	
	public VpkHeaderV1(final int treeSize) {
		this.treeSize = treeSize;
	}
}
