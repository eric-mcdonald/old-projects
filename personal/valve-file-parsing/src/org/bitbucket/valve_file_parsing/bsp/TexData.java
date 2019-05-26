package org.bitbucket.valve_file_parsing.bsp;

import org.bitbucket.eric_generic.math.Vector;

public class TexData {
	public final Vector reflectivity;
	public final int nameStringTableId;
	public final int width, height;
	public final int viewWidth, viewHeight;
	
	public TexData(final Vector reflectivity, final int nameStringTableId, final int width, final int height, final int viewWidth, final int viewHeight) {
		this.reflectivity = reflectivity;
		this.nameStringTableId = nameStringTableId;
		this.width = height;
		this.height = height;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
	}
}
