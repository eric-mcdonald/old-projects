package org.bitbucket.valve_file_parsing.bsp;

import org.bitbucket.valve_file_parsing.math.CompressedLightCube;

public class Leaf {
	public final int contents;
	public final short cluster;
	public final int areaFlags;
	public final short[] mins, maxs;
	public final char firstLeafFace, numLeafFaces;
	public final char firstLeafBrush, numLeafBrushes;
	public final short leafWaterId;
	public CompressedLightCube ambientLighting;
	public short padding;
	
	public Leaf(final int contents, final short cluster, final int areaFlags, final short[] mins, final short[] maxs, final char firstLeafFace, final char numLeafFaces, final char firstLeafBrush, final char numLeafBrushes, final short leafWaterId) {
		this.contents = contents;
		this.cluster = cluster;
		this.areaFlags = areaFlags;
		this.mins = mins;
		this.maxs = maxs;
		this.firstLeafFace = firstLeafFace;
		this.numLeafFaces = numLeafFaces;
		this.firstLeafBrush = firstLeafBrush;
		this.numLeafBrushes = numLeafBrushes;
		this.leafWaterId = leafWaterId;
	}
	
	public final int area() {
		return areaFlags & 0x1FF;
	}
	public final int flags() {
		return (areaFlags >> 9) & 0x3F;
	}
}
