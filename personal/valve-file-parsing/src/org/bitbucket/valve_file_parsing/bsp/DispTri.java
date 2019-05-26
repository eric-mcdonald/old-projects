package org.bitbucket.valve_file_parsing.bsp;

public class DispTri {
	public static final char DISPTRI_TAG_SURFACE	= 0x1;
	public static final char DISPTRI_TAG_WALKABLE	= 0x2;
	public static final char DISPTRI_TAG_BUILDABLE	= 0x4;
	public static final char DISPTRI_FLAG_SURFPROP1	= 0x8;
	public static final char DISPTRI_FLAG_SURFPROP2	= 0x10;
	public final char tags;
	
	public DispTri(final char tags) {
		this.tags = tags;
	}
}
