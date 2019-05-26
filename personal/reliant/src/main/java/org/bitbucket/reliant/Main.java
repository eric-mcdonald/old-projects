package org.bitbucket.reliant;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bitbucket.eric_generic.lang.PlatformUtils;
import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.log.Logger;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.Configuration;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.cfg.Option;
import org.bitbucket.reliant.handler.PlayerHandler;
//import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.memory.WriteMemoryException;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.Routine;
import org.bitbucket.reliant.routine.impl.AutoconfigureRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class Main {
	static final String TARGET_PROCESS = "csgo.exe";
	public static final String TARGET_WINDOW_CLASS = "Valve001";
	private static String targetWindow = "Counter-Strike: Global Offensive", targetDataDir = "csgo";
	public static final String DIR_SEPARATOR = "\"";
	//private static byte mouseEnabledConst = 16; // TODO(Eric) Hardcoded constant is correct as of 2017-12-28
	//private static boolean hasMouseDisabledConst;

	public static boolean csco() {
		return targetWindow.equals("Counter-Strike: Classic Offensive");
	}
	private static native boolean cursorShowing();
	public static String getTargetDataDir() {
		return targetDataDir;
	}
	public static String getTargetWindow() {
		return targetWindow;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (!PlatformUtils.windows()) {
			throw new UnsupportedOsException(System.getProperty("os.name").toLowerCase());
		}
		boolean antiAliasing = false, logFile = false, checkBuild = false, devEnv = false, downloadDumpers = false, bypassKillSwitch = false, legacyToolchain = false, hasCfgDir = false, hasCheatPriority = false, hasGamePriority = false, dlOffsets = false;
		final String BYPASS_KILL_SWITCH = StringUtils.PROGRAM_ARG_PREFIX + "bypass-kill-switch";
		Reliant.dataDir.mkdir();
		File configsDir = new File(Reliant.dataDir, "cfg");
		int cheatPriority = 0x00000020, gamePriority = 0x00000020;
		int argCount = 0;
		for (final String argument : args) {
			if (hasCheatPriority) {
				cheatPriority = Integer.parseInt(argument);
				hasCheatPriority = false;
			} else if (hasGamePriority) {
				gamePriority = Integer.parseInt(argument);
				hasGamePriority = false;
			} else if (hasCfgDir) {
				final String dirArgs = StringUtils.arguments(args, argCount);
				final int firstIdx = dirArgs.indexOf(Main.DIR_SEPARATOR), lastIdx = dirArgs.lastIndexOf(Main.DIR_SEPARATOR);
				configsDir = new File(firstIdx != -1 && lastIdx != -1 && firstIdx != lastIdx ? dirArgs.substring(Main.DIR_SEPARATOR.length(), lastIdx) : dirArgs);
				hasCfgDir = false;
			} else {
				if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "log-file")) {
					logFile = true;
				} else if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "check-build")) {
					checkBuild = true;
				} else if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "dev-env")) {
					devEnv = true;
				} else if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "cheat-priority")) {
					hasCheatPriority = true;
				} else if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "game-priority")) {
					hasGamePriority = true;
				} else if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "download-dumpers")) {
					downloadDumpers = true;
				} else if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "download-offsets")) {
					dlOffsets = true;
				} else if (argument.equalsIgnoreCase(BYPASS_KILL_SWITCH)) {
					bypassKillSwitch = true;
				} else if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "csco")) {
					targetWindow = "Counter-Strike: Classic Offensive";
					targetDataDir = "csco";
				} else if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "legacy-toolchain")) {
					legacyToolchain = true;
				} else if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "config-dir")) {
					hasCfgDir = true;
				} else if (argument.equalsIgnoreCase(StringUtils.PROGRAM_ARG_PREFIX + "anti-aliasing")) { 
					antiAliasing = true;
				} else {
					System.err.println("Invalid argument specified: \"" + argument + "\"");
				}
			}
			++argCount;
		}
		System.out.println("Checking if this program has been kill switched.");
		try {
			final BufferedReader killSwitchIn = new BufferedReader(new InputStreamReader(new URL("https://dl.dropboxusercontent.com/s/r9v52grtctkwv8x/kill_switch.txt").openStream()));
			String line;
			int lineCount = 0;
			boolean killSwitched = false, bypassable = false;
			final List<String> reason = new ArrayList<String>();
			while ((line = killSwitchIn.readLine()) != null) {
				if (lineCount == 0) {
					killSwitched = Boolean.parseBoolean(line);
				} else if (lineCount == 1) {
					bypassable = Boolean.parseBoolean(line);
				} else if (lineCount >= 2) {
					reason.add(line);
				}
				++lineCount;
			}
			if (killSwitched) {
				System.out.print("This program has been kill switched for the following reason: \"");
				for (int lineIdx = 0; lineIdx < reason.size(); lineIdx++) {
					System.out.print(reason.get(lineIdx));
					if (lineIdx >= reason.size() - 1) {
						System.out.print("\"");
					}
					System.out.println();
				}
				if (bypassKillSwitch && bypassable) {
					System.out.println("Bypassing the kill switch. Use this program at your own risk!");
				} else {
					if (bypassable) {
						System.out.println("This kill switch is bypassable. Launch this program with \"" + BYPASS_KILL_SWITCH + "\" to ignore it.");
					}
					System.exit(0);
				}
			}
			killSwitchIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Reliant.instance.init(configsDir, logFile, checkBuild, devEnv, cheatPriority, gamePriority, downloadDumpers, legacyToolchain, antiAliasing, dlOffsets);
		//System.out.println(Reliant.instance.getRenderer().textSize("No players are being spectated.", Reliant.instance.getGuiFont(), 24, true)[0] + ":" + Reliant.instance.getRenderer().textSize("Clan Tag [VAC-undetected]", Reliant.instance.getGuiFont(), 24, true)[0]);
		final List<Routine> sortedRoutines = new ArrayList<Routine>(Reliant.instance.getRoutineRegistry().objects());
		Collections.sort(sortedRoutines, Routine.priorityCmp);
		while (processActive(TARGET_PROCESS)) {
			try {
				//Reliant.instance.getProcessStream().enableMouse(); // Eric: Enable mouse movement if it was disabled.
				GameCache.update();
				if (GameCache.getClientState() == MemoryStream.NULL) {
					//throw new InvalidDataException("client_state");
					continue; // Eric: This could mean CS:GO is simply shutting down.
				}
				final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
				/*if (GameCache.getInGame() == SdkUtils.SIGNONSTATE_NONE && clientAddr != MemoryStream.NULL && !hasMouseDisabledConst) {
					mouseEnabledConst = Reliant.instance.getProcessStream().readByte(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwMouseEnable"));
					//System.out.println(mouseEnabledConst);
					hasMouseDisabledConst = true;
				}*/
				final Renderer renderer = Reliant.instance.getRenderer();
				renderer.begin();
				final boolean checkMouse = (Boolean) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Check Mouse").getValue();
				final AutoconfigureRoutine autoCfgRoutine = (AutoconfigureRoutine) Reliant.instance.getRoutineRegistry().get("Auto-configure");
				updateRoutine(autoCfgRoutine, checkMouse);
				for (final Routine routine : sortedRoutines) {
					if (routine.equals(autoCfgRoutine)) {
						continue;
					}
					updateRoutine(routine, checkMouse);
				}
				if (clientAddr != MemoryStream.NULL && GameCache.getInGame() == SdkUtils.SIGNONSTATE_FULL) {
					for (int playerIdx = 0; playerIdx <= SdkUtils.PLAYERS_SZ; playerIdx++) {
						final int player = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + playerIdx * SdkUtils.NEXT_ENTITY_SZ);
						if (player == MemoryStream.NULL || StringUtils.empty(SdkUtils.readRadarName(playerIdx)) && playerIdx == 64) {
							continue;
						}
						for (final PlayerHandler handler : Reliant.instance.getPlayerHandlerRegistry().objects()) {
							final KeyOption key = handler instanceof Configuration ? (KeyOption) ((Configuration) handler).getOptionByName("Key") : null;
							if (key == null || key.getValue() <= 0x0 || key.keyDown(true)) {
								handler.handle(player, playerIdx);
							}
						}
					}
				}
				for (final Routine routine : sortedRoutines) {
					if (routine.equals(autoCfgRoutine)) {
						continue;
					}
					final KeyOption key = routine instanceof Configuration ? (KeyOption) ((Configuration) routine).getOptionByName("Key") : null;
					if (Reliant.instance.isRunning() && (!routine.inGameOnly() || GameCache.getInGame() == SdkUtils.SIGNONSTATE_FULL) && routine.isEnabled() && (!checkMouse || routine.ignoresMouse() || !mouseEnabled()) && (key == null || key.getValue() <= 0x0 || key.keyDown(true))) {
						routine.update(true);
					}
				}
				renderer.end();
				updateWindow(Reliant.instance.getOverlayedWindow());
				if (GameCache.getInGame() != SdkUtils.SIGNONSTATE_FULL) {
					SdkUtils.reset();
				}
				@SuppressWarnings("unchecked")
				final int updateInterval = ((Option<ClampedNumber<Integer>>) Reliant.instance.getConfigRegistry().get("General").getOptionByName("Update Interval")).getValue().intValue();
				if (updateInterval > 0) {
					try {
						Thread.sleep(updateInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Reliant.instance.getLogger().logError(e);
					}
				}
			} catch (final WriteMemoryException writeMemEx) {
				Reliant.instance.getLogger().log(writeMemEx.getWriteAddr() == WriteMemoryException.NO_ADDR ? "Failed to write \"" + writeMemEx.getWriteVal() + "\"" : "Failed to write \"" + writeMemEx.getWriteVal() + "\" to " + Long.toHexString(writeMemEx.getWriteAddr()), Logger.Type.ERROR);
				Reliant.instance.getLogger().logError(writeMemEx);
			} catch (final CheatException cheatEx) {
				Reliant.instance.getLogger().logError(cheatEx);
			}
		}
		System.exit(0); // Eric: The program won't actually close without this line because of the GUI
	}
	public static boolean mouseEnabled() {
		// TODO(Eric) Broken code as of 2018-01-01
		/*final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		final byte mouseEnabled = Reliant.instance.getProcessStream().readByte(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwMouseEnable"));
		if (GameCache.getInGame() == SdkUtils.SIGNONSTATE_NONE && !hasMouseDisabledConst) {
			mouseEnabledConst = mouseEnabled;
			hasMouseDisabledConst = true;
		}
		//System.out.println(Reliant.instance.getProcessStream().readByte(clientAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwMouseEnable")));
		return mouseEnabled == mouseEnabledConst || cursorShowing();*/
		return cursorShowing();
	}
	static native boolean processActive(final String process);
	private static void updateRoutine(final Routine routine, final boolean checkMouse) {
		final KeyOption key = routine instanceof Configuration ? (KeyOption) ((Configuration) routine).getOptionByName("Key") : null;
		final boolean update = Reliant.instance.isRunning() && (!routine.inGameOnly() || GameCache.getInGame() == SdkUtils.SIGNONSTATE_FULL) && routine.isEnabled() && (!checkMouse || routine.ignoresMouse() || !mouseEnabled()), active = key == null || key.getValue() <= 0x0 || key.keyDown(true);
		if (update && active) {
			routine.update(false);
		} else if (!update && (key == null || key.getValue() <= 0x0 || !active)) { // Eric: Do not reset inactive routines
			routine.reset(false); // Eric: Continuously reset the routines for routines that require this.
		}
	}
	private static native void updateWindow(final long window);
	public static native boolean windowIsForeground(final String window);
}
