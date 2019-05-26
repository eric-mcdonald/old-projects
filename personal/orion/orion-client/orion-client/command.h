#pragma once

#include <string>
#include <vector>
#include <map>

#include "orion_def.h"

namespace orion {

	class Command {
		std::string id;
	public:
		Command(const std::string &);
		virtual ErrorCode Execute(std::vector<std::string> /*args*/) = 0;
		std::string get_id() const;
	};

	extern std::map<std::string, Command*> g_cmds;

	ErrorCode RegisterCmds();

}  // namespace orion
