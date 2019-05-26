package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;

public final class NoFallRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private boolean cancelled;

	public NoFallRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.PLAYER);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Stops the player from taking fall damage.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "No Fall";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (Lanius.mc.player == null) {
			return; // Eric: hotfix for a network crash
		}
		final ItemStack chestStack = Lanius.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if (!phase.equals(Phase.START) || !isEnabled() || !(data.retVal instanceof CPacketPlayer)
				|| Lanius.mc.player.isElytraFlying()
				|| RoutineUtils.enabled("Elytra") && !Lanius.mc.player.capabilities.isFlying
						&& !RoutineUtils.flyEnabled() && !RoutineUtils.noclipEnabled()
						&& InventoryUtils.isStackValid(chestStack) && chestStack.getItem() == Items.ELYTRA
						&& ItemElytra.isUsable(chestStack)
				|| cancelled) {
			return;
		}
		ReflectHelper.setValue(CPacketPlayer.class, (CPacketPlayer) data.retVal, true, "field_149474_g", "onGround");
	}

	void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

}
