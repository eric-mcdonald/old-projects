#pragma once

#include "feature.h"

namespace cheat {

	class FeatureBase : public FeatureInterface {
		std::string name, desc;
	public:
		FeatureBase(bool, std::string /*cfg_filename*/, std::string /*name_*/, std::string /*desc_*/);
		const std::string &Name() const override {
			return name;
		}
		const std::string &Desc() const override {
			return desc;
		}
	};

}  // namespace cheat
