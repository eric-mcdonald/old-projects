/**********************************************
Workshop #2
Course: JAC444 - Winter 2020
Last Name: McDonald
First Name: Eric
ID: 153581160
Section: NAB
This assignment represents my own work in accordance with Seneca
Academic Policy.
Eric McDonald
Date: 2020-01-31
**********************************************/
package ca.senecacollege.emcdonald5.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles console-related I/O processing. This class contains methods to prompt the user in the console for certain values.
 * 
 * @author Eric McDonald
 */
public class ConsoleIo {
	/**
	 * The input source.
	 */
	private final Scanner input = new Scanner(System.in);
	
	/**
	 * Throws an error if there is not enough inputted values.
	 * 
	 * @param receivedOutputs	The amount of inputs received by the input source
	 * @param expectedOutputs	The amount of inputs that were expected
	 */
	private static void assertInputs(final int receivedInputs, final int expectedInputs) {
		if (receivedInputs < expectedInputs) {
			throw new RuntimeException("Received " + receivedInputs + " outputs, expected: " + expectedInputs);
		}
	}
	/**
	 * Asks the user for the specified amount of integers starting with the message. 
	 * Extra validation can be specified with the validator parameter.
	 * 
	 * @param message	The message asking the user for input
	 * @param expectedOutputs	The amount of inputted values expected to receive
	 * @param validator	A callback specifying any extra validation for each value, or null if none is needed
	 * @return	The list of inputted values from the user
	 * @throws RuntimeException	if the validator throws an exception, or not enough inputs were received
	 */
	public List<Integer> promptInts(final String message, final int expectedInputs, final AssertCallback<Integer> validator) throws RuntimeException {
		if (expectedInputs <= 0) {
			throw new IllegalArgumentException("expectedInputs is less than 1: " + expectedInputs);
		}
		List<Integer> inputs = new ArrayList<Integer>();
		System.out.print(message);
		for (int i = 0; i < expectedInputs && input.hasNextInt(); i++) {
			final int inputtedVal = input.nextInt();
			if (validator != null) {
				validator.assertValid(inputtedVal);
			}
			inputs.add(inputtedVal);
		}
		assertInputs(inputs.size(), expectedInputs);
		return inputs;
	}
	/**
	 * Convenience method. This method is equivalent to: promptInts(message, 1, validator).get(0).
	 * 
	 * @param message	the message to pass to promptInts
	 * @param validator	the validator to pass to promptInts
	 * @return	the inputted value from the user
	 * @throws RuntimeException	if the validator throws an exception
	 */
	public int promptInt(final String message, final AssertCallback<Integer> validator) throws RuntimeException {
		return promptInts(message, 1, validator).get(0);
	}
	/**
	 * Asks the user for the specified amount of floating-point inputs starting with the message. 
	 * Extra validation can be specified with the validator parameter.
	 * 
	 * @param message	The message asking the user for input
	 * @param expectedOutputs	The amount of inputted values expected to receive
	 * @param validator	A callback specifying any extra validation for each value, or null if none is needed
	 * @return	The list of inputted values from the user
	 * @throws RuntimeException	if the validator throws an exception, or not enough inputs were received
	 */
	public List<Float> promptFloats(final String message, final int expectedInputs, final AssertCallback<Float> validator) throws RuntimeException {
		if (expectedInputs <= 0) {
			throw new IllegalArgumentException("expectedInputs is less than 1: " + expectedInputs);
		}
		List<Float> inputs = new ArrayList<Float>();
		System.out.print(message);
		for (int i = 0; i < expectedInputs && input.hasNextFloat(); i++) {
			final float inputtedVal = input.nextFloat();
			if (validator != null) {
				validator.assertValid(inputtedVal);
			}
			inputs.add(inputtedVal);
		}
		assertInputs(inputs.size(), expectedInputs);
		return inputs;
	}
	/**
	 * Convenience method. This method is equivalent to: promptFloats(message, 1, validator).get(0).
	 * 
	 * @param message	the message to pass to promptFloats
	 * @param validator	the validator to pass to promptFloats
	 * @return	the inputted value from the user
	 * @throws RuntimeException	if the validator throws an exception
	 */
	public float promptFloat(final String message, final AssertCallback<Float> validator) throws RuntimeException {
		return promptFloats(message, 1, validator).get(0);
	}
}
