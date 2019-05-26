#include "stdafx.h"

#include "feature_base.h"

namespace cheat {

	FeatureBase::FeatureBase(bool disabled_, std::string cfg_filename, std::string name_, std::string desc_) : FeatureInterface(disabled_, cfg_filename), name(name_), desc(desc_) {}

}  // namespace cheat
