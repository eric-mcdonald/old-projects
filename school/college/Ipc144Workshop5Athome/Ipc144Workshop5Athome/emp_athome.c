#pragma warning(disable : 4996)

/*
Author: Eric McDonald
Date created: 2017-10-09
Program name: IPC144 - Workshop 5 (At-home)
Purpose: To add, remove, and display employees and to update their salaries.
*/

#include <stdio.h>

#define SIZE 4

struct Employee {
	int id;
	int age;
	double salary;
};

int main() {
	int option = 2;
	struct Employee employees[SIZE];
	int current_employee, employee_count = 0;
	int employee_id, target_employee = 0;

	printf("---=== EMPLOYEE DATA ===---\n");
	do {
		printf("\n1. Display Employee Information");
		printf("\n2. Add Employee");
		printf("\n3. Update Employee Salary");
		printf("\n4. Remove Employee");
		printf("\n0. Exit");
		printf("\n\nPlease select from the above options: ");
		scanf("%i", &option);
		printf("\n");
		switch (option) {
		case 0:
			// Eric McDonald: Empty switch case to simply exit the loop.
			break;
		case 1:
			printf("EMP ID  EMP AGE EMP SALARY\n======  ======= ==========\n");
			for (current_employee = 0; current_employee < employee_count; ++current_employee) {
				printf("   %i       %i   %.2lf\n", employees[current_employee].id, employees[current_employee].age, employees[current_employee].salary);
			}
			break;
		case 2:
			printf("Adding Employee\n===============");
			if (employee_count >= SIZE) {
				printf("\nERROR!!! Maximum Number of Employees Reached\n");
			}
			else {
				printf("\nEnter Employee ID: ");
				scanf("%i", &employees[employee_count].id);
				printf("Enter Employee Age: ");
				scanf("%i", &employees[employee_count].age);
				printf("Enter Employee Salary: ");
				scanf("%lf", &employees[employee_count].salary);
				++employee_count;
			}
			break;
		case 3:
			printf("Update Employee Information\n===========================");
			printf("\nEnter Employee ID: ");
			scanf("%i", &employee_id);
			for (current_employee = 0; current_employee < employee_count; ++current_employee) {
				if (employees[current_employee].id == employee_id) {
					target_employee = current_employee;
				}
			}
			printf("The current salary is %.2lf\n", employees[target_employee].salary);
			printf("Enter Employee New Salary: ");
			scanf("%lf", &employees[target_employee].salary);
			break;
		case 4:
			printf("Remove Employee\n===============");
			printf("\nEnter Employee ID: ");
			scanf("%i", &employee_id);
			for (current_employee = 0; current_employee < employee_count; ++current_employee) {
				if (employees[current_employee].id == employee_id) {
					target_employee = current_employee;
				}
			}
			printf("Employee %i will be removed\n", target_employee);
			--employee_count;
			break;
		default:
			printf("ERROR: Incorrect Option: Try Again\n");
		}
	} while (option != 0);

	printf("Exiting Employee Data Program. Good Bye!!!\n");
	return 0;
}