#include "stdafx.h"

#include "logger.h"

namespace cheat {

	Logger *g_info_log = nullptr, *g_err_log = nullptr;

	Logger::Logger(std::ostream &out_stream_, LogLevels level_, const std::string &file_path_) : out_stream(out_stream_), level(level_), file_path(file_path_) {}
	std::ostream &Logger::get_out_stream() const {
		return out_stream;
	}
	LogLevels Logger::get_level() const {
		return level;
	}
	std::string Logger::get_file_path() const {
		return file_path;
	}
	bool Logger::WriteToFile() const {
		return !file_path.empty();
	}
	std::string Logger::GetPrefix() const {
		std::string prefix = "[" + kCheatName + "] [";
		switch (level) {
		case LogLevels::kInfo:
			prefix += "INFO";
			break;
		case LogLevels::kError:
			prefix += "ERROR";
			break;
		}
		prefix += "] ";
		return prefix;
	}
	Logger &Logger::operator<<(IoManipFn manip_func) {
		out_stream << manip_func;
		if (WriteToFile()) {
			std::ofstream log_file(file_path, std::ios::out | std::ios::app);
			if (log_file.is_open()) {
				log_file << manip_func;
				log_file.close();
			}
		}
		return *this;
	}

}  // namespace cheat
