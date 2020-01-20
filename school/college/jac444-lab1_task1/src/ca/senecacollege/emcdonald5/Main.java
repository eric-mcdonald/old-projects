package ca.senecacollege.emcdonald5;

public final class Main {
	public static void main(String[] args) {
		final long ORIGINAL_POPULATION = 312032486;
		final long SECONDS_IN_YEAR = 60 * 60 * 24 * 365;
		for (int year = 1; year <= 5; year++) {
			System.out.println("Year " + year + ": " + (ORIGINAL_POPULATION + SECONDS_IN_YEAR / 7L - SECONDS_IN_YEAR / 13L + SECONDS_IN_YEAR / 45L) * year);
		}
	}
}
