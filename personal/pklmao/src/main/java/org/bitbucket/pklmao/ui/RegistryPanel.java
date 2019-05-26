package org.bitbucket.pklmao.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;

public abstract class RegistryPanel<T, E> extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Map<T, E> objects = new HashMap<T, E>();
	
	public void register(T key, E value) {
		objects.put(key, value);
	}
	public void unregisterKey(T key) {
		objects.remove(key);
	}
	public void unregisterObj(E value) {
		Iterator<E> values = objects.values().iterator();
		while (values.hasNext()) {
			if (values.next().equals(value)) {
				values.remove();
			}
		}
	}
	public E getObj(T key) {
		return objects.get(key);
	}
	@SuppressWarnings("unchecked")
	public E[] objects() {
		return (E[]) objects.values().toArray();
	}
}
