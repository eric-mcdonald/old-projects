package org.bitbucket.reliant.memory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.bitbucket.reliant.Reliant;

public final class ZatOffsetManager extends InputOffsetManager {

	public ZatOffsetManager() {
		super("Zat's Offsets.txt", "https://dl.dropboxusercontent.com/s/lufg59agu6gux1s/Zats%20CSGO-Dumper.exe", "Zats CSGO-Dumper.exe");
		// TODO Auto-generated constructor stub
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
					final int foundEquals = line.indexOf(EQUALS);
					if (foundEquals == -1) {
						continue;
					}
					offsetMap.put(line.substring(" ".length(), foundEquals), Long.valueOf(line.substring(foundEquals + EQUALS.length() + "0x".length()), 16));
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
