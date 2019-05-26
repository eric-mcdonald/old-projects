#include "stdafx.h"

#include "cheat.h"
#include "string_utils.h"

#include "cmd_impl.h"

namespace cheat {

	ErrorCode CmdToggleRoutine(std::string *args, size_t args_sz, Logger &out, Logger &err_out) {
		for (size_t i = 0; i < args_sz; ++i) {
			Routine *found_routine;
			ErrorCode routine_err = Cheat::GetInstance().GetRoutine(args[i], &found_routine);
			if (routine_err != CheatErrorCodes::kSuccess) {
				err_out << err_out.GetPrefix() << "Failed to find a routine with name " << args[i] << ", error code " << routine_err << "." << std::endl;
				continue;
			}
			found_routine->Toggle();
			out << out.GetPrefix() << "Toggled routine " << found_routine->Name() << " " << (found_routine->is_enabled() ? "on" : "off") << "." << std::endl;
		}
		return CheatErrorCodes::kSuccess;
	}
	void DisplayCmds(Logger &out) {
		out << out.GetPrefix() << "Commands (" << Cheat::GetInstance().get_cmds().get_unique_instances().size() << "): ";
		size_t cmd_count = 0;
		for (auto &it : Cheat::GetInstance().get_cmds().get_unique_instances()) {
			out << reinterpret_cast<Command*>(it.second)->get_id();
			if (cmd_count < Cheat::GetInstance().get_cmds().get_unique_instances().size() - 1) {
				out << ", ";
			}
			++cmd_count;
		}
		out << std::endl;
	}
	ErrorCode CmdHelp(std::string *args, size_t args_sz, Logger &out, Logger &err_out) {
		if (args_sz == 0) {
			DisplayCmds(out);
			return CheatErrorCodes::kSuccess;
		}
		else if (args_sz == 1) {
			return CheatErrorCodes::kInvalidParam;
		}
		if (StrEqIgnoreCase(args[0], "command")) {
			Command *found_cmd;
			ErrorCode error = Cheat::GetInstance().GetCmd(args[1], &found_cmd);
			if (error != CheatErrorCodes::kSuccess) {
				return error;
			}
			out << out.GetPrefix() << "Identifier: " << found_cmd->get_id() << std::endl;
			out << out.GetPrefix() << "Usage: " << found_cmd->Usage() << std::endl;
			out << out.GetPrefix() << "Description: " << found_cmd->Desc() << std::endl;
		}
		else if (StrEqIgnoreCase(args[0], "routine")) {
			Routine *found_routine;
			ErrorCode error = Cheat::GetInstance().GetRoutine(args[1], &found_routine);
			if (error != CheatErrorCodes::kSuccess) {
				return error;
			}
			out << out.GetPrefix() << "Name: " << found_routine->Name() << std::endl;
			out << out.GetPrefix() << "Description: " << found_routine->Desc() << std::endl;
			out << out.GetPrefix() << "State: " << (found_routine->is_enabled() ? "enabled" : "disabled") << std::endl;
		}
		else if (StrEqIgnoreCase(args[0], "list")) {
			if (StrEqIgnoreCase(args[1], "commands")) {
				DisplayCmds(out);
			}
			else if (StrEqIgnoreCase(args[1], "routines")) {
				out << out.GetPrefix() << "Routines (" << Cheat::GetInstance().get_routines().get_unique_instances().size() << "): ";
				HANDLE std_out = GetStdHandle(STD_OUTPUT_HANDLE);
				CONSOLE_SCREEN_BUFFER_INFO console_info;
				if (!GetConsoleScreenBufferInfo(std_out, &console_info)) {
					return GetLastError();
				}
				size_t routine_count = 0;
				for (auto &it : Cheat::GetInstance().get_routines().get_unique_instances()) {
					SetConsoleTextAttribute(std_out, reinterpret_cast<Routine*>(it.second)->is_enabled() ? FOREGROUND_GREEN : FOREGROUND_RED);
					out << it.second->Name();
					SetConsoleTextAttribute(std_out, console_info.wAttributes);
					if (routine_count < Cheat::GetInstance().get_routines().get_unique_instances().size() - 1) {
						out << ", ";
					}
					++routine_count;
				}
				out << std::endl;
			}
			else {
				return CheatErrorCodes::kInvalidParam;
			}
		}
		else {
			return CheatErrorCodes::kInvalidParam;
		}
		return CheatErrorCodes::kSuccess;
	}
	inline ErrorCode AddCmd(Command *cmd, Cheat &instance) {
		if (cmd == nullptr) {
			return CheatErrorCodes::kInvalidParam;
		}
		ErrorCode error = CheatErrorCodes::kSuccess;
		error = instance.RegisterCmd(cmd);
		if (error != CheatErrorCodes::kSuccess) {
			*g_err_log << g_err_log->GetPrefix() << "Failed to register command \"" << cmd->get_id() << "\" with error code " << error << "." << std::endl;
			return error;
		}
		return CheatErrorCodes::kSuccess;
	}
	ErrorCode RegisterCmds(Cheat &instance) {
		ErrorCode error = AddCmd(new BasicCmd(false, "toggle_cmd.cfg", "toggle", "Toggles routines on or off.", "toggle", "<routine> [routine2]", 1, std::vector<std::string>(), CmdToggleRoutine), instance);
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		std::vector<std::string> aliases;
		aliases.push_back("?");
		error = AddCmd(new BasicCmd(false, "help_cmd.cfg", "help", "Displays useful information about features.", "help", "[command|routine|list] [feature|commands|routines]", 0, aliases, CmdHelp), instance);
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		aliases.clear();

		return CheatErrorCodes::kSuccess;
	}

}  // namespace cheat
