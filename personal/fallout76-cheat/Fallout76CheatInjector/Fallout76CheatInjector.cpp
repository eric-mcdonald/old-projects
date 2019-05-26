// Fallout76CheatInjector.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"


struct FreeLibParams {
	HMODULE library;
	DWORD exit_code;
};

int InjectDll(DWORD proc_id, const std::string &dll_path, HANDLE snapshot) {
	HANDLE process = OpenProcess(PROCESS_CREATE_THREAD | PROCESS_VM_OPERATION | PROCESS_VM_WRITE | SYNCHRONIZE | PROCESS_VM_READ | PROCESS_QUERY_INFORMATION, FALSE, proc_id);
	if (!process) {
		CloseHandle(snapshot);
		return GetLastError();
	}
	const char *dll_path_c = dll_path.c_str();
	LPVOID alloc_dll_path = VirtualAllocEx(process, NULL, strlen(dll_path_c) + 1, MEM_COMMIT | MEM_RESERVE, PAGE_READWRITE);
	if (!alloc_dll_path) {
		CloseHandle(process);
		CloseHandle(snapshot);
		return GetLastError();
	}
	if (!WriteProcessMemory(process, alloc_dll_path, dll_path_c, strlen(dll_path_c) + 1, NULL)) {
		CloseHandle(process);
		VirtualFreeEx(process, alloc_dll_path, 0, MEM_RELEASE);
		CloseHandle(snapshot);
		return GetLastError();
	}
	DWORD load_thread_id;
	HANDLE load_lib_thread = CreateRemoteThread(process, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(LoadLibraryA), alloc_dll_path, 0, &load_thread_id);
	if (!load_lib_thread) {
		CloseHandle(process);
		VirtualFreeEx(process, alloc_dll_path, 0, MEM_RELEASE);
		CloseHandle(snapshot);
		return GetLastError();
	}
	if (WaitForSingleObject(load_lib_thread, INFINITE) == WAIT_FAILED) {
		CloseHandle(load_lib_thread);
		VirtualFreeEx(process, alloc_dll_path, 0, MEM_RELEASE);
		CloseHandle(process);
		CloseHandle(snapshot);
		return GetLastError();
	}
	DWORD load_exit_code;
	if (!GetExitCodeThread(load_lib_thread, &load_exit_code)) {
		return GetLastError();
	}
	CloseHandle(load_lib_thread);
	VirtualFreeEx(process, alloc_dll_path, 0, MEM_RELEASE);
	CloseHandle(process);
	return 0;
}
int main()
{
	static const std::string program_name = "csgo.exe", dll_path = "C:\\Users\\eric_\\OneDrive\\Documents\\Visual Studio 2015\\Projects\\Fallout76CheatInjector\\Debug\\Fallout76Cheat.dll";
	HANDLE snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	if (snapshot == INVALID_HANDLE_VALUE) {
		return GetLastError();
	}
	PROCESSENTRY32 pe32 = { 0 };
	pe32.dwSize = sizeof(PROCESSENTRY32);
	if (!Process32First(snapshot, &pe32)) {
		CloseHandle(snapshot);
		return GetLastError();
	}
	do {
		USES_CONVERSION;
		if (!_tcscmp(pe32.szExeFile, A2CW(program_name.c_str()))) {
			int error = InjectDll(pe32.th32ProcessID, "C:\\Users\\eric_\\OneDrive\\Documents\\Visual Studio 2015\\Projects\\Fallout76CheatInjector\\Fallout76Cheat\\lib\\freetype.dll", snapshot);
			if (error) {
				CloseHandle(snapshot);
				return error;
			}
			error = InjectDll(pe32.th32ProcessID, dll_path, snapshot);
			if (error) {
				CloseHandle(snapshot);
				return error;
			}
			/*std::cout << "Enter any text to unload " << dll_path << "..." << std::endl;
			char text[256];
			std::cin >> text;
			DWORD unload_thread_id;
			FreeLibParams *unload_params = new FreeLibParams();
			unload_params->library = reinterpret_cast<HMODULE>(load_exit_code);
			unload_params->exit_code = 0;
			HANDLE unload_lib_thread = CreateRemoteThread(process, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(FreeLibraryAndExitThread), *reinterpret_cast<LPVOID*>(unload_params), 0, &unload_thread_id);
			if (!unload_lib_thread) {
				CloseHandle(process);
				CloseHandle(snapshot);
				delete unload_params;
				unload_params = nullptr;
				return GetLastError();
			}
			if (WaitForSingleObject(unload_lib_thread, INFINITE) == WAIT_FAILED) {
				CloseHandle(unload_lib_thread);
				CloseHandle(process);
				CloseHandle(snapshot);
				delete unload_params;
				unload_params = nullptr;
				return GetLastError();
			}
			DWORD unload_exit_code;
			if (!GetExitCodeThread(unload_lib_thread, &unload_exit_code)) {
				delete unload_params;
				unload_params = nullptr;
				return GetLastError();
			}
			CloseHandle(unload_lib_thread);
			VirtualFreeEx(process, alloc_dll_path, 0, MEM_RELEASE);
			CloseHandle(process);
			delete unload_params;
			unload_params = nullptr;*/
			break;
		}
	} while (Process32Next(snapshot, &pe32));
	CloseHandle(snapshot);
    return 0;
}

