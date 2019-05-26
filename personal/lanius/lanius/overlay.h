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
#include <vector>
#include <map>

#include <d3d9.h>
#include <d3dx9.h>

#include "native_util.h"  // NOLINT
#include "font.h"  // NOLINT
#include "window_rect.h"  // NOLINT
#include "render_data.h"  // NOLINT

namespace lanius {

	using Color = unsigned int;
	class Overlay {
		std::string overlay_title_;
		WindowId target_id_;
		WindowRect window_pos_;
		HWND window_handle_;
		bool stop_requested_;
		bool frame_started_;
		RenderData render_data_;
		NativeHandle update_thread_;
	public:
		Overlay(const std::string &, const WindowId &, unsigned int /*multi_sample_quality*/ = 0);
		Overlay(const Overlay &);
		Overlay();
		~Overlay();
		const std::string &overlay_title() const;
		const WindowId &target_id() const;
		const WindowRect &window_pos() const;
		void set_window_pos();
		ErrorCode DrawRect(float /*x*/, float /*y*/, float /*width*/, float /*height*/, float /*line_width*/, Color, bool /*antialias*/ = false);
		ErrorCode DrawFilledRect(float /*x*/, float /*y*/, float /*width*/, float /*height*/, Color);
		ErrorCode DrawLine(float /*x1*/, float /*y1*/, float /*x2*/, float /*y2*/, float /*width*/, Color, bool /*antialias*/ = false);
		ErrorCode DrawTexture(const std::string &, float /*x*/, float /*y*/, float /*width*/, float /*height*/);
		ErrorCode DrawStr(const Font &, const std::string &, int /*x*/, int /*y*/, Color);
		ErrorCode GetTextSz(const Font &, const std::string &, int &/*width*/, int &/*height*/) const;
		bool operator==(const Overlay &) const;
		bool operator!=(const Overlay &) const;
		bool IsRunning() const;
		HWND window_handle() const;
		void set_stop_requested();
		RenderData &render_data();  // Cannot make this function constant.
		ErrorCode StartFrame();
		ErrorCode EndFrame();
		bool frame_started() const;
		void UpdateWindow();
		bool IsActive() const;
	};

}  // namespace lanius
