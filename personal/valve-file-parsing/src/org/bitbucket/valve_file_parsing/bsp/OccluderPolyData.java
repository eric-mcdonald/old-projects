package org.bitbucket.valve_file_parsing.bsp;

public class OccluderPolyData {
	public final int firstVertexIdx, vertexCount;
	public final int planeNum;
	
	public OccluderPolyData(final int firstVertexIdx, final int vertexCount, final int planeNum) {
		this.firstVertexIdx = firstVertexIdx;
		this.vertexCount = vertexCount;
		this.planeNum = planeNum;
	}
}
