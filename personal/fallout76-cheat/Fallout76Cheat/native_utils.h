#pragma once

#include <vector>

#include "cheat_def.h"
#include "signatures.h"

namespace internals {

	typedef unsigned char Byte;

	extern HMODULE g_this_dll;
	static constexpr DWORD_PTR kNull = 0;

	cheat::ErrorCode CloseNativeHandle(HANDLE handle);
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
	cheat::ErrorCode SendMouseIn(MOUSEINPUT *);
	cheat::ErrorCode SendKeyboardIn(KEYBDINPUT *);
	void FixedSleep(DWORD);
	cheat::ErrorCode GetNtHeadersFromFile(const std::string &, IMAGE_NT_HEADERS *);
	cheat::ErrorCode GetModuleFilename(HMODULE, std::string *);
	cheat::ErrorCode GetModuleDirectory(HMODULE, std::string *);
	cheat::ErrorCode GetFuncSize(void *, HMODULE, size_t *, bool /*break_mod_end*/ = false);
	cheat::ErrorCode GetModulesInDir(const std::string &, std::vector<HMODULE> *);
	template<class Function>
	inline Function GetVirtualFunc(void *this_ptr, size_t func_idx) {
		if (this_ptr == nullptr) {
			return nullptr;
		}
		return reinterpret_cast<Function>((*reinterpret_cast<void***>(this_ptr))[func_idx]);
	}

}  // namespace internals
