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
#include <map>
#include <locale>
#include <fstream>

#include "lanius.h"  // NOLINT

namespace lanius {

	using TranslationMap = std::map<const std::string, const std::string>;
	class Language {
		std::string filepath_;
		std::locale loc_;
		TranslationMap translations_;
	public:
		Language(const std::string &, const std::locale &);
		Language();
		ErrorCode Load();
		const std::string &filepath() const;
		const TranslationMap &translations() const;
		bool operator==(const Language &) const;
		bool operator!=(const Language &) const;
		inline bool FileExists() const {
			std::ifstream file_in(filepath());
			bool exists = file_in.is_open();
			file_in.close();
			return exists;
		}
		const std::locale &loc() const;
		ErrorCode Get(std::string &, const std::string &/*key*/, int /*fmt_args_sz*/ = 0, ...) const;
	};

	ErrorCode SetGlobalLocale(const std::locale &);

}  // namespace lanius
