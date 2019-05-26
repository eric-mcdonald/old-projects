package org.bitbucket.valve_file_parsing.mdl;

public class MsStudioHitboxSet {
	public final int nameIndex;
	public final int numHitboxes;
	public final int hitboxIndex;
	public final MsStudioBbox[] bboxes;
	
	public MsStudioHitboxSet(final int nameIndex, final int numHitboxes, final int hitboxIndex) {
		this.nameIndex = nameIndex;
		this.numHitboxes = numHitboxes;
		this.hitboxIndex = hitboxIndex;
		bboxes = new MsStudioBbox[numHitboxes];
	}
}
