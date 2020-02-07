#pragma once

#include <string>

#include "px_def.h"
#include "string_util.h"
#include "projectx-shared.h"

namespace projx {

	PROJECTXSHARED_API ProgError GetDisplayName(std::string *);
	PROJECTXSHARED_API ProgError IsProcElevated(bool * /*elevated_out*/, HANDLE = GetCurrentProcess());
	PROJECTXSHARED_API ProgError GetModulePath(HMODULE, std::string *);
	PROJECTXSHARED_API ProgError GetModuleFilename(HMODULE, std::string *);
	PROJECTXSHARED_API ProgError GetModuleDirectory(HMODULE, std::string *);
	PROJECTXSHARED_API ProgError GetCurrentUserDir(std::string *);
	PROJECTXSHARED_API inline void HideConsole() {
		HWND console_wnd = GetConsoleWindow();
		if (console_wnd != NULL) {
			ShowWindow(console_wnd, SW_HIDE);
		}
	}
	PROJECTXSHARED_API ProgError GetProcExeById(DWORD, std::string *);
	PROJECTXSHARED_API inline ProgError SetProgPriority(DWORD new_priority, DWORD *old_priority = nullptr, HANDLE process = GetCurrentProcess()) {
		if (old_priority != nullptr) {
			*old_priority = GetPriorityClass(process);
			if (*old_priority == 0) {
				return GetLastError();
			}
		}
		return !SetPriorityClass(process, new_priority) ? GetLastError() : ErrorCodes::kSuccess;
	}
	PROJECTXSHARED_API ProgError IsProc64Bit(bool *, HANDLE = GetCurrentProcess());
	PROJECTXSHARED_API ProgError GetFuncSize(void * /*func_ptr*/, HMODULE, size_t * /*bytes_sz_out*/, bool /*break_mod_end*/ = false);
	PROJECTXSHARED_API ProgError GetProcPathById(DWORD, std::string *);

}  // namespace projx
