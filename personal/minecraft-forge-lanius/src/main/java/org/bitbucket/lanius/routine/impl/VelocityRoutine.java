package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class VelocityRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private float prevReduction;

	public VelocityRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.COMBAT);
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
		return "Changes the player's velocity to the specified.";
	}

	@Override
	public void init() {
		prevReduction = -1.0F;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Velocity";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!getBoolean("No Collision") || !clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null
				|| prevReduction == -1.0F) {
			return;
		}
		Lanius.mc.player.entityCollisionReduction = prevReduction;
		prevReduction = -1.0F;
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START)) {
			return;
		}
		final boolean packetVelocity = data.retVal instanceof SPacketEntityVelocity,
				packetExplosion = data.retVal instanceof SPacketExplosion;
		Entity packetEntity = null;
		SPacketEntityVelocity velPacket = null;
		if (!isEnabled() || !packetVelocity && !packetExplosion
				|| packetVelocity && Lanius.mc.world != null
						&& (packetEntity = Lanius.mc.world
								.getEntityByID((velPacket = (SPacketEntityVelocity) data.retVal).getEntityID())) != null
						&& !packetEntity.equals(Lanius.mc.player)) {
			return;
		}
		final float velocityH = getFloat("H-multiplier").floatValue(),
				velocityY = getFloat("Y-multiplier").floatValue();
		SPacketExplosion explosionPacket = null;
		if (packetExplosion) {
			explosionPacket = (SPacketExplosion) data.retVal;
		}
		data.retVal = velocityH == 0.0F && velocityY == 0.0F ? null
				: packetVelocity ? new SPacketEntityVelocity(packetEntity.getEntityId(),
						velPacket.getMotionX() / 8000.0D * velocityH, velPacket.getMotionY() / 8000.0D * velocityY,
						velPacket.getMotionZ() / 8000.0D * velocityH)
						: new SPacketExplosion(explosionPacket.getX(), explosionPacket.getY(), explosionPacket.getZ(),
								explosionPacket.getStrength(), explosionPacket.getAffectedBlockPositions(),
								new Vec3d(explosionPacket.getMotionX() * velocityH,
										explosionPacket.getMotionY() * velocityY,
										explosionPacket.getMotionZ() * velocityH));
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!getBoolean("No Collision") || !entityLiving.equals(Lanius.mc.player)
				|| entityLiving instanceof EntityPlayerMP) {
			return;
		}
		prevReduction = entityLiving.entityCollisionReduction;
		entityLiving.entityCollisionReduction = 1.0F;
	}

	@Override
	public void registerValues() {
		registerValue("H-multiplier", 0.0F, -9.0F, 9.0F, "Specifies how much to multiply received h-velocity by.");
		registerValue("Y-multiplier", 0.0F, -9.0F, 9.0F, "Specifies how much to multiply received y-velocity by.");
		registerValue("No Collision", true, "Determines whether or not to prevent entity collision.");
	}

}
