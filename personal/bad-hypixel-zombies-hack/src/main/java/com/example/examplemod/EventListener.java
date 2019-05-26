package com.example.examplemod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.registry.GameData;

public final class EventListener {
	private boolean masterEnabled;
	private final ArrayList<Routine> routines = new ArrayList<Routine>();
	private static final Comparator<Routine> nameCmp = new Comparator<Routine>() {

		@Override
		public int compare(Routine arg0, Routine arg1) {
			// TODO Auto-generated method stub
			final int result = String.CASE_INSENSITIVE_ORDER.compare(arg0.name, arg1.name);
			return result == 0 ? arg0.name.compareTo(arg1.name) : result;
		}
		
	};

	public EventListener() {
		routines.add(new SprintRoutine());
		routines.add(new BunnyhopRoutine());
		routines.add(new AimbotRoutine());
		routines.add(new AutoreloadRoutine());
		routines.add(new FullbrightRoutine());
		routines.add(new PlayerTracersRoutine());
	}
	
	void cleanup() {
		for (final Routine r : routines) {
			r.cleanup();
		}
	}
	@SubscribeEvent
	public void onKeyInput(final InputEvent.KeyInputEvent event) {
		if (Keyboard.getEventKeyState()) {
			final int evKey = Keyboard.getEventKey() == Keyboard.KEY_NONE ? Keyboard.getEventCharacter() + Keyboard.KEYBOARD_SIZE : Keyboard.getEventKey();
			if (evKey == Keyboard.KEY_V) {
				masterEnabled = !masterEnabled;
				if (!masterEnabled) {
					cleanup();
				}
			} else if (masterEnabled) {
				for (final Routine r : routines) {
					if (evKey == r.key) {
						r.enabled = !r.enabled;
						if (!r.enabled) {
							r.cleanup();
						}
					}
				}
			}
		}
	}
	@SubscribeEvent
	public void onRenderGameOverlayText(final RenderGameOverlayEvent.Text event) {
		if (masterEnabled && event.left.isEmpty() && event.right.isEmpty()) {
			int y = 2;
			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Shitty Hypixel Zombies Hack", 2, y, 0xFFFFFF);
			Collections.sort(routines, nameCmp);
			for (final Routine r : routines) {
				y += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(Keyboard.getKeyName(r.key) + ": " + r.name, 2, y, r.enabled ? 0x00FF00 : 0xFF0000);
			}
		}
	}
	@SubscribeEvent
	public void onClientTick(final ClientTickEvent event) {
		if (event.phase.equals(Phase.START)) {
			if (Minecraft.getMinecraft().thePlayer != null) {
				if (!masterEnabled) {
					return;
				}
				for (final Routine r : routines) {
					if (!r.enabled) {
						continue;
					}
					r.preUpdate();
				}
			}
		} else {
			if (Minecraft.getMinecraft().thePlayer != null) {
				if (!masterEnabled) {
					return;
				}
				for (final Routine r : routines) {
					if (!r.enabled) {
						continue;
					}
					r.postUpdate();
				}
			} else {
				cleanup();
				masterEnabled = false;
			}
		}
	}
	@SubscribeEvent
	public void onRenderWorldLast(final RenderWorldLastEvent event) {
		if (masterEnabled) {
			for (final Routine r : routines) {
				if (!r.enabled) {
					continue;
				}
				r.render3d(event.partialTicks);
			}
		}
	}
}
