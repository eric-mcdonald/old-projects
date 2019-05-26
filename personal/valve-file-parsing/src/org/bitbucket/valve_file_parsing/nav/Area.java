package org.bitbucket.valve_file_parsing.nav;

import org.bitbucket.eric_generic.math.Vector;

public class Area {
	public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3, DIRECTIONS_SZ = 4;
	public final int id;
	public final int attributeFlags; // Eric: byte on version <= 8, char on version < 13
	public final Vector nwCorner, seCorner;
	public final float neZ, swZ;
	public final Connection[] connections;
	public byte spotCount;
	public HidingSpot[] hidingSpots;
	public int encounterPathCount;
	
	public Area(final int id, final int attributeFlags, final Vector nwCorner, final Vector seCorner, final float neZ, final float swZ, final Connection[] connections) {
		this.id = id;
		this.attributeFlags = attributeFlags;
		this.nwCorner = nwCorner;
		this.seCorner = seCorner;
		this.neZ = neZ;
		this.swZ = swZ;
		this.connections = connections;
	}
}
