// Eric: Ported from my Minecraft cheats lol.

package org.bitbucket.reliant.util.concurrent;

public abstract class LoopThread extends Thread {
	protected abstract long delay();
	protected abstract void onExecute();
	@Override
	public final void run() {
		// TODO Auto-generated method stub
		while (running()) {
			try {
				try {
					Thread.sleep(delay());
				} catch (final InterruptedException interruptedEx) {
					// TODO Auto-generated catch block
					interruptedEx.printStackTrace();
				}
				onExecute();
			} catch (final NullPointerException nullPtrEx) {
				// Eric: Empty implementation for concurrency issues.
			}
		}
	}
	protected abstract boolean running();
}