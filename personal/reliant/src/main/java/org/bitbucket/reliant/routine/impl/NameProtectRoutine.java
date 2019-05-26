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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BaseConfigurable;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.Configurable;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.handler.CrosshairHandler;
//import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.PlayerRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class NameProtectRoutine extends PlayerRoutine implements CrosshairHandler {
	private final Map<String, String> nameMap = new HashMap<String, String>();
	private int crosshairEntity = MemoryStream.NULL;
	private final Map<String, String> changedNameMap = new HashMap<String, String>(); // Eric: key=alias, value=name
	public final Configurable namesCfg = new BaseConfigurable(new File(Reliant.instance.getConfigsDir(), "name_protect.txt")) {
		private static final String ENTRY_SEPARATOR = ":";
		
		@Override
		public void load() {
			// TODO Auto-generated method stub
			if (!getConfigFile().exists()) {
				return;
			}
			nameMap.clear();
			try {
				final BufferedReader configReader = new BufferedReader(new InputStreamReader(new FileInputStream(getConfigFile()), "UTF-8"));
				try {
					String entry;
					while ((entry = configReader.readLine()) != null) {
						if (StringUtils.comment(entry)) {
							continue;
						}
						final int separatorIdx = entry.lastIndexOf(ENTRY_SEPARATOR);
						final String name = entry.substring(0, separatorIdx);
						final int localIdx = SdkUtils.localPlayerIdx();
						if (GameCache.getClientState() != MemoryStream.NULL && localIdx != SdkUtils.INVALID_PLAYER_IDX && name.equals(SdkUtils.readRadarName(localIdx))) {
							continue;
						}
						nameMap.put(name, entry.substring(separatorIdx + ENTRY_SEPARATOR.length()));
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
				configWriter.println(StringUtils.COMMENT_PREFIX + " Format of the entries is \"name" + ENTRY_SEPARATOR + "value\"");
				for (final Map.Entry<String, String> nameEntry : nameMap.entrySet()) {
					configWriter.println(nameEntry.getKey() + ENTRY_SEPARATOR + nameEntry.getValue());
				}
				configWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e);
			}
		}
		
	};

	public NameProtectRoutine() {
		super("Name Protect", "Protects a player's name with an alias.", true, true, 1007, new BoolOption("Allies", "Specifies whether or not the added players should not be targeted by this cheat.", true), new KeyOption("Add Key", "Specifies the key that should be pressed to add a player to the name map.", 0x2D), new KeyOption("Remove Key", "Specifies the key that should be pressed to remove a player from the name map.", 0x2E));
		// TODO Auto-generated constructor stub
	}

	public void clearNameMap() {
		nameMap.clear();
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFBFFF00;
	}

	public boolean containsAlias(final String alias) {
		return nameMap.containsValue(alias);
	}
	
	@Override
	public int getCrosshairEntity() {
		// TODO Auto-generated method stub
		return crosshairEntity == MemoryStream.NULL ? GameCache.getCrosshairEntity() : crosshairEntity;
	}

	@Override
	public boolean handle(final int player, final int entityIdx) {
		if (!super.handle(player, entityIdx) || player == GameCache.getLocalPlayer()) {
			return false;
		}
		final String name = SdkUtils.readRadarName(entityIdx);
		if (StringUtils.empty(name)) {
			return false;
		}
		final boolean hasAlias = nameMap.containsKey(name), changedName = changedNameMap.containsKey(name);
		if (!hasAlias && changedName && !nameMap.containsValue(name) && writeName(changedNameMap.get(name), entityIdx)) {
			changedNameMap.remove(name);
			return true;
		} else if (!hasAlias || changedName) {
			return false;
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			return false;
		}
		final String alias = nameMap.get(name);
		if (!writeName(alias, entityIdx)) {
			return false;
		}
		changedNameMap.put(alias, name);
		return true;
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return String.valueOf(nameMap.size());
	}

	public String putAlias(final String name, final String alias) {
		return nameMap.put(name, alias);
	}

	public String removeAlias(final String name) {
		return nameMap.remove(name);
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		crosshairEntity = MemoryStream.NULL;
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr != MemoryStream.NULL && GameCache.getInGame() == SdkUtils.SIGNONSTATE_FULL) {
			final Iterator<Map.Entry<String, String>> changedNameIt = changedNameMap.entrySet().iterator();
			while (changedNameIt.hasNext()) {
				final Map.Entry<String, String> changedNameEntry = changedNameIt.next();
				for (int playerIdx = 0; playerIdx <= SdkUtils.PLAYERS_SZ; playerIdx++) {
					final int player = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + playerIdx * SdkUtils.NEXT_ENTITY_SZ);
					if (player == MemoryStream.NULL || StringUtils.empty(SdkUtils.readRadarName(playerIdx)) && playerIdx == 64) {
						continue;
					}
					final String playerName = SdkUtils.readRadarName(playerIdx);
					if (StringUtils.empty(playerName) || !playerName.equals(changedNameEntry.getKey()) || !writeName(changedNameEntry.getValue(), playerIdx)) {
						continue;
					}
					changedNameIt.remove();
					break;
				}
			}
		} else {
			changedNameMap.clear();
		}
	}

	@Override
	public void setCrosshairEntity(final int crosshairEntity) {
		// TODO Auto-generated method stub
		this.crosshairEntity = crosshairEntity;
	}
	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		super.update(post);
		if (post) {
			return;
		}
		final int crosshairEntity = getCrosshairEntity();
		if (crosshairEntity == MemoryStream.NULL) {
			return;
		}
		final int crosshairIdx = SdkUtils.playerIdx(crosshairEntity);
		if (crosshairIdx == SdkUtils.INVALID_PLAYER_IDX) {
			return;
		}
		final String name = SdkUtils.readRadarName(crosshairIdx);
		if (StringUtils.empty(name)) {
			return;
		}
		final KeyOption addKey = getKeyOption("Add Key"), removeKey = getKeyOption("Remove Key");
		final boolean changedName = changedNameMap.containsKey(name), hasAlias = changedName ? nameMap.containsValue(name) : nameMap.containsKey(name);
		if (addKey.getValue() > 0x0 && addKey.keyPressed(true) && !hasAlias) {
			putAlias(name, name);
		} else if (removeKey.getValue() > 0x0 && removeKey.keyPressed(true) && hasAlias) {
			removeAlias(changedName ? changedNameMap.get(name) : name);
		}
		setCrosshairEntity(MemoryStream.NULL);
	}

	private boolean writeName(final String name, final int entityIdx) {
		// TODO(Eric) This code is broken for now.
		/*final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		final int gameResources = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("zat").offset("ScoreBoardBase/GameResources"));
		if (gameResources == MemoryStream.NULL) {
			throw new InvalidDataException("game_resources");
		}
		int namePtr = Reliant.instance.getProcessStream().readInt(gameResources + entityIdx * 0x4);
		final String currentRadarName = SdkUtils.readRadarName(entityIdx);
		for (int i = 0; i <= SdkUtils.PLAYERS_SZ * 0xFF; i++) {
			final int player = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwEntityList") + i * SdkUtils.NEXT_ENTITY_SZ);
			if (player == MemoryStream.NULL || player == GameCache.getLocalPlayer() || StringUtils.empty(SdkUtils.readRadarName(i)) && i == 64) {
				continue;
			}
			final String playerRadarName = Reliant.instance.getProcessStream().read(namePtr, SdkUtils.PLAYER_NAME_SZ, "UTF-8");
			if (playerRadarName != null && playerRadarName.equals(currentRadarName)) {
				break;
			} else {
				namePtr = Reliant.instance.getProcessStream().readInt(gameResources + i * 0x4);
			}
		}
		if (namePtr == MemoryStream.NULL) {
			throw new InvalidDataException("name_ptr");
		}*/
		return SdkUtils.writeRadarName(name, entityIdx)/* && Reliant.instance.getProcessStream().write(namePtr, name, SdkUtils.PLAYER_NAME_SZ, "UTF-8")*/;
	}
}
