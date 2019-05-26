package org.bitbucket.eric_generic.math;

public class MathHelper {
	public static final float MAX_FOV = 180.0F;
	public static final float EQUALS_PRECISION = 0.001F;
	
	public static float[][] angleMatrix(final float[] angles) {
		final float[][] matrix = new float[3][4];
		final float sinPitch = (float) Math.sin(Math.toRadians(angles[0])), sinYaw = (float) Math.sin(Math.toRadians(angles[1])), sinRoll = (float) Math.sin(Math.toRadians(angles[2])), cosPitch = (float) Math.cos(Math.toRadians(angles[0])), cosYaw = (float) Math.cos(Math.toRadians(angles[1])), cosRoll = (float) Math.cos(Math.toRadians(angles[2]));
		matrix[0][0] = cosPitch * cosYaw;
		matrix[1][0] = cosPitch * sinYaw;
		matrix[2][0] = -sinPitch;
		final float crcy = cosRoll * cosYaw, crsy = cosRoll * sinYaw, srcy = sinRoll * cosYaw, srsy = sinRoll * sinYaw;
		matrix[0][1] = sinPitch * srcy - crsy;
		matrix[1][1] = sinPitch * srsy + crcy;
		matrix[2][1] = sinRoll * cosPitch;
		matrix[0][2] = sinPitch * crcy + srsy;
		matrix[1][2] = sinPitch * crsy - srcy;
		matrix[2][2] = cosRoll * cosPitch;
		matrix[0][3] = 0.0F;
		matrix[1][3] = 0.0F;
		matrix[2][3] = 0.0F;
		return matrix;
	}
	public static Vector anglesVec(final float[] angles) {
		final float sinPitch = (float) Math.sin(Math.toRadians(angles[0])), sinYaw = (float) Math.sin(Math.toRadians(angles[1])), cosPitch = (float) Math.cos(Math.toRadians(angles[0])), cosYaw = (float) Math.cos(Math.toRadians(angles[1]));
		return new Vector(cosPitch * cosYaw, cosPitch * sinYaw, -sinPitch);
	}
	public static void clampAngles(final float[] angles, final float maxPitch) {
		if (Float.isNaN(angles[0]) || Float.isInfinite(angles[0])) {
			angles[0] = 0.0F;
		}
		if (Float.isNaN(angles[1]) || Float.isInfinite(angles[1])) {
			angles[1] = 0.0F;
		}
		while (angles[0] > maxPitch) {
			angles[0] -= 360.0F;
		}
		while (angles[0] < -maxPitch) {
			angles[0] += 360.0F;
		}
		while (angles[1] > MAX_FOV) {
			angles[1] -= 360.0F;
		}
		while (angles[1] < -MAX_FOV) {
			angles[1] += 360.0F;
		}
		if (angles[0] > maxPitch) {
			angles[0] = maxPitch;
		}
		if (angles[0] < -maxPitch) {
			angles[0] = -maxPitch;
		}
		if (angles[1] > MAX_FOV) {
			angles[1] = MAX_FOV;
		}
		if (angles[1] < -MAX_FOV) {
			angles[1] = -MAX_FOV;
		}
	}
	public static float distance(final float x1, final float z1, final float y1, final float x2, final float z2, final float y2) {
		final float deltaX = x2 - x1, deltaZ = z2 - z1, deltaY = y2 - y1;
		return (float) Math.sqrt(deltaX * deltaX + deltaZ * deltaZ + deltaY * deltaY);
	}
	public static float[] rotate(final float[] vec, final float centerX, final float centerY, final float angle) {
		final float xt = vec[0] - centerX, yt = vec[1] - centerY;
		final float cosAng = (float) Math.cos(Math.toRadians(angle)), sinAng = (float) Math.sin(Math.toRadians(angle));
		return new float[] {xt * cosAng - yt * sinAng, xt * sinAng + yt * cosAng};
	}
}
