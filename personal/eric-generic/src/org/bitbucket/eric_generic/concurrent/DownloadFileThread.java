package org.bitbucket.eric_generic.concurrent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class DownloadFileThread extends Thread {
	private final File toFile;
	private final URL fileUrl;
	
	public DownloadFileThread(final File toFile, final URL fileUrl) {
		this.toFile = toFile;
		this.fileUrl = fileUrl;
	}
	
	@Override
	public void run() {
		try {
			toFile.delete();
			final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(toFile));
			final BufferedInputStream in = new BufferedInputStream(fileUrl.openStream());
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
