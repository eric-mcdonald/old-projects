#include "stdafx.h"

#include "routine_impl.h"

#include <vector>
#include <lodepng.h>
#include <Windows.h>

#include "native_utils.h"

namespace orion {

	JumpScareRoutine::JumpScareRoutine(std::string name_, bool enabled_) : Routine(name_, enabled_), last_time(0), width(0), height(0), bitmap(NULL), black_screen(false) {
		std::string path;
		ErrorCode error = GetModuleDirectory(NULL, &path);
		if (error == ErrorCodes::kSuccess) {
			/*lodepng::decode(img_data, width, height, path + "\\momo.png");
			for (size_t i = 0; i < img_data.size() - 4; i += 4) {
				Byte r, g, b, a;
				r = img_data[i + 2];
				g = img_data[i + 1];
				b = img_data[i];
				a = img_data[i + 3];
				img_data[i] = r;
				img_data[i + 1] = g;
				img_data[i + 2] = b;
				img_data[i + 3] = a;
			}*/
			BITMAPINFO bmi;
			memset(&bmi, 0, sizeof(bmi));
			bmi.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
			bmi.bmiHeader.biWidth = width;
			bmi.bmiHeader.biHeight = height;
			bmi.bmiHeader.biPlanes = 1;
			bmi.bmiHeader.biCompression = BI_RGB;
			bmi.bmiHeader.biBitCount = 24;
			USES_CONVERSION;
			bitmap = reinterpret_cast<HBITMAP>(LoadImage(NULL, (path + "\\momo.bmp").c_str(), IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE));
		}
	}
	JumpScareRoutine::~JumpScareRoutine() {
		if (bitmap != NULL) {
			DeleteObject(bitmap);
			bitmap = NULL;
		}
	}
	ErrorCode JumpScareRoutine::Update() {
		if (last_time == 0) {
			last_time = GetTickCount64();
		}
		RECT resolution = GetResolution();
		if (black_screen) {
			HDC device_ctx = GetDC(NULL);
			HBRUSH black = CreateSolidBrush(RGB(0, 0, 0));
			FillRect(device_ctx, &resolution, black);
			DeleteObject(black);
			ReleaseDC(NULL, device_ctx);
		}
		else {
			HDC device_ctx = GetDC(NULL);
			HBRUSH bitmap_tex = CreatePatternBrush(bitmap);
			FillRect(device_ctx, &resolution, bitmap_tex);
			DeleteObject(bitmap_tex);
			ReleaseDC(NULL, device_ctx);
		}
		if (GetTickCount64() - last_time >= 250) {
			black_screen = !black_screen;
			last_time = GetTickCount64();

		}
		return ErrorCodes::kSuccess;
	}
	void JumpScareRoutine::SetEnabled(bool enabled_) {
		if (is_enabled() != enabled_) {
			Routine::SetEnabled(enabled_);
			if (!enabled_) {
				PlaySound(NULL, NULL, SND_LOOP | SND_ASYNC);
				RECT resolution = GetResolution();
				InvalidateRect(NULL, &resolution, TRUE);
				last_time = 0;
				black_screen = false;
			}
			else {
				std::string path;
				ErrorCode error = GetModuleDirectory(NULL, &path);
				if (error == ErrorCodes::kSuccess) {
					PlaySound((path + "\\scary.wav").c_str(), NULL, SND_LOOP | SND_ASYNC | SND_FILENAME);
				}
			}
		}
	}
	RandMouseMoveRoutine::RandMouseMoveRoutine(std::string name_, bool enabled_) : Routine(name_, enabled_), last_time(0) {}
	ErrorCode RandMouseMoveRoutine::Update() {
		if (last_time == 0) {
			last_time = GetTickCount64();
		}
		if (GetTickCount64() - last_time >= 250) {
			MOUSEINPUT mouse_in{ 0 };
			int delta_x = rand() % 11;
			mouse_in.dx = rand() % 2 ? -delta_x : delta_x;
			int delta_y = rand() % 11;
			mouse_in.dy = rand() % 2 ? -delta_y : delta_y;
			mouse_in.dwFlags = MOUSEEVENTF_MOVE;
			SendMouseIn(&mouse_in);
		}
		return ErrorCodes::kSuccess;
	}
	void RandMouseMoveRoutine::SetEnabled(bool enabled_) {
		Routine::SetEnabled(enabled_);
		if (!enabled_) {
			last_time = 0;
		}
	}

}  // namespace orion
