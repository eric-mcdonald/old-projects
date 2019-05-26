#include "stdafx.h"

#include "feature_base.h"

#include "command.h"

namespace cheat {

	Command::Command(bool disabled_, std::string cfg_filename, std::string name_, std::string desc_, std::string id_, std::string usage_, size_t min_params_, const std::vector<std::string> &aliases_) : FeatureBase(disabled_, cfg_filename, name_, desc_), id(id_), usage(usage_), min_params(min_params_), aliases(aliases_) {}
	const std::vector<std::string> &Command::get_aliases() const {
		return aliases;
	}
	const std::string &Command::get_id() const {
		return id;
	}
	std::string Command::Usage() const {
		std::string usage_str = id;
		for (const auto &it : aliases) {
			usage_str += "|" + it;
		}
		usage_str += " " + usage;
		return usage_str;
	}
	ErrorCode Command::Execute(std::string *args, size_t args_sz, Logger &output, Logger &err_out) {
		if (args == nullptr) {
			return CheatErrorCodes::kInvalidParam;
		}
		ErrorCode error = CheatErrorCodes::kSuccess;
		if (args_sz < min_params) {
			error = CheatErrorCodes::kInvalidParam;
			return error;
		}
		return error;
	}

}  // namespace cheat
