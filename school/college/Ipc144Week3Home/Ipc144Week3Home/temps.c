/*
Author: Eric McDonald
Date created: 2017-09-22
Program name: IPC144: Week 3 (Home)
Purpose: To calculate the average temperature over NUMS days and display the highest & lowest temperatures plus their days.
*/

// Eric McDonald: Comments about "Nonconforming code" refers to code that does not conform to Google's C++ style guide (modified for C).

#include <stdio.h>
#include <stdlib.h>

// Eric McDonald: Nonconforming code specified: "Before the declaration of main define NUMS as 3"
#define NUMS 4

typedef int TemperatureIn;

inline void ScanTemperatureOrDie(TemperatureIn *temperature) {
	int chars_read = scanf("%ui", temperature);
	if (chars_read < 0) {
		exit(chars_read);
	}
}
inline int IsTemperatureValid(TemperatureIn temperature) {
	return temperature > -41 && temperature < 41;
}
inline void PromptTemperatures(int day, TemperatureIn *high, TemperatureIn *low) {
	printf("\nEnter the high value for day %i: ", day);
	ScanTemperatureOrDie(high);
	printf("\nEnter the low value for day %i: ", day);
	ScanTemperatureOrDie(low);
}
int main() {
	printf("---=== IPC Temperature Analyzer ===---");
	TemperatureIn high_sum = 0, low_sum = 0;
	TemperatureIn highest = 0, lowest = 0;
	int highest_day, lowest_day;
	int day_count;
	for (day_count = 0; day_count < NUMS;) {
		TemperatureIn high, low;
		PromptTemperatures(day_count + 1, &high, &low);
		while (!IsTemperatureValid(high) || !IsTemperatureValid(low) || high <= low) {
			printf("\nIncorrect values, temperatures must be in the range -40 to 40, high must be greater than low.\n");
			PromptTemperatures(day_count + 1, &high, &low);
		}
		high_sum += high;
		low_sum += low;
		if (high > highest) {
			highest = high;
			highest_day = day_count + 1;
		}
		if (low < lowest) {
			lowest = low;
			lowest_day = day_count + 1;
		}
		++day_count;
	}
	printf("\nThe average (mean) temperature was: %.2f\n", ((float)high_sum / NUMS + (float)low_sum / NUMS) / 2.0F);
	printf("The highest temperature was %i, on day %i\n", highest, highest_day);
	printf("The lowest temperature was %i, on day %i\n", lowest, lowest_day);
	return 0;
}