#include "stdafx.h"

#include <TlHelp32.h>
#include <native_util.h>
#include <px_file.h>

#include "web_browser_info.h"
#include "app_volume_ctrl.h"
#include "screenshot_helper.h"

#include "main_prog.h"

namespace projx {

	struct WebBrowserInfo_HideWindows_Params {
		WebBrowserInfo *browser;
		TimeDiff exec_time;
		HWND topmost_wnd;
		Timer set_foreground_timer;
		ScreenshotHelper screenshot_hlpr;
		static constexpr TimeDiff kSetForegroundTime = 2500;
	};
	struct WebBrowserInfo_AddHookByway_Params {
		bool added_dll;
		std::string hook_dll;
	};

	static constexpr char *kHookedDll = "dwmapi.dll";
	static constexpr char *kBywayDlls[]{ "zlib1.dll", "libcurl.dll", "web_browser_hook.dll" };  // These DLLs need to be in order according to their dependencies.

	BOOL CALLBACK WebBrowserInfo_AddHookByway(_In_opt_ PVOID pContext,
		_In_opt_ LPCSTR pszFile,
		_Outptr_result_maybenull_ LPCSTR *ppszOutFile) {
		if (pContext != NULL) {
			WebBrowserInfo_AddHookByway_Params *hook_ctx = (WebBrowserInfo_AddHookByway_Params*)pContext;
			if (!pszFile && !hook_ctx->added_dll) {
				hook_ctx->added_dll = true;
				if (ppszOutFile) {
					*ppszOutFile = hook_ctx->hook_dll.c_str();
				}
			}
		}
		return TRUE;
	}
	BOOL CALLBACK WebBrowserInfo_HideWindow(_In_ HWND hwnd, _In_ LPARAM lParam) {
		WebBrowserInfo_HideWindows_Params *params = (WebBrowserInfo_HideWindows_Params*)lParam;
		DWORD proc_id = -1;
		GetWindowThreadProcessId(hwnd, &proc_id);
		if (proc_id == -1) {
			return TRUE;
		}
		std::string proc_path;
		if (GetProcPathById(proc_id, &proc_path) == ErrorCodes::kSuccess) {
			std::string proc_name;
			FileSplitDir(proc_path, &proc_name);
			if (StrEqIgnoreCase(proc_name, params->browser->get_exe_name())) {
				HWND wnd = hwnd, parent_wnd = hwnd;
				while (wnd != NULL) {
					parent_wnd = wnd;
					wnd = GetWindow(wnd, GW_OWNER);
				}
				ShowWindow(parent_wnd, SW_HIDE);
				AppVolumeControl(proc_name).SetMuted(TRUE);
				if (params->set_foreground_timer.get_start_time() == 0) {
					params->set_foreground_timer.set_start_time();
				}
				if (!params->set_foreground_timer.HasTimePassed(WebBrowserInfo_HideWindows_Params::kSetForegroundTime)) {
					SetForegroundWindow(params->topmost_wnd);
				}
				else {
					params->screenshot_hlpr.StopDisplayingImage();
				}
			}
		}
		return TRUE;
	}
	ProgError WebBrowserInfo_HideProcWindows(WebBrowserInfo_HideWindows_Params *params) {
		if (params == nullptr) {
			ResetPrevProcPriority();
			return ErrorCodes::kInvalidParam;
		}
		Timer hide_timer;
		hide_timer.set_start_time();
		while (!hide_timer.HasTimePassed(params->exec_time)) {
			if (!EnumWindows(WebBrowserInfo_HideWindow, (LPARAM)params)) {
				params->browser->set_hide_wnds_thread();
				ResetPrevProcPriority();
				delete params;
				return GetLastError();
			}
		}
		params->browser->set_hide_wnds_thread();
		ResetPrevProcPriority();
		delete params;
		return ErrorCodes::kSuccess;
	}

