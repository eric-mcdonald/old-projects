package org.bitbucket.pklmao.event;

public class TickEvent extends BaseEvent {
	public static enum Phase {
		START,
		END;
	}
	
	private Phase phase;
	
	public TickEvent(Object source, Phase phase) {
		super(source);
		this.phase = phase;
	}
	
	public Phase getPhase() {
		return phase;
	}
}
