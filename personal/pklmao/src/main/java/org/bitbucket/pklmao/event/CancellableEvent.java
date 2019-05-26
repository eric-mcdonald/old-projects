package org.bitbucket.pklmao.event;

public abstract class CancellableEvent extends BaseEvent implements Cancellable {
	private boolean cancelled;
	
	public CancellableEvent(Object source) {
		super(source);
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
