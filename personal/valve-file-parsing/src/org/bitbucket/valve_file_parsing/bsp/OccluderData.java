package org.bitbucket.valve_file_parsing.bsp;

import org.bitbucket.eric_generic.math.Vector;

public class OccluderData {
	public final int flags;
	public final int firstPoly;	// index into doccluderpolys
	public final int polyCount;	// amount of polygons
	public final Vector	mins;	        // minima of all vertices
	public final Vector	maxs;	        // maxima of all vertices
	// since v1
	public final int area;
	
	public OccluderData(final int flags, final int firstPoly, final int polyCount, final Vector mins, final Vector maxs, final int area) {
		this.flags = flags;
		this.firstPoly = firstPoly;
		this.polyCount = polyCount;
		this.mins = mins;
		this.maxs = maxs;
		this.area = area;
	}
}
