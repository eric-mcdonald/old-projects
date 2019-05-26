package com.example.examplemod;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemTool;
import net.minecraft.item.Item;

public class AutoreloadRoutine extends Routine {
	private final Map<Item, Integer> maxAmmoMap = new HashMap<Item, Integer>();
	
	AutoreloadRoutine() {
		super("Auto-reload", Keyboard.KEY_F, false);
		// TODO Auto-generated constructor stub
	}

	void postUpdate() {
		if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemHoe || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemTool) && (!maxAmmoMap.containsKey(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem()) || maxAmmoMap.get(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem()) < Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().stackSize)) {
			maxAmmoMap.put(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem(), Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().stackSize);
		}
		for (final Entity e : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (!(e instanceof EntityLivingBase)) {
				continue;
			}
			final EntityLivingBase enemy = (EntityLivingBase) e;
			if (enemy.isEntityAlive() && enemy.getHealth() > 0.0F && (e instanceof EntityMob || e instanceof EntityWolf) && Minecraft.getMinecraft().thePlayer.canEntityBeSeen(e)) {
				return;
			}
		}
		if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemHoe || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemTool) && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().stackSize < maxAmmoMap.get(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem()) && !Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().isItemDamaged()) {
			Minecraft.getMinecraft().thePlayer.swingItem();
		}
	}
	void cleanup() {
		maxAmmoMap.clear();
	}
}
