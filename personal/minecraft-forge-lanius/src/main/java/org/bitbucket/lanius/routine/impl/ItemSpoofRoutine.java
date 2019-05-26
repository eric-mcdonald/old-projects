package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.network.play.client.CPacketHeldItemChange;

public final class ItemSpoofRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private boolean sendHeld;

	public ItemSpoofRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.PLAYER);
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
		return "Prevents held item change packets from sending.";
	}

	@Override
	public void init() {
		if (sendHeld && Lanius.mc.player != null) {
			Lanius.mc.player.connection.sendPacket(new CPacketHeldItemChange(Lanius.mc.player.inventory.currentItem));
		}
		sendHeld = false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Item Spoof";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (isEnabled() && phase.equals(Phase.START) && data.retVal instanceof CPacketHeldItemChange) {
			data.retVal = null;
			sendHeld = true;
		}
	}
}
