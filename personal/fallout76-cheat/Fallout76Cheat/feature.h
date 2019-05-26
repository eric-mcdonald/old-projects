#pragma once

#include <string>

#include "configuration.h"

namespace cheat {

	class Cheat;
	class FeatureInterface {
		bool disabled;
		Configuration *config;
	public:
		FeatureInterface(bool, std::string);
		~FeatureInterface();
		virtual const std::string &Name() const = 0;
		virtual const std::string &Desc() const = 0;
		bool is_disabled() const;
		virtual ErrorCode OnRegister(Cheat &) {
			return CheatErrorCodes::kSuccess;
		}
		virtual ErrorCode OnUnregister(Cheat &) {
			return CheatErrorCodes::kSuccess;
		}
		virtual Configuration *get_config();
	};

}  // namespace cheat
