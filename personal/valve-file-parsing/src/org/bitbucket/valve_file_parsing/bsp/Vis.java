package org.bitbucket.valve_file_parsing.bsp;

public class Vis {
	public final int numClusters;
	public final int[][] byteOfs;
	
	public Vis(final int numClusters, final int[][] byteOfs) {
		this.numClusters = numClusters;
		this.byteOfs = byteOfs;
	}
}
