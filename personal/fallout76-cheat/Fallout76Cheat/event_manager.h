#pragma once

#include <vector>

#include "cheat_def.h"

namespace cheat {

	struct Event {
		void *source_fn;
		void *data;
		bool cancel;
	};
	typedef ErrorCode(*EventListenerFn)(Event *);
	class EventManager {
		std::vector<std::pair<EventListenerFn, Priorities>> listeners;
		bool mod_listeners;  // Eric McDonald: For making listeners thread-safe.
	public:
		EventManager();
		ErrorCode RegisterListener(EventListenerFn, Priorities = Priorities::kNormal);
		ErrorCode UnregisterListener(EventListenerFn, Priorities = Priorities::kNormal);
		ErrorCode Dispatch(const Event *, Event *) const;
	};

}  // namespace cheat
