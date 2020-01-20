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

import java.util.Random;

/**
 * Contain's entry-point related functionality for this program. This program plays the game "Craps" until it loses or wins.
 * 
 * @author Eric McDonald
 */
public final class Main {
	/**
	 * The random number generator for the game.
	 */
	private static final Random rng = new Random();
	/**
	 * The minimum and maximum values on dice.
	 */
	private static final int DICE_MIN = 1, DICE_MAX = 6;
	/**
	 * The initial win/lose values for the game.
	 */
	private static final int[] CRAP_VALS = new int[] {2, 3, 12}, NATURAL_WIN_VALS = new int[] {7, 11};
	/**
	 * A sentinel value indicating that the point value for the game has not been set yet.
	 */
	private static final int INVALID_POINT = -1;
	
	/**
	 * Rolls the dice recursively until the user wins or loses.
	 * 
	 * @param point	The value the user must roll to win
	 * @return	Whether or not the user won the game
	 */
	private static boolean rollDice(int point) {
		int sum = 0;
		int[] diceResult = new int[2];
		for (int i = 0; i < diceResult.length; i++) {
			diceResult[i] = rng.nextInt(DICE_MAX - DICE_MIN + 1) + DICE_MIN;
			sum += diceResult[i];
		}
		System.out.print("You rolled ");
		for (int i = 0; i < diceResult.length; i++) {
			System.out.print(diceResult[i] + (i < diceResult.length - 1 ? " + " : " = "));
		}
		System.out.println(sum);
		if (point == INVALID_POINT) {
			for (int i = 0; i < NATURAL_WIN_VALS.length; i++) {
				if (sum == NATURAL_WIN_VALS[i]) {
					return true;
				}
			}
			for (int i = 0; i < CRAP_VALS.length; i++) {
				if (sum == CRAP_VALS[i]) {
					return false;
				}
			}
			point = sum;
			System.out.println("Point is set to " + point);
		} else if (sum == 7) {
			return false;
		} else if (sum == point) {
			return true;
		}
		return rollDice(point);
	}
	/**
	 * The program's entry point.
	 * 
	 * @param args	The command-line arguments
	 */
	public static void main(String[] args) {
		System.out.println(rollDice(INVALID_POINT) ? "Congratulations, You win" : "Better Luck Next Time, You lose");
	}
}
