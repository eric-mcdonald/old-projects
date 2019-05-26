package org.bitbucket.pklmao.test;

import org.bitbucket.pklmao.event.SubscribeEvent;
import org.bitbucket.pklmao.event.TickEvent;
import org.bitbucket.pklmao.routine.BaseRoutine;

public class AttackTestRoutine extends BaseRoutine {
	private long startTime = 0L;

	public AttackTestRoutine() {
		super("attack_test", "Attack Test", "A test routine.");
	}
	
	@SubscribeEvent
	public void onTickPost(TickEvent event) {
		if (event.getPhase().equals(TickEvent.Phase.END)) {
			if (startTime == 0L) {
				startTime = System.currentTimeMillis();
			}
			if (System.currentTimeMillis() - startTime >= 50L) {
				
				startTime = System.currentTimeMillis();
			}
		}
	}
}
