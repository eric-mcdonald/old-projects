package org.bitbucket.reliant.memory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.RunAsAdminException;

public final class Y3t1y3tOffsetManager extends BaseOffsetManager {

	public Y3t1y3tOffsetManager() {
		super("OffsetManager.txt", "https://dl.dropboxusercontent.com/s/s8j1iu56058ukwn/Dumper_%5Bunknowncheats.me%5D_.exe", "Dumper_[unknowncheats.me]_.exe");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadOffsets() {
		// TODO Auto-generated method stub
		try {
			final BufferedReader offsetsIn = new BufferedReader(new FileReader(getOffsetsFile()));
			String line;
			int skipCount = 4;
			final String ARROW = " -> ", COLON = ":", SPACE = " ";
			try {
				while ((line = offsetsIn.readLine()) != null) {
					if (skipCount > 0) {
						--skipCount;
						continue;
					}
					int foundArrow = line.indexOf(ARROW);
					final int foundColon = line.indexOf(COLON), foundSpace = line.lastIndexOf(SPACE);
					if (foundArrow == -1 || foundColon == -1 || foundSpace == -1) {
						continue;
					}
					foundArrow += ARROW.length();
					offsetMap.put(line.substring(foundArrow, foundColon), Long.valueOf(line.substring(foundSpace + SPACE.length() + "0x".length()), 16));
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		}
	}
	
	@Override
	public long offset(String name) {
		if (!Reliant.instance.isLegacyToolchain()) {
			return super.offset(name);
		}
		return name.equals("m_dwRadarBasePointer") ? super.offset(name) - 0x30 : name.equals("m_dwLocalPlayer") ? super.offset(name) + 0x1C : super.offset(name);
	}

	@Override
	protected void onRunDumper() {
		// TODO Auto-generated method stub
		final String dumperProgram = getDumperFile().getName();
		if (!Reliant.instance.runAdmin(dumperProgram, OffsetManager.offsetDir.toString(), true, true)) {
			throw new RunAsAdminException(dumperProgram);
		}
	}

}
