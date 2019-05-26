package org.bitbucket.reliant.memory;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitbucket.eric_generic.concurrent.DownloadFileThread;
import org.bitbucket.eric_generic.log.Logger;
import org.bitbucket.reliant.Reliant;

public abstract class BaseOffsetManager implements OffsetManager {
	private static final List<File> ranDumpers = new ArrayList<File>();
	private final File offsetsFile, dumperFile;
	private URL dumperUrl;
	protected final Map<String, Long> offsetMap = new HashMap<String, Long>();
	
	public BaseOffsetManager(final String offsetsFilename, final String dumperUrl, final String dumperFilename) {
		offsetsFile = new File(offsetDir, offsetsFilename);
		try {
			this.dumperUrl = new URL(dumperUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		}
		dumperFile = new File(offsetDir, dumperFilename);
	}
	
	@Override
	public final Thread downloadThread() {
		return new DownloadFileThread(getDumperFile(), dumperUrl());
	}

	@Override
	public final URL dumperUrl() {
		// TODO Auto-generated method stub
		return dumperUrl;
	}

	@Override
	public final File getDumperFile() {
		return dumperFile;
	}
	
	@Override
	public final File getOffsetsFile() {
		// TODO Auto-generated method stub
		return offsetsFile;
	}
	
	@Override
	public long offset(String name) {
		// TODO Auto-generated method stub
		if (!offsetMap.containsKey(name)) {
			Reliant.instance.getLogger().logFatal(new OffsetNotFoundException("Offset " + name + " does not exist in offset dumper " + dumperFile));
		}
		return offsetMap.get(name);
	}
	
	protected abstract void onRunDumper();
	
	@Override
	public final Long putOffset(final String name, final long offset) {
		if (offsetMap.containsKey(name)) {
			Reliant.instance.getLogger().log("Replacing offset " + name + "'s value of 0x" + Long.toHexString(offsetMap.get(name)).toUpperCase() + " with 0x" + Long.toHexString(offset).toUpperCase() + " in offset dumper " + dumperFile, Logger.Type.INFO);
		}
		return offsetMap.put(name, offset);
	}
	
	@Override
	public final void runDumper() {
		// TODO Auto-generated method stub
		if (ranDumpers.contains(dumperFile)) {
			return;
		}
		if (!dumperFile.exists()) {
			Reliant.instance.getLogger().logFatal(new FileNotFoundException(dumperFile.toString()));
		}
		Reliant.instance.getLogger().log("Waiting for " + dumperFile + " to finish executing.", Logger.Type.INFO);
		onRunDumper();
		ranDumpers.add(dumperFile);
	}
}
