#include "stdafx.h"

#include "routine.h"

namespace cheat {

	Routine::Routine(bool disabled_, std::string cfg_filename, std::string name_, std::string desc_) : FeatureBase(disabled_, cfg_filename, name_, desc_), enabled(false) {
		get_config()->SetEntry("start_enabled", enabled);
	}
	Routine::Routine() : FeatureBase(false, "internal_routine.cfg", "", ""), enabled(false) {}
	Routine::~Routine() {
		set_enabled(false);
	}
	bool Routine::is_enabled() const {
		return enabled;
	}
	void Routine::Toggle() {
		set_enabled(!is_enabled());
	}
	void Routine::set_enabled(bool enabled_) {
		bool prev_enabled = is_enabled();
		enabled = enabled_;
		if (prev_enabled != is_enabled()) {
			OnToggled();
		}
	}

}  // namespace cheat
