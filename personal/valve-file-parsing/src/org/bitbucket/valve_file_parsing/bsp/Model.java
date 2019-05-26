package org.bitbucket.valve_file_parsing.bsp;

import org.bitbucket.eric_generic.math.Vector;

public class Model {
	public final Vector mins, maxs;
	public final Vector origin;
	public final int headNode;
	public final int firstFace, numFaces;
	
	public Model(final Vector mins, final Vector maxs, final Vector origin, final int headNode, final int firstFace, final int numFaces) {
		this.mins = mins;
		this.maxs = maxs;
		this.origin = origin;
		this.headNode = headNode;
		this.firstFace = firstFace;
		this.numFaces = numFaces;
	}
}
