package org.bitbucket.lanius.routine.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.hook.impl.NetHandlerHook;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class SpeedyRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private int bowMoveCount;
	private boolean executedEat;

	public SpeedyRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.WORLD);
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
		return "Makes the player mine blocks, place blocks, use food, potions, and bow faster.";
	}

	@Override
	public void init() {
		setMoveCount();
		executedEat = false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Speedy Gonzales";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (clientTickEv.phase.equals(TickEvent.Phase.START)) {
			if (Lanius.mc.player != null && !Lanius.mc.isGamePaused()) {
				if (!Lanius.mc.player.isHandActive()) {
					executedEat = false;
				}
				final ItemStack heldStack = Lanius.mc.player.getHeldItem(EnumHand.MAIN_HAND);
				try {
					if (!(InventoryUtils.isStackValid(heldStack) && heldStack.getItem() == Items.BOW
							&& (Lanius.mc.player.capabilities.isCreativeMode
									|| EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, heldStack) > 0
									|| ReflectionHelper
											.findMethod(ItemBow.class, "findAmmo", "func_185060_a", EntityPlayer.class)
											.invoke(Items.BOW, Lanius.mc.player) != null))
							&& !(InventoryUtils.isStackValid(Lanius.mc.player.inventory.getCurrentItem())
									&& (Lanius.mc.player.inventory.getCurrentItem().getItem() instanceof ItemFood
											|| Lanius.mc.player.inventory.getCurrentItem()
													.getItem() instanceof ItemPotion))) {
						return;
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
			setRightClickDelayTimer();
		} else if (clientTickEv.phase.equals(TickEvent.Phase.END) && Lanius.mc.player != null) {
			if (!Lanius.mc.isGamePaused()) {
				if (Lanius.mc.player.isHandActive()) {
					final boolean ncpEnabled = RoutineUtils.ncpEnabled(),
							viaVersionEnabled = RoutineUtils.viaVersionEnabled();
					final float EXPAND_VEC = 0.0625F;
					final AxisAlignedBB playerBox = Lanius.mc.player.getEntityBoundingBox();
					if (bowMoveCount >= 0) {
						++bowMoveCount;
						final int shootDelay = getInt("Shoot Delay").intValue();
						final boolean multiPacket = shootDelay == 0 && !ncpEnabled;
						final boolean extraMovement = viaVersionEnabled && getBoolean("Extra Movement");
						if (extraMovement && (multiPacket || bowMoveCount % 2 == 0)
								&& (!Lanius.mc.player.capabilities.isCreativeMode
										&& Lanius.mc.player.world.checkBlockCollision(playerBox
												.grow(EXPAND_VEC, EXPAND_VEC, EXPAND_VEC).expand(0.0D, -0.55D, 0.0D))
										|| Lanius.mc.player.capabilities.isCreativeMode)) {
							for (int count = 0; count < (multiPacket ? 20 : 1); count++) {
								NetHandlerHook.sendPlayerPacket(new CPacketPlayer(Lanius.mc.player.onGround));
							}
						}
						final int NORMAL_MIN = 3;
						if (multiPacket && extraMovement || bowMoveCount >= shootDelay
								+ (shootDelay < NORMAL_MIN && !(multiPacket && extraMovement) ? NORMAL_MIN - shootDelay
										: 0)
								+ (!viaVersionEnabled ? 1 : 0) + (ncpEnabled ? NetworkUtils.lagTicks() : 0)) {
							Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
						}
					} else if (!executedEat && viaVersionEnabled
							&& InventoryUtils.isStackValid(Lanius.mc.player.inventory.getCurrentItem())
							&& (Lanius.mc.player.inventory.getCurrentItem().getItem() instanceof ItemFood
									|| Lanius.mc.player.inventory.getCurrentItem().getItem() instanceof ItemPotion)
							&& (!Lanius.mc.player.capabilities.isCreativeMode
									&& Lanius.mc.player.world.checkBlockCollision(playerBox
											.grow(EXPAND_VEC, EXPAND_VEC, EXPAND_VEC).expand(0.0D, -0.55D, 0.0D))
									|| Lanius.mc.player.capabilities.isCreativeMode)) {
						for (int count = 0; count < (ncpEnabled
								? Lanius.mc.player.inventory.getCurrentItem().getMaxItemUseDuration() * (700.0F
										/ (Lanius.mc.player.inventory.getCurrentItem().getMaxItemUseDuration() * 50.0F))
										- (ncpEnabled ? NetworkUtils.lagTicks() + 2 : 0)
								: Lanius.mc.player.inventory.getCurrentItem().getMaxItemUseDuration()); count++) {
							NetHandlerHook.sendPlayerPacket(new CPacketPlayer(Lanius.mc.player.onGround));
						}
						executedEat = true;
					}
				} else {
					executedEat = false;
				}
			}
			final boolean isHittingBlock = !Lanius.mc.player.capabilities.isCreativeMode
					&& Lanius.mc.playerController != null
					&& (Boolean) ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class,
							Lanius.mc.playerController, "field_78778_j", "isHittingBlock"),
					autoTool = getBoolean("Auto-tool");
			if (autoTool && isHittingBlock) {
				int bestSlotId = InventoryUtils.INIT_SLOT;
				float bestStrength = 1.0F;
				final IBlockState currentBlock = Lanius.mc.world
						.getBlockState((BlockPos) ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class,
								Lanius.mc.playerController, "field_178895_c", "currentBlock"));
				for (int slotId = InventoryUtils.HOTBAR_BEGIN; slotId < InventoryUtils.MAX_SLOT; slotId++) {
					final ItemStack slotStack = Lanius.mc.player.inventoryContainer.getSlot(slotId).getStack();
					if (InventoryUtils.isStackValid(slotStack)) {
						float strength = slotStack.getItem().getDestroySpeed(slotStack, currentBlock);
						if (Lanius.mc.player.inventoryContainer.getSlot(slotId).getHasStack()
								&& strength > bestStrength) {
							bestSlotId = slotId;
							bestStrength = strength;
						}
					}
				}
				boolean hotbarFull = true;
				for (int slotId = InventoryUtils.HOTBAR_BEGIN; slotId < InventoryUtils.MAX_SLOT; slotId++) {
					if (!Lanius.mc.player.inventoryContainer.getSlot(slotId).getHasStack()) {
						hotbarFull = false;
						break;
					}
				}
				if (!hotbarFull) {
					for (int slotId = InventoryUtils.MIN_SLOT; slotId < InventoryUtils.HOTBAR_BEGIN; slotId++) {
						final ItemStack slotStack = Lanius.mc.player.inventoryContainer.getSlot(slotId).getStack();
						if (InventoryUtils.isStackValid(slotStack)) {
							float strength = slotStack.getItem().getDestroySpeed(slotStack, currentBlock);
							if (Lanius.mc.player.inventoryContainer.getSlot(slotId).getHasStack()
									&& strength > bestStrength) {
								bestSlotId = slotId;
								bestStrength = strength;
							}
						}
					}
				}
				if (bestSlotId != InventoryUtils.INIT_SLOT) {
					InventoryUtils.ensureInventory();
					if (bestSlotId < InventoryUtils.HOTBAR_BEGIN) {
						int bestItem = InventoryUtils.INIT_SLOT;
						for (int newSlotId = InventoryUtils.HOTBAR_BEGIN; newSlotId < InventoryUtils.MAX_SLOT; newSlotId++) {
							if (!Lanius.mc.player.inventoryContainer.getSlot(newSlotId).getHasStack()) {
								bestItem = newSlotId - InventoryUtils.HOTBAR_BEGIN;
								break;
							}
						}
						InventoryUtils.clickWindow(bestSlotId, 0, ClickType.QUICK_MOVE);
						if (bestItem != InventoryUtils.INIT_SLOT) {
							InventoryUtils.switchItem(bestItem);
						}
					} else {
						InventoryUtils.switchItem(bestSlotId - InventoryUtils.HOTBAR_BEGIN);
					}
				}
			}
			final String[] hitDelayMappings = new String[] { "field_78781_i", "blockHitDelay" };
			int hitDelay = getInt("Hit Delay").intValue();
			if ((Integer) ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class,
					Lanius.mc.playerController, hitDelayMappings) == 5 && hitDelay != 5) {
				ReflectHelper.setValue(PlayerControllerMP.class, Lanius.mc.playerController, hitDelay,
						hitDelayMappings);
			}
			final String[] blockDamMappings = new String[] { "field_78770_f", "curBlockDamageMP" };
			if (isHittingBlock && (Float) ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class,
					Lanius.mc.playerController, blockDamMappings) >= getFloat("Threshold").floatValue()) {
				ReflectHelper.setValue(PlayerControllerMP.class, Lanius.mc.playerController, 1.0F, blockDamMappings);
			}
			setRightClickDelayTimer();
		}
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START)) {
			return;
		}
		if (isEnabled() && data.retVal instanceof CPacketPlayerTryUseItem
				&& InventoryUtils.isStackValid(Lanius.mc.player.inventory.getCurrentItem())
				&& Lanius.mc.player.inventory.getCurrentItem().getItem() == Items.BOW) {
			bowMoveCount = 0;
		} else if (data.retVal instanceof CPacketPlayerDigging || data.retVal instanceof CPacketHeldItemChange) {
			setMoveCount();
			executedEat = false;
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdateLowest(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP
				|| !RoutineUtils.viaVersionEnabled()) {
			return;
		}
		HookManager.netHook.forcePlayerPacket();
	}

	@Override
	public void registerValues() {
		registerValue("Click Delay", 0, 0, 4,
				"Specifies how long to wait the player must wait before right-clicking again.");
		registerValue("Auto-tool", true, "Determines whether or not to switch to the player's best tool.");
		registerValue("Hit Delay", 0, 0, 5,
				"Specifies the delay that " + name() + " will wait until before hitting another block.");
		registerValue("Threshold", 0.7F, 0.7F, 1.0F, "Specifies the threshold of damage the block will break at.");
		registerValue("Extra Movement", true,
				"Determines whether or not to send extra packets to make " + name() + " faster.");
		registerValue("Shoot Delay", 20, 0, 20, "Specifies how long to wait before shooting a bow.");
	}

	private void setMoveCount() {
		bowMoveCount = -1;
	}

	private void setRightClickDelayTimer() {
		final String[] clickDelayMappings = new String[] { "field_71467_ac", "rightClickDelayTimer" };
		final int rightClickDelay = getInt("Click Delay").intValue();
		if (((Integer) ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Lanius.mc,
				clickDelayMappings)) > rightClickDelay) {
			ReflectHelper.setValue(Minecraft.class, Lanius.mc, rightClickDelay, clickDelayMappings);
		}
	}

}
