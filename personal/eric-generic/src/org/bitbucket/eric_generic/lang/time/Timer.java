package org.bitbucket.eric_generic.lang.time;

public abstract class Timer {
	private long startTime;
	
	protected abstract long delay();
	public boolean delayPassed() {
		final long currentTime = System.currentTimeMillis();
		return currentTime >= startTime && currentTime - startTime >= delay();
	}
	public final long getStartTime() {
		return startTime;
	}
	public void reset() {
		startTime = 0L;
	}
	public final void setStartTime() {
		startTime = System.currentTimeMillis();
	}
}
