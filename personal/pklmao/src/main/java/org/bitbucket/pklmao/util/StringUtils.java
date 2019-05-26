package org.bitbucket.pklmao.util;

public final class StringUtils {
	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}
	public String concatStrings(String[] strings, String separator) {
		String result = strings.length == 0 ? "" : strings[0];
		for (int i = 1; i < strings.length; i++) {
			result += separator + strings[i];
		}
		return result;
	}
}
