#include "stdafx.h"

#include "cheat.h"

namespace internals {

	cheat::ErrorCode CloseNativeHandle(HANDLE handle) {
		if (handle == INVALID_HANDLE_VALUE) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		return CloseHandle(handle) ? cheat::CheatErrorCodes::kSuccess : GetLastError();
	}
	cheat::ErrorCode SendMouseIn(MOUSEINPUT *mouse_in) {
		if (mouse_in == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		mouse_in->dwExtraInfo = GetMessageExtraInfo();
		INPUT in;
		in.type = INPUT_MOUSE;
		in.mi = *mouse_in;
		return !SendInput(1, &in, sizeof(INPUT)) ? GetLastError() : cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode SendKeyboardIn(KEYBDINPUT *keyboard_in) {
		if (keyboard_in == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		keyboard_in->dwExtraInfo = GetMessageExtraInfo();
		INPUT in;
		in.type = INPUT_KEYBOARD;
		in.ki = *keyboard_in;
		return !SendInput(1, &in, sizeof(INPUT)) ? GetLastError() : cheat::CheatErrorCodes::kSuccess;
	}
	void FixedSleep(DWORD miliseconds) {
		if (cheat::Cheat::GetInstance().get_speed() == 1.0F) {
			Sleep(miliseconds);
			return;
		}
		cheat::Timer delay;
		delay.set_start_time();
		while (!delay.HasDelayPassed(miliseconds)) {
			Sleep(miliseconds);
		}
	}
	cheat::ErrorCode GetNtHeadersFromFile(const std::string &file, IMAGE_NT_HEADERS *headers_out) {
		if (headers_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		USES_CONVERSION;
		HANDLE mod_file = CreateFile(A2CT(file.c_str()), GENERIC_READ, FILE_SHARE_READ, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
		if (mod_file == INVALID_HANDLE_VALUE) {
			return GetLastError();
		}
		HANDLE mod_mapping = CreateFileMapping(mod_file, NULL, PAGE_READONLY, 0, 0, NULL);
		if (!mod_mapping) {
			CloseNativeHandle(mod_file);
			return GetLastError();
		}
		LPVOID mod_base = MapViewOfFile(mod_mapping, FILE_MAP_READ, 0, 0, 0);
		if (!mod_base) {
			CloseNativeHandle(mod_mapping);
			CloseNativeHandle(mod_file);
			return GetLastError();
		}
		PIMAGE_NT_HEADERS nt_headers = ImageNtHeader(mod_base);
		if (!nt_headers) {
			UnmapViewOfFile(mod_base);
			CloseNativeHandle(mod_mapping);
			CloseNativeHandle(mod_file);
			return GetLastError();
		}
		*headers_out = *nt_headers;
		UnmapViewOfFile(mod_base);
		CloseNativeHandle(mod_mapping);
		CloseNativeHandle(mod_file);
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode GetModuleFilename(HMODULE module, std::string *filename_out) {
		if (filename_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
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
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode GetModuleDirectory(HMODULE module, std::string *dir_out) {
		if (dir_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
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
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode GetFuncSize(void *func_ptr, HMODULE module, size_t *bytes_sz_out, bool break_mod_end) {
		if (func_ptr == nullptr || bytes_sz_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		size_t func_length = 0;
		void *current_addr = func_ptr;
		MODULEINFO mod_info = { 0 };
		if (break_mod_end) {
			if (!GetModuleInformation(GetCurrentProcess(), module, &mod_info, sizeof(MODULEINFO))) {
				return GetLastError();
			}
		}
		do {
			DWORD old_protect;
			if (!VirtualProtect(current_addr, 3 * sizeof(BYTE), PAGE_EXECUTE_READWRITE, &old_protect)) {
				continue;
			}
			BYTE delim_bytes[3];
			if (!ReadProcessMemory(GetCurrentProcess(), current_addr, delim_bytes, sizeof(delim_bytes), NULL)) {
				DWORD dw;
				VirtualProtect(current_addr, 3 * sizeof(BYTE), old_protect, &dw);
				continue;
			}
			bool delim = true;
			for (size_t i = 0; i < sizeof(delim_bytes); ++i) {
				if (delim_bytes[i] != 0xCC) {
					delim = false;
					break;
				}
			}
			if (delim) {
				*bytes_sz_out = func_length;
				break;
			}
			DWORD dw;
			VirtualProtect(current_addr, 3 * sizeof(BYTE), old_protect, &dw);
			current_addr = reinterpret_cast<void*>(reinterpret_cast<DWORD_PTR>(current_addr) + 1);
			++func_length;
		} while (!break_mod_end || reinterpret_cast<DWORD_PTR>(current_addr) < reinterpret_cast<DWORD_PTR>(mod_info.lpBaseOfDll) + mod_info.SizeOfImage);
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode GetModulesInDir(const std::string &directory, std::vector<HMODULE> *modules) {
		if (modules == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		HANDLE snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE, 0);
		if (snapshot == INVALID_HANDLE_VALUE) {
			return GetLastError();
		}
		MODULEENTRY32 mod_entry;
		mod_entry.dwSize = sizeof(MODULEENTRY32);
		if (!Module32First(snapshot, &mod_entry)) {
			return GetLastError();
		}
		do {
			std::string mod_dir;
			if (GetModuleDirectory(mod_entry.hModule, &mod_dir) != cheat::CheatErrorCodes::kSuccess) {
				continue;
			}
			if (cheat::StrEqIgnoreCase(mod_dir, directory)) {
				modules->push_back(mod_entry.hModule);
			}
		} while (Module32Next(snapshot, &mod_entry));
		CloseNativeHandle(snapshot);
		return cheat::CheatErrorCodes::kSuccess;
	}

}  // namespace internals
