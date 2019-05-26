// Eric: Ported from my Minecraft cheats lol.

package org.bitbucket.reliant.util.concurrent;

public abstract class Rate<T> {
	private int execCount, execMax;
	private long resetDelay;
	private boolean running;

	public Rate(final int execMax, final long resetDelay) {
		setExecMax(execMax);
		setResetDelay(resetDelay);
	}
	public final boolean canExecute(final int amount, final int extraExec) {
		return running && execCount + amount - 1 < execMax + extraExec;
	}
	public final boolean execute(final T object, final int extraExec) {
		if (!canExecute(1, extraExec)) {
			return false;
		}
		onExecute(object);
		setExecCount(execCount + 1);
		return true;
	}
	protected abstract void onExecute(final T object);
	private synchronized final void setExecCount(final int execCount) {
		this.execCount = execCount;
	}
	public final void setExecMax(final int execMax) {
		this.execMax = execMax;
	}
	public final void setResetDelay(final long resetDelay) {
		this.resetDelay = Math.max(resetDelay, 0L);
	}
	public final void start() {
		if (running) {
			return;
		}
		running = true;
		new LoopThread() {

			@Override
			protected long delay() {
				// TODO Auto-generated method stub
				return resetDelay;
			}

			@Override
			protected void onExecute() {
				// TODO Auto-generated method stub
				setExecCount(0);
			}

			@Override
			protected boolean running() {
				// TODO Auto-generated method stub
				return running;
			}

		}.start();
	}
	public final void stop() {
		if (!running) {
			return;
		}
		setExecCount(0);
		running = false;
	}
}