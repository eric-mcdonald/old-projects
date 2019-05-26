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

#include "overlay_test.h"  // NOLINT

#include "overlay.h"  // NOLINT

namespace lanius {

	namespace test {

		ErrorCode DoOverlayTest(const WindowId &window_id) {
			Overlay renderer(kCheatName + " Test Overlay", window_id);
			Font font("Times New Roman", 36, 0, false, ANTIALIASED_QUALITY, renderer.render_data().d3d_device());
			float x = 0, y = 0;
			float width = 100, height = 100;
			ErrorCode error = ErrorCodes::kSuccess;
			while (renderer.IsRunning()) {
				error = renderer.StartFrame();
				if (error != ErrorCodes::kSuccess) {
					return error;
				}
				if (renderer.IsActive()) {
					error = renderer.DrawTexture("D:\\test_texture.png", x, y, width, height);
					if (error != ErrorCodes::kSuccess) {
						return error;
					}
					error = renderer.DrawStr(font, "Test string.", 256, 256, 0x8000FFFF);
					if (error != ErrorCodes::kSuccess) {
						return error;
					}
				}
				error = renderer.EndFrame();
				if (error != ErrorCodes::kSuccess) {
					return error;
				}
				renderer.UpdateWindow();
				x += 1;
				y += 1;
				if (x >= renderer.window_pos().x() + renderer.window_pos().width() - width) {
					x = 0;
				}
				if (y >= renderer.window_pos().y() + renderer.window_pos().height() - height) {
					y = 0;
				}
				Sleep(1);
			}
			return error;
		}

	}  // namespace test

}  // namespace lanius
