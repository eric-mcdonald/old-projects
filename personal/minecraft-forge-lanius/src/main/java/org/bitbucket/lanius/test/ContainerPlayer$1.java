package org.bitbucket.lanius.test;

import javax.annotation.Nullable;

import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.ItemValidData;
import org.bitbucket.lanius.util.Phase;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

class ContainerPlayer$1 extends Slot {
	ContainerPlayer$1(ContainerPlayer this$0, IInventory inventoryIn, int index, int xPosition, int yPosition,
			EntityEquipmentSlot paramEntityEquipmentSlot) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		ItemStack itemstack = getStack();
		return (!itemstack.isEmpty()) && (!playerIn.isCreative()) && (EnchantmentHelper.hasBindingCurse(itemstack))
				? false
				: super.canTakeStack(playerIn);
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	@Nullable
	@SideOnly(Side.CLIENT)
	public String getSlotTexture() {
		return net.minecraft.item.ItemArmor.EMPTY_SLOT_NAMES[0];
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return HookManager.itemValidManager
				.execute(new ItemValidData(this, stack, stack.getItem().isValidArmor(stack, null, null)), Phase.START);
	}
}
