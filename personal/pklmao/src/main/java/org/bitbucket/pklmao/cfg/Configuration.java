package org.bitbucket.pklmao.cfg;

import java.io.File;
import java.io.IOException;

public interface Configuration {
	String COMMENT_PREFIX = "#";
	String OPT_FIELD_SEP = ":";
	
	int version();
	void load() throws IOException;
	void save() throws IOException;
	File cfgFile();
}
