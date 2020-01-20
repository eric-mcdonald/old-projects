package ca.senecacollege.emcdonald5;

import java.util.Scanner;

public final class Main {
	public static void main(String[] args) {
		final Scanner input = new Scanner(System.in);
		try {
			System.out.print("Enter a number between 0 and 1000: ");
			if (input.hasNextLong()) {
				final int number = input.nextInt();
				if (number < 0 || number > 1000) {
					throw new NumberFormatException("Expected a number between 0 and 1000.");
				}
				int digitSum = 0;
				String numStr = String.valueOf(number);
				for (int digit = 0; digit < numStr.length(); digit++) {
					digitSum += Character.getNumericValue(numStr.charAt(digit));
				}
				System.out.println("The sum of the digits is " + digitSum);
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
