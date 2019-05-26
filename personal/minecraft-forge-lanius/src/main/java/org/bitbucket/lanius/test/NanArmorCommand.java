package org.bitbucket.lanius.test;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.util.InventoryUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public final class NanArmorCommand extends ModCommand {

	public NanArmorCommand() {
		super("nanarmor", "nan");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		final ItemStack nanStack = new ItemStack(Items.SPAWN_EGG);
		final String ATTR_NAME = "generic.attackDamage";
		NBTTagCompound nanCompound = new NBTTagCompound();
		NBTTagCompound entityTag = new NBTTagCompound();
		/*
		 * AbstractAttributeMap attrMap = new AttributeMap();
		 * attrMap.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		 * Multimap<String, AttributeModifier> attrMods = HashMultimap.create();
		 * attrMods.put(ATTR_NAME, new AttributeModifier(UUID.randomUUID(), ATTR_NAME,
		 * Double.NaN, 0)); attrMap.applyAttributeModifiers(attrMods);
		 */
		entityTag.setString("id", "minecraft:pig");
		// entityTag.setTag("Attributes",
		// SharedMonsterAttributes.writeBaseAttributeMapToNBT(attrMap));
		/*
		 * NBTTagList motionList = new NBTTagList(); for (int i = 0; i < 3; i++) {
		 * motionList.appendTag(new NBTTagDouble(1.0D)); }
		 */
		// entityTag.setTag("Motion", motionList);
		entityTag.setBoolean("Leashed", true);
		nanCompound.setTag("EntityTag", entityTag);
		nanStack.setTagCompound(nanCompound);
		// for (final EntityEquipmentSlot equipSlot : EntityEquipmentSlot.values()) {
		// nanStack.addAttributeModifier(ATTR_NAME, new
		// AttributeModifier(UUID.randomUUID(), ATTR_NAME, 0.0, 0),
		// equipSlot);
		// }
		// Lanius.mc.player.inventoryContainer.putStackInSlot(InventoryUtils.HOTBAR_BEGIN
		// + Lanius.mc.player.inventory.currentItem, nanStack);
		Lanius.mc.playerController.sendSlotPacket(nanStack,
				InventoryUtils.HOTBAR_BEGIN + Lanius.mc.player.inventory.currentItem);
		Lanius.mc.player.dropItem(nanStack.getCount() > 1);
	}

}
