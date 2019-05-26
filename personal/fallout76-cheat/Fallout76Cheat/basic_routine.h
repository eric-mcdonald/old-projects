#pragma once

#include "event_manager.h"
#include "routine.h"

namespace cheat {

	struct RoutineListener {
		EventManager *event_bus;
		EventListenerFn listener_func;
		Priorities listener_priority;
		RoutineListener();
	};
	class BasicRoutine : public Routine {
		std::vector<RoutineListener> listeners;
	public:
		BasicRoutine(bool /*disabled_*/, std::string /*cfg_filename*/, std::string /*name_*/, std::string /*desc_*/);
		virtual void OnToggled() override;
		void OnRegistered();
	protected:
		virtual void AddListeners(std::vector<RoutineListener> &) = 0;
	};

}  // namespace cheat
