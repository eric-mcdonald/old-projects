package org.bitbucket.eric_generic.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public static enum Type {
		INFO("INFO"), ERROR("ERROR");
		private final String name;
		
		private Type(final String name) {
			this.name = name;
		}
	}
	private final String name;
	private final File logFile;
	
	public Logger(final String name) {
		this(name, null);
	}
	
	public Logger(final String name, final File logDir) {
		this.name = name;
		this.logFile = logDir == null ? null : new File(logDir, "log-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".txt");
	}
	
	public void log(String message, final Type type) {
		message = "[" + name + "/" + type.name + "]: " + message;
		if (type.equals(Type.INFO)) {
			System.out.println(message);
		} else {
			System.err.println(message);
		}
		if (logFile != null) {
			logFile.getParentFile().mkdirs();
			try {
				final PrintWriter logWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
				logWriter.println(message);
				logWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void logError(final Throwable error) {
		error.printStackTrace(System.err);
		if (logFile != null) {
			logFile.getParentFile().mkdirs();
			try {
				final PrintWriter errorWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
				error.printStackTrace(errorWriter);
				errorWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void logFatal(final Throwable error) {
		logError(error);
		System.exit(-1);
	}
}
