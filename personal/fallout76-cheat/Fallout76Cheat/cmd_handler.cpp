#include "stdafx.h"

#include <ios>

#include "cmd_handler.h"
#include "command.h"
#include "string_utils.h"
#include "native_utils.h"

namespace cheat {

	void ProcessInput(CmdHandler *handler) {
		Command *help_cmd;
		ErrorCode error = CheatErrorCodes::kSuccess;
		error = handler->get_cmds().Get("help", reinterpret_cast<FeatureInterface**>(&help_cmd));
		handler->get_output() << handler->get_output().GetPrefix() << "Type \"" << help_cmd->get_id() << "\" for a list of commands." << std::endl;
		while (!handler->is_stop()) {
			std::string line;
			std::getline(handler->get_input(), line);
			if (!handler->get_input().good()) {
				handler->get_err_out() << handler->get_err_out().GetPrefix() << "Failed to get input: flags=" << handler->get_input().rdstate() << std::endl;
				internals::FixedSleep(1);
				continue;
			}
			std::string cmd_id = line.substr(0, line.find(" "));
			Command *found_cmd;
			error = handler->get_cmds().Get(cmd_id, reinterpret_cast<FeatureInterface**>(&found_cmd));
			if (error != CheatErrorCodes::kSuccess) {
				handler->get_err_out() << handler->get_err_out().GetPrefix() << "Failed to get command: " << cmd_id << ", failed with error code " << error << "." << std::endl;
				internals::FixedSleep(1);
				continue;
			}
			std::vector<std::string> cmd_args;
			error = StrSplit(line, " ", &cmd_args);
			if (error != CheatErrorCodes::kSuccess) {
				handler->get_err_out() << handler->get_err_out().GetPrefix() << "Failed to parse arguments for command: " << cmd_id << ", failed with error code " << error << "." << std::endl;
				handler->get_err_out() << handler->get_err_out().GetPrefix() << "Usage for command " << cmd_id << ": " << found_cmd->Usage() << std::endl;
				internals::FixedSleep(1);
				continue;
			}
			if (cmd_args.size() != 0U) {
				cmd_args.erase(cmd_args.begin());
			}
			error = found_cmd->Execute(cmd_args.data(), cmd_args.size(), handler->get_output(), handler->get_err_out());
			if (error != CheatErrorCodes::kSuccess) {
				handler->get_err_out() << handler->get_err_out().GetPrefix() << "Failed to execute command: " << cmd_id << ", failed with error code " << error << "." << std::endl;
				handler->get_err_out() << handler->get_err_out().GetPrefix() << "Usage for command " << cmd_id << ": " << found_cmd->Usage() << std::endl;
				internals::FixedSleep(1);
				continue;
			}
			internals::FixedSleep(1);
		}
	}
	CmdHandler::CmdHandler(std::istream &input_, Logger &output_, Logger &err_out_, FeatureRegistry &cmds_) : input(input_), output(output_), err_out(err_out_), cmds(cmds_), stop(false) {
		proc_loop = CreateThread(NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(ProcessInput), this, 0, NULL);  // Eric McDonald: Creates the thread via WinAPI due to the standard library implementation deadlocking when used in DllMain.
	}
	CmdHandler::~CmdHandler() {
		stop = true;
		input.ignore(std::numeric_limits<std::streamsize>::max());
		if (proc_loop != NULL) {
			WaitForSingleObject(proc_loop, INFINITE);
			internals::CloseNativeHandle(proc_loop);
			proc_loop = NULL;
		}
	}
	bool CmdHandler::is_stop() const {
		return stop;
	}
	FeatureRegistry &CmdHandler::get_cmds() {
		return cmds;
	}
	std::istream &CmdHandler::get_input() {
		return input;
	}
	Logger &CmdHandler::get_output() {
		return output;
	}
	Logger &CmdHandler::get_err_out() {
		return err_out;
	}

}  // namespace cheat
