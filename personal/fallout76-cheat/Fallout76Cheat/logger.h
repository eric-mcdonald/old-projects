#pragma once

#include <fstream>
#include <string>

#include "cheat_def.h"

namespace cheat {

	enum class LogLevels {
		kInfo,
		kError,
	};
	typedef std::ostream &(*IoManipFn)(std::ostream &/*os*/);
	class Logger {
		std::ostream &out_stream;
		LogLevels level;
		std::string file_path;
	public:
		Logger(std::ostream &, LogLevels, const std::string &/*file_path_*/ = "");
		std::ostream &get_out_stream() const;
		std::string GetPrefix() const;
		template<class Value>
		Logger &operator<<(Value value) {
			out_stream << value;
			if (WriteToFile()) {
				std::ofstream log_file(file_path, std::ios::out | std::ios::app);
				if (log_file.is_open()) {
					log_file << value;
					log_file.close();
				}
			}
			return *this;
		}
		Logger &operator<<(IoManipFn);
		LogLevels get_level() const;
		std::string get_file_path() const;
		bool WriteToFile() const;
	};

	extern Logger *g_info_log, *g_err_log;

}  // namespace cheat
