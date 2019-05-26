#include "Text.h"

#include <fstream>
#include <iostream>
#include <string>

namespace sict {

	Text::Text() : strings(nullptr), strings_sz(0) {}
	Text::Text(const char *filename) : Text() {
		std::ifstream file(filename);
		while (file.good()) {
			std::string line;
			std::getline(file, line);
			++strings_sz;
		}
		--strings_sz;
		strings = new std::string[strings_sz];
		file = std::ifstream(filename);
		size_t string_count = 0;
		while (file.good() && string_count < strings_sz - 1) {
			std::string line;
			std::getline(file, line);
			strings[string_count++] = line;
		}
	}
	Text::Text(const Text &src) {
		strings = new std::string[src.strings_sz];
		for (size_t i = 0; i < src.strings_sz; ++i) {
			strings[i] = src.strings[i];
		}
		strings_sz = src.strings_sz;
	}
	Text::~Text() {
		if (strings != nullptr) {
			delete[] strings;
			strings = nullptr;
			strings_sz = 0;
		}
	}
	Text &Text::operator=(const Text &src) {
		if (this != &src) {
			strings = new std::string[src.strings_sz];
			for (size_t i = 0; i < src.strings_sz; ++i) {
				strings[i] = src.strings[i];
			}
			strings_sz = src.strings_sz;
			}
		return *this;
	}
	size_t Text::size() const {
		return strings_sz;
	}

}  // namespace sict
