package org.bitbucket.valve_file_parsing;

import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.valve_file_parsing.mdl.MsStudioBbox;
import org.bitbucket.valve_file_parsing.mdl.MsStudioHitboxSet;
import org.bitbucket.valve_file_parsing.mdl.StudioHdr;

public final class MdlParser extends FileParser {
	private StudioHdr studioHdr;
	private MsStudioHitboxSet[] hitboxSets;
	
	@Override
	public byte[] data() {
		// TODO Auto-generated method stub
		return null;
	}

	public MsStudioHitboxSet[] getHitboxSets() {
		return hitboxSets;
	}
	public StudioHdr getStudioHdr() {
		return studioHdr;
	}
	@Override
	public void parse(final ByteBuffer dataBuf) throws InvalidDataException {
		// TODO Auto-generated method stub
		dataBuf.mark();
		studioHdr = new StudioHdr();
		studioHdr.id = dataBuf.getInt();
		studioHdr.version = dataBuf.getInt();
		studioHdr.checksum = dataBuf.getInt();
		final byte[] name = new byte[64];
		dataBuf.get(name);
		studioHdr.name = name;
		studioHdr.dataLength = dataBuf.getInt();
		studioHdr.eyePosition = new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat());
		studioHdr.illumPosition = new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat());
		studioHdr.hullMin = new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat());
		studioHdr.hullMax = new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat());
		studioHdr.viewBbmin = new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat());
		studioHdr.viewBbmax = new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat());
		studioHdr.flags = dataBuf.getInt();
		studioHdr.boneCount = dataBuf.getInt();
		studioHdr.boneOffset = dataBuf.getInt();
		studioHdr.bonecontrollerCount = dataBuf.getInt();
		studioHdr.bonecontrollerOffset = dataBuf.getInt();
		studioHdr.hitboxCount = dataBuf.getInt();
		studioHdr.hitboxOffset = dataBuf.getInt();
		hitboxSets = new MsStudioHitboxSet[studioHdr.hitboxCount];
		for (int i = 0; i < hitboxSets.length; i++) {
			dataBuf.position(studioHdr.hitboxOffset + i * 4 * 3);
			hitboxSets[i] = new MsStudioHitboxSet(dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt());
			for (int j = 0; j < hitboxSets[i].numHitboxes; j++) {
				dataBuf.position(studioHdr.hitboxOffset + i * 4 * 3 + hitboxSets[i].hitboxIndex + j * (4 * 2 + 4 * 3 + 4 * 3 + 4 + 4 * 3 + 4 + 4 * 4));
				hitboxSets[i].bboxes[j] = new MsStudioBbox(dataBuf.getInt(), dataBuf.getInt(), new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat()), new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat()), dataBuf.getInt(), new int[] {dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt()}, dataBuf.getInt(), new int[] {dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt()});
			}
		}
		// TODO(Eric) Parse the rest of it?
	}

}
