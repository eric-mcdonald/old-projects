#pragma once

#include <string>

#include "px_def.h"
#include "projectx-shared.h"

namespace projx {

	PROJECTXSHARED_API std::string StrReplaceAll(std::string /*input*/, const std::string &/*old_value*/, const std::string &/*new_value*/);
	PROJECTXSHARED_API ProgError StrGenRandom(const std::string &/*valid_chars*/, size_t /*min_len*/, size_t /*max_len*/, std::string * /*output*/);
	PROJECTXSHARED_API std::string StrToLowercase(std::string);
	PROJECTXSHARED_API std::string StrToUppercase(std::string);
	PROJECTXSHARED_API inline bool StrEqIgnoreCase(const std::string &str1, const std::string &str2) {
		return StrToUppercase(str1) == StrToUppercase(str2);
	}
	PROJECTXSHARED_API ProgError StrSplt(std::string /*input*/, const std::string &/*delimiter*/, std::vector<std::string> * /*output*/);

}  // namespace projx
