// Workshop 4 - Containers
// w4.cpp
// Chris Szalwinski
// 2019/01/27

#include <iostream>
#include <fstream>
#include <utility>
#include "Message.h"
using namespace sict;

const char REC_DELIMITER = '\n';

int main(int argc, char* argv[]) {
	std::cout << "Command Line : ";
	for (int i = 0; i < argc; i++) {
		std::cout << argv[i] << ' ';
	}
	if (argc == 1) {
		std::cerr << argv[0] << ": missing file operand\n";
		return 1;
	}
	else if (argc > 2) {
		std::cerr << argv[0] << ": too many arguments\n";
		return 2;
	}

	// open user specified file
	//
	std::ifstream file(argv[1]);
	if (!file) {
		std::cerr << argv[0] << "\n***Failed to open " << argv[1] << " ***\n";
		return 3;
	}
	std::cout << "\n\nMessages loaded from file\n=========================\n";

	int max_msgs{ 0 };
	while (file) {
		std::string str;
		getline(file, str, REC_DELIMITER);
		if (file.good()) max_msgs++;
	}
	file.clear();
	Message** message = new Message*[max_msgs];
	file.seekg(0);
	int no_msgs{ 0 };
	while (file) {
		std::string str;
		getline(file, str, REC_DELIMITER);
		if (file.good() && !str.empty())
			message[no_msgs++] = new Message(str);
	}
	file.close(); 

	// display messages read from file
	for (int i = 0; i < no_msgs; i++) 
		message[i]->display(std::cout);
	std::cout << std::endl;

	// deallocate messages
	//
	for (int i = 0; i < no_msgs; i++) {
		delete message[i];
		message[i] = nullptr;
	}
	delete[] message;
}