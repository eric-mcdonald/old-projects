package org.bitbucket.lanius.hook;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.hook.impl.CollisionData;
import org.bitbucket.lanius.hook.impl.CollisionHook;
import org.bitbucket.lanius.hook.impl.DoRenderData;
import org.bitbucket.lanius.hook.impl.FrustumData;
import org.bitbucket.lanius.hook.impl.ItemValidData;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.hook.impl.NetHandlerHook;
import org.bitbucket.lanius.hook.impl.OutlineModeData;
import org.bitbucket.lanius.hook.impl.RenderEffectData;
import org.bitbucket.lanius.hook.impl.SlowdownData;
import org.bitbucket.lanius.hook.impl.SoulSandData;
import org.bitbucket.lanius.hook.impl.TabOverlayData;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.routine.impl.FreecamRoutine;
import org.bitbucket.lanius.routine.impl.NameProtectRoutine;
import org.bitbucket.lanius.routine.impl.SpeedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.registry.Registry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public final class HookManager<V, T extends HookData<?, V>> {
	public static final HookManager<AxisAlignedBB, CollisionData> collisionManager = new HookManager<AxisAlignedBB, CollisionData>();

	public static final HookManager<Boolean, DoRenderData> doRenderManager = new HookManager<Boolean, DoRenderData>();

	public static final HookManager<Boolean, FrustumData> frustumManager = new HookManager<Boolean, FrustumData>();
	public static final HookManager<Boolean, ItemValidData> itemValidManager = new HookManager<Boolean, ItemValidData>();

	public static final NetHandlerHook netHook;

	public static final HookManager<Integer, OutlineModeData> outlineManager = new HookManager<Integer, OutlineModeData>();

	public static final HookManager<Packet, NetHandlerData> packetManager = new HookManager<Packet, NetHandlerData>();
	public static final HookManager<Boolean, RenderEffectData> renderEffManager = new HookManager<Boolean, RenderEffectData>();
	public static final HookManager<float[], SlowdownData> slowdownManager = new HookManager<float[], SlowdownData>();
	public static final HookManager<Double, SoulSandData> soulSandManager = new HookManager<Double, SoulSandData>();
	public static final HookManager<String, TabOverlayData> tabManager = new HookManager<String, TabOverlayData>();
	static {
		final Lanius instance = Lanius.getInstance();
		final Registry<Routine> routineRegistry = instance.getRoutineRegistry();
		// Eric: the order of hooks in the list is also the order of execution
		collisionManager.addHook(new CollisionHook());
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Step"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Kill Aura"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Entity Launcher"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Nuker"));
		packetManager.addHook(instance.getCmdHandler());
		packetManager.addHook(netHook = new NetHandlerHook());
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Jesus"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Regen"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Speedy Gonzales"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Flight"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("No Fall"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Long Jump"));
		final SpeedRoutine speedRoutine = (SpeedRoutine) routineRegistry.get("Speed");
		packetManager.addHook(speedRoutine.velNetHook);
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Velocity"));
		final NameProtectRoutine protectRoutine = (NameProtectRoutine) routineRegistry.get("Name Protect");
		tabManager.addHook(protectRoutine);
		packetManager.addHook(protectRoutine.netHook);
		final FreecamRoutine freecamRoutine = (FreecamRoutine) routineRegistry.get("Freecam");
		packetManager.addHook(freecamRoutine.netHook);
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Sneak"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Elytra"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Auto-use"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("No Swing"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Item Spoof"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("No Weather"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Retard"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Blink"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Auto-respond"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Spam"));
		packetManager.addHook(speedRoutine.netHook);
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Back"));
		packetManager.addHook((Hook<NetHandlerData>) routineRegistry.get("Waypoints"));
		// packetManager.addHook((Hook<NetHandlerData>)
		// routineRegistry.get("Boat"));
		frustumManager.addHook(freecamRoutine);
		itemValidManager.addHook((Hook<ItemValidData>) routineRegistry.get("Creative Armor"));
		slowdownManager.addHook(speedRoutine);
		soulSandManager.addHook(speedRoutine.soulSandHook);
		renderEffManager.addHook(Lanius.getInstance().getGuiHandler());
		outlineManager.addHook((Hook<OutlineModeData>) routineRegistry.get("Tracers"));
		doRenderManager.addHook((Hook<DoRenderData>) routineRegistry.get("No Render"));
		// Eric: unsorted test hooks go here
		// packetManager.addHook((Hook<NetHandlerData>)
		// routineRegistry.get("Sign Length"));
	}

	public static AxisAlignedBB executeCollision(final Block block, IBlockState state, IBlockAccess worldIn,
			BlockPos pos) {
		return collisionManager.execute(new CollisionData(block, state, worldIn, pos), Phase.START);
	}

	private final List<Hook<T>> hooks = new ArrayList<Hook<T>>();

	public boolean addHook(final Hook<T> hook) {
		return hooks.add(hook);
	}

	public V execute(final T data, final Phase phase) {
		V retVal = data.retVal;
		for (final Hook hook : hooks) {
			hook.onExecute(data, phase);
			retVal = data.retVal; // Eric: for in case data#retVal is changed
		}
		return retVal;
	}

	public boolean removeHook(final Hook<T> hook) {
		return hooks.remove(hook);
	}

}
