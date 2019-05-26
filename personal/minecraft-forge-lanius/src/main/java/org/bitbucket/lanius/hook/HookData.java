package org.bitbucket.lanius.hook;

public abstract class HookData<T, V> {
	public V retVal;
	public final T source;

	public HookData(final T source) {
		this.source = source;
	}
}
