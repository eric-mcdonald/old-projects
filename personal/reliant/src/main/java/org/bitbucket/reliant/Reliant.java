package org.bitbucket.reliant;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Desktop;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.bitbucket.eric_generic.concurrent.DownloadFileThread;
import org.bitbucket.eric_generic.lang.PlatformUtils;
import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.log.Logger;
import org.bitbucket.eric_generic.registry.Registry;
import org.bitbucket.reliant.cfg.BasicConfiguration;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.Configurable;
import org.bitbucket.reliant.cfg.Configuration;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.cfg.Option;
import org.bitbucket.reliant.cmd.Command;
import org.bitbucket.reliant.cmd.impl.AutoconfigureCommand;
import org.bitbucket.reliant.cmd.impl.ClanTagCommand;
import org.bitbucket.reliant.cmd.impl.CmdSpamCommand;
import org.bitbucket.reliant.cmd.impl.ConfigCommand;
import org.bitbucket.reliant.cmd.impl.HelpCommand;
import org.bitbucket.reliant.cmd.impl.NameProtectCommand;
import org.bitbucket.reliant.cmd.impl.StateCommand;
import org.bitbucket.reliant.cmd.impl.ToggleCommand;
import org.bitbucket.reliant.cmd.impl.WaypointsCommand;
import org.bitbucket.reliant.handler.CrosshairHandler;
import org.bitbucket.reliant.handler.PlayerHandler;
import org.bitbucket.reliant.memory.HazeOffsetManager;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.memory.NetVarOffsetManager;
import org.bitbucket.reliant.memory.OffsetManager;
import org.bitbucket.reliant.memory.OpenProcessException;
import org.bitbucket.reliant.memory.Y3t1y3tOffsetManager;
import org.bitbucket.reliant.memory.ZatOffsetManager;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.BaseRoutine;
import org.bitbucket.reliant.routine.Displayable;
import org.bitbucket.reliant.routine.Routine;
import org.bitbucket.reliant.routine.impl.AimbotRoutine;
import org.bitbucket.reliant.routine.impl.AntiflashRoutine;
import org.bitbucket.reliant.routine.impl.AutocockRoutine;
import org.bitbucket.reliant.routine.impl.AutoconfigureRoutine;
import org.bitbucket.reliant.routine.impl.AutoduckRoutine;
import org.bitbucket.reliant.routine.impl.AutostopRoutine;
import org.bitbucket.reliant.routine.impl.AutozoomRoutine;
import org.bitbucket.reliant.routine.impl.BunnyhopRoutine;
import org.bitbucket.reliant.routine.impl.CalloutsRoutine;
import org.bitbucket.reliant.routine.impl.ClanTagRoutine;
import org.bitbucket.reliant.routine.impl.CmdSpamRoutine;
import org.bitbucket.reliant.routine.impl.CustomCrosshairRoutine;
import org.bitbucket.reliant.routine.impl.EspRoutine;
import org.bitbucket.reliant.routine.impl.FakeLagRoutine;
import org.bitbucket.reliant.routine.impl.GlowRoutine;
import org.bitbucket.reliant.routine.impl.HitmarkerRoutine;
import org.bitbucket.reliant.routine.impl.ImmunitySaverRoutine;
import org.bitbucket.reliant.routine.impl.InflaterRoutine;
import org.bitbucket.reliant.routine.impl.NameProtectRoutine;
import org.bitbucket.reliant.routine.impl.NoViewmodelRoutine;
import org.bitbucket.reliant.routine.impl.RadarRoutine;
import org.bitbucket.reliant.routine.impl.RcsRoutine;
import org.bitbucket.reliant.routine.impl.TracersRoutine;
import org.bitbucket.reliant.routine.impl.TriggerbotRoutine;
import org.bitbucket.reliant.routine.impl.WaypointsRoutine;
import org.bitbucket.reliant.test.TestClientCmdRoutine;
import org.bitbucket.reliant.test.TestCommand;
import org.bitbucket.reliant.test.TestFovRoutine;
import org.bitbucket.reliant.test.TestRoutineImplRoutine;
import org.bitbucket.reliant.ui.ConfigPanel;
import org.bitbucket.reliant.ui.ConsolePanel;
import org.bitbucket.reliant.ui.EnabledList;
import org.bitbucket.reliant.ui.Spectators;
import org.bitbucket.reliant.ui.TabUi;
import org.bitbucket.reliant.ui.TrayListener;
import org.bitbucket.reliant.ui.UiFrame;
import org.bitbucket.reliant.ui.Watermark;
import org.bitbucket.reliant.util.ClientCmdRate;
import org.bitbucket.reliant.util.I18n;

