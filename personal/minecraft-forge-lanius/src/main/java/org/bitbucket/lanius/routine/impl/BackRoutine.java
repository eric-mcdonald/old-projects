package org.bitbucket.lanius.routine.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class BackRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private boolean firstTick, displayedMsg, firstReset = true;

	private Entity prevRidingEntity;

	private final List<Packet> savedPackets = new CopyOnWriteArrayList<Packet>();

	public BackRoutine() {
		super(Keyboard.KEY_I, false, Tab.MOVEMENT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Allows the player to travel somewhere and then teleport back.";
	}

	private void dismountEntity() {
		Lanius.mc.player.connection.sendPacket(new CPacketInput(0.0F, 0.0F, false, true));
		Lanius.mc.player.dismountRidingEntity();
	}

	@Override
	public void init() {
		savedPackets.clear();
		firstTick = true;
		prevRidingEntity = null;
		displayedMsg = false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Back";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null) {
			return;
		}
		final Entity ridingEntity = Lanius.mc.player.getLowestRidingEntity();
		final boolean steerableVehicle = Lanius.mc.player.isRiding() && ridingEntity != Lanius.mc.player
				&& ridingEntity.canPassengerSteer();
		if (!displayedMsg && !steerableVehicle) {
			CommandUtils.addText(Lanius.mc.player,
					"Cannot execute routine " + name() + " while the player is not in a steerable vehicle.");
		} else if (firstTick && steerableVehicle) {
			prevRidingEntity = ridingEntity;
			dismountEntity();
			firstTick = false;
		}
		displayedMsg = true;
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!isEnabled()) {
			return;
		}
		final boolean dismount = data.retVal instanceof CPacketInput && ((CPacketInput) data.retVal).isSneaking();
		if (data.retVal instanceof SPacketPlayerPosLook) {
			if (prevRidingEntity == null && phase.equals(Phase.START) && !firstReset) {
				savedPackets.add(data.retVal);
				data.retVal = null;
			} else if (phase.equals(Phase.END) && prevRidingEntity != null) {
				firstReset = false;
				final List<Entity> sortedEntities = new ArrayList<Entity>(Lanius.mc.world.loadedEntityList);
				Collections.sort(sortedEntities, KillAuraRoutine.distanceCmp);
				boolean hasRidingEntity = false;
				for (final Entity entity : sortedEntities) {
					final double maxDist = Lanius.mc.player.canEntityBeSeen(entity) ? 36.0D : 9.0D;
					if (!(entity instanceof AbstractHorse) && !(entity instanceof EntityPig)
							&& !(entity instanceof EntityBoat) || Lanius.mc.player.getDistanceSq(entity) >= maxDist
							|| !entity.equals(prevRidingEntity)) {
						continue;
					}
					hasRidingEntity = true;
					break;
				}
				for (final Entity entity : sortedEntities) {
					final double maxDist = Lanius.mc.player.canEntityBeSeen(entity) ? 36.0D : 9.0D;
					if (!(entity instanceof AbstractHorse) && !(entity instanceof EntityPig)
							&& !(entity instanceof EntityBoat) || Lanius.mc.player.getDistanceSq(entity) >= maxDist
							|| hasRidingEntity && !entity.equals(prevRidingEntity)) {
						continue;
					}
					for (final EnumHand hand : EnumHand.values()) {
						if (Lanius.mc.playerController.interactWithEntity(Lanius.mc.player, entity,
								hand) == EnumActionResult.SUCCESS || RoutineUtils.viaVersionEnabled()) {
							break;
						}
					}
					break;
				}
				prevRidingEntity = null;
			}
		} else if (phase.equals(Phase.START) && prevRidingEntity == null) {
			if (dismount) {
				CommandUtils.addText(Lanius.mc.player,
						"Cannot dismount the player's riding entity until routine " + name() + " is disabled.");
				final CPacketInput inputPacket = (CPacketInput) data.retVal;
				data.retVal = new CPacketInput(inputPacket.getStrafeSpeed(), inputPacket.getForwardSpeed(),
						inputPacket.isJumping(), false);
			} else if (data.retVal instanceof CPacketPlayer) {
				data.retVal = null;
			}
		}
	}

	@Override
	@SubscribeEvent
	public void onUnload(final WorldEvent.Unload unloadEv) {
		super.onUnload(unloadEv);
		firstReset = true;
	}

	private void posLookNoEnqueueCheck(SPacketPlayerPosLook packetIn) {
		EntityPlayer entityplayer = Lanius.mc.player;
		double d0 = packetIn.getX();
		double d1 = packetIn.getY();
		double d2 = packetIn.getZ();
		float f = packetIn.getYaw();
		float f1 = packetIn.getPitch();
		if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X)) {
			d0 += entityplayer.posX;
		} else {
			entityplayer.motionX = 0.0D;
		}
		if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y)) {
			d1 += entityplayer.posY;
		} else {
			entityplayer.motionY = 0.0D;
		}
		if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Z)) {
			d2 += entityplayer.posZ;
		} else {
			entityplayer.motionZ = 0.0D;
		}
		if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
			f1 += entityplayer.rotationPitch;
		}
		if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
			f += entityplayer.rotationYaw;
		}
		entityplayer.setPositionAndRotation(d0, d1, d2, f, f1);
		Lanius.mc.player.connection.getNetworkManager()
				.sendPacket(new CPacketConfirmTeleport(packetIn.getTeleportId()));
		Lanius.mc.player.connection.getNetworkManager().sendPacket(
				new CPacketPlayer.PositionRotation(entityplayer.posX, entityplayer.getEntityBoundingBox().minY,
						entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false));
		final String[] loadingTerrainMappings = new String[] { "field_147309_h", "doneLoadingTerrain" };
		if (!(Boolean) ObfuscationReflectionHelper.getPrivateValue(NetHandlerPlayClient.class,
				Lanius.mc.player.connection, loadingTerrainMappings)) {
			Lanius.mc.player.prevPosX = Lanius.mc.player.posX;
			Lanius.mc.player.prevPosY = Lanius.mc.player.posY;
			Lanius.mc.player.prevPosZ = Lanius.mc.player.posZ;
			ReflectHelper.setValue(NetHandlerPlayClient.class, Lanius.mc.player.connection, true,
					loadingTerrainMappings);
			Lanius.mc.displayGuiScreen((GuiScreen) null);
		}
	}

	@Override
	public void setEnabled() {
		final boolean hasPackets = !savedPackets.isEmpty() && Lanius.mc.player != null;
		if (isEnabled() && hasPackets) {
			for (final Packet savedPacket : savedPackets) {
				if (savedPacket instanceof SPacketPlayerPosLook) {
					posLookNoEnqueueCheck((SPacketPlayerPosLook) savedPacket);
				}
			}
		}
		super.setEnabled();
		if (!isEnabled() && hasPackets) {
			final Entity ridingEntity = Lanius.mc.player.getLowestRidingEntity();
			if (Lanius.mc.player.isRiding() && ridingEntity != Lanius.mc.player && ridingEntity.canPassengerSteer()) {
				dismountEntity();
			}
		}
	}

}
