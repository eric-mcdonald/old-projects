package org.bitbucket.pklmao.cfg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public abstract class BaseConfiguration implements Configuration {
	private int version;
	private File cfgFile;

	public BaseConfiguration(int version, File cfgFile) {
		this.version = version;
		this.cfgFile = cfgFile;
	}

	@Override
	public File cfgFile() {
		return cfgFile;
	}
	@Override
	public int version() {
		return version;
	}
	protected String read(BufferedReader in) throws IOException {
		String line = in.readLine();
		if (line != null && line.startsWith(COMMENT_PREFIX)) {
			return "";
		}
		return line;
	}
	protected void assertVersion(BufferedReader in) throws NumberFormatException, IOException, OutdatedConfigException {
		String line;
		while ((line = read(in)) != null) {
			if (line.isEmpty()) {
				continue;
			}
			int inVersion = Integer.parseInt(line);
			if (version() != inVersion) {
				in.close();
				throw new OutdatedConfigException(inVersion, version());
			}
			break;
		}
	}

}
