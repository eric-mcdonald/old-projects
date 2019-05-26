package org.bitbucket.lanius;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.cfg.ConfigHandler;
import org.bitbucket.lanius.cfg.Configurable;
import org.bitbucket.lanius.cmd.CommandHandler;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.cmd.impl.AllOffCommand;
import org.bitbucket.lanius.cmd.impl.AutoRespondCommand;
import org.bitbucket.lanius.cmd.impl.BreadcrumbsCommand;
import org.bitbucket.lanius.cmd.impl.CheckCommand;
import org.bitbucket.lanius.cmd.impl.ConfigCommand;
import org.bitbucket.lanius.cmd.impl.CreativeDropCommand;
import org.bitbucket.lanius.cmd.impl.DamageCommand;
import org.bitbucket.lanius.cmd.impl.DescCommand;
import org.bitbucket.lanius.cmd.impl.GiveCommand;
import org.bitbucket.lanius.cmd.impl.GodmodeMobCommand;
import org.bitbucket.lanius.cmd.impl.HelpCommand;
import org.bitbucket.lanius.cmd.impl.NameProtectCommand;
import org.bitbucket.lanius.cmd.impl.NanCommand;
import org.bitbucket.lanius.cmd.impl.PingCommand;
import org.bitbucket.lanius.cmd.impl.PotionCommand;
import org.bitbucket.lanius.cmd.impl.SearchCommand;
import org.bitbucket.lanius.cmd.impl.SpamCommand;
import org.bitbucket.lanius.cmd.impl.TimersCommand;
import org.bitbucket.lanius.cmd.impl.ToggleCommand;
import org.bitbucket.lanius.cmd.impl.ToggleHideCommand;
import org.bitbucket.lanius.cmd.impl.VClipCommand;
import org.bitbucket.lanius.cmd.impl.WaypointsCommand;
import org.bitbucket.lanius.gui.ClickGui;
import org.bitbucket.lanius.gui.Component;
import org.bitbucket.lanius.gui.FrameComponent;
import org.bitbucket.lanius.gui.GuiHandler;
import org.bitbucket.lanius.gui.TextFieldComponent;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.NetHandlerHook;
import org.bitbucket.lanius.hook.impl.TabOverlaySub;
import org.bitbucket.lanius.routine.Renderable;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.routine.RoutineDataParser;
import org.bitbucket.lanius.routine.RoutineHandler;
import org.bitbucket.lanius.routine.impl.AntiCactusRoutine;
import org.bitbucket.lanius.routine.impl.AutoBreakRoutine;
import org.bitbucket.lanius.routine.impl.AutoRespondRoutine;
import org.bitbucket.lanius.routine.impl.AutoUseRoutine;
import org.bitbucket.lanius.routine.impl.BackRoutine;
import org.bitbucket.lanius.routine.impl.BlinkRoutine;
import org.bitbucket.lanius.routine.impl.BoatRoutine;
import org.bitbucket.lanius.routine.impl.BreadcrumbsRoutine;
import org.bitbucket.lanius.routine.impl.BrightRoutine;
import org.bitbucket.lanius.routine.impl.BunnyHopRoutine;
import org.bitbucket.lanius.routine.impl.CreativeArmorRoutine;
import org.bitbucket.lanius.routine.impl.CreativeDropRoutine;
import org.bitbucket.lanius.routine.impl.ElytraRoutine;
import org.bitbucket.lanius.routine.impl.EntityLauncherRoutine;
import org.bitbucket.lanius.routine.impl.FlightRoutine;
import org.bitbucket.lanius.routine.impl.FreecamRoutine;
import org.bitbucket.lanius.routine.impl.HighJumpRoutine;
import org.bitbucket.lanius.routine.impl.ItemSpoofRoutine;
import org.bitbucket.lanius.routine.impl.JesusRoutine;
import org.bitbucket.lanius.routine.impl.KillAuraRoutine;
import org.bitbucket.lanius.routine.impl.LagRoutine;
import org.bitbucket.lanius.routine.impl.LongJumpRoutine;
import org.bitbucket.lanius.routine.impl.NameProtectRoutine;
import org.bitbucket.lanius.routine.impl.NcpRoutine;
import org.bitbucket.lanius.routine.impl.NoFallRoutine;
import org.bitbucket.lanius.routine.impl.NoOverlaysRoutine;
import org.bitbucket.lanius.routine.impl.NoRenderRoutine;
import org.bitbucket.lanius.routine.impl.NoSwingRoutine;
import org.bitbucket.lanius.routine.impl.NoWeatherRoutine;
import org.bitbucket.lanius.routine.impl.NoclipRoutine;
import org.bitbucket.lanius.routine.impl.NukerRoutine;
import org.bitbucket.lanius.routine.impl.RegenRoutine;
import org.bitbucket.lanius.routine.impl.RetardRoutine;
import org.bitbucket.lanius.routine.impl.ReviveRoutine;
import org.bitbucket.lanius.routine.impl.SearchRoutine;
import org.bitbucket.lanius.routine.impl.SneakRoutine;
import org.bitbucket.lanius.routine.impl.SpamRoutine;
import org.bitbucket.lanius.routine.impl.SpeedRoutine;
import org.bitbucket.lanius.routine.impl.SpeedyRoutine;
import org.bitbucket.lanius.routine.impl.SpiderRoutine;
import org.bitbucket.lanius.routine.impl.StepRoutine;
import org.bitbucket.lanius.routine.impl.TeamRoutine;
import org.bitbucket.lanius.routine.impl.TracersRoutine;
import org.bitbucket.lanius.routine.impl.VelocityRoutine;
import org.bitbucket.lanius.routine.impl.ViaVersionRoutine;
import org.bitbucket.lanius.routine.impl.WallhackRoutine;
import org.bitbucket.lanius.routine.impl.WaypointsRoutine;
import org.bitbucket.lanius.util.RoutineUtils;
import org.bitbucket.lanius.util.concurrent.Rate;
import org.bitbucket.lanius.util.registry.Registry;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Lanius.MODID, name = Lanius.NAME, version = Lanius.VERSION, guiFactory = "org.bitbucket.lanius.gui.LaniusGuiFactory")
public final class Lanius implements Configurable {
	public static final Minecraft mc = Minecraft.getMinecraft();

