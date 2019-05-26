/*
Author: Eric McDonald
Date created: 2017-10-02
Program name: IPC144 Week 4 (At-home)
Purpose: To display the high and low temperatures for each inputted day, the highest temperature, the lowest temperature, and to calculate the mean temperature.
*/

#pragma warning(disable : 4996)

#include <stdio.h>
#include <stdlib.h>

/*
Description: Initializes all elements of an array of temperatures to zero.
Parameters:
	temperatures - An allocated array of temperatures.
	temperatures_sz - The number of elements in temperatures.
Return value: A non-zero value if temperatures and temperatures_sz are valid.
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
/*
Description: Tests whether or not the upper bound average is a signal to exit the program.
Parameters:
	upper_bound_avg - A threshold for the amount of days to use to calculate an average temperature.
Return value: A non-zero value if upper_bound_avg is a negative number.
*/
inline int IsUpperBoundExit(int upper_bound_avg) {
	return upper_bound_avg < 0;
}
int main() {
	unsigned int days, current_day;
	int *high_temperatures, *low_temperatures;
	unsigned int highest_day, lowest_day;
	int highest_temperature, lowest_temperature;
	int upper_bound_avg;
	float avg_temperature;

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
	for (current_day = 1U; current_day <= days; ++current_day) {
		printf("Day %u - High: ", current_day);
		scanf("%i", &high_temperatures[current_day - 1]);
		printf("Day %u - Low: ", current_day);
		scanf("%i", &low_temperatures[current_day - 1]);
	}

	printf("\nDay  Hi  Low\n");
	for (current_day = 1U; current_day <= days; ++current_day) {
		printf("%u    %i    %i\n", current_day, high_temperatures[current_day - 1], low_temperatures[current_day - 1]);
	}
	highest_day = lowest_day = 1; // Eric McDonald: 1 is the lowest day, not zero.
	upper_bound_avg = 0;
	highest_temperature = lowest_temperature = 0;
	for (current_day = 0U; current_day < days; ++current_day) {
		if (highest_temperature < high_temperatures[current_day]) {
			highest_temperature = high_temperatures[current_day];
			highest_day = current_day + 1;
		}
		if (lowest_temperature > low_temperatures[current_day]) {
			lowest_temperature = low_temperatures[current_day];
			lowest_day = current_day + 1;
		}
	}
	printf("\nThe highest temperature was %i, on day %u\n", highest_temperature, highest_day);
	printf("The lowest temperature was %i, on day %u\n", lowest_temperature, lowest_day);
	do {
		printf("\nEnter a number between 1 and 4 to see the average temperature for the entered number of days, enter a negative number to exit: ");
		scanf("%i", &upper_bound_avg);
		printf("\n");
		if (!IsUpperBoundExit(upper_bound_avg)) {
			// Eric McDonald: GCC throws a warning if I don't put the unecessary parenthesis there.
			while ((upper_bound_avg >= 0 && upper_bound_avg < 1) || upper_bound_avg > 4) {
				printf("Invalid entry, please enter a number between 1 and 4, inclusive: ");
				scanf("%i", &upper_bound_avg);
				printf("\n");
			}
			avg_temperature = 0.0F;
			for (current_day = 0U; current_day < (unsigned int) upper_bound_avg; ++current_day) {
				avg_temperature += (high_temperatures[current_day] + low_temperatures[current_day]) / 2.0F;
			}
			avg_temperature /= upper_bound_avg;
			printf("The average temperature up to day %i is: %.2f\n", upper_bound_avg, avg_temperature);
		}
	} while (!IsUpperBoundExit(upper_bound_avg));

	printf("Goodbye!\n");
	free(high_temperatures);
	free(low_temperatures);
	return 0;
}