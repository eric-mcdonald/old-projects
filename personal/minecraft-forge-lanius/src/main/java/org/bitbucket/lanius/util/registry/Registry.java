package org.bitbucket.lanius.util.registry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Registry<T> {
	protected final SortedMap<String, T> objMap = new TreeMap<String, T>(new Comparator<String>() {

		@Override
		public int compare(String arg0, String arg1) {
			// TODO Auto-generated method stub
			final int result = String.CASE_INSENSITIVE_ORDER.compare(arg0, arg1);
			return result == 0 ? arg0.compareTo(arg1) : result;
		}

	});

	public T get(final String name) {
		return objMap.get(name);
	}

	public final List<T> objects() {
		// Eric: hotfix for duplicate entries
		final List<T> objects = new ArrayList<T>();
		for (final T object : objMap.values()) {
			if (!objects.contains(object)) {
				objects.add(object);
			}
		}
		return objects;
	}

	public T register(final String name, final T object) {
		return objMap.put(name, object);
	}
}
