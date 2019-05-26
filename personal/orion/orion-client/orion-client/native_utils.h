#pragma once

#include <vector>
#include <Windows.h>
#include <Lmcons.h>
#include <atlconv.h>

#include "orion_def.h"

namespace orion {

	typedef unsigned char Byte;

	static constexpr DWORD_PTR kNull = 0;

	ErrorCode CloseNativeHandle(HANDLE handle);
	inline bool IsKeyDown(int key_code) {
		SHORT key_state = GetAsyncKeyState(key_code);
		return key_state && key_state & 0x8000;
	}
	inline bool IsKeyPressed(int key_code) {
		SHORT key_state = GetAsyncKeyState(key_code);
		return key_state && key_state & 0x00001;
	}
	inline RECT GetWinRectAdjusted(HWND window) {
		RECT win_pos = { 0 };
		GetWindowRect(window, &win_pos);
		if (GetWindowLong(window, GWL_STYLE) & WS_BORDER) {
			win_pos.left += 6;
			win_pos.top += 28;
		}
		return win_pos;
	}
	ErrorCode SendMouseIn(MOUSEINPUT *);
	ErrorCode SendKeyboardIn(KEYBDINPUT *);
	ErrorCode GetModuleFilename(HMODULE, std::string *);
	ErrorCode GetModuleDirectory(HMODULE, std::string *);
	inline RECT GetResolution() {
		RECT resolution{ 0 };
		resolution.right = GetSystemMetrics(SM_CXSCREEN);
		resolution.bottom = GetSystemMetrics(SM_CYSCREEN);
		return resolution;
	}
	inline std::string GetWinUser() {
		TCHAR username[UNLEN + 1]{ 0 };
		DWORD user_len = sizeof(username);
		GetUserName(username, &user_len);
		USES_CONVERSION;
		return T2CA(username);
	}

}  // namespace orion
