package org.bitbucket.reliant.routine.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BaseConfigurable;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.Configurable;
import org.bitbucket.reliant.cfg.DirOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.CrosshairRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class AutoconfigureRoutine extends CrosshairRoutine {
	private enum Mode {
		CROSSHAIR_ONLY,
		VISIBLE,
		BOTH
	}
	public final Set<String> names = new HashSet<String>();
	private File currentDirVal;

	public final Configurable namesCfg = new BaseConfigurable(new File(Reliant.instance.getConfigsDir(), "auto-configure.txt")) {

		@Override
		public void load() {
			// TODO Auto-generated method stub
			names.clear();
			if (!getConfigFile().exists()) {
				return;
			}
			try {
				final BufferedReader configReader = new BufferedReader(new InputStreamReader(new FileInputStream(getConfigFile()), "UTF-8"));
				try {
					String entry;
					while ((entry = configReader.readLine()) != null) {
						if (StringUtils.comment(entry)) {
							continue;
						}
						final int localIdx = SdkUtils.localPlayerIdx();
						if (GameCache.getClientState() != MemoryStream.NULL && localIdx != SdkUtils.INVALID_PLAYER_IDX && entry.equals(SdkUtils.readRadarName(localIdx))) {
							continue;
						}
						names.add(entry);
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
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e1);
			}
		}

		@Override
		public void save() {
			// TODO Auto-generated method stub
			getConfigFile().getParentFile().mkdirs();
			getConfigFile().delete();
			try {
				final PrintWriter configWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getConfigFile()), "UTF-8")));
				configWriter.println(StringUtils.COMMENT_PREFIX + " This is the custom configuration file of " + name() + ". It is not recommended that you edit this file directly.");
				configWriter.println(StringUtils.COMMENT_PREFIX + " Format of the entries is \"name\"");
				for (final String nameEntry : names) {
					configWriter.println(nameEntry);
				}
				configWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e);
			}
		}

	};

	private File prevCfgDir;

	public AutoconfigureRoutine() {
		super("Auto-configure", "Automatically switches to a configuration based on the specified condition.", true, true, -999, new KeyOption("Add Key", "Specifies the key that should be pressed to add a player to the name list.", 0x0), new KeyOption("Remove Key", "Specifies the key that should be pressed to remove a player from the name list.", 0x0), new IntOption("Mode", "Specifies the condition to execute on.", new ClampedNumber<Integer>(Mode.BOTH.ordinal(), 0, Mode.values().length - 1), 1), new BoolOption("Switch Back", "Specifies whether or not to switch back to the previous configuration.", true), new DirOption("Configuration", "Specifies the configuration directory to use.", "data/cfg_auto-configure"));
		// TODO Auto-generated constructor stub
	}

	private boolean changeCfg() {
		if (!Reliant.instance.isRunning()) {
			return false;
		}
		final File dirVal = getDirectory("Configuration");
		if (currentDirVal == null || !dirVal.equals(currentDirVal)) {
			if (prevCfgDir == null) {
				prevCfgDir = Reliant.instance.getConfigsDir();
			}
			Reliant.instance.saveConfigs(true);
			Reliant.instance.loadConfigs(currentDirVal = dirVal, true);
			return true;
		}
		return false;
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFFFF00;
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return String.valueOf(names.size());
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		// TODO(Eric) Not checking the shutdown flag can cause the configurations to be written to recursively...
		if (!shutdown && currentDirVal != null && prevCfgDir != null) {
			Reliant.instance.saveConfigs(true);
			Reliant.instance.loadConfigs(prevCfgDir, true);
		}
		currentDirVal = prevCfgDir = null;
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post) {
			return;
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		final int mode = getInt("Mode");
		boolean hasEntity = false;
		if (SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			final int crosshairEnt = getCrosshairEntity();
			int crosshairIdx = SdkUtils.INVALID_PLAYER_IDX;
			String crosshairName = "";
			if (crosshairEnt != MemoryStream.NULL && SdkUtils.entityAlive(crosshairEnt)) {
				crosshairIdx = SdkUtils.playerIdx(crosshairEnt);
				if (crosshairIdx != SdkUtils.INVALID_PLAYER_IDX) {
					crosshairName = SdkUtils.readRadarName(crosshairIdx);
					if (!StringUtils.empty(crosshairName)) {
						final KeyOption addKey = getKeyOption("Add Key"), removeKey = getKeyOption("Remove Key");
						if (addKey.getValue() > 0x0 && addKey.keyPressed(true)) {
							names.add(crosshairName);
						} else if (removeKey.getValue() > 0x0 && removeKey.keyPressed(true)) {
							names.remove(crosshairName);
						}
					}
				}
			}
			if ((mode == Mode.VISIBLE.ordinal() || mode == Mode.BOTH.ordinal())) {
				for (int playerIdx = 0; playerIdx <= SdkUtils.PLAYERS_SZ; playerIdx++) {
					final int player = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + playerIdx * SdkUtils.NEXT_ENTITY_SZ);
					if (player == MemoryStream.NULL || !SdkUtils.entityAlive(player) || player == GameCache.getLocalPlayer() || StringUtils.empty(SdkUtils.readRadarName(playerIdx)) && playerIdx == 64) {
						continue;
					}
					final String playerName = SdkUtils.readRadarName(playerIdx);
					if (StringUtils.empty(playerName)) {
						continue;
					}
					if (names.contains(playerName) && SdkUtils.entityVisible(player, true, SdkUtils.VisAggressiveness.BASIC, true, 1.0F)) {
						changeCfg();
						hasEntity = true;
						break;
					}
				}
			}
			if ((mode == Mode.CROSSHAIR_ONLY.ordinal() || mode == Mode.BOTH.ordinal())) {
				if (!(crosshairEnt == MemoryStream.NULL || !SdkUtils.entityAlive(crosshairEnt) || StringUtils.empty(crosshairName)) && names.contains(crosshairName)) {
					changeCfg();
					hasEntity = true;
				}
			}
		}
		if (!hasEntity && getBoolean("Switch Back")) {
			reset(false);
		}
		setCrosshairEntity(MemoryStream.NULL);
	}
}
