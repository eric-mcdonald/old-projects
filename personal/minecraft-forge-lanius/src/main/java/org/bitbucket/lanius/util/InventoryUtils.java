package org.bitbucket.lanius.util;

import java.util.UUID;

import org.bitbucket.lanius.Lanius;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public final class InventoryUtils {
	public static final int HOTBAR_BEGIN = 36, INIT_SLOT = -1, MIN_SLOT = 9, MAX_SLOT = 45;

	public static void addAllNanAttributes(ItemStack stack) {
		if (!isStackValid(stack)) {
			return;
		}
		for (IAttributeInstance attribute : Lanius.mc.player.getAttributeMap().getAllAttributes()) {
			for (final EntityEquipmentSlot equipSlot : EntityEquipmentSlot.values()) {
				for (int operation = 0; operation <= 2; operation++) {
					addNanAttribute(stack, equipSlot, attribute, operation);
				}
			}
		}
	}

	public static void addNanAttribute(final ItemStack stack, EntityEquipmentSlot equipSlot,
			IAttributeInstance attribute, int operation) {
		if (!isStackValid(stack) || equipSlot == null || attribute == null || operation < 0 || operation > 2) {
			return;
		}
		stack.addAttributeModifier(attribute.getAttribute().getName(),
				new AttributeModifier(UUID.randomUUID(), attribute.getAttribute().getName(), Double.NaN, operation),
				equipSlot);
	}

	public static void clickWindow(final int slot, final int mouseBtn, final ClickType type) {
		ensureInventory();
		Lanius.mc.playerController.windowClick(Lanius.mc.player.inventoryContainer.windowId, slot, mouseBtn, type,
				Lanius.mc.player);
	}

	public static void ensureInventory() {
		if (Lanius.mc.player.openContainer != Lanius.mc.player.inventoryContainer) {
			Lanius.mc.player.closeScreen();
		}
	}

	public static boolean hasArmor(final EntityPlayer player) {
		for (int armorIdx = 0; armorIdx < player.inventory.armorInventory.size(); armorIdx++) {
			if (isStackValid(player.inventory.armorInventory.get(armorIdx))) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasHeldItem(final EntityPlayer player) {
		boolean holdingItem = isStackValid(player.getHeldItemMainhand());
		if (!RoutineUtils.viaVersionEnabled()) {
			holdingItem |= isStackValid(player.getHeldItemOffhand());
		}
		return holdingItem;
	}

	public static boolean isStackValid(ItemStack stack) {
		return stack != null && stack != ItemStack.EMPTY && stack.getItem() != Items.AIR;
	}

	public static void putStackInHotbar(final ItemStack stack) {
		if (!Lanius.mc.player.capabilities.isCreativeMode) {
			return;
		}
		Lanius.mc.player.inventoryContainer.putStackInSlot(HOTBAR_BEGIN, stack);
		Lanius.mc.playerController.sendSlotPacket(stack, HOTBAR_BEGIN);
	}

	public static void switchItem(final int newItem) {
		if (newItem != Lanius.mc.player.inventory.currentItem) {
			Lanius.mc.player.inventory.currentItem = newItem;
			Lanius.mc.player.connection.sendPacket(new CPacketHeldItemChange(Lanius.mc.player.inventory.currentItem));
		}
	}

	/**
	 * Sends an invalid ClickWindow packet with unmatching ItemStacks to force the
	 * server to resend the player's inventory.
	 */
	public static void syncInventory() {
		ensureInventory();
		int slotId;
		for (slotId = MIN_SLOT; slotId < MAX_SLOT; slotId++) {
			if (Lanius.mc.player.inventoryContainer.getSlot(slotId).getHasStack()) {
				break;
			}
		}
		if (slotId < MAX_SLOT) {
			Lanius.mc.player.connection.sendPacket(new CPacketClickWindow(Lanius.mc.player.openContainer.windowId, -1,
					0, ClickType.QUICK_MOVE, Lanius.mc.player.openContainer.getSlot(slotId).getStack(),
					Lanius.mc.player.openContainer.getNextTransactionID(Lanius.mc.player.inventory)));
		}
	}
}
