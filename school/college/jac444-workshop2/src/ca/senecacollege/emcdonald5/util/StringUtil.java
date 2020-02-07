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

/**
 * Contains static methods to manipulate text.
 * 
 * @author Eric McDonald
 */
public final class StringUtil {
	/**
	 * Returns a formatted a tax-related number.
	 * 
	 * @param taxes	the tax number to format
	 * @return	the formatted tax string
	 */
	public static String formatTaxText(final float taxes) {
		return String.format("%.2f", taxes);
	}
}
