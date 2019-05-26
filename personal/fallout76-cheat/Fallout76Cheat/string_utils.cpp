#include "stdafx.h"

#include "string_utils.h"

namespace cheat {

	ErrorCode StrSplit(std::string input, const std::string &delimiter, std::vector<std::string> *output) {
		ErrorCode error = CheatErrorCodes::kSuccess;
		if (output == nullptr || input.length() == 0) {
			error = CheatErrorCodes::kInvalidParam;
			return error;
		}
		if (!StrContains(input, delimiter)) {
			output->push_back(input);
			return error;
		}
		size_t delim_pos;
		std::string old_input(input);
		size_t last_delim = old_input.find_last_of(delimiter);
		while ((delim_pos = input.find(delimiter)) != std::string::npos) {
			std::string token = input.substr(0, delim_pos - 0);
			if (token.length() != 0) {
				output->push_back(token);
			}
			input.erase(0, delim_pos + delimiter.length() - 0);
		}
		if (last_delim != std::string::npos && last_delim != old_input.length() - 1) {
			std::string token = old_input.substr(last_delim + 1);
			if (token.length() != 0) {
				output->push_back(token);
			}
		}
		return error;
	}
	std::string StrToUppercase(const std::string &input) {
		std::string output = input;
		for (size_t i = 0; i < input.length(); ++i) {
			output[i] = toupper(input[i]);
		}
		return output;
	}
	std::string StrToLowercase(const std::string &input) {
		std::string output = input;
		for (size_t i = 0; i < input.length(); ++i) {
			output[i] = tolower(input[i]);
		}
		return output;
	}

}  // namespace cheat
