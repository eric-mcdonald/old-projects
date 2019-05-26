package org.bitbucket.lanius.cmd.impl;

import java.util.UUID;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.cmd.WrongGameModeException;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.InventoryUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class GodmodeMobCommand extends ModCommand {

	public GodmodeMobCommand() {
		super("godmodemob", "godmob", "mob");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "[amount]";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		if (!Lanius.mc.player.capabilities.isCreativeMode) {
			throw new WrongGameModeException("Creative");
		}
		ItemStack prevStack = Lanius.mc.player.inventoryContainer.getSlot(InventoryUtils.HOTBAR_BEGIN).getStack();
		int amount = args.length >= 1 ? Integer.parseInt(args[0]) : 1;
		ItemStack newStack = new ItemStack(Items.SPAWN_EGG, amount);
		String attrName = "generic.attackDamage";
		NBTTagCompound nanCompound = new NBTTagCompound();
		NBTTagCompound entityTag = new NBTTagCompound();
		AbstractAttributeMap attrMap = new AttributeMap();
		attrMap.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		Multimap<String, AttributeModifier> attrMods = HashMultimap.create();
		attrMods.put(attrName, new AttributeModifier(UUID.randomUUID(), attrName, Double.NaN, 0));
		attrMap.applyAttributeModifiers(attrMods);
		entityTag.setString("id", "minecraft:zombie");
		entityTag.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(attrMap));
		nanCompound.setTag("EntityTag", entityTag);
		newStack.setTagCompound(nanCompound);
		Lanius.mc.playerController.sendSlotPacket(newStack, InventoryUtils.HOTBAR_BEGIN);
		InventoryUtils.putStackInHotbar(newStack);
		InventoryUtils.clickWindow(InventoryUtils.HOTBAR_BEGIN, 1, ClickType.THROW);
		InventoryUtils.putStackInHotbar(prevStack);
		CommandUtils.addText(sender,
				"Given the player a stack of " + amount + " zombie spawn egg with NaN attack damage.");
	}

}
