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

#include "window_id.h"  // NOLINT

namespace lanius {

	WindowId::WindowId(const std::string &identifier, WindowIdTypes type) : identifier_(identifier), type_(type) {}
	WindowId::WindowId() : identifier_(""), type_(WindowIdTypes::kInvalid) {}
	const std::string &WindowId::identifier() const {
		return identifier_;
	}
	WindowIdTypes WindowId::type() const {
		return type_;
	}
	HWND WindowId::FindHandle() const {
		USES_CONVERSION;
		const TCHAR *identifier_c = A2CT(identifier_.c_str());
		WindowsCmpPrefixRequest cmp_prefix_req;
		HWND found_window = NULL;
		switch (type_) {
		case WindowIdTypes::kClass:
			return FindWindow(identifier_c, NULL);
		case WindowIdTypes::kTitle:
			return FindWindow(NULL, identifier_c);
		case WindowIdTypes::kPrefix:
			if (FindWindowByPrefix(identifier_, cmp_prefix_req) == ErrorCodes::kSuccess) {
				return cmp_prefix_req.found_handle;
			}
		}
		return NULL;
	}
	bool WindowId::operator==(const WindowId &operand) const {
		return identifier_ == operand.identifier_ && type_ == operand.type_;
	}
	bool WindowId::operator!=(const WindowId &operand) const {
		return !(*this == operand);
	}

}  // namespace lanius
