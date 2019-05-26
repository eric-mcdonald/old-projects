#include "stdafx.h"
#include "logger.h"

namespace orion {

	Logger::Logger(const Email &email_) : email(email_) {}
	Email &Logger::get_email() {
		return email;
	}
	const auto &Logger::get_unsent_logs() const {
		return unsent_logs;
	}

}  // namespace orion
