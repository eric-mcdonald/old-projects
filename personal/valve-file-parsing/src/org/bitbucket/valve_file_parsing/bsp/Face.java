package org.bitbucket.valve_file_parsing.bsp;

public class Face {
	public final char planeNum;
	public final byte side;
	public final byte onNode;
	public final int firstEdge;
	public final short numEdges;
	public final short texInfo;
	public final short dispInfo;
	public final short surfaceFogVolumeId;
	public final byte[] styles;
	public final int lightOffs;
	public final float area;
	public final int[] lightmapTexMinsLuxels, lightmapTexSzLuxels;
	public final int origFace;
	public final char numPrims, firstPrimId;
	public final int smoothingGroups;
	
	public Face(final char planeNum, final byte side, final byte onNode, final int firstEdge, final short numEdges, final short texInfo, final short dispInfo, final short surfaceFogVolumeId, final byte[] styles, final int lightOffs, final float area, final int[] lightmapTexMinsLuxels, final int[] lightmapTexSzLuxels, final int origFace, final char numPrims, final char firstPrimId, final int smoothingGroups) {
		this.planeNum = planeNum;
		this.side = side;
		this.onNode = onNode;
		this.firstEdge = firstEdge;
		this.numEdges = numEdges;
		this.texInfo = texInfo;
		this.dispInfo = dispInfo;
		this.surfaceFogVolumeId = surfaceFogVolumeId;
		this.styles = styles;
		this.lightOffs = lightOffs;
		this.area = area;
		this.lightmapTexMinsLuxels = lightmapTexMinsLuxels;
		this.lightmapTexSzLuxels = lightmapTexSzLuxels;
		this.origFace = origFace;
		this.numPrims = numPrims;
		this.firstPrimId = firstPrimId;
		this.smoothingGroups = smoothingGroups;
	}
}
