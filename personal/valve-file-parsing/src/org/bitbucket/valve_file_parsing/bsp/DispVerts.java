package org.bitbucket.valve_file_parsing.bsp;

import org.bitbucket.eric_generic.math.Vector;

public class DispVerts {
	public final Vector vec;
	public final float dist;
	public final float alpha;
	
	public DispVerts(final Vector vec, final float dist, final float alpha) {
		this.vec = vec;
		this.dist = dist;
		this.alpha = alpha;
	}
}
