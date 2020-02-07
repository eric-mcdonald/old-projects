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

import java.text.NumberFormat;
import java.util.Locale;

import ca.senecacollege.emcdonald5.tax.TaxCalculator;
import ca.senecacollege.emcdonald5.tax.TaxRate;
import ca.senecacollege.emcdonald5.util.AssertCallback;
import ca.senecacollege.emcdonald5.util.ConsoleIo;
import ca.senecacollege.emcdonald5.util.StringUtil;

/**
 * Contain's this program's input handling.
 * 
 * @author Eric McDonald
 */
public final class Workshop2 {
	/**
	 * The I/O handler.
	 */
	private final ConsoleIo io = new ConsoleIo();
	/**
	 * The tax tables.
	 */
	private static TaxCalculator taxCalc2001, taxCalc2009;
	
	static {
		taxCalc2001 = new TaxCalculator("2001", new TaxRate[][] {new TaxRate[] {new TaxRate(0.15F, 0, 27050), new TaxRate(0.275F, 27050, 65550), 
				new TaxRate(0.305F, 65550, 136750), new TaxRate(0.355F, 136750, 297350), new TaxRate(0.391F, 297350, Integer.MAX_VALUE)},
				
				new TaxRate[] {new TaxRate(0.15F, 0, 45200), new TaxRate(0.275F, 45200, 109250), 
						new TaxRate(0.305F, 109250, 166500), new TaxRate(0.355F, 166500, 297350), new TaxRate(0.391F, 297350, Integer.MAX_VALUE)},
		
				new TaxRate[] {new TaxRate(0.15F, 0, 22600), new TaxRate(0.275F, 22600, 54625), 
						new TaxRate(0.305F, 54625, 83250), new TaxRate(0.355F, 83250, 148675), new TaxRate(0.391F, 148675, Integer.MAX_VALUE)},
				
				new TaxRate[] {new TaxRate(0.15F, 0, 36250), new TaxRate(0.275F, 36250, 93650), 
						new TaxRate(0.305F, 93650, 151650), new TaxRate(0.355F, 151650, 297350), new TaxRate(0.391F, 297350, Integer.MAX_VALUE)}
		});
		taxCalc2009 = new TaxCalculator("2009", new TaxRate[][] {new TaxRate[] {new TaxRate(0.10F, 0, 8350), new TaxRate(0.15F, 8350, 33950), new TaxRate(0.25F, 33950, 82250),
				new TaxRate(0.28F, 82250, 171550), new TaxRate(0.33F, 171550, 372950), new TaxRate(0.35F, 372950, Integer.MAX_VALUE)},
				
				new TaxRate[] {new TaxRate(0.10F, 0, 16700), new TaxRate(0.15F, 16700, 67900), new TaxRate(0.25F, 67900, 137050),
						new TaxRate(0.28F, 137050, 208850), new TaxRate(0.33F, 208850, 372950), new TaxRate(0.35F, 372950, Integer.MAX_VALUE)},
				
				new TaxRate[] {new TaxRate(0.10F, 0, 8350), new TaxRate(0.15F, 8350, 33950), new TaxRate(0.25F, 33950, 68525),
						new TaxRate(0.28F, 68525, 104425), new TaxRate(0.33F, 104425, 186475), new TaxRate(0.35F, 186475, Integer.MAX_VALUE)},
		
				new TaxRate[] {new TaxRate(0.10F, 0, 11950), new TaxRate(0.15F, 11950, 45500), new TaxRate(0.25F, 45500, 117450),
						new TaxRate(0.28F, 117450, 190200), new TaxRate(0.33F, 190200, 372950), new TaxRate(0.35F, 372950, Integer.MAX_VALUE)}
		});
	}
	
