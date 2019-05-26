package org.bitbucket.valve_file_parsing.bsp;

public class BrushSide {
	public final char planeNum;
	public final short texInfo;
	public final short dispInfo;
	public final short bevel;
	
	public BrushSide(final char planeNum, final short texInfo, final short dispInfo, final short bevel) {
		this.planeNum = planeNum;
		this.texInfo = texInfo;
		this.dispInfo = dispInfo;
		this.bevel = bevel;
	}
}
