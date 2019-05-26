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

#include <fstream>

#include "language.h"  // NOLINT

namespace lanius {

	Language::Language(const std::string &filepath, const std::locale &loc) : filepath_(ReplaceChar(filepath, '\\', '/')), loc_(loc) {}
	Language::Language() : filepath_(""), loc_() {}
	ErrorCode Language::Load() {
		if (!FileExists()) {
			return ErrorCodes::kFileNotFound;
		}
		// TODO(Eric McDonald): Continue implementing this.
		return ErrorCodes::kSuccess;
	}
	const std::string &Language::filepath() const {
		return filepath_;
	}
	const TranslationMap &Language::translations() const {
		return translations_;
	}
	bool Language::operator==(const Language &operand) const {
		return filepath_ == operand.filepath_ && loc_ == operand.loc_;
	}
	bool Language::operator!=(const Language &operand) const {
		return !(*this == operand);
	}
	const std::locale &Language::loc() const {
		return loc_;
	}
	ErrorCode Language::Get(std::string &str, const std::string &key, int fmt_args_sz, ...) const {
		// TODO(Eric McDonald): Continue implementing this.
		str = key;
		return ErrorCodes::kSuccess;
	}

	// TODO(Eric McDonald): Continue implementing this.

}  // namespace lanius
