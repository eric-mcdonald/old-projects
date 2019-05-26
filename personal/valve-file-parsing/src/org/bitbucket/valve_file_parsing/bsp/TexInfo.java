package org.bitbucket.valve_file_parsing.bsp;

public class TexInfo {
	public static final int SURF_LIGHT = 0x1; // value will hold the light strength
	public static final int SURF_SKY2D = 0x2; // don't draw, indicates we should skylight + draw 2d sky but not draw the 3D skybox
	public static final int SURF_SKY = 0x4; // don't draw, but add to skybox
	public static final int SURF_WARP = 0x8; // turbulent water warp
	public static final int SURF_TRANS = 0x10; // texture is translucent
	public static final int SURF_NOPORTAL = 0x20; // the public static final int SURFace can not have a portal placed on it
	public static final int SURF_TRIGGER = 0x40; // FIXME: This is an xbox hack to work around elimination of trigger public static final int SURFaces, which breaks occluders
	public static final int SURF_NODRAW = 0x80; // don't bother referencing the texture
	public static final int SURF_HINT = 0x100; // make a primary bsp splitter
	public static final int SURF_SKIP = 0x200; // completely ignore, allowing non-closed brushes
	public static final int SURF_NOLIGHT = 0x400; // Don't calculate light
	public static final int SURF_BUMPLIGHT = 0x800; // calculate three lightmaps for the public static final int SURFace for bumpmapping
	public static final int SURF_NOSHADOWS = 0x1000; // Don't receive shadows
	public static final int SURF_NODECALS = 0x2000; // Don't receive decals
	public static final int SURF_NOCHOP = 0x4000; // Don't subdivide patches on this public static final int SURFace
	public static final int SURF_HITBOX = 0x8000; // surface is part of a hitbox
	public final float[][] textureVecs;
	public final float[][] lightmapVecs;
	public final int flags;
	public final int texData;
	
	public TexInfo(final float[][] textureVecs, final float[][] lightmapVecs, final int flags, final int texData) {
		this.textureVecs = textureVecs;
		this.lightmapVecs = lightmapVecs;
		this.flags = flags;
		this.texData = texData;
	}
}
