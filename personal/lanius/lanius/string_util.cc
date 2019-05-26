/*
Copyright 2018 Eric McDonald

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

#include "stdafx.h"  // NOLINT

#include "string_util.h"  // NOLINT

namespace lanius {

	std::string ToUppercase(const std::string &str) {
		std::string uppercase_str(str);
		for (size_t i = 0; i < uppercase_str.length(); ++i) {
			uppercase_str[i] = toupper(uppercase_str[i]);
		}
		return uppercase_str;
	}
	std::string ReplaceChar(const std::string &str, char old_ch, char new_ch) {
		std::string new_str(str);
		for (size_t i = 0; i < new_str.length(); ++i) {
			if (new_str[i] == old_ch) {
				new_str[i] = new_ch;
			}
		}
		return new_str;
	}
	std::string RemoveChar(const std::string &str, char ch) {
		std::string new_str(str);
		for (std::string::iterator it = new_str.begin(); it != new_str.end();) {
			if (*it == ch) {
				it = new_str.erase(it);
			}
			else {
				++it;
			}
		}
		return new_str;
	}

}  // namespace lanius
