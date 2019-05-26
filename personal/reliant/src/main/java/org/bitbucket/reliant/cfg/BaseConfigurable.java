package org.bitbucket.reliant.cfg;

import java.io.File;

public abstract class BaseConfigurable implements Configurable {
	private File configFile;
	
	public BaseConfigurable(File configFile) {
		setConfigFile(configFile);
	}

	@Override
	public final File getConfigFile() {
		// TODO Auto-generated method stub
		return configFile;
	}

	@Override
	public final void setConfigFile(File configFile) {
		// TODO Auto-generated method stub
		this.configFile = configFile;
	}
}
