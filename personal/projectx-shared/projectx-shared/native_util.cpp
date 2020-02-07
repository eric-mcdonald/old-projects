#include "stdafx.h"

#include "native_util.h"

#include "px_file.h"

namespace projx {

	ProgError GetDisplayName(std::string *username_out) {
		if (username_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		TCHAR pc_name[MAX_COMPUTERNAME_LENGTH + 1]{ '\0' };
		DWORD pc_name_len = sizeof(pc_name) / sizeof(TCHAR);
		if (!GetComputerName(pc_name, &pc_name_len)) {
			return GetLastError();
		}
		TCHAR username[UNLEN + 1]{ '\0' };
		DWORD username_len = sizeof(username) / sizeof(TCHAR);
		if (!GetUserName(username, &username_len)) {
			return GetLastError();
		}
		TCHAR display_name[MAX_COMPUTERNAME_LENGTH + 1 + UNLEN + 1]{ '\0' };
		errno_t std_err = strcpy_s(display_name, pc_name);
		if (std_err) {
			return std_err;
		}
		std_err = strcat_s(display_name, sizeof(display_name), "_");
		if (std_err) {
			return std_err;
		}
		std_err = strcat_s(display_name, sizeof(display_name), username);
		if (std_err) {
			return std_err;
		}
		*username_out = display_name;
		return ErrorCodes::kSuccess;
	}
	ProgError IsProcElevated(bool *elevated_out, HANDLE process) {
		if (elevated_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		HANDLE token = NULL;
		if (!OpenProcessToken(process, TOKEN_QUERY, &token)) {
			return GetLastError();
		}
		TOKEN_ELEVATION elevated{ 0 };
		DWORD ret_len = sizeof(elevated);
		if (!GetTokenInformation(token, TokenElevation, &elevated, sizeof(elevated), &ret_len)) {
			CloseHandle(token);
			return GetLastError();
		}
		*elevated_out = elevated.TokenIsElevated != 0;
		CloseHandle(token);
		return ErrorCodes::kSuccess;
	}
	ProgError GetModulePath(HMODULE module, std::string *path_out) {
		if (path_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		TCHAR path[MAX_PATH]{ '\0' };
		if (!GetModuleFileName(module, path, sizeof(path) / sizeof(TCHAR))) {
			return GetLastError();
		}
		*path_out = path;
		return ErrorCodes::kSuccess;
	}
	ProgError GetFuncSize(void *func_ptr, HMODULE module, size_t *bytes_sz_out, bool break_mod_end) {
		if (func_ptr == nullptr || bytes_sz_out == nullptr) {
			return ErrorCodes::kInvalidParam;
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
		return ErrorCodes::kSuccess;
	}
	ProgError IsProc64Bit(bool *x64_out, HANDLE process) {
		if (x64_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		TCHAR proc_name[MAX_PATH]{ '\0' };
		DWORD proc_name_sz = sizeof(proc_name);
		if (!QueryFullProcessImageName(process, 0, proc_name, &proc_name_sz)) {
			return GetLastError();
		}
		DWORD binary_type;
		if (!GetBinaryType(proc_name, &binary_type)) {
			return GetLastError();
		}
		*x64_out = binary_type == SCS_64BIT_BINARY;
		return ErrorCodes::kSuccess;
	}
	ProgError GetModuleFilename(HMODULE module, std::string *filename_out) {
		if (filename_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		std::string filename;
		ProgError error = GetModulePath(module, &filename);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		FileSplitDir(filename, filename_out);
		return ErrorCodes::kSuccess;
	}
	ProgError GetModuleDirectory(HMODULE module, std::string *dir_out) {
		if (dir_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		std::string directory;
		ProgError error = GetModulePath(module, &directory);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		*dir_out = FileSplitDir(directory, nullptr);
		return ErrorCodes::kSuccess;
	}
	ProgError GetCurrentUserDir(std::string *user_dir_out) {
		if (user_dir_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		TCHAR user_path[MAX_PATH]{ '\0' };
		ProgError error = SHGetFolderPath(NULL, CSIDL_PROFILE, NULL, SHGFP_TYPE_CURRENT, user_path);
		if (error != S_OK) {
			return error;
		}
		*user_dir_out = user_path;
		return ErrorCodes::kSuccess;
	}
	ProgError GetProcExeById(DWORD proc_id, std::string *exe_name_out) {
		if (exe_name_out == nullptr) {
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
		*exe_name_out = "";
		do {
			if (proc_entry.th32ProcessID == proc_id) {
				*exe_name_out = proc_entry.szExeFile;
				break;
			}
		} while (Process32Next(snapshot, &proc_entry));
		CloseHandle(snapshot);
		return ErrorCodes::kSuccess;
	}
	ProgError GetProcPathById(DWORD proc_id, std::string *path) {
		if (path == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		HANDLE process = OpenProcess(PROCESS_QUERY_INFORMATION, FALSE, proc_id);
		if (process == NULL) {
			return GetLastError();
		}
		TCHAR proc_path[MAX_PATH];
		DWORD dw = sizeof(proc_path);
		if (!QueryFullProcessImageName(process, 0, proc_path, &dw)) {
			CloseHandle(process);
			return GetLastError();
		}
		*path = proc_path;
		CloseHandle(process);
		return ErrorCodes::kSuccess;
	}

}  // namespace projx