	/**
	 * Prompts the user for a single tax calculation input and displays the resulting taxes.
	 */
	private void handleTaxCalc() {
		System.out.println("0 - single filer\n" + 
				"1 - married jointly or qualifying widow(er)\n" + 
				"2 - married separately\n" + 
				"3 - head of household");
		final int filingStatus = io.promptInt("Enter the filing status: ", new AssertCallback<Integer>() {
			@Override
			public void assertValid(Integer value) throws RuntimeException {
				if (value < TaxRate.SINGLE_FILER || value > TaxRate.HOUSE_HEAD) {
					throw new RuntimeException("The specified filing status is invalid: " + value);
				}
			}
		});
		float taxableIncome = io.promptFloat("Enter the Taxable Income: ", new AssertCallback<Float>() {
			@Override
			public void assertValid(Float value) throws RuntimeException {
				if (value <= 0.0F) {
					throw new RuntimeException("The specified taxable income is invalid: " + value);
				}
			}
		});
		System.out.println("Tax is: $" + StringUtil.formatTaxText(taxCalc2009.calcTaxes(taxableIncome, filingStatus)));
	}
	/**
	 * Prints a single tax table with incomes from the specified minimum and maximum. It increments by incomeStep.
	 * 
	 * @param calculator	the tax table
	 * @param incomeStep	the amount to increment the taxable income by
	 * @param minIncome		the minimum taxable income
	 * @param maxIncome		the maximum taxable income
	 */
	private void printTaxTable(final TaxCalculator calculator, float incomeStep, final float minIncome, final float maxIncome) {
		System.out.println(calculator + " for taxable income from $" + NumberFormat.getNumberInstance(Locale.US).format(minIncome) + " to $" + NumberFormat.getNumberInstance(Locale.US).format(maxIncome) + "\n" + 
				"-----------------------------------------------------------------------------------------------------\n" + 
				"Taxable Income\tSingle\tMarried Joint or Qualifying Window(er)\tMarried Separate\tHead of House\n" +
				"-----------------------------------------------------------------------------------------------------");
		for (float currentIncome = minIncome; currentIncome <= maxIncome; currentIncome += incomeStep) {
			System.out.print(StringUtil.formatTaxText(currentIncome) + "\t");
			for (int status = TaxRate.SINGLE_FILER; status <= TaxRate.HOUSE_HEAD; status++) {
				System.out.print(StringUtil.formatTaxText(calculator.calcTaxes(currentIncome, status)));
				if (status <= TaxRate.HOUSE_HEAD - 1) {
					System.out.print(" \t");
				}
			}
			System.out.println();
		}
	}
	/**
	 * Prints the 2001 and 2009 US tax tables.
	 */
	private void handleTaxTables() {
		final float minIncome = io.promptFloat("Enter the amount From: ", new AssertCallback<Float>() {
			@Override
			public void assertValid(Float value) throws RuntimeException {
				if (value <= 0.0F) {
					throw new RuntimeException("The specified minimum income is invalid: " + value);
				}
			}
		});
		final float maxIncome = io.promptFloat("Enter the amount To: ", new AssertCallback<Float>() {
			@Override
			public void assertValid(Float value) throws RuntimeException {
				if (value <= 0.0F || value <= minIncome) {
					throw new RuntimeException("The specified maximum income is invalid: " + value);
				}
			}
		});
		final float incomeStep = (maxIncome - minIncome) / 10.0F;
		printTaxTable(taxCalc2001, incomeStep, minIncome, maxIncome);
		System.out.println();
		printTaxTable(taxCalc2009, incomeStep, minIncome, maxIncome);
	}
	/**
	 * Starts this program.
	 */
	void start() {
		System.out.println("1. Compute personal income Tax\n" + 
				"2. Print the tax tables for taxable incomes (with range)\n" + 
				"3. Exit");
		try {
			final int choice = io.promptInt("Please enter your choice: ", 
					new AssertCallback<Integer>() {
				@Override
				public void assertValid(Integer value) throws RuntimeException {
					if (value < 1 || value > 3) {
						throw new RuntimeException("The specified choice is invalid: " + value);
					}
				}
			});
			if (choice == 3) {
				System.exit(0);
			}
			if (choice == 1) {
				handleTaxCalc();
			} else {
				handleTaxTables();
			}
		} catch (final RuntimeException error) {
			System.err.println(error.getMessage());
		}
	}
}
