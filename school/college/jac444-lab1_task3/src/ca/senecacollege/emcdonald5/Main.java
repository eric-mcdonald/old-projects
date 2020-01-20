package ca.senecacollege.emcdonald5;

import java.util.Scanner;

public final class Main {
	private static final double TAX_MULT = 1.0D + 0.00417D;
	
	public static void main(String[] args) {
		System.out.print("Enter the monthly savings amount: ");
		final Scanner input = new Scanner(System.in);
		try {
			if (input.hasNextDouble()) {
				final double initSavings = input.nextDouble();
				double totalSavings = 0.0D;
				for (int month = 1; month <= 6; month++) {
					totalSavings = (initSavings + totalSavings) * TAX_MULT;
				}
				System.out.println("After the sixth month, the account value is " + String.format("%.2f", totalSavings));
			} else {
				throw new NumberFormatException("Expected a long integer.");
			}
		} catch (NumberFormatException ex) {
			System.err.println(ex.getMessage());
		} finally {
			input.close();
		}
	}
}
