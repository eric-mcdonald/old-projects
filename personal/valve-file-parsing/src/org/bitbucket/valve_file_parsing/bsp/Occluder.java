package org.bitbucket.valve_file_parsing.bsp;

public class Occluder {
	public final int count;
	public final OccluderData[] data;
	public final int polyDataCount;
	public final OccluderPolyData[] polyData;
	public final int vertexIndexCount;
	public final int[] vertexIndices;
	
	public Occluder(final int count, final OccluderData[] data, final int polyDataCount, final OccluderPolyData[] polyData, final int vertexIndexCount, final int[] vertexIndices) {
		this.count = count;
		this.data = data;
		this.polyDataCount = polyDataCount;
		this.polyData = polyData;
		this.vertexIndexCount = vertexIndexCount;
		this.vertexIndices = vertexIndices;
	}
}
