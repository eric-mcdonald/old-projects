#pragma once

#include <string>

namespace orion {

	typedef int ErrorCode;
	enum ErrorCodes {
		kSuccess = 0,
		kInvalidOp = -1,
		kInvalidParam = -2,
	};

	static const std::string kIrcChannel = "#OrionControlPanel";
	static const std::string kEmailHost = "gmail.com", kEmailUser = "orion.c_logger", kEmailPassword = "OrionClientLogger0#"; // TODO make a configurable email
	static constexpr size_t kEmailMaxFileSz = 25000000;  // 25 MB

}  // namespace orion
