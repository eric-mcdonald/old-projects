// Workshop 5 - Functions
// w5.cpp
// Chris Szalwinski
// 2019/02/10

#include <iostream>
#include <iomanip>
#include <fstream>
#include <string>
#include "KVPair.h"
#include "KVList.h"
#include "Taxable.h"
using namespace sict;

float HST = 0.13f;
int fieldWidth = 10;

template <typename L, typename T, typename K, typename V>
L createList(char* filename) {
	std::ifstream file(filename);
	if (!file) {
		L list(0);
		return std::move(list);
	}
	int no_records = 0;
	do {
		char c = file.get();
		if (file.good() && c == '\n') no_records++;
	} while (!file.eof());
	file.clear();
	file.seekg(std::ios::beg);
	L list(no_records);
	do {
		K key;
		V value;
		file >> key;
		if (file) {
			file >> value;
			file.ignore();
			list.push_back(T(key, value));
		}
	} while (file);

	return std::move(list);
}

int main(int argc, char* argv[]) {
	std::cout << "Command Line : ";
	for (int i = 0; i < argc; i++) {
		std::cout << argv[i] << ' ';
	}
	std::cout << std::endl;

	// check for command line errors
	if (argc != 2) exit(1);

	// set for fixed, 2-decimal point output
	std::cout << std::fixed << std::setprecision(2);

	// process price list file
	try {
		KVList<KVPair<std::string, float>> priceList = createList<
			KVList<KVPair<std::string, float>>,
			KVPair<std::string, float>,
			std::string,
			float>
			(argv[1]);
		std::cout << "\nPrice List with G+S Taxes Included\n==================================\n";
		std::cout << "Description:      Price Price+Tax\n";
		priceList.display(std::cout, Taxable(HST));
	}
	catch (sict::Error<size_t> &error) {
		std::cerr << error.GetErrorTxt() << std::endl;
		exit(1);
	}
	catch (...) {
		exit(2);
	}
	try {
		KVList<KVPair<int, float>> grades = createList<KVList<KVPair<int, float>>, KVPair<int, float>, int, float>("Student.txt");
		grades.display(std::cout, [](float value) {
			if (value >= 90.0f) {
				return "A+";
			}
			else if (value >= 80.0f) {
				return "A ";
			}
			else if (value >= 75.0f) {
				return "B+";
			}
			else if (value >= 70.0f) {
				return "B ";
			}
			else if (value >= 65.0f) {
				return "C+";
			}
			else if (value >= 60.0f) {
				return "C ";
			}
			else if (value >= 55.0f) {
				return "D+";
			}
			else if (value >= 50.0f) {
				return "D ";
			}
			else {
				return "F ";
			}
		});
	} catch (sict::Error<size_t> &error) {
		std::cerr << error.GetErrorTxt() << std::endl;
		exit(3);
	}
	catch (...) {
		exit(4);
	}
}