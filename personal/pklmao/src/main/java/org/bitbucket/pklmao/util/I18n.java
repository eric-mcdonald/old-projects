package org.bitbucket.pklmao.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18n {
	private Locale currentLoc;
	private ResourceBundle msgBundle;
	private MessageFormat formatter = new MessageFormat("");
	
	public I18n(String language, String country) {
		currentLoc = new Locale(language, country);
		msgBundle = ResourceBundle.getBundle("lang/messages", currentLoc);
	}
	
	public Locale getCurrentLoc() {
		return currentLoc;
	}
	public void setCurrentLoc(Locale loc) {
		if (loc == null || loc.equals(currentLoc)) {
			return;
		}
		currentLoc = loc;
		msgBundle = ResourceBundle.getBundle("messages", currentLoc);
	}
	public String translate(String key, Object... formatArgs) {
		String message;
		try {
			message = msgBundle.getString(key);
		} catch (MissingResourceException missingEx) {
			message = key;
			return message;
		}
		if (formatArgs.length == 0) {
			return message;
		}
		formatter.setLocale(currentLoc);
		formatter.applyPattern(message);
		return formatter.format(formatArgs);
	}
}
