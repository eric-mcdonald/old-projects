package net.hackforums.pootpoot.monumental;

import net.hackforums.pootpoot.monumental.command.Command;
import net.hackforums.pootpoot.monumental.event.ModeListener;
import net.hackforums.pootpoot.monumental.init.Commands;
import net.hackforums.pootpoot.monumental.init.Modes;
import net.hackforums.pootpoot.monumental.settings.ModSettings;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Monumental.MODID, version = Monumental.VERSION)
public class Monumental
{
	public static final String MODID = "monumental", VERSION = "1.0", NAME = "Monumental";
	private static final Logger LOGGER = LogManager.getLogger();

	@EventHandler
	public void init(FMLInitializationEvent event) {
		for (Command command : Command.COMMANDS) {
			ClientCommandHandler.instance.registerCommand(command);
		}
		ModeListener modeListener = new ModeListener();
		FMLCommonHandler.instance().bus().register(modeListener);
		MinecraftForge.EVENT_BUS.register(modeListener);
		LOGGER.info(NAME + ": mod initialized");
	}
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LOGGER.info(NAME + ": initializing mod");
		Modes.init();
		Commands.init();
		Modes.NOCHEATPLUS_MODE.setEnabled(true);
		final ModSettings settings = new ModSettings();
		settings.loadModes();
		settings.loadKeyBinds();
		settings.loadValues();
		Runtime.getRuntime().addShutdownHook(new Thread(NAME + " Shutdown Thread")
		{
			@Override
			public void run() {
				settings.saveModes();
				settings.saveKeyBinds();
				settings.saveValues();
			}
		});
	}
}
