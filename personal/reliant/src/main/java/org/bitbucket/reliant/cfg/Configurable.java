package org.bitbucket.reliant.cfg;

import java.io.File;

public interface Configurable {
	File getConfigFile();
	void load();
	void save();
	void setConfigFile(File configFile);
}
