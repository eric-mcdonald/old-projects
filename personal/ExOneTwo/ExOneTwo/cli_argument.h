#pragma once

#include <vector>

#include "main_def.h"
#include "registry.h"

namespace x12 {

	namespace cli {

		// TODO(Eric) Impl
		const char *kCmdPrefix = "/", *kArgPrefix = "--";
		struct Argument : X12Obj {
			std::string name;
			std::vector<std::string> aliases;
		};
		struct Command : Argument {
			std::vector<std::string> params;
		};
		class InteractCommand : Command {
		public:
			virtual void Execute() = 0;
		};
		bool ParseArg(std::string arg_text, const util::registry::NamedObjRegistry *valid_args, Argument *argument);

	}

}