package org.bitbucket.valve_file_parsing.nav;

import org.bitbucket.eric_generic.math.Vector;

public class HidingSpot {
	public final int id;
	public final Vector location;
	public final byte flags;
	
	public HidingSpot(final int id, final Vector location, final byte flags) {
		this.id = id;
		this.location = location;
		this.flags = flags;
	}
}
