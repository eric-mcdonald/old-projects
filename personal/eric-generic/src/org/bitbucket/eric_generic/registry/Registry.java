package org.bitbucket.eric_generic.registry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bitbucket.eric_generic.lang.StringUtils;

public class Registry<E> {
	private final Map<String, E> objMap;
	
	public Registry(final Comparator<String> objCmp) {
		objMap = objCmp == null ? new LinkedHashMap<String, E>() : new TreeMap<String, E>(objCmp);
	}

	public E get(String name) {
		name = StringUtils.configName(name);
		return objMap.get(name);
	}
	public final List<E> objects() {
		// Eric: Hotfix for duplicate entries
		final List<E> objects = new ArrayList<E>();
		for (final E object : objMap.values()) {
			if (!objects.contains(object)) {
				objects.add(object);
			}
		}
		return objects;
	}
	public E register(String name, final E object) {
		name = StringUtils.configName(name);
		return objMap.put(name, object);
	}
}