#include "stdafx.h"

#include "cheat.h"

#include "feature_registry.h"

namespace cheat {

	bool FeatureRegistry::Contains(std::string name) const {
		for (auto &it : features) {
			if (StrEqIgnoreCase(it.first, name)) {
				return true;
			}
		}
		return false;
	}
	ErrorCode FeatureRegistry::Get(std::string name, FeatureInterface **feature_out) const {
		FeatureInterface *found_feat = nullptr;
		for (auto &it : features) {
			if (StrEqIgnoreCase(it.first, name)) {
				found_feat = it.second;
				break;
			}
		}
		if (found_feat == nullptr) {
			return CheatErrorCodes::kElemNotFound;
		}
		if (feature_out) {
			*feature_out = found_feat;
		}
		return CheatErrorCodes::kSuccess;
	}
	ErrorCode FeatureRegistry::Register(std::string name, FeatureInterface *feature) {
		if (feature->is_disabled()) {
			return CheatErrorCodes::kFeatDisabled;
		}
		if (Contains(name)) {
			return CheatErrorCodes::kElemAlreadyRegistered;
		}
		ErrorCode error = feature->OnRegister(Cheat::GetInstance());
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		features.insert(std::pair<std::string, FeatureInterface*>(name, feature));
		bool has_instance = false;
		for (auto &it : unique_instances) {
			if (it.second == feature) {
				has_instance = true;
				break;
			}
		}
		if (!has_instance) {
			unique_instances.insert(std::pair<std::string, FeatureInterface*>(name, feature));
		}
		return error;
	}
	void FeatureRegistry::Unregister(std::string name) {
		if (!Contains(name)) {
			return;
		}
		auto feature_it = features.find(name);
		FeatureInterface *feature = feature_it->second;
		if (feature_it->second->OnUnregister(Cheat::GetInstance()) != CheatErrorCodes::kSuccess) {
			return;
		}
		features.erase(feature_it);
		bool has_instance = false;
		for (auto &it : features) {
			if (it.second == feature) {
				has_instance = true;
				break;
			}
		}
		if (!has_instance) {
			unique_instances.erase(unique_instances.find(name));
		}
	}
	const std::map<std::string, FeatureInterface*> &FeatureRegistry::get_features() const {
		return features;
	}
	const std::map<std::string, FeatureInterface*> &FeatureRegistry::get_unique_instances() const {
		return unique_instances;
	}

}  // namespace cheat
