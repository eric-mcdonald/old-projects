/**********************************************
Workshop #1
Course: JAC444 - Winter 2020
Last Name: McDonald
First Name: Eric
ID: 153581160
Section: NAB
This assignment represents my own work in accordance with Seneca
Academic Policy.
Eric McDonald
Date: 2020-01-19
**********************************************/
package ca.senecacollege.emcdonald5;

import java.util.List;

/**
 * Contain's entry-point related functionality for this program. This program asks the user for an array and 
 * outputs the highest element in the array.
 * 
 * @author Eric McDonald
 */
public final class Main {
	/**
	 * The program's entry point.
	 * 
	 * @param args	The command-line arguments
	 */
	public static void main(String[] args) {
		// TODO Ask the professor if this ConsoleIo and AssertCallback stuff is overkill for a small assignment like this.
		// TODO Ask the professor if I can just tell him what each method does, instead of writing all this documentation.
		final ConsoleIo io = new ConsoleIo();
		try {
			final List<Integer> arrayDims = io.promptInts("Enter the number of rows and columns in the array: ", 2, 
					new AssertCallback<Integer>() {
				@Override
				public void assertValid(Integer value) throws RuntimeException {
					if (value <= 0) {
						throw new RuntimeException("The specified array length is too small: " + value);
					}
				}
			});
			System.out.println();
			float[][] inArray = new float[arrayDims.get(0)][arrayDims.get(1)];
			final List<Float> arrayVals = io.promptFloats("Enter the array:\n", inArray.length * inArray[0].length, null);
			for (int i = 0; i < inArray.length; i++) {
				for (int j = 0; j < inArray[i].length; j++) {
					inArray[i][j] = arrayVals.get(j + i * inArray[i].length);
				}
			}
			float largestElem = Float.MIN_VALUE;
			for (int i = 0; i < inArray.length; i++) {
				for (int j = 0; j < inArray[i].length; j++) {
					largestElem = Math.max(largestElem, inArray[i][j]);
				}
			}
			System.out.println("The largest element in the specified array is " + largestElem);
		} catch (final RuntimeException error) {
			System.err.println(error.getMessage());
		}
	}
}
