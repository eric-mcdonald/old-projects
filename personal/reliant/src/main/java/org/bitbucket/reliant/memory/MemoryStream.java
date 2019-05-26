package org.bitbucket.reliant.memory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.Reliant;

public final class MemoryStream {
	private final class MemoryProtection {
		public final int size;
		public final int protectConst;
		
		public MemoryProtection(final int size, final int protectConst) {
			this.size = size;
			this.protectConst = protectConst;
		}
	}
	public static final int LONG_SZ = Long.SIZE / Byte.SIZE, INT_SZ = Integer.SIZE / Byte.SIZE, SHORT_SZ = Short.SIZE / Byte.SIZE, BYTE_SZ = Byte.SIZE / Byte.SIZE, FLOAT_SZ = Float.SIZE / Byte.SIZE, DOUBLE_SZ = Double.SIZE / Byte.SIZE;
	public static final int NULL = 0x0;
	public static final int FALSE = 0, TRUE = 1;
	private final Map<String, Long> moduleCache = new HashMap<String, Long>(), moduleSizes = new HashMap<String, Long>();
	private final Map<Long, MemoryProtection> memProtectCache = new HashMap<Long, MemoryProtection>();
	private final long processHandle;
	//private long mouseHook;
	//private boolean suspended;
	
	public MemoryStream(final String process) throws OpenProcessException {
		final long processHandle = open(process);
		if (processHandle == NULL) {
			throw new OpenProcessException(process);
		}
		this.processHandle = processHandle;
	}
	private native void clientCmd(final long processHandle, final long engine, final String command);
	public void clientCmd(final String command) {
		final long clientAddr = moduleAddress("client.dll");
		if (clientAddr == NULL) {
			throw new InvalidDataException("client_addr");
		}
		clientCmd(processHandle, clientAddr + Reliant.instance.getCustomOffsetsRegistry().get("engine"), command);
	}
	private native void clientCmdUnrestricted(final long processHandle, final long engine, final String command, final byte param);
	public void clientCmdUnrestricted(final String command, final byte param) {
		final long clientAddr = moduleAddress("client.dll");
		if (clientAddr == NULL) {
			throw new InvalidDataException("client_addr");
		}
		clientCmdUnrestricted(processHandle, clientAddr + Reliant.instance.getCustomOffsetsRegistry().get("engine"), command, param);
	}
	/*public long callFunc(final long address, final long param) {
		return callFunc(address, param, 0xFFFFFFFF); // Eric: infinite timeout
	}
	public long callFunc(final long address, final long param, final int timeout) {
		return callFunc(processHandle, address, param, timeout);
	}
	private native long callFunc(final long processHandle, final long address, final long param, final int timeout);*/
	public boolean close() {
		return close(processHandle);
	}
	private native boolean close(final long processHandle);
	public long find(final String pattern, final String module) {
		final long moduleAddr = moduleAddress(module);
		if (moduleAddr == NULL) {
			return NULL;
		}
		final int patternBytes = pattern.length() / 2;
		final long moduleSz = moduleSize(module);
		if (moduleSz == 0L) {
			return NULL;
		}
		for (long currentAddr = moduleAddr; currentAddr < moduleAddr + moduleSz - patternBytes; currentAddr++) {
			boolean matches = true;
			for (int charIdx = 0, byteIdx = 0; charIdx < pattern.length() - 1 && byteIdx < patternBytes; charIdx += 2, byteIdx++) {
				final String byteStr = pattern.substring(charIdx, charIdx + 2);
				if (byteStr.equals("??")) {
					continue;
				}
				final byte[] data = read(currentAddr + byteIdx, BYTE_SZ);
				if (NioUtils.createBuffer(data.length * SHORT_SZ).put(data).getShort(0) != Short.parseShort(byteStr, 16)) {
					matches = false;
					break;
				}
			}
			if (matches) {
				return currentAddr;
			}
		}
		return NULL;
	}
	public int getPriorityClass() {
		return getPriorityClass(processHandle);
	}
	private native int getPriorityClass(final long processHandle);
	public long moduleAddress(final String module) {
		if (moduleCache.containsKey(module)) {
			return moduleCache.get(module);
		}
		final long modAddr = moduleAddress0(module);
		if (modAddr != NULL) {
			moduleCache.put(module, modAddr);
		}
		return modAddr;
	}
	/*private native long disableMouse0();
	public long disableMouse() {
		if (mouseHook != NULL) {
			return mouseHook;
		}
		return mouseHook = disableMouse0();
	}
	public boolean enableMouse() {
		if (mouseHook == NULL) {
			return false;
		}
		final boolean success = enableMouse(mouseHook);
		mouseHook = NULL;
		return success;
	}
	private native boolean enableMouse(final long mouseHook);*/
	private native long moduleAddress0(final String module);
	public String moduleFileName(final long module) {
		return moduleFileName(processHandle, module);
	}
	private native String moduleFileName(final long processHandle, final long module);
	public long moduleSize(final String module) {
		if (moduleSizes.containsKey(module)) {
			return moduleSizes.get(module);
		}
		final long modSize = moduleSize0(module);
		if (modSize != 0L) {
			moduleSizes.put(module, modSize);
		}
		return modSize;
	}
	private native long moduleSize0(final String module);
	private native long open(final String process);
	public int protect(final long address, final int size, final int protect) {
		final int oldProtect = protect(processHandle, address, size, protect);
		if (!memProtectCache.containsKey(address) && oldProtect != 0x0) {
			memProtectCache.put(address, new MemoryProtection(size, oldProtect));
		}
		return oldProtect;
	}
	private native int protect(final long processHandle, final long address, final int size, final int protect);
	public byte[] read(final long address, final int size) {
		return read(processHandle, address, size);
	}
	public String read(final long address, final int size, final String charsetName) {
		try {
			return StringUtils.crop(new String(read(address, size), charsetName));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		}
		return null;
	}
	/*private native void suspend0();
	public void suspend() {
		if (suspended) {
			return;
		}
		suspend0();
		suspended = true;
	}
	private native void resume0();
	public void resume() {
		if (!suspended) {
			return;
		}
		resume0();
		suspended = false;
	}*/
	//public native int captureInterface(final String name, final long module);
	private native byte[] read(final long processHandle, final long address, final int size);
	public byte readByte(final long address) {
		final byte[] data = read(address, BYTE_SZ);
		return NioUtils.createBuffer(data.length).put(data).get(0);
	}
	public double readDouble(final long address) {
		final byte[] data = read(address, DOUBLE_SZ);
		return NioUtils.createBuffer(data.length).put(data).getDouble(0);
	}
	public float readFloat(final long address) {
		final byte[] data = read(address, FLOAT_SZ);
		return NioUtils.createBuffer(data.length).put(data).getFloat(0);
	}
	public int readInt(final long address) {
		final byte[] data = read(address, INT_SZ);
		return NioUtils.createBuffer(data.length).put(data).getInt(0);
	}
	public long readLong(final long address) {
		final byte[] data = read(address, LONG_SZ);
		return NioUtils.createBuffer(data.length).put(data).getLong(0);
	}
	public short readShort(final long address) {
		final byte[] data = read(address, SHORT_SZ);
		return NioUtils.createBuffer(data.length).put(data).getShort(0);
	}
	public void resetProtect() {
		final Iterator<Map.Entry<Long, MemoryProtection>> memProtectIt = memProtectCache.entrySet().iterator();
		while (memProtectIt.hasNext()) {
			final Map.Entry<Long, MemoryProtection> memProtectEntry = memProtectIt.next();
			protect(processHandle, memProtectEntry.getKey(), memProtectEntry.getValue().size, memProtectEntry.getValue().protectConst);
			memProtectIt.remove();
		}
	}
	public int resetProtect(final long address) {
		if (!memProtectCache.containsKey(address)) {
			return 0;
		}
		final MemoryProtection protectInfo = memProtectCache.get(address);
		return protect(processHandle, address, protectInfo.size, protectInfo.protectConst);
	}
	private native int setClanTag(final long processHandle, final long setClanTagAddr, final String tag);
	public int setClanTag(final String tag) {
		return setClanTag(processHandle, moduleAddress("engine.dll") + Reliant.instance.getCustomOffsetsRegistry().get("SetClanTag"), tag);
	}
	public boolean setPriorityClass(final int priorityClass) {
		return setPriorityClass(processHandle, priorityClass);
	}
	private native boolean setPriorityClass(final long processHandle, final int priorityClass);
	public boolean write(final long address, final byte value) {
		return write(address, NioUtils.createBuffer(BYTE_SZ).put(value).array());
	}
	public boolean write(final long address, final byte[] buffer) {
		return write(processHandle, address, buffer);
	}
	public boolean write(final long address, final double value) {
		return write(address, NioUtils.createBuffer(DOUBLE_SZ).putDouble(value).array());
	}
	public boolean write(final long address, final float value) {
		return write(address, NioUtils.createBuffer(FLOAT_SZ).putFloat(value).array());
	}
	public boolean write(final long address, final int value) {
		return write(address, NioUtils.createBuffer(INT_SZ).putInt(value).array());
	}
	public boolean write(final long address, final long value) {
		return write(address, NioUtils.createBuffer(LONG_SZ).putLong(value).array());
	}
	private native boolean write(final long processHandle, final long address, final byte[] buffer);
	public boolean write(final long address, final short value) {
		return write(address, NioUtils.createBuffer(SHORT_SZ).putShort(value).array());
	}
	public boolean write(final long address, final String value, final int size, final String charsetName) {
		try {
			final byte[] valBytes = value.getBytes(charsetName), toWrite = new byte[Math.max(size, valBytes.length)];
			System.arraycopy(valBytes, 0, toWrite, 0, valBytes.length);
			return write(address, toWrite);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		}
		return false;
	}
}
