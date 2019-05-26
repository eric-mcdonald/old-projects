package org.bitbucket.pklmao.event;

public interface Cancellable {
	boolean isCancelled();
	void setCancelled(boolean cancelled);
}
