package org.bitbucket.pklmao.test;

import org.bitbucket.pklmao.event.EventPriority;
import org.bitbucket.pklmao.event.SubscribeEvent;
import org.bitbucket.pklmao.event.TickEvent;

public class EventListenerTest {
	@SubscribeEvent
	public void onTickEvent(TickEvent event) {
		System.out.println("second");
	}
	public void test(Object test) {
		System.out.println("never executed");
	}
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onTickEventHigh(TickEvent event) {
		System.out.println("first" + ":" + event.source());
	}
}
