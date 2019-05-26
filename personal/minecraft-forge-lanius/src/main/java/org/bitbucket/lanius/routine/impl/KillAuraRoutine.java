package org.bitbucket.lanius.routine.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.RoutineHandler;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.bitbucket.lanius.util.concurrent.Rate;
import org.bitbucket.lanius.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class KillAuraRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	/**
	 * This class acts as a container for an item's strength vs. entity and the slot
	 * it was in. This was ported from Lanius v1.0
	 * 
	 * @author Eric
	 */
	private final class Weapon {
		private final int slot;
		private final double strength;

		private Weapon(final double strength, final int slot) {
			this.strength = strength;
			this.slot = slot;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Weapon) {
				return ((Weapon) obj).strength == this.strength;
			}
			return false;
		}

		@Override
		public int hashCode() {
			final long strengthBits = Double.doubleToLongBits(strength);
			int hash = (int) (strengthBits ^ strengthBits >>> 32);
			hash += slot * 31;
			return hash;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("strength", strength).add("slot", slot).toString();
		}
	}

	static final Comparator<Entity> distanceCmp = new Comparator<Entity>() {

		@Override
		public int compare(Entity arg0, Entity arg1) {
			// TODO Auto-generated method stub
			return Float.compare(Lanius.mc.player.getDistance(arg0), Lanius.mc.player.getDistance(arg1));
		}

	};

	private static final float MAX_INCREMENT = 180.0F;

	private static final Comparator<Entity> rotationCmp = new Comparator<Entity>() {

		@Override
		public int compare(Entity o1, Entity o2) {
			// TODO Auto-generated method stub
			return Float.compare(yawDist(o1), yawDist(o2));
		}

		private float yawDist(final Entity entity) {
			final float prevYaw = Lanius.mc.player.rotationYaw, prevPitch = Lanius.mc.player.rotationPitch;
			MathHelper.faceEntity(Lanius.mc.player, entity, MAX_INCREMENT, MAX_INCREMENT);
			final float yawDist = net.minecraft.util.math.MathHelper
					.abs(net.minecraft.util.math.MathHelper.wrapDegrees(Lanius.mc.player.rotationYaw)
							- net.minecraft.util.math.MathHelper.wrapDegrees(HookManager.netHook.getServerYaw()));
			Lanius.mc.player.rotationYaw = prevYaw;
			Lanius.mc.player.rotationPitch = prevPitch;
			return yawDist;
		}

	};

	private boolean angleBypass, attackTarget, hasStack, stackSaved, attackEntities;

	private long attackStartTime;

	private Map<Entity, Long> attackTimeMap;

	private EnumHand blockingHand;

	private boolean ignoreAttackPacket, ignorePlayerPacket;

	private ItemStack prevStack;

	private final List<Entity> targetEntities = new ArrayList<Entity>(), possibleTargets = new ArrayList<Entity>();

	private int weaponIdx, prevItem, prevWeaponSlot;

	public KillAuraRoutine() {
		super(Keyboard.KEY_K, false, Tab.COMBAT);
		// TODO Auto-generated constructor stub
	}

	private List<Weapon> attackWeapons() {
		final List<Weapon> weapons = new ArrayList<Weapon>();
		final int itemIdx = Lanius.mc.player.inventory.currentItem + InventoryUtils.HOTBAR_BEGIN;
		final Slot heldSlot = Lanius.mc.player.inventoryContainer.getSlot(itemIdx);
		boolean addedLowest = false;
		final double baseStrength = Lanius.mc.player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
				.getBaseValue();
		if (heldSlot.getHasStack()) {
			final double damageVsEntity = damageVsEntity(heldSlot.getStack(), Lanius.mc.player);
			if (damageVsEntity == baseStrength) {
				weapons.add(new Weapon(damageVsEntity, itemIdx));
				addedLowest = true;
			}
		}
		if (!addedLowest) {
			for (int hotbarIdx = InventoryUtils.HOTBAR_BEGIN; hotbarIdx < InventoryUtils.MAX_SLOT; hotbarIdx++) {
				final Slot slot = Lanius.mc.player.inventoryContainer.getSlot(hotbarIdx);
				if (!slot.getHasStack() || damageVsEntity(slot.getStack(), Lanius.mc.player) == baseStrength) {
					weapons.add(new Weapon(baseStrength, hotbarIdx));
					break;
				}
			}
		}
		for (int slotIdx = InventoryUtils.MAX_SLOT - 1; slotIdx >= InventoryUtils.MIN_SLOT; slotIdx--) {
			if (Lanius.mc.player.inventoryContainer.getSlot(slotIdx).getHasStack()) {
				final Weapon weapon = new Weapon(damageVsEntity(
						Lanius.mc.player.inventoryContainer.getSlot(slotIdx).getStack(), Lanius.mc.player), slotIdx);
				if (!weapons.contains(weapon)) {
					weapons.add(weapon);
				}
			}
		}
		if (!weapons.isEmpty()) {
			Collections.sort(weapons, new Comparator<Weapon>() {

				@Override
				public int compare(Weapon arg0, Weapon arg1) {
					// TODO Auto-generated method stub
					return Double.compare(arg0.strength, arg1.strength);
				}

			});
			final Iterator<Weapon> weaponsIt = weapons.iterator();
			int weapIdx = 0;
			final int weaponsSz = weapons.size();
			final Weapon lastWeapon = weapons.get(weaponsSz - 1);
			final int sizeOff = net.minecraft.util.math.MathHelper
					.floor(10.0D / (Math.max((getInt("Delay").intValue() + NetworkUtils.lagTime()) / 50.0D,
							getFloat("Strength").floatValue() * Lanius.mc.player.getCooldownPeriod()) + 0.1D));
			while (weaponsIt.hasNext() && weapIdx < weaponsSz - sizeOff) {
				if (weaponsIt.next().equals(lastWeapon)) {
					continue;
				}
				weaponsIt.remove();
				++weapIdx;
			}
		}
		return weapons;
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	double damageVsEntity(final ItemStack stack, final EntityPlayer player) {
		final double baseStrength = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
		if (!InventoryUtils.isStackValid(stack)) {
			return baseStrength;
		}
		final Item item = stack.getItem();
		final float enchMod = EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
		return item instanceof ItemSword
				? RoutineUtils.viaVersionEnabled() ? 4.0D + ((ItemSword) item).getAttackDamage() + enchMod
						: baseStrength + 3.0D + ((ItemSword) item).getAttackDamage() + enchMod
				: item instanceof ItemTool ? RoutineUtils.viaVersionEnabled()
						? (item instanceof ItemAxe ? 3.0D : item instanceof ItemPickaxe ? 2.0D : 1.0D)
								+ findToolMaterial((ItemTool) item).getAttackDamage() + enchMod
						: baseStrength + (Float) ObfuscationReflectionHelper.getPrivateValue(ItemTool.class,
								(ItemTool) item, "field_77865_bY", "attackDamage") + enchMod
						: baseStrength;
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Attacks nearby entities.";
	}

	@Override
	public String displayData() {
		return String.valueOf(possibleTargets.size());
	}

	private void executeMoreAttacks(final boolean delayReady, final Entity targetEntity) {
		if (!getBoolean("More Attacks")) {
			return;
		}
		if (delayReady && !((AutoUseRoutine) Lanius.getInstance().getRoutineRegistry().get("Auto-use")).usingItem()) {
			final List<Weapon> weapons = attackWeapons();
			if (!weapons.isEmpty()) {
				final long currentTime = System.currentTimeMillis();
				if (!attackTimeMap.containsKey(targetEntity) || currentTime - attackTimeMap.get(targetEntity) < 500L) {
					double fullDam = weapons.isEmpty() ? 0.0D : weapons.get(0).strength;
					final int lastWeapIdx = weapons.size() - 1;
					for (int weaponIndex = 0; weaponIndex < lastWeapIdx; weaponIndex++) {
						fullDam += weapons.get(weaponIndex + 1).strength - weapons.get(weaponIndex).strength;
					}
					if (!weapons.isEmpty() && weapons.get(lastWeapIdx).strength >= fullDam) {
						final int weaponIndex = weaponIdx >= lastWeapIdx ? 0 : lastWeapIdx;
						int weaponSlot = weapons.get(weaponIndex).slot;
						if (weaponSlot < InventoryUtils.HOTBAR_BEGIN) {
							for (int hotbarSlot = InventoryUtils.HOTBAR_BEGIN; hotbarSlot < InventoryUtils.MAX_SLOT; hotbarSlot++) {
								if (!Lanius.mc.player.inventoryContainer.getSlot(hotbarSlot).getHasStack()) {
									InventoryUtils.clickWindow(weaponSlot, 0, ClickType.QUICK_MOVE);
									weaponSlot = hotbarSlot;
									break;
								}
							}
						}
						if (weaponSlot >= InventoryUtils.HOTBAR_BEGIN) {
							switchWeapon(weaponSlot);
						}
						weaponIdx = weaponIndex;
					} else {
						for (int weaponIndex = 0; weaponIndex < weapons.size(); weaponIndex++) {
							if (weaponIndex > weaponIdx) {
								int weaponSlot = weapons.get(weaponIndex).slot;
								if (weaponSlot < InventoryUtils.HOTBAR_BEGIN) {
									for (int hotbarSlot = InventoryUtils.HOTBAR_BEGIN; hotbarSlot < InventoryUtils.MAX_SLOT; hotbarSlot++) {
										if (!Lanius.mc.player.inventoryContainer.getSlot(hotbarSlot).getHasStack()) {
											InventoryUtils.clickWindow(weaponSlot, 0, ClickType.QUICK_MOVE);
											weaponSlot = hotbarSlot;
											break;
										}
									}
								}
								if (weaponSlot >= InventoryUtils.HOTBAR_BEGIN) {
									switchWeapon(weaponSlot);
								}
								weaponIdx = weaponIndex;
								if (weaponIdx < weapons.size() - 1) {
									break;
								}
							}
							if (weaponIdx >= weapons.size() - 1) {
								setWeaponIdx();
								break;
							}
						}
					}
				} else {
					final int weaponsSize = weapons.size();
					for (int weapIndex = 0; weapIndex < weaponsSize; weapIndex++) {
						final Weapon weapon = weapons.get(weapIndex);
						final int heldItem = Lanius.mc.player.inventory.currentItem + InventoryUtils.HOTBAR_BEGIN;
						if (Lanius.mc.player.inventoryContainer.getSlot(heldItem).getHasStack()
								&& weapon.equals(new Weapon(
										damageVsEntity(Lanius.mc.player.inventoryContainer.getSlot(heldItem).getStack(),
												Lanius.mc.player),
										heldItem))) {
							break;
						}
						int weapSlot = weapon.slot;
						if (weapSlot < InventoryUtils.HOTBAR_BEGIN) {
							for (int hotbarSlot = InventoryUtils.HOTBAR_BEGIN; hotbarSlot < InventoryUtils.MAX_SLOT; hotbarSlot++) {
								if (!Lanius.mc.player.inventoryContainer.getSlot(hotbarSlot).getHasStack()) {
									InventoryUtils.clickWindow(weapSlot, 0, ClickType.QUICK_MOVE);
									weapSlot = hotbarSlot;
									break;
								}
							}
						}
						if (weapSlot >= InventoryUtils.HOTBAR_BEGIN) {
							switchWeapon(weapSlot);
							break;
						}
					}
					setWeaponIdx();
				}
			}
		}
	}

	private Item.ToolMaterial findToolMaterial(ItemTool tool) {
		for (Item.ToolMaterial material : Item.ToolMaterial.values()) {
			if (material.toString().equals(tool.getToolMaterialName())) {
				return material;
			}
		}
		return null;
	}

	@Override
	public void init() {
		attackStartTime = 0L;
		targetEntities.clear();
		if (attackTimeMap == null) {
			attackTimeMap = new HashMap<Entity, Long>();
		} else {
			attackTimeMap.clear();
		}
		angleBypass = false;
		attackTarget = true;
		setWeaponIdx();
		resetCurrentItem();
		attackEntities = false;
		prevWeaponSlot = InventoryUtils.INIT_SLOT;
		blockingHand = null;
		possibleTargets.clear();
	}

	boolean isAttackEntities() {
		return attackEntities;
	}

	boolean isIgnoreAttackPacket() {
		return ignoreAttackPacket;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Kill Aura";
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onClientTickHigh(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || Lanius.mc.isGamePaused()
				|| targetEntities.isEmpty()
				|| ((AutoUseRoutine) Lanius.getInstance().getRoutineRegistry().get("Auto-use")).usingSplashPot()
				|| !attackTarget) {
			return;
		}
		final List<Weapon> weapons = attackWeapons();
		Material posMat = Lanius.mc.player.world.getBlockState(new BlockPos(Lanius.mc.player.posX,
				Lanius.mc.player.posY - JesusRoutine.BLOCK_OFF, Lanius.mc.player.posZ)).getMaterial();
		final boolean swing = getBoolean("Swing"), criticalHits = getBoolean("Criticals") && Lanius.mc.player.onGround
				&& !Lanius.mc.player.isOnLadder() && !Lanius.mc.player.isInWater()
				&& !Lanius.mc.player.isPotionActive(MobEffects.BLINDNESS) && Lanius.mc.player.getRidingEntity() == null
				&& (!getBoolean("More Attacks")
						|| !weapons.isEmpty() && damageVsEntity(Lanius.mc.player.inventoryContainer
								.getSlot(Lanius.mc.player.inventory.currentItem + InventoryUtils.HOTBAR_BEGIN)
								.getStack(), Lanius.mc.player) >= weapons.get(weapons.size() - 1).strength)
				&& (!RoutineUtils.ncpEnabled() || !RoutineUtils.enabled("Jesus")
						|| posMat != Material.WATER && posMat != Material.LAVA);
		final ItemStack currentItem = Lanius.mc.player.inventory.getCurrentItem();
		final boolean ncpEnabled = RoutineUtils.ncpEnabled(),
				ncpBlocking = ncpEnabled && Lanius.mc.player.isActiveItemStackBlocking(),
				hasItem = InventoryUtils.isStackValid(currentItem);
		final AxisAlignedBB playerBB = Lanius.mc.player.getEntityBoundingBox();
		final ItemStack offItem = Lanius.mc.player.getHeldItemOffhand();
		// Eric: rough detection for server-sided blocking
		if (ncpBlocking || ncpEnabled && HookManager.netHook.isUsingItem()
				&& (RoutineUtils.viaVersionEnabled() && hasItem && currentItem.getItem() instanceof ItemSword
						|| InventoryUtils.isStackValid(offItem) && offItem.getItem() == Items.SHIELD)) {
			Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
		}
		final boolean prevSprint = Lanius.mc.player.isSprinting();
		for (final Entity targetEntity : targetEntities) {
			if (criticalHits && targetEntity instanceof EntityLivingBase) {
				final NoFallRoutine noFallRoutine = (NoFallRoutine) Lanius.getInstance().getRoutineRegistry()
						.get("No Fall");
				noFallRoutine.setCancelled(true);
				final Rate<CPacketPlayer> playerPacketRate = Lanius.getInstance().getPlayerPacketRate();
				if (playerPacketRate.canExecute(2, 0)) {
					Lanius.mc.player.connection.getNetworkManager()
							.sendPacket(new CPacketPlayer.Position(Lanius.mc.player.posX,
									playerBB.minY + 0.06251D + 0.01D + 0.01D, Lanius.mc.player.posZ, false));
					if (ncpEnabled) {
						float prevYaw = Lanius.mc.player.rotationYaw, prevPitch = Lanius.mc.player.rotationPitch;
						double prevY = Lanius.mc.player.posY;
						Lanius.mc.player.posY += 0.01D;
						MathHelper.faceEntity(Lanius.mc.player, targetEntity, 180.0F, 180.0F);
						Lanius.mc.player.posY = prevY;
						Lanius.mc.player.connection.getNetworkManager()
								.sendPacket(new CPacketPlayer.PositionRotation(Lanius.mc.player.posX,
										playerBB.minY + 0.01D, Lanius.mc.player.posZ, Lanius.mc.player.rotationYaw,
										Lanius.mc.player.rotationPitch, false));
						Lanius.mc.player.rotationYaw = prevYaw;
						Lanius.mc.player.rotationPitch = prevPitch;
					} else {
						Lanius.mc.player.connection.getNetworkManager().sendPacket(new CPacketPlayer.Position(
								Lanius.mc.player.posX, playerBB.minY + 0.01D, Lanius.mc.player.posZ, false));
					}
					playerPacketRate.setExecCount(playerPacketRate.getExecCount() + 2);
					if (prevSprint) {
						Lanius.mc.player.setSprinting(false);
						Lanius.mc.player.connection.sendPacket(
								new CPacketEntityAction(Lanius.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
					}
					Lanius.mc.player.world.playSound(null, Lanius.mc.player.posX, Lanius.mc.player.posY,
							Lanius.mc.player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
							Lanius.mc.player.getSoundCategory(), 1.0F, 1.0F);
					Lanius.mc.player.onCriticalHit(targetEntity);
				}
				noFallRoutine.setCancelled(false);
			}
			ignoreAttackPacket = true;
			Lanius.mc.playerController.attackEntity(Lanius.mc.player, targetEntity);
			ignoreAttackPacket = false;
			if (swing) {
				Lanius.mc.player.swingArm(EnumHand.MAIN_HAND);
			}
			attackTimeMap.put(targetEntity, System.currentTimeMillis());
			if (ncpEnabled) {
				break;
			}
		}
		if (prevWeaponSlot != InventoryUtils.INIT_SLOT) {
			switchWeapon(prevWeaponSlot);
			InventoryUtils.syncInventory();
			prevWeaponSlot = InventoryUtils.INIT_SLOT;
		}
		if (prevSprint) {
			Lanius.mc.player.setSprinting(prevSprint);
			Lanius.mc.player.connection
					.sendPacket(new CPacketEntityAction(Lanius.mc.player, CPacketEntityAction.Action.START_SPRINTING));
		}
		if (ncpBlocking && hasItem) {
			for (final EnumHand hand : EnumHand.values()) {
				if (!(InventoryUtils.isStackValid(currentItem) && !Lanius.mc.player.isHandActive())) {
					break;
				}
				if (rightClick(currentItem, hand)) {
					if (!HookManager.netHook.isUseSent()) {
						Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
					}
					break;
				}
				if (!HookManager.netHook.isUseSent()) {
					Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
				}
			}
		}
		attackTarget = false;
		targetEntities.clear();
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!(data.retVal instanceof CPacketPlayer)) {
			return;
		}
		if (isEnabled() && phase.equals(Phase.END) && blockingHand != null) {
			if (InventoryUtils.isStackValid(Lanius.mc.player.getHeldItem(blockingHand))
					&& !Lanius.mc.player.isHandActive()) {
				HookManager.netHook.setUseFromCheat(true);
				rightClick(Lanius.mc.player.getHeldItem(blockingHand), blockingHand);
				if (!HookManager.netHook.isUseSent()) {
					Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
				}
			}
			blockingHand = null;
		}
		if (ignorePlayerPacket) {
			if (phase.equals(Phase.END)) {
				ignorePlayerPacket = false;
			}
			return;
		}
		if (!isEnabled() || !phase.equals(Phase.START)) {
			return;
		}
		data.retVal = null;
		if (Lanius.mc.player.getHealth() <= 0.0F && getBoolean("Disable on Death")) {
			setEnabled();
			sendMovement();
			return;
		}
		attackEntities = false;
		if (((AutoUseRoutine) Lanius.getInstance().getRoutineRegistry().get("Auto-use")).usingSplashPot()) {
			sendMovement();
			return;
		}
		final boolean players = getBoolean("Players"), armor = getBoolean("Armor"),
				cfgHeldItem = getBoolean("Held Item"), mobs = getBoolean("Mobs"), animals = getBoolean("Animals"),
				invisEntities = getBoolean("Invisible Entities"), friends = getBoolean("Friends");
		final EntityPlayerSP player = Lanius.mc.player;
		final float prevYaw = Lanius.mc.player.rotationYaw, prevPitch = Lanius.mc.player.rotationPitch,
				radius = getFloat("Radius").floatValue(), INVIS_RADIUS = 3.0F, fov = getFloat("FOV").floatValue();
		final int entityAge = getInt("Entity Age").intValue(),
				lagAge = entityAge + (entityAge <= 0 ? 0 : NetworkUtils.lagTicks());
		final AxisAlignedBB playerBB = player.getEntityBoundingBox();
		final List<Entity> sortedEntities = new ArrayList<Entity>(Lanius.mc.player.world.loadedEntityList);
		Collections.sort(sortedEntities, distanceCmp);
		final boolean ncpEnabled = RoutineUtils.ncpEnabled();
		if (ncpEnabled) {
			// Eric: helps bypass NoCheatPlus's Angle check
			Collections.sort(sortedEntities, rotationCmp);
		}
		boolean firstTarget = true, allAttacked = true;
		final NameProtectRoutine protectRoutine = (NameProtectRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Name Protect");
		final boolean protectEnabled = protectRoutine.isEnabled();
		final RoutineHandler routineHandler = Lanius.getInstance().getRoutineHandler();
		boolean hasTarget = false;
		final long currentTime = System.currentTimeMillis();
		for (final Entity possibleTarget : sortedEntities) {
			final boolean attackPlayer = players && possibleTarget instanceof EntityOtherPlayerMP
					&& (!armor || InventoryUtils.hasArmor((EntityPlayer) possibleTarget))
					&& (!cfgHeldItem || InventoryUtils.hasHeldItem((EntityPlayer) possibleTarget));
			if (possibleTarget.equals(player) || !(possibleTarget instanceof EntityLivingBase)
					|| !((attackPlayer || mobs && possibleTarget instanceof EntityMob
							|| animals && possibleTarget instanceof EntityAnimal)
							&& (!possibleTarget.isInvisible() || invisEntities))
					|| !possibleTarget.isEntityAlive()
					|| player.getDistance(
							possibleTarget) >= (radius > INVIS_RADIUS && !player.canEntityBeSeen(possibleTarget)
									? INVIS_RADIUS
									: radius)
					|| routineHandler.entityAge(possibleTarget) < lagAge) {
				continue;
			}
			if (attackPlayer && protectEnabled && protectRoutine.getAlias(possibleTarget.getName()) != null && !friends
					|| ((TeamRoutine) Lanius.getInstance().getRoutineRegistry().get("Team")).teammate(possibleTarget)) {
				continue;
			}
			if (targetEntities.contains(possibleTarget)) {
				hasTarget = true;
			}
			if (attackTimeMap.containsKey(possibleTarget) && currentTime - attackTimeMap.get(possibleTarget) >= 500L) {
				allAttacked = false;
			}
		}
		possibleTargets.clear();
		final EntityOtherPlayerMP renderEntity = ((FreecamRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Freecam")).getRenderEntity(),
				posEntity = ((BlinkRoutine) Lanius.getInstance().getRoutineRegistry().get("Blink")).getPosEntity();
		final boolean fullStrength = ((EntityPlayer) Lanius.mc.player)
				.getCooledAttackStrength(0.5F) >= getFloat("Strength").floatValue();
		final float prevPlayerYaw = player.rotationYaw, prevPlayerPitch = player.rotationPitch;
		final boolean attackReady = currentTime - attackStartTime > getInt("Delay").intValue() + NetworkUtils.lagTime();
		for (final Entity possibleTarget : sortedEntities) {
			final boolean attackPlayer = players && possibleTarget instanceof EntityOtherPlayerMP
					&& (!armor || InventoryUtils.hasArmor((EntityPlayer) possibleTarget))
					&& (!cfgHeldItem || InventoryUtils.hasHeldItem((EntityPlayer) possibleTarget));
			if (possibleTarget.equals(player) || possibleTarget.equals(renderEntity) || possibleTarget.equals(posEntity)
					|| !(possibleTarget instanceof EntityLivingBase)
					|| !((attackPlayer || mobs && possibleTarget instanceof EntityMob
							|| animals && possibleTarget instanceof EntityAnimal)
							&& (!possibleTarget.isInvisible() || invisEntities))
					|| !possibleTarget.isEntityAlive()
					|| player.getDistance(
							possibleTarget) >= (radius > INVIS_RADIUS && !player.canEntityBeSeen(possibleTarget)
									? INVIS_RADIUS
									: radius)) {
				continue;
			}
			if (attackPlayer && protectEnabled && protectRoutine.getAlias(possibleTarget.getName()) != null && !friends
					|| ((TeamRoutine) Lanius.getInstance().getRoutineRegistry().get("Team")).teammate(possibleTarget)) {
				continue;
			}
			possibleTargets.add(possibleTarget);
			attackEntities = true;
			if (hasTarget) {
				boolean facingEntity = false;
				if (targetEntities.contains(possibleTarget) && firstTarget) {
					facingEntity = MathHelper.faceEntity(player, possibleTarget, fov, MAX_INCREMENT);
					if (!facingEntity) {
						player.rotationYaw = prevYaw;
						player.rotationPitch = prevPitch;
					}
					executeMoreAttacks(facingEntity && attackReady && fullStrength, possibleTarget);
					firstTarget = false;
				}
				if (facingEntity && attackReady && fullStrength) {
					attackTarget = true;
				}
				continue;
			}
			if (attackTimeMap.containsKey(possibleTarget) && currentTime - attackTimeMap.get(possibleTarget) < 500L
					&& !allAttacked) {
				continue;
			}
			if (firstTarget) {
				final boolean facingEntity = MathHelper.faceEntity(player, possibleTarget, fov, MAX_INCREMENT);
				if (!facingEntity) {
					player.rotationYaw = prevYaw;
					player.rotationPitch = prevPitch;
				}
				executeMoreAttacks(facingEntity && attackReady && fullStrength, possibleTarget);
				if (facingEntity && attackReady && fullStrength) {
					if (!targetEntities.contains(possibleTarget)) {
						targetEntities.add(possibleTarget);
					}
					attackTarget = true;
				}
			} else if (!ncpEnabled) {
				if (!targetEntities.contains(possibleTarget)) {
					targetEntities.add(possibleTarget);
				}
				attackTarget = true;
			}
			firstTarget = false;
		}
		if (possibleTargets.isEmpty()) {
			resetCurrentItem();
			if (player.isActiveItemStackBlocking()
					&& InventoryUtils.isStackValid(Lanius.mc.player.inventory.getCurrentItem())
					&& !GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindUseItem)) {
				Lanius.mc.playerController.onStoppedUsingItem(player);
			}
		} else {
			if (getBoolean("Auto-block") && !player.isActiveItemStackBlocking()) {
				// Eric: Get the held stack from the inventory container since
				// the getHeldItem method will have not been updated yet
				final ItemStack mainItem = player.inventoryContainer
						.getSlot(player.inventory.currentItem + InventoryUtils.HOTBAR_BEGIN).getStack(),
						offItem = player.getHeldItem(EnumHand.OFF_HAND);
				EnumHand shieldHand = !RoutineUtils.viaVersionEnabled() || !InventoryUtils.isStackValid(mainItem)
						|| !(mainItem.getItem() instanceof ItemSword)
								? null
								: InventoryUtils.isStackValid(offItem) && offItem.getItem() == Items.SHIELD
										? EnumHand.OFF_HAND
										: EnumHand.MAIN_HAND;
				if (shieldHand == null) {
					for (final EnumHand hand : EnumHand.values()) {
						final ItemStack heldItem = player.getHeldItem(hand);
						if (InventoryUtils.isStackValid(heldItem) && heldItem.getItem() == Items.SHIELD
								&& (!RoutineUtils.viaVersionEnabled() || InventoryUtils.isStackValid(mainItem)
										&& mainItem.getItem() instanceof ItemSword)) {
							shieldHand = hand;
							break;
						}
					}
				}
				if (shieldHand != null) {
					blockingHand = shieldHand;
				}
			}
		}
		final double BLOCK_UNDER_OFF = -0.01D;
		final Material posMat = Lanius.mc.player.world.getBlockState(
				new BlockPos(Lanius.mc.player.posX, Lanius.mc.player.posY + BLOCK_UNDER_OFF, Lanius.mc.player.posZ))
				.getMaterial(),
				predictMat = Lanius.mc.player.world
						.getBlockState(new BlockPos(Lanius.mc.player.posX + Lanius.mc.player.motionX,
								Lanius.mc.player.posY + BLOCK_UNDER_OFF + Lanius.mc.player.motionY,
								Lanius.mc.player.posZ + Lanius.mc.player.motionZ))
						.getMaterial();
		// Eric: this is for NoCheatPlus' Angle check
		if (ncpEnabled && Lanius.mc.player.moveForward == 0.0F && Lanius.mc.player.moveStrafing == 0.0F
				&& Lanius.mc.player.onGround && !Lanius.mc.player.isInWater() && !Lanius.mc.player.isInLava()
				&& !(Boolean) ObfuscationReflectionHelper.getPrivateValue(Entity.class, Lanius.mc.player,
						"field_70134_J", "isInWeb")
				&& (!RoutineUtils.enabled("Jesus") || posMat != Material.WATER && posMat != Material.LAVA
						&& predictMat != Material.WATER && predictMat != Material.LAVA)
				&& possibleTargets.size() >= 2 && !targetEntities.isEmpty()) {
			if (angleBypass) {
				if (Lanius.mc.gameSettings.keyBindJump.isKeyDown()) {
					ReflectHelper.setValue(EntityLivingBase.class, Lanius.mc.player, 0, "field_70773_bE", "jumpTicks");
				} else {
					((EntityPlayer) Lanius.mc.player).jump();
				}
			}
			angleBypass = !angleBypass;
		}
		if (prevPlayerYaw != player.rotationYaw || prevPlayerPitch != player.rotationPitch) {
			HookManager.netHook.setServerYaw(player.rotationYaw);
			HookManager.netHook.setServerPitch(player.rotationPitch);
		}
		if (!getBoolean("Lockview")) {
			player.rotationYaw = prevYaw;
			player.rotationPitch = prevPitch;
		}
		if (attackReady) {
			attackStartTime = System.currentTimeMillis();
		}
		sendMovement();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdateLowest(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP) {
			return;
		}
		HookManager.netHook.forcePlayerPacket();
	}

	private void pressUseBind(final boolean pressed) {
		KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindUseItem.getKeyCode(), pressed);
	}

	@Override
	public void registerValues() {
		registerValue("Swing", true, "Determines whether or not to swing the player's item.");
		final String displayName = name();
		registerValue("Criticals", true, "Determines whether or not " + displayName + " will land critical hits.");
		registerValue("Disable on Death", true,
				"Determines whether or not to disable " + displayName + " when the player dies.");
		registerValue("Players", true, "Determines whether or not " + displayName + " should attack players.");
		registerValue("Mobs", true, "Determines whether or not " + displayName + " should attack mobs.");
		registerValue("Animals", true, "Determines whether or not " + displayName + " should attack animals.");
		registerValue("Invisible Entities", true,
				"Determines whether or not " + displayName + " will attack invisible entities.");
		registerValue("Friends", false, "Determines whether or not " + displayName + " will attack friends.");
		registerValue("Radius", 6.0F, 1.0F, 6.0F, "Determines how far " + displayName + " can reach from the player.");
		registerValue("FOV", 180.0F, 10.0F, 180.0F,
				"Specifies how much " + displayName + " will increment the player's yaw.");
		registerValue("Entity Age", 0, 0, 200,
				"Specifies how long " + displayName + " will wait before attacking a recently spawned entity.");
		registerValue("Delay", 0, 0, 10000, "Specifies how long " + displayName + " will wait before attacking.");
		registerValue("Strength", 1.0F, 0.0F, 1.0F,
				"Determines the percentage of strength " + displayName + " will attack at.");
		registerValue("Lockview", false,
				"Determines whether or not to lock the player's client-side rotations onto entities.");
		registerValue("More Attacks", false,
				"Determines whether or not to register attacks on the target more frequently.");
		registerValue("Auto-block", true, "Determines whether or not to make the player block.");
		registerValue("Armor", false, "Determines whether or not to only attack a player that has armor equipped.");
		registerValue("Held Item", false, "Determines whether or not to only attack a player that is holding an item.");
	}

	private void resetCurrentItem() {
		if (Lanius.mc.player != null) {
			final boolean ncpEnabled = RoutineUtils.ncpEnabled();
			if (!ncpEnabled && prevItem != InventoryUtils.INIT_SLOT) {
				InventoryUtils.switchItem(prevItem);
			} else if (ncpEnabled && stackSaved) {
				for (int hotbarIdx = InventoryUtils.HOTBAR_BEGIN; hotbarIdx < InventoryUtils.MAX_SLOT; hotbarIdx++) {
					final Slot slot = Lanius.mc.player.inventoryContainer.getSlot(hotbarIdx);
					final boolean slotHasStack = slot.getHasStack();
					if (!hasStack && !slotHasStack && !InventoryUtils.isStackValid(prevStack)
							|| hasStack && slotHasStack && InventoryUtils.isStackValid(prevStack)
									&& ItemStack.areItemStacksEqual(slot.getStack(), prevStack)) {
						InventoryUtils.clickWindow(hotbarIdx, Lanius.mc.player.inventory.currentItem, ClickType.SWAP);
						InventoryUtils.syncInventory();
						break;
					}
				}
			}
		}
		prevItem = InventoryUtils.INIT_SLOT;
		hasStack = stackSaved = false;
		prevStack = null;
		resetUseBind();
	}

	private void resetUseBind() {
		pressUseBind(GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindUseItem));
	}

	private boolean rightClick(final ItemStack currentItem, final EnumHand hand) {
		if (InventoryUtils.isStackValid(currentItem) && !Lanius.mc.player.isHandActive()) {
			HookManager.netHook.setUseFromCheat(true);
			if (Lanius.mc.playerController.processRightClick(Lanius.mc.player, Lanius.mc.world,
					hand) == EnumActionResult.SUCCESS) {
				Lanius.mc.entityRenderer.itemRenderer.resetEquippedProgress(hand);
				return true;
			}
		}
		return false;
	}

	private void sendMovement() {
		setIgnorePlayerPacket();
		HookManager.netHook.forcePlayerPacket();
		try {
			ReflectionHelper.findMethod(EntityPlayerSP.class, "onUpdateWalkingPlayer", "func_175161_p")
					.invoke(Lanius.mc.player);
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

	public void setIgnorePlayerPacket() {
		ignorePlayerPacket = true;
	}

	private void setWeaponIdx() {
		weaponIdx = InventoryUtils.INIT_SLOT;
	}

	private void switchWeapon(final int weaponSlot) {
		final int hotbarSlot = weaponSlot - InventoryUtils.HOTBAR_BEGIN;
		final boolean changeWeapon = hotbarSlot != Lanius.mc.player.inventory.currentItem;
		if (changeWeapon) {
			final Slot weapSlot = Lanius.mc.player.inventoryContainer.getSlot(weaponSlot);
			if (weapSlot.getHasStack() && weapSlot.getStack().getItem() instanceof ItemSword) {
				resetUseBind();
			} else {
				if (Lanius.mc.player.isHandActive()) {
					Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
				}
				pressUseBind(false);
			}
		}
		if (RoutineUtils.ncpEnabled()) {
			if (!stackSaved) {
				final Slot slot = Lanius.mc.player.inventoryContainer
						.getSlot(Lanius.mc.player.inventory.currentItem + InventoryUtils.HOTBAR_BEGIN);
				hasStack = slot.getHasStack();
				prevStack = hasStack ? slot.getStack() : null;
				stackSaved = true;
			}
			if (changeWeapon) {
				InventoryUtils.clickWindow(weaponSlot, Lanius.mc.player.inventory.currentItem, ClickType.SWAP);
				prevWeaponSlot = weaponSlot;
			}
		} else {
			if (changeWeapon) {
				prevWeaponSlot = Lanius.mc.player.inventory.currentItem + InventoryUtils.HOTBAR_BEGIN;
			}
			if (prevItem == InventoryUtils.INIT_SLOT) {
				prevItem = Lanius.mc.player.inventory.currentItem;
			}
			InventoryUtils.switchItem(hotbarSlot);
		}
	}

}