	WebBrowserInfo::WebBrowserInfo() : main_dir(""), exe_name(""), launch_cmd_prefix(""), hide_wnds_thread(NULL), prev_priority_class(0) {}
	WebBrowserInfo::WebBrowserInfo(const File &exe_path, const std::string &launch_cmd_prefix_) : WebBrowserInfo() {
		launch_cmd_prefix = launch_cmd_prefix_;
		main_dir = FileSplitDir(exe_path.get_path(), &exe_name);
		if (launch_cmd_prefix.empty()) {
			launch_cmd_prefix = "start /b \"\" \"" + exe_path.get_path() + "\"";
		}
	}
	WebBrowserInfo::WebBrowserInfo(const WebBrowserInfo &src) : WebBrowserInfo() {
		main_dir = src.main_dir;
		exe_name = src.exe_name;
		launch_cmd_prefix = src.launch_cmd_prefix;
		prev_priority_class = src.prev_priority_class;
	}
	WebBrowserInfo::WebBrowserInfo(WebBrowserInfo &&src) : WebBrowserInfo() {
		main_dir = src.main_dir;
		exe_name = src.exe_name;
		launch_cmd_prefix = src.launch_cmd_prefix;
		hide_wnds_thread = src.hide_wnds_thread;
		prev_priority_class = src.prev_priority_class;
		src.prev_priority_class = 0;
		src.hide_wnds_thread = NULL;
	}
	WebBrowserInfo::~WebBrowserInfo() {
		FreeHideThread();
		ResetPriorityClass();
	}
	ProgError WebBrowserInfo::ChangePriorityClass(DWORD new_priority) {
		if (new_priority == 0) {
			return ErrorCodes::kInvalidParam;
		}
		HANDLE process = NULL;
		ProgError error = OpenProc(PROCESS_QUERY_INFORMATION | PROCESS_SET_INFORMATION, FALSE, &process);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		if (process != NULL) {
			if (!prev_priority_class) {
				prev_priority_class = GetPriorityClass(process);
				if (!prev_priority_class) {
					CloseHandle(process);
					return GetLastError();
				}
			}
			if (!SetPriorityClass(process, new_priority)) {
				CloseHandle(process);
				return GetLastError();
			}
			CloseHandle(process);
		}
		return ErrorCodes::kSuccess;
	}
	ProgError WebBrowserInfo::ResetPriorityClass() {
		if (prev_priority_class == 0) {
			return ErrorCodes::kInvalidState;
		}
		HANDLE process = NULL;
		ProgError error = OpenProc(PROCESS_SET_INFORMATION, FALSE, &process);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		if (process != NULL) {
			if (!SetPriorityClass(process, prev_priority_class)) {
				CloseHandle(process);
				return GetLastError();
			}
			CloseHandle(process);
		}
		prev_priority_class = 0;
		return ErrorCodes::kSuccess;
	}
	void WebBrowserInfo::set_hide_wnds_thread() {
		if (hide_wnds_thread != NULL) {
			CloseHandle(hide_wnds_thread);
			hide_wnds_thread = NULL;
		}
	}
	bool WebBrowserInfo::operator==(const WebBrowserInfo &rhs) const {
		return exe_name == rhs.exe_name && launch_cmd_prefix == rhs.launch_cmd_prefix;
	}
	bool WebBrowserInfo::operator!=(const WebBrowserInfo &rhs) const {
		return !(*this == rhs);
	}
	void WebBrowserInfo::FreeHideThread() {
		if (hide_wnds_thread != NULL) {
			WaitForSingleObject(hide_wnds_thread, INFINITE);
			set_hide_wnds_thread();
		}
	}
	ProgError WebBrowserInfo::Is64Bit(bool *x64_out) const {
		if (x64_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		DWORD binary_type;
		if (!GetBinaryType(FileFormatPath(main_dir + "/" + exe_name, true).c_str(), &binary_type)) {
			return GetLastError();
		}
		*x64_out = binary_type == SCS_64BIT_BINARY;
		return ErrorCodes::kSuccess;
	}
	ProgError WebBrowserInfo::StartSilent(const std::string &arguments) {
		if (g_prev_proc_priority == 0) {
			SetProgPriority(ABOVE_NORMAL_PRIORITY_CLASS, &g_prev_proc_priority);
		}
		HWND foreground = GetForegroundWindow();
		while (foreground == NULL) {
			foreground = GetForegroundWindow();
		}
		ProgError error = HideWindows(5000, foreground);
		if (error != ErrorCodes::kSuccess) {
			ResetPrevProcPriority();
			return error;
		}
		system((arguments.empty() ? launch_cmd_prefix : launch_cmd_prefix + " " + arguments).c_str());
		return ErrorCodes::kSuccess;
	}
	ProgError WebBrowserInfo::HideWindows(const TimeDiff exec_time, HWND topmost_wnd) {
		if (hide_wnds_thread != NULL) {
			return ErrorCodes::kInvalidState;
		}
		WebBrowserInfo_HideWindows_Params *hide_wnds_params = new WebBrowserInfo_HideWindows_Params();
		if (hide_wnds_params == nullptr) {
			return ErrorCodes::kOutOfMem;
		}
		hide_wnds_params->browser = this;
		hide_wnds_params->exec_time = exec_time;
		hide_wnds_params->topmost_wnd = topmost_wnd;
		ProgError error = hide_wnds_params->screenshot_hlpr.CaptureImage();
		if (error != ErrorCodes::kSuccess) {
			delete hide_wnds_params;
			return error;
		}
		error = hide_wnds_params->screenshot_hlpr.StartDisplayingImage();
		if (error != ErrorCodes::kSuccess) {
			delete hide_wnds_params;
			return error;
		}
		hide_wnds_thread = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)WebBrowserInfo_HideProcWindows, hide_wnds_params, 0, NULL);
		if (hide_wnds_thread == NULL) {
			delete hide_wnds_params;
			return GetLastError();
		}
		return !SetThreadPriority(hide_wnds_thread, THREAD_PRIORITY_TIME_CRITICAL) ? GetLastError() : ErrorCodes::kSuccess;
	}
	ProgError WebBrowserInfo::TerminateSync() {
		HANDLE browser_handle;
		ProgError error = OpenProc(PROCESS_TERMINATE | SYNCHRONIZE, FALSE, &browser_handle);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		if (browser_handle != NULL) {
			TerminateProcess(browser_handle, 0);
			if (WaitForSingleObject(browser_handle, INFINITE) == WAIT_FAILED) {
				CloseHandle(browser_handle);
				return GetLastError();
			}
			CloseHandle(browser_handle);
			Sleep(500);  // Delay needed to ensure that the process is fully destroyed so that
						 // OpenProcess can't find it.
		}
		return ErrorCodes::kSuccess;
	}
	std::string WebBrowserInfo::get_main_dir() const {
		return main_dir;
	}
	std::string WebBrowserInfo::get_exe_name() const {
		return exe_name;
	}
	std::string WebBrowserInfo::get_launch_cmd_prefix() const {
		return launch_cmd_prefix;
	}
	ProgError WebBrowserInfo::IsRunning(bool *running_out) const {
		if (running_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		HANDLE process = NULL;
		ProgError error = OpenProc(PROCESS_QUERY_INFORMATION, FALSE, &process);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		if (process != NULL) {
			CloseHandle(process);
			*running_out = true;
		}
		else {
			*running_out = false;
		}
		return ErrorCodes::kSuccess;
	}
	ProgError WebBrowserInfo::OpenProc(DWORD desired_acc, BOOL inherit, HANDLE *process_out) const {
		if (process_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		HANDLE snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
		if (snapshot == INVALID_HANDLE_VALUE) {
			return GetLastError();
		}
		PROCESSENTRY32 proc_entry{ 0 };
		proc_entry.dwSize = sizeof(PROCESSENTRY32);
		if (!Process32First(snapshot, &proc_entry)) {
			CloseHandle(snapshot);
			return GetLastError();
		}
		*process_out = NULL;
		do {
			if (!strcmp(proc_entry.szExeFile, exe_name.c_str())) {
				*process_out = OpenProcess(desired_acc, inherit, proc_entry.th32ProcessID);
				if (*process_out == NULL) {
					CloseHandle(snapshot);
					return GetLastError();
				}
				break;
			}
		} while (Process32Next(snapshot, &proc_entry));
		CloseHandle(snapshot);
		return ErrorCodes::kSuccess;
	}
	WebBrowserInfo &WebBrowserInfo::operator=(WebBrowserInfo &&src) {
		if (this != &src) {
			FreeHideThread();
			ResetPriorityClass();
			main_dir = src.main_dir;
			exe_name = src.exe_name;
			launch_cmd_prefix = src.launch_cmd_prefix;
			hide_wnds_thread = src.hide_wnds_thread;
			prev_priority_class = src.prev_priority_class;
			src.prev_priority_class = 0;
			src.hide_wnds_thread = NULL;
		}
		return *this;
	}
	WebBrowserInfo &WebBrowserInfo::operator=(const WebBrowserInfo &src) {
		if (this != &src) {
			FreeHideThread();
			ResetPriorityClass();
			main_dir = src.main_dir;
			exe_name = src.exe_name;
			launch_cmd_prefix = src.launch_cmd_prefix;
			prev_priority_class = src.prev_priority_class;
		}
		return *this;
	}

}  // namespace projx
