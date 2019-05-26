package org.bitbucket.lanius.routine;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bitbucket.lanius.Lanius;
import org.lwjgl.input.Keyboard;

import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public final class RoutineHandler {

	private final ConcurrentMap<Entity, Long> entityAgeMap = new ConcurrentHashMap<Entity, Long>(); // Eric:
																									// EntityLivingBase#getAge
																									// is
																									// broken
																									// on
																									// base
																									// living
																									// entities
	private long joinTime = -1L;

	public long entityAge(final Entity entity) {
		return entityAgeMap.containsKey(entity) ? (System.currentTimeMillis() - entityAgeMap.get(entity)) / 50L : 0L;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onClientTickLowest(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(Phase.END) || Lanius.mc.world == null) {
			return;
		}
		final Iterator<Entry<Entity, Long>> entityAgeIt = entityAgeMap.entrySet().iterator();
		while (entityAgeIt.hasNext()) {
			if (!Lanius.mc.world.loadedEntityList.contains(entityAgeIt.next().getKey())) {
				entityAgeIt.remove();
			}
		}
		if (joinTime == -1L) {
			joinTime = System.currentTimeMillis();
		}
	}

	@SubscribeEvent
	public void onKeyInput(final InputEvent.KeyInputEvent keyInEv) {
		if (Keyboard.getEventKeyState()) {
			final int evKey = Keyboard.getEventKey() == Keyboard.KEY_NONE
					? Keyboard.getEventCharacter() + Keyboard.KEYBOARD_SIZE
					: Keyboard.getEventKey();
			for (final Routine routine : Lanius.getInstance().getRoutineRegistry().objects()) {
				if (evKey == routine.keyBind.getKeyCode()) {
					routine.setEnabled();
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdateLowest(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		if (!entityAgeMap.containsKey(livingUpdateEv.getEntity())) {
			entityAgeMap.put(livingUpdateEv.getEntity(), System.currentTimeMillis());
		}
	}

	@SubscribeEvent
	public void onRenderWorldLast(final RenderWorldLastEvent renderWorldLastEv) {
		final float partialTicks = renderWorldLastEv.getPartialTicks();
		for (final Entity entity : Lanius.mc.world.loadedEntityList) {
			for (final Renderable render : Lanius.getInstance().getRenderRegistry().objects()) {
				if (render instanceof Routine && ((Routine) render).isEnabled()) {
					render.renderEsp(entity, partialTicks);
				}
			}
		}
	}

	@SubscribeEvent
	public void onUnload(final WorldEvent.Unload unloadEv) {
		entityAgeMap.clear();
		joinTime = -1L;
	}

	public long timeSinceJoin() {
		return joinTime == -1L ? 0L : System.currentTimeMillis() - joinTime;
	}

}
