package org.bitbucket.valve_file_parsing;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.bitbucket.valve_file_parsing.vpk.VpkDirectoryEntry;
import org.bitbucket.valve_file_parsing.vpk.VpkHeaderV1;
import org.bitbucket.valve_file_parsing.vpk.VpkHeaderV2;

public final class VpkParser extends FileParser {
	private static final String END_PARENT = "";
	private Map<String, VpkDirectoryEntry> dirEntryMap;
	private VpkHeaderV1 vpkHeader1;
	private VpkHeaderV2 vpkHeader2;
	
	@Override
	public byte[] data() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, VpkDirectoryEntry> getDirEntryMap() {
		return dirEntryMap;
	}
	public VpkHeaderV1 getVpkHeader1() {
		return vpkHeader1;
	}
	public VpkHeaderV2 getVpkHeader2() {
		return vpkHeader2;
	}
	@Override
	public void parse(final ByteBuffer dataBuf) throws InvalidDataException {
		// TODO Auto-generated method stub
		dataBuf.mark();
		dataBuf.getInt();
		final int version = dataBuf.getInt();
		if (version == 1) {
			vpkHeader1 = new VpkHeaderV1(dataBuf.getInt());
		} else {
			vpkHeader2 = new VpkHeaderV2(dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt(), dataBuf.getInt());
		}
		dirEntryMap = new HashMap<String, VpkDirectoryEntry>();
		final String EMPTY_NODE = " ";
		while (true) {
			final String extension = read(dataBuf);
			if (extension.equals(END_PARENT)) {
				break;
			}
			while (true) {
				final String path = read(dataBuf);
				if (path.equals(END_PARENT)) {
					break;
				}
				while (true) {
					final String file = read(dataBuf);
					if (file.equals(END_PARENT)) {
						break;
					}
					String key = (path.equals(EMPTY_NODE) ? "" : path + "/") + file;
					if (!extension.equals(EMPTY_NODE)) {
						key += "." + extension;
					}
					dirEntryMap.put(key, new VpkDirectoryEntry(dataBuf.getInt(), dataBuf.getChar(), dataBuf.getChar(), dataBuf.getInt(), dataBuf.getInt()));
					dataBuf.getChar();
					final VpkDirectoryEntry dirEntry = dirEntryMap.get(key);
					if (dirEntry.preloadBytes != 0) {
						dirEntry.preloadData = new byte[dirEntry.preloadBytes];
						dataBuf.get(dirEntry.preloadData);
					}
				}
			}
			// Eric: We are only using the MDLs for now
			if (extension.equals("mdl")) {
				break;
			}
		}
	}

	private String read(final ByteBuffer dataBuf) {
		String str = END_PARENT;
		while (true) {
			final byte character = dataBuf.get();
			if (character == 0) {
				return str;
			}
			try {
				str += new String(new byte[] {character}, "US-ASCII");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
