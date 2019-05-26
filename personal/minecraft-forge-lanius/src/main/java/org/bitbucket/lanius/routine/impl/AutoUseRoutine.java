package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class AutoUseRoutine extends TabbedRoutine implements Hook<NetHandlerData> {
	private static final float INIT_HEALTH = 0.0F;
	private static final AxisAlignedBB zeroBox = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	private int heldItem, prevItem;
	private float prevHealth;
	private long startTime;

	public AutoUseRoutine() {
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
		return "Automatically uses the selected item to heal the player.";
	}

	@Override
	public String displayData() {
		int itemCount = 0;
		for (int invSlot = InventoryUtils.MIN_SLOT; invSlot < InventoryUtils.MAX_SLOT; invSlot++) {
			if (matchesItem(Lanius.mc.player.inventoryContainer.getSlot(invSlot).getStack())) {
				++itemCount;
			}
		}
		return String.valueOf(itemCount);
	}

	public boolean healthPotion(final ItemStack stack, final boolean checkSplash) {
		if (!InventoryUtils.isStackValid(stack)) {
			return false;
		}
		final Item item = stack.getItem();
		if (checkSplash ? item == Items.SPLASH_POTION : item == Items.POTIONITEM) {
			for (final Object effectObj : PotionUtils.getEffectsFromStack(stack)) {
				if (((PotionEffect) effectObj).getPotion() == MobEffects.INSTANT_HEALTH) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void init() {
		setHeldItem();
		setPrevItem();
		setPrevHealth();
		startTime = 0L;
	}

	private void lookDown() {
		HookManager.netHook.setServerPitch(90.0F);
	}

	private boolean matchesItem(final ItemStack stack) {
		if (!InventoryUtils.isStackValid(stack)) {
			return false;
		}
		final Item item = Item.getByNameOrId(getString("Item"));
		return item == Items.SPLASH_POTION && healthPotion(stack, true)
				|| item == Items.POTIONITEM && healthPotion(stack, false) || stack.getItem() == item;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Auto-use";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || Lanius.mc.isGamePaused()) {
			return;
		}
		if (!usingItem()) {
			startTime = System.currentTimeMillis();
		} else {
			if (prevItem == InventoryUtils.INIT_SLOT) {
				prevItem = Lanius.mc.player.inventory.currentItem;
			}
			InventoryUtils.switchItem(heldItem);
			if (System.currentTimeMillis() - startTime > getInt("Delay").intValue() + NetworkUtils.lagTime()) {
				final boolean prevHandActive = Lanius.mc.player.isHandActive();
				final ItemStack heldStack = Lanius.mc.player.inventoryContainer
						.getSlot(heldItem + InventoryUtils.HOTBAR_BEGIN).getStack();
				if (RoutineUtils.viaVersionEnabled()) {
					rightClick(heldStack, EnumHand.MAIN_HAND);
				} else {
					for (final EnumHand hand : EnumHand.values()) {
						if (rightClick(heldStack, hand)) {
							break;
						}
					}
				}
				if (!prevHandActive) {
					Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onClientTickHighest(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null
				|| Lanius.mc.player.isEntityAlive() || Lanius.mc.isGamePaused()) {
			return;
		}
		setPrevHealth();
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START) || !(data.retVal instanceof CPacketPlayerTryUseItem)
				&& !(data.retVal instanceof CPacketPlayerTryUseItemOnBlock) || !isEnabled() || !usingItem()) {
			return;
		}
		data.retVal = null;
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onLivingUpdateLow(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		final float entityHealth = livingEntity.getHealth();
		final boolean healthInit = prevHealth == INIT_HEALTH;
		if (Lanius.mc.player == null || !livingEntity.equals(Lanius.mc.player)
				|| livingEntity instanceof EntityPlayerMP) {
			return;
		}
		if (usingSplashPot()) {
			lookDown();
		}
		if (entityHealth > getFloat("Threshold").floatValue() * 2.0F || !healthInit && prevHealth == entityHealth) {
			return;
		}
		if (healthInit) {
			prevHealth = entityHealth;
		} else {
			boolean hasItem = false;
			final EntityPlayerSP playerSp = (EntityPlayerSP) livingEntity;
			for (int invSlot = InventoryUtils.MIN_SLOT; invSlot < InventoryUtils.MAX_SLOT; invSlot++) {
				if (matchesItem(playerSp.inventoryContainer.getSlot(invSlot).getStack())) {
					hasItem = true;
					break;
				}
			}
			if (!hasItem) {
				return;
			}
			boolean usePotion = livingEntity.onGround;
			final boolean splashPotion = Item.getByNameOrId(getString("Item")) == Items.SPLASH_POTION;
			if (!usePotion && splashPotion) {
				final double H_VEC = 4.0D;
				final double motionY = livingEntity.onGround ? 0.0D : livingEntity.motionY; // Eric:
																							// ensures
																							// that
																							// motionY
																							// isn't
																							// negative
																							// when
																							// it
																							// needs
																							// not
																							// to
																							// be
				final AxisAlignedBB aabb = zeroBox.grow(H_VEC, 2.0D, H_VEC).expand(livingEntity.motionX, motionY,
						livingEntity.motionZ);
				final List entitiesInBox = livingEntity.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
				if (!entitiesInBox.contains(livingEntity)) {
					entitiesInBox.add(livingEntity);
				}
				int groundY = MathHelper.floor(livingEntity.posY + motionY);
				for (; Lanius.mc.world
						.getBlockState(new BlockPos(MathHelper.floor(livingEntity.posX + livingEntity.motionX), groundY,
								MathHelper.floor(livingEntity.posZ + livingEntity.motionZ)))
						.getCollisionBoundingBox(Lanius.mc.world,
								new BlockPos(MathHelper.floor(livingEntity.posX + livingEntity.motionX), groundY,
										MathHelper.floor(livingEntity.posZ + livingEntity.motionZ))) == null
						|| Lanius.mc.world
								.getBlockState(new BlockPos(MathHelper.floor(livingEntity.posX + livingEntity.motionX),
										groundY, MathHelper.floor(livingEntity.posZ + livingEntity.motionZ)))
								.getCollisionBoundingBox(Lanius.mc.world,
										new BlockPos(MathHelper.floor(livingEntity.posX + livingEntity.motionX),
												groundY, MathHelper.floor(livingEntity.posZ + livingEntity.motionZ)))
								.equals(Block.NULL_AABB); groundY--) {
					if (groundY <= 0) {
						break;
					}
				}
				groundY += 2; // Eric: for distance calculations
				if (!entitiesInBox.isEmpty()) {
					final Iterator entitiesIt = entitiesInBox.iterator();
					while (entitiesIt.hasNext()) {
						EntityLivingBase entityLiving = (EntityLivingBase) entitiesIt.next();
						if (entityLiving.equals(livingEntity) && org.bitbucket.lanius.util.math.MathHelper
								.distance(0.0D, livingEntity.posY + motionY, 0.0D, 0.0D, groundY, 0.0D) < 16.0D) {
							usePotion = true;
							break;
						}
					}
				}
			}
			if (!usePotion && splashPotion) {
				return;
			}
			InventoryUtils.ensureInventory();
			final int heldSlot = playerSp.inventory.currentItem + InventoryUtils.HOTBAR_BEGIN;
			int itemSlot = matchesItem(playerSp.openContainer.getSlot(heldSlot).getStack()) ? heldSlot : 0;
			if (itemSlot == 0) {
				for (int hotbarSlot = InventoryUtils.HOTBAR_BEGIN; hotbarSlot < InventoryUtils.MAX_SLOT; hotbarSlot++) {
					if (matchesItem(playerSp.openContainer.getSlot(hotbarSlot).getStack())) {
						itemSlot = hotbarSlot;
						break;
					}
				}
				boolean emptySlot = false, allWeapons = true;
				final KillAuraRoutine fighterRoutine = (KillAuraRoutine) Lanius.getInstance().getRoutineRegistry()
						.get("Kill Aura");
				final double baseStrength = playerSp.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
						.getBaseValue();
				for (int hotbarSlot = InventoryUtils.HOTBAR_BEGIN; hotbarSlot < InventoryUtils.MAX_SLOT; hotbarSlot++) {
					final ItemStack slotStack = playerSp.openContainer.getSlot(hotbarSlot).getStack();
					if (!InventoryUtils.isStackValid(slotStack)) {
						emptySlot = true;
						allWeapons = false;
						break;
					} else if (fighterRoutine.damageVsEntity(slotStack, playerSp) == baseStrength) {
						allWeapons = false;
					}
				}
				if (itemSlot == 0 && !emptySlot && getBoolean("Drop Hotbar Item")) {
					for (int hotbarSlot = InventoryUtils.HOTBAR_BEGIN; hotbarSlot < InventoryUtils.MAX_SLOT; hotbarSlot++) {
						final ItemStack slotStack = playerSp.openContainer.getSlot(hotbarSlot).getStack();
						final int invItem = hotbarSlot - InventoryUtils.HOTBAR_BEGIN;
						if (InventoryUtils.isStackValid(slotStack)
								&& (fighterRoutine.damageVsEntity(slotStack, playerSp) == baseStrength || allWeapons)
								&& invItem != playerSp.inventory.currentItem && !matchesItem(slotStack)) {
							playerSp.connection.sendPacket(new CPacketHeldItemChange(invItem));
							playerSp.dropItem(slotStack.getCount() > 1);
							playerSp.connection.sendPacket(new CPacketHeldItemChange(playerSp.inventory.currentItem));
							emptySlot = true;
							break;
						}
					}
				}
				if (itemSlot == 0 && emptySlot) {
					inventory: for (int invSlot = InventoryUtils.MIN_SLOT; invSlot < 36; invSlot++) {
						if (matchesItem(playerSp.openContainer.getSlot(invSlot).getStack())) {
							InventoryUtils.clickWindow(invSlot, 0, ClickType.QUICK_MOVE);
							for (int hotbarSlot = InventoryUtils.HOTBAR_BEGIN; hotbarSlot < InventoryUtils.MAX_SLOT; hotbarSlot++) {
								if (matchesItem(playerSp.openContainer.getSlot(hotbarSlot).getStack())) {
									itemSlot = hotbarSlot;
									break inventory;
								}
							}
						}
					}
				}
			}
			if (itemSlot >= InventoryUtils.HOTBAR_BEGIN) {
				if (splashPotion) {
					lookDown();
				}
				heldItem = itemSlot - InventoryUtils.HOTBAR_BEGIN;
				prevHealth = entityHealth;
			}
		}
	}

	@SubscribeEvent
	public void onRightClickBlock(final PlayerInteractEvent.RightClickBlock rightClickBlockEv) {
		if (usingItem()) {
			rightClickBlockEv.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onRightClickItem(final PlayerInteractEvent.RightClickItem rightClickItemEv) {
		if (usingItem()) {
			rightClickItemEv.setCanceled(true);
		}
	}

	@Override
	public void registerValues() {
		registerValue("Delay", 0, 0, 1000, "Specifies the delay to wait before using the selected item.");
		final String displayName = name();
		registerValue("Threshold", 6.0F, 0.5F, 9.5F,
				"Specifies the health at which " + displayName + " will heal the player.");
		registerValue("Drop Hotbar Item", true,
				"Determines whether or not " + displayName + " will drop an item if the player's hotbar is full.");
		registerValue("Item", "splash_potion", "Specifies the item that should be used to heal the player.");
	}

	private void reset() {
		if (Item.getByNameOrId(getString("Item")) == Items.MUSHROOM_STEW) {
			Lanius.mc.player.dropItem(false);
		}
		InventoryUtils.switchItem(prevItem);
		setPrevItem();
	}

	private boolean rightClick(final ItemStack heldStack, final EnumHand hand) {
		final boolean handActive = Lanius.mc.player.isHandActive();
		final int prevHeldItem = heldItem;
		setHeldItem();
		if (handActive && Lanius.mc.player.getActiveHand().equals(hand)) {
			reset();
			return false;
		} else if (handActive) {
			Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
		}
		HookManager.netHook.setUseFromCheat(true);
		if (!InventoryUtils.isStackValid(heldStack)) {
			Lanius.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(hand));
			if (HookManager.netHook.isUseSent()) {
				reset();
			} else {
				Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
				heldItem = prevHeldItem;
			}
			return true;
		} else if (Lanius.mc.playerController.processRightClick(Lanius.mc.player, Lanius.mc.world,
				hand) == EnumActionResult.SUCCESS) {
			Lanius.mc.entityRenderer.itemRenderer.resetEquippedProgress(hand);
			if (HookManager.netHook.isUseSent()) {
				reset();
			} else {
				Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
				heldItem = prevHeldItem;
			}
			return true;
		}
		if (HookManager.netHook.isUseSent()) {
			reset();
		} else {
			Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
			heldItem = prevHeldItem;
		}
		return false;
	}

	private void setHeldItem() {
		heldItem = InventoryUtils.INIT_SLOT;
	}

	private void setPrevHealth() {
		prevHealth = INIT_HEALTH;
	}

	private void setPrevItem() {
		prevItem = InventoryUtils.INIT_SLOT;
	}

	boolean usingItem() {
		return heldItem != InventoryUtils.INIT_SLOT;
	}

	boolean usingSplashPot() {
		return usingItem() && Item.getByNameOrId(getString("Item")) == Items.SPLASH_POTION;
	}
}
