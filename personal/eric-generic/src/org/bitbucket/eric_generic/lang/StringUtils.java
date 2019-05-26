package org.bitbucket.eric_generic.lang;

import java.util.Comparator;

public class StringUtils {
	public static final Comparator<String> alphabetCmp = new Comparator<String>() {

		@Override
		public int compare(String arg0, String arg1) {
			// TODO Auto-generated method stub
			final int result = String.CASE_INSENSITIVE_ORDER.compare(arg0, arg1);
			return result == 0 ? arg0.compareTo(arg1) : result;
		}

	};
	public static final String COMMENT_PREFIX = "#", PROGRAM_ARG_PREFIX = "--";
	
	public static String arguments(final String[] args, final int beginIdx) {
		String argStr = "";
		for (int argIdx = beginIdx; argIdx < args.length; argIdx++) {
			argStr += args[argIdx];
			if (argIdx < args.length - 1) {
				argStr += " ";
			}
		}
		return argStr;
	}
	public static boolean comment(final String text) {
		return text.startsWith(COMMENT_PREFIX);
	}
	public static String configName(final String displayName) {
		return displayName.replace(' ', '_').toLowerCase();
	}
	public static String crop(final String string) {
		final int nullIdx = string.indexOf('\0');
		return nullIdx == -1 ? string : string.substring(0, nullIdx);
	}
	public static boolean empty(final String string) {
		return string == null || string.isEmpty();
	}
	public static String formatNum(final double number, final String format) {
		final int flooredNum = (int) Math.floor(number);
		return flooredNum == number ? String.valueOf(flooredNum) : String.format(format, number);
	}
	public static String list(final String[] strings, final int beginIdx) {
		String list = "";
		for (int strIdx = beginIdx; strIdx < strings.length; strIdx++) {
			list += strings[strIdx];
			if (strIdx < strings.length - 1) {
				list += ", ";
			}
		}
		return list;
	}
	public static boolean programArg(final String text) {
		return text.startsWith(PROGRAM_ARG_PREFIX);
	}
}
