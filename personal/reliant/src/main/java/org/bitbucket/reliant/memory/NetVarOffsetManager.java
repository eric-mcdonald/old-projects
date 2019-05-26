package org.bitbucket.reliant.memory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.bitbucket.reliant.Reliant;

public final class NetVarOffsetManager extends BaseOffsetManager {

	public NetVarOffsetManager() {
		super("NetVarManager.txt", "https://dl.dropboxusercontent.com/s/s8j1iu56058ukwn/Dumper_%5Bunknowncheats.me%5D_.exe", "Dumper_[unknowncheats.me]_.exe");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadOffsets() {
		// TODO Auto-generated method stub
		try {
			final BufferedReader offsetsIn = new BufferedReader(new FileReader(getOffsetsFile()));
			String line;
			int skipCount = 4;
			final String LEVEL = "|__", ARROW = " -> ";
			try {
				while ((line = offsetsIn.readLine()) != null) {
					if (skipCount > 0) {
						--skipCount;
						continue;
					}
					int foundLvl = line.indexOf(LEVEL), foundArrow = line.indexOf(ARROW);
					if (foundLvl == -1 || foundArrow == -1) {
						continue;
					}
					foundLvl += LEVEL.length();
					int lastNameIdx = -1;
					for (int i = foundLvl + 2; i < line.length(); i++) {
						final char character = line.charAt(i);
						if (character == '_' || i == foundArrow) {
							lastNameIdx = i;
							break;
						}
					}
					if (lastNameIdx == -1) {
						continue;
					}
					final String offsetName = line.substring(foundLvl, lastNameIdx);
					if (!offsetMap.containsKey(offsetName)) {
						foundArrow += ARROW.length();
						offsetMap.put(offsetName, Long.valueOf(line.substring(foundArrow + "0x".length(), foundArrow + 6), 16));
					}
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e);
			}
			try {
				offsetsIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		}
	}

	@Override
	protected void onRunDumper() {
		// TODO Auto-generated method stub
		// Eric: Empty implementation to prevent the same dumper from being executed twice
	}

}