public final class Reliant {
	public static final String NAME = "Reliant";
	public static final int BUILD = 4;
	public static final Reliant instance = new Reliant();
	public static final File dataDir = new File("data");
	private File configsDir;
	//public static final int OVERWATCH_MAX_TIME = 150 + 1;
	private Registry<Configuration> configRegistry;
	private Registry<Configurable> customCfgRegistry;
	private Registry<Routine> routineRegistry;
	private Registry<Command> cmdRegistry;
	private Registry<OffsetManager> offsetsRegistry;
	private Registry<Long> customOffsetsRegistry;
	private Registry<PlayerHandler> playerHandlerRegistry;
	private Registry<CrosshairHandler> crosshairHandlerRegistry;
	private Registry<Displayable> displayRoutineRegistry;
	private Logger logger;
	private ConfigPanel configGui;
	private ConsolePanel consoleGui;
	private MemoryStream processStream;
	private Renderer renderer;
	private TrayListener trayListener;
	private long overlayedWindow;
	private String worldFont, guiFont;
	private boolean legacyToolchain, devEnv;
	private int cheatPriority, prevGamePriority;
	public final ClientCmdRate clientCmdRate = new ClientCmdRate(1, 1000L);
	private long keyboardHook, mouseHook;
	private I18n i18n;
	private boolean running; // Eric: This prevents a race condition between the routines resetting and updating.
	//private Random overwatchRand;

