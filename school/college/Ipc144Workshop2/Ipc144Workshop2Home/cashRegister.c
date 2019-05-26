/*
Name:      Eric McDonald
Student#:  153581160
Section:   IPC144A
*/

// Start your code below:

#include <stdio.h>

int main() {
	double amount_owing;
	double change;
	int change_amount, change_int, change_val_int;
	int loonies;

	printf("Please enter the amount to be paid: $");
	scanf("%lf", &amount_owing);
	printf("GST: %.2lf\n", 1.13);
	amount_owing = 1.13 * amount_owing + 0.005;
	loonies = (int)amount_owing;
	printf("Balance owing: $%.2lf\n", amount_owing);
	change = amount_owing - loonies;
	printf("Loonies required: %i, balance owing $%.2lf\n", loonies, change);

	change_amount = (int)(change / 0.25);
	change_int = (int)(change * 100), change_val_int = (int)(0.25 * 100);
	if (change - 0.25 >= 0.00) {
		change -= change_amount * 0.25;
	}
	printf("Quarters required: %i, balance owing $%.2lf\n", change_amount, change_int % change_val_int / 100.0);

	change_amount = (int)(change / 0.10);
	change_int = (int)(change * 100), change_val_int = (int)(0.10 * 100);
	if (change - 0.10 >= 0.00) {
		change -= change_amount * 0.10;
	}
	printf("Dimes required: %i, balance owing $%.2lf\n", change_amount, change_int % change_val_int / 100.0);

	change_amount = (int)(change / 0.05);
	change_int = (int)(change * 100), change_val_int = (int)(0.05 * 100);
	if (change - 0.05 >= 0.00) {
		change -= change_amount * 0.05;
	}
	printf("Nickels required: %i, balance owing $%.2lf\n", change_amount, change_int % change_val_int / 100.0);

	change_amount = (int)(change / 0.01);
	change_int = (int)(change * 100), change_val_int = (int)(0.01 * 100);
	if (change - 0.01 >= 0.00) {
		change -= change_amount * 0.01;
	}
	printf("Pennies required: %i, balance owing $%.2lf\n", change_amount, change_int % change_val_int / 100.0);

	return 0;
}