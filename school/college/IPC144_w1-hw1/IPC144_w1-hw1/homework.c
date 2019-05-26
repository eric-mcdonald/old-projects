/*
Programmer:	Eric McDonald
Program Name:	homework.c
Date:	September 7th, 2017
Purpose:	Displays the first character of a student name, marks for two tests, final exam, and average mark.
*/

/*
I am disabling MSVC's warnings for not using their 'secure' versions of C library functions (example: strtok_s instead of strtok). I am using my laptop to code this. I have my compiler flags optimized completely for efficiency in speed, to have production-level warnings, and to treat warnings as errors.
I have it setup like this for my other, non-school related projects.
If this is an issue, please let me know and I will reset my compiler flags to their defaults.
*/
#pragma warning(disable : 4996)

#include <stdio.h>
#include <stdlib.h>
//#include <windows.h>

/*
I am using Google's C++ naming conventions, since none was specified by the professor.
Please note I am more familiar with C++ than C; so, I do not know the proper terminology for certain C features.
Also, I made some functions for printf and scanf because I am too tired right now to type out printf and scanf with all of their parameters and such. Usually, I would not do this since it is overkill for such a small project.
*/

// Prints the newline character to the standard character output stream (or whatever it is called in C)
int PrintNewline() {
	return printf("\n");
}
int ScanGradeOrDie(float *grade_num) {
	int chars_read = scanf("%f", grade_num);
	if (chars_read < 0)
		exit(chars_read);
	return chars_read;
}
int main() {
	char first_letter;
	printf("Please, enter the first letter of your first name:");
	scanf("%c", &first_letter);

	// Commented out the newlines because it is already being written to the character output stream as part of the string in printf.
	//PrintNewline();

	float test_one, test_two, test_final;
	printf("Please, enter the grade for your test one:");
	ScanGradeOrDie(&test_one);
	//PrintNewline();
	printf("Please, enter the grade for your test two:");
	ScanGradeOrDie(&test_two);
	//PrintNewline();
	printf("Please, enter the grade for your final test:");
	ScanGradeOrDie(&test_final);

	PrintNewline();
	printf("The student with the first letter %c\n\n", first_letter);

	printf("Test one\tTest two\tFinal test\n");
	printf("-----------------------------------------\n");
	printf("%f\t%f\t%f\n", test_one, test_two, test_final);
	PrintNewline();
	printf("The average mark: %f\n", (test_one + test_two + test_final) / 3);

	//Sleep(INFINITE);
	return 0;
}