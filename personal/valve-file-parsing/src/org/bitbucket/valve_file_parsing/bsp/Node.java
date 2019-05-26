package org.bitbucket.valve_file_parsing.bsp;

public class Node {
	public final int planeNum;
	public final int[] children;
	public final short[] mins, maxs;
	public final char firstFace, numFaces;
	public final short area;
	public final short padding;
	
	public Node(final int planeNum, final int[] children, final short[] mins, final short[] maxs, final char firstFace, final char numFaces, final short area, final short padding) {
		this.planeNum = planeNum;
		this.children = children;
		this.mins = mins;
		this.maxs = maxs;
		this.firstFace = firstFace;
		this.numFaces = numFaces;
		this.area = area;
		this.padding = padding;
	}
}
