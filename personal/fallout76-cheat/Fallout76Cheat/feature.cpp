#include "stdafx.h"

#include "native_utils.h"

#include "feature.h"

namespace cheat {

	FeatureInterface::FeatureInterface(bool disabled_, std::string cfg_filename) : disabled(disabled_), config(nullptr) {
		std::string cfg_path;
		if (internals::GetModuleDirectory(internals::g_this_dll, &cfg_path) == cheat::CheatErrorCodes::kSuccess) {
			config = new Configuration(cfg_path + "\\" + kCfgDir + "\\" + cfg_filename);
		}
	}
	FeatureInterface::~FeatureInterface() {
		if (config != nullptr) {
			delete config;
			config = nullptr;
		}
	}
	bool FeatureInterface::is_disabled() const {
		return disabled;
	}
	Configuration *FeatureInterface::get_config() {
		return config;
	}

}  // namespace cheat
