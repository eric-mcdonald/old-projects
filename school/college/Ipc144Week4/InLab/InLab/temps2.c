/*
Author: Eric McDonald
Date created: 2017-10-02
Program name: IPC144 Week 4 (In-lab)
Purpose: To display the high and low temperatures for each inputted day.
*/

#include <stdio.h>
#include <stdlib.h>

/*
Description: Initializes all elements of an array of temperatures to zero.
Parameters: 
	temperatures - An allocated array of temperatures.
	temperatures_sz - The number of elements in temperatures.
Return value: Returns a non-zero value if temperatures and temperatures_sz are valid.
*/
int InitTemperatures(int *temperatures, size_t temperatures_sz) {
	int success = 0;
	if (temperatures != 0 && temperatures_sz > 0) {
		size_t i;
		for (i = 0; i < temperatures_sz; ++i) {
			temperatures[i] = 0;
		}
		success = 1;
	}
	return success;
}
int main() {
	unsigned int days, current_day;
	int *high_temperatures, *low_temperatures;

	printf("---=== IPC Temperature Calculator V2.0 ===---\n");
	printf("Please enter the number of days, between 3 and 10, inclusive: ");
	days = 0;
	scanf("%u", &days);
	while (days < 3U || days > 10U) {
		printf("\nInvalid entry, please enter a number between 3 and 10, inclusive: ");
		scanf("%u", &days);
	}
	printf("\n");
	high_temperatures = malloc(days * sizeof(int));
	low_temperatures = malloc(days * sizeof(int));
	InitTemperatures(high_temperatures, days);
	InitTemperatures(low_temperatures, days);
	for (current_day = 1; current_day <= days; ++current_day) {
		printf("Day %u - High: ", current_day);
		scanf("%i", &high_temperatures[current_day - 1]);
		printf("Day %u - Low: ", current_day);
		scanf("%i", &low_temperatures[current_day - 1]);
	}

	printf("\nDay\tHi\tLow\n");
	for (current_day = 1; current_day <= days; ++current_day) {
		printf("%u\t%i\t%i\n", current_day, high_temperatures[current_day - 1], low_temperatures[current_day - 1]);
	}

	free(high_temperatures);
	free(low_temperatures);
	return 0;
}