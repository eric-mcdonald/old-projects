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

#pragma once

#include <string>

namespace lanius {

	std::string ToUppercase(const std::string &);
	inline size_t FindIgnoreCase(const std::string &str, const std::string &to_find) {
		return ToUppercase(str).find(ToUppercase(to_find));
	}
	inline bool EqualsIgnoreCase(const std::string &str1, const std::string &str2) {
		return ToUppercase(str1) == ToUppercase(str2);
	}
	std::string ReplaceChar(const std::string &, char /*old_ch*/, char /*new_ch*/);
	std::string RemoveChar(const std::string &, char);

}  // namespace lanius