	public static final File dataDir = new File(mc.mcDataDir, "lanius");

	private static final File enabledFile = new File(dataDir, "enabled_routines.cfg"),
			hiddenRoutinesFile = new File(dataDir, "hidden_routines.cfg");

	private static final RoutineDataParser routineEnabledParser = new RoutineDataParser() {

		@Override
		public void loadRoutine(Routine routine, String value) {
			// TODO Auto-generated method stub
			final boolean enabled = Boolean.parseBoolean(value);
			if (routine.isEnabled() && !enabled || !routine.isEnabled() && enabled) {
				routine.setEnabled();
			}
		}

		@Override
		public void saveRoutine(PrintWriter out, Routine routine) {
			// TODO Auto-generated method stub
			out.println(routine.name() + ":" + routine.isEnabled());
		}

	}, routineHiddenParser = new RoutineDataParser() {

		@Override
		public void loadRoutine(Routine routine, String value) {
			// TODO Auto-generated method stub
			final boolean hidden = Boolean.parseBoolean(value);
			if (routine.isHidden() && !hidden || !routine.isHidden() && hidden) {
				routine.setHidden();
			}
		}

		@Override
		public void saveRoutine(PrintWriter out, Routine routine) {
			// TODO Auto-generated method stub
			out.println(routine.name() + ":" + routine.isHidden());
		}

	};

	public static final String MODID = "lanius";

	@Mod.Instance(MODID)
	private static Lanius instance;

	public static final String NAME = "Lanius", VERSION = "3.9";

	public static Lanius getInstance() {
		return instance;
	}

	private Registry<ConfigContainer> cfgContainerRegistry;

	private CommandHandler cmdHandler;

	private Registry<ModCommand> cmdRegistry;

	private GuiHandler guiHandler;

