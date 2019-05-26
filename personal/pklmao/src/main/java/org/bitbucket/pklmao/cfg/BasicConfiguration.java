package org.bitbucket.pklmao.cfg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bitbucket.pklmao.PkLmao;

public class BasicConfiguration extends BaseConfiguration {
	private static final Logger LOGGER = Logger.getLogger(BasicConfiguration.class.getName());
	private Set<Option<?>> options = new HashSet<Option<?>>();

	public BasicConfiguration(int version, String file) {
		super(version, new File(PkLmao.getInstance().getCfgDir(), file));
	}

	@Override
	public void load() throws IOException, OutdatedConfigException {
		if (!cfgFile().exists()) {
			return;
		}
		BufferedReader in = new BufferedReader(new FileReader(cfgFile()));
		assertVersion(in);
		String line;
		while ((line = read(in)) != null) {
			if (line.isEmpty()) {
				continue;
			}
			String[] optFields = line.split(OPT_FIELD_SEP);
			Option<?> option = getOptById(optFields[0]);
			if (option != null) {
				option.setValue(line.substring(optFields[0].length() + OPT_FIELD_SEP.length()), true);
				addOption(option);
			} else {
				LOGGER.log(Level.WARNING, "Skipping bad option {0} in config {1}", new Object[] {optFields[0], cfgFile()});
			}
		}
		in.close();
	}
	@Override
	public void save() throws IOException {
		cfgFile().delete();
		PrintWriter out = new PrintWriter(new FileWriter(cfgFile()));
		out.println(COMMENT_PREFIX + " It is not recommended to edit configuration files directly!");
		out.println(COMMENT_PREFIX);
		out.println(COMMENT_PREFIX + " The format version of this configuration:");
		out.println(version());
		for (Option<?> option : options()) {
			out.println(option.id() + OPT_FIELD_SEP + option.getValue());
		}
		out.close();
	}
	public Set<Option<?>> options() {
		return options;
	}
	public void addOption(Option<?> option) {
		options.add(option);
	}
	public Option<?> getOptById(String id) {
		for (Option<?> option : options()) {
			if (option.id().equals(id)) {
				return option;
			}
		}
		return null;
	}

}
