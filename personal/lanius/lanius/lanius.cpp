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

// lanius.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"  // NOLINT

#include "lanius.h"  // NOLINT

#include "native_util_test.h"  // NOLINT
#include "overlay_test.h"  // NOLINT
#include "impl_test.h"  // NOLINT
#include "inject_test.h"  // NOLINT

static const std::string kServiceFilename = "lanius-test-service.exe";

inline lanius::ErrorCode AssertVolType() {
	TCHAR sys_name_buf[MAX_PATH + 1] = { '\0' };
	if (!GetVolumeInformation(NULL, NULL, 0, NULL, NULL, NULL, sys_name_buf, sizeof(sys_name_buf) / sizeof(*sys_name_buf))) {
		return GetLastError();
	}
	return !_tcscmp(sys_name_buf, TEXT("FAT32")) ? lanius::ErrorCodes::kSuccess : lanius::ErrorCodes::kInvalidVolType;
}
namespace lanius {

	ErrorCode ClearArtifacts() {
		SHELLEXECUTEINFO exec_info = { 0 };
		exec_info.cbSize = sizeof(exec_info);
		exec_info.fMask = SEE_MASK_NOASYNC | SEE_MASK_NOCLOSEPROCESS;
		exec_info.hwnd = NULL;
		exec_info.lpVerb = TEXT("runas");
		exec_info.lpFile = TEXT("wevtutil");
		exec_info.lpParameters = TEXT("cl System");
		exec_info.lpDirectory = TEXT("C:\\WINDOWS\\system32");
		exec_info.nShow = SW_SHOWDEFAULT;
		if (!ShellExecuteEx(&exec_info)) {
			return GetLastError();
		}
		if (exec_info.hProcess && WaitForSingleObject(exec_info.hProcess, INFINITE) == WAIT_FAILED) {
			CloseNativeHandle(exec_info.hProcess);
			return GetLastError();
		}
		CloseNativeHandle(exec_info.hProcess);
		return 0;
	}

}  // namespace lanius
lanius::ErrorCode RebootPc() {
	HANDLE proc_token = NULL;
	if (!OpenProcessToken(GetCurrentProcess(), TOKEN_ADJUST_PRIVILEGES | TOKEN_QUERY, &proc_token)) {
		return GetLastError();
	}
	TOKEN_PRIVILEGES token_privileges = { 0 };
	if (!LookupPrivilegeValue(NULL, SE_SHUTDOWN_NAME, &token_privileges.Privileges[0].Luid)) {
		lanius::CloseNativeHandle(proc_token);
		return GetLastError();
	}
	token_privileges.PrivilegeCount = 1;
	token_privileges.Privileges[0].Attributes = SE_PRIVILEGE_ENABLED;
	if (!AdjustTokenPrivileges(proc_token, FALSE, &token_privileges, 0, NULL, NULL)) {
		lanius::CloseNativeHandle(proc_token);
		return GetLastError();
	}
	lanius::CloseNativeHandle(proc_token);
	UINT flags = EWX_REBOOT;
	if (lanius::test::kRestartForceHung) {
		flags |= EWX_FORCEIFHUNG;
	}
	return ExitWindowsEx(flags, SHTDN_REASON_MINOR_OTHER | SHTDN_REASON_FLAG_PLANNED) ? lanius::ErrorCodes::kSuccess : GetLastError();
}
lanius::ErrorCode TerminateService() {
	lanius::ErrorCode error = lanius::ErrorCodes::kSuccess;
	lanius::ProcessInfo proc_info = { 0 };
	error = FindProcess(kServiceFilename, proc_info, INVALID_HANDLE_VALUE);
	if (error != lanius::ErrorCodes::kSuccess) {
		return error;
	}
	lanius::NativeHandle service_handle = OpenProcess(PROCESS_TERMINATE, FALSE, proc_info.pid);
	if (!service_handle) {
		return GetLastError();
	}
	error = lanius::TerminateProc(service_handle, 0);
	if (error != lanius::ErrorCodes::kSuccess) {
		return error;
	}
	return lanius::CloseNativeHandle(service_handle);
}
void OnExit() {
	TerminateService();
	lanius::ClearArtifacts();
	if (lanius::test::kRestartOnShutdown && lanius::WillBsod()) {
		RebootPc();
	}
}
void OnQuickExit() {
	TerminateService();
	lanius::ClearArtifacts();
	if (lanius::test::kRestartOnShutdown && lanius::WillBsod()) {
		RebootPc();
	}
}
lanius::ErrorCode PreInit() {
	lanius::ErrorCode error = lanius::ErrorCodes::kSuccess;
	if (!IsWindowsVistaOrGreater()) {
		error = lanius::ErrorCodes::kUnsupportedOs;
		return error;
	}
	// Asserts that the cheat was launched from a FAT32-formatted volume for security reasons.
	if (lanius::test::kWillAssertVolType) {
		error = AssertVolType();
		if (error != lanius::ErrorCodes::kSuccess) {
			return error;
		}
	}
	return error;
}
lanius::ErrorCode Init() {
	lanius::ErrorCode error = lanius::ErrorCodes::kSuccess;
	while (error == lanius::ErrorCodes::kSuccess) {
		error = TerminateService();
		Sleep(1);
	}
	//error = lanius::CreateProc("D:\\lanius\\lanius-test-service\\x64\\Debug\\" + kServiceFilename, "D:\\lanius\\lanius-test-service", false);
	//if (error != lanius::ErrorCodes::kSuccess) {
		//return error;
	//}
	error = lanius::test::DoApcInjectTest();
	if (error != lanius::ErrorCodes::kSuccess) {
		return error;
	}
	lanius::dse_patched = true;
	//return error;
	return lanius::ErrorCodes::kSuccess;
}
lanius::ErrorCode PostInit() {
	lanius::ErrorCode error = lanius::ErrorCodes::kSuccess;
	// Tests if FindProcess works as it should.
	/*error = lanius::test::FindProcTest(lanius::test::kTargetProc);
	if (error != lanius::ErrorCodes::kSuccess) {
		return error;
	}*/
	/*error = lanius::test::DoOverlayTest(lanius::WindowId("Fortnite", lanius::WindowIdTypes::kPrefix));
	if (error != lanius::ErrorCodes::kSuccess) {
		return error;
	}*/
	/*error = lanius::test::DoTestImpl();
	if (error != lanius::ErrorCodes::kSuccess) {
		return error;
	}*/
	error = std::atexit(OnExit);
	if (error) {
		return error;
	}
	error = std::at_quick_exit(OnQuickExit);
	if (error) {
		return error;
	}
	lanius::ClearArtifacts();
	return error;
}
int main()
{
	int exit_code = PreInit();
	if (exit_code != lanius::ErrorCodes::kSuccess) {
		return exit_code;
	}
	exit_code = Init();
	if (exit_code != lanius::ErrorCodes::kSuccess) {
		return exit_code;
	}
	exit_code = PostInit();
	if (exit_code != lanius::ErrorCodes::kSuccess) {
		return exit_code;
	}
	// TODO(Eric McDonald): Start cheat ticking here:

    return exit_code;
}
