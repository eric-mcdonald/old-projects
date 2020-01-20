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

/**
 * A callback interface that throws an error if a value is invalid.
 * 
 * @author Eric McDonald
 * @param <T> The type of value to check
 */
public interface AssertCallback<T> {
	/**
	 * Throws an error if the value is invalid.
	 * 
	 * @param value	The value to check against
	 * @throws RuntimeException	if the specified value is invalid
	 */
	void assertValid(T value) throws RuntimeException;
}
