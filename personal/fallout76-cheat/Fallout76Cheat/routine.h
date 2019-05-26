#pragma once

#include <string>
#include <vector>

#include "feature_base.h"
#include "event_manager.h"

namespace cheat {

	class Cheat;
	class Routine : public FeatureBase {
		bool enabled;
	public:
		Routine(bool /*disabled_*/, std::string /*cfg_filename*/, std::string /*name_*/, std::string /*desc_*/);
		Routine();
		~Routine();
		bool is_enabled() const;
		void Toggle();
		void set_enabled(bool);
		virtual void OnToggled() {}
	};

}  // namespace cheat
