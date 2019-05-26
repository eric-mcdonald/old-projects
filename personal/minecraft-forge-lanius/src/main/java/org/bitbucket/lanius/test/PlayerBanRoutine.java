package org.bitbucket.lanius.test;

import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerListItem.AddPlayerData;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class PlayerBanRoutine extends Routine {

	private EntityOtherPlayerMP banEntity;
	private final Random msgRand = new Random();
	private long msgStartTime;
	private boolean banBypassFilter;

	public PlayerBanRoutine() {
		super(Keyboard.KEY_NONE, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return TEST_COLOR;
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Gets a player banned.";
	}

	@Override
	public void init() {
		if (banEntity != null) {
			Lanius.mc.world.removeEntityFromWorld(banEntity.getEntityId());
			banEntity = null;
		}
		msgStartTime = 0L;
		banBypassFilter = false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Player Ban";
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		if (Lanius.mc.world == null)
			return;
		final String BAN_NAME = "I_Am_A_Gamer_";
		if (banEntity == null) {
			for (final EntityPlayer player : Lanius.mc.world.playerEntities) {
				if (player.getName().contains(BAN_NAME)) {
					banEntity = (EntityOtherPlayerMP) player;
					break;
				}
			}
			if (banEntity == null) {
				final GameProfile banProfile = new GameProfile(UUID.fromString("f9d76b6e-9143-4cd5-9b37-32ca929d0fc7"),
						BAN_NAME);
				final SPacketPlayerListItem playerListPacket = new SPacketPlayerListItem(
						SPacketPlayerListItem.Action.ADD_PLAYER);
				final AddPlayerData addPlayerData = playerListPacket.new AddPlayerData(banProfile, 2, GameType.SURVIVAL,
						new TextComponentString(BAN_NAME));
				playerListPacket.getEntries().add(addPlayerData);
				Lanius.mc.getConnection().handlePlayerListItem(playerListPacket);
				banEntity = new EntityOtherPlayerMP(Lanius.mc.world, banProfile) {
					@Override
					public ResourceLocation getLocationSkin() {
						final ResourceLocation skinLoc = AbstractClientPlayer.getLocationSkin(BAN_NAME);
						AbstractClientPlayer.getDownloadImageSkin(skinLoc, BAN_NAME);
						return skinLoc;
					}
				};
			} else {
				banEntity = new EntityOtherPlayerMP(Lanius.mc.world, banEntity.getGameProfile());
			}
			banEntity.setPositionAndRotation(Lanius.mc.player.posX, Lanius.mc.player.posY, Lanius.mc.player.posZ,
					Lanius.mc.player.rotationYaw, Lanius.mc.player.rotationPitch);
			/*
			 * setArmor(Items.DIAMOND_HELMET); setArmor(Items.DIAMOND_CHESTPLATE);
			 * setArmor(Items.DIAMOND_LEGGINGS); setArmor(Items.DIAMOND_BOOTS);
			 */
			Lanius.mc.world.addEntityToWorld(banEntity.getEntityId(), banEntity);
			// System.out.println(banEntity);
		}
		banEntity.rotationYaw = 180.0F;
		banEntity.rotationPitch = 0.0F;
		banEntity.rotationYawHead = banEntity.rotationYaw;
		for (final EntityPlayer player : Lanius.mc.world.playerEntities) {
			if (player.equals(banEntity)) {
				continue;
			}
			if (banEntity.getDistance(player) <= 4.4F) {
				MathHelper.faceEntity(banEntity, player, 180.0F, 180.0F);
				banEntity.rotationYawHead = banEntity.rotationYaw;
				banEntity.swingArm(EnumHand.MAIN_HAND);
				break;
			}
		}
		if (System.currentTimeMillis() - msgStartTime >= 3000L) {
			Lanius.mc.player.sendMessage(new TextComponentString(
					"\2477 \2478[\2477Member\2478] \2477\2477\2477I_Am_A_Gamer_ \2478\u00bb \2477C137_Scientist is a stupid nigfer i hate him"
							+ (banBypassFilter ? "4234" : "")));
			banBypassFilter = !banBypassFilter;
			msgStartTime = System.currentTimeMillis();
		}
	}

	private void setArmor(final ItemArmor armor) {
		ItemStack itemStackIn = new ItemStack(armor);
		EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemStackIn);
		ItemStack itemstack = banEntity.getItemStackFromSlot(entityequipmentslot);
		if (!InventoryUtils.isStackValid(itemstack)) {
			banEntity.setItemStackToSlot(entityequipmentslot, itemStackIn.copy());
		}
	}

}
