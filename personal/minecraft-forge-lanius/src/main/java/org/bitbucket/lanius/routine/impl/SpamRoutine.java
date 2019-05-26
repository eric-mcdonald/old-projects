package org.bitbucket.lanius.routine.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.Configurable;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class SpamRoutine extends TabbedRoutine implements Hook<NetHandlerData>, Configurable {

	private static final File spamFile = new File(Lanius.dataDir, "spam.cfg");

	private boolean allowChat;

	private List<String> captchas;

	private long lastMsgTime, lastMoveTime;

	private final List<String> lines = new CopyOnWriteArrayList<String>();

	private double prevX, prevY, prevZ;

	private float prevYaw, prevPitch;

	private int spamIdx;

	public SpamRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.MISCELLANEOUS);
		// TODO Auto-generated constructor stub
	}

	public boolean addLine(final String line) {
		return lines.add(line);
	}

	public void clearLines() {
		lines.clear();
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Spams the specified chat message.";
	}

	@Override
	public void init() {
		if (captchas == null) {
			captchas = new ArrayList<String>();
		} else {
			captchas.clear();
		}
		spamIdx = 0;
	}

	public boolean isAllowChat() {
		return isEnabled() && allowChat;
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		clearLines();
		if (!spamFile.exists()) {
			addLine("I am using Lanius.");
			addLine("NoCheatPlus-compatible cheats for Minecraft Forge.");
			addLine("https://bitbucket.org/eric_ptr/minecraft-forge-lanius");
			return;
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(spamFile));
			String line;
			try {
				while ((line = in.readLine()) != null) {
					addLine(line);
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

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Spam";
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onClientTickLowest(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || Lanius.mc.isGamePaused()
				|| !Lanius.mc.player.isEntityAlive()) {
			return;
		}
		final long TIMEOUT = 8000L;
		if (System.currentTimeMillis() - lastMsgTime > getInt("Delay").intValue()
				&& (Lanius.getInstance().getRoutineHandler().timeSinceJoin() >= TIMEOUT
						|| !RoutineUtils.ncpEnabled())) {
			if (lines.isEmpty()) {
				return;
			}
			String message = lines.get(spamIdx);
			if (captchas.isEmpty()) {
				++spamIdx;
				if (spamIdx >= lines.size()) {
					spamIdx = 0;
				}
			} else {
				final int CAPTCHA_IDX = 0;
				message = captchas.get(CAPTCHA_IDX);
				captchas.remove(CAPTCHA_IDX);
			}
			if (RoutineUtils.ncpEnabled()) {
				if (System.currentTimeMillis() - lastMoveTime > TIMEOUT - 50L * 2L - NetworkUtils.lagTime()) {
					final double contractVec = 0.0625D;
					double yOffset = 0.0626D;
					final AxisAlignedBB entityBox = Lanius.mc.player.getEntityBoundingBox();
					final List<AxisAlignedBB> collisionBoxes = Lanius.mc.player.world.getCollisionBoxes(
							Lanius.mc.player,
							entityBox.grow(-contractVec, 0.0D, -contractVec).expand(0.0D, yOffset, 0.0D));
					for (int boxIdx = 0; boxIdx < collisionBoxes.size(); boxIdx++) {
						yOffset = collisionBoxes.get(boxIdx).calculateYOffset(entityBox, yOffset);
					}
					// Eric: using NetworkManager#sendPacket to bypass the
					// hooking system
					Lanius.mc.player.connection.getNetworkManager().sendPacket(new CPacketPlayer.Position(
							Lanius.mc.player.posX, entityBox.minY + yOffset, Lanius.mc.player.posZ, false));
					lastMoveTime = System.currentTimeMillis();
				}
			}
			allowChat = true;
			Lanius.mc.player.sendChatMessage(message);
			allowChat = false;
		}
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.END)) {
			return;
		}
		if (NetworkUtils.motionPacket(data.retVal)) {
			final CPacketPlayer playerPacket = (CPacketPlayer) data.retVal;
			if ((Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket,
					"field_149480_h", "moving")
					&& Math.pow(prevX - playerPacket.getX(0.0D), 2.0D) + Math.pow(prevY - playerPacket.getY(0.0D), 2.0D)
							+ Math.pow(prevZ - playerPacket.getZ(0.0D), 2.0D) > 0.00390625D
					|| (Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket,
							"field_149481_i", "rotating")
							&& Math.abs(prevYaw - playerPacket.getYaw(0.0F))
									+ Math.abs(prevPitch - playerPacket.getPitch(0.0F)) > 10.0F) {
				lastMoveTime = System.currentTimeMillis();
				if ((Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket,
						"field_149480_h", "moving")) {
					prevX = playerPacket.getX(0.0D);
					prevY = playerPacket.getY(0.0D);
					prevZ = playerPacket.getZ(0.0D);
				}
				if ((Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket,
						"field_149481_i", "rotating")) {
					prevYaw = playerPacket.getYaw(0.0F);
					prevPitch = playerPacket.getPitch(0.0F);
				}
			}
		} else if (data.retVal instanceof CPacketChatMessage) {
			lastMsgTime = System.currentTimeMillis();
		} else if (data.retVal instanceof SPacketChat && isEnabled()) {
			final String unformattedMsg = ((SPacketChat) data.retVal).getChatComponent().getUnformattedText(),
					MATCH_START = "Please type '", MATCH_END = "' to continue sending messages/commands.";
			if (unformattedMsg.startsWith(MATCH_START) && unformattedMsg.endsWith(MATCH_END)) {
				captchas.add(unformattedMsg.substring(unformattedMsg.indexOf(MATCH_START) + MATCH_START.length(),
						unformattedMsg.indexOf(MATCH_END)));
			}
		}
	}

	@Override
	@SubscribeEvent
	public void onUnload(final WorldEvent.Unload unloadEv) {
		super.onUnload(unloadEv);
		lastMsgTime = 0L;
		lastMoveTime = 0L;
		prevX = prevY = prevZ = 0.0D;
		prevYaw = prevPitch = 0.0F;
	}

	@Override
	public void registerValues() {
		registerValue("Delay", 1000, 0, 10000, "Specifies how long to wait before sending another chat message.");
	}

	public String removeLine(final int lineIdx) {
		return lines.remove(lineIdx);
	}

	@Override
	public void save() {
		spamFile.delete();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter((new FileWriter(spamFile))));
			for (final String line : lines) {
				out.println(line);
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
