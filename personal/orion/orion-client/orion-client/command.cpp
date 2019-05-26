#include "stdafx.h"

#include "command.h"
#include "cmd_impl.h"

namespace orion {

	Command::Command(const std::string &id_) : id(id_) {}
	std::string Command::get_id() const {
		return id;
	}

	std::map<std::string, Command*> g_cmds;

	ErrorCode RegisterCmds() {
		ToggleCmd *toggle = new ToggleCmd("toggle");
		g_cmds["toggle"] = toggle;
		KillProcCmd *kill_proc = new KillProcCmd("kill_proc");
		g_cmds["kill_proc"] = kill_proc;
		RestartCmd *restart = new RestartCmd("restart");
		g_cmds["restart"] = restart;
		LogoffCmd *logoff = new LogoffCmd("logoff");
		g_cmds["logoff"] = logoff;
		return ErrorCodes::kSuccess;
	}

}  // namespace orion
