#include "stdafx.h"

#include "basic_routine.h"

namespace cheat {

	RoutineListener::RoutineListener() : listener_priority(Priorities::kNormal) {}
	BasicRoutine::BasicRoutine(bool disabled_, std::string cfg_filename, std::string name_, std::string desc_) : Routine(disabled_, cfg_filename, name_, desc_) {}
	void BasicRoutine::OnToggled() {
		Routine::OnToggled();
		for (auto &it : listeners) {
			if (is_enabled()) {
				it.event_bus->RegisterListener(it.listener_func, it.listener_priority);
			}
			else {
				it.event_bus->UnregisterListener(it.listener_func, it.listener_priority);
			}
		}
	}
	void BasicRoutine::OnRegistered() {
		AddListeners(listeners);
	}

}  // namespace cheat
