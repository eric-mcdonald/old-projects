package org.bitbucket.pklmao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final File crashReportDir = new File(PkLmao.getInstance().getDataDir(), "crash_reports");
	private static final Logger LOGGER = Logger.getLogger(ReportedException.class.getName());
	
	public ReportedException(String msgKey, Object... formatArgs) {
		super(PkLmao.getInstance().getI18n().translate(msgKey, formatArgs));
	}
	
	public void createCrashReport() {
		LOGGER.log(Level.WARNING, "An error has occured:", this);
		crashReportDir.mkdirs();
		try {
			writeTo(new File(crashReportDir, "crash_report-" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	private void writeTo(File crashReport) throws FileNotFoundException {
		crashReport.delete();
		PrintStream errStream = new PrintStream(new FileOutputStream(crashReport));
		errStream.println("Occurred on " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		printStackTrace(errStream);
		errStream.close();
	}
}
