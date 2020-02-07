#include "stdafx.h"

#include "hide_windows_hook.h"

#include "native_util.h"
#include "timer.h"
#include "shared_mem.h"

namespace projx {

	static constexpr size_t kTargetProcsSz = 32, kTargetProcsElemSz = MAX_PATH;

	HWND __stdcall HideWndHk_CreateWindowExA(
		DWORD     dwExStyle,
		LPCSTR    lpClassName,
		LPCSTR    lpWindowName,
		DWORD     dwStyle,
		int       X,
		int       Y,
		int       nWidth,
		int       nHeight,
		HWND      hWndParent,
		HMENU     hMenu,
		HINSTANCE hInstance,
		LPVOID    lpParam
	) {
		sizeof(_STARTUPINFOA);
		if ((dwStyle & WS_VISIBLE) != 0) {
			std::unordered_set<std::string> target_procs;
			std::string proc_name;
			if (GetModuleFilename(NULL, &proc_name) == ErrorCodes::kSuccess) {
				proc_name = StrToUppercase(proc_name);
				if (HideWindowsHook::GetInstance().GetTargetProcs(&target_procs) == ErrorCodes::kSuccess && target_procs.find(proc_name) != target_procs.end()) {
					dwStyle &= ~WS_VISIBLE;
				}
			}
		}
		return HideWindowsHook::GetInstance().create_wnd_exa_hk.get_real_func()(dwExStyle, lpClassName, lpWindowName, dwStyle, X, Y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam);
	}
	HWND __stdcall HideWndHk_CreateWindowExW(
		DWORD     dwExStyle,
		LPCWSTR   lpClassName,
		LPCWSTR   lpWindowName,
		DWORD     dwStyle,
		int       X,
		int       Y,
		int       nWidth,
		int       nHeight,
		HWND      hWndParent,
		HMENU     hMenu,
		HINSTANCE hInstance,
		LPVOID    lpParam
	) {
		if ((dwStyle & WS_VISIBLE) != 0) {
			std::unordered_set<std::string> target_procs;
			std::string proc_name;
			if (GetModuleFilename(NULL, &proc_name) == ErrorCodes::kSuccess) {
				proc_name = StrToUppercase(proc_name);
				if (HideWindowsHook::GetInstance().GetTargetProcs(&target_procs) == ErrorCodes::kSuccess && target_procs.find(proc_name) != target_procs.end()) {
					dwStyle &= ~WS_VISIBLE;
				}
			}
		}
		return HideWindowsHook::GetInstance().create_wnd_exw_hk.get_real_func()(dwExStyle, lpClassName, lpWindowName, dwStyle, X, Y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam);
	}
	BOOL __stdcall HideWndHk_ShowWindow(
		HWND hWnd,
		int  nCmdShow
	) {
		std::unordered_set<std::string> target_procs;
		std::string proc_name;
		if (GetModuleFilename(NULL, &proc_name) == ErrorCodes::kSuccess) {
			proc_name = StrToUppercase(proc_name);
			DWORD proc_id;
			GetWindowThreadProcessId(hWnd, &proc_id);
			if (HideWindowsHook::GetInstance().GetTargetProcs(&target_procs) == ErrorCodes::kSuccess && target_procs.find(proc_name) != target_procs.end() &&
				proc_id == GetCurrentProcessId()) {
				if ((nCmdShow & SW_SHOW) != 0) {
					nCmdShow &= ~SW_SHOW;
				}
				if ((nCmdShow & SW_SHOWDEFAULT) != 0) {
					nCmdShow &= ~SW_SHOWDEFAULT;
				}
				if ((nCmdShow & SW_SHOWMAXIMIZED) != 0) {
					nCmdShow &= ~SW_SHOWMAXIMIZED;
				}
				if ((nCmdShow & SW_SHOWMINIMIZED) != 0) {
					nCmdShow &= ~SW_SHOWMINIMIZED;
				}
				if ((nCmdShow & SW_SHOWMINNOACTIVE) != 0) {
					nCmdShow &= ~SW_SHOWMINNOACTIVE;
				}
				if ((nCmdShow & SW_SHOWNA) != 0) {
					nCmdShow &= ~SW_SHOWNA;
				}
				if ((nCmdShow & SW_SHOWNOACTIVATE) != 0) {
					nCmdShow &= ~SW_SHOWNOACTIVATE;
				}
				if ((nCmdShow & SW_SHOWNORMAL) != 0) {
					nCmdShow &= ~SW_SHOWNORMAL;
				}
				if ((nCmdShow & SW_HIDE) == 0) {
					nCmdShow |= SW_HIDE;
				}
			}
		}
		return HideWindowsHook::GetInstance().show_wnd_hk.get_real_func()(hWnd, nCmdShow);
	}
	BOOL __stdcall HideWndHk_ShowWindowAsync(
		HWND hWnd,
		int  nCmdShow
	) {
		std::unordered_set<std::string> target_procs;
		std::string proc_name;
		if (GetModuleFilename(NULL, &proc_name) == ErrorCodes::kSuccess) {
			proc_name = StrToUppercase(proc_name);
			DWORD proc_id;
			GetWindowThreadProcessId(hWnd, &proc_id);
			if (HideWindowsHook::GetInstance().GetTargetProcs(&target_procs) == ErrorCodes::kSuccess && target_procs.find(proc_name) != target_procs.end() &&
				proc_id == GetCurrentProcessId()) {
				if ((nCmdShow & SW_SHOW) != 0) {
					nCmdShow &= ~SW_SHOW;
				}
				if ((nCmdShow & SW_SHOWDEFAULT) != 0) {
					nCmdShow &= ~SW_SHOWDEFAULT;
				}
				if ((nCmdShow & SW_SHOWMAXIMIZED) != 0) {
					nCmdShow &= ~SW_SHOWMAXIMIZED;
				}
				if ((nCmdShow & SW_SHOWMINIMIZED) != 0) {
					nCmdShow &= ~SW_SHOWMINIMIZED;
				}
				if ((nCmdShow & SW_SHOWMINNOACTIVE) != 0) {
					nCmdShow &= ~SW_SHOWMINNOACTIVE;
				}
				if ((nCmdShow & SW_SHOWNA) != 0) {
					nCmdShow &= ~SW_SHOWNA;
				}
				if ((nCmdShow & SW_SHOWNOACTIVATE) != 0) {
					nCmdShow &= ~SW_SHOWNOACTIVATE;
				}
				if ((nCmdShow & SW_SHOWNORMAL) != 0) {
					nCmdShow &= ~SW_SHOWNORMAL;
				}
				if ((nCmdShow & SW_HIDE) == 0) {
					nCmdShow |= SW_HIDE;
				}
			}
		}
		return HideWindowsHook::GetInstance().show_wnd_async_hk.get_real_func()(hWnd, nCmdShow);
	}
	BOOL __stdcall HideWndHk_SetWindowPos(
		HWND hWnd,
		HWND hWndInsertAfter,
		int  X,
		int  Y,
		int  cx,
		int  cy,
		UINT uFlags
	) {
		std::unordered_set<std::string> target_procs;
		std::string proc_name;
		if (GetModuleFilename(NULL, &proc_name) == ErrorCodes::kSuccess) {
			proc_name = StrToUppercase(proc_name);
			DWORD proc_id;
			GetWindowThreadProcessId(hWnd, &proc_id);
			if (HideWindowsHook::GetInstance().GetTargetProcs(&target_procs) == ErrorCodes::kSuccess && target_procs.find(proc_name) != target_procs.end() &&
				proc_id == GetCurrentProcessId()) {
				if ((uFlags & SWP_SHOWWINDOW) != 0) {
					uFlags &= ~SWP_SHOWWINDOW;
				}
				if ((uFlags & SWP_HIDEWINDOW) == 0) {
					uFlags |= SWP_HIDEWINDOW;
				}
			}
		}
		return HideWindowsHook::GetInstance().set_wnd_pos_hk.get_real_func()(hWnd, hWndInsertAfter, X, Y, cx, cy, uFlags);
	}
	LONG __stdcall HideWndHk_SetWindowLongA(
		HWND hWnd,
		int  nIndex,
		LONG dwNewLong
	) {
		std::unordered_set<std::string> target_procs;
		std::string proc_name;
		if (GetModuleFilename(NULL, &proc_name) == ErrorCodes::kSuccess) {
			proc_name = StrToUppercase(proc_name);
			DWORD proc_id;
			GetWindowThreadProcessId(hWnd, &proc_id);
			if (nIndex == GWL_STYLE &&
				HideWindowsHook::GetInstance().GetTargetProcs(&target_procs) == ErrorCodes::kSuccess && target_procs.find(proc_name) != target_procs.end() &&
				proc_id == GetCurrentProcessId()) {
				if ((dwNewLong & WS_VISIBLE) != 0) {
					dwNewLong &= ~WS_VISIBLE;
				}
			}
		}
		return HideWindowsHook::GetInstance().set_wnd_longa_hk.get_real_func()(hWnd, nIndex, dwNewLong);
	}
	LONG __stdcall HideWndHk_SetWindowLongW(
		HWND hWnd,
		int  nIndex,
		LONG dwNewLong
	) {
		std::unordered_set<std::string> target_procs;
		std::string proc_name;
		if (GetModuleFilename(NULL, &proc_name) == ErrorCodes::kSuccess) {
			proc_name = StrToUppercase(proc_name);
			DWORD proc_id;
			GetWindowThreadProcessId(hWnd, &proc_id);
			if (nIndex == GWL_STYLE &&
				HideWindowsHook::GetInstance().GetTargetProcs(&target_procs) == ErrorCodes::kSuccess && target_procs.find(proc_name) != target_procs.end() &&
				proc_id == GetCurrentProcessId()) {
				if ((dwNewLong & WS_VISIBLE) != 0) {
					dwNewLong &= ~WS_VISIBLE;
				}
			}
		}
		return HideWindowsHook::GetInstance().set_wnd_longw_hk.get_real_func()(hWnd, nIndex, dwNewLong);
	}
	BOOL __stdcall HideWndHk_AnimateWindow(
		HWND  hWnd,
		DWORD dwTime,
		DWORD dwFlags
	) {
		std::unordered_set<std::string> target_procs;
		std::string proc_name;
		if (GetModuleFilename(NULL, &proc_name) == ErrorCodes::kSuccess) {
			proc_name = StrToUppercase(proc_name);
			DWORD proc_id;
			GetWindowThreadProcessId(hWnd, &proc_id);
			if (HideWindowsHook::GetInstance().GetTargetProcs(&target_procs) == ErrorCodes::kSuccess && target_procs.find(proc_name) != target_procs.end() &&
				proc_id == GetCurrentProcessId()) {
				if ((dwFlags & AW_HIDE) == 0) {
					dwFlags |= AW_HIDE;
				}
			}
		}
		return HideWindowsHook::GetInstance().anim_wnd_hk.get_real_func()(hWnd, dwTime, dwFlags);
	}

