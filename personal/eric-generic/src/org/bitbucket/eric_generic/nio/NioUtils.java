package org.bitbucket.eric_generic.nio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NioUtils {
	public static ByteBuffer createBuffer(final int size) {
		return ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
	}
}
