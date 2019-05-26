package org.bitbucket.valve_file_parsing.mdl;

import org.bitbucket.eric_generic.math.Vector;

public class MsStudioBbox {
	public final int		bone;
	public final int		group; // intersection group
	public final Vector	bbMin; // bounding box 
	public final Vector	bbMax;
	public final int		hitboxNameIndex; // offset to the name of the hitbox.
	public final int[]		padding; // 3
	public final float	radius;
	public final int[]	padding1; // 4
	
	public MsStudioBbox(final int bone, final int group, final Vector bbMin, final Vector bbMax, final int hitboxNameIndex, final int[] padding, final float radius, final int[] padding1) {
		this.bone = bone;
		this.group = group;
		this.bbMin = bbMin;
		this.bbMax = bbMax;
		this.hitboxNameIndex = hitboxNameIndex;
		this.padding = padding;
		this.radius = radius;
		this.padding1 = padding1;
	}
}