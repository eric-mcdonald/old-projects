package org.bitbucket.reliant.cfg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Reliant;

public class BasicConfiguration extends BaseConfigurable implements Configuration {
	protected static final String ENTRY_SEPARATOR = ":";
	private final String name;
	private final List<Option<?>> options = new ArrayList<Option<?>>();

	public BasicConfiguration(final String name, final Option<?>[] options) {
		super(new File(Reliant.instance.getConfigsDir(), StringUtils.configName(name) + ".cfg"));
		this.name = name;
		if (options != null) {
			for (final Option<?> option : options) {
				this.options.add(option);
			}
		}
		Collections.sort(this.options, new Comparator<Option<?>>() {

			@Override
			public int compare(Option<?> o1, Option<?> o2) {
				// TODO Auto-generated method stub
				return String.CASE_INSENSITIVE_ORDER.compare(o1.name(), o2.name());
			}
		});
	}

	public final boolean getBoolean(final String name) {
		return (Boolean) getOptionByName(name).getValue();
	}

	public final File getDirectory(final String name) {
		return new File((String) getOptionByName(name).getValue());
	}

	@SuppressWarnings("unchecked")
	public final float getFloat(final String name) {
		return ((ClampedNumber<Float>) getOptionByName(name).getValue()).getValue();
	}

	@SuppressWarnings("unchecked")
	public final int getInt(final String name) {
		return ((ClampedNumber<Integer>) getOptionByName(name).getValue()).getValue();
	}

	public final KeyOption getKeyOption(final String name) {
		return (KeyOption) getOptionByName(name);
	}

	@Override
	public final Option<?> getOptionByName(final String name) {
		for (final Option<?> option : options) {
			if (StringUtils.configName(option.name()).equals(StringUtils.configName(name))) {
				return option;
			}
		}
		return null;
	}

	@Override
	public final List<Option<?>> getOptions() {
		// TODO Auto-generated method stub
		return options;
	}

	public final SoundOption getSoundOpt(final String name) {
		return (SoundOption) getOptionByName(name);
	}

	public final long getTexture(final String name) {
		return (Long) getOptionByName(name).getValue();
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		if (!getConfigFile().exists() || options.isEmpty()) {
			return;
		}
		try {
			final BufferedReader configReader = new BufferedReader(new FileReader(getConfigFile()));
			try {
				String entry;
				while ((entry = configReader.readLine()) != null) {
					if (StringUtils.comment(entry)) {
						continue;
					}
					try {
						final Option<?> entryOption = getOptionByName(StringUtils.configName(entry.substring(0, entry.indexOf(ENTRY_SEPARATOR))));
						entryOption.setValue(entryOption.parseValue(entry.substring(entry.indexOf(ENTRY_SEPARATOR) + ENTRY_SEPARATOR.length())));
					} catch (final NullPointerException nullPtrEx) {
						Reliant.instance.getLogger().logError(nullPtrEx);
					}
				}
			} catch (final IOException ioEx) {
				Reliant.instance.getLogger().logError(ioEx);
			} finally {
				try {
					configReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Reliant.instance.getLogger().logError(e);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		}
	}

	@Override
	public void loadDefaults() {
		// TODO Auto-generated method stub
		for (final Option<?> option : options) {
			option.setValue(option.parseValue(option.getCfgDefault().toString()));
		}
	}

	@Override
	public final String name() {
		return name;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		if (options.isEmpty()) {
			return;
		}
		getConfigFile().getParentFile().mkdirs();
		getConfigFile().delete();
		try {
			final PrintWriter configWriter = new PrintWriter(new BufferedWriter(new FileWriter(getConfigFile())));
			configWriter.println(StringUtils.COMMENT_PREFIX + " This is the configuration file of " + name + ". It is not recommended that you edit this file directly.");
			configWriter.println(StringUtils.COMMENT_PREFIX + " Format of the entries is \"name" + ENTRY_SEPARATOR + "value\"");
			for (final Option<?> option : options) {
				configWriter.println(StringUtils.configName(option.name()) + ENTRY_SEPARATOR + option.getCfgValue());
			}
			configWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		}
	}
}
