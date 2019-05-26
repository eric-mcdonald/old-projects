/*
Name:      Eric McDonald
Student#:  153581160
Section:   IPC144A
*/

// Start your code below:

#include <stdio.h>

int main() {
	double paid_amount, change;
	int loonies, quarters;
	printf("Please enter the amount to be paid: $");
	scanf("%lf", &paid_amount);
	loonies = (int)paid_amount;
	change = paid_amount - loonies;
	quarters = (int)(change / 0.25);
	printf("Loonies required: %i, balance owing $%.2lf\n", loonies, change);
	printf("Quarters required: %i, balance owing $%.2lf\n", quarters, change - 0.25 * quarters);
	return 0;
}