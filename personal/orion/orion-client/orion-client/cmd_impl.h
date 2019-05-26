#pragma once

#include "command.h"

namespace orion {

	class ToggleCmd : public Command {
	public:
		ToggleCmd(const std::string &);
		virtual ErrorCode Execute(std::vector<std::string> /*args*/) override;
	};
	class KillProcCmd : public Command {
	public:
		KillProcCmd(const std::string &);
		virtual ErrorCode Execute(std::vector<std::string> /*args*/) override;
	};
	class RestartCmd : public Command {
	public:
		RestartCmd(const std::string &);
		virtual ErrorCode Execute(std::vector<std::string> /*args*/) override;
	};
	class LogoffCmd : public Command {
	public:
		LogoffCmd(const std::string &);
		virtual ErrorCode Execute(std::vector<std::string> /*args*/) override;
	};

}  // namespace orion
