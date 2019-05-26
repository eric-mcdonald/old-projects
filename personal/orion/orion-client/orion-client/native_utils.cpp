#include "stdafx.h"
#include "native_utils.h"

#include <Windows.h>
#include <atlconv.h>
#include <dbghelp.h>

namespace orion {

	ErrorCode CloseNativeHandle(HANDLE handle) {
		if (handle == INVALID_HANDLE_VALUE) {
			return ErrorCodes::kInvalidParam;
		}
		return CloseHandle(handle) ? ErrorCodes::kSuccess : GetLastError();
	}
	ErrorCode SendMouseIn(MOUSEINPUT *mouse_in) {
		if (mouse_in == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		mouse_in->dwExtraInfo = GetMessageExtraInfo();
		INPUT in;
		in.type = INPUT_MOUSE;
		in.mi = *mouse_in;
		return !SendInput(1, &in, sizeof(INPUT)) ? GetLastError() : ErrorCodes::kSuccess;
	}
	ErrorCode SendKeyboardIn(KEYBDINPUT *keyboard_in) {
		if (keyboard_in == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		keyboard_in->dwExtraInfo = GetMessageExtraInfo();
		INPUT in;
		in.type = INPUT_KEYBOARD;
		in.ki = *keyboard_in;
		return !SendInput(1, &in, sizeof(INPUT)) ? GetLastError() : ErrorCodes::kSuccess;
	}
	ErrorCode GetModuleFilename(HMODULE module, std::string *filename_out) {
		if (filename_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		TCHAR filename[MAX_PATH];
		if (!GetModuleFileName(module, filename, sizeof(filename) / sizeof(TCHAR))) {
			return GetLastError();
		}
		USES_CONVERSION;
		std::string filename_str = T2CA(filename);
		size_t separator_idx = filename_str.find_last_of("\\");
		if (separator_idx != std::string::npos && separator_idx != filename_str.length() - 1) {
			filename_str = filename_str.substr(separator_idx + 1);
		}
		*filename_out = filename_str;
		return ErrorCodes::kSuccess;
	}
	ErrorCode GetModuleDirectory(HMODULE module, std::string *dir_out) {
		if (dir_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		TCHAR directory[MAX_PATH];
		if (!GetModuleFileName(module, directory, sizeof(directory) / sizeof(TCHAR))) {
			return GetLastError();
		}
		USES_CONVERSION;
		std::string directory_str = T2CA(directory);
		size_t separator_idx = directory_str.find_last_of("\\");
		if (separator_idx != std::string::npos && separator_idx != directory_str.length() - 1) {
			directory_str = directory_str.substr(0, separator_idx - 0);
		}
		*dir_out = directory_str;
		return ErrorCodes::kSuccess;
	}

}  // namespace orion
