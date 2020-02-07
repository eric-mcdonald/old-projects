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
package ca.senecacollege.emcdonald5;

/**
 * Contain's entry-point related functionality for this program. This program does a bunch of tax calculations.
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
		new Workshop2().start();  // Delegates to Workshop2#start because I like structuring a program so that
								  // it parses program args in main and then starts the actual program.
								  // I think it's cleaner to code like that.
	}
}
