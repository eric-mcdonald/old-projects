

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionHelper {

	public static Field findField(Class<?> declaringClass, int modifiers, Class<?> type, String name) {
		return obfuscated ? findObfuscatedField(declaringClass, modifiers, type) : findUnobfuscatedField(declaringClass, modifiers, name);
	}
	public static Method findMethod(Class<?> declaringClass, int modifiers, Class<?> returnType, String name, Class<?>[] parameterTypes) {
		return obfuscated ? findObfuscatedMethod(declaringClass, modifiers, returnType, parameterTypes) : findUnobfuscatedMethod(declaringClass, modifiers, name, parameterTypes);
	}
	public static Field findObfuscatedField(Class<?> declaringClass, int modifiers, Class<?> type) {
		for (Field f : Modifier.isPublic(modifiers) ? declaringClass.getFields() : declaringClass.getDeclaredFields()) {
			if (f.getModifiers() == modifiers && f.getType() == type) {
				if (!Modifier.isPublic(modifiers)) {
					f.setAccessible(true);
				}
				if (Modifier.isFinal(modifiers)) {
					try {
						Field modifiersField = Field.class.getDeclaredField("modifiers");
						modifiersField.setAccessible(true);
						modifiersField.setInt(f, modifiers & ~Modifier.FINAL);
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return f;
			}
		}
		return null;
	}
	public static Method findObfuscatedMethod(Class<?> declaringClass, int modifiers, Class<?> returnType, Class<?>[] parameterTypes) {
		for (Method m : Modifier.isPublic(modifiers) ? declaringClass.getMethods() : declaringClass.getDeclaredMethods()) {
			if (m.getModifiers() == modifiers && m.getReturnType() == returnType && m.getParameterTypes() == parameterTypes) {
				if (!Modifier.isPublic(modifiers)) {
					m.setAccessible(true);
				}
				return m;
			}
		}
		return null;
	}
	public static Field findUnobfuscatedField(Class<?> declaringClass, int modifiers, String name) {
		try {
			Field field;
			if (!Modifier.isPublic(modifiers)) {
				field = declaringClass.getDeclaredField(name);
				field.setAccessible(true);
			}
			else {
				field = declaringClass.getField(name);
			}
			if (Modifier.isFinal(modifiers)) {
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(field, modifiers & ~Modifier.FINAL);
			}
			return field;
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static Method findUnobfuscatedMethod(Class<?> declaringClass, int modifiers, String name, Class<?>[] parameterTypes) {
		try {
			if (Modifier.isPublic(modifiers)) {
				return declaringClass.getMethod(name, parameterTypes);
			}
			Method method = declaringClass.getDeclaredMethod(name, parameterTypes);
			method.setAccessible(true);
			return method;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void setObfuscated(boolean obfuscated) {
		ReflectionHelper.obfuscated = obfuscated;
	}
	private static boolean obfuscated;
}
