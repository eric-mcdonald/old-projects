#pragma once

#include "feature_base.h"

namespace cheat {

	class WeaponCfgFeature : public FeatureBase {
	public:
		WeaponCfgFeature(bool, std::string /*cfg_filename*/, std::string /*name_*/, std::string /*desc_*/);
		virtual Configuration *get_config() override;
	};

}  // namespace cheat
