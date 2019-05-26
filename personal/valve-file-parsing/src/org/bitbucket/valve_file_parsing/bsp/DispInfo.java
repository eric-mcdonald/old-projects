package org.bitbucket.valve_file_parsing.bsp;

import org.bitbucket.eric_generic.math.Vector;

public class DispInfo {
	public final Vector			startPosition;		// start position used for orientation
	public final int			dispVertStart;		// Index public final into LUMP_DISP_VERTS.
	public final int			dispTriStart;		// Index public final into LUMP_DISP_TRIS.
	public final int			power;			// power - indicates size of surface (2^power	1)
	public final int			minTess;		// minimum tesselation allowed
	public final float			smoothingAngle;		// lighting smoothing angle
	public final int			contents;		// surface contents
	public final char		mapFace;		// Which map face this displacement comes from.
	public final int			lightmapAlphaStart;	// Index public final into ddisplightmapalpha.
	public final int			lightmapSamplePositionStart;	// Index public final into LUMP_DISP_LIGHTMAP_SAMPLE_POSITIONS.
	public final byte[] neighborData;
	// Eric: Unsupported for now
	//DispNeighbor[]		edgeNeighbors;	// Indexed by NEIGHBOREDGE_ defines.
	//DispCornerNeighbors[]	cornerNeighbors;	// Indexed by CORNER_ defines.
	public final int[]		allowedVerts;	// active verticies
	
	public DispInfo(final Vector startPosition, final int dispVertStart, final int dispTriStart, final int power, final int minTess, final float smoothingAngle, final int contents, final char mapFace, final int lightmapAlphaStart, final int lightmapSamplePositionStart, final byte[] neighborData, final int[] allowedVerts) {
		this.startPosition = startPosition;
		this.dispVertStart = dispVertStart;
		this.dispTriStart = dispTriStart;
		this.power = power;
		this.minTess = minTess;
		this.smoothingAngle = smoothingAngle;
		this.contents = contents;
		this.mapFace = mapFace;
		this.lightmapAlphaStart = lightmapAlphaStart;
		this.lightmapSamplePositionStart = lightmapSamplePositionStart;
		this.neighborData = neighborData;
		this.allowedVerts = allowedVerts;
	}
}
