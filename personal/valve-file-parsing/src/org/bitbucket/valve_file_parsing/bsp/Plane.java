package org.bitbucket.valve_file_parsing.bsp;

import org.bitbucket.eric_generic.math.Vector;

public class Plane {
	public final Vector normal;
	public final float dist;
	public final int type;
	
	public Plane(final Vector normal, final float dist, final int type) {
		this.normal = normal;
		this.dist = dist;
		this.type = type;
	}
}
