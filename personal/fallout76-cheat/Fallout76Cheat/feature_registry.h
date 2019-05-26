#pragma once

#include <map>

#include "feature.h"
#include "cheat_def.h"

namespace cheat {

	class FeatureRegistry {
		std::map<std::string, FeatureInterface*> features, unique_instances;
	public:
		ErrorCode Get(std::string, FeatureInterface **) const;
		bool Contains(std::string) const;
		ErrorCode Register(std::string, FeatureInterface *);
		void Unregister(std::string);
		const std::map<std::string, FeatureInterface*> &get_features() const;
		const std::map<std::string, FeatureInterface*> &get_unique_instances() const;
	};

}  // namespace cheat
