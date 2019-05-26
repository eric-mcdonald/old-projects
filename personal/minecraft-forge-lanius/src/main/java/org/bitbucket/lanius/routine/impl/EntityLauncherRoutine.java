package org.bitbucket.lanius.routine.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityLauncherRoutine extends TabbedRoutine implements Hook<NetHandlerData> {
	private static final Comparator<BlockPos> rotationCmp = new Comparator<BlockPos>() {
		@Override
		public int compare(BlockPos o1, BlockPos o2) {
			// TODO Auto-generated method stub
			return Float.compare(rotateSqDist(o1), rotateSqDist(o2));
		}

		private float rotateSqDist(final BlockPos pos) {
			final float prevYaw = Lanius.mc.player.rotationYaw, prevPitch = Lanius.mc.player.rotationPitch;
			org.bitbucket.lanius.util.math.MathHelper.faceBlock(Lanius.mc.player, pos, 180.0F, 180.0F);
			float yawDelta = net.minecraft.util.math.MathHelper.wrapDegrees(Lanius.mc.player.rotationYaw)
					- net.minecraft.util.math.MathHelper.wrapDegrees(HookManager.netHook.getServerYaw()),
					pitchDelta = net.minecraft.util.math.MathHelper.wrapDegrees(Lanius.mc.player.rotationPitch)
							- net.minecraft.util.math.MathHelper.wrapDegrees(HookManager.netHook.getServerPitch());
			Lanius.mc.player.rotationYaw = prevYaw;
			Lanius.mc.player.rotationPitch = prevPitch;
			return yawDelta * yawDelta + pitchDelta * pitchDelta;
		}
	}, distCmp = new Comparator<BlockPos>() {

		@Override
		public int compare(BlockPos arg0, BlockPos arg1) {
			// TODO Auto-generated method stub
			return Double.compare(Lanius.mc.player.getDistanceSqToCenter(arg0),
					Lanius.mc.player.getDistanceSqToCenter(arg1));
		}

	};
	private boolean ignorePlayerPacket;
	private boolean pressedUse;
	private BlockPos usePos;
	private long startTime;
	private float prevReduction;
	private boolean executing;

	public EntityLauncherRoutine() {
		super(Keyboard.KEY_NONE, false, Tab.MISCELLANEOUS);
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
		return "Launches entities from spawner eggs.";
	}

	@Override
	public void init() {
		usePos = null;
		pressedUse = false;
		prevReduction = -1.0F;
		executing = false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Entity Launcher";
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
		if (!(data.retVal instanceof CPacketPlayer)) {
			return;
		}
		if (ignorePlayerPacket) {
			if (phase.equals(Phase.END)) {
				ignorePlayerPacket = false;
			}
			return;
		}
		if (!isEnabled()) {
			return;
		}
		if (pressedUse) {
			KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindUseItem.getKeyCode(),
					GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindUseItem));
			pressedUse = false;
		}
		if (System.currentTimeMillis() - startTime < getInt("Delay").intValue() + NetworkUtils.lagTime()) {
			return;
		}
		if (Lanius.mc.player.capabilities.isCreativeMode
				&& GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindUseItem)) {
			float maxDist = RoutineUtils.ncpEnabled() ? Lanius.mc.playerController.getBlockReachDistance() : 6.0F;
			if (phase.equals(Phase.START)) {
				final ArrayList<EnumHand> hands = new ArrayList<EnumHand>();
				if (RoutineUtils.viaVersionEnabled()) {
					hands.add(EnumHand.MAIN_HAND);
				} else {
					for (final EnumHand hand : EnumHand.values()) {
						hands.add(hand);
					}
				}
				boolean hasSpawnEgg = false;
				for (final EnumHand hand : hands) {
					final ItemStack stack = Lanius.mc.player.getHeldItem(hand);
					if (stack.getItem() != Items.SPAWN_EGG) {
						continue;
					}
					NBTTagCompound stackNbt = stack.getTagCompound();
					if (stackNbt == null || !stackNbt.hasKey("EntityTag", 10)) {
						continue;
					}
					hasSpawnEgg = true;
					break;
				}
				if (hasSpawnEgg) {
					final int flooredRadius = MathHelper.floor(maxDist);
					List<BlockPos> usablePos = new ArrayList<BlockPos>();
					for (int y = flooredRadius + 1; y >= -flooredRadius - 1; y--) {
						for (int x = -flooredRadius - 1; x < flooredRadius + 1; x++) {
							for (int z = -flooredRadius - 1; z < flooredRadius + 1; z++) {
								BlockPos pos = new BlockPos(Lanius.mc.player.posX + x, Lanius.mc.player.posY + y,
										Lanius.mc.player.posZ + z);
								if (Lanius.mc.player.world.isAirBlock(pos)
										|| Lanius.mc.player.getDistanceSqToCenter(pos) > maxDist * maxDist) {
									continue;
								}
								final float oldYaw = Lanius.mc.player.rotationYaw,
										oldPitch = Lanius.mc.player.rotationPitch,
										oldYawHead = Lanius.mc.player.rotationYawHead;
								if (!org.bitbucket.lanius.util.math.MathHelper.faceBlock(Lanius.mc.player, pos, 180.0F,
										180.0F)) {
									Lanius.mc.player.rotationYaw = oldYaw;
									Lanius.mc.player.rotationPitch = oldPitch;
									continue;
								}
								Lanius.mc.player.rotationYawHead = Lanius.mc.player.rotationYaw;
								final RayTraceResult traceResult = Lanius.mc.player.rayTrace(maxDist, 1.0F);
								Lanius.mc.player.rotationYawHead = oldYawHead;
								Lanius.mc.player.rotationYaw = oldYaw;
								Lanius.mc.player.rotationPitch = oldPitch;
								if (traceResult == null || !traceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)
										|| !traceResult.getBlockPos().equals(pos)) {
									continue;
								}
								usablePos.add(pos);
							}
						}
					}
					RayTraceResult realTrace = Lanius.mc.player
							.rayTrace(Lanius.mc.playerController.getBlockReachDistance(), 1.0F);
					if (!usablePos.isEmpty() && (realTrace == null
							|| !realTrace.typeOfHit.equals(RayTraceResult.Type.BLOCK)
							|| Lanius.mc.world.getBlockState(realTrace.getBlockPos()).getMaterial() == Material.AIR)) {
						Collections.sort(usablePos, rotationCmp);
						Collections.sort(usablePos, distCmp);
						BlockPos bestPos = getBoolean("Fire Always") ? usablePos.get(0) : null;
						float savedHeadYaw = Lanius.mc.player.rotationYawHead,
								savedPrevHead = Lanius.mc.player.prevRotationYawHead;
						Lanius.mc.player.rotationYawHead = Lanius.mc.player.rotationYaw;
						Lanius.mc.player.prevRotationYawHead = Lanius.mc.player.prevRotationYaw;
						final Vec3d lookVec = Lanius.mc.player.getLookVec();
						Lanius.mc.player.rotationYawHead = savedHeadYaw;
						Lanius.mc.player.prevRotationYawHead = savedPrevHead;
						float multiplier = getFloat("Multiplier").floatValue();
						double xVel = lookVec.x * multiplier, yVel = lookVec.y * multiplier,
								zVel = lookVec.z * multiplier;
						for (BlockPos pos : usablePos) {
							final float prevYaw = Lanius.mc.player.rotationYaw,
									prevPitch = Lanius.mc.player.rotationPitch,
									prevYawHead = Lanius.mc.player.rotationYawHead;
							org.bitbucket.lanius.util.math.MathHelper.faceBlock(Lanius.mc.player, pos, 180.0F, 180.0F);
							Lanius.mc.player.rotationYawHead = Lanius.mc.player.rotationYaw;
							final RayTraceResult traceResult = Lanius.mc.player.rayTrace(maxDist, 1.0F);
							Lanius.mc.player.rotationYawHead = prevYawHead;
							Lanius.mc.player.rotationYaw = prevYaw;
							Lanius.mc.player.rotationPitch = prevPitch;
							if (traceResult != null && traceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)
									&& traceResult.getBlockPos().equals(pos)) {
								AxisAlignedBB blockBox = Lanius.mc.world.getBlockState(pos)
										.getCollisionBoundingBox(Lanius.mc.world, pos);
								if (blockBox == null || blockBox.equals(Block.NULL_AABB)) {
									blockBox = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D,
											pos.getY() + 1.0D, pos.getZ() + 1.0D);
								} else {
									blockBox = blockBox.offset(pos);
								}
								blockBox = blockBox.offset(new BlockPos(traceResult.sideHit.getDirectionVec()));
								if (!Lanius.mc.world.collidesWithAnyBlock(blockBox.expand(xVel, yVel, zVel))) {
									bestPos = pos;
									break;
								}
							}
						}
						if (bestPos != null) {
							usePos = bestPos;
							if (RoutineUtils.ncpEnabled()) {
								final float oldYaw = Lanius.mc.player.rotationYaw,
										oldPitch = Lanius.mc.player.rotationPitch;
								org.bitbucket.lanius.util.math.MathHelper.faceBlock(Lanius.mc.player, usePos, 180.0F,
										180.0F);
								float serverYaw = Lanius.mc.player.rotationYaw,
										serverPitch = Lanius.mc.player.rotationPitch;
								Lanius.mc.player.rotationYaw = oldYaw;
								Lanius.mc.player.rotationPitch = oldPitch;
								HookManager.netHook.setServerYaw(serverYaw);
								HookManager.netHook.setServerPitch(serverPitch);
								setIgnorePlayerPacket();
								try {
									ReflectionHelper
											.findMethod(EntityPlayerSP.class, "onUpdateWalkingPlayer", "func_175161_p")
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
						}
					}
				}
			} else if (usePos != null) {
				final ArrayList<EnumHand> hands = new ArrayList<EnumHand>();
				if (RoutineUtils.viaVersionEnabled()) {
					hands.add(EnumHand.MAIN_HAND);
				} else {
					for (final EnumHand hand : EnumHand.values()) {
						hands.add(hand);
					}
				}
				final float prevYaw = Lanius.mc.player.rotationYaw, prevPitch = Lanius.mc.player.rotationPitch,
						prevYawHead = Lanius.mc.player.rotationYawHead;
				org.bitbucket.lanius.util.math.MathHelper.faceBlock(Lanius.mc.player, usePos, 180.0F, 180.0F);
				Lanius.mc.player.rotationYawHead = Lanius.mc.player.rotationYaw;
				final RayTraceResult traceResult = Lanius.mc.player.rayTrace(maxDist, 1.0F);
				Lanius.mc.player.rotationYawHead = prevYawHead;
				Lanius.mc.player.rotationYaw = prevYaw;
				Lanius.mc.player.rotationPitch = prevPitch;
				EnumFacing sideHit = usePos.getY() <= Lanius.mc.player.posY ? EnumFacing.UP : EnumFacing.DOWN;
				Vec3d hitVec = new Vec3d(usePos);
				RayTraceResult realTrace = Lanius.mc.player.rayTrace(Lanius.mc.playerController.getBlockReachDistance(),
						1.0F);
				if (traceResult != null && traceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)
						&& traceResult.getBlockPos().equals(usePos)
						&& (realTrace == null || !realTrace.typeOfHit.equals(RayTraceResult.Type.BLOCK)
								|| Lanius.mc.world.getBlockState(realTrace.getBlockPos())
										.getMaterial() == Material.AIR)) {
					sideHit = traceResult.sideHit;
					hitVec = traceResult.hitVec;
					boolean useSent = true;
					for (final EnumHand hand : hands) {
						if (Lanius.mc.world.getBlockState(usePos).getMaterial() != Material.AIR) {
							final ItemStack stack = Lanius.mc.player.getHeldItem(hand);
							if (stack.getItem() != Items.SPAWN_EGG) {
								continue;
							}
							NBTTagCompound stackNbt = stack.getTagCompound();
							if (stackNbt == null || !stackNbt.hasKey("EntityTag", 10)) {
								continue;
							}
							NBTTagCompound entityTag = stackNbt.getCompoundTag("EntityTag");
							boolean hasMotion = entityTag.hasKey("Motion", 9);
							NBTTagList oldMotion = hasMotion ? entityTag.getTagList("Motion", 6) : new NBTTagList();
							if (!hasMotion) {
								for (int i = 0; i < 3; i++) {
									oldMotion.appendTag(new NBTTagDouble(0.0D));
								}
							}
							NBTTagList motionList = new NBTTagList();
							float savedHeadYaw = Lanius.mc.player.rotationYawHead,
									savedPrevHead = Lanius.mc.player.prevRotationYawHead;
							Lanius.mc.player.rotationYawHead = Lanius.mc.player.rotationYaw;
							Lanius.mc.player.prevRotationYawHead = Lanius.mc.player.prevRotationYaw;
							final Vec3d lookVec = Lanius.mc.player.getLookVec();
							Lanius.mc.player.rotationYawHead = savedHeadYaw;
							Lanius.mc.player.prevRotationYawHead = savedPrevHead;
							float multiplier = getFloat("Multiplier").floatValue();
							motionList.appendTag(new NBTTagDouble(lookVec.x * multiplier));
							motionList.appendTag(new NBTTagDouble(lookVec.y * multiplier));
							motionList.appendTag(new NBTTagDouble(lookVec.z * multiplier));
							entityTag.setTag("Motion", motionList);
							Lanius.mc.playerController.sendSlotPacket(stack,
									InventoryUtils.HOTBAR_BEGIN + Lanius.mc.player.inventory.currentItem);
							final int prevStackSz = InventoryUtils.isStackValid(stack) ? stack.getCount() : 0;
							if (Lanius.mc.gameSettings.keyBindUseItem.isKeyDown()) {
								KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
								while (Lanius.mc.gameSettings.keyBindUseItem.isPressed()) {
								}
								pressedUse = true;
							}
							if (Lanius.mc.playerController.processRightClickBlock(Lanius.mc.player, Lanius.mc.world,
									usePos, sideHit, hitVec, hand) == EnumActionResult.SUCCESS) {
								if (getBoolean("Swing")) {
									Lanius.mc.player.swingArm(hand);
								}
								if (InventoryUtils.isStackValid(stack)) {
									if (stack.getCount() == 0) {
										Lanius.mc.player.setHeldItem(hand, ItemStack.EMPTY);
									} else if (stack.getCount() != prevStackSz
											|| Lanius.mc.playerController.isInCreativeMode()) {
										Lanius.mc.entityRenderer.itemRenderer.resetEquippedProgress(hand);
									}
								}
								startTime = System.currentTimeMillis();
								useSent = HookManager.netHook.isUseSent();
								entityTag.setTag("Motion", oldMotion);
								Lanius.mc.playerController.sendSlotPacket(stack,
										InventoryUtils.HOTBAR_BEGIN + Lanius.mc.player.inventory.currentItem);
								executing = true;
								break;
							}
							startTime = System.currentTimeMillis();
							entityTag.setTag("Motion", oldMotion);
							Lanius.mc.playerController.sendSlotPacket(stack,
									InventoryUtils.HOTBAR_BEGIN + Lanius.mc.player.inventory.currentItem);
							useSent = HookManager.netHook.isUseSent();
							executing = true;
						}
					}
					if (!useSent) {
						Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
					}
				}
				usePos = null;
			}
		}
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

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdateLowest(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP) {
			return;
		}
		if (System.currentTimeMillis() - startTime >= getInt("Delay").intValue() + NetworkUtils.lagTime()) {
			executing = false;
		}
		HookManager.netHook.forcePlayerPacket();
	}

	@SubscribeEvent
	public void onRenderLivingPre(final RenderLivingEvent.Pre<EntityLivingBase> renderLivingPreEv) {
		float noRenderDist = getFloat("No Render").floatValue();
		if (noRenderDist == 0.0F) {
			return;
		}
		if (executing && Lanius.mc.player.capabilities.isCreativeMode
				&& GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindUseItem)) {
			final EntityLivingBase entity = renderLivingPreEv.getEntity();
			if (Lanius.mc.player.getDistance(entity) <= noRenderDist) {
				final ArrayList<EnumHand> hands = new ArrayList<EnumHand>();
				if (RoutineUtils.viaVersionEnabled()) {
					hands.add(EnumHand.MAIN_HAND);
				} else {
					for (final EnumHand hand : EnumHand.values()) {
						hands.add(hand);
					}
				}
				ResourceLocation entityRes = EntityList.getKey(entity);
				String entityId = entityRes == null ? "" : entityRes.toString();
				for (final EnumHand hand : hands) {
					final ItemStack stack = Lanius.mc.player.getHeldItem(hand);
					if (stack.getItem() != Items.SPAWN_EGG) {
						continue;
					}
					NBTTagCompound stackNbt = stack.getTagCompound();
					if (stackNbt == null || !stackNbt.hasKey("EntityTag", 10)) {
						continue;
					}
					NBTTagCompound entityTag = stackNbt.getCompoundTag("EntityTag");
					if (!entityTag.hasKey("id", 8)) {
						continue;
					}
					if (entityId.equals(entityTag.getString("id"))) {
						renderLivingPreEv.setCanceled(true);
						break;
					}
				}
			}
		}
	}

	@Override
	@SubscribeEvent
	public void onUnload(final WorldEvent.Unload unloadEv) {
		super.onUnload(unloadEv);
		startTime = 0L;
	}

	@Override
	public void registerValues() {
		registerValue("Multiplier", 10.0F, 0.1F, 10.0F, "Specifies the amount to multiply the motion by.");
		registerValue("Swing", true, "Determines whether or not to swing the player's item.");
		registerValue("Delay", 0, 0, 10000, "Specifies how long to wait before launching more entities.");
		registerValue("Fire Always", true, "Determines whether or not to always fire even if it might miss.");
		registerValue("No Collision", true, "Determines whether or not to collide with entities.");
		registerValue("No Render", 2.0F, 0.0F, 10.0F,
				"Specifies the maximum distance to not render an entity near the player.");
	}

	public void setIgnorePlayerPacket() {
		ignorePlayerPacket = true;
	}
}
