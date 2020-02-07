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

import java.util.Date;

/**
 * A user's bank account. When an account has a negative balance, it is handled as a loan that was given.
 * 
 * @author Eric McDonald
 */
public class Account {
	/**
	 * The account's balance. This can be negative.
	 */
	private float balance;
	/**
	 * The monthly rate of interest ranging from 0.0-1.0.
	 */
	private final float interestRate;
	/**
	 * The date in which this account was created.
	 */
	private final Date creationDate;
	
	/**
	 * Constructs an Account with the specified starting balance and monthly interest rate.
	 * 
	 * @param balance	the starting balance
	 * @param interestRate	the montly interest rate, ranging from 0.0-1.0
	 */
	public Account(float balance, float interestRate) {
		this.balance = balance;
		this.interestRate = interestRate;
		creationDate = new Date();
	}
	
	/**
	 * Adds or removes money from the balance.
	 * 
	 * @param offset	the amount to add or remove from the balance
	 */
	public void adjustBalance(float offset) {
		balance += offset;
	}
	/**
	 * Retrieves the account's balance. A negative balance indicates that it is a loan.
	 * 
	 * @return	the account's balance
	 */
	public float getBalance() {
		return balance;
	}
	/**
	 * Retrieves the monthly interest rate for this account.
	 * 
	 * @return	the account's monthly interest rate
	 */
	public float getInterestRate() {
		return interestRate;
	}
	/**
	 * Retrieves the date in which this account was created.
	 * 
	 * @return	the date of when this account was created
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * Calculates how many months are left to pay off the loan at the specified interest rate, or zero if there is no loan given.
	 * 
	 * @return	the amount of months left to pay off the loan
	 */
	public int calcMonthsLeft() {
		if (balance >= 0.0F) {
			return 0;
		}
		int monthsLeft = 0;
		for (float interest = 0.0F; interest < -balance; interest += -balance * interestRate) {
			++monthsLeft;
		}
		return monthsLeft;
	}
	/**
	 * Retrieves this object as text formatted for console output.
	 */
	@Override
	public String toString() {
		return "balance: $" + String.format("%.2f", balance) + ", annual interest rate: %" + 
				interestRate * 12.0F * 100.0F + ", creation date: " + creationDate;
	}
}
