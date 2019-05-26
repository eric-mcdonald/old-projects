package org.bitbucket.reliant.memory;

import java.io.File;
import java.net.URL;

public interface OffsetManager {
	File offsetDir = new File("offset");
	Thread downloadThread();
	URL dumperUrl();
	File getDumperFile();
	File getOffsetsFile();
	void loadOffsets();
	long offset(final String name);
	Long putOffset(final String name, final long offset);
	void runDumper();
}
