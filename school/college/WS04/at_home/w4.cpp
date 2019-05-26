// Workshop 4 - Containers
// w4.cpp
// Chris Szalwinski
// 2019/01/27

#include <iostream>
#include <fstream>
#include <utility>
#include "Message.h"
#include "Notifications.h"
#include "MessagePack.h"
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

	MessagePack pack_1(message, no_msgs);
	std::cout << "Message Pack 1 Contents\n" << "=======================\n";
	std::cout << " No of Messages " << pack_1.size() << std::endl;
	std::cout << pack_1 << std::endl;

	MessagePack pack_2 = std::move(pack_1);
	std::cout << "Message Pack 2 Contents\n" << "=======================\n";
	std::cout << " No of Messages " << pack_2.size() << std::endl;
	std::cout << pack_2 << std::endl;

	std::cout << "Message Pack 1 Contents\n" << "=======================\n";
	std::cout << " No of Messages " << pack_1.size() << std::endl;
	std::cout << pack_1 << std::endl;

	{
		Notifications notifications(no_msgs);
		std::cout << "Notification Contents\n" << "=====================\n";
		std::cout << " No of Messages " << notifications.size() << std::endl;
		std::cout << notifications << std::endl;

		for (int i = 0; i < no_msgs; i++) {
			notifications += *message[i];
		}
		if (notifications.size() > 0) notifications -= *message[0];

		std::cout << "Notification Contents\n" << "=====================\n";
		std::cout << " No of Messages " << notifications.size() << std::endl;
		std::cout << notifications << std::endl;

		Notifications backup = std::move(notifications);
		std::cout << "Backup Contents\n" << "===============\n";
		std::cout << " No of Messages " << backup.size() << std::endl;
		std::cout << backup << std::endl;

		std::cout << "Notification Contents\n" << "=====================\n";
		std::cout << " No of Messages " << notifications.size() << std::endl;
		std::cout << notifications << std::endl;
	}

	// deallocate messages
	//
	for (int i = 0; i < no_msgs; i++) {
		delete message[i];
		message[i] = nullptr;
	}
	delete[] message;
}