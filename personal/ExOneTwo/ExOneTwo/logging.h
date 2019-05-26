#pragma once

#include "main_def.h"

namespace x12 {

	namespace log {

		enum Types {
			kInfo,
			kWarn,
			kError,
			kFatal
		};
		class Logger : X12Obj {
			std::string file;
		public:
			Logger(std::string file_);
			void Log(std::string message, Types type);
		};

	}

}