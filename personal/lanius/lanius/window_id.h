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

#include <Windows.h>

namespace lanius {

	enum WindowIdTypes {
		kInvalid,
		kClass,
		kTitle,
		kPrefix,
	};
	class WindowId {
		std::string identifier_;
		WindowIdTypes type_;
	public:
		WindowId(const std::string &, WindowIdTypes);
		WindowId();
		const std::string &identifier() const;
		WindowIdTypes type() const;
		HWND FindHandle() const;  // Cannot rename this to "FindWindow" because of a Windows macro.
		bool operator==(const WindowId &) const;
		bool operator!=(const WindowId &) const;
	};

}  // namespace lanius
