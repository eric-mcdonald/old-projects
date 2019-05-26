package org.bitbucket.lanius.util.concurrent;

import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;

import com.google.common.base.MoreObjects;

public abstract class Rate<T> {
	public static final int UNLIMITED_EXEC = -1;
	private int execCount, execMax;

	private long resetDelay;

	private boolean running;

	public Rate(final int execMax, final long resetDelay) {
		setExecMax(execMax);
		setResetDelay(resetDelay);
	}

	public final boolean canExecute(final int amount, final int extraExec) {
		if (running && execMax == UNLIMITED_EXEC) {
			return true;
		}
		return running && getExecCount() + amount - 1 < execMax + extraExec;
	}

	public final boolean execute(final T object, final int extraExec) {
		if (!canExecute(1, extraExec)) {
			return false;
		}
		onExecute(object);
		setExecCount(getExecCount() + 1);
		return true;
	}

	public synchronized final int getExecCount() {
		return execCount;
	}

	protected abstract void onExecute(final T object);

	public synchronized final void setExecCount(final int execCount) {
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
				return resetDelay + NetworkUtils.lagTime();
			}

			@Override
			protected void onExecute(final Phase phase) {
				// TODO Auto-generated method stub
				if (phase.equals(Phase.END)) {
					setExecCount(0);
				}
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

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this).add("execCount", getExecCount()).add("execMax", execMax)
				.add("resetDelay", resetDelay).add("running", running).toString();
	}

}
