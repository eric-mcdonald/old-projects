/*
Author: Eric McDonald
Date created: 2017-09-14
Program: w2_homework.c
Purpose: To calculate the price of a child's ticket.
*/

#pragma warning(disable : 4996)

#include <stdio.h>
//#include <windows.h>

int main() {
	printf("Please enter a child's age: ");
	unsigned int age;
	int chars_read = scanf("%ui", &age);
	if (chars_read < 0) {
		return chars_read;
	}
	if (age > 12) {
		printf("You must buy an adult ticket.\n");
		return age;
	}
	float price;
	if (age < 6)
		price = 0.0F;
	else
		price = 79.99F * (1.0F - 0.1F);
	printf("The price of a ticket is: $%.2f\n", price);
	//Sleep(INFINITE);
	return 0;
}