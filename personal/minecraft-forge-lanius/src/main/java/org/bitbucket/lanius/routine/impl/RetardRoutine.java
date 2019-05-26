package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;
import java.util.Random;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class RetardRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private boolean cancelSwing = true; // Eric: workaround for No Swing
										// blocking Retard swings

	private boolean prevRotationSent;

	private float prevYaw, prevPitch;

	private final Random rand = new Random();

	public RetardRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.PLAYER);
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
		return "Changes the player's yaw and pitch to random values.";
	}

	@Override
	public void init() {
		prevRotationSent = false;
	}

	boolean isCancelSwing() {
		return cancelSwing;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Retard";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!isEnabled()) {
			return;
		}
		final Lanius instance = Lanius.getInstance();
		if (RoutineUtils.ncpEnabled() && phase.equals(Phase.START)
				&& (NetworkUtils.digBlockPacket(data.retVal)
						&& !((CPacketPlayerDigging) data.retVal).getAction()
								.equals(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK)
						|| data.retVal instanceof CPacketPlayerTryUseItemOnBlock
						|| data.retVal instanceof CPacketUseEntity
								&& ((CPacketUseEntity) data.retVal).getAction().equals(CPacketUseEntity.Action.ATTACK)
								&& (!RoutineUtils.enabled("Kill Aura")
										|| !((KillAuraRoutine) instance.getRoutineRegistry().get("Kill Aura"))
												.isIgnoreAttackPacket()))
				&& playersLoaded()) {
			HookManager.netHook.resetRotations(
					!instance.getPlayerPacketRate().execute(new CPacketPlayer.Rotation(Lanius.mc.player.rotationYaw,
							Lanius.mc.player.rotationPitch, Lanius.mc.player.onGround), 0));
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP) {
			return;
		}
		if (RoutineUtils.ncpEnabled()
				&& (Lanius.mc.playerController.getIsHittingBlock() || Lanius.mc.gameSettings.keyBindAttack.isKeyDown()
						&& Lanius.mc.objectMouseOver != null && !Lanius.mc.player.isRowingBoat()
						&& Lanius.mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK)
						&& !Lanius.mc.world.isAirBlock(Lanius.mc.objectMouseOver.getBlockPos()))
				|| !playersLoaded()) {
			HookManager.netHook.resetRotations(true);
			prevRotationSent = false;
			return;
		}
		// Eric: ensures different rotations are sent every time
		final float YAW_CLAMP = 360.0F, PITCH_CLAMP = 360.0F;
		float serverYaw = rand.nextFloat() * YAW_CLAMP, serverPitch = rand.nextFloat() * PITCH_CLAMP;
		if (prevRotationSent) {
			while (serverYaw == prevYaw) {
				serverYaw = rand.nextFloat() * YAW_CLAMP;
			}
			prevYaw = serverYaw;
			while (serverPitch == prevPitch) {
				serverPitch = rand.nextFloat() * PITCH_CLAMP;
			}
			prevPitch = serverPitch;
		} else {
			while (serverYaw == entityLiving.rotationYaw) {
				serverYaw = rand.nextFloat() * YAW_CLAMP;
			}
			prevYaw = serverYaw;
			while (serverPitch == entityLiving.rotationPitch) {
				serverPitch = rand.nextFloat() * PITCH_CLAMP;
			}
			prevPitch = serverPitch;
		}
		HookManager.netHook.setServerYaw(serverYaw);
		HookManager.netHook.setServerPitch(serverPitch);
		prevRotationSent = true;
		if (getBoolean("Swing")) {
			cancelSwing = false;
			Lanius.mc.player.connection.sendPacket(new CPacketAnimation(
					RoutineUtils.viaVersionEnabled() || !rand.nextBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
			cancelSwing = true;
		}
	}

	private boolean playersLoaded() {
		for (final EntityPlayer player : Lanius.mc.world.playerEntities) {
			if (player instanceof EntityOtherPlayerMP
					&& !player.equals(((FreecamRoutine) Lanius.getInstance().getRoutineRegistry().get("Freecam"))
							.getRenderEntity())
					&& !player.equals(
							((BlinkRoutine) Lanius.getInstance().getRoutineRegistry().get("Blink")).getPosEntity())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void registerValues() {
		registerValue("Swing", false, "Determines whether or not to swing the player's arm.");
	}

}