	private void addTrayIcon() {
		if (!SystemTray.isSupported()) {
			return;
		}
		final PopupMenu popup = new PopupMenu();
		final Menu routinesMenu = new Menu("Routines");
		trayListener = new TrayListener();
		for (final Routine routine : routineRegistry.objects()) {
			final CheckboxMenuItem routineItem = new CheckboxMenuItem(routine.name(), routine.isEnabled());
			trayListener.putTrayBox(routineItem, routine);
			routineItem.addItemListener(trayListener);
			routinesMenu.add(routineItem);
		}
		popup.add(routinesMenu);
		final MenuItem exitItem = new MenuItem("Exit");
		exitItem.setActionCommand(StringUtils.configName(exitItem.getLabel()));
		exitItem.addActionListener(trayListener);
		popup.add(exitItem);
		final TrayIcon trayIcon = new TrayIcon(UiFrame.logoImage, NAME, popup);
		trayIcon.setImageAutoSize(true);
		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			logger.logError(e);
		}
	}
	//public boolean antiOverwatch() {
	//return (Boolean) configRegistry.get("General").getOptionByName("Randomization").getValue();
	//}
	public String buildText() {
		String buildTxt = "b" + BUILD;
		if (devEnv) {
			buildTxt += "_dev";
		}
		return buildTxt;
	}
	private native long createD3dDevice(final long window, final boolean antiAliasing);
	private native long createOverlayedWindow(final String targetWindow, final String windowTitle);
	public int getCheatPriority() {
		return cheatPriority;
	}
	public Registry<Command> getCmdRegistry() {
		return cmdRegistry;
	}
	public ConfigPanel getConfigGui() {
		return configGui;
	}
	public Registry<Configuration> getConfigRegistry() {
		return configRegistry;
	}
	public File getConfigsDir() {
		return configsDir;
	}
	public ConsolePanel getConsoleGui() {
		return consoleGui;
	}
	public Registry<CrosshairHandler> getCrosshairHandlerRegistry() {
		return crosshairHandlerRegistry;
	}
	public Registry<Long> getCustomOffsetsRegistry() {
		return customOffsetsRegistry;
	}
	public Registry<Displayable> getDisplayRoutineRegistry() {
		return displayRoutineRegistry;
	}
	public String getGuiFont() {
		return guiFont;
	}
	public Logger getLogger() {
		return logger;
	}
	public Registry<OffsetManager> getOffsetsRegistry() {
		return offsetsRegistry;
	}
	long getOverlayedWindow() {
		return overlayedWindow;
	}
	//public Random getOverwatchRand() {
	//return overwatchRand;
	//}
	Registry<PlayerHandler> getPlayerHandlerRegistry() {
		return playerHandlerRegistry;
	}
	public MemoryStream getProcessStream() {
		return processStream;
	}
	public Renderer getRenderer() {
		return renderer;
	}
	public Registry<Routine> getRoutineRegistry() {
		return routineRegistry;
	}
	public TrayListener getTrayListener() {
		return trayListener;
	}
	public String getWorldFont() {
		return worldFont;
	}
	public I18n getI18n() {
		return i18n;
	}
	void init(final File configsDir, final boolean logFile, final boolean checkBuild, final boolean devEnv, final int cheatPriority, final int gamePriority, final boolean downloadDumpers, final boolean legacyToolchain, final boolean antiAliasing, final boolean dlOffsets) {
		this.legacyToolchain = legacyToolchain;
		this.devEnv = devEnv;
		this.configsDir = configsDir;
		this.cheatPriority = cheatPriority;
		logger = new Logger(NAME, logFile ? new File(dataDir, "logs") : null);
		i18n = new I18n("locales/Messages"); // TODO(Eric) Make the bundle name configurable?
		final File nativesDir = new File("natives");
		nativesDir.mkdir();
		final String architecture = PlatformUtils.architecture();
		final boolean is64Bit = PlatformUtils.is64Bit();
		final File nativeFile = new File(nativesDir, StringUtils.configName(NAME) + "_b" + BUILD + "_" + architecture + ".dll");
		final String BUILDS_ENTRY_SEPARATOR = "§";
		if (checkBuild) {
			logger.log("Checking for the latest build.", Logger.Type.INFO);
			BufferedReader buildsIn = null;
			try {
				buildsIn = new BufferedReader(new InputStreamReader(new URL("https://dl.dropboxusercontent.com/s/1a2ya3camw2sig4/builds.txt").openStream()));
				String line, prevLine = null;
				while ((line = buildsIn.readLine()) != null) {
					prevLine = line;
				}
				if (prevLine != null) {
					final String[] entries = prevLine.split(BUILDS_ENTRY_SEPARATOR);
					final int latestBuild = Integer.parseInt(entries[0]);
					if (latestBuild > BUILD) {
						buildsIn.close();
						logger.log("A new build was found: b" + latestBuild + ". Downloading it.", Logger.Type.INFO);
						final DownloadFileThread dlJarThread = new DownloadFileThread(new File(StringUtils.configName(NAME) + "_b" + latestBuild + ".jar"), new URL(entries[1])), dlNativeThread = new DownloadFileThread(nativeFile, new URL(is64Bit ? entries[3] : entries[2]));
						dlJarThread.start();
						dlNativeThread.start();
						while (dlJarThread.isAlive() || dlNativeThread.isAlive()) {
							try {
								Thread.sleep(1L);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								logger.logError(e);
							}
						}
						Desktop.getDesktop().open(new File(System.getProperty("user.dir")));
						System.exit(0);
					} else {
						logger.log("No new build was found.", Logger.Type.INFO);
					}
				}
				buildsIn.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				logger.logError(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.logError(e);
			} finally {
				if (buildsIn != null) {
					try {
						buildsIn.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.logError(e);
					}
				}
			}
		}
		if (!nativeFile.exists()) {
			logger.log("Downloading native library " + nativeFile, Logger.Type.INFO);
			BufferedReader buildsIn = null;
			try {
				buildsIn = new BufferedReader(new InputStreamReader(new URL("https://dl.dropboxusercontent.com/s/1a2ya3camw2sig4/builds.txt").openStream()));
				String line;
				while ((line = buildsIn.readLine()) != null) {
					final String[] entries = line.split(BUILDS_ENTRY_SEPARATOR);
					if (Integer.parseInt(entries[0]) == BUILD) {
						final Thread dlNativeThread = new DownloadFileThread(nativeFile, new URL(is64Bit ? entries[3] : entries[2]));
						dlNativeThread.start();
						while (dlNativeThread.isAlive()) {
							try {
								Thread.sleep(1L);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								logger.logError(e);
							}
						}
						break;
					}
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				logger.logFatal(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.logFatal(e);
			} finally {
				if (buildsIn != null) {
					try {
						buildsIn.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.logError(e);
					}
				}
			}
		}
		final String dllName;
		System.loadLibrary(dllName = StringUtils.configName(NAME) + "_b" + BUILD + "_" + architecture);
		//JniTest.printTest();
		logger.log("Waiting for " + Main.TARGET_PROCESS + " to start.", Logger.Type.INFO);
		while (!Main.processActive(Main.TARGET_PROCESS)) {
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.logError(e);
			}
		}
		logger.log("Initializing cheat.", Logger.Type.INFO);
		keyboardHook = installKeyboardHook(dllName);
		mouseHook = installMouseHook(dllName);
		if (cheatPriority != 0x00000020 && !setPriorityClass(cheatPriority)) {
			logger.log("Failed to enter priority class " + cheatPriority + " for the current process.", Logger.Type.ERROR);
		}
		OffsetManager.offsetDir.mkdir();
		offsetsRegistry = new Registry<OffsetManager>(null);
		offsetsRegistry.register("y3", new Y3t1y3tOffsetManager());
		offsetsRegistry.register("net_var", new NetVarOffsetManager());
		offsetsRegistry.register("haze", new HazeOffsetManager());
		offsetsRegistry.register("zat", new ZatOffsetManager());
		customOffsetsRegistry = new Registry<Long>(StringUtils.alphabetCmp);
		if (legacyToolchain) {
			if (downloadDumpers) {
				logger.log("Downloading offset dumpers.", Logger.Type.INFO);
				final Thread[] dlThreads = new Thread[offsetsRegistry.objects().size()];
				for (int i = 0; i < dlThreads.length; i++) {
					dlThreads[i] = offsetsRegistry.objects().get(i).downloadThread();
					dlThreads[i].start();
				}
				boolean allDone;
				do {
					allDone = true;
					for (final Thread dlThread : dlThreads) {
						if (dlThread.isAlive()) {
							allDone = false;
							break;
						}
					}
				} while (!allDone);
			}
			for (final OffsetManager offsets : offsetsRegistry.objects()) {
				offsets.runDumper();
				if (offsets.getOffsetsFile().exists()) {
					offsets.loadOffsets();
				}
			}
		} else {
			logger.log("Loading current toolchain offsets.", Logger.Type.INFO);
			final OffsetManager y3Offsets = offsetsRegistry.get("y3"), netVarOffsets = offsetsRegistry.get("net_var"), hazeOffsets = offsetsRegistry.get("haze"), zatOffsets = offsetsRegistry.get("zat");
			final File hazeFile = new File(OffsetManager.offsetDir, "csgo.toml");
			final String ENTRY_SEPARATOR = " = ";
			if (dlOffsets) {
				URL hazeUrl = null;
				try {
					hazeUrl = new URL("https://raw.githubusercontent.com/frk1/hazedumper/master/csgo.toml");
				} catch (MalformedURLException e2) {
					// TODO Auto-generated catch block
					logger.logError(e2);
				}
				boolean download = true;
				if (hazeFile.exists()) {
					BufferedReader hazeFileIn = null, hazeUrlIn = null;
					try {
						hazeFileIn = new BufferedReader(new FileReader(hazeFile));
						hazeUrlIn = new BufferedReader(new InputStreamReader(hazeUrl.openStream()));
						String fileLine, urlLine;
						int lineCount = 0;
						try {
							while ((fileLine = hazeFileIn.readLine()) != null && (urlLine = hazeUrlIn.readLine()) != null) {
								String[] fileEntry = fileLine.split(ENTRY_SEPARATOR), urlEntry = urlLine.split(ENTRY_SEPARATOR);
								// Eric: Check if the timestamps are equal
								if (fileEntry.length == 2 && urlEntry.length == 2 && Long.parseLong(fileEntry[1]) == Long.parseLong(urlEntry[1]) && lineCount == 1) {
									download = false;
								}
								if (lineCount == 1) {
									break;
								}
								++lineCount;
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.logError(e);
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						logger.logError(e1);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						logger.logError(e1);
					}
					try {
						hazeFileIn.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						logger.logError(e1);
					}
					try {
						hazeUrlIn.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						logger.logError(e1);
					}
				}
				if (download) {
					final Thread hazeThread = new DownloadFileThread(hazeFile, hazeUrl);
					hazeThread.start();
					while (hazeThread.isAlive()) {
						try {
							Thread.sleep(1L);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							logger.logError(e);
						}
					}
				}
			} else {
				hazeOffsets.runDumper();
			}
			final Map<String, Long> hazeOffsetMap = new HashMap<String, Long>();
			BufferedReader hazeFileIn = null;
			try {
				hazeFileIn = new BufferedReader(new FileReader(hazeFile));
				String fileLine;
				try {
					while ((fileLine = hazeFileIn.readLine()) != null) {
						String[] fileEntry = fileLine.split(ENTRY_SEPARATOR);
						if (fileEntry.length == 2) {
							hazeOffsetMap.put(fileEntry[0], Long.valueOf(fileEntry[1]));
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.logError(e);
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				logger.logError(e1);
			}
			try {
				hazeFileIn.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.logError(e1);
			}
			// Eric: Converts as many names as possible the lazy way.
			for (final Map.Entry<String, Long> hazeOffsetEntry : hazeOffsetMap.entrySet()) {
				String key = hazeOffsetEntry.getKey();
				final long value = hazeOffsetEntry.getValue();
				y3Offsets.putOffset(key, value);
				hazeOffsets.putOffset(key, value);
				netVarOffsets.putOffset(key, value);
				final String NET_VAR_PREFIX = "m_"; // Eric: Some of the offset dumpers use this prefix senselessly...
				if (!key.startsWith(NET_VAR_PREFIX)) {
					key = NET_VAR_PREFIX + key;
					y3Offsets.putOffset(key, value);
				}
				y3Offsets.putOffset(key + "[0]", value);
			}
			y3Offsets.putOffset("m_dwRadarBasePointer", 0x54); // TODO(Eric) Hardcoded offset correct as of 2017-02-12
			y3Offsets.putOffset("m_bDormant", 0xE9); // TODO(Eric) Hardcoded offset correct as of 2017-02-12
			y3Offsets.putOffset("m_dwInGame", hazeOffsetMap.get("dwClientState_State"));
			y3Offsets.putOffset("m_dwViewAngles", hazeOffsetMap.get("dwClientState_ViewAngles"));
			y3Offsets.putOffset("m_vecPunch", hazeOffsetMap.get("m_aimPunchAngle"));
			y3Offsets.putOffset("m_dwGlowObject", hazeOffsetMap.get("dwGlowObjectManager"));
			y3Offsets.putOffset("m_dwMapDirectory", hazeOffsetMap.get("dwClientState_MapDirectory"));
			y3Offsets.putOffset("m_iCrossHairID", hazeOffsetMap.get("m_iCrosshairId"));
			y3Offsets.putOffset("m_angEyeAngles", hazeOffsetMap.get("m_ArmorValue") + 0x4);
			y3Offsets.putOffset("CSPlayerResource", hazeOffsetMap.get("dwPlayerResource"));
			y3Offsets.putOffset("m_zoomLevel", hazeOffsetMap.get("m_fAccuracyPenalty") + 0x90);
			y3Offsets.putOffset("m_szClan", hazeOffsetMap.get("m_iCompetitiveWins") + 0x25D8 + 0x44); // TODO(Eric) Hardcoded offset correct as of 2017-02-12
			netVarOffsets.putOffset("m_flModelScale", hazeOffsetMap.get("m_nForceBone") + 0xBC);
			netVarOffsets.putOffset("m_bDrawViewmodel", hazeOffsetMap.get("m_aimPunchAngleVel") + 0x15);
			netVarOffsets.putOffset("m_vecMins", hazeOffsetMap.get("m_Collision") + 8);
			netVarOffsets.putOffset("m_vecMaxs", hazeOffsetMap.get("m_Collision") + 8 + 0xC);
			zatOffsets.putOffset("ScoreBoardBase/GameResources", hazeOffsetMap.get("dwPlayerResource"));
			customOffsetsRegistry.register("SetClanTag", hazeOffsetMap.get("dwSetClanTag"));
			hazeOffsets.putOffset("dwForceAttack2", hazeOffsets.offset("dwForceAttack2") - 0x30); // TODO(Eric) Often broken offset correct as of 2017-02-12
		}
		try {
			processStream = new MemoryStream(Main.TARGET_PROCESS);
		} catch (OpenProcessException e) {
			// TODO Auto-generated catch block
			logger.logFatal(e);
		}
		prevGamePriority = processStream.getPriorityClass();
		if (gamePriority != 0x00000020 && !processStream.setPriorityClass(gamePriority)) {
			logger.log("Failed to enter priority class " + gamePriority + " for " + Main.TARGET_PROCESS, Logger.Type.ERROR);
		}
		logger.log("Loading custom offsets.", Logger.Type.INFO);
		String module = "client.dll";
		long moduleAddr = processStream.moduleAddress(module);
		//customOffsetsRegistry.register("m_dwGlobalVars", processStream.readInt(processStream.find("A1????????5F8B4010", module) + 0x1) - moduleAddr);
		customOffsetsRegistry.register("m_vecAbsOrigin", offsetsRegistry.get("y3").offset("m_vecOrigin") - 0x94);
		//customOffsetsRegistry.register("m_szName", (long) 0x9D8);
		customOffsetsRegistry.register("m_dwForceDuck", offsetsRegistry.get("y3").offset("m_dwForceJump") + 0x24);
		customOffsetsRegistry.register("m_iPlayerC4", offsetsRegistry.get("haze").offset("m_iCompetitiveRanking") - 0x428); // TODO(Eric) Hardcoded offset correct as of 2017-01-12
		//customOffsetsRegistry.register("m_bInReload", offsetsRegistry.get("net_var").offset("m_bReloadVisuallyComplete") - 0x97);
		//customOffsetsRegistry.register("FindEntityInFrontOfLocalPlayer", processStream.find("558BEC81EC84000000568B35????????85F60F84????????6A006A008D45F4", module) - moduleAddr);
		//customOffsetsRegistry.register("v_angle", offsetsRegistry.get("net_var").offset("pl") + 0x8);
		customOffsetsRegistry.register("engine", processStream.readInt(processStream.find("8B0D????????68????????8B01FF501C5EC3", module) + 2) - moduleAddr);
		customOffsetsRegistry.register("m_iTotalHits", offsetsRegistry.get("haze").offset("m_iShotsFired") + 0x18);
		module = "engine.dll";
		moduleAddr = processStream.moduleAddress(module);
		//customOffsetsRegistry.register("bSendPackets", processStream.find("B3018B018B", module) + 1 - moduleAddr);
		if (legacyToolchain) {
			customOffsetsRegistry.register("SetClanTag", processStream.find("5356578BDA8BF9FF15", module) - moduleAddr);
		}
		final LookAndFeel prevLookAndFeel = UIManager.getLookAndFeel();
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			logger.logError(e1);
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			logger.logError(e1);
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			logger.logError(e1);
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			logger.logError(e1);
		}
		worldFont = new JLabel().getFont().getFontName();
		try {
			UIManager.setLookAndFeel(prevLookAndFeel);
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			logger.logError(e);
		}
		guiFont = new JLabel().getFont().getFontName();
		try {
			overlayedWindow = createOverlayedWindow(Main.getTargetWindow(), NAME + " " + buildText() + ": Direct3D Overlay");
			if (overlayedWindow == MemoryStream.NULL) {
				throw new CreateD3dOverlayException("Window cannot be null.");
			}
			renderer = new Renderer(createD3dDevice(overlayedWindow, antiAliasing));
		} catch (final CreateD3dOverlayException createD3dOverlayEx) {
			logger.logFatal(createD3dOverlayEx);
		}
		configRegistry = new Registry<Configuration>(StringUtils.alphabetCmp);
		customCfgRegistry = new Registry<Configurable>(StringUtils.alphabetCmp);
		playerHandlerRegistry = new Registry<PlayerHandler>(StringUtils.alphabetCmp);
		crosshairHandlerRegistry = new Registry<CrosshairHandler>(StringUtils.alphabetCmp);
		displayRoutineRegistry = new Registry<Displayable>(StringUtils.alphabetCmp);
		routineRegistry = new Registry<Routine>(StringUtils.alphabetCmp);
		cmdRegistry = new Registry<Command>(StringUtils.alphabetCmp);
		clientCmdRate.start();
		registerConfig(new BasicConfiguration("General", new Option<?>[] {new KeyOption("Attack Key", "Specifies the attack key.", 0x1), new KeyOption("Reload Key", "Specifies the reload key.", 0x52), new KeyOption("Attack 2 Key", "Specifies the attack2 key.", 0x2), new KeyOption("Duck Key", "Specifies the duck key.", 0xA2), new KeyOption("Forward Key", "Specifies the move forward key.", 0x57), new KeyOption("Backward Key", "Specifies the move backward key.", 0x53), new KeyOption("Right Key", "Specifies the move right key.", 0x44), new KeyOption("Left Key", "Specifies the move left key.", 0x41), new IntOption("Update Interval", "Specifies the interval the routines will be updated by.", new ClampedNumber<Integer>(0, 0, 1000), 100), new BoolOption("Check Mouse", "Specifies whether or not to only update the routines if the cursor is not shown.", false), /*new BoolOption("Randomization", "Experimental randomization to prevent patterns that might put you in the Overwatch queue.", false), */}));
		//overwatchRand = new Random();
		registerRoutine(new AutoconfigureRoutine());
		registerRoutine(new AimbotRoutine());
		registerRoutine(new AntiflashRoutine());
		registerRoutine(new AutocockRoutine());
		registerRoutine(new AutoduckRoutine());
		registerRoutine(new AutostopRoutine());
		registerRoutine(new AutozoomRoutine());
		registerRoutine(new BunnyhopRoutine());
		registerRoutine(new CalloutsRoutine());
		registerRoutine(new ClanTagRoutine());
		registerRoutine(new CmdSpamRoutine());
		registerRoutine(new CustomCrosshairRoutine());
		registerRoutine(new EspRoutine());
		registerRoutine(new FakeLagRoutine());
		registerRoutine(new GlowRoutine());
		registerRoutine(new HitmarkerRoutine());
		registerRoutine(new ImmunitySaverRoutine());
		registerRoutine(new InflaterRoutine());
		registerRoutine(new NameProtectRoutine());
		registerRoutine(new NoViewmodelRoutine());
		registerRoutine(new RadarRoutine());
		registerRoutine(new RcsRoutine());
		registerRoutine(new TracersRoutine());
		registerRoutine(new TriggerbotRoutine());
		registerRoutine(new WaypointsRoutine());
		if (devEnv) {
			registerRoutine(new TestClientCmdRoutine());
			registerRoutine(new TestFovRoutine());
			registerRoutine(new TestRoutineImplRoutine());
		}
		registerRoutine(new EnabledList());
		registerRoutine(new Spectators());
		registerRoutine(new TabUi());
		registerRoutine(new Watermark());
		registerCmd(new AutoconfigureCommand());
		registerCmd(new ClanTagCommand());
		registerCmd(new CmdSpamCommand());
		registerCmd(new ConfigCommand());
		registerCmd(new HelpCommand());
		registerCmd(new NameProtectCommand());
		registerCmd(new StateCommand());
		registerCmd(new ToggleCommand());
		registerCmd(new WaypointsCommand());
		if (devEnv) {
			registerCmd(new TestCommand());
		}
		final NameProtectRoutine nameProtectRoutine = (NameProtectRoutine) routineRegistry.get("Name Protect");
		customCfgRegistry.register(StringUtils.configName(nameProtectRoutine.name()) + "_names", nameProtectRoutine.namesCfg);
		final ClanTagRoutine clanTagRoutine = (ClanTagRoutine) routineRegistry.get("Clan Tag");
		customCfgRegistry.register(StringUtils.configName(clanTagRoutine.name()) + "_clans", clanTagRoutine.clanTagsCfg);
		final CmdSpamRoutine cmdSpamRoutine = (CmdSpamRoutine) routineRegistry.get("Command Spammer");
		customCfgRegistry.register(StringUtils.configName(cmdSpamRoutine.name()) + "_commands", cmdSpamRoutine.commandsCfg);
		final AutoconfigureRoutine autoCfgRoutine = (AutoconfigureRoutine) routineRegistry.get("Auto-configure");
		customCfgRegistry.register(StringUtils.configName(autoCfgRoutine.name()) + "_names", autoCfgRoutine.namesCfg);
		addTrayIcon();
		loadConfigs(configsDir, false);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new UiFrame("Configuration", configGui = new ConfigPanel()).setVisible(true);
				new UiFrame("Console", consoleGui = new ConsolePanel()).setVisible(true);
			}

		});
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				logger.log("Shutting down.", Logger.Type.INFO);
				running = false;
				// Eric: Let the routines reset themselves before shutdown, just for in case the target process is still running.
				final AutoconfigureRoutine autoCfgRoutine = (AutoconfigureRoutine) Reliant.instance.getRoutineRegistry().get("Auto-configure");
				autoCfgRoutine.reset(true);
				for (final Routine routine : Reliant.instance.getRoutineRegistry().objects()) {
					if (routine.equals(autoCfgRoutine)) {
						continue;
					}
					routine.reset(true);
				}
				saveConfigs(false);
				//processStream.resume(); // Eric: Ensures the process gets resumed.
				//processStream.enableMouse();
				clientCmdRate.stop();
				processStream.resetProtect();
				processStream.setPriorityClass(prevGamePriority);
				processStream.close();
				if (!isMouseEnabled()) {
					setMouseEnabled();
				}
				uninstallHook(mouseHook);
				uninstallHook(keyboardHook);
			}

		}));
		running = true;
		logger.log("Cheat initialized.", Logger.Type.INFO);
	}
	private native long installKeyboardHook(final String dllName);
	// TODO(Eric) Refactor these
	private native long installMouseHook(final String dllName);
	public boolean isDevEnv() {
		return devEnv;
	}
	public native boolean isKeyboardEnabled();
	public boolean isLegacyToolchain() {
		return legacyToolchain;
	}
	public native boolean isMouseEnabled();
	public boolean isRunning() {
		return running;
	}
	@SuppressWarnings("unlikely-arg-type")
	public void loadConfigs(final File configsDir, final boolean excludeAutoCfg) {
		final boolean dirsEqual = configsDir.equals(this.configsDir);
		if (!dirsEqual) {
			this.configsDir = configsDir;
			configsDir.mkdirs();
		}
		final List<Configuration> configs = configRegistry.objects();
		for (final Configuration config : configs) {
			if (excludeAutoCfg && config.equals(routineRegistry.get("Auto-configure"))) {
				continue;
			}
			if (!dirsEqual) {
				config.setConfigFile(new File(configsDir, config.getConfigFile().getName()));
				config.loadDefaults(); // Eric: Loads the default values for incase the directory does not exist.
			}
			config.load();
		}
		for (final Configurable customCfg : customCfgRegistry.objects()) {
			if (configs.contains(customCfg) || excludeAutoCfg && customCfg.equals(((AutoconfigureRoutine) routineRegistry.get("Auto-configure")).namesCfg)) {
				continue;
			}
			if (!dirsEqual) {
				customCfg.setConfigFile(new File(configsDir, customCfg.getConfigFile().getName()));
			}
			customCfg.load();
		}
		for (final Routine routine : routineRegistry.objects()) {
			if (routine instanceof BaseRoutine && !(excludeAutoCfg && routine.equals(routineRegistry.get("Auto-configure")))) {
				((BaseRoutine) routine).startupToggle();
			}
		}
	}
	private void registerCmd(final Command cmd) {
		for (final String name : cmd.names()) {
			cmdRegistry.register(name, cmd);
			if (cmd instanceof Configuration) {
				registerConfig((Configuration) cmd);
			}
			if (cmd instanceof Configurable) {
				customCfgRegistry.register(name, (Configurable) cmd);
			}
			if (cmd instanceof PlayerHandler) {
				playerHandlerRegistry.register(name, (PlayerHandler) cmd);
			}
			if (cmd instanceof CrosshairHandler) {
				crosshairHandlerRegistry.register(name, (CrosshairHandler) cmd);
			}
		}
	}
	private void registerConfig(final Configuration config) {
		configRegistry.register(config.name(), config);
	}
	private void registerRoutine(final Routine routine) {
		if (routine instanceof Configuration) {
			registerConfig((Configuration) routine);
		}
		if (routine instanceof Configurable) {
			customCfgRegistry.register(routine.name(), (Configurable) routine);
		}
		if (routine instanceof PlayerHandler) {
			playerHandlerRegistry.register(routine.name(), (PlayerHandler) routine);
		}
		if (routine instanceof CrosshairHandler) {
			crosshairHandlerRegistry.register(routine.name(), (CrosshairHandler) routine);
		}
		if (routine instanceof Displayable) {
			displayRoutineRegistry.register(routine.name(), (Displayable) routine);
		}
		routineRegistry.register(routine.name(), routine);
	}
	public native boolean runAdmin(final String file, final String directory, final boolean hide, final boolean wait);
	@SuppressWarnings("unlikely-arg-type")
	public void saveConfigs(final boolean excludeAutoCfg) {
		final List<Configuration> configs = configRegistry.objects();
		for (final Configuration config : configs) {
			if (excludeAutoCfg && config.equals(routineRegistry.get("Auto-configure"))) {
				continue;
			}
			config.save();
		}
		for (final Configurable customCfg : customCfgRegistry.objects()) {
			if (configs.contains(customCfg) || excludeAutoCfg && customCfg.equals(((AutoconfigureRoutine) routineRegistry.get("Auto-configure")).namesCfg)) {
				continue;
			}
			customCfg.save();
		}
	}
	public native void setKeyboardEnabled();
	public native void setMouseEnabled();
	public native boolean setPriorityClass(final int priorityClass);
	private native long uninstallHook(final long hookProc);
	public native int getMouseX();
	public native int getMouseY();
	public void wait(final String program) {
		while (!Main.processActive(program)) {
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.logError(e);
			}
		}
		while (Main.processActive(program)) {
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.logError(e);
			}
		}
	}
}
