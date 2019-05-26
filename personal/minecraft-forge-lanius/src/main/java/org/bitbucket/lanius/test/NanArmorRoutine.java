package org.bitbucket.lanius.test;

import java.util.HashSet;
import java.util.UUID;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class NanArmorRoutine extends TabbedRoutine {

	public NanArmorRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.WORLD);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		return TEST_COLOR;
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Rapidly spawns diamond_chestplates with NaN generic.movementSpeed. Do not pick up the chestplates!";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "NaN Armor";
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP
				|| !Lanius.mc.player.capabilities.isCreativeMode) {
			return;
		}
		final ItemStack nanStack = new ItemStack(Items.DIAMOND_CHESTPLATE);
		final String ATTR_NAME = "generic.movementSpeed";
		for (final EntityEquipmentSlot equipSlot : EntityEquipmentSlot.values()) {
			nanStack.addAttributeModifier(ATTR_NAME, new AttributeModifier(UUID.randomUUID(), ATTR_NAME, Double.NaN, 0),
					equipSlot);
		}
		// Lanius.mc.player.inventoryContainer.putStackInSlot(InventoryUtils.HOTBAR_BEGIN
		// + Lanius.mc.player.inventory.currentItem, nanStack);
		Lanius.mc.playerController.sendSlotPacket(nanStack,
				InventoryUtils.HOTBAR_BEGIN + Lanius.mc.player.inventory.currentItem);
		Lanius.mc.player.dropItem(nanStack.getCount() > 1);
	}

}
