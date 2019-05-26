package org.bitbucket.pklmao.test;

import java.util.Random;

import org.objectweb.asm.util.ASMifier;

public class MacAddrSpoof {
	private static class Dump {
		@SuppressWarnings("unused")
		private static String test(byte[] testArray) {
			Random rand = new Random();
			for (int i = 0; i < testArray.length; i++) {
				testArray[i] = (byte) rand.nextInt(Byte.MAX_VALUE + 1);
			}
			return "";
		}
	}
	
	public static void main(String[] args) {
		try {
			ASMifier.main(new String[] {Dump.class.getName()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
