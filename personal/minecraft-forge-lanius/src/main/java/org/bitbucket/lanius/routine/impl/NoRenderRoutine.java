package org.bitbucket.lanius.routine.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.DoRenderData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class NoRenderRoutine extends TabbedRoutine implements Hook<DoRenderData> {

	private final List<EntityItem> deadItems = new ArrayList<EntityItem>();

	public NoRenderRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.RENDER);
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
		return "Prevents animals, items, and monsters from rendering.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "No Render";
	}

	@Override
	public void onExecute(final DoRenderData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!isEnabled() || !phase.equals(Phase.START) || !getBoolean("Items")
				|| !(data.source instanceof RenderEntityItem)) {
			return;
		}
		data.retVal = true;
	}

	@SubscribeEvent
	public void onRenderLivingPre(final RenderLivingEvent.Pre<EntityLivingBase> renderLivingPreEv) {
		final EntityLivingBase entity = renderLivingPreEv.getEntity();
		if (getBoolean("Animals") && entity instanceof EntityAnimal
				|| getBoolean("Mobs") && entity instanceof EntityMob) {
			renderLivingPreEv.setCanceled(true);
		}
	}

	@Override
	public void registerValues() {
		registerValue("Animals", false, "Determines whether or not to prevent animals from rendering.");
		registerValue("Items", true, "Determines whether or not to prevent items from rendering.");
		registerValue("Mobs", false, "Determines whether or not to prevent mobs from rendering.");
	}

}
