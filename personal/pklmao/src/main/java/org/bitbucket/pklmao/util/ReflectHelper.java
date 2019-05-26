package org.bitbucket.pklmao.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bitbucket.pklmao.PkLmao;
import org.bitbucket.pklmao.inject.ReflectCache;

public final class ReflectHelper {
	private static final Logger LOGGER = Logger.getLogger(ReflectHelper.class.getName());
	
	public static Class<?> getClassByName(String clsName) throws ClassNotFoundException {
		clsName = PkLmao.getInstance().getMappings().getObfName(clsName);
		return Class.forName(clsName);
	}
	public static Method getMethod(Class<?> clazz, String methodName, boolean isPublic, Class<?>... paramTypes) throws SecurityException, NoSuchMethodException {
		methodName = PkLmao.getInstance().getMappings().getObfName(methodName);
		Method method = isPublic ? clazz.getMethod(methodName, paramTypes) : clazz.getDeclaredMethod(methodName, paramTypes);
		if (!isPublic) {
			method.setAccessible(true);
		}
		return method;
	}
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException, NoSuchMethodException {
		return getMethod(clazz, methodName, false, paramTypes);
	}
	public static Field getField(Class<?> clazz, String fieldName, boolean isPublic) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		fieldName = PkLmao.getInstance().getMappings().getObfName(fieldName);
		Field field = isPublic ? clazz.getField(fieldName) : clazz.getDeclaredField(fieldName);
		if (!isPublic) {
			field.setAccessible(true);
		}
		if (Modifier.isFinal(field.getModifiers())) {
			ReflectCache.getModifiersField().setInt(field, field.getModifiers() & ~Modifier.FINAL);
		}
		return field;
	}
	public static Field getField(Class<?> clazz, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		return getField(clazz, fieldName, false);
	}
	
	// TODO Use crash reports for error handling.
	
	public static void set(Field field, Object instance, Object value) {
		try {
			field.set(instance, value);
		} catch (Throwable error) {
			LOGGER.log(Level.SEVERE, "Failed to set field: " + field, error);
		}
	}
	@SuppressWarnings("unchecked")
	public static <T> T get(Field field, Object instance) {
		try {
			return (T) field.get(instance);
		} catch (Throwable error) {
			LOGGER.log(Level.SEVERE, "Failed to get field: " + field, error);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Method method, Object instance, Object... args) {
		try {
			return (T) method.invoke(instance, args);
		} catch (Throwable error) {
			LOGGER.log(Level.SEVERE, "Failed to invoke method: " + method, error);
		}
		return null;
	}
}
