package org.bitbucket.pklmao.event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bitbucket.pklmao.PkLmao;
import org.bitbucket.pklmao.util.ReflectHelper;

public class EventManager {
	private static final Logger LOGGER = Logger.getLogger(PkLmao.class.getName());
	private static final Comparator<Method> methodCmp = new Comparator<Method>() {
		@Override
		public int compare(Method o1, Method o2) {
			SubscribeEvent subAnnotation1 = o1.getAnnotation(SubscribeEvent.class), subAnnotation2 = o2.getAnnotation(SubscribeEvent.class);
			return subAnnotation1.priority().ordinal() - subAnnotation2.priority().ordinal();
		}
	};
	private static EventManager instance;
	private final Map<Object, Method[]> listeners = new HashMap<Object, Method[]>();
	
	public static EventManager getInstance() {
		if (instance == null) {
			instance = new EventManager();
		}
		return instance;
	}
	private Method[] getListenerMethodsFor(Object listener) {
		final List<Method> methods = new ArrayList<Method>();
		for (Method method : listener.getClass().getDeclaredMethods()) {
			if (!Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(SubscribeEvent.class) && method.getParameterTypes().length == 1 && Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
				methods.add(method);
			}
		}
		return methods.toArray(new Method[0]);
	}
	public void addListener(Object listener) {
		if (listeners.containsKey(listener)) {
			LOGGER.log(Level.WARNING, "Tried to re-add listener: " + listener);
			return;
		}
		Method[] methods = getListenerMethodsFor(listener);
		if (methods.length == 0) {
			return;
		}
		listeners.put(listener, methods);
		for (Method[] listenerMethods : listeners.values()) {
			Arrays.sort(listenerMethods, methodCmp);
		}
	}
	public void removeListener(Object listener) {
		listeners.remove(listener);
	}
	public void fireEvent(Event event) {
		for (Map.Entry<Object, Method[]> listenerEntry : listeners.entrySet()) {
			for (Method evHandler : listenerEntry.getValue()) {
				if (evHandler.getParameterTypes()[0].isInstance(event)) {
					ReflectHelper.invoke(evHandler, listenerEntry.getKey(), event);
				}
			}
		}
	}
}
