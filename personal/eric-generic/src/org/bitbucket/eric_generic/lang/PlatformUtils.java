package org.bitbucket.eric_generic.lang;

public final class PlatformUtils {
	public static String architecture() {
		for (final String propertyName : new String[] {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"}) {
			final String propertyValue = System.getProperty(propertyName);
			if (propertyValue != null && propertyValue.contains("64")) {
				return "64";
			}
		}
		return "32";
	}
	public static boolean is64Bit() {
		return architecture().equals("64");
	}
	public static boolean windows() {
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
}
