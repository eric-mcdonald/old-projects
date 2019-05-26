// Workshop 7 - STL Algorithms
// w7.cpp
// updated by Cornel on 18.10.2018
// updated by Chris Szalwinski 
// 2019/03/10

#include <iostream>
#include <fstream>
#include <string>
#include "DataTable.h"
#include "DataTable.h" // this is intentional
using namespace std;
int FW = 8; // field width
int ND = 4; // precision for numbers

// Reports the Statistics for file named pFileName
//
void processFile(const char* pFileName) {

	cout << endl;
	cout << "****************************************" << endl;
	cout << "*** Processing file [" << pFileName << "]" << endl;
	cout << "****************************************" << endl;

	std::ifstream dataFile(pFileName);
	if (!dataFile) {
		cerr << endl << "***Failed to open file " << pFileName << "***" << endl;
		return;
	}

	try {
		sict::DataTable<float> data(dataFile);
		cout << endl;
		data.displayData(cout);
		data.displayStatistics(cout);
	}
	catch (std::string& msg) {
		cout << "ERROR: " << msg << endl;
	}
}

int main(int argc, char** argv) {
#ifndef SICT_DATA_TABLE_H
	cout << "Improper header guard for DataTable.h! Follow the convention "
		<< "SICT_DATA_TABLE_H when defining a header guard.\n";
#endif

	cout << "Command Line: " << argv[0];
	for (int i = 1; i < argc; i++)
		cout << " " << argv[i];
	cout << endl;
	if (argc < 2) {
		cerr << endl << "***Incorrect number of arguments***" << endl;
		return 1;
	}

	for (int i = 1; i < argc; ++i)
		processFile(argv[i]);
}
