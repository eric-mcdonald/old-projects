package org.bitbucket.lanius.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindFieldException;

public final class ReflectHelper extends ObfuscationReflectionHelper {
	/*
	 * public static String[] remapMethodNames(String className, String methodDesc,
	 * String... methodNames) { final String internalName =
	 * FMLDeobfuscatingRemapper.INSTANCE .unmap(className.replace('.', '/')); final
	 * String[] mappedNames = new String[methodNames.length]; int nameCount = 0; for
	 * (String methodName : methodNames) { mappedNames[nameCount++] =
	 * FMLDeobfuscatingRemapper.INSTANCE .mapMethodName(internalName, methodName,
	 * methodDesc); } return mappedNames; }
	 */

	/*
	 * public static <T, E> void copyValue(Class<? super T> classToAccess, T to, T
	 * from, String... fieldNames) { setValue(classToAccess, to,
	 * getPrivateValue(classToAccess, from, fieldNames), fieldNames); }
	 */

	public static <T> void copyFields(Class<T> classToAccess, T from, T to) {
		for (final Field fromField : classToAccess.getDeclaredFields()) {
			final String fromName = fromField.getName();
			try {
				final Field toField = classToAccess.getDeclaredField(fromName);
				if (fromField.equals(toField) && !Modifier.isStatic(fromField.getModifiers())) {
					setValue(classToAccess, to, getPrivateValue(classToAccess, from, fromName), fromName);
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static <T, E> void setValue(Class<? super T> classToAccess, T instance, E value, String... fieldNames) {
		try {
			for (final String fieldName : fieldNames) {
				try {
					final Field valueField = classToAccess.getDeclaredField(fieldName);
					final int valueModifiers = valueField.getModifiers();
					if (Modifier.isFinal(valueModifiers)) {
						// Eric: I don't think Minecraft Forge's
						// ObfuscationReflectionHelper actually sets finalized
						// values
						valueField.setAccessible(true);
						final Field modifiersField = Field.class.getDeclaredField("modifiers");
						modifiersField.setAccessible(true);
						modifiersField.setInt(valueField, valueField.getModifiers() & ~Modifier.FINAL);
						valueField.set(instance, value);
					} else {
						setPrivateValue(classToAccess, instance, value, fieldNames);
					}
					break;
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					// Eric: empty catch block to prevent annoying console spam
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
		} catch (UnableToFindFieldException e) {
			FMLLog.log(Level.ERROR, e, "Unable to locate any field %s on type %s", Arrays.toString(fieldNames),
					classToAccess.getName());
			throw e;
		} catch (UnableToAccessFieldException e) {
			FMLLog.log(Level.ERROR, e, "Unable to set any field %s on type %s", Arrays.toString(fieldNames),
					classToAccess.getName());
			throw e;
		}
	}

}
