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

#include <Windows.h>

namespace lanius {

	class WindowRect {
		int x_, y_;
		int width_, height_;
	public:
		WindowRect(int /*x1*/, int /*y1*/, int /*x2*/, int /*y2*/);
		explicit WindowRect(const RECT &);
		WindowRect();
		int x() const;
		int y() const;
		int width() const;
		int height() const;
		bool operator==(const WindowRect &) const;
		bool operator!=(const WindowRect &) const;
	};

	bool operator==(const RECT &/*native_rect*/, const WindowRect &/*window_rect*/);
	bool operator!=(const RECT &/*native_rect*/, const WindowRect &/*window_rect*/);

}  // namespace lanius
