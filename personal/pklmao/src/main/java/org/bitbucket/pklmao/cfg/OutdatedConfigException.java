package org.bitbucket.pklmao.cfg;

import org.bitbucket.pklmao.ReportedException;

public class OutdatedConfigException extends ReportedException {
	private static final long serialVersionUID = 1L;
	
	public OutdatedConfigException(int version1, int version2) {
		super("cfg.err.outdated", version1, version2);
	}
}
