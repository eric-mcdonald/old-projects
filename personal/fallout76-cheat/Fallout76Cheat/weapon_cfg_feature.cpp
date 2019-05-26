#include "stdafx.h"

#include "weapon_cfg_feature.h"

namespace cheat {

	WeaponCfgFeature::WeaponCfgFeature(bool disabled_, std::string cfg_filename, std::string name_, std::string desc_) : FeatureBase(disabled_, cfg_filename, name_, desc_) {}
	Configuration *WeaponCfgFeature::get_config() {
		return nullptr;  // TODO(Eric McDonald): Finish implementing this function.
	}

}  // namespace cheat
