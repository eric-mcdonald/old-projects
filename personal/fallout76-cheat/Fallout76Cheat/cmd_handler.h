#pragma once

#include <iostream>

#include <Windows.h>

#include "feature_registry.h"
#include "logger.h"

namespace cheat {

	class CmdHandler {
		FeatureRegistry &cmds;
		std::istream &input;
		Logger &output, &err_out;
		HANDLE proc_loop;
		bool stop;
	public:
		CmdHandler(std::istream &, Logger &/*output_*/, Logger &/*err_out_*/, FeatureRegistry &);
		~CmdHandler();
		bool is_stop() const;
		FeatureRegistry &get_cmds();
		std::istream &get_input();
		Logger &get_output();
		Logger &get_err_out();
	};

}  // namespace cheat
