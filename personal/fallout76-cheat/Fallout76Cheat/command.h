#pragma once

#include <vector>
#include <iostream>

#include "feature_base.h"
#include "cheat_def.h"
#include "logger.h"

namespace cheat {

	class Command : public FeatureBase {
		std::string id;
		std::string usage;
		std::vector<std::string> aliases;
		size_t min_params;
	public:
		Command(bool, std::string /*cfg_filename*/, std::string /*name_*/, std::string /*desc_*/, std::string /*id_*/, std::string /*usage_*/, size_t, const std::vector<std::string> &);
		const std::vector<std::string> &get_aliases() const;
		virtual ErrorCode Execute(std::string *, size_t, Logger &/*out*/, Logger &/*err_out*/);
		const std::string &get_id() const;
		std::string Usage() const;
	};

}  // namespace cheat
