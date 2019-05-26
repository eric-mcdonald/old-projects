package org.bitbucket.pklmao.ui;

import javax.swing.BoxLayout;

import org.bitbucket.pklmao.routine.Routine;

public class TogglesPanel extends RegistryPanel<String, Routine> {
	private static final long serialVersionUID = 1L;

	public TogglesPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	@Override
	public void register(String key, Routine value) {
		final boolean hadObj = getObj(key) != null;
		super.register(key, value);
		if (!hadObj) {
			add(value.toggleBtn());
			validate();
		}
	}
	@Override
	public void unregisterKey(String key) {
		Routine value = getObj(key);
		super.unregisterKey(key);
		if (value != null) {
			remove(value.toggleBtn());
			validate();
		}
	}
	@Override
	public void unregisterObj(Routine value) {
		super.unregisterObj(value);
		remove(value.toggleBtn());
		validate();
	}
}
