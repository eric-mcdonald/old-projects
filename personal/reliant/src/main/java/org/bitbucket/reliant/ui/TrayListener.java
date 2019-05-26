package org.bitbucket.reliant.ui;

import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bitbucket.reliant.routine.Routine;

public final class TrayListener implements ActionListener, ItemListener {
	private final Map<ItemSelectable, Routine> trayBoxMap = new HashMap<ItemSelectable, Routine>();
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		final String actionCmd = e.getActionCommand();
		if (actionCmd.equals("exit")) {
			System.exit(0);
		}
	}
	public Set<Map.Entry<ItemSelectable, Routine>> getTrayBoxEntries() {
		return trayBoxMap.entrySet();
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		final ItemSelectable itemSelectable = e.getItemSelectable();
		if (trayBoxMap.containsKey(itemSelectable)) {
			final Routine routine = trayBoxMap.get(itemSelectable);
			final boolean selected = e.getStateChange() == ItemEvent.SELECTED;
			if (selected && !routine.isEnabled() || !selected && routine.isEnabled()) {
				routine.setEnabled();
			}
		}
	}
	public Routine putTrayBox(final ItemSelectable itemSelectable, final Routine routine) {
		return trayBoxMap.put(itemSelectable, routine);
	}
}