	HideWindowsHook::HideWindowsHook() : create_wnd_exa_hk(CreateWindowExA, HideWndHk_CreateWindowExA), create_wnd_exw_hk(CreateWindowExW, HideWndHk_CreateWindowExW), show_wnd_hk(ShowWindow, HideWndHk_ShowWindow),
		show_wnd_async_hk(ShowWindowAsync, HideWndHk_ShowWindowAsync), set_wnd_pos_hk(SetWindowPos, HideWndHk_SetWindowPos), set_wnd_longa_hk(SetWindowLongA, HideWndHk_SetWindowLongA),
		set_wnd_longw_hk(SetWindowLongW, HideWndHk_SetWindowLongW), anim_wnd_hk(AnimateWindow, HideWndHk_AnimateWindow), target_procs("HideWindowsHook#target_procs", kTargetProcsSz * kTargetProcsElemSz), added_current_proc(false) {}
	HideWindowsHook::~HideWindowsHook() {
		EnsureRemovedCurrentProc();
		Detach();
	}
	HideWindowsHook &HideWindowsHook::GetInstance() {
		static HideWindowsHook instance;
		return instance;
	}
	ProgError HideWindowsHook::AddTargetProc(std::string proc_name) {
		if (proc_name.empty()) {
			return ErrorCodes::kInvalidParam;
		}
		proc_name = StrToUppercase(proc_name);
		char processes[kTargetProcsSz][kTargetProcsElemSz]{ '\0' };
		ProgError error = target_procs.Read(processes);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		for (size_t i = 0; i < kTargetProcsSz; ++i) {
			if (std::string(processes[i]).empty()) {
				strcpy_s(processes[i], proc_name.c_str());
				ProgError error = target_procs.Write(processes);
				if (error != ErrorCodes::kSuccess) {
					return error;
				}
				return ErrorCodes::kSuccess;
			}
		}
		return ErrorCodes::kOutOfMem;
	}
	ProgError HideWindowsHook::RemoveTargetProc(std::string proc_name) {
		if (proc_name.empty()) {
			return ErrorCodes::kInvalidParam;
		}
		proc_name = StrToUppercase(proc_name);
		char processes[kTargetProcsSz][kTargetProcsElemSz]{ '\0' };
		ProgError error = target_procs.Read(processes);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		for (size_t i = 0; i < kTargetProcsSz; ++i) {
			if (processes[i] == proc_name) {
				memset(processes[i], 0, sizeof(processes[i]));
				ProgError error = target_procs.Write(processes);
				if (error != ErrorCodes::kSuccess) {
					return error;
				}
				return ErrorCodes::kSuccess;
			}
		}
		return ErrorCodes::kFileNotFound;
	}
	ProgError HideWindowsHook::GetTargetProcs(std::unordered_set<std::string> *targets_out) const {
		if (targets_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		char processes[kTargetProcsSz][kTargetProcsElemSz]{ '\0' };
		ProgError error = target_procs.Read(processes);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		for (size_t i = 0; i < kTargetProcsSz; ++i) {
			targets_out->insert(processes[i]);
		}
		return ErrorCodes::kSuccess;
	}
	ProgError HideWindowsHook::Attach() {
		ProgError error = create_wnd_exa_hk.Attach();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		create_wnd_exw_hk.Attach();
		show_wnd_hk.Attach();
		show_wnd_async_hk.Attach();
		set_wnd_pos_hk.Attach();
		set_wnd_longa_hk.Attach();
		set_wnd_longw_hk.Attach();
		anim_wnd_hk.Attach();
		return ErrorCodes::kSuccess;
	}
	ProgError HideWindowsHook::EnsureRemovedCurrentProc() {
		if (added_current_proc) {
			std::string current_proc;
			ProgError error = GetModuleFilename(NULL, &current_proc);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			error = RemoveTargetProc(current_proc);
			if (error != ErrorCodes::kSuccess && error != ErrorCodes::kFileNotFound) {
				return error;
			}
			added_current_proc = false;
		}
		return ErrorCodes::kSuccess;
	}
	ProgError HideWindowsHook::AddCurrentProc() {
		if (added_current_proc) {
			return ErrorCodes::kInvalidState;
		}
		std::string current_proc;
		ProgError error = GetModuleFilename(NULL, &current_proc);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		error = AddTargetProc(current_proc);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		added_current_proc = true;
		return ErrorCodes::kSuccess;
	}
	ProgError HideWindowsHook::Detach() {
		ProgError error = create_wnd_exa_hk.Detach();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		create_wnd_exw_hk.Detach();
		show_wnd_hk.Detach();
		show_wnd_async_hk.Detach();
		set_wnd_pos_hk.Detach();
		set_wnd_longa_hk.Detach();
		set_wnd_longw_hk.Detach();
		anim_wnd_hk.Detach();
		return ErrorCodes::kSuccess;
	}

}  // namespace projx
