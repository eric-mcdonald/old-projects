package org.bitbucket.eric_generic.crypto;

public final class RotCipher {
	public static final int MIN = 1, MAX = 32767;
	
	public static String decrypt(final String encrypted, final int rotTimes) {
		validate(rotTimes);
		String decrypted = "";
		for (int i = 0; i < encrypted.length(); i++) {
			decrypted += Character.toString((char) (encrypted.charAt(i) - rotTimes));
		}
		return decrypted;
	}
	public static String encrypt(final String input, final int rotTimes) {
		validate(rotTimes);
		String output = "";
		for (int i = 0; i < input.length(); i++) {
			output += Character.toString((char) (input.charAt(i) + rotTimes));
		}
		return output;
	}
	private static void validate(final int rotTimes) {
		if (rotTimes < MIN || rotTimes > MAX) {
			throw new IllegalArgumentException("Rotation times must be between " + MIN + " and " + MAX);
		}
	}
}
