package org.bitbucket.reliant.memory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Reliant;

public final class HazeOffsetManager extends InputOffsetManager {
	private long timestamp;
	
	public HazeOffsetManager() {
		super("csgo.toml", "https://dl.dropboxusercontent.com/s/f45o6w3dep6zz5m/hazedumper_v1.3.exe", "hazedumper_v1.3.exe");
		// TODO Auto-generated constructor stub
	}

	public long getTimestamp() {
		return timestamp;
	}
	@Override
	public void loadOffsets() {
		// TODO Auto-generated method stub
		try {
			final BufferedReader offsetsIn = new BufferedReader(new FileReader(getOffsetsFile()));
			String line;
			final String EQUALS = " = ";
			try {
				while ((line = offsetsIn.readLine()) != null) {
					if (StringUtils.comment(line)) {
						continue;
					}
					final int foundEquals = line.indexOf(EQUALS);
					if (foundEquals == -1) {
						continue;
					}
					if (line.startsWith("timestamp")) {
						timestamp = Long.parseLong(line.substring(foundEquals + EQUALS.length()));
					} else {
						offsetMap.put(line.substring(0, foundEquals), Long.valueOf(line.substring(foundEquals + EQUALS.length())));
					}
				}
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e1);
			}
			try {
				offsetsIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e);
			}
		} catch (final FileNotFoundException fileNotFoundEx) {
			Reliant.instance.getLogger().logError(fileNotFoundEx);
		}
	}
}
