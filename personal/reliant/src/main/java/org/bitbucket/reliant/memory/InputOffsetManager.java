package org.bitbucket.reliant.memory;

import java.io.IOException;

import org.bitbucket.reliant.Reliant;

public abstract class InputOffsetManager extends BaseOffsetManager {
	public InputOffsetManager(final String offsetsFilename, final String dumperUrl, final String dumperFilename) {
		super(offsetsFilename, dumperUrl, dumperFilename);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected final void onRunDumper() {
		// TODO Auto-generated method stub
		final String program = getDumperFile().getName();
		try {
			Runtime.getRuntime().exec("cmd /c start cmd /c \"" + program + "\"", null, OffsetManager.offsetDir).waitFor();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		}
		// Eric: Ensures that the dumper actually finishes
		Reliant.instance.wait(program);
	}
}
