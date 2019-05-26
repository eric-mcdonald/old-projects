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

#include "window_rect.h"  // NOLINT

namespace lanius {

	WindowRect::WindowRect(int x1, int y1, int x2, int y2) : x_(x1), y_(y1), width_(x2 - x1), height_(y2 - y1) {}
	WindowRect::WindowRect(const RECT &native_rect) : WindowRect(native_rect.left, native_rect.top, native_rect.right, native_rect.bottom) {}
	WindowRect::WindowRect() : x_(0), y_(0), width_(0), height_(0) {}
	int WindowRect::x() const {
		return x_;
	}
	int WindowRect::y() const {
		return y_;
	}
	int WindowRect::width() const {
		return width_;
	}
	int WindowRect::height() const {
		return height_;
	}
	bool WindowRect::operator==(const WindowRect &operand) const {
		return x_ == operand.x_ && y_ == operand.y_ && width_ == operand.width_ && height_ == operand.height_;
	}
	bool WindowRect::operator!=(const WindowRect &operand) const {
		return !(*this == operand);
	}
	bool operator==(const RECT &native_rect, const WindowRect &window_rect) {
		return WindowRect(native_rect) == window_rect;
	}
	bool operator!=(const RECT &native_rect, const WindowRect &window_rect) {
		return !(native_rect == window_rect);
	}

}  // namespace lanius