	private Configuration modCfg;

	private Rate<CPacketPlayer> playerPacketRate;

	private Registry<Renderable> renderRegistry;

	private RoutineHandler routineHandler;

	private Registry<Routine> routineRegistry;

	private Registry<Configurable> standaloneCfgRegistry;

	public Registry<ConfigContainer> getCfgContainerRegistry() {
		return cfgContainerRegistry;
	}

	public CommandHandler getCmdHandler() {
		return cmdHandler;
	}

	public Registry<ModCommand> getCmdRegistry() {
		return cmdRegistry;
	}

	public GuiHandler getGuiHandler() {
		return guiHandler;
	}

	public Configuration getModCfg() {
		return modCfg;
	}

	public Rate<CPacketPlayer> getPlayerPacketRate() {
		final boolean ncpEnabled = RoutineUtils.ncpEnabled();
		playerPacketRate.setExecMax(ncpEnabled ? 2 : RoutineUtils.viaVersionEnabled() ? Rate.UNLIMITED_EXEC : 5);
		// Eric: +50 because this seems to be required on NCP. +100 for
		// variations in ping before they are taken into account by
		// NetworkUtils#lagTime.
		playerPacketRate.setResetDelay(ncpEnabled ? 1000L + 50L + 50L * 2L : 50L);
		return playerPacketRate;
	}

	public Registry<Renderable> getRenderRegistry() {
		return renderRegistry;
	}

	public RoutineHandler getRoutineHandler() {
		return routineHandler;
	}

	public Registry<Routine> getRoutineRegistry() {
		return routineRegistry;
	}

	@EventHandler
	public void init(final FMLInitializationEvent initEv) {
		playerPacketRate = new Rate<CPacketPlayer>(5, 50L) {

			@Override
			protected void onExecute(final CPacketPlayer object) {
				// TODO Auto-generated method stub
				NetHandlerHook.sendPlayerPacket(object);
			}

			@SubscribeEvent
			public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
				final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
				if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP) {
					return;
				}
				start();
			}

