package org.bitbucket.lanius.util.concurrent;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.util.Phase;

public abstract class LoopThread extends Thread {
	protected abstract long delay();

	/**
	 * Prints an empty string. Used as a hotfix for the no-empty-blocks-of-code
	 * issue.
	 */
	private void dummy() {
		System.out.print("");
	}

	protected abstract void onExecute(final Phase phase);

	@Override
	public final void run() {
		// TODO Auto-generated method stub
		while (running()) {
			while (Lanius.mc.isGamePaused()) {
				dummy();
				try {
					Thread.sleep(1L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				onExecute(Phase.START);
				try {
					Thread.sleep(delay());
				} catch (final InterruptedException interruptedEx) {
					// TODO Auto-generated catch block
					interruptedEx.printStackTrace();
				}
				onExecute(Phase.END);
			} catch (final NullPointerException nullPtrEx) {
				dummy();
			}
		}
	}

	protected abstract boolean running();
}
