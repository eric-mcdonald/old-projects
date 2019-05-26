package org.bitbucket.pklmao.inject;

import java.io.File;

import org.bitbucket.pklmao.ReportedException;

public class MappingsNotFoundException extends ReportedException {
	private static final long serialVersionUID = 1L;

	public MappingsNotFoundException(File mappingsFile) {
		super("inject.err.mappings_not_found", mappingsFile);
	}
}
