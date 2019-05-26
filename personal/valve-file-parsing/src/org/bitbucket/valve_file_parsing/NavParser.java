package org.bitbucket.valve_file_parsing;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.valve_file_parsing.nav.Area;
import org.bitbucket.valve_file_parsing.nav.Connection;
import org.bitbucket.valve_file_parsing.nav.HidingSpot;

@SuppressWarnings("unused") // TODO(Eric) Finish the NAV parser
public final class NavParser extends FileParser {
	private int identifier, version;
	private int subVersion;
	private int saveBspSize;
	private byte analyzed;
	private byte hasUnnamedAreas;
	private String[] names;
	private int areaCount;
	private Area[] navAreas;
	
	@Override
	public byte[] data() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parse(final ByteBuffer dataBuf) throws InvalidDataException {
		// TODO Auto-generated method stub
		dataBuf.mark();
		identifier = dataBuf.getInt();
		if (identifier != 0xFEEDFACE) {
			throw new InvalidDataException("Invalid NAV identifier: " + identifier);
		}
		version = dataBuf.getInt();
		if (version >= 10) {
			subVersion = dataBuf.getInt();
		}
		if (version >= 4) {
			saveBspSize = dataBuf.getInt();
		}
		if (version >= 14) {
			analyzed = dataBuf.get();
		}
		if (version >= 5) {
			names = new String[dataBuf.getChar()];
			for (int i = 0; i < names.length; i++) {
				final byte[] nameData = new byte[dataBuf.getChar()];
				dataBuf.get(nameData);
				try {
					names[i] = new String(nameData, "US-ASCII");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (version > 11) {
				hasUnnamedAreas = dataBuf.get();
			}
		}
		areaCount = dataBuf.getInt();
		navAreas = new Area[areaCount];
		for (int i = 0; i < navAreas.length; i++) {
			navAreas[i] = new Area(dataBuf.getInt(), version <= 8 ? dataBuf.get() : version < 13 ? dataBuf.getChar() : dataBuf.getInt(), new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat()), new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat()), dataBuf.getFloat(), dataBuf.getFloat(), new Connection[] {new Connection(dataBuf.getInt()), new Connection(dataBuf.getInt()), new Connection(dataBuf.getInt()), new Connection(dataBuf.getInt())});
			// TODO(Eric) Refactor this; put the following in the constructor
			navAreas[i].spotCount = dataBuf.get();
			navAreas[i].hidingSpots = new HidingSpot[navAreas[i].spotCount];
			for (int j = 0; j < navAreas[i].hidingSpots.length; j++) {
				navAreas[i].hidingSpots[j] = new HidingSpot(dataBuf.getInt(), new Vector(dataBuf.getFloat(), dataBuf.getFloat(), dataBuf.getFloat()), dataBuf.get());
			}
			if (version < 15) {
				final byte approachAreaCount = dataBuf.get();
				dataBuf.position(dataBuf.position() + (4 * 3 + 2) * approachAreaCount);
			}
		}
	}

}
