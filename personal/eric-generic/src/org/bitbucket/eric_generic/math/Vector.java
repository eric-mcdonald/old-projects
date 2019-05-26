package org.bitbucket.eric_generic.math;

public class Vector {
	public static Vector wrap(final float[] data) {
		return new Vector(data[0], data[1], data[2]);
	}
	
	public float x, z, y;
	
	public Vector(final float x, final float z, final float y) {
		this.x = x;
		this.z = z;
		this.y = y;
	}
	
	public void add(final Vector vec) {
		x += vec.x;
		z += vec.z;
		y += vec.y;
	}
	
	public Vector copy() {
		return new Vector(x, z, y);
	}
	
	public float[] data() {
		return new float[] {x, z, y};
	}
	
	public float distance(final Vector vec) {
		return MathHelper.distance(x, z, y, vec.x, vec.z, vec.y);
	}
	
	public void divide(final Vector vec) {
		x /= vec.x;
		z /= vec.z;
		y /= vec.y;
	}
	
	public float dotProduct(final Vector vec) {
        return x * vec.x + z * vec.z + y * vec.y;
    }
	
	public float dotProductAbs(final Vector vec) {
		return Math.abs(x * vec.x) + Math.abs(z * vec.z) + Math.abs(y * vec.y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vector)) {
			return false;
		}
		final Vector vec = (Vector) obj;
		return x == vec.x && z == vec.z && y == vec.y;
	}
	
	@Override
	public int hashCode() {
		int hashCode = Float.floatToIntBits(x);
		hashCode = 31 * hashCode + Float.floatToIntBits(z);
		hashCode = 31 * hashCode + Float.floatToIntBits(y);
		return hashCode;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + z * z + y * y);
	}
	
	public void maximum(final Vector vec) {
		x = Math.max(x, vec.x);
		z = Math.max(z, vec.z);
		y = Math.max(y, vec.y);
	}
	
	public void minimum(final Vector vec) {
		x = Math.min(x, vec.x);
		z = Math.min(z, vec.z);
		y = Math.min(y, vec.y);
	}
	
	public void multiply(final Vector vec) {
		x *= vec.x;
		z *= vec.z;
		y *= vec.y;
	}
	
	public void rotate(final float[][] matrix) {
		x = dotProduct(Vector.wrap(matrix[0]));
		z = dotProduct(Vector.wrap(matrix[1]));
		y = dotProduct(Vector.wrap(matrix[2]));
	}
	
	public void subtract(final Vector vec) {
		x -= vec.x;
		z -= vec.z;
		y -= vec.y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + z + ", " + y + ")";
	}
	
	public void transform(final float[][] matrix) {
		x = dotProduct(wrap(matrix[0])) + matrix[0][3];
		z = dotProduct(wrap(matrix[1])) + matrix[1][3];
		y = dotProduct(wrap(matrix[2])) + matrix[2][3];
	}
}
