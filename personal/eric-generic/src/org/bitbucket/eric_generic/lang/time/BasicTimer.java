package org.bitbucket.eric_generic.lang.time;

public class BasicTimer extends Timer {
	private final long delay;
	
	public BasicTimer(final long delay) {
		this.delay = delay;
	}
	
	@Override
	protected long delay() {
		// TODO Auto-generated method stub
		return delay;
	}
}
