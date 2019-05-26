package org.bitbucket.lanius.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.util.InventoryUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.server.MinecraftServer;

public class HorriblePotCommand extends ModCommand {

	public HorriblePotCommand() {
		super("horriblepotiontest", "horriblepotion", "potion", "pot");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		ItemStack crashStack = new ItemStack(Items.SPLASH_POTION);
		NBTTagCompound crashTag = new NBTTagCompound();
		NBTTagList customEffects = new NBTTagList();
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		// PotionEffect effect = new PotionEffect(MobEffects.LEVITATION,
		// Integer.MAX_VALUE, 127, false, true);
		// effects.add(effect);
		// effects.add(new PotionEffect(MobEffects., Integer.MAX_VALUE, 127, false,
		// true))
		Iterator<Potion> potIt = Potion.REGISTRY.iterator();
		while (potIt.hasNext()) {
			Potion pot = potIt.next();
			if (!pot.isBadEffect()) {
				continue;
			}
			PotionEffect effect = new PotionEffect(pot, Integer.MAX_VALUE, 127, false, true);
			effects.add(effect);
		}
		PotionUtils.appendEffects(crashStack, effects);
		/*
		 * for (final EntityEquipmentSlot equipSlot : EntityEquipmentSlot.values()) {
		 * for (IAttributeInstance attr :
		 * Lanius.mc.player.getAttributeMap().getAllAttributes()) { for (int operation =
		 * 0; operation <= 2; operation++) {
		 * crashStack.addAttributeModifier(attr.getAttribute().getName(), new
		 * AttributeModifier( UUID.randomUUID(), attr.getAttribute().getName(),
		 * Double.NaN, operation), equipSlot); } } }
		 */
		Lanius.mc.playerController.sendSlotPacket(crashStack,
				InventoryUtils.HOTBAR_BEGIN + Lanius.mc.player.inventory.currentItem);
	}

}
