package org.bitbucket.pklmao.inject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bitbucket.pklmao.PkLmao;
import org.bitbucket.pklmao.cfg.BaseConfiguration;
import org.bitbucket.pklmao.cfg.OutdatedConfigException;

public class NameMap extends BaseConfiguration {
	private final Map<String, String> names = new HashMap<String, String>();
	
	public NameMap(int version, String filename) {
		super(version, new File(PkLmao.getInstance().getDataDir(), filename));
	}

	@Override
	public void load() throws IOException, MappingsNotFoundException, OutdatedConfigException {
		if (!cfgFile().exists()) {
			throw new MappingsNotFoundException(cfgFile());
		}
		BufferedReader in = new BufferedReader(new FileReader(cfgFile()));
		assertVersion(in);
		String line;
		while ((line = read(in)) != null) {
			if (line.isEmpty()) {
				continue;
			}
			String[] mappingFields = line.split(OPT_FIELD_SEP);
			names.put(mappingFields[0], mappingFields[1]);
		}
		in.close();
	}
	public String getObfName(String refactoredName) {
		return names.containsKey(refactoredName) ? names.get(refactoredName) : refactoredName;
	}
	@Override
	public void save() throws IOException {
		// Empty stub implementation
	}
}
