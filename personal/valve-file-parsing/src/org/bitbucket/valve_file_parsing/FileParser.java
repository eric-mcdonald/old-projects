package org.bitbucket.valve_file_parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.nio.NioUtils;

public abstract class FileParser implements Parser {
	public final void parse(final File file) throws IOException {
		final FileInputStream fileIn = new FileInputStream(file);
		final ByteBuffer fileDataBuf = NioUtils.createBuffer(Math.min((int) file.length(), 67108864)); // Eric: Limit the amount of bytes to 64 MB to prevent OutOfMemoryError
		fileIn.read(fileDataBuf.array());
		fileIn.close();
		parse(fileDataBuf);
	}
}
