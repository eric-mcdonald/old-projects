#include "stdafx.h"

#include "basic_cmd.h"

namespace cheat {

	BasicCmd::BasicCmd(bool disabled_, std::string cfg_filename, std::string name_, std::string desc_, std::string id_, std::string usage_, size_t min_params_, const std::vector<std::string> &aliases_, CmdExecFn exec_fn_) : Command(disabled_, cfg_filename, name_, desc_, id_, usage_, min_params_, aliases_), exec_fn(exec_fn_) {}
	ErrorCode BasicCmd::Execute(std::string *args, size_t args_sz, Logger &output, Logger &err_out) {
		ErrorCode error = CheatErrorCodes::kSuccess;
		error = Command::Execute(args, args_sz, output, err_out);
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		error = exec_fn(args, args_sz, output, err_out);
		return error;
	}

}  // namespace cheat
