package org.bitbucket.lanius.routine.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.hook.impl.SlowdownData;
import org.bitbucket.lanius.hook.impl.SoulSandData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.CollisionUtils;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.bitbucket.lanius.util.concurrent.Rate;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class SpeedRoutine extends TabbedRoutine implements Hook<SlowdownData> {
	static final double GROUND_OFF = -0.001D;
	private static final double INIT_SPEED = 0.0D, INIT_POS = -999.0D;
	private static final int INIT_STATE = 0, HACC_MAX_MOVES = 14;
	static final float MOVE_MULT = 0.98F;
	static final float NO_MOVE = 0.0F;
	private final List<Block> moddedBlocks = new ArrayList<Block>();

	public final Hook<NetHandlerData> netHook = new Hook<NetHandlerData>() {
		@Override
		public void onExecute(final NetHandlerData data, final Phase phase) {
			// TODO Auto-generated method stub
			final boolean packetPlayer = data.retVal instanceof CPacketPlayer;
			if (phase.equals(Phase.START)) {
				if (packetPlayer) {
					final CPacketPlayer playerPacket = (CPacketPlayer) data.retVal;
					// Eric: hotfix for Speed making the player take damage
					if (!playerPacket.isOnGround() && !Lanius.mc.player.world
							.getCollisionBoxes(Lanius.mc.player,
									Lanius.mc.player.getEntityBoundingBox().offset(NO_MOVE, GROUND_OFF, NO_MOVE))
							.isEmpty()) {
						ReflectHelper.setValue(CPacketPlayer.class, playerPacket, true, "field_149474_g", "onGround");
					}
				}
				if (data.retVal instanceof SPacketPlayerPosLook) {
					moveSpeed = INIT_SPEED;
					setState();
					wasReset = true;
					hVelocity = 0.0D;
					return;
				}
				final boolean stopSprint = data.retVal instanceof CPacketEntityAction
						&& ((CPacketEntityAction) data.retVal).getAction() == CPacketEntityAction.Action.STOP_SPRINTING;
				if (sprinting) {
					if (stopSprint) {
						data.retVal = sprint();
					} else if (packetPlayer) {
						Lanius.mc.player.connection.sendPacket(sprint());
					}
				}
				final CPacketPlayer playerPacket;
				double playerX = INIT_POS, playerY = INIT_POS, playerZ = INIT_POS;
				if (packetPlayer
						&& (!((Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class,
								playerPacket = (CPacketPlayer) data.retVal, "field_149480_h", "moving"))
								|| Math.sqrt(Math.pow((playerX = playerPacket.getX(0.0D)) - oldX, 2.0D)
										+ Math.pow((playerY = playerPacket.getY(0.0D)) - oldY, 2.0D)
										+ Math.pow((playerZ = playerPacket.getZ(0.0D)) - oldZ, 2.0D)) <= 0.0625D)
						|| !isEnabled() || !stopSprint && !packetPlayer || !getBoolean("No Slowdown")) {
					return;
				}
				if (packetPlayer) {
					final boolean packetUse = !Lanius.mc.player.isActiveItemStackBlocking()
							&& RoutineUtils.viaVersionEnabled()
							&& InventoryUtils.isStackValid(Lanius.mc.player.getHeldItemMainhand())
							&& Lanius.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword
							&& !GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindUseItem)
							&& HookManager.netHook.isUsingItem();
					if (RoutineUtils.ncpEnabled() && prevOnGround
							&& (Lanius.mc.player.isActiveItemStackBlocking() || RoutineUtils.viaVersionEnabled()
									&& InventoryUtils.isStackValid(Lanius.mc.player.getHeldItemMainhand())
									&& Lanius.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword
									&& (GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindUseItem)
											|| HookManager.netHook.isUsingItem()))
							&& !Lanius.mc.player.isRiding() && !Lanius.mc.player.capabilities.allowFlying) {
						data.source.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
								BlockPos.ORIGIN, EnumFacing.DOWN));
						reblock = !packetUse;
					}
					if (!(playerX == INIT_POS && playerY == INIT_POS && playerZ == INIT_POS)) {
						oldX = playerX;
						oldY = playerY;
						oldZ = playerZ;
					}
				}
			} else if (packetPlayer) {
				prevOnGround = Lanius.mc.player.onGround
						|| !Lanius.mc.player.world
								.getCollisionBoxes(Lanius.mc.player,
										Lanius.mc.player.getEntityBoundingBox().offset(NO_MOVE, GROUND_OFF, NO_MOVE))
								.isEmpty();
				if (reblock) {
					HookManager.netHook.setUseFromCheat(true);
					if (RoutineUtils.viaVersionEnabled() && !Lanius.mc.player.isActiveItemStackBlocking()) {
						if (Lanius.mc.playerController.processRightClick(Lanius.mc.player, Lanius.mc.world,
								EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS) {
							Lanius.mc.entityRenderer.itemRenderer.resetEquippedProgress(EnumHand.MAIN_HAND);
						}
					} else {
						data.source.sendPacket(
								new CPacketPlayerTryUseItem(RoutineUtils.viaVersionEnabled() ? EnumHand.MAIN_HAND
										: Lanius.mc.player.getActiveHand()));
					}
					if (!HookManager.netHook.isUseSent()) {
						Lanius.mc.playerController.onStoppedUsingItem(Lanius.mc.player);
					}
					reblock = false;
				}
			}
		}

		private CPacketEntityAction sprint() {
			Lanius.mc.player.setSprinting(true);
			sprinting = false;
			return new CPacketEntityAction(Lanius.mc.player, CPacketEntityAction.Action.START_SPRINTING);

		}
	}, velNetHook = new Hook<NetHandlerData>() {

		@Override
		public void onExecute(final NetHandlerData data, final Phase phase) {
			// TODO Auto-generated method stub
			if (!phase.equals(Phase.START)) {
				return;
			}
			if (data.retVal instanceof SPacketEntityVelocity) {
				final SPacketEntityVelocity entityVelPacket = (SPacketEntityVelocity) data.retVal;
				final Entity velEntity = Lanius.mc.world.getEntityByID(entityVelPacket.getEntityID());
				if (velEntity != null && velEntity.equals(Lanius.mc.player)) {
					if (velEntity.isRiding()) {
						hVelocity = 0.0D;
					} else if (isEnabled()) {
						hVelocity += Math.sqrt(Math.pow(entityVelPacket.getMotionX() / 8000.0D, 2.0D)
								+ Math.pow(entityVelPacket.getMotionZ() / 8000.0D, 2.0D));
						data.retVal = null;
					}
				}
			} else if (data.retVal instanceof SPacketRespawn) {
				hVelocity = 0.0D;
			}
		}

	};

	private double oldX, oldY, oldZ, moveSpeed, hVelocity;

	public final Hook<SoulSandData> soulSandHook = new Hook<SoulSandData>() {

		@Override
		public void onExecute(final SoulSandData data, final Phase phase) {
			// TODO Auto-generated method stub
			if (phase.equals(Phase.START) && data.entity.equals(Lanius.mc.player) && isEnabled()
					&& getBoolean("No Slowdown")) {
				data.retVal = 1.0D;
			}
		}

	};

	private boolean sprinting, reblock, prevOnGround, pressedJump, fastDown, fastLowhop, wasReset, hopping;

	private int state, haccCount = 30, haccMoves;

	public SpeedRoutine() {
		super(Keyboard.KEY_R, false, Tab.MOVEMENT);
		// TODO Auto-generated constructor stub
	}

	double baseSpeed(final EntityLivingBase entityLiving) {
		double speed = 0.221D;
		if (entityLiving == null) {
			return speed;
		}
		final boolean sprinting = entityLiving.isSprinting();
		if (sprinting) {
			speed *= 1.30000002D;
		}
		if (entityLiving.isPotionActive(MobEffects.SPEED)) {
			speed *= 1.2D; // Eric: Speed II screws up when the amplifier is
			// applied...
		} else {
			final double SLOWNESS_MULT = 0.29D;
			if (sprinting && speed < SLOWNESS_MULT && entityLiving.isPotionActive(MobEffects.SLOWNESS)) {
				speed = SLOWNESS_MULT;
			}
		}
		return speed;
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Moves the player at the specified pace.";
	}

	float distToGround(final EntityLivingBase entityLiving, final boolean interpolate) {
		return MathHelper.abs((float) (toGroundMotion(entityLiving, interpolate) - GROUND_OFF));
	}

	boolean execSilent() {
		final float distGround = distToGround(Lanius.mc.player, false);
		return isEnabled() && getBoolean("Silent") && hopping && !Lanius.mc.player.onGround && distGround > 0.0F
				&& distGround <= (getBoolean("Y-port") ? 1.0F : 2.0F);
	}

	private void executeBhop(final EntityLivingBase entityLiving, final boolean jump) {
		if (((LongJumpRoutine) Lanius.getInstance().getRoutineRegistry().get("Long Jump")).isJumped()) {
			setState();
			return;
		}
		final double baseSpeed = baseSpeed(entityLiving);
		if (moveSpeed == INIT_SPEED) {
			moveSpeed = baseSpeed;
		}
		double diff = org.bitbucket.lanius.util.math.MathHelper.INVALID_MAX_Y;
		AxisAlignedBB blockBB = null;
		if ((entityLiving.motionX != NO_MOVE || entityLiving.motionZ != NO_MOVE) && entityLiving.onGround
				&& !Lanius.mc.player.capabilities.isFlying && !entityLiving.isInWater() && !entityLiving.isOnLadder()
				&& !entityLiving.world.getCollisionBoxes(entityLiving,
						entityLiving.getEntityBoundingBox().expand(entityLiving.motionX, NO_MOVE, entityLiving.motionZ))
						.isEmpty()) {
			final AxisAlignedBB boundingBox = entityLiving.getEntityBoundingBox();
			final double OFFSET_Y = 0.5D;
			final List collisionBoxes = entityLiving.world.getCollisionBoxes(entityLiving,
					boundingBox.expand(entityLiving.motionX, OFFSET_Y, entityLiving.motionZ));
			for (final Object collisionBox : collisionBoxes) {
				diff = ((AxisAlignedBB) collisionBox).calculateYOffset(boundingBox,
						diff == org.bitbucket.lanius.util.math.MathHelper.INVALID_MAX_Y ? OFFSET_Y : diff);
			}
			boundingBox.offset(NO_MOVE,
					diff == org.bitbucket.lanius.util.math.MathHelper.INVALID_MAX_Y ? OFFSET_Y : diff, NO_MOVE);
			if (!collisionBoxes.isEmpty()) {
				blockBB = (AxisAlignedBB) collisionBoxes.get(collisionBoxes.size() - 1);
				diff = boundingBox.maxY - blockBB.maxY;
			}
		}
		if (!jump || diff != org.bitbucket.lanius.util.math.MathHelper.INVALID_MAX_Y
				&& (diff > 1.79D || diff > 1.1D && diff < 1.8D || diff < 0.8D)
				&& org.bitbucket.lanius.util.math.MathHelper.calculateStep(entityLiving.getEntityBoundingBox().minY,
						blockBB != null ? blockBB.maxY : org.bitbucket.lanius.util.math.MathHelper.INVALID_MAX_Y,
						entityLiving.onGround) <= entityLiving.stepHeight) {
			setState();
			return;
		}
		hopping = true;
		final boolean yPort = getBoolean("Y-port");
		if (entityLiving.onGround
				|| !Lanius.mc.player.world
						.getCollisionBoxes(Lanius.mc.player,
								Lanius.mc.player.getEntityBoundingBox().offset(NO_MOVE, GROUND_OFF, NO_MOVE))
						.isEmpty()) {
			fastDown = fastLowhop = true;
			executeLowJump(entityLiving);
		} else if (yPort && fastDown
				&& entityLiving.motionY < (entityLiving.isPotionActive(MobEffects.JUMP_BOOST)
						? -0.438174222343538D - entityLiving.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier()
								* 0.0709384004514778D
						: -0.22768848754498797D)) {
			final float newMotY = toGroundMotion(entityLiving, false);
			if (distToGround(entityLiving, false) <= 3.0F && newMotY < -0.29773507F) {
				entityLiving.motionY = newMotY; // Eric:
				// negative
				// absolute
				// to
				// ensure
				// that
				// the
				// player
				// goes
				// downward.
			} else {
				fastDown = false;
				executeLowJump(entityLiving);
			}
		} else {
			executeLowJump(entityLiving);
		}
		if (Lanius.mc.gameSettings.keyBindJump.isKeyDown()) {
			pressJump(false);
		}
		if (hVelocity > 0.0D) {
			haccMoves = 0;
		}
		if (haccCount <= HACC_MAX_MOVES) {
			if (hVelocity <= 0.0D) {
				haccMoves = HACC_MAX_MOVES;
				haccCount = 29;
			}
			if (moveSpeed > baseSpeed) {
				hVelocity -= moveSpeed;
				hVelocity = Math.max(hVelocity, 0.0D);
			}
		}
		if (getBoolean("H-acc") && haccMoves > 0) {
			negateMotion(entityLiving);
			entityLiving.moveRelative(Lanius.mc.player.movementInput.moveStrafe, 0.0F,
					Lanius.mc.player.movementInput.moveForward, yPort ? (float) baseSpeed : 0.18F);
			setState();
			--haccMoves;
			return;
		}
		final double BASE_OFF = 0.0001D;
		if ((Lanius.mc.player.movementInput.moveForward != NO_MOVE
				|| Lanius.mc.player.movementInput.moveStrafe != NO_MOVE) && entityLiving.onGround
				&& state == INIT_STATE) {
			final double prevMotX = entityLiving.motionX, prevMotZ = entityLiving.motionZ;
			Lanius.mc.player.jump();
			entityLiving.motionY -= 0.02D;
			entityLiving.motionX = prevMotX;
			entityLiving.motionZ = prevMotZ;
			setMoveSpeed(2.15D * baseSpeed - BASE_OFF, baseSpeed + BASE_OFF);
			++state;
		} else if (state == 1) {
			setMoveSpeed(-(0.66D * (moveSpeed - baseSpeed) - moveSpeed) - (yPort ? BASE_OFF : 0.0D),
					baseSpeed + BASE_OFF);
			++state;
		} else if (state >= 2) {
			if (!entityLiving.onGround && entityLiving.motionY < 0.0D
					&& !entityLiving.world
							.getCollisionBoxes(entityLiving,
									entityLiving.getEntityBoundingBox().offset(NO_MOVE, entityLiving.motionY, NO_MOVE))
							.isEmpty()
					|| entityLiving.onGround) {
				setState();
			}
			setMoveSpeed(-(moveSpeed / 160.0D - moveSpeed), baseSpeed + BASE_OFF);
		}
		negateMotion(entityLiving);
		entityLiving.moveRelative(Lanius.mc.player.movementInput.moveStrafe, 0.0F,
				Lanius.mc.player.movementInput.moveForward, (float) moveSpeed);
		if (moveSpeed > baseSpeed) {
			--haccCount;
		}
	}

	private void executeLowJump(final EntityLivingBase entityLiving) {
		if (!getBoolean("Y-port")
				|| ((StepRoutine) Lanius.getInstance().getRoutineRegistry().get("Step")).willNcpStep()) {
			return;
		}
		if (distToGround(entityLiving, false) > 1.0F) {
			fastLowhop = false;
			return;
		}
		final int SCALE = 3;
		final double deltaY = org.bitbucket.lanius.util.math.MathHelper
				.round(entityLiving.posY - MathHelper.floor(entityLiving.posY), SCALE);
		if (deltaY == org.bitbucket.lanius.util.math.MathHelper.round(0.4D, SCALE)) {
			moveVertically(entityLiving, 0.31D);
		} else if (deltaY == org.bitbucket.lanius.util.math.MathHelper.round(0.71D, SCALE)) {
			moveVertically(entityLiving, 0.04D);
		} else if (deltaY == org.bitbucket.lanius.util.math.MathHelper.round(0.75D, SCALE) && fastLowhop) {
			moveVertically(entityLiving, toGroundMotion(entityLiving, false));
		} else if (deltaY < org.bitbucket.lanius.util.math.MathHelper.round(1.0D, SCALE) && fastLowhop) {
			moveVertically(entityLiving, toGroundMotion(entityLiving, false));
		}
	}

	@Override
	public void init() {
		sprinting = false;
		moveSpeed = INIT_SPEED;
		setState();
		if (Lanius.mc.player != null) {
			Lanius.mc.player.setSprinting(false);
		}
		pressedJump = false;
		fastDown = true;
	}

	private void moveVertically(final EntityLivingBase entityLiving, final double motionY) {
		entityLiving.move(MoverType.SELF, NO_MOVE, motionY, NO_MOVE);
		entityLiving.motionY = NO_MOVE;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Speed";
	}

	private void negateMotion(final EntityLivingBase entityLiving) {
		entityLiving.motionX = entityLiving.motionZ = NO_MOVE;
		float f6 = 0.91F;
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos
				.retain(entityLiving.posX, entityLiving.getEntityBoundingBox().minY - 1.0D, entityLiving.posZ);
		if (entityLiving.onGround) {
			f6 = entityLiving.world.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91F;
		}
		float f7 = 0.16277136F / (f6 * f6 * f6), f8;
		if (entityLiving.onGround && entityLiving.motionY >= 0.0D) {
			f8 = entityLiving.getAIMoveSpeed() * f7;
		} else {
			f8 = entityLiving.jumpMovementFactor;
		}
		double d0 = entityLiving.posY;
		float f1 = 0.0F, f2 = 0.0F, f3 = 0.0F;
		try {
			f1 = (Float) ReflectionHelper.findMethod(EntityLivingBase.class, "getWaterSlowDown", "func_189749_co")
					.invoke(entityLiving);
			f2 = 0.02F;
			f3 = EnchantmentHelper.getDepthStriderModifier(entityLiving);
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
		if (f3 > 3.0F) {
			f3 = 3.0F;
		}
		if (!entityLiving.onGround) {
			f3 *= 0.5F;
		}
		if (f3 > 0.0F) {
			f1 += (0.54600006F - f1) * f3 / 3.0F;
			f2 += (entityLiving.getAIMoveSpeed() - f2) * f3 / 3.0F;
		}
		entityLiving.moveRelative(entityLiving.moveStrafing * MOVE_MULT, 0.0F, entityLiving.moveForward * MOVE_MULT,
				!entityLiving.isInWater()
						|| entityLiving instanceof EntityPlayer && ((EntityPlayer) entityLiving).capabilities.isFlying
								? !entityLiving.isInLava() || entityLiving instanceof EntityPlayer
										&& ((EntityPlayer) entityLiving).capabilities.isFlying ? f8 : 0.02F
								: f2);
		entityLiving.motionX = -entityLiving.motionX;
		entityLiving.motionZ = -entityLiving.motionZ;
	}

	@SubscribeEvent
	public void onCameraSetup(final EntityViewRenderEvent.CameraSetup cameraSetupEv) {
		if (execSilent()) {
			final float roll = cameraSetupEv.getRoll(), pitch = cameraSetupEv.getPitch(), yaw = cameraSetupEv.getYaw(),
					partialTicks = (float) cameraSetupEv.getRenderPartialTicks();
			GlStateManager.rotate(roll, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(0.0F, distToGround(Lanius.mc.player, true), 0.0F);
			GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-pitch, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-roll, 0.0F, 0.0F, 1.0F);
		}
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || Lanius.mc.isGamePaused()) {
			return;
		}
		sprinting = false;
		if (pressedJump) {
			pressJump(GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindJump));
			pressedJump = false;
		}
	}

	@Override
	public void onExecute(final SlowdownData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.END) || !isEnabled() || !getBoolean("No Slowdown")) {
			return;
		}
		final float MOVE_MULT = 5.0F;
		data.retVal[0] *= MOVE_MULT;
		data.retVal[1] *= MOVE_MULT;
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		if (Lanius.mc.player == null || Lanius.mc.player.movementInput == null || RoutineUtils.enabled("Freecam")) {
			return;
		}
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		final float prevStrafing = entityLiving.moveStrafing, prevForward = entityLiving.moveForward;
		final boolean playerEntity = entityLiving.equals(Lanius.mc.player) && !(entityLiving instanceof EntityPlayerMP);
		if (playerEntity) {
			// Eric: hotfix for Speed breaking the vanilla flight toggle
			final boolean prevJump = Lanius.mc.player.movementInput.jump;
			Lanius.mc.player.movementInput.updatePlayerMoveState();
			// Eric: only update move variables to avoid breaking the hand
			// rotate animation
			entityLiving.moveStrafing = Lanius.mc.player.movementInput.moveStrafe;
			entityLiving.moveForward = Lanius.mc.player.movementInput.moveForward;
			Lanius.mc.player.movementInput.jump = prevJump;
		}
		final boolean movingForward = MathHelper.abs(Lanius.mc.player.movementInput.moveForward) > NO_MOVE,
				strafing = MathHelper.abs(Lanius.mc.player.movementInput.moveStrafe) > NO_MOVE,
				hMove = movingForward || strafing;
		if (playerEntity) {
			prevOnGround = entityLiving.onGround
					|| !Lanius.mc.player.world
							.getCollisionBoxes(Lanius.mc.player,
									Lanius.mc.player.getEntityBoundingBox().offset(NO_MOVE, GROUND_OFF, NO_MOVE))
							.isEmpty();
			hopping = false;
		}
		if (!playerEntity || !hMove || wasReset) {
			if (playerEntity && !hMove) {
				init();
			}
			entityLiving.moveStrafing = prevStrafing;
			entityLiving.moveForward = prevForward;
			wasReset &= !prevOnGround;
			return;
		}
		final boolean ncpEnabled = RoutineUtils.ncpEnabled();
		final float cfgMult = getFloat("Multiplier").floatValue();
		final EntityPlayerSP playerSp = (EntityPlayerSP) entityLiving;
		final int extraMoves = MathHelper.ceil((cfgMult - NetworkUtils.lagTime() / 1000.0F) * 20.0F - 20.0F);
		final Rate<CPacketPlayer> playerPacketRate = Lanius.getInstance().getPlayerPacketRate();
		if (entityLiving.isOnLadder() && entityLiving.motionY > 0.0D) {
			if (ncpEnabled && !playerSp.capabilities.allowFlying) {
				// Eric: NoCheatPlus patched the old fast ladder
				for (int moveCount = 0; moveCount < extraMoves && playerPacketRate.canExecute(1, 0); moveCount++) {
					prevOnGround = entityLiving.onGround || !Lanius.mc.player.world
							.getCollisionBoxes(Lanius.mc.player,
									Lanius.mc.player.getEntityBoundingBox().offset(NO_MOVE, GROUND_OFF, NO_MOVE))
							.isEmpty();
					entityLiving.travel(entityLiving.moveStrafing * MOVE_MULT, 0.0F,
							entityLiving.moveForward * MOVE_MULT);
					if (!playerPacketRate.execute(new CPacketPlayer.Position(entityLiving.posX,
							entityLiving.getEntityBoundingBox().minY, entityLiving.posZ, entityLiving.onGround), 0)) {
						break;
					}
				}
			} else {
				entityLiving.motionY *= cfgMult;
			}
			entityLiving.moveStrafing = prevStrafing;
			entityLiving.moveForward = prevForward;
			return;
		}
		final boolean sprintFood = playerSp.getFoodStats().getFoodLevel() > 6.0F || playerSp.capabilities.allowFlying;
		// Eric: NCP Speed seems to break when you move backwards/sideways
		// regardless of whether or not you're sprinting on the server
		entityLiving
				.setSprinting((ncpEnabled && cfgMult > 1.0F ? Lanius.mc.player.movementInput.moveForward > 0.0F : hMove)
						&& sprintFood);
		sprinting = entityLiving.isSprinting();
		Material predictMat = entityLiving.world.getBlockState(new BlockPos(entityLiving.posX + entityLiving.motionX,
				entityLiving.posY - JesusRoutine.BLOCK_OFF + entityLiving.motionY,
				entityLiving.posZ + entityLiving.motionZ)).getMaterial();
		final IBlockState posState = entityLiving.world.getBlockState(
				new BlockPos(entityLiving.posX, entityLiving.posY - JesusRoutine.BLOCK_OFF, entityLiving.posZ));
		final Block posBlock = posState.getBlock();
		Material posMat = posState.getMaterial();
		final boolean normSpeed = ncpEnabled && cfgMult > 1.0F && !playerSp.capabilities.allowFlying,
				iceUnder = posMat == Material.ICE || posMat == Material.PACKED_ICE,
				jesusEnabled = RoutineUtils.enabled("Jesus");
		if (normSpeed && !iceUnder && !playerSp.capabilities.isFlying && !RoutineUtils.flyEnabled()
				&& !RoutineUtils.noclipEnabled()) {
			boolean timer = !CollisionUtils.collidesWall(entityLiving, true) && hMove,
					jump = !(entityLiving.isInWater() || entityLiving.isInLava()
							|| (Boolean) ObfuscationReflectionHelper.getPrivateValue(Entity.class, entityLiving,
									"field_70134_J", "isInWeb")
							|| jesusEnabled && (posMat == Material.WATER || posMat == Material.LAVA
									|| predictMat == Material.WATER || predictMat == Material.LAVA))
							&& !entityLiving.isOnLadder() && timer && !entityLiving.isSneaking();
			if (timer && getBoolean("Extra Movement")) {
				for (int moveCount = 0; moveCount < extraMoves && playerPacketRate.canExecute(1, 0); moveCount++) {
					prevOnGround = entityLiving.onGround || !Lanius.mc.player.world
							.getCollisionBoxes(Lanius.mc.player,
									Lanius.mc.player.getEntityBoundingBox().offset(NO_MOVE, GROUND_OFF, NO_MOVE))
							.isEmpty();
					// Eric: predict the player's motion variables first
					executeBhop(entityLiving, jump);
					entityLiving.travel(entityLiving.moveStrafing * MOVE_MULT, 0.0F,
							entityLiving.moveForward * MOVE_MULT);
					posMat = entityLiving.world.getBlockState(new BlockPos(entityLiving.posX,
							entityLiving.posY - JesusRoutine.BLOCK_OFF, entityLiving.posZ)).getMaterial();
					predictMat = entityLiving.world.getBlockState(new BlockPos(entityLiving.posX + entityLiving.motionX,
							entityLiving.posY - JesusRoutine.BLOCK_OFF + entityLiving.motionY,
							entityLiving.posZ + entityLiving.motionZ)).getMaterial();
					timer = !CollisionUtils.collidesWall(entityLiving, true) && hMove;
					jump = !(entityLiving.isInWater() || entityLiving.isInLava()
							|| (Boolean) ObfuscationReflectionHelper.getPrivateValue(Entity.class, entityLiving,
									"field_70134_J", "isInWeb")
							|| jesusEnabled && (posMat == Material.WATER || posMat == Material.LAVA
									|| predictMat == Material.WATER || predictMat == Material.LAVA))
							&& !entityLiving.isOnLadder() && timer && !entityLiving.isSneaking();
					// entityLiving.motionX = prevMotX;
					// entityLiving.motionY = prevMotY;
					// entityLiving.motionZ = prevMotZ;
					if (!playerPacketRate.execute(new CPacketPlayer.Position(entityLiving.posX,
							entityLiving.getEntityBoundingBox().minY, entityLiving.posZ, entityLiving.onGround), 0)) {
						break;
					}
				}
			}
			prevOnGround = entityLiving.onGround
					|| !Lanius.mc.player.world
							.getCollisionBoxes(Lanius.mc.player,
									Lanius.mc.player.getEntityBoundingBox().offset(NO_MOVE, GROUND_OFF, NO_MOVE))
							.isEmpty();
			if (entityLiving.isInWater() || entityLiving.isInLava() || (Boolean) ObfuscationReflectionHelper
					.getPrivateValue(Entity.class, entityLiving, "field_70134_J", "isInWeb")) {
				haccCount = 30;
				haccMoves = 0;
			}
			executeBhop(entityLiving, jump);
		} else if (cfgMult != 1.0F
				&& (!RoutineUtils.flyEnabled() || !(RoutineUtils.ncpEnabled() && !RoutineUtils.viaVersionEnabled()))
				&& !((LongJumpRoutine) Lanius.getInstance().getRoutineRegistry().get("Long Jump")).isJumped()) {
			negateMotion(entityLiving);
			entityLiving.moveRelative(Lanius.mc.player.movementInput.moveStrafe, 0.0F,
					Lanius.mc.player.movementInput.moveForward, (float) (baseSpeed(entityLiving) * cfgMult));
		}
		if (entityLiving.onGround && iceUnder) {
			posBlock.slipperiness = 0.6F;
			if (!moddedBlocks.contains(posBlock)) {
				moddedBlocks.add(posBlock);
			}
		}
		entityLiving.moveStrafing = prevStrafing;
		entityLiving.moveForward = prevForward;
	}

	@Override
	@SubscribeEvent
	public void onUnload(final WorldEvent.Unload unloadEv) {
		super.onUnload(unloadEv);
		final Iterator<Block> blockIt = moddedBlocks.iterator(); // Eric:
		// ConcurrentModificationException
		// hotfix
		while (blockIt.hasNext()) {
			blockIt.next().slipperiness = 0.98F;
			blockIt.remove();
		}
		fastLowhop = true;
		wasReset = false;
		hopping = false;
		haccCount = 30;
		haccMoves = 0;
		hVelocity = 0.0D;
	}

	private void pressJump(final boolean pressed) {
		KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindJump.getKeyCode(), pressed);
		pressedJump = true;
	}

	@Override
	public void registerValues() {
		registerValue("No Slowdown", true, "Determines whether or not " + name()
				+ " will prevent the player from slowing down while using items and while walking on soul sand.");
		registerValue("Multiplier", 1.7F, 0.1F, 9.0F, "Specifies the speed the player will move at.");
		registerValue("Y-port", true, "Determines whether or not to teleport the player to the ground.");
		registerValue("Silent", true, "Determines whether or not to bunny-hop without moving the camera.");
		registerValue("H-acc", true, "Determines whether or not to compensate for NoCheatPlus's hacc check.");
		registerValue("Extra Movement", true,
				"Determines whether or not to send extra movement packets to move faster.");
	}

	@Override
	public void setEnabled() {
		super.setEnabled();
		if (!isEnabled()) {
			fastLowhop = true;
			wasReset = false;
			hopping = false;
		}
	}

	private void setMoveSpeed(final double moveSpeed, final double minSpeed) {
		this.moveSpeed = moveSpeed;
		this.moveSpeed = Math.max(this.moveSpeed, minSpeed);
	}

	void setSprinting() {
		sprinting = false;
	}

	private void setState() {
		state = INIT_STATE;
	}

	private float toGroundMotion(final EntityLivingBase entityLiving, final boolean interpolate) {
		int groundY = MathHelper.floor(entityLiving.posY - 0.20000000298023224D);
		for (; Lanius.mc.world
				.getBlockState(new BlockPos(MathHelper.floor(entityLiving.posX + entityLiving.motionX), groundY,
						MathHelper.floor(entityLiving.posZ + entityLiving.motionZ)))
				.getCollisionBoundingBox(Lanius.mc.world,
						new BlockPos(MathHelper.floor(entityLiving.posX + entityLiving.motionX), groundY,
								MathHelper.floor(entityLiving.posZ + entityLiving.motionZ))) == null
				|| Lanius.mc.world
						.getBlockState(new BlockPos(MathHelper.floor(entityLiving.posX + entityLiving.motionX), groundY,
								MathHelper.floor(entityLiving.posZ + entityLiving.motionZ)))
						.getCollisionBoundingBox(Lanius.mc.world,
								new BlockPos(MathHelper.floor(entityLiving.posX + entityLiving.motionX), groundY,
										MathHelper.floor(entityLiving.posZ + entityLiving.motionZ)))
						.equals(Block.NULL_AABB); groundY--) {
			if (groundY <= 0) {
				break;
			}
		}
		final AxisAlignedBB groundBox = Lanius.mc.world
				.getBlockState(new BlockPos(MathHelper.floor(entityLiving.posX + entityLiving.motionX), groundY,
						MathHelper.floor(entityLiving.posZ + entityLiving.motionZ)))
				.getCollisionBoundingBox(Lanius.mc.world,
						new BlockPos(MathHelper.floor(entityLiving.posX + entityLiving.motionX), groundY,
								MathHelper.floor(entityLiving.posZ + entityLiving.motionZ)));
		return -MathHelper.abs((float) (groundY
				+ ((groundBox == null || groundBox.equals(Block.NULL_AABB) ? 1.0D : groundBox.maxY - groundBox.minY)
						+ GROUND_OFF)
				- (interpolate
						? org.bitbucket.lanius.util.math.MathHelper.interpolate(entityLiving.posY,
								entityLiving.lastTickPosY,
								((Timer) ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Lanius.mc,
										"field_71428_T", "timer")).renderPartialTicks)
						: entityLiving.posY)));
	}
}