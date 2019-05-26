package org.bitbucket.lanius.routine.impl;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINE_WIDTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glIsEnabled;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class NukerRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private static final Comparator<BlockPos> selectedCmp = new Comparator<BlockPos>() {

		@Override
		public int compare(BlockPos o1, BlockPos o2) {
			// TODO Auto-generated method stub
			final double oneDist = distSqBlock(o1), twoDist = distSqBlock(o2);
			return Double.compare(oneDist, twoDist);
		}

	};

	private static double distSqBlock(final BlockPos pos) {
		if (Lanius.getInstance().getRoutineRegistry().get("Nuker").getBoolean("Reverse")) {
			return Lanius.mc.player.getDistanceSqToCenter(pos);
		}
		final double dX = Lanius.mc.player.posX - (pos.getX() + 0.5D),
				dY = Lanius.mc.player.posY - (pos.getY() + 0.5D) + 1.5D,
				dZ = Lanius.mc.player.posZ - (pos.getZ() + 0.5D);
		return dX * dX + dY * dY + dZ * dZ;
	}

	private BlockPos digBlock;
	private boolean ignorePlayerPacket, ignoreSelect, pressedUse, pressedAttack;
	private final List<BlockPos> selectedBlocks = new CopyOnWriteArrayList<BlockPos>(),
			targets = new CopyOnWriteArrayList<BlockPos>();

	private long startTime;

	public NukerRoutine() {
		super(Keyboard.KEY_N, false, Tab.WORLD);
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
		return "Automatically breaks or right-clicks selected blocks.";
	}

	@Override
	public String displayData() {
		return String.valueOf(selectedBlocks.size());
	}

	@Override
	public void init() {
		selectedBlocks.clear();
		targets.clear();
		if (digBlock != null && Lanius.mc.player != null && Lanius.mc.world != null) {
			Lanius.mc.player.connection.sendPacket(new CPacketPlayerDigging(
					CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, digBlock, EnumFacing.DOWN));
			Lanius.mc.world.sendBlockBreakProgress(Lanius.mc.player.getEntityId(), digBlock, -1);
			Lanius.mc.player.resetCooldown();
		}
		digBlock = null;
		pressedAttack = pressedUse = false;
	}

	private boolean isValidBlock(BlockPos pos) {
		float nukeRadius = getFloat("Nuke Radius").floatValue();
		boolean reverse = getBoolean("Reverse");
		GameType currentGameType = Lanius.mc.playerController.getCurrentGameType();
		return !(distSqBlock(pos) > nukeRadius * nukeRadius || (reverse
				? !Lanius.mc.world.getWorldBorder().contains(pos) || Lanius.mc.player.world.isAirBlock(pos)
				: !(!currentGameType.equals(GameType.ADVENTURE) && Lanius.mc.player.isAllowEdit()
						&& Lanius.mc.world.getWorldBorder().contains(pos) && !Lanius.mc.player.world.isAirBlock(pos)
						&& (currentGameType.isCreative() || Lanius.mc.world.getBlockState(pos)
								.getBlockHardness(Lanius.mc.world, pos) != -1.0F))))
				&& (!RoutineUtils.ncpEnabled() && Lanius.mc.player.capabilities.isCreativeMode
						|| Lanius.mc.player.world.getBlockState(pos).getMaterial() != Material.WATER
								&& Lanius.mc.player.world.getBlockState(pos)
										.getMaterial() != Material.LAVA) /* Eric: Hotfix for Jesus routine. */;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Nuker";
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
		if (pressedAttack) {
			KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindAttack.getKeyCode(),
					GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindAttack));
			pressedAttack = false;
		}
		if (pressedUse) {
			KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindUseItem.getKeyCode(),
					GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindUseItem));
			pressedUse = false;
		}
		if (System.currentTimeMillis() - startTime < getInt("Delay").intValue() + NetworkUtils.lagTime()
				&& digBlock == null) {
			return;
		}
		final float nukeRadius = getFloat("Nuke Radius").floatValue();
		final boolean reverse = getBoolean("Reverse");
		final boolean ncpEnabled = RoutineUtils.ncpEnabled();
		if (phase.equals(Phase.START)) {
			final float prevYaw = Lanius.mc.player.rotationYaw, prevPitch = Lanius.mc.player.rotationPitch;
			final float fov = getFloat("FOV").floatValue();
			boolean facedBlock = false;
			final ArrayList<BlockPos> blocks = new ArrayList<BlockPos>(selectedBlocks);
			Collections.sort(blocks, selectedCmp);
			final float MAX_FOV = 180.0F;
			final float prevYawHead = Lanius.mc.player.rotationYawHead;
			for (final BlockPos selectedPos : blocks) {
				if (!isValidBlock(selectedPos)) {
					continue;
				}
				if (ncpEnabled) {
					org.bitbucket.lanius.util.math.MathHelper.faceBlock(Lanius.mc.player, selectedPos, MAX_FOV,
							MAX_FOV);
					Lanius.mc.player.rotationYawHead = Lanius.mc.player.rotationYaw;
					final RayTraceResult traceResult = Lanius.mc.player.rayTrace(nukeRadius, 1.0F);
					Lanius.mc.player.rotationYawHead = prevYawHead;
					Lanius.mc.player.rotationYaw = prevYaw;
					Lanius.mc.player.rotationPitch = prevPitch;
					EnumFacing sideHit = selectedPos.getY() <= Lanius.mc.player.posY ? EnumFacing.UP : EnumFacing.DOWN;
					Vec3d hitVec = new Vec3d(selectedPos);
					if (!(traceResult != null && traceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)
							&& traceResult.getBlockPos().equals(selectedPos))) {
						continue;
					}
				}
				if (!org.bitbucket.lanius.util.math.MathHelper.faceBlock(Lanius.mc.player, selectedPos, fov, fov)) {
					Lanius.mc.player.rotationYaw = prevYaw;
					Lanius.mc.player.rotationPitch = prevPitch;
					continue;
				} else {
					facedBlock = true;
					if (!targets.contains(selectedPos)) {
						targets.add(selectedPos);
					}
					if (ncpEnabled || !Lanius.mc.player.capabilities.isCreativeMode) {
						break;
					}
				}
			}
			if (prevYaw != Lanius.mc.player.rotationYaw || prevPitch != Lanius.mc.player.rotationPitch) {
				HookManager.netHook.setServerYaw(Lanius.mc.player.rotationYaw);
				HookManager.netHook.setServerPitch(Lanius.mc.player.rotationPitch);
				setIgnorePlayerPacket();
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
				if (!getBoolean("Lockview")) {
					Lanius.mc.player.rotationYaw = prevYaw;
					Lanius.mc.player.rotationPitch = prevPitch;
				}
			}
		} else {
			final float MAX_FOV = 180.0F;
			final float prevYaw = Lanius.mc.player.rotationYaw, prevPitch = Lanius.mc.player.rotationPitch,
					prevYawHead = Lanius.mc.player.rotationYawHead;
			final String[] hitDelayMappings = new String[] { "field_78781_i", "blockHitDelay" };
			final boolean swing = getBoolean("Swing"), noParticles = getBoolean("No Particles"),
					optimize = getBoolean("Optimize") && Lanius.mc.player.capabilities.isCreativeMode && !ncpEnabled;
			boolean done = false;
			final BlockPos prevDigBlock = digBlock;
			for (final BlockPos target : targets) {
				if (!done) {
					org.bitbucket.lanius.util.math.MathHelper.faceBlock(Lanius.mc.player, target, MAX_FOV, MAX_FOV);
					Lanius.mc.player.rotationYawHead = Lanius.mc.player.rotationYaw;
					final RayTraceResult traceResult = Lanius.mc.player.rayTrace(nukeRadius, 1.0F);
					Lanius.mc.player.rotationYawHead = prevYawHead;
					Lanius.mc.player.rotationYaw = prevYaw;
					Lanius.mc.player.rotationPitch = prevPitch;
					EnumFacing sideHit = target.getY() <= Lanius.mc.player.posY ? EnumFacing.UP : EnumFacing.DOWN;
					Vec3d hitVec = new Vec3d(target);
					if (traceResult != null && traceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)
							&& traceResult.getBlockPos().equals(target)) {
						sideHit = traceResult.sideHit;
						hitVec = traceResult.hitVec;
						if (ncpEnabled && digBlock != null && !reverse && isValidBlock(target)) {
							if (!isValidBlock(digBlock)) {
								digBlock = target;
							} else {
								final float oldYaw = Lanius.mc.player.rotationYaw,
										oldPitch = Lanius.mc.player.rotationPitch,
										oldYawHead = Lanius.mc.player.rotationYawHead;
								org.bitbucket.lanius.util.math.MathHelper.faceBlock(Lanius.mc.player, digBlock, MAX_FOV,
										MAX_FOV);
								Lanius.mc.player.rotationYawHead = Lanius.mc.player.rotationYaw;
								final RayTraceResult traceResultDig = Lanius.mc.player.rayTrace(nukeRadius, 1.0F);
								Lanius.mc.player.rotationYawHead = oldYawHead;
								Lanius.mc.player.rotationYaw = oldYaw;
								Lanius.mc.player.rotationPitch = oldPitch;
								if (!(traceResultDig != null
										&& traceResultDig.typeOfHit.equals(RayTraceResult.Type.BLOCK)
										&& traceResultDig.getBlockPos().equals(digBlock))) {
									digBlock = target;
								}
							}
						}
					} else if (ncpEnabled) {
						continue;
					}
					if (reverse) {
						final ArrayList<EnumHand> hands = new ArrayList<EnumHand>();
						if (RoutineUtils.viaVersionEnabled()) {
							hands.add(EnumHand.MAIN_HAND);
						} else {
							for (final EnumHand hand : EnumHand.values()) {
								hands.add(hand);
							}
						}
						boolean useSent = true;
						for (final EnumHand hand : hands) {
							if (Lanius.mc.world.getBlockState(target).getMaterial() != Material.AIR) {
								final ItemStack stack = Lanius.mc.player.getHeldItem(hand);
								final int prevStackSz = InventoryUtils.isStackValid(stack) ? stack.getCount() : 0;
								if (Lanius.mc.gameSettings.keyBindUseItem.isKeyDown()) {
									KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindUseItem.getKeyCode(),
											false);
									pressedUse = true;
								}
								ignoreSelect = true;
								HookManager.netHook.setUseFromCheat(true);
								if (Lanius.mc.playerController.processRightClickBlock(Lanius.mc.player, Lanius.mc.world,
										target, sideHit, hitVec, hand) == EnumActionResult.SUCCESS) {
									if (swing) {
										if (optimize) {
											Lanius.mc.player.connection.sendPacket(new CPacketAnimation(hand));
										} else {
											Lanius.mc.player.swingArm(hand);
										}
									}
									if (InventoryUtils.isStackValid(stack)) {
										if (stack.getCount() == 0) {
											Lanius.mc.player.setHeldItem(hand, ItemStack.EMPTY);
										} else if (stack.getCount() != prevStackSz
												|| Lanius.mc.playerController.isInCreativeMode()) {
											Lanius.mc.entityRenderer.itemRenderer.resetEquippedProgress(hand);
										}
									}
									useSent = HookManager.netHook.isUseSent();
									break;
								}
								useSent = HookManager.netHook.isUseSent();
							}
						}
						if (useSent) {
							done = ncpEnabled;
							targets.remove(target);
							selectedBlocks.remove(target);
						} else {
							Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
						}
					} else {
						if (!isValidBlock(target)) {
							continue;
						}
						// Eric: Check for optimization:
						if (digBlock != null && (ncpEnabled || !Lanius.mc.player.capabilities.isCreativeMode)) {
							boolean contains = targets.contains(digBlock);
							if (!contains || !isValidBlock(digBlock)) {
								digBlock = target;
							} else if (contains && isValidBlock(digBlock) && !target.equals(digBlock)) {
								continue;
							}
						}
						if ((ncpEnabled || !Lanius.mc.player.capabilities.isCreativeMode) && prevDigBlock == null) {
							digBlock = target;
						}
						if (Lanius.mc.gameSettings.keyBindAttack.isKeyDown()) {
							KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindAttack.getKeyCode(), false);
							pressedAttack = true;
						}
						if ((Integer) ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class,
								Lanius.mc.playerController, hitDelayMappings) > 0) {
							ReflectHelper.setValue(PlayerControllerMP.class, Lanius.mc.playerController, 0,
									hitDelayMappings);
						}
						ignoreSelect = true;
						boolean packet = optimize && Lanius.mc.world.getWorldBorder().contains(target);
						if (packet) {
							Lanius.mc.player.connection.sendPacket(new CPacketPlayerDigging(
									CPacketPlayerDigging.Action.START_DESTROY_BLOCK, target, sideHit));
							ItemStack stack = Lanius.mc.player.getHeldItemMainhand();
							if (!Lanius.mc.world.extinguishFire(Lanius.mc.player, target, sideHit)
									&& (stack.isEmpty() || stack.getItem().canDestroyBlockInCreative(Lanius.mc.world,
											target, stack, Lanius.mc.player))) {
								Lanius.mc.world.setBlockToAir(target);
							}
						}
						if (packet || Lanius.mc.playerController.onPlayerDamageBlock(target, sideHit)) {
							if (!packet && !noParticles) {
								Lanius.mc.effectRenderer.addBlockHitEffects(target, traceResult);
							}
							if (swing) {
								if (optimize) {
									Lanius.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
								} else {
									Lanius.mc.player.swingArm(EnumHand.MAIN_HAND);
								}
							}
						}
						done = ncpEnabled || !Lanius.mc.player.capabilities.isCreativeMode;
						if (!ncpEnabled && Lanius.mc.player.capabilities.isCreativeMode || digBlock != null
								&& Lanius.mc.world.getBlockState(digBlock).getMaterial() == Material.AIR) {
							digBlock = null;
						}
						if (digBlock == null) {
							targets.remove(target);
						}
						if (Lanius.mc.world.isAirBlock(target)) {
							selectedBlocks.remove(target);
						}
					}
				}
			}
			ignoreSelect = false;
			if (reverse) {
				digBlock = null;
			}
			if (reverse || digBlock == null) {
				startTime = System.currentTimeMillis();
			}
		}
	}

	@SubscribeEvent
	public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock leftClickBlockEv) {
		final EntityPlayer entityPlayer = leftClickBlockEv.getEntityPlayer();
		if (getBoolean("Reverse") || !entityPlayer.equals(Lanius.mc.player) || entityPlayer instanceof EntityPlayerMP
				|| ignoreSelect) {
			return;
		}
		final float clickRadius = getFloat("Click Radius").floatValue();
		final int flooredRadius = MathHelper.floor(clickRadius);
		final BlockPos pos = leftClickBlockEv.getPos();
		final GameType currentGameType = Lanius.mc.playerController.getCurrentGameType();
		for (int y = flooredRadius + 1; y >= -flooredRadius - 1; y--) {
			for (int x = -flooredRadius - 1; x < flooredRadius + 1; x++) {
				for (int z = -flooredRadius - 1; z < flooredRadius + 1; z++) {
					final BlockPos selectedPos = new BlockPos(entityPlayer.posX + x, entityPlayer.posY + y,
							entityPlayer.posZ + z);
					if (distSqBlock(selectedPos) > clickRadius * clickRadius
							|| Lanius.mc.player.capabilities.isCreativeMode && selectedPos.equals(pos)
							|| !(!entityPlayer.world.isAirBlock(selectedPos)
									&& (currentGameType.isCreative() || Lanius.mc.world.getBlockState(selectedPos)
											.getBlockHardness(Lanius.mc.world, selectedPos) != -1.0F))
							|| selectedBlocks.contains(selectedPos)
							|| (!Lanius.mc.player.capabilities.isCreativeMode || RoutineUtils.ncpEnabled())
									&& (Lanius.mc.player.world.getBlockState(selectedPos)
											.getMaterial() == Material.WATER
											|| Lanius.mc.player.world.getBlockState(selectedPos)
													.getMaterial() == Material.LAVA)) {
						continue;
					}
					selectedBlocks.add(selectedPos);
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
		HookManager.netHook.forcePlayerPacket();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onRenderWorldLastLow(final RenderWorldLastEvent renderWorldLastEv) {
		if (selectedBlocks.isEmpty()) {
			return;
		}
		final boolean textureEnabled = glIsEnabled(GL_TEXTURE_2D), lightEnabled = glIsEnabled(GL_LIGHTING),
				depthEnabled = glIsEnabled(GL_DEPTH_TEST);
		if (textureEnabled) {
			GlStateManager.disableTexture2D();
		}
		if (lightEnabled) {
			GlStateManager.disableLighting();
		}
		if (depthEnabled) {
			GlStateManager.disableDepth();
		}
		final float prevLineWidth = glGetFloat(GL_LINE_WIDTH);
		GlStateManager.glLineWidth(getFloat("Line Width").floatValue());
		final ICamera camera = new Frustum();
		final Entity viewEntity = Lanius.mc.getRenderViewEntity();
		final float partialTicks = renderWorldLastEv.getPartialTicks();
		camera.setPosition(
				org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX,
						partialTicks),
				org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posY, viewEntity.lastTickPosY,
						partialTicks),
				org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posZ, viewEntity.lastTickPosZ,
						partialTicks));
		final int blockDist = (Lanius.mc.gameSettings.renderDistanceChunks * 2 + 1) * 16 / 2;
		final float nukeRadius = getFloat("Nuke Radius").floatValue();
		for (final BlockPos selectedPos : selectedBlocks) {
			if (!Lanius.mc.world.isAirBlock(selectedPos)) {
				continue;
			}
			selectedBlocks.remove(selectedPos);
		}
		// Eric: render backwards because depth test is disabled
		for (int selectedIdx = selectedBlocks.size() - 1; selectedIdx >= 0; selectedIdx--) {
			final BlockPos selectedPos = selectedBlocks.get(selectedIdx);
			final double deltaX = selectedPos.getX() - Lanius.mc.player.posX,
					deltaZ = selectedPos.getZ() - Lanius.mc.player.posZ;
			if (deltaX * deltaX + deltaZ * deltaZ > blockDist * blockDist) {
				continue;
			}
			final Color selectedCol = new Color(
					distSqBlock(selectedPos) <= nukeRadius * nukeRadius ? 0x00FF00 : 0xFF0000);
			final float red = selectedCol.getRed() / 255.0F, green = selectedCol.getGreen() / 255.0F,
					blue = selectedCol.getBlue() / 255.0F, alpha = selectedCol.getAlpha() / 255.0F;
			final IBlockState selectedState = Lanius.mc.world.getBlockState(selectedPos);
			if (SearchRoutine.doubleChest(Lanius.mc.world, selectedPos)) {
				for (final EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
					final BlockPos offPos = selectedPos.offset(facing);
					if (Lanius.mc.world.getBlockState(offPos).getBlock() == Blocks.CHEST) {
						final AxisAlignedBB blockBox = selectedState.getSelectedBoundingBox(Lanius.mc.world,
								selectedPos), offBox = selectedState.getSelectedBoundingBox(Lanius.mc.world, offPos),
								outlineBox = blockBox.expand(offBox.minX - blockBox.minX, offBox.minY - blockBox.minY,
										offBox.minZ - blockBox.minZ);
						if (camera.isBoundingBoxInFrustum(outlineBox)) {
							SearchRoutine.drawOutline(outlineBox, partialTicks, red, green, blue, alpha, this, true);
						}
					}
				}
			} else {
				final AxisAlignedBB outlineBox = selectedState.getSelectedBoundingBox(Lanius.mc.world, selectedPos);
				if (camera.isBoundingBoxInFrustum(outlineBox)) {
					SearchRoutine.drawOutline(outlineBox, partialTicks, red, green, blue, alpha, this, true);
				}
			}
		}
		GlStateManager.glLineWidth(prevLineWidth);
		if (textureEnabled) {
			GlStateManager.enableTexture2D();
		}
		if (lightEnabled) {
			GlStateManager.enableLighting();
		}
		if (depthEnabled) {
			GlStateManager.enableDepth();
		}
	}

	@SubscribeEvent
	public void onRightClickBlock(final PlayerInteractEvent.RightClickBlock rightClickBlockEv) {
		final EntityPlayer entityPlayer = rightClickBlockEv.getEntityPlayer();
		if (!getBoolean("Reverse") || !entityPlayer.equals(Lanius.mc.player) || entityPlayer instanceof EntityPlayerMP
				|| ignoreSelect) {
			return;
		}
		final float clickRadius = getFloat("Click Radius").floatValue();
		final int flooredRadius = MathHelper.floor(clickRadius);
		final BlockPos pos = rightClickBlockEv.getPos();
		for (int y = flooredRadius + 1; y >= -flooredRadius - 1; y--) {
			for (int x = -flooredRadius - 1; x < flooredRadius + 1; x++) {
				for (int z = -flooredRadius - 1; z < flooredRadius + 1; z++) {
					final BlockPos selectedPos = new BlockPos(entityPlayer.posX + x, entityPlayer.posY + y,
							entityPlayer.posZ + z);
					if (distSqBlock(selectedPos) > clickRadius * clickRadius || selectedPos.equals(pos)
							|| entityPlayer.world.isAirBlock(selectedPos) || selectedBlocks.contains(selectedPos)) {
						continue;
					}
					selectedBlocks.add(selectedPos);
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
		registerValue("Reverse", false, "Determines whether or not to right-click selected blocks instead.");
		registerValue("FOV", 180.0F, 10.0F, 180.0F, "Specifies how much to increment the player's rotations by.");
		registerValue("Nuke Radius", 6.0F, 1.0F, 6.0F, "Specifies the maximum interaction distance from the player.");
		registerValue("Click Radius", 8.0F, 2.0F, 16.0F,
				"Specifies the maximum distance from the position at which the player clicked.");
		registerValue("Swing", true, "Determines whether or not to swing the player's item.");
		registerValue("No Particles", true, "Determines whether or not to remove particles while breaking blocks.");
		registerValue("Optimize", true, "Specifies whether or not to optimize this routine whenever possible.");
		registerValue("Lockview", false,
				"Determines whether or not to lock the player's client-side rotations onto the selected blocks.");
		registerValue("Line Width", 1.5F, 1.0F, 3.0F, "Specifies the thickness of the outlines' lines.");
		registerValue("Fade", true, "Determines whether or not to make the outlines fade out.");
		registerValue("Delay", 0, 0, 10000, "Specifies how long to wait before nuking more blocks.");
	}

	public void setIgnorePlayerPacket() {
		ignorePlayerPacket = true;
	}

}
