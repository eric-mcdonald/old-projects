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

#pragma once

#include <vector>
#include <map>
#include <string>

#include <Windows.h>

#include "lanius.h"  // NOLINT

namespace lanius {

	using NativeHandle = HANDLE;
	using ProcessId = DWORD;
	using ExternalAddr = DWORD_PTR;
	struct ModuleInfo;
	using ModuleMap = std::map<std::string, ModuleInfo>;
	struct ModuleInfo {
		std::string full_path;
		ExternalAddr module_addr;
		unsigned int module_sz;
	};
	struct ProcessInfo {
		ProcessId pid;
		std::string exe_filename, exe_dir;
		ModuleMap modules;
	};
	struct WindowsCmpPrefixRequest {
		TCHAR prefix[MAX_PATH];
		TCHAR found_title[MAX_PATH];
		HWND found_handle;
	};

	static constexpr ExternalAddr kNull = 0;

	inline ErrorCode CloseNativeHandle(NativeHandle handle) {
		if (handle == INVALID_HANDLE_VALUE) {
			return ErrorCodes::kInvalidNativeHandle;
		}
		BOOL success = CloseHandle(handle);
		return success ? ErrorCodes::kSuccess : GetLastError();
	}
	inline ErrorCode TerminateProc(NativeHandle proc_handle, unsigned int exit_code) {
		if (!TerminateProcess(proc_handle, exit_code)) {
			return GetLastError();
		}
		return ErrorCodes::kSuccess;
	}
	inline ErrorCode OpenCheatDevice(NativeHandle *device_file) {
		if (!device_file) {
			return ErrorCodes::kIllegalParam;
		}
		*device_file = CreateFile(L"\\\\.\\LaniusDevice", GENERIC_READ | GENERIC_WRITE, FILE_SHARE_READ | FILE_SHARE_WRITE, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
		return *device_file == INVALID_HANDLE_VALUE ? GetLastError() : ErrorCodes::kSuccess;
	}
	ErrorCode CreateProc(const std::string &/*exe_filepath*/, const std::string &/*working_dir*/, const bool /*wait*/);
	ErrorCode FindProcess(const std::string &/*filename*/, ProcessInfo &, NativeHandle /*device_file*/);
	ErrorCode GetModules(const ProcessId, ModuleMap &, NativeHandle /*device_file*/);
	ErrorCode FindWindowByPrefix(const std::string &/*prefix*/, WindowsCmpPrefixRequest &);
	ErrorCode QueueInjectLib(const std::string &/*lib_path*/, const std::string &/*proc_filename*/, NativeHandle /*device_file*/);

}  // namespace lanius
