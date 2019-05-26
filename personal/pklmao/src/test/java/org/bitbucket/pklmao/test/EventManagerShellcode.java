package org.bitbucket.pklmao.test;

import org.bitbucket.pklmao.event.EventManager;
import org.bitbucket.pklmao.event.TickEvent;
import org.objectweb.asm.util.ASMifier;

public class EventManagerShellcode {
	private static class Dump {
		@SuppressWarnings("unused")
		public void run() {
			int i = 0, j = 1, k = 2, fgh = 3, jghkg = 4;
			System.out.println(i + ", " + j + "" + k + "" + fgh + "" + jghkg);
			EventManager.getInstance().fireEvent(new TickEvent(this, TickEvent.Phase.END));
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
