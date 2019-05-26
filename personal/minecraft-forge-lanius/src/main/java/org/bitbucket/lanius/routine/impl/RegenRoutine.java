package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;
import java.util.Iterator;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.hook.impl.NetHandlerHook;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class RegenRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private boolean peaceful;

	public RegenRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.PLAYER);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Heals the player.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Regen";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START)) {
			return;
		}
		final boolean packetJoinGame = data.retVal instanceof SPacketJoinGame;
		if (!packetJoinGame && !(data.retVal instanceof SPacketRespawn)) {
			return;
		}
		peaceful = (packetJoinGame ? ((SPacketJoinGame) data.retVal).getDifficulty()
				: ((SPacketRespawn) data.retVal).getDifficulty()) == EnumDifficulty.PEACEFUL;
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onLivingUpdateHigh(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (Lanius.mc.player == null || !livingEntity.equals(Lanius.mc.player)
				|| livingEntity instanceof EntityPlayerMP) {
			return;
		}
		final EntityPlayerSP playerSp = (EntityPlayerSP) livingEntity;
		final AxisAlignedBB entityBB = livingEntity.getEntityBoundingBox();
		final float EXPAND_VEC = 0.0625F;
		if (!playerSp.capabilities.isCreativeMode
				&& livingEntity.world.checkBlockCollision(
						entityBB.grow(EXPAND_VEC, EXPAND_VEC, EXPAND_VEC).expand(0.0D, -0.55D, 0.0D))
				|| playerSp.capabilities.isCreativeMode) {
			int playerPackets = 0;
			final boolean ncpEnabled = RoutineUtils.ncpEnabled();
			if ((!ncpEnabled || peaceful) && livingEntity.getHealth() < livingEntity.getMaxHealth()
					&& (playerSp.getFoodStats().getFoodLevel() >= 18 || peaceful)) {
				playerPackets = (int) ((livingEntity.getMaxHealth() - livingEntity.getHealth()) * (peaceful ? 20 : 80));
			}
			if (getBoolean("Anti-burn")) {
				final int fire = MathHelper.abs((Integer) ObfuscationReflectionHelper.getPrivateValue(Entity.class,
						livingEntity, "fire", "field_190534_ay"));
				if (livingEntity.isBurning() && !playerSp.capabilities.isCreativeMode && playerPackets < fire) {
					playerPackets = fire;
				}
			}
			if (getBoolean("Anti-potion")) {
				final Iterator effectIterator = livingEntity.getActivePotionEffects().iterator();
				while (effectIterator.hasNext()) {
					final PotionEffect effect = (PotionEffect) effectIterator.next();
					if (effect.getPotion().isBadEffect() && effect.getDuration() > playerPackets
							&& !effect.getIsPotionDurationMax()) {
						playerPackets = effect.getDuration();
					}
				}
			}
			for (int packetCount = 0; packetCount < playerPackets; packetCount++) {
				NetHandlerHook.sendPlayerPacket(new CPacketPlayer(livingEntity.onGround));
			}
		}
	}

	@Override
	public void registerValues() {
		final String displayName = name();
		registerValue("Anti-burn", true, "Determines whether or not " + displayName + " will remove fire.");
		registerValue("Anti-potion", true,
				"Determines whether or not " + displayName + " will remove bad potion effects.");
	}

}
