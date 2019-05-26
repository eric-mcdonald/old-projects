package org.bitbucket.lanius.routine.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.Configurable;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.routine.impl.AutoRespondRoutine.ResponseEntry.MatchType;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoRespondRoutine extends TabbedRoutine implements Hook<NetHandlerData>, Configurable {
	public static class ResponseEntry {
		public static enum MatchType {
			STARTS_WITH("starts_with"), CONTAINS("contains"), EQUALS("equals"), ENDS_WITH("ends_with");

			private static final MatchType[] values = values();

			public static MatchType getById(String id) {
				for (MatchType type : values) {
					if (type.id.equalsIgnoreCase(id)) {
						return type;
					}
				}
				return null;
			}

			private String id;

			private MatchType(String id) {
				this.id = id;
			}

			public String getId() {
				return id;
			}

			@Override
			public String toString() {
				return getId();
			}
		}

		private String key;
		private String responseMsg;
		private MatchType matchType;
		private boolean ignoreCase;

		public ResponseEntry(String key, String responseMsg, MatchType matchType, boolean ignoreCase) {
			this.key = key;
			this.responseMsg = responseMsg;
			this.matchType = matchType;
			this.ignoreCase = ignoreCase;
		}

		public String getKey() {
			return key;
		}

		public MatchType getMatchType() {
			return matchType;
		}

		public String getResponseMsg() {
			return responseMsg;
		}

		public boolean isIgnoreCase() {
			return ignoreCase;
		}

		public boolean matches(String message) {
			String key = getKey();
			if (isIgnoreCase()) {
				// Eric: Converts the strings to upper case for case-insensitive comparisons.
				key = key.toUpperCase();
				message = message.toUpperCase();
			}
			boolean matches = false;
			switch (getMatchType()) {
			case STARTS_WITH:
				matches = message.startsWith(key);
				break;
			case CONTAINS:
				matches = message.contains(key);
				break;
			case EQUALS:
				matches = message.equals(key);
				break;
			case ENDS_WITH:
				matches = message.endsWith(key);
				break;
			}
			return matches;
		}

		public void setIgnoreCase(boolean ignoreCase) {
			this.ignoreCase = ignoreCase;
		}

		public void setMatchType(MatchType matchType) {
			this.matchType = matchType;
		}

		public void setResponseMsg(String responseMsg) {
			this.responseMsg = responseMsg;
		}

		@Override
		public String toString() {
			return "'" + getKey() + "\':'" + getResponseMsg() + "':" + getMatchType() + ":" + isIgnoreCase();
		}
	}

	private static final File respondFile = new File(Lanius.dataDir, "auto-respond.cfg");
	private final List<ResponseEntry> responses = new CopyOnWriteArrayList<ResponseEntry>();
	private final Queue<String> queuedResponses = new LinkedBlockingQueue<String>();

	private long lastRespondTime;

	public AutoRespondRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.MISCELLANEOUS);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Automatically responds to chat messages.";
	}

	public List<ResponseEntry> getResponses() {
		return responses;
	}

	@Override
	public void init() {
		lastRespondTime = 0L;
		queuedResponses.clear();
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		responses.clear();
		if (!respondFile.exists()) {
			responses.add(
					new ResponseEntry(" has requested to teleport to you.", "/tpaccept", MatchType.ENDS_WITH, false));
			return;
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(respondFile));
			String line;
			while ((line = in.readLine()) != null) {
				int keyLastSep = line.substring(1).indexOf("'") + 1,
						responseSecondSep = line.substring(keyLastSep + 1 + ":'".length() + 1).indexOf("'") + keyLastSep
								+ 1 + ":'".length() + 1;
				String[] fields = line.split(":");
				ResponseEntry entry = new ResponseEntry(line.substring(1, keyLastSep),
						line.substring(keyLastSep + 1 + ":'".length(), responseSecondSep),
						MatchType.getById(fields[fields.length - 2]), Boolean.parseBoolean(fields[fields.length - 1]));
				responses.add(entry);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Auto-respond";
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onClientTickLowest(final TickEvent.ClientTickEvent event) {
		if (!event.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || Lanius.mc.isGamePaused()
				|| !Lanius.mc.player.isEntityAlive()) {
			return;
		}
		if (System.currentTimeMillis() - lastRespondTime >= getInt("Delay").intValue() && !queuedResponses.isEmpty()
				&& (!RoutineUtils.ncpEnabled() || !RoutineUtils.enabled("Spam"))) {
			Lanius.mc.player.connection.getNetworkManager().sendPacket(new CPacketChatMessage(queuedResponses.poll())); // Eric:
																														// Sends
																														// the
																														// chat
																														// message
																														// through
																														// the
																														// NetworkManager
																														// to
																														// bypass
																														// the
																														// hooks
																														// on
																														// EntityPlayerSP#sendChatMessage.
			lastRespondTime = System.currentTimeMillis();
		}
	}

	@Override
	public void onExecute(NetHandlerData data, Phase phase) {
		// TODO Auto-generated method stub
		if (phase.equals(Phase.END) && data.retVal instanceof SPacketChat && isEnabled()) {
			String message = ((SPacketChat) data.retVal).getChatComponent().getUnformattedText();
			for (ResponseEntry entry : responses) {
				if (entry.matches(message)) {
					queuedResponses.add(entry.getResponseMsg());
				}
			}
		}
	}

	@Override
	public void registerValues() {
		registerValue("Delay", 1000, 0, 10000, "Specifies how long to wait before sending another chat message.");
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		respondFile.delete();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter((new FileWriter(respondFile))));
			for (ResponseEntry entry : responses) {
				out.println(entry);
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
