/**********************************************
Workshop #3
Course: JAC444 - Winter 2020
Last Name: McDonald
First Name: Eric
ID: 153581160
Section: NAB
This assignment represents my own work in accordance with Seneca
Academic Policy.
Eric McDonald
Date: 2020-02-06
**********************************************/
package ca.senecacollege.emcdonald5;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import ca.senecacollege.emcdonald5.util.AssertCallback;
import ca.senecacollege.emcdonald5.util.ConsoleIo;

/**
 * Contains the program's entry point and input handling.
 * 
 * @author Eric McDonald
 */
public final class Main {
	/**
	 * The I/O handler.
	 */
	private static final ConsoleIo console = new ConsoleIo();
	/**
	 * The list of registered accounts.
	 */
	private static final Map<Integer, Account> accounts = new HashMap<Integer, Account>();
	/**
	 * The program option constants.
	 */
	private static final int ADD_BALANCE = 1, WITHDRAW_BALANCE = 2, DISPLAY_ACCOUNT_INFO = 3, EXIT_PROGRAM = 4;

	/**
	 * Handles what happens when the user chooses to display an account's information.
	 * 
	 * @param account	the account to display information for
	 */
	private static void handleDisplayInfo(final Account account) {
		System.out.println(account);
		if (account.getBalance() < 0.0F) {
			System.out.println("Loan given: $"+ String.format("%.2f", -account.getBalance()));
		}
		System.out.println("amount of monthly payments needed: " + account.calcMonthsLeft() + ", monthly payment day: every " + new SimpleDateFormat("d").format(account.getCreationDate()) + "th day of the month");
	}
	/**
	 * Prompts the user for an amount to add to an account.
	 * 
	 * @param account	the account to add the inputted balance to
	 */
	private static void handleAddBalance(final Account account) {
		float balanceOff = console.promptFloat("Enter the amount to add: $", new AssertCallback<Float>() {
			@Override
			public void assertValid(Float value) throws RuntimeException {
				if (value <= 0.0F) {
					throw new RuntimeException("The specified balance offset is too low: " + value);
				}
			}
		});
		account.adjustBalance(balanceOff);
		handleDisplayInfo(account);
	}
	/**
	 * Prompts the user for an amount to withdraw from an account.
	 * 
	 * @param account	the account to withdraw the inputted balance from
	 */
	private static void handleWithdrawBalance(final Account account) {
		float balanceOff = console.promptFloat("Enter the amount to withdraw: $", new AssertCallback<Float>() {
			@Override
			public void assertValid(Float value) throws RuntimeException {
				if (value <= 0.0F) {
					throw new RuntimeException("The specified balance offset is too low: " + value);
				}
			}
		});
		account.adjustBalance(-balanceOff);
		handleDisplayInfo(account);
	}
	/**
	 * The program's entry point.
	 * 
	 * @param args	The command-line arguments
	 */
	public static void main(String[] args) {
		// This workshop is not my best work. I was pretty stressed and under pressure when I coded this workshop...
		while (true) {
			try {
				// Negative numbers are allowed as an account ID because why limit it to only positive integers?
				int accountId = console.promptInt("Enter an account ID (a new one will be created if it doesn't exist): ", null);
				if (!accounts.containsKey(accountId)) {
					float balance = console.promptFloat("Enter an account balance: $", new AssertCallback<Float>() {
						@Override
						public void assertValid(Float value) throws RuntimeException {
							if (value < 0.01F) {
								throw new RuntimeException("The specified balance is too low: " + value);
							}
						}
					});
					float interestRate = console.promptFloat("Enter an annual interest rate: %", new AssertCallback<Float>() {
						@Override
						public void assertValid(Float value) throws RuntimeException {
							if (value <= 0.0F || value > 100.0F) {
								throw new RuntimeException("The specified interest rate is invalid: " + value);
							}
						}
					});
					interestRate = interestRate / 100.0F / 12.0F;
					accounts.put(accountId, new Account(balance, interestRate));
				}
				final Account foundAcc = accounts.get(accountId);
				int action = console.promptInt("Please select an action: " + ADD_BALANCE + ". add balance, " + WITHDRAW_BALANCE + ". withdraw balance, " + 
						DISPLAY_ACCOUNT_INFO + ". display account info, " + EXIT_PROGRAM + ". exit: ", 
						new AssertCallback<Integer>() {
					@Override
					public void assertValid(Integer value) throws RuntimeException {
						if (value < ADD_BALANCE || value > EXIT_PROGRAM) {
							throw new RuntimeException("The specified action is too invalid: " + value);
						}
					}
				});
				if (action == EXIT_PROGRAM) {
					System.exit(0);
				}
				System.out.println("--- Account #" + accountId + " ---");
				switch (action) {
				case ADD_BALANCE:
					handleAddBalance(foundAcc);
					break;
				case WITHDRAW_BALANCE:
					handleWithdrawBalance(foundAcc);
					break;
				case DISPLAY_ACCOUNT_INFO:
					handleDisplayInfo(foundAcc);
					break;
				}
			} catch (final RuntimeException error) {
				System.err.println(error.getMessage());
				console.consumeNextLine();  // Prevents ConsoleIo#input from skipping over the user's input.
			}
		}
	}
}
