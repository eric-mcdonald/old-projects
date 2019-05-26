#pragma once

#include <string>
#include <vector>

#include "cheat_def.h"

namespace cheat {

	ErrorCode StrSplit(std::string, const std::string &, std::vector<std::string> *);
	std::string StrToUppercase(const std::string &);
	std::string StrToLowercase(const std::string &);
	inline bool StrStartsWith(const std::string &input, const std::string &start_str) {
		return input.find_first_of(start_str) == 0;
	}
	inline bool StrEndsWith(const std::string &input, const std::string &end_str) {
		return input.length() != 0 && input.find_last_of(end_str) == input.length() - 1;
	}
	inline bool StrContains(const std::string &str, const std::string &contained_val) {
		return str.find(contained_val) != std::string::npos;
	}
	inline bool StrEqIgnoreCase(const std::string &str1, const std::string &str2) {
		return StrToUppercase(str1) == StrToUppercase(str2);
	}

}  // namespace cheat
