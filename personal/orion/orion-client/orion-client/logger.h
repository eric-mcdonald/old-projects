#pragma once

#include <string>
#include <vector>

#include "email.h"
#include "orion_def.h"

namespace orion {

	class Logger {
		std::vector<std::string> unsent_logs;
		Email email;
	public:
		Logger(const Email &);
		virtual ErrorCode Log() = 0;
		ErrorCode SendEmail();
		Email &get_email();
		const auto &get_unsent_logs() const;
	};

}  // namespace orion
