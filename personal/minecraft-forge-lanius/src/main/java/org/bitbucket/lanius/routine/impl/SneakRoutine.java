package org.bitbucket.lanius.routine.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.bitbucket.lanius.util.concurrent.Rate;
import org.bitbucket.lanius.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class SneakRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private static final double INIT_POS = -999.0D;

	private double oldX, oldZ;

	private boolean sneaking;
	private boolean edgeOfBlock; // Eric: For getting around the moved wrongly issue.

	public SneakRoutine() {
		super(Keyboard.KEY_Z, false, Tab.PLAYER);
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
		return "Automatically makes the player sneak.";
	}

	@Override
	public void init() {
		oldX = oldZ = INIT_POS;
		edgeOfBlock = false;
		final NoFallRoutine noFallRoutine = (NoFallRoutine) Lanius.getInstance().getRoutineRegistry().get("No Fall");
		noFallRoutine.setCancelled(false);
		setSneaking(false);
		if (Lanius.mc.player != null && !GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindSneak)) {
			KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindSneak.getKeyCode(), false);
		}
	}

	boolean isSneaking() {
		return sneaking;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Sneak";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (clientTickEv.phase.equals(TickEvent.Phase.END) && edgeOfBlock) {
			final NoFallRoutine noFallRoutine = (NoFallRoutine) Lanius.getInstance().getRoutineRegistry()
					.get("No Fall");
			noFallRoutine.setCancelled(false);
			edgeOfBlock = false;
		}
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || !playersInRange()
				|| Lanius.mc.player.isSneaking() || !getBoolean("Packet") && Lanius.mc.inGameHasFocus
				|| Lanius.mc.isGamePaused()) {
			return;
		}
		setSneaking(true);
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (isEnabled() && data.retVal instanceof CPacketPlayer
				&& (Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class,
						(CPacketPlayer) data.retVal, "field_149480_h", "moving")
				&& edgeOfBlock && phase.equals(Phase.START)) {
			ReflectHelper.setValue(CPacketPlayer.class, (CPacketPlayer) data.retVal, false, "field_149474_g",
					"onGround");
		}
		if (!isEnabled() || !(data.retVal instanceof CPacketPlayerTryUseItem) || !getBoolean("Packet")
				|| !RoutineUtils.ncpEnabled()) {
			return;
		}
		setSneaking(phase.equals(Phase.END));
	}

	@SubscribeEvent
	public void onInput(final InputEvent inputEv) {
		if (Lanius.mc.gameSettings.keyBindSneak.isKeyDown()) {
			setSneaking();
		}
		if ((getBoolean("Packet") || !Lanius.mc.inGameHasFocus) && RoutineUtils.ncpEnabled()
				&& Lanius.mc.gameSettings.keyBindUseItem.isKeyDown() && Lanius.mc.player != null) {
			final ItemStack currentStack = Lanius.mc.player.inventory.getCurrentItem();
			if (InventoryUtils.isStackValid(currentStack)) {
				final Item currentItem = currentStack.getItem();
				try {
					if (currentItem == Items.BOW
							&& (Lanius.mc.player.capabilities.isCreativeMode
									|| EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, currentStack) > 0
									|| ReflectionHelper
											.findMethod(ItemBow.class, "findAmmo", "func_185060_a", EntityPlayer.class)
											.invoke(Items.BOW, Lanius.mc.player) != null)
							|| currentItem == Items.ENDER_PEARL || currentItem == Items.ENDER_EYE
							|| currentItem == Items.EGG || currentItem == Items.SNOWBALL
							|| currentItem == Items.EXPERIENCE_BOTTLE || currentItem == Items.SPLASH_POTION) {
						KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
						while (Lanius.mc.gameSettings.keyBindUseItem.isPressed()) {
						}
						CommandUtils.addText(Lanius.mc.player, "Cannot use the player's current item while both " + this
								+ " and NoCheatPlus are enabled.");
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onLivingUpdateHigh(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP) {
			return;
		}
		final boolean packet = getBoolean("Packet"), entitySneak = livingEntity.isSneaking();
		if ((packet || !Lanius.mc.inGameHasFocus) && !entitySneak && !playersInRange()) {
			setSneaking(false);
		} else if ((packet || !Lanius.mc.inGameHasFocus) && (oldX == INIT_POS && oldZ == INIT_POS
				|| MathHelper.distance(oldX, 0.0D, oldZ, livingEntity.posX, 0.0D, livingEntity.posZ) > 0.03D)) {
			oldX = livingEntity.posX;
			oldZ = livingEntity.posZ;
			if (RoutineUtils.ncpEnabled() && !((EntityPlayer) livingEntity).capabilities.allowFlying && !entitySneak) {
				setSneaking(false);
			}
			if (!RoutineUtils.ncpEnabled() && !((EntityPlayer) livingEntity).capabilities.allowFlying && !entitySneak
					&& livingEntity.onGround && packet && playersInRange()) {
				final NoFallRoutine noFallRoutine = (NoFallRoutine) Lanius.getInstance().getRoutineRegistry()
						.get("No Fall");
				noFallRoutine.setCancelled(true);
				edgeOfBlock = true;
			}
		} else if (!packet && !GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindSneak)) {
			final boolean playersInRange = playersInRange();
			final int sneakKeyCode = Lanius.mc.gameSettings.keyBindSneak.getKeyCode();
			if (entitySneak && !playersInRange) {
				KeyBinding.setKeyBindState(sneakKeyCode, false);
			} else if (!entitySneak && playersInRange) {
				if (!Lanius.mc.inGameHasFocus) {
					setSneaking(true);
				} else {
					setSneaking();
					KeyBinding.setKeyBindState(sneakKeyCode, true);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdateLowest(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP) {
			return;
		}
		if (edgeOfBlock) {
			final Material predictMat = livingEntity.world
					.getBlockState(new BlockPos(livingEntity.posX + livingEntity.motionX,
							livingEntity.posY - JesusRoutine.BLOCK_OFF + livingEntity.motionY,
							livingEntity.posZ + livingEntity.motionZ))
					.getMaterial(),
					posMat = livingEntity.world.getBlockState(new BlockPos(livingEntity.posX,
							livingEntity.posY - JesusRoutine.BLOCK_OFF, livingEntity.posZ)).getMaterial();
			if (!livingEntity.world.getCollisionBoxes(livingEntity,
					livingEntity.getEntityBoundingBox().offset(livingEntity.motionX, -0.5D, livingEntity.motionZ))
					.isEmpty()
					&& !(RoutineUtils.enabled("Jesus") && (posMat == Material.WATER || posMat == Material.LAVA
							|| predictMat == Material.WATER || predictMat == Material.LAVA))) {
				final NoFallRoutine noFallRoutine = (NoFallRoutine) Lanius.getInstance().getRoutineRegistry()
						.get("No Fall");
				noFallRoutine.setCancelled(false);
				edgeOfBlock = false;
			} else {
				final Rate<CPacketPlayer> playerPacketRate = Lanius.getInstance().getPlayerPacketRate();
				if (playerPacketRate.canExecute(1, 0)) {
					Lanius.mc.player.connection.getNetworkManager().sendPacket(new CPacketPlayer(false));
					playerPacketRate.setExecCount(playerPacketRate.getExecCount() + 1);
				} else {
					final NoFallRoutine noFallRoutine = (NoFallRoutine) Lanius.getInstance().getRoutineRegistry()
							.get("No Fall");
					noFallRoutine.setCancelled(false);
					edgeOfBlock = false;
					setSneaking(false);
				}
			}
		}
	}

	private boolean playersInRange() {
		if (getBoolean("Always")) {
			return true;
		}
		NameProtectRoutine protectRoutine = (NameProtectRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Name Protect");
		boolean friends = getBoolean("Friends");
		for (final EntityPlayer player : Lanius.mc.world.playerEntities) {
			if (player instanceof EntityOtherPlayerMP && player.getDistance(Lanius.mc.player) < 64.0F
					&& !player.equals(((FreecamRoutine) Lanius.getInstance().getRoutineRegistry().get("Freecam"))
							.getRenderEntity())
					&& !player.equals(
							((BlinkRoutine) Lanius.getInstance().getRoutineRegistry().get("Blink")).getPosEntity())
					&& (!protectRoutine.isEnabled() || protectRoutine.getAlias(player.getName()) == null || friends)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void registerValues() {
		registerValue("Packet", true, "Determines whether or not the player should sneak server-sidedly.");
		registerValue("Friends", false, "Determines whether or not to sneak when a friend is in range.");
		registerValue("Always", false, "Determines whether or not to always sneak.");
	}

	/**
	 * Sets sneaking to false without sending an unsneak packet.
	 */
	private void setSneaking() {
		sneaking = false;
	}

	private void setSneaking(boolean sneaking) {
		sneaking &= (!RoutineUtils.enabled("Kill Aura") || !RoutineUtils.ncpEnabled()
				|| !((KillAuraRoutine) Lanius.getInstance().getRoutineRegistry().get("Kill Aura")).isAttackEntities())
				&& (!RoutineUtils.ncpEnabled() || Lanius.mc.objectMouseOver == null
						|| !Lanius.mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK)
						|| Lanius.mc.world.isAirBlock(Lanius.mc.objectMouseOver.getBlockPos())
						|| !GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindUseItem) || !Lanius.mc.inGameHasFocus);
		if (Lanius.mc.player != null) {
			if (!this.sneaking && sneaking) {
				Lanius.mc.player.connection.sendPacket(
						new CPacketEntityAction(Lanius.mc.player, CPacketEntityAction.Action.START_SNEAKING));
			} else if (this.sneaking && !sneaking) {
				Lanius.mc.player.connection.sendPacket(
						new CPacketEntityAction(Lanius.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
			}
		}
		this.sneaking = sneaking;
	}

}
