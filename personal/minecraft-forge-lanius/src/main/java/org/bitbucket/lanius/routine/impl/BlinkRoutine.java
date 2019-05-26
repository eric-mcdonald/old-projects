package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.Timer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class BlinkRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private int moveCount;

	private CopyOnWriteArrayList<Packet<?>> packets;

	private EntityOtherPlayerMP posEntity;

	private double prevX, prevY, prevZ;

	public BlinkRoutine() {
		super(Keyboard.KEY_B, false, Tab.MOVEMENT);
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
		return "Lags the player, simulating a teleportation.";
	}

	@Override
	public String displayData() {
		return String.valueOf(packets == null ? 0 : packets.size());
	}

	public EntityOtherPlayerMP getPosEntity() {
		return posEntity;
	}

	@Override
	public void init() {
		if (packets == null) {
			packets = new CopyOnWriteArrayList<Packet<?>>();
		}
		if (Lanius.mc.player != null) {
			for (final Packet<?> packet : packets) {
				Lanius.mc.player.connection.sendPacket(packet);
			}
		}
		packets.clear();
		moveCount = 0;
		setPosEntity();
		prevX = prevY = prevZ = 0.0D;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Blink";
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onClientTickLowest(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || !RoutineUtils.ncpEnabled()
				|| Lanius.mc.isGamePaused()) {
			return;
		}
		if (moveCount >= 40 - NetworkUtils.lagTicks()) {
			HookManager.packetManager.removeHook(this);
			init();
			HookManager.packetManager.addHook(this);
		}
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!isEnabled() || !phase.equals(Phase.START)) {
			return;
		}
		if (NetworkUtils.motionPacket(data.retVal) || NetworkUtils.digBlockPacket(data.retVal)
				|| data.retVal instanceof CPacketPlayerTryUseItemOnBlock || data.retVal instanceof CPacketUseEntity
				|| data.retVal instanceof CPacketVehicleMove) {
			if (NetworkUtils.motionPacket(data.retVal)) {
				final CPacketPlayer playerPacket = (CPacketPlayer) data.retVal;
				if ((Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket,
						"field_149480_h", "moving")) {
					final double deltaX = playerPacket.getX(0.0D) - prevX, deltaY = playerPacket.getY(0.0D) - prevY,
							deltaZ = playerPacket.getZ(0.0D) - prevZ;
					if (deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ <= 9.0E-4D) {
						data.retVal = null;
						return;
					} else {
						prevX = playerPacket.getX(0.0D);
						prevY = playerPacket.getY(0.0D);
						prevZ = playerPacket.getZ(0.0D);
					}
				}
			}
			final boolean spawnEntity = getBoolean("Position Entity");
			if (spawnEntity && posEntity == null) {
				posEntity = new EntityOtherPlayerMP(Lanius.mc.player.world, Lanius.mc.player.getGameProfile());
				final float savedHeadYaw = Lanius.mc.player.rotationYawHead,
						savedPrevHead = Lanius.mc.player.prevRotationYawHead;
				Lanius.mc.player.rotationYawHead = Lanius.mc.player.rotationYaw;
				Lanius.mc.player.prevRotationYawHead = Lanius.mc.player.prevRotationYaw;
				final Vec3d lookVec = Lanius.mc.player.getLook(((Timer) ObfuscationReflectionHelper
						.getPrivateValue(Minecraft.class, Lanius.mc, "field_71428_T", "timer")).renderPartialTicks);
				Lanius.mc.player.rotationYawHead = savedHeadYaw;
				Lanius.mc.player.prevRotationYawHead = savedPrevHead;
				posEntity.setPositionAndRotation(Lanius.mc.player.posX - lookVec.x, Lanius.mc.player.posY - lookVec.y,
						Lanius.mc.player.posZ - lookVec.z, Lanius.mc.player.rotationYaw,
						Lanius.mc.player.rotationPitch);
				posEntity.rotationYawHead = Lanius.mc.player.rotationYawHead;
				Lanius.mc.world.addEntityToWorld(posEntity.getEntityId(), posEntity);
			} else if (!spawnEntity) {
				setPosEntity();
			}
			packets.add(data.retVal);
			if (NetworkUtils.motionPacket(data.retVal)) {
				++moveCount;
			}
			data.retVal = null;
		} else if (data.retVal instanceof SPacketRespawn || data.retVal instanceof SPacketPlayerPosLook) {
			init();
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onLivingUpdateLow(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP) {
			return;
		}
		ReflectHelper.setValue(EntityPlayerSP.class, Lanius.mc.player, 0, "field_175168_bP", "positionUpdateTicks");
	}

	@SubscribeEvent
	public void onRenderSpecialsPre(final RenderLivingEvent.Specials.Pre renderSpecialsPre) {
		if (renderSpecialsPre.getEntity().equals(posEntity)) {
			renderSpecialsPre.setCanceled(true);
		}
	}

	@Override
	public void registerValues() {
		registerValue("Position Entity", true,
				"Specifies whether or not to spawn a client-side entity at the player's last server position.");
	}

	private void setPosEntity() {
		if (posEntity == null) {
			return;
		}
		Lanius.mc.world.removeEntityFromWorld(posEntity.getEntityId());
		posEntity = null;
	}

}
