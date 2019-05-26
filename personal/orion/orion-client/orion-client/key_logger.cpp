#include "stdafx.h"
#include "key_logger.h"

namespace orion {

	KeyLogger::KeyLogger(const Email &email_, size_t max_file_sz_) : Logger(email_), max_file_sz(max_file_sz_), last_window(NULL) {}
	ErrorCode KeyLogger::Log() {
		// TODO Implemnt this
		return ErrorCodes::kInvalidOp;
	}
	size_t KeyLogger::get_max_file_sz() const {
		return max_file_sz;
	}

}  // namespace orion
