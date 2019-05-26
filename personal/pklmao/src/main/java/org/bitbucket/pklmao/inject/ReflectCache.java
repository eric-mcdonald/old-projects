package org.bitbucket.pklmao.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bitbucket.pklmao.util.ReflectHelper;

public final class ReflectCache {
	private static Field modifiersField;
	private static Method defClsMethod;
	private static Field interfaceCacheField;
	private static Class<?> entryPointCls;
	private static Class<?> interfaceCls;
	private static Class<?> playerCls;
	private static Field invItemsField;
	private static Method mainMethod;
	private static Field playersField;
	private static Class<?> mainCls;
	private static Field localPlayerField;
	
	private static void preInit() throws SecurityException, NoSuchFieldException {
		if (modifiersField == null) {
			modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
		}
	}
	public static void initLib() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		preInit();
		defClsMethod = ReflectHelper.getMethod(ClassLoader.class, "defineClass", String.class, byte[].class, int.class, int.class);
	}
	public static void initGame() throws SecurityException, NoSuchFieldException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		preInit();
		// TODO Consider specifying whether a field is public or not.
		entryPointCls = ReflectHelper.getClassByName("class_EntryPoint");
		mainCls = ReflectHelper.getClassByName("class_Main");
		interfaceCls = ReflectHelper.getClassByName("class_Interface");
		interfaceCacheField = ReflectHelper.getField(interfaceCls, "field_interfaceCache");
		mainMethod = ReflectHelper.getMethod(entryPointCls, "main", String[].class);
		invItemsField = ReflectHelper.getField(mainCls, "field_inventoryItems");
		playerCls = ReflectHelper.getClassByName("class_Player");
		playersField = ReflectHelper.getField(mainCls, "field_players");
		localPlayerField = ReflectHelper.getField(mainCls, "field_localPlayer");
	}
	
	public static Field getLocalPlayerField() {
		return localPlayerField;
	}
	public static Class<?> getMainCls() {
		return mainCls;
	}
	public static Field getPlayersField() {
		return playersField;
	}
	public static Class<?> getPlayerCls() {
		return playerCls;
	}
	public static Method getMainMethod() {
		return mainMethod;
	}
	public static Field getInvItemsField() {
		return invItemsField;
	}
	public static Class<?> getInterfaceCls() {
		return interfaceCls;
	}
	public static Field getInterfaceCacheField() {
		return interfaceCacheField;
	}
	public static Class<?> getEntryPointCls() {
		return entryPointCls;
	}
	public static Field getModifiersField() {
		return modifiersField;
	}
	public static Method getDefClsMethod() {
		return defClsMethod;
	}
}
