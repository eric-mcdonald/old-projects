package org.bitbucket.lanius.hook.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.routine.impl.AutoUseRoutine;
import org.bitbucket.lanius.routine.impl.EntityLauncherRoutine;
import org.bitbucket.lanius.routine.impl.KillAuraRoutine;
import org.bitbucket.lanius.routine.impl.NukerRoutine;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.bitbucket.lanius.util.registry.Registry;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class NetHandlerHook extends ConfigContainer implements Hook<NetHandlerData> {
	public static void sendPlayerPacket(final CPacketPlayer playerPacket) {
		((KillAuraRoutine) Lanius.getInstance().getRoutineRegistry().get("Kill Aura")).setIgnorePlayerPacket();
		((NukerRoutine) Lanius.getInstance().getRoutineRegistry().get("Nuker")).setIgnorePlayerPacket();
		((EntityLauncherRoutine) Lanius.getInstance().getRoutineRegistry().get("Entity Launcher"))
				.setIgnorePlayerPacket();
		Lanius.mc.player.connection.sendPacket(playerPacket);
	}

	private float serverYaw, serverPitch, prevYaw, prevPitch, prevOldYaw, prevOldPitch;
	private CPacketPlayerTryUseItemOnBlock useBeforePacket;
	private int useBlockCount, tabState;
	private boolean useFromCheat, useSent, usingItem, offlineMode = true;
	private long useStartTime;

	@Override
	public String category() {
		// TODO Auto-generated method stub
		return "Network";
	}

	public void forcePlayerPacket() {
		ReflectHelper.setValue(EntityPlayerSP.class, Lanius.mc.player, 20, "field_175168_bP", "positionUpdateTicks");
	}

	public float getServerPitch() {
		return serverPitch;
	}

	public float getServerYaw() {
		return serverYaw;
	}

	public boolean isUseSent() {
		final boolean prevUseSent = useSent;
		useSent = false;
		return prevUseSent;
	}

	public boolean isUsingItem() {
		return usingItem;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onClientTickLowest(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null) {
			return;
		}
		resetRotations(true);
		if (useBeforePacket != null) {
			Lanius.mc.player.connection.sendPacket(useBeforePacket);
			setUseBeforePacket();
		}
		setUseBlockCount();
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		// Eric: null check since NetLoginTransformer does not check this
		if (Lanius.mc.player == null) {
			return;
		}
		final boolean startPhase = phase.equals(Phase.START),
				wasUse = data.retVal instanceof CPacketPlayerTryUseItemOnBlock
						|| data.retVal instanceof CPacketPlayerTryUseItem;
		if (phase.equals(Phase.END)) {
			if (data.retVal instanceof CPacketPlayerTryUseItemOnBlock
					|| data.retVal instanceof CPacketPlayerTryUseItem) {
				usingItem = true;
			} else if (data.retVal instanceof CPacketPlayerDigging
					&& ((CPacketPlayerDigging) data.retVal).getAction()
							.equals(CPacketPlayerDigging.Action.RELEASE_USE_ITEM)
					|| data.retVal instanceof CPacketHeldItemChange) {
				usingItem = false;
			}
		}
		if (startPhase && data.retVal instanceof SPacketTabComplete && tabState == 1) {
			final Set<String> plugins = new HashSet<String>();
			for (final String match : ((SPacketTabComplete) data.retVal).getMatches()) {
				final String[] splitMatch = match.split(":");
				if (splitMatch.length <= 1) {
					continue;
				}
				plugins.add(splitMatch[0].substring(1));
			}
			final Iterator<String> pluginIt = plugins.iterator();
			while (pluginIt.hasNext()) {
				final String plugin = pluginIt.next();
				if (plugin.equals("bukkit") || plugin.equals("minecraft")) {
					pluginIt.remove();
				}
			}
			if (!plugins.isEmpty()) {
				String message = "Server plugins (" + plugins.size() + "): ";
				int pluginCount = 0;
				for (final String plugin : plugins) {
					message += plugin;
					if (pluginCount < plugins.size() - 1) {
						message += ", ";
					}
					++pluginCount;
				}
				CommandUtils.addText(Lanius.mc.player, message);
			}
			if (getBoolean("Auto-configure")) {
				final Registry<Routine> routineRegistry = Lanius.getInstance().getRoutineRegistry();
				if (plugins.contains("nocheatplus") && !RoutineUtils.ncpEnabled()
						|| !plugins.isEmpty() && !plugins.contains("nocheatplus") && RoutineUtils.ncpEnabled()) {
					routineRegistry.get("NoCheatPlus").setEnabled();
				}
				if (plugins.contains("viaversion") && !RoutineUtils.viaVersionEnabled()
						|| !plugins.isEmpty() && !plugins.contains("viaversion") && RoutineUtils.viaVersionEnabled()) {
					routineRegistry.get("ViaVersion").setEnabled();
				}
			}
			data.retVal = null;
			++tabState;
		} else if (startPhase && data.retVal instanceof CPacketPlayer) {
			if (offlineMode && getBoolean("Notify Offline") && !Lanius.mc.isSingleplayer()) {
				CommandUtils.addText(Lanius.mc.player, "The current server is in offline mode.");
				offlineMode = false;
			}
			if (tabState == 0 && getBoolean("Plugin Discovery") && !Lanius.mc.isSingleplayer()) {
				data.source.sendPacket(new CPacketTabComplete("/", null, false));
				++tabState;
			}
			if (tabState == 0 && getBoolean("Auto-configure") && Lanius.mc.isSingleplayer()) {
				if (RoutineUtils.ncpEnabled()) {
					Lanius.getInstance().getRoutineRegistry().get("NoCheatPlus").setEnabled();
				}
				if (RoutineUtils.viaVersionEnabled()) {
					Lanius.getInstance().getRoutineRegistry().get("ViaVersion").setEnabled();
				}
				tabState = 2;
			}
			final CPacketPlayer playerPacket = (CPacketPlayer) data.retVal;
			if ((Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket,
					"field_149481_i", "rotating")) {
				ReflectHelper.setValue(CPacketPlayer.class, playerPacket, serverYaw, "field_149476_e", "yaw");
				ReflectHelper.setValue(CPacketPlayer.class, playerPacket, serverPitch, "field_149473_f", "pitch");
			} else {
				data.retVal = new CPacketPlayer.PositionRotation(playerPacket.getX(Lanius.mc.player.posX),
						playerPacket.getY(Lanius.mc.player.getEntityBoundingBox().minY),
						playerPacket.getZ(Lanius.mc.player.posZ), serverYaw, serverPitch, playerPacket.isOnGround());
			}
		} else if (startPhase && data.retVal instanceof CPacketPlayerTryUseItemOnBlock) {
			if (RoutineUtils.viaVersionEnabled() && useBeforePacket == null && useBlockCount < 2) {
				useBeforePacket = (CPacketPlayerTryUseItemOnBlock) data.retVal;
				data.retVal = null;
				++useBlockCount;
			} else if (useBlockCount >= 2) {
				data.retVal = null;
				setUseBlockCount();
			}
		} else if (startPhase && data.retVal instanceof CPacketPlayerTryUseItem) {
			setUseBeforePacket();
			if (useBlockCount >= 1) {
				++useBlockCount;
			}
		} else if (data.retVal instanceof SPacketPlayerPosLook) {
			if (startPhase) {
				final SPacketPlayerPosLook posLookPacket = (SPacketPlayerPosLook) data.retVal;
				float offYaw = posLookPacket.getYaw(), offPitch = posLookPacket.getPitch();
				if (posLookPacket.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
					offPitch += serverPitch;
				}
				if (posLookPacket.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
					offYaw += serverYaw;
				}
				setServerPitch(offPitch);
				setServerYaw(offYaw);
				prevOldYaw = Lanius.mc.player.prevRotationYaw;
				prevOldPitch = Lanius.mc.player.prevRotationPitch;
				prevYaw = Lanius.mc.player.rotationYaw;
				prevPitch = Lanius.mc.player.rotationPitch;
			} else if (getBoolean("No Rotation Set")) {
				Lanius.mc.player.prevRotationYaw = prevOldYaw;
				Lanius.mc.player.prevRotationPitch = prevOldPitch;
				Lanius.mc.player.rotationYaw = prevYaw;
				Lanius.mc.player.rotationPitch = prevPitch;
			}
		}
		if (startPhase && wasUse && useFromCheat) {
			if (data.retVal instanceof CPacketPlayerTryUseItemOnBlock
					|| data.retVal instanceof CPacketPlayerTryUseItem) {
				final ItemStack heldStack = data.retVal instanceof CPacketPlayerTryUseItemOnBlock
						? Lanius.mc.player.getHeldItem(((CPacketPlayerTryUseItemOnBlock) data.retVal).getHand())
						: Lanius.mc.player.getHeldItem(((CPacketPlayerTryUseItem) data.retVal).getHand());
				if (RoutineUtils.ncpEnabled()
						&& System.currentTimeMillis() - useStartTime < 250L + NetworkUtils.lagTime()
						&& (!InventoryUtils.isStackValid(heldStack) || heldStack.getItem() == Items.ENDER_PEARL
								|| heldStack.getItem() == Items.EGG || heldStack.getItem() == Items.SNOWBALL
								|| ((AutoUseRoutine) Lanius.getInstance().getRoutineRegistry().get("Auto-use"))
										.healthPotion(heldStack, true)
								|| data.retVal instanceof CPacketPlayerTryUseItemOnBlock)) {
					data.retVal = null;
					useSent = false;
				} else {
					if (!InventoryUtils.isStackValid(heldStack) || heldStack.getItem() == Items.ENDER_PEARL
							|| heldStack.getItem() == Items.EGG || heldStack.getItem() == Items.SNOWBALL
							|| ((AutoUseRoutine) Lanius.getInstance().getRoutineRegistry().get("Auto-use"))
									.healthPotion(heldStack, true)
							|| data.retVal instanceof CPacketPlayerTryUseItemOnBlock) {
						useStartTime = System.currentTimeMillis();
					}
					useSent = true;
				}
			} else {
				useSent = true;
			}
			useFromCheat = false;
		}
	}

	@SubscribeEvent
	public void onUnload(final WorldEvent.Unload unloadEv) {
		setUseBeforePacket();
		setUseBlockCount();
		useStartTime = 0L;
		usingItem = false;
		offlineMode = true;
		tabState = 0;
	}

	@Override
	public void registerValues() {
		registerValue("No Rotation Set", true,
				"Determines whether or not to prevent the player's rotations from being reset.");
		registerValue("Notify Offline", true,
				"Determines whether or not to notify you of the server being in offline mode.");
		registerValue("Plugin Discovery", true, "Determines whether or not to try to discover the server's plugins.");
		registerValue("Auto-configure", true, "Determines whether or not to automatically configure " + Lanius.NAME
				+ " if certain plugins are found.");
	}

	public void resetRotations(final boolean forcePacket) {
		if (forcePacket) {
			setServerYaw(Lanius.mc.player.rotationYaw);
			setServerPitch(Lanius.mc.player.rotationPitch);
		} else {
			serverYaw = Lanius.mc.player.rotationYaw;
			serverPitch = Lanius.mc.player.rotationPitch;
		}
	}

	void setOfflineMode() {
		offlineMode = false;
	}

	public void setServerPitch(final float serverPitch) {
		if (this.serverPitch == serverPitch) {
			return;
		}
		this.serverPitch = serverPitch;
		forcePlayerPacket();
	}

	public void setServerYaw(final float serverYaw) {
		if (this.serverYaw == serverYaw) {
			return;
		}
		this.serverYaw = serverYaw;
		forcePlayerPacket();
	}

	private void setUseBeforePacket() {
		useBeforePacket = null;
	}

	private void setUseBlockCount() {
		useBlockCount = 0;
	}

	public void setUseFromCheat(final boolean useFromCheat) {
		this.useFromCheat = useFromCheat;
	}
}