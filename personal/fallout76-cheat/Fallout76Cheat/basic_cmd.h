#pragma once

#include "command.h"

namespace cheat {

	typedef ErrorCode(*CmdExecFn)(std::string *, size_t, Logger &/*out*/, Logger &/*err_out*/);
	class BasicCmd : public Command {
		CmdExecFn exec_fn;
	public:
		BasicCmd(bool, std::string /*cfg_filename*/, std::string /*name_*/, std::string /*desc_*/, std::string /*id_*/, std::string /*usage_*/, size_t, const std::vector<std::string> &, CmdExecFn);
		ErrorCode Execute(std::string *, size_t, Logger &/*out*/, Logger &/*err_out*/);
	};

}  // namespace cheat
