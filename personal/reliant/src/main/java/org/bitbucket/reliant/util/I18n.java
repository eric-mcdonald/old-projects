package org.bitbucket.reliant.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

// TODO(Eric) Move this to the eric-generic dependency.
public final class I18n {
	private ResourceBundle msgBundle;
	private final String bundleName;
	
	public I18n(final String bundleName) {
		this.bundleName = bundleName;
		setMsgBundle("en", "US");
	}
	
	public boolean setMsgBundle(final String language, final String country) throws LocaleException {
		Locale newLocale = null;
		for (final Locale availableLoc : Locale.getAvailableLocales()) {
			if (language.equalsIgnoreCase(availableLoc.getLanguage()) && country.equalsIgnoreCase(availableLoc.getCountry())) {
				newLocale = availableLoc;
				break;
			}
		}
		if (newLocale == null) {
			throw new LocaleException("Invalid locale specified.");
		}
		if (msgBundle != null && newLocale.equals(msgBundle.getLocale())) {
			return false;
		}
		msgBundle = ResourceBundle.getBundle(bundleName, newLocale);
		return true;
	}
	public ResourceBundle getMsgBundle() {
		return msgBundle;
	}
	public String format(final String key, final Object... formatArgs) {
		if (formatArgs.length <= 0) {
			return msgBundle.getString(key);
		}
		return new MessageFormat(msgBundle.getString(key), msgBundle.getLocale()).format(formatArgs);
	}
}
