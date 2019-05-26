package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class NoOverlaysRoutine extends TabbedRoutine {

	private PotionEffect prevBlindness, prevNausea;
	private int prevHurtTime;

	public NoOverlaysRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.RENDER);
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
		return "Removes unwanted effects such as the hurt camera, blindness, nausea, water, fire, pumpkin blur, and block textures.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "No Overlays";
	}

	@SubscribeEvent
	public void onFovModifier(final EntityViewRenderEvent.FOVModifier fovModifierEv) {
		final Entity entity = fovModifierEv.getEntity();
		if (!(entity instanceof EntityLivingBase)) {
			return;
		}
		final EntityLivingBase entityLiving = (EntityLivingBase) entity;
		if (getBoolean("Nausea") && entityLiving.isPotionActive(MobEffects.NAUSEA)) {
			prevNausea = entityLiving.getActivePotionEffect(MobEffects.NAUSEA);
			entityLiving.removeActivePotionEffect(MobEffects.NAUSEA);
		}
		if (getBoolean("Hurt Camera") && entityLiving.hurtTime > 0 && prevHurtTime == 0) {
			prevHurtTime = entityLiving.hurtTime;
			entityLiving.hurtTime = 0;
		}
	}

	@SubscribeEvent
	public void onRenderBlockOverlay(final RenderBlockOverlayEvent renderBlockOverlayEv) {
		final RenderBlockOverlayEvent.OverlayType overlayType = renderBlockOverlayEv.getOverlayType();
		if (overlayType.equals(RenderBlockOverlayEvent.OverlayType.BLOCK) && getBoolean("Block")
				|| overlayType.equals(RenderBlockOverlayEvent.OverlayType.FIRE) && getBoolean("Fire")
				|| overlayType.equals(RenderBlockOverlayEvent.OverlayType.WATER) && getBoolean("Water")) {
			renderBlockOverlayEv.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onRenderGameOverlayPre(final RenderGameOverlayEvent.Pre renderGameOverlayPreEv) {
		if (prevBlindness != null) {
			Lanius.mc.player.addPotionEffect(prevBlindness);
			prevBlindness = null;
		}
		if (prevNausea != null) {
			Lanius.mc.player.addPotionEffect(prevNausea);
			prevNausea = null;
		}
		if (getBoolean("Pumpkin Blur")
				&& renderGameOverlayPreEv.getType().equals(RenderGameOverlayEvent.ElementType.HELMET)) {
			final ItemStack helmet = Lanius.mc.player.inventory.armorItemInSlot(3);
			if (InventoryUtils.isStackValid(helmet) && helmet.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
				renderGameOverlayPreEv.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onRenderLivingPre(final RenderLivingEvent.Pre<AbstractClientPlayer> renderLivingEv) {
		final EntityLivingBase entity = renderLivingEv.getEntity();
		if (!entity.equals(Lanius.mc.getRenderViewEntity())) {
			return;
		}
		if (prevHurtTime > 0 && entity.hurtTime == 0) {
			entity.hurtTime = prevHurtTime;
		}
	}

	@SubscribeEvent
	public void onRenderTick(final TickEvent.RenderTickEvent renderTickEv) {
		if (renderTickEv.phase.equals(TickEvent.Phase.START) && getBoolean("Blindness") && Lanius.mc.player != null
				&& Lanius.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
			prevBlindness = Lanius.mc.player.getActivePotionEffect(MobEffects.BLINDNESS);
			Lanius.mc.player.removeActivePotionEffect(MobEffects.BLINDNESS);
		} else if (renderTickEv.phase.equals(TickEvent.Phase.END)) {
			prevHurtTime = 0;
		}
	}

	@Override
	public void registerValues() {
		registerValue("Hurt Camera", true,
				"Determines whether or not to prevent the camera from shaking when the player is damaged.");
		registerValue("Blindness", true, "Determines whether or not to prevent blindness from rendering.");
		registerValue("Nausea", true, "Determines whether or not to prevent nausea from rendering.");
		registerValue("Pumpkin Blur", true,
				"Determines whether or not to prevent the blur effect when the player equips a pumpkin.");
		registerValue("Block", true,
				"Determines whether or not to prevent the block texture from rendering while the player is inside a block.");
		registerValue("Water", true, "Determines whether or not to prevent the underwater overlay from rendering.");
		registerValue("Fire", true, "Determines whether or not to prevent the fire overlay from rendering.");
	}

}
