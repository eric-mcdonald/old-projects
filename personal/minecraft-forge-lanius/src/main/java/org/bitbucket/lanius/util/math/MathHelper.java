package org.bitbucket.lanius.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bitbucket.lanius.Lanius;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public final class MathHelper {
	private static final float FACE_PRECISION = 0.1F;
	public static final double INVALID_MAX_Y = -999.0D;

	public static float calculateStep(final double playerMinY, final double blockMaxY, final boolean ground) {
		if (blockMaxY == INVALID_MAX_Y || !ground) {
			return 0.0F;
		}
		final double yDiff = blockMaxY - playerMinY;
		final int flooredDiff = net.minecraft.util.math.MathHelper.floor(yDiff);
		final double diff = yDiff - flooredDiff;
		return flooredDiff + (diff <= 0.5D ? diff == 0.0D ? 0.0F : 0.5F : 1.0F);
	}

	public static double distance(final double x1, final double y1, final double z1, final double x2, final double y2,
			final double z2) {
		return Math.sqrt(Math.pow(x2 - x1, 2.0D) + Math.pow(y2 - y1, 2.0D) + Math.pow(z2 - z1, 2.0D));
	}

	public static boolean faceBlock(final EntityPlayer player, final BlockPos targetPos, final float maxIncYaw,
			final float maxIncPitch) {
		final AxisAlignedBB targetBox = Lanius.mc.world.getBlockState(targetPos).getSelectedBoundingBox(Lanius.mc.world,
				targetPos);
		final double deltaX = (targetBox.minX + targetBox.maxX) / 2.0D - player.posX,
				deltaY = (targetBox.minY + targetBox.maxY) / 2.0D - (player.posY + player.getEyeHeight()),
				deltaZ = (targetBox.minZ + targetBox.maxZ) / 2.0D - player.posZ;
		final double hDistance = net.minecraft.util.math.MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
		final float calcYaw = (float) (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI - 90.0F),
				calcPitch = (float) -(Math.atan2(deltaY, hDistance) * 180.0D / Math.PI);
		player.rotationPitch = updateRotation(player.rotationPitch, calcPitch, maxIncPitch);
		player.rotationYaw = updateRotation(player.rotationYaw, calcYaw, maxIncYaw);
		return net.minecraft.util.math.MathHelper
				.abs(net.minecraft.util.math.MathHelper.wrapDegrees(player.rotationPitch)
						- net.minecraft.util.math.MathHelper.wrapDegrees(calcPitch)) < FACE_PRECISION
				&& net.minecraft.util.math.MathHelper
						.abs(net.minecraft.util.math.MathHelper.wrapDegrees(player.rotationYaw)
								- net.minecraft.util.math.MathHelper.wrapDegrees(calcYaw)) < FACE_PRECISION;
	}

	/**
	 * Changes pitch and yaw so that the entity calling the function is facing the
	 * entity provided as an argument.
	 */
	public static boolean faceEntity(final EntityPlayer player, final Entity targetEntity, final float maxIncYaw,
			final float maxIncPitch) {
		final double deltaX = targetEntity.posX - player.posX, deltaZ = targetEntity.posZ - player.posZ, deltaY;
		if (targetEntity instanceof EntityLivingBase) {
			final EntityLivingBase entityLiving = (EntityLivingBase) targetEntity;
			deltaY = entityLiving.posY + entityLiving.getEyeHeight() - (player.posY + player.getEyeHeight());
		} else {
			deltaY = (targetEntity.getEntityBoundingBox().minY + targetEntity.getEntityBoundingBox().maxY) / 2.0D
					- (player.posY + player.getEyeHeight());
		}
		final double hDistance = net.minecraft.util.math.MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
		final float calcYaw = (float) (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI - 90.0F),
				calcPitch = (float) -(Math.atan2(deltaY, hDistance) * 180.0D / Math.PI);
		player.rotationPitch = updateRotation(player.rotationPitch, calcPitch, maxIncPitch);
		player.rotationYaw = updateRotation(player.rotationYaw, calcYaw, maxIncYaw);
		return net.minecraft.util.math.MathHelper
				.abs(net.minecraft.util.math.MathHelper.wrapDegrees(player.rotationPitch)
						- net.minecraft.util.math.MathHelper.wrapDegrees(calcPitch)) < FACE_PRECISION
				&& net.minecraft.util.math.MathHelper
						.abs(net.minecraft.util.math.MathHelper.wrapDegrees(player.rotationYaw)
								- net.minecraft.util.math.MathHelper.wrapDegrees(calcYaw)) < FACE_PRECISION;
	}

	public static double interpolate(final double value, final double prevValue, final float partialTicks) {
		return prevValue + (value - prevValue) * partialTicks;
	}

	public static double round(final double value, final int scale) {
		return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	public static float updateRotation(final float oldRotation, final float newRotation, final float maxIncRotation) {
		float deltaRotation = net.minecraft.util.math.MathHelper.wrapDegrees(newRotation - oldRotation);
		if (deltaRotation > maxIncRotation) {
			deltaRotation = maxIncRotation;
		}
		if (deltaRotation < -maxIncRotation) {
			deltaRotation = -maxIncRotation;
		}
		return oldRotation + deltaRotation;
	}
}
