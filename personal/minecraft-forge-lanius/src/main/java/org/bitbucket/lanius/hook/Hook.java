package org.bitbucket.lanius.hook;

import org.bitbucket.lanius.util.Phase;

public interface Hook<T extends HookData<?, ?>> {
	void onExecute(final T data, final Phase phase);
}
