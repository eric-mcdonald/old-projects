package org.bitbucket.reliant.test;

public final class JniTest {
	public static void printTest() {
		new JniTest().test();
	}
	private native void test();
}
