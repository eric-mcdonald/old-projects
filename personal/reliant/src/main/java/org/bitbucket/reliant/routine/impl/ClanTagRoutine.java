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
import java.util.ArrayList;
import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BaseConfigurable;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.Configurable;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class ClanTagRoutine extends DisplayRoutine {
	private final List<String> clanTags = new ArrayList<String>();
	public final Configurable clanTagsCfg = new BaseConfigurable(new File(Reliant.instance.getConfigsDir(), "clan_tag.txt")) {
		@Override
		public void load() {
			// TODO Auto-generated method stub
			clearClanTags();
			if (!getConfigFile().exists()) {
				addDefaultTags();
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
						addClanTag(entry);
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
				configWriter.println(StringUtils.COMMENT_PREFIX + " Format of the entries is \"value\"");
				for (final String clanTag : clanTags) {
					configWriter.println(clanTag);
				}
				configWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e);
			}
		}
	};
	private final Timer cycleTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			/*long retVal = getInt("Interval");
			if (Reliant.instance.antiOverwatch()) {
				retVal += Reliant.instance.getOverwatchRand().nextInt(Reliant.OVERWATCH_MAX_TIME);
			}*/
			return /*retVal*/getInt("Interval");
		}
		
	};
	private int currentTag, prevTag;
	private String prevClanTag;
	
	public ClanTagRoutine() {
		super("Clan Tag", "Cycles through a list of clan tags.", true, 1000, new IntOption("Interval", "Specifies the delay before switching to another clan tag.", new ClampedNumber<Integer>(2000, 0, 10000), 1000));
		addDefaultTags();
		// TODO Auto-generated constructor stub
	}
	
	public boolean addClanTag(final String clanTag) {
		return clanTags.add(clanTag);
	}

	private void addDefaultTags() {
		addClanTag(Reliant.NAME);
		addClanTag("Java-based");
		addClanTag("VAC-undetected");
		addClanTag("External");
	}
	
	public void clearClanTags() {
		clanTags.clear();
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFF4000;
	}
	@Override
	public String info() {
		// TODO Auto-generated method stub
		return clanTags.isEmpty() ? null : clanTags.get(prevTag >= clanTags.size() ? 0 : prevTag);
	}
	public boolean removeClanTag(final String clanTag) {
		return clanTags.remove(clanTag);
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		currentTag = prevTag = 0;
		if (prevClanTag != null) {
			Reliant.instance.getProcessStream().setClanTag(prevClanTag);
			prevClanTag = null;
		}
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post || clanTags.isEmpty() || !cycleTimer.delayPassed() || GameCache.getClientState() == MemoryStream.NULL) {
			return;
		}
		final int localIdx = SdkUtils.localPlayerIdx();
		if (localIdx == SdkUtils.INVALID_PLAYER_IDX) {
			return;
		}
		if (prevClanTag == null) {
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr == MemoryStream.NULL) {
				throw new InvalidDataException("client_addr");
			}
			final int playerRes = Reliant.instance.getProcessStream().readInt(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("CSPlayerResource"));
			if (playerRes == MemoryStream.NULL) {
				throw new InvalidDataException("player_res");
			}
			prevClanTag = Reliant.instance.getProcessStream().read(playerRes + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_szClan") + (localIdx + 0x1) * 0x10, 16, "UTF-8");
		}
		if (currentTag >= clanTags.size()) {
			currentTag = prevTag = 0;
		}
		Reliant.instance.getProcessStream().setClanTag(clanTags.get(prevTag = currentTag++));
		cycleTimer.setStartTime();
	}
}
