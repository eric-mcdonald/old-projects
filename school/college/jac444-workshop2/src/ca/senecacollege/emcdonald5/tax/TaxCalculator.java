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

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a tax table and applies the income tax calculations from TaxRate.
 * 
 * @author Eric McDonald
 */
public class TaxCalculator {
	/**
	 * The identifier of this tax calculator. This is typically formatted as a date such as "2001".
	 */
	private final String id;
	/**
	 * The table of taxes for each marital status.
	 */
	private final List<TaxRate[]> taxTable = new ArrayList<TaxRate[]>();
	
	/**
	 * Constructs a new TaxCalculator with the specified identifier and tax table.
	 * 
	 * @param id	the calculator's identifier
	 * @param ratesList	the tax table
	 */
	public TaxCalculator(final String id, TaxRate[][] ratesList) {
		this.id = id;
		for (TaxRate[] rates : ratesList) {
			taxTable.add(rates);
		}
	}
	
	/**
	 * Applies all of the tax equations that apply to the specified amount of income for the martial status.
	 * 
	 * @param taxableIncome	the amount of income
	 * @param filerType	the marital status
	 * @return	the amount of taxes from the income
	 */
	public float calcTaxes(float taxableIncome, int filerType) {
		float taxes = 0.0F;
		TaxRate[] rates = taxTable.get(filerType);
		for (int i = rates.length - 1; i >= 0; i--) {
			final float applicableTax = rates[i].applyTaxCalc(taxableIncome);
			if (applicableTax != -1.0F) {
				taxes += applicableTax;
			}
		}
		return taxes;
	}
	/**
	 * Returns a readable representation of this tax calculator for console output.
	 */
	public String toString() {
		return id + " tax tables";
	}
}
