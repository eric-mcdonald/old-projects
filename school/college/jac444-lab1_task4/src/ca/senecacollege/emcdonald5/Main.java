package ca.senecacollege.emcdonald5;

import java.text.DateFormatSymbols;
import java.util.Locale;

public final class Main {
	private static final int MIN_MONTH = 1, MAX_MONTH = 12;
	
	public static void main(String[] args) {
		final DateFormatSymbols dateFormats = new DateFormatSymbols(Locale.CANADA);
		int monthNum = (int) (Math.floor(Math.random() * (MAX_MONTH - MIN_MONTH + 1)) + MIN_MONTH);
		System.out.println(dateFormats.getMonths()[(monthNum - 1) % dateFormats.getMonths().length]);
	}
}
