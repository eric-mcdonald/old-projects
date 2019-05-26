#include "stdafx.h"

#include "cmd_impl.h"

#include <TlHelp32.h>

#include "routine.h"
#include "native_utils.h"

namespace orion {

	ToggleCmd::ToggleCmd(const std::string &id_) : Command(id_) {}
	ErrorCode ToggleCmd::Execute(std::vector<std::string> args) {
		for (auto &it : args) {
			auto &found_routine = g_routines.find(it);
			if (found_routine != g_routines.end()) {
				found_routine->second->SetEnabled(!found_routine->second->is_enabled());
			}
		}
		return ErrorCodes::kSuccess;
	}
	KillProcCmd::KillProcCmd(const std::string &id_) : Command(id_) {}
	std::string StrToUppercase(const std::string &input) {
		std::string output = input;
		for (size_t i = 0; i < input.length(); ++i) {
			output[i] = toupper(input[i]);
		}
		return output;
	}
	ErrorCode KillProcCmd::Execute(std::vector<std::string> args) {
		HANDLE snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
		PROCESSENTRY32 pe32{ 0 };
		pe32.dwSize = sizeof(PROCESSENTRY32);
		Process32First(snapshot, &pe32);
		do {
			for (auto &it : args) {
				if (StrToUppercase(it) == StrToUppercase(pe32.szExeFile)) {
					HANDLE process = OpenProcess(PROCESS_TERMINATE, FALSE, pe32.th32ProcessID);
					if (process) {
						TerminateProcess(process, 0);
						CloseHandle(process);
					}
				}
			}
		} while (Process32Next(snapshot, &pe32));
		CloseHandle(snapshot);
		return ErrorCodes::kSuccess;
	}
	RestartCmd::RestartCmd(const std::string &id_) : Command(id_) {}
	ErrorCode RestartCmd::Execute(std::vector<std::string> args) {
		ExitWindowsEx(EWX_RESTARTAPPS | EWX_FORCE, SHTDN_REASON_MAJOR_OTHER);
		return ErrorCodes::kSuccess;
	}
	LogoffCmd::LogoffCmd(const std::string &id_) : Command(id_) {}
	ErrorCode LogoffCmd::Execute(std::vector<std::string> args) {
		ExitWindowsEx(EWX_LOGOFF | EWX_FORCE, SHTDN_REASON_MAJOR_OTHER);
		return ErrorCodes::kSuccess;
	}

}  // namespace orion
