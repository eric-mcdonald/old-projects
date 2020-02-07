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
package ca.senecacollege.emcdonald5.tax;

/**
 * Contains an individual tax rate.
 * 
 * @author Eric McDonald
 */
public class TaxRate {
	/**
	 * The percentage of income that is taxed. This percentage ranges from 0.0-1.0.
	 */
	private float taxPercent;
	/**
	 * The boundaries of the tax range.
	 */
	private int lowBracket, highBracket;
	/**
	 * Constants representing each marital status for taxing.
	 */
	public static final int SINGLE_FILER = 0, MARRIED_JOINT = 1, MARRIED_SEPARATE = 2, HOUSE_HEAD = 3;
	
	/**
	 * Constructs a new TaxRate with the specified tax percentage and range.
	 * 
	 * @param taxPercent	the percentage of income to tax
	 * @param lowBracket	the minimum of the tax rate
	 * @param highBracket	the maximum of the tax rate
	 */
	public TaxRate(float taxPercent, int lowBracket, int highBracket) {
		this.taxPercent = taxPercent;
		this.lowBracket = lowBracket;
		this.highBracket = highBracket;
	}
	
	/**
	 * Applies this tax rate to the income, if applicable. Otherwise, it returns -1.0 indicating that the taxable income was not modified.
	 * 
	 * @param taxableIncome	the income that can be taxed
	 * @return	the applicable taxes, or -1.0 if none
	 */
	public float applyTaxCalc(float taxableIncome) {
		final float prevTaxIncome = taxableIncome;
		if (taxableIncome >= lowBracket) {
			if (taxableIncome > highBracket) {
				taxableIncome = highBracket;
			}
			taxableIncome = (taxableIncome - lowBracket) * taxPercent;
		}
		return prevTaxIncome != taxableIncome ? taxableIncome : -1.0F;
	}
}
