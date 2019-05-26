package org.bitbucket.lanius.routine.impl;

import static org.lwjgl.opengl.GL11.GL_POLYGON_OFFSET_FILL;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonOffset;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class WallhackRoutine extends TabbedRoutine {

	public WallhackRoutine() {
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
		return "Renders entities through walls.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Wallhack";
	}

	@SubscribeEvent
	public void onRenderLivingPost(final RenderLivingEvent.Post<EntityLivingBase> renderLivingPostEv) {
		final EntityLivingBase entity = renderLivingPostEv.getEntity();
		if ((getBoolean("Animals") && entity instanceof EntityAnimal
				|| getBoolean("Mobs") && entity instanceof EntityMob
				|| getBoolean("Players") && entity instanceof EntityOtherPlayerMP
						&& !entity.equals(((FreecamRoutine) Lanius.getInstance().getRoutineRegistry().get("Freecam"))
								.getRenderEntity())
						&& !entity.equals(((BlinkRoutine) Lanius.getInstance().getRoutineRegistry().get("Blink"))
								.getPosEntity()))) {
			if (!Lanius.mc.player.canEntityBeSeen(entity)) {
				glPolygonOffset(0.0F, 0.0F);
				glDisable(GL_POLYGON_OFFSET_FILL);
			}
			if (getBoolean("Disable Lightmap")) {
				Lanius.mc.entityRenderer.enableLightmap();
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onRenderLivingPreLow(final RenderLivingEvent.Pre<EntityLivingBase> renderLivingPreEv) {
		if (renderLivingPreEv.isCanceled()) {
			return;
		}
		final EntityLivingBase entity = renderLivingPreEv.getEntity();
		if ((getBoolean("Animals") && entity instanceof EntityAnimal
				|| getBoolean("Mobs") && entity instanceof EntityMob
				|| getBoolean("Players") && entity instanceof EntityOtherPlayerMP
						&& !entity.equals(((FreecamRoutine) Lanius.getInstance().getRoutineRegistry().get("Freecam"))
								.getRenderEntity())
						&& !entity.equals(((BlinkRoutine) Lanius.getInstance().getRoutineRegistry().get("Blink"))
								.getPosEntity()))) {
			if (getBoolean("Disable Lightmap")) {
				Lanius.mc.entityRenderer.disableLightmap();
			}
			if (!Lanius.mc.player.canEntityBeSeen(entity)) {
				glPolygonOffset(0.1F, -3000000.0F);
				glEnable(GL_POLYGON_OFFSET_FILL);
			}
		}
	}

	@Override
	public void registerValues() {
		registerValue("Players", true, "Determines whether or not to render players through walls.");
		registerValue("Mobs", false, "Determines whether or not to render mobs through walls.");
		registerValue("Animals", false, "Determines whether or not to render animals through walls.");
		registerValue("Disable Lightmap", true, "Determines whether or not to disable the lightmap.");
	}

}
