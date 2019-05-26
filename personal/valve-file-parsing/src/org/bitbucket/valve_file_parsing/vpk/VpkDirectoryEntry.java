package org.bitbucket.valve_file_parsing.vpk;

public class VpkDirectoryEntry
{
	public final int crc; // A 32bit CRC of the file's data.
	public final char preloadBytes; // The number of bytes contained in the index file.
 
	// A zero based index of the archive this file's data is contained in.
	// If 0x7fff, the data follows the directory.
	public final char archiveIndex;
 
	// If ArchiveIndex is 0x7fff, the offset of the file data relative to the end of the directory (see the header for more details).
	// Otherwise, the offset of the data from the start of the specified archive.
	public final int entryOffset;
 
	// If zero, the entire file is stored in the preload data.
	// Otherwise, the number of bytes stored starting at EntryOffset.
	public final int entryLength;
 
	public final char terminator = 0xffff;
	
	public byte[] preloadData;
	
	public VpkDirectoryEntry(final int crc, final char preloadBytes, final char archiveIndex, final int entryOffset, final int entryLength) {
		this.crc = crc;
		this.preloadBytes = preloadBytes;
		this.archiveIndex = archiveIndex;
		this.entryOffset = entryOffset;
		this.entryLength = entryLength;
	}
};