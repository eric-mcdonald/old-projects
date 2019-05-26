// Workshop 9 - Multi-Threading
// SecureData.cpp
// Chris Szalwinski after Cornel Barna
// 2019/03/19

#include <iostream>
#include <fstream>
#include <string>
#include <thread>
#include <functional>
#include "SecureData.h"

using namespace std;

namespace sict {

	void converter(char* t, char key, int n, const Cryptor& c) {
		for (int i = 0; i < n; i++)
			t[i] = c(t[i], key);
	}

	SecureData::SecureData(const char* file, char key, ostream* pOfs) {
		ofs = pOfs;

		// open text file
		fstream input(file, std::ios::in);
		if (!input)
			throw string("\n***Failed to open file ") +
			string(file) + string(" ***\n");

		// copy from file into memory
		input.seekg(0, std::ios::end);
		nbytes = (int)input.tellg() + 1;

		text = new char[nbytes];

		input.seekg(ios::beg);
		int i = 0;
		input >> noskipws;
		while (input.good())
			input >> text[i++];
		text[nbytes - 1] = '\0';
		*ofs << "\n" << nbytes - 1 << " bytes copied from file "
			<< file << " into memory (null byte added)\n";
		encoded = false;

		// encode using key
		code(key);
		*ofs << "Data encrypted in memory\n";
	}

	SecureData::~SecureData() {
		delete[] text;
	}

	void SecureData::display(std::ostream& os) const {
		if (text && !encoded)
			os << text << std::endl;
		else if (encoded)
			throw std::string("\n***Data is encoded***\n");
		else
			throw std::string("\n***No data stored***\n");
	}

	void SecureData::code(char key) {
		std::thread first_half(converter, text, key, nbytes / 2, Cryptor());
		std::thread second_half(converter, &text[nbytes / 2], key, nbytes / 2, Cryptor());
		first_half.join();
		second_half.join();
		encoded = !encoded;
	}

	void SecureData::backup(const char* file) {
		if (!text)
			throw std::string("\n***No data stored***\n");
		else if (!encoded)
			throw std::string("\n***Data is not encoded***\n");
		else
		{
			std::ofstream store_file(file, std::ios_base::out | std::ios_base::binary);
			if (store_file.is_open()) {
				store_file.write(text, nbytes);
				store_file.close();
			}

		}
	}

	void SecureData::restore(const char* file, char key) {
		std::ifstream store_file(file, std::ios_base::in | std::ios_base::binary);
		store_file.seekg(0, store_file.end);
		unsigned int file_sz = static_cast<unsigned int>(store_file.tellg());
		store_file.seekg(0, store_file.beg);
		text = new char[file_sz] {0};
		store_file.read(text, file_sz);
		*ofs << "\n" << nbytes << " bytes copied from binary file "
			<< file << " into memory.\n";

		encoded = true;

		// decode using key
		code(key);

		*ofs << "Data decrypted in memory\n\n";
	}

	std::ostream& operator<<(std::ostream& os, const SecureData& sd) {
		sd.display(os);
		return os;
	}
}
