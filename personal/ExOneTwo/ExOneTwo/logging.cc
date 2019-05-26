#include "stdafx.h"

#include "logging.h"

namespace x12 {

	namespace log {

		Logger::Logger(std::string file_) : file(file_) {}
		void Logger::Log(std::string message, Types type) {
			// TODO(Eric McDonald) Needs an implementation. An invalid enum constant for 'type' should put the message through std::cout with a prefix like '[UNKNOWN]'.
		}

	}

}