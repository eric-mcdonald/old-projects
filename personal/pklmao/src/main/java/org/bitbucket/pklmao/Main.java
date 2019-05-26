package org.bitbucket.pklmao;

import org.bitbucket.pklmao.inject.ReflectCache;
import org.bitbucket.pklmao.util.ReflectHelper;

public final class Main {
	public static void main(String[] args) {
		// TODO Parse the program arguments here:
		
		try {
			PkLmao.getInstance().init();
		} catch (ReportedException ex) {
			ex.createCrashReport();
			System.exit(-4);
		}
		ReflectHelper.invoke(ReflectCache.getMainMethod(), null, new Object[] {args});
	}
}
