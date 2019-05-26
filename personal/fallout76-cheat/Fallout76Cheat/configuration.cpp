#include "stdafx.h"

#include "configuration.h"
#include "string_utils.h"

namespace cheat {

	Configuration::Configuration(const std::string &filename_) : file_path(filename_), version(0) {}
	const std::map<std::string, std::string> &Configuration::get_loaded_entries() const {
		return loaded_entries;
	}
	unsigned int Configuration::get_version() const {
		return version;
	}
	ErrorCode Configuration::Read() {
		std::ifstream file(file_path);
		if (!file.is_open()) {
			return CheatErrorCodes::kFileNotOpen;
		}
		while (file.good()) {
			std::string line;
			std::getline(file, line);
			if (StrStartsWith(line, Configuration::kCommentPrefix)) {
				continue;
			}
			unsigned int version_ = strtoul(line.c_str(), nullptr, 10);
			if (version != version_) {
				*g_err_log << g_err_log->GetPrefix() << "Mismatching configuration versions detected in file: " << file_path << " (" << version << "!=" << version_ << ")." << " Trying anyway...";
			}
			break;
		}
		while (file.good()) {
			std::string line;
			std::getline(file, line);
			if (StrStartsWith(line, Configuration::kCommentPrefix)) {
				continue;
			}
			size_t delim_idx = line.find_first_of(Configuration::kEntryDelim);
			if (delim_idx != std::string::npos && delim_idx != line.length() - 1) {
				SetEntry(line.substr(0, delim_idx - 0), line.substr(delim_idx + 1));
			}
		}
		file.close();
		return CheatErrorCodes::kSuccess;
	}
	ErrorCode Configuration::Write() const {
		std::ofstream file(file_path, std::fstream::out | std::fstream::trunc);
		if (!file.is_open()) {
			return CheatErrorCodes::kFileNotOpen;
		}
		file << Configuration::kCommentPrefix << " The version of this configuration file:" << std::endl << version << std::endl;
		for (const auto &it : loaded_entries) {
			file << it.first << Configuration::kEntryDelim << it.second << std::endl;
		}
		return CheatErrorCodes::kSuccess;
	}
	std::string Configuration::get_file_path() const {
		return file_path;
	}

}  // namespace cheat
