

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

	public static boolean addListener(Object obj) {
		for (Method m : obj.getClass().getDeclaredMethods()) {
			if (isMethodValid(m)) {
				return listeners.add(obj);
			}
		}
		return false;
	}
	public static Event callEvent(Event event) {
		for (Object o : listeners) {
			for (Method m : o.getClass().getDeclaredMethods()) {
				if (isMethodValid(m) && m.getParameterTypes()[0] == event.getClass()) {
					try {
						m.invoke(o, event);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return event;
	}
	public static void clearListeners() {
		listeners.clear();
	}
	private static boolean isMethodValid(Method method) {
		if (method.isAnnotationPresent(SubscribeEvent.class) && method.getReturnType() == Void.TYPE && method.getParameterTypes().length == 1) {
			Class<?> parameterType = method.getParameterTypes()[0];
			while (parameterType.getSuperclass() != null) {
				if (parameterType.getSuperclass().getSuperclass() == null) {
					break;
				}
				parameterType = parameterType.getSuperclass();
			}
			for (Class<?> c : parameterType.getInterfaces()) {
				if (c == Event.class) {
					return true;
				}
			}
		}
		return false;
	}
	public static boolean removeListener(Object obj) {
		return listeners.remove(obj);
	}
	private static List<Object> listeners = new ArrayList<Object>();
}
