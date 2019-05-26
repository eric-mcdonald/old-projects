package org.bitbucket.pklmao;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import org.bitbucket.pklmao.cfg.BasicConfiguration;
import org.bitbucket.pklmao.cfg.BoolOption;
import org.bitbucket.pklmao.cfg.Configuration;
import org.bitbucket.pklmao.cfg.OutdatedConfigException;
import org.bitbucket.pklmao.cfg.StringOption;
import org.bitbucket.pklmao.cmd.Command;
import org.bitbucket.pklmao.inject.Injector;
import org.bitbucket.pklmao.inject.MappingsNotFoundException;
import org.bitbucket.pklmao.inject.NameMap;
import org.bitbucket.pklmao.inject.ReflectCache;
import org.bitbucket.pklmao.routine.Routine;
import org.bitbucket.pklmao.test.TestCommand;
import org.bitbucket.pklmao.test.TestRoutine;
import org.bitbucket.pklmao.ui.Gui;
import org.bitbucket.pklmao.util.I18n;

public class PkLmao {
	private static final Logger LOGGER = Logger.getLogger(PkLmao.class.getName());
	// Program metadata
	public static final String NAME = "PkLmao";
	public static final float VERSION = 1.0F;

	private static PkLmao instance;
	private File dataDir = new File("pklmao"), cfgDir = new File(dataDir, "cfg");
	private I18n i18n;
	private BasicConfiguration mainCfg;
	private NameMap mappings;
	private Injector injector;
	private Gui gui;
	private final Set<Configuration> featureCfgs = new HashSet<Configuration>();

	public static PkLmao getInstance() {
		if (instance == null) {
			instance = new PkLmao();
		}
		return instance;
	}
	public BasicConfiguration getMainCfg() {
		return mainCfg;
	}
	public NameMap getMappings() {
		return mappings;
	}
	public File getDataDir() {
		return dataDir;
	}
	public File getCfgDir() {
		return cfgDir;
	}
	public I18n getI18n() {
		return i18n;
	}
	public Injector getInjector() {
		return injector;
	}
	private void initConfigs() {
		for (Configuration config : featureCfgs) {
			try {
				config.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void register(Command cmd) {
		getGui().getConsole().register(cmd.name(), cmd);
		for (String alias : cmd.aliases()) {
			getGui().getConsole().register(alias, cmd);
		}
	}
	private void register(Routine routine) {
		getGui().getToggles().register(routine.id(), routine);
		if (routine.config() != null) {
			featureCfgs.add(routine.config());
		}
	}
	private void initRoutines() {
		register(new TestRoutine());
	}
	private void initCmds() {
		register(new TestCommand());
	}
	public Gui getGui() {
		return gui;
	}
	private void preInit() {
		dataDir.mkdirs();
		cfgDir.mkdirs();
		featureCfgs.clear();
		i18n = new I18n("en", "US");
		mainCfg = new BasicConfiguration(0, "main.cfg");
		mainCfg.addOption(new BoolOption("randomize_mac_address", "cfg.impl.main.opt.name.rand_mac_addr", "cfg.impl.main.opt.desc.rand_mac_addr", true, true));
		mainCfg.addOption(new BoolOption("save_on_exit", "cfg.impl.main.opt.name.save_on_exit", "cfg.impl.main.opt.desc.save_on_exit", true, false));
		mainCfg.addOption(new StringOption("locale_lang", "cfg.impl.main.opt.name.locale_lang", "cfg.impl.main.opt.desc.locale_lang", "en", true));
		mainCfg.addOption(new StringOption("locale_country", "cfg.impl.main.opt.name.locale_country", "cfg.impl.main.opt.desc.locale_country", "US", true));
		try {
			mainCfg.load();
		} catch (OutdatedConfigException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getI18n().setCurrentLoc(new Locale((String) mainCfg.getOptById("locale_lang").getValue(), (String) mainCfg.getOptById("locale_country").getValue()));
		mappings = new NameMap(0, "mappings.cfg");
		try {
			mappings.load();
		} catch (MappingsNotFoundException e1) {
			e1.createCrashReport();
			System.exit(-1);
		} catch (OutdatedConfigException e1) {
			e1.printStackTrace();
			System.exit(-2);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-3);
		}
		try {
			ReflectCache.initLib();
		} catch (Throwable error) {
			error.printStackTrace();
			System.exit(2);
		}
		injector = new Injector(this);
		injector.injectClasses(ClassLoader.getSystemClassLoader());
		try {
			ReflectCache.initGame();
		} catch (Throwable error) {
			error.printStackTrace();
			System.exit(1);
		}
	}
	public void init() {
		LOGGER.info("Started initializing " + NAME + " v" + VERSION);
		preInit();
		gui = new Gui(this);
		initRoutines();
		initCmds();
		initConfigs();
		// TODO Register event listeners here:
		
		LOGGER.info("Adding shutdown hook");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				if ((Boolean) mainCfg.getOptById("save_on_exit").getValue()) {
					try {
						mainCfg.save();
					} catch (IOException e) {
						e.printStackTrace();
					}
					for (Configuration config : featureCfgs) {
						try {
							config.save();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}));
		gui.show();
		LOGGER.info("Finished initialization");
	}
}