			@SubscribeEvent
			public void onUnload(final WorldEvent.Unload unloadEv) {
				stop();
			}

		};
		MinecraftForge.EVENT_BUS.register(playerPacketRate);
		registerCommands();
		registerRoutine(new SpeedRoutine());
		registerRoutine(new TracersRoutine());
		registerRoutine(new JesusRoutine());
		registerRoutine(new NoFallRoutine());
		registerRoutine(new FlightRoutine());
		registerRoutine(new StepRoutine());
		registerRoutine(new VelocityRoutine());
		registerRoutine(new KillAuraRoutine());
		registerRoutine(new ReviveRoutine());
		registerRoutine(new RegenRoutine());
		registerRoutine(new NameProtectRoutine());
		registerRoutine(new LagRoutine());
		registerRoutine(new SneakRoutine());
		registerRoutine(new FreecamRoutine());
		registerRoutine(new SearchRoutine());
		registerRoutine(new SpeedyRoutine());
		registerRoutine(new ElytraRoutine());
		registerRoutine(new BoatRoutine());
		registerRoutine(new AntiCactusRoutine());
		registerRoutine(new BlinkRoutine());
		registerRoutine(new CreativeDropRoutine());
		registerRoutine(new AutoUseRoutine());
		registerRoutine(new BreadcrumbsRoutine());
		registerRoutine(new NoSwingRoutine());
		registerRoutine(new ItemSpoofRoutine());
		registerRoutine(new LongJumpRoutine());
		registerRoutine(new NoclipRoutine());
		registerRoutine(new NoWeatherRoutine());
		registerRoutine(new RetardRoutine());
		registerRoutine(new AutoBreakRoutine());
		registerRoutine(new SpamRoutine());
		registerRoutine(new NoOverlaysRoutine());
		registerRoutine(new NukerRoutine());
		registerRoutine(new BrightRoutine());
		registerRoutine(new NoRenderRoutine());
		registerRoutine(new SpiderRoutine());
		registerRoutine(new TeamRoutine());
		registerRoutine(new HighJumpRoutine());
		registerRoutine(new WallhackRoutine());
		registerRoutine(new BackRoutine());
		registerRoutine(new WaypointsRoutine());
		registerRoutine(new EntityLauncherRoutine());
		registerRoutine(new AutoRespondRoutine());
		registerRoutine(new CreativeArmorRoutine());
		registerRoutine(new BunnyHopRoutine());
		// Eric: register instances of ConfigRoutine here
		registerRoutine(new NcpRoutine());
		registerRoutine(new ViaVersionRoutine());
		// Eric: test routines go here
		// registerRoutine(new TestRoutine());
		// registerRoutine(new PlayerBanRoutine());
		// registerRoutine(new SignLenRoutine());
		// registerRoutine(new NanArmorRoutine());
		// registerRoutine(new CrashAuraRoutine());
		MinecraftForge.EVENT_BUS.register(registerCfgContainer(cmdHandler = new CommandHandler()));
		MinecraftForge.EVENT_BUS.register(routineHandler = new RoutineHandler());
		MinecraftForge.EVENT_BUS.register(registerCfgContainer(guiHandler = new GuiHandler()));
		MinecraftForge.EVENT_BUS.register(registerCfgContainer(HookManager.netHook));
		MinecraftForge.EVENT_BUS.register(new TabOverlaySub());
		MinecraftForge.EVENT_BUS.register(new ConfigHandler());
		loadCfgContainers();
		loadStandaloneCfgs();
		// Eric: miscellaneous testing code goes here
		/*
		 * try { System.setOut(new PrintStream(new FileOutputStream("log.txt"))); }
		 * catch (FileNotFoundException e1) { //TODO Auto-generated catch block
		 * e1.printStackTrace(); } try { ASMifier.main(new String[] {
		 * "net.minecraft.inventory.ContainerPlayer$1" }); } catch (Exception e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
	}

	@Override
	public void load() {
		if (!enabledFile.exists()) {
			routineRegistry.get("Anti-cactus").setEnabled();
			routineRegistry.get("Name Protect").setEnabled();
			routineRegistry.get("No Fall").setEnabled();
			routineRegistry.get("Revive").setEnabled();
			routineRegistry.get("Speedy Gonzales").setEnabled();
			routineRegistry.get("Step").setEnabled();
			routineRegistry.get("Wallhack").setEnabled();
			routineRegistry.get("Waypoints").setEnabled();
			return;
		}
		loadRoutineData(enabledFile, routineEnabledParser);
		loadRoutineData(hiddenRoutinesFile, routineHiddenParser);
	}

	public void loadCfgContainers() {
		for (final ConfigContainer cfgContainer : cfgContainerRegistry.objects()) {
			cfgContainer.registerValues();
		}
		for (final FrameComponent frame : ClickGui.instance.frames) {
			for (final Component child : frame.children) {
				if (child instanceof TextFieldComponent) {
					final TextFieldComponent txtField = (TextFieldComponent) child;
					txtField.setText(txtField.cfgContainer.getString(child.text()));
				}
			}
		}
	}

	private void loadRoutineData(File dataFile, RoutineDataParser parser) {
		if (!dataFile.exists()) {
			return;
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(dataFile));
			String line;
			try {
				while ((line = in.readLine()) != null) {
					final int colonIdx = line.indexOf(":");
					parser.loadRoutine(routineRegistry.get(line.substring(0, colonIdx)), line.substring(colonIdx + 1));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void loadStandaloneCfgs() {
		for (final Configurable config : standaloneCfgRegistry.objects()) {
			config.load();
		}
	}

	@EventHandler
	public void postInit(final FMLPostInitializationEvent postInitEv) {
		for (final Routine routine : routineRegistry.objects()) {
			routine.init();
		}
		load();
		System.out.println("Finished startup");
	}

	@EventHandler
	public void preInit(final FMLPreInitializationEvent preInitEv) {
		System.out.println("Starting up");
		dataDir.mkdir();
		modCfg = new Configuration(preInitEv.getSuggestedConfigurationFile(), true);
		modCfg.load();
		// Eric: the code below is a hotfix for Minecraft Forge creating
		// duplicate category entries
		final Set<String> categoryNames = modCfg.getCategoryNames();
		if (!categoryNames.isEmpty()) {
			final Iterator<String> categoryIt = categoryNames.iterator();
			String firstCategory = categoryIt.next().toLowerCase();
			for (final String categoryName : categoryNames) {
				if (categoryName.equals(firstCategory)) {
					modCfg.removeCategory(modCfg.getCategory(categoryName));
					if (categoryIt.hasNext()) {
						firstCategory = categoryIt.next().toLowerCase();
					}
				}
			}
		}
		standaloneCfgRegistry = new Registry<Configurable>();
		cfgContainerRegistry = new Registry<ConfigContainer>();
		renderRegistry = new Registry<Renderable>();
		routineRegistry = new Registry<Routine>() {
			@Override
			public final Routine get(final String name) {
				return objMap.get(RoutineUtils.configName(name));
			}

			@Override
			public final Routine register(final String name, final Routine object) {
				ClientRegistry.registerKeyBinding(object.keyBind);
				if (object instanceof Configurable) {
					standaloneCfgRegistry.register(name, (Configurable) object);
				}
				if (object instanceof Renderable) {
					renderRegistry.register(name, (Renderable) object);
				}
				return objMap.put(RoutineUtils.configName(name), object);
			}
		};
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public final void run() {
				// TODO Auto-generated method stub
				System.out.println("Saving configuration changes");
				if (modCfg.hasChanged()) {
					modCfg.save();
				}
				ClickGui.instance.save();
				for (final Configurable config : standaloneCfgRegistry.objects()) {
					config.save();
				}
				save();
			}

		}));
	}

	private ConfigContainer registerCfgContainer(final ConfigContainer cfgContainer) {
		cfgContainerRegistry.register(cfgContainer.category(), cfgContainer);
		return cfgContainer;
	}

	private void registerCmd(final ModCommand cmd) {
		ClientCommandHandler.instance.registerCommand(cmd);
		for (final String name : cmd.names()) {
			cmdRegistry.register(name, cmd);
		}
	}

	public void registerCommands() {
		cmdRegistry = new Registry<ModCommand>();
		registerCmd(new VClipCommand());
		registerCmd(new ToggleCommand());
		registerCmd(new ConfigCommand());
		registerCmd(new DescCommand());
		registerCmd(new CheckCommand());
		registerCmd(new NameProtectCommand());
		registerCmd(new SearchCommand());
		registerCmd(new CreativeDropCommand());
		registerCmd(new BreadcrumbsCommand());
		registerCmd(new NanCommand());
		registerCmd(new SpamCommand());
		registerCmd(new GiveCommand());
		registerCmd(new HelpCommand());
		registerCmd(new DamageCommand());
		registerCmd(new WaypointsCommand());
		registerCmd(new TimersCommand());
		registerCmd(new GodmodeMobCommand());
		registerCmd(new ToggleHideCommand());
		registerCmd(new PingCommand());
		registerCmd(new PotionCommand());
		registerCmd(new AutoRespondCommand());
		registerCmd(new AllOffCommand());
		// Eric: test commands go here
		// registerCmd(new HorriblePotCommand());
		// registerCmd(new NanSwordCommand());
		// registerCmd(new NanArmorCommand());
		// registerCmd(new SendRespawnCommand());
		// registerCmd(new CrashServerCommand());
	}

	private Routine registerRoutine(final Routine routine) {
		registerCfgContainer(routine);
		return routineRegistry.register(routine.name(), routine);
	}

	@Override
	public void save() {
		saveRoutineData(enabledFile, routineEnabledParser);
		saveRoutineData(hiddenRoutinesFile, routineHiddenParser);
	}

	private void saveRoutineData(File dataFile, RoutineDataParser parser) {
		dataFile.delete();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter((new FileWriter(dataFile))));
			for (final Routine routine : routineRegistry.objects()) {
				parser.saveRoutine(out, routine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
