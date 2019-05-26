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

#include "native_util_test.h"  // NOLINT

#include "memory_stream.h"  // NOLINT

namespace lanius {

	namespace test {

		int FindProcTest(const std::string &proc_name) {
			ErrorCode error = ErrorCodes::kSuccess;
			NativeHandle device_file = INVALID_HANDLE_VALUE;
			OpenCheatDevice(&device_file);
			// Keep trying to open it as it might not be available yet.
			while (device_file == INVALID_HANDLE_VALUE && GetLastError() == ERROR_FILE_NOT_FOUND) {
				OpenCheatDevice(&device_file);
				Sleep(2000);
			}

			if (device_file == INVALID_HANDLE_VALUE) {
				return GetLastError();
			}
			ProcessInfo proc_info = { 0 };
			error = FindProcess(proc_name, proc_info, device_file);
			if (error != ErrorCodes::kSuccess) {
				CloseNativeHandle(device_file);
				return error;
			}
			std::cout << proc_info.pid << ":" << proc_info.exe_dir << ":" << proc_info.exe_filename << std::endl;
			for (const auto &it : proc_info.modules) {
				std::cout << it.first << ":" << it.second.full_path << ":" << std::hex << it.second.module_addr << ":" << std::dec << it.second.module_sz << std::endl;
			}
			CloseNativeHandle(device_file);
			return error;
		}

	}  // namespace test

}  // namespace lanius
