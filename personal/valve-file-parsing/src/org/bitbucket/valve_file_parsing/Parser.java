package org.bitbucket.valve_file_parsing;

import java.nio.ByteBuffer;

public interface Parser {
	byte[] data();
	void parse(final ByteBuffer dataBuf) throws InvalidDataException;
}
