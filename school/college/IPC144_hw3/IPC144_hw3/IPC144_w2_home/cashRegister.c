/*
     Name:      Eric McDonald 
     Student#:  153581160
     Section:   IPC144A
*/

// Start your code below:

//#pragma warning(disable : 4996)

#include <stdio.h>
#include <stdlib.h>
//#include <math.h>
//#include <windows.h>

void PrintChange(const char *message, double *change, double change_val) {
	static const int kChangePrecision = 100;
	int change_amount = (int) (*change / change_val);
	int change_int = (int) (*change * kChangePrecision), change_val_int = (int) (change_val * kChangePrecision);
	if (*change - change_val >= 0.0) {
		*change -= change_amount * change_val;
	}
	printf(message, change_amount, change_int % change_val_int / (double) kChangePrecision);
}
int main() {
	static const double kGstVal = 1.13;
	double amount_owing;
	int chars_read;
	double change;
	int loonies;
	printf("Please enter the amount to be paid: $");
	chars_read = scanf("%lf", &amount_owing);
	if (chars_read < 0) {
		return chars_read;
	}
	printf("GST: %.2lf\n", kGstVal);
	amount_owing = kGstVal * amount_owing + 0.005;
	loonies = (int) amount_owing;
	printf("Balance owing: $%.2lf\n", amount_owing);
	change = amount_owing - loonies;
	printf("Loonies required: %i, balance owing $%.2lf\n", loonies, change);
	PrintChange("Quarters required: %i, balance owing $%.2lf\n", &change, 0.25);
	PrintChange("Dimes required: %i, balance owing $%.2lf\n", &change, 0.10);
	PrintChange("Nickels required: %i, balance owing $%.2lf\n", &change, 0.05);
	PrintChange("Pennies required: %i, balance owing $%.2lf\n", &change, 0.01);
	//Sleep(INFINITE);
	return 0;
}