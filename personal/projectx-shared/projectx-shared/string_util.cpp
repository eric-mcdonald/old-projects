#include "stdafx.h"

#include "string_util.h"

namespace projx {

	std::string StrReplaceAll(std::string input, const std::string &old_value, const std::string &new_value) {
		for (size_t pos = input.find(old_value); pos != std::string::npos; pos = input.find(old_value)) {
			input.replace(pos, old_value.length(), new_value);
		}
		return input;
	}
	ProgError StrGenRandom(const std::string &valid_chars, size_t min_len, size_t max_len, std::string *output) {
		if (output == nullptr || min_len > max_len || valid_chars.empty()) {
			return ErrorCodes::kInvalidParam;
		}
		*output = "";
		for (size_t i = 0; i < max_len || output->length() < min_len; ++i) {
			if (rand() % 2) {
				*output += valid_chars[rand() % valid_chars.length()];
			}
		}
		return ErrorCodes::kSuccess;
	}
	std::string StrToLowercase(std::string input) {
		for (size_t i = 0; i < input.length(); ++i) {
			if (!islower(input[i])) {
				input[i] = tolower(input[i]);
			}
		}
		return input;
	}
	std::string StrToUppercase(std::string input) {
		for (size_t i = 0; i < input.length(); ++i) {
			if (!isupper(input[i])) {
				input[i] = toupper(input[i]);
			}
		}
		return input;
	}
	ProgError StrSplt(std::string input, const std::string &delimiter, std::vector<std::string> *output) {
		if (output == nullptr || delimiter.empty()) {
			return ErrorCodes::kInvalidParam;
		}
		size_t delim_pos = input.find_first_of(delimiter);
		if (delim_pos == std::string::npos) {
			output->push_back(input);
			return ErrorCodes::kSuccess;
		}
		else {
			std::string token;
			do {
				token = input.substr(0, delim_pos - 0);
				input.erase(0, (delim_pos + delimiter.length()) - 0);
				output->push_back(token);
				delim_pos = input.find_first_of(delimiter);
			} while (delim_pos != std::string::npos);
			if (!input.empty()) {
				output->push_back(input);
			}
		}
		return ErrorCodes::kSuccess;
	}

}  // namespace projx
