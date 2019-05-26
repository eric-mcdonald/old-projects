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

#include "native_util.h"  // NOLINT

#include "lanius.h"  // NOLINT

#define IOCTL_GET_MODULES CTL_CODE(SIOCTL_TYPE, 2050, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_INJECT_LIB CTL_CODE(SIOCTL_TYPE, 2056, METHOD_BUFFERED, FILE_ANY_ACCESS)

namespace lanius {

	typedef HMODULE(WINAPI *LoadLibraryFn)(_In_ LPCWSTR lpFileName);
	struct Image {
		wchar_t full_img_name[MAX_PATH];
		HANDLE proc_id;
		ULONGLONG base_addr;
		SIZE_T size;
	};
	struct GetModulesRequest {
		DWORD proc_id;
		Image loaded_images_buf[256];
		size_t loaded_images_ret;
	};
	struct InjectLibRequest {
		// Do not use TCHAR as it is defined as char in x64 drivers.
		wchar_t proc_filename[MAX_PATH + 1];
		wchar_t lib_path[MAX_PATH + 1];
		LoadLibraryFn load_lib_func;
	};

	BOOL CALLBACK EnumWindowsCmpPrefix(_In_ HWND hwnd, _In_ LPARAM lParam) {
		TCHAR title[MAX_PATH] = { '\0' };
		if (!GetWindowText(hwnd, title, sizeof(title) / sizeof(*title))) {
			return TRUE;
		}
		WindowsCmpPrefixRequest *cmp_prefix_req = reinterpret_cast<WindowsCmpPrefixRequest *>(lParam);
		if (!_tcsncmp(cmp_prefix_req->prefix, title, _tcslen(cmp_prefix_req->prefix))) {
			_tcscpy_s(cmp_prefix_req->found_title, sizeof(cmp_prefix_req->found_title) / sizeof(*cmp_prefix_req->found_title), title);
			cmp_prefix_req->found_handle = hwnd;
			return TRUE;
		}
		return TRUE;
	}
	ErrorCode CreateProc(const std::string &exe_filepath, const std::string &working_dir, const bool wait) {
		STARTUPINFO start_info = { 0 };
		start_info.cb = sizeof(STARTUPINFO);
		USES_CONVERSION;
		wchar_t *exe_path = A2W(exe_filepath.c_str());
		PROCESS_INFORMATION process_info = { 0 };
		ErrorCode error = CreateProcess(exe_path, exe_path, NULL, NULL, FALSE, 0, NULL, A2W(working_dir.c_str()), &start_info, &process_info) ? ErrorCodes::kSuccess : GetLastError();
		if (wait && error == ErrorCodes::kSuccess) {
			WaitForSingleObject(process_info.hProcess, INFINITE);
		}
		CloseNativeHandle(process_info.hProcess);
		CloseNativeHandle(process_info.hThread);
		return error;
	}
	ErrorCode FindProcess(const std::string &filename, ProcessInfo &proc_info, NativeHandle device_file) {
		NativeHandle proc_snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
		if (proc_snapshot == INVALID_HANDLE_VALUE) {
			return GetLastError();
		}
		PROCESSENTRY32 proc_entry = { 0 };
		proc_entry.dwSize = sizeof(PROCESSENTRY32);
		if (!Process32First(proc_snapshot, &proc_entry)) {
			CloseNativeHandle(proc_snapshot);
			return GetLastError();
		}
		bool found_proc = false;
		do {
			USES_CONVERSION;
			const char *exe_file = W2CA(proc_entry.szExeFile);
			if (filename == exe_file) {
				proc_info.pid = proc_entry.th32ProcessID;
				proc_info.exe_filename = exe_file;
				if (device_file != INVALID_HANDLE_VALUE) {
					ErrorCode error = GetModules(proc_info.pid, proc_info.modules, device_file);
					if (error != ErrorCodes::kSuccess) {
						CloseNativeHandle(proc_snapshot);
						return error;
					}
				}
				for (const auto &it : proc_info.modules) {
					if (it.first.find(filename) != std::string::npos) {
						proc_info.exe_dir = it.second.full_path.substr(0, it.second.full_path.find_last_of("\\") - 0);
						break;
					}
				}
				found_proc = true;
				break;
			}
		} while (Process32Next(proc_snapshot, &proc_entry));
		CloseNativeHandle(proc_snapshot);
		return found_proc ? ErrorCodes::kSuccess : ErrorCodes::kProcNotFound;
	}
	ErrorCode GetModules(const ProcessId pid, ModuleMap &modules, NativeHandle device_file) {
		if (device_file == INVALID_HANDLE_VALUE) {
			return ErrorCodes::kIllegalParam;
		}
		GetModulesRequest *get_mods_req = new GetModulesRequest();
		memset(get_mods_req, 0, sizeof(*get_mods_req));
		get_mods_req->proc_id = pid;
		DWORD bytes_ret = 0;
		if (!DeviceIoControl(device_file, IOCTL_GET_MODULES, get_mods_req, sizeof(*get_mods_req), get_mods_req, sizeof(*get_mods_req), &bytes_ret, NULL)) {
			delete get_mods_req;
			return GetLastError();
		}
		for (size_t i = 0; i < get_mods_req->loaded_images_ret; ++i) {
			ModuleInfo mod_info;
			USES_CONVERSION;
			mod_info.full_path = W2CA(get_mods_req->loaded_images_buf[i].full_img_name);
			mod_info.module_addr = get_mods_req->loaded_images_buf[i].base_addr;
			mod_info.module_sz = static_cast<unsigned int>(get_mods_req->loaded_images_buf[i].size);
			modules[mod_info.full_path.substr(mod_info.full_path.find_last_of("\\") + 1)] = mod_info;
		}
		ErrorCode error = get_mods_req->loaded_images_ret == 0 ? ErrorCodes::kNoModulesFound : ErrorCodes::kSuccess;
		delete get_mods_req;
		return error;
	}
	ErrorCode FindWindowByPrefix(const std::string &prefix, WindowsCmpPrefixRequest &cmp_prefix_req) {
		cmp_prefix_req = { 0 };
		cmp_prefix_req.found_handle = NULL;
		USES_CONVERSION;
		const TCHAR *prefix_c = A2CT(prefix.c_str());
		if (!prefix_c) {
			return ErrorCodes::kAtlFailure;
		}
		_tcscpy_s(cmp_prefix_req.prefix, prefix_c);
		if (!EnumWindows(EnumWindowsCmpPrefix, reinterpret_cast<LPARAM>(&cmp_prefix_req))) {
			return GetLastError();
		}
		return cmp_prefix_req.found_handle ? ErrorCodes::kSuccess : ErrorCodes::kWindowNotFound;
	}
	ErrorCode QueueInjectLib(const std::string &lib_path, const std::string &proc_filename, NativeHandle device_file) {
		if (device_file == INVALID_HANDLE_VALUE) {
			return ErrorCodes::kIllegalParam;
		}
		// TODO(Eric McDonald): Implement a way that uses WinAPI functionality.
		InjectLibRequest *inject_lib_req = new InjectLibRequest();  // Allocates it on the heap to avoid stack corruption.
		memset(inject_lib_req, 0, sizeof(*inject_lib_req));
		inject_lib_req->load_lib_func = reinterpret_cast<LoadLibraryFn>(GetProcAddress(GetModuleHandle(L"kernel32.dll"), "LoadLibraryW"));
		USES_CONVERSION;
		const wchar_t *lib_path_c = A2CW(lib_path.c_str());
		if (!lib_path_c) {
			delete inject_lib_req;
			return ErrorCodes::kAtlFailure;
		}
		wcscpy_s(inject_lib_req->lib_path, lib_path.length() + sizeof(wchar_t), lib_path_c);
		const wchar_t *proc_filename_c = A2CW(proc_filename.c_str());
		if (!proc_filename_c) {
			delete inject_lib_req;
			return ErrorCodes::kAtlFailure;
		}
		wcscpy_s(inject_lib_req->proc_filename, proc_filename.length() + sizeof(wchar_t), proc_filename_c);
		DWORD io_info = 0;
		if (!DeviceIoControl(device_file, IOCTL_INJECT_LIB, inject_lib_req, sizeof(*inject_lib_req), inject_lib_req, sizeof(*inject_lib_req), &io_info, NULL)) {
			delete inject_lib_req;
			return GetLastError();
		}
		std::cout << io_info << std::endl;
		delete inject_lib_req;
		return ErrorCodes::kSuccess;
	}

}  // namespace lanius
