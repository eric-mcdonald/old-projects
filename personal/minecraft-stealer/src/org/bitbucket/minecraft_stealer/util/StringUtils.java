package org.bitbucket.minecraft_stealer.util;

public final class StringUtils {
	public static boolean endsWith(final String str, final String[] suffixes) {
		for (final String suffix : suffixes) {
			if (str.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}

	public static String list(final String[] strings) {
		String list = "";
		for (int strIdx = 0; strIdx < strings.length; strIdx++) {
			list += strings[strIdx];
			if (strIdx < strings.length - 1) {
				list += ", ";
			}
		}
		return list;
	}
}
