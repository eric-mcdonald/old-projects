package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;
import java.util.List;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.CollisionUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.bitbucket.lanius.util.concurrent.Rate;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class StepRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private boolean allowPlayerPacket;
	private boolean prevAutoJump = Lanius.mc.gameSettings.autoJump, setAutoJump;
	private AxisAlignedBB prevEntityBox;
	private double prevPosX, prevPosY, prevPosZ;

	public StepRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.MOVEMENT);
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
		return "Steps over high blocks.";
	}

	@Override
	public void init() {
		if (Lanius.mc.player != null) {
			Lanius.mc.player.stepHeight = 0.5F;
		}
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Step";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!isEnabled() || !phase.equals(Phase.START) || !(data.retVal instanceof CPacketPlayer)
				|| !((Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class,
						(CPacketPlayer) data.retVal, "field_149480_h", "moving"))
				|| allowPlayerPacket) {
			allowPlayerPacket = false;
			return;
		}
		final double deltaY = Lanius.mc.player.posY - prevPosY;
		if (RoutineUtils.ncpEnabled() && Lanius.mc.player.stepHeight == getFloat("Height").floatValue()
				&& prevPosX != Lanius.mc.player.posX && deltaY > 0.5F && deltaY <= 1.0F
				&& prevPosZ != Lanius.mc.player.posZ
				&& (Lanius.mc.player.onGround || !Lanius.mc.player.world
						.getCollisionBoxes(Lanius.mc.player, Lanius.mc.player.getEntityBoundingBox()
								.offset(SpeedRoutine.NO_MOVE, SpeedRoutine.GROUND_OFF, SpeedRoutine.NO_MOVE))
						.isEmpty())) {
			final AxisAlignedBB entityBB = Lanius.mc.player.getEntityBoundingBox();
			Lanius.mc.player.setEntityBoundingBox(prevEntityBox);
			final Rate<CPacketPlayer> sendQueue = Lanius.getInstance().getPlayerPacketRate();
			final AxisAlignedBB entityBox = Lanius.mc.player.getEntityBoundingBox();
			final double contractVec = 0.0625D;
			double minY = 0.42D;
			for (int packet = 0; packet < 2; packet++) {
				double yOffset = minY;
				final List<AxisAlignedBB> collisionBoxes = Lanius.mc.player.world.getCollisionBoxes(Lanius.mc.player,
						entityBox.grow(-contractVec, 0.0D, -contractVec).expand(0.0D, yOffset, 0.0D));
				for (int boxIdx = 0; boxIdx < collisionBoxes.size(); boxIdx++) {
					yOffset = collisionBoxes.get(boxIdx).calculateYOffset(entityBox, yOffset);
				}
				allowPlayerPacket = true;
				if (!sendQueue.execute(new CPacketPlayer.Position(prevPosX, prevPosY + yOffset, prevPosZ, false), 1)) {
					break;
				}
				minY += minY - 0.0834D - 0.021D + 0.0144D;
			}
			Lanius.mc.player.setEntityBoundingBox(entityBB);
			final double motionY = (deltaY < 1.0D ? 1.15D : 1.0D) - deltaY;
			if (motionY > 0.0D) {
				Lanius.mc.player.move(MoverType.SELF, 0.0D, motionY, 0.0D);
				final CPacketPlayer playerPacket = (CPacketPlayer) data.retVal;
				allowPlayerPacket = true;
				sendQueue.execute(new CPacketPlayer.Position(playerPacket.getX(0.0D),
						playerPacket.getY(0.0D) + (1.0D - deltaY), playerPacket.getZ(0.0D), false), 1);
				ReflectionHelper.setPrivateValue(CPacketPlayer.class, playerPacket, playerPacket.getY(0.0D) + motionY,
						"field_149477_b", "y");
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdateLowest(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP
				|| Lanius.mc.player.movementInput == null) {
			return;
		}
		prevEntityBox = entityLiving.getEntityBoundingBox();
		prevPosX = entityLiving.posX;
		prevPosY = entityLiving.posY;
		prevPosZ = entityLiving.posZ;
		entityLiving.stepHeight = 0.5F;
		final float stepHeight = getFloat("Height").floatValue();
		if (!RoutineUtils.ncpEnabled() || ((EntityPlayer) entityLiving).capabilities.allowFlying) {
			entityLiving.stepHeight = stepHeight;
			resetAutoJump();
		} else {
			init();
			final Material predictMat = entityLiving.world
					.getBlockState(new BlockPos(entityLiving.posX + entityLiving.motionX,
							entityLiving.posY - JesusRoutine.BLOCK_OFF + entityLiving.motionY,
							entityLiving.posZ + entityLiving.motionZ))
					.getMaterial(),
					posMat = entityLiving.world.getBlockState(new BlockPos(entityLiving.posX,
							entityLiving.posY - JesusRoutine.BLOCK_OFF, entityLiving.posZ)).getMaterial();
			if ((entityLiving.onGround || !Lanius.mc.player.world.getCollisionBoxes(Lanius.mc.player,
					Lanius.mc.player.getEntityBoundingBox().offset(SpeedRoutine.NO_MOVE, SpeedRoutine.GROUND_OFF,
							SpeedRoutine.NO_MOVE))
					.isEmpty())
					&& !entityLiving.isInWater() && !entityLiving.isInLava()
					&& !(Boolean) ObfuscationReflectionHelper.getPrivateValue(Entity.class, entityLiving,
							"field_70134_J", "isInWeb")
					&& (entityLiving.moveForward != 0.0F || entityLiving.moveStrafing != 0.0F)
					&& !(RoutineUtils.enabled("Jesus") && (posMat == Material.WATER || posMat == Material.LAVA
							|| predictMat == Material.WATER || predictMat == Material.LAVA))) {
				final double prevMotionX = entityLiving.motionX, prevMotionZ = entityLiving.motionZ;
				entityLiving.motionX = entityLiving.motionZ = 0.0D;
				entityLiving.moveRelative(Lanius.mc.player.movementInput.moveStrafe, 0.0F,
						Lanius.mc.player.movementInput.moveForward,
						(float) ((SpeedRoutine) Lanius.getInstance().getRoutineRegistry().get("Speed"))
								.baseSpeed(entityLiving));
				double y = 0.0D;
				final List<AxisAlignedBB> collisionBoxes = entityLiving.world.getCollisionBoxes(entityLiving,
						entityLiving.getEntityBoundingBox().expand(entityLiving.motionX, y, entityLiving.motionZ));
				entityLiving.motionX = prevMotionX;
				entityLiving.motionZ = prevMotionZ;
				for (int boxCount = 0, boxesSz = collisionBoxes.size(); boxCount < boxesSz; ++boxCount) {
					final double yOffset = collisionBoxes.get(boxCount).maxY - entityLiving.getEntityBoundingBox().minY;
					if (yOffset > y) {
						y = yOffset;
					}
				}
				if (!setAutoJump) {
					prevAutoJump = Lanius.mc.gameSettings.autoJump;
					setAutoJump = true;
				}
				Lanius.mc.gameSettings.autoJump = false;
				if (stepHeight > 0.5F && stepHeight <= 1.0F && Lanius.getInstance().getPlayerPacketRate()
						.canExecute(y > 0.5D && y < 1.0D ? 3 : 2, y > 0.5D && y < 1.0D ? 1 : 0)) {
					entityLiving.stepHeight = stepHeight;
				} else if (entityLiving.collidedHorizontally && !CollisionUtils.collidesWall(entityLiving, true)) {
					if (Lanius.mc.gameSettings.keyBindJump.isKeyDown()) {
						ReflectHelper.setValue(EntityLivingBase.class, entityLiving, 0, "field_70773_bE", "jumpTicks");
					} else {
						((EntityPlayer) entityLiving).jump();
						entityLiving.motionY = y / 2.7117623904D;
						if (y != 1.0D) {
							entityLiving.motionY += y == 0.5625D ? 0.06D
									: y == 0.75D ? 0.04D : y == 0.8125D ? 0.03D : 0.02D;
						}
						if (entityLiving.isPotionActive(MobEffects.JUMP_BOOST)) {
							entityLiving.motionY += (entityLiving.getActivePotionEffect(MobEffects.JUMP_BOOST)
									.getAmplifier() + 1) * 0.1F;
						}
					}
				}
			}
		}
	}

	@Override
	public void registerValues() {
		registerValue("Height", 1.0F, 0.6F, 9.0F, "Specifies the maximum height of a block that can be stepped over.");
	}

	private void resetAutoJump() {
		if (!setAutoJump) {
			return;
		}
		Lanius.mc.gameSettings.autoJump = prevAutoJump;
		setAutoJump = false;
	}

	@Override
	public void setEnabled() {
		super.setEnabled();
		if (!isEnabled()) {
			resetAutoJump();
		}
	}

	/**
	 * Retrieves whether or not NoCheatPlus Step will execute. Note that the
	 * conditions in this method must match the conditions of the actual execution
	 * (except for certain conditions like being on the ground).
	 * 
	 * @return whether or not NoCheatPlus Step will execute
	 */
	boolean willNcpStep() {
		final Material predictMat = Lanius.mc.player.world
				.getBlockState(new BlockPos(Lanius.mc.player.posX + Lanius.mc.player.motionX,
						Lanius.mc.player.posY - JesusRoutine.BLOCK_OFF + Lanius.mc.player.motionY,
						Lanius.mc.player.posZ + Lanius.mc.player.motionZ))
				.getMaterial(),
				posMat = Lanius.mc.player.world.getBlockState(new BlockPos(Lanius.mc.player.posX,
						Lanius.mc.player.posY - JesusRoutine.BLOCK_OFF, Lanius.mc.player.posZ)).getMaterial();
		return isEnabled() && RoutineUtils.ncpEnabled() && !Lanius.mc.player.capabilities.allowFlying
				&& !Lanius.mc.player.isInWater() && !Lanius.mc.player.isInLava()
				&& !(Boolean) ObfuscationReflectionHelper.getPrivateValue(Entity.class, Lanius.mc.player,
						"field_70134_J", "isInWeb")
				&& (Lanius.mc.player.moveForward != 0.0F || Lanius.mc.player.moveStrafing != 0.0F)
				&& !(RoutineUtils.enabled("Jesus") && (posMat == Material.WATER || posMat == Material.LAVA
						|| predictMat == Material.WATER || predictMat == Material.LAVA))
				&& Lanius.mc.player.collidedHorizontally && !CollisionUtils.collidesWall(Lanius.mc.player, true);
	}

}
