package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class ElytraRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private int jumpState;

	private boolean modElytra;

	private double prevY;

	public ElytraRoutine() {
		super(Keyboard.KEY_Y, false, Tab.MOVEMENT);
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
		return "Gives the player a larger range of motion while flying with an elytra.";
	}

	@Override
	public void init() {
		jumpState = 0;
		prevY = 0.0D;
		modElytra = false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Elytra";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || Lanius.mc.isGamePaused()) {
			return;
		}
		modElytra = false;
		if (jumpState >= 2 && !Lanius.mc.player.isElytraFlying() && !Lanius.mc.player.capabilities.isFlying
				&& !RoutineUtils.flyEnabled() && !RoutineUtils.noclipEnabled()) {
			final ItemStack chestStack = Lanius.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			if (InventoryUtils.isStackValid(chestStack) && chestStack.getItem() == Items.ELYTRA
					&& ItemElytra.isUsable(chestStack)) {
				Lanius.mc.player.connection.sendPacket(
						new CPacketEntityAction(Lanius.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
			}
			++jumpState;
		}
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		final boolean ncpEnabled = RoutineUtils.ncpEnabled();
		if (!phase.equals(Phase.START) || !isEnabled() || !modElytra
				|| !((!ncpEnabled || Lanius.mc.player.capabilities.isCreativeMode)
						&& !Lanius.mc.player.movementInput.jump
						|| ncpEnabled && !Lanius.mc.player.capabilities.isCreativeMode
								&& Lanius.mc.player.rotationPitch * 0.017453292F < 0.0F)
				|| !(data.retVal instanceof CPacketPlayer) || !Lanius.mc.player.isElytraFlying()) {
			return;
		}
		final CPacketPlayer playerPacket = (CPacketPlayer) data.retVal;
		if ((Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket, "field_149480_h",
				"moving") && prevY < Lanius.mc.player.posY) {
			final double deltaY = Lanius.mc.player.posY - prevY;
			Lanius.mc.player.move(MoverType.SELF, 0.0D, -deltaY, 0.0D);
			ReflectHelper.setValue(CPacketPlayer.class, playerPacket, playerPacket.getY(0.0D) - deltaY,
					"field_149477_b", "y");
		}
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP) {
			return;
		}
		jumpState = Lanius.mc.player.onGround ? 0 : jumpState + 1;
		final double BLOCK_UNDER_OFF = -0.01D;
		final Material predictMat = entityLiving.world
				.getBlockState(new BlockPos(entityLiving.posX + entityLiving.motionX,
						entityLiving.posY + BLOCK_UNDER_OFF + entityLiving.motionY,
						entityLiving.posZ + entityLiving.motionZ))
				.getMaterial();
		final Material posMat = entityLiving.world
				.getBlockState(new BlockPos(entityLiving.posX, entityLiving.posY + BLOCK_UNDER_OFF, entityLiving.posZ))
				.getMaterial();
		final boolean jump = !(entityLiving.isInWater() || entityLiving.isInLava()
				|| RoutineUtils.enabled("Jesus") && (posMat == Material.WATER || posMat == Material.LAVA
						|| predictMat == Material.WATER || predictMat == Material.LAVA))
				&& !entityLiving.isOnLadder();
		final boolean elytraFlying = entityLiving.isElytraFlying();
		if (jump && jumpState == 0 && Lanius.mc.player.onGround && !elytraFlying
				&& !Lanius.mc.player.capabilities.isFlying && !RoutineUtils.flyEnabled()
				&& !RoutineUtils.noclipEnabled()) {
			final ItemStack chestStack = Lanius.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			if (InventoryUtils.isStackValid(chestStack) && chestStack.getItem() == Items.ELYTRA
					&& ItemElytra.isUsable(chestStack)) {
				Lanius.mc.player.jump();
				++jumpState;
			}
		}
		final boolean ncpEnabled = RoutineUtils.ncpEnabled();
		if (elytraFlying && (!ncpEnabled || jump)) {
			final float speed = getFloat("Speed").floatValue();
			entityLiving.motionY = Lanius.mc.player.movementInput.jump
					&& (!ncpEnabled || Lanius.mc.player.capabilities.isCreativeMode) ? speed
							: Lanius.mc.player.movementInput.sneak ? -speed : 0.0D;
			entityLiving.motionX = entityLiving.motionZ = 0.0D;
			final Vec3d lookVec = entityLiving.getLookVec();
			final float pitchRad = entityLiving.rotationPitch * 0.017453292F;
			final double hLook = Math.sqrt(lookVec.x * lookVec.x + lookVec.z * lookVec.z),
					hMotion = Math.sqrt(
							entityLiving.motionX * entityLiving.motionX + entityLiving.motionZ * entityLiving.motionZ),
					lookLen = lookVec.lengthVector();
			float cosPitch = MathHelper.cos(pitchRad);
			cosPitch = (float) ((double) cosPitch * (double) cosPitch * Math.min(1.0D, lookLen / 0.4D));
			final double H_FACTOR = 0.9900000095367432D;
			entityLiving.motionX /= H_FACTOR;
			entityLiving.motionY /= 0.9800000190734863D;
			entityLiving.motionZ /= H_FACTOR;
			if (hLook > 0.0D) {
				entityLiving.motionX -= (lookVec.x / hLook * hMotion - entityLiving.motionX) * 0.1D;
				entityLiving.motionZ -= (lookVec.z / hLook * hMotion - entityLiving.motionZ) * 0.1D;
			}
			if (pitchRad < 0.0F) {
				final double factor = hMotion * (-MathHelper.sin(pitchRad)) * 0.04D;
				entityLiving.motionY -= factor * 3.2D;
				entityLiving.motionX += lookVec.x * factor / hLook;
				entityLiving.motionZ += lookVec.z * factor / hLook;
			}
			if (entityLiving.motionY < 0.0D && hLook > 0.0D) {
				final double factor = entityLiving.motionY * -0.1D * cosPitch;
				entityLiving.motionY -= factor;
				entityLiving.motionX -= lookVec.x * factor / hLook;
				entityLiving.motionZ -= lookVec.z * factor / hLook;
			}
			entityLiving.motionY -= -0.08D + cosPitch * 0.06D;
			entityLiving.moveRelative(Lanius.mc.player.movementInput.moveStrafe, 0.0F,
					Lanius.mc.player.movementInput.moveForward, speed);
			modElytra = true;
		}
		prevY = Lanius.mc.player.posY;
	}

	@Override
	public void registerValues() {
		registerValue("Speed", 0.6F, 0.1F, 6.0F,
				"Specifies the speed at which the player will move while elytra flying.");
	}

}
