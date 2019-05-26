package org.bitbucket.valve_file_parsing.vpk;

public class VpkHeaderV2 {
	public final int signature = 0x55aa1234, version = 2;
 
	// The size, in bytes, of the directory tree
	public final int treeSize;
 
	// How many bytes of file content are stored in this VPK file (0 in CSGO)
	public final int fileDataSectionSize;
 
	// The size, in bytes, of the section containing MD5 checksums for external archive content
	public final int archiveMd5SectionSize;
 
	// The size, in bytes, of the section containing MD5 checksums for content in this file (should always be 48)
	public final int otherMd5SectionSize;
 
	// The size, in bytes, of the section containing the public key and signature. This is either 0 (CSGO & The Ship) or 296 (HL2, HL2:DM, HL2:EP1, HL2:EP2, HL2:LC, TF2, DOD:S & CS:S)
	public final int signatureSectionSize;
	
	public VpkHeaderV2(final int treeSize, final int fileDataSectionSize, final int archiveMd5SectionSize, final int otherMd5SectionSize, final int signatureSectionSize) {
		this.treeSize = treeSize;
		this.fileDataSectionSize = fileDataSectionSize;
		this.archiveMd5SectionSize = archiveMd5SectionSize;
		this.otherMd5SectionSize = otherMd5SectionSize;
		this.signatureSectionSize = signatureSectionSize;
	}
}
