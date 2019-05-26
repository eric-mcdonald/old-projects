package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookData;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ItemValidData extends HookData<Slot, Boolean> {
	private ItemStack stack;

	public ItemValidData(Slot source, ItemStack stack, boolean retVal) {
		super(source);
		this.stack = stack;
		this.retVal = retVal;
		// TODO Auto-generated constructor stub
	}

	public ItemStack getStack() {
		return stack;
	}
}
