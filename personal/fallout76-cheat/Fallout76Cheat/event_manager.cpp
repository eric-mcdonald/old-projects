#include "stdafx.h"

#include <algorithm>

#include "native_utils.h"

#include "event_manager.h"

namespace cheat {

	bool SortByPriority(std::pair<EventListenerFn, Priorities> listener_pair1, std::pair<EventListenerFn, Priorities> listener_pair2) {
		return listener_pair1.second < listener_pair2.second;
	}
	EventManager::EventManager() : mod_listeners(false) {}
	ErrorCode EventManager::RegisterListener(EventListenerFn listener, Priorities priority) {
		if (listener == nullptr) {
			return CheatErrorCodes::kInvalidParam;
		}
		auto listener_pair = std::pair<EventListenerFn, Priorities>(listener, priority);
		if (std::find(listeners.begin(), listeners.end(), listener_pair) != listeners.end()) {
			return CheatErrorCodes::kElemAlreadyRegistered;
		}
		mod_listeners = true;
		listeners.push_back(listener_pair);
		std::sort(listeners.begin(), listeners.end(), SortByPriority);
		mod_listeners = false;
		return CheatErrorCodes::kSuccess;
	}
	ErrorCode EventManager::UnregisterListener(EventListenerFn listener, Priorities priority) {
		if (listener == nullptr) {
			return CheatErrorCodes::kInvalidParam;
		}
		auto listener_it = std::find(listeners.begin(), listeners.end(), std::pair<EventListenerFn, Priorities>(listener, priority));
		if (listener_it == listeners.end()) {
			return CheatErrorCodes::kElemNotFound;
		}
		mod_listeners = true;
		listeners.erase(listener_it);
		std::sort(listeners.begin(), listeners.end(), SortByPriority);
		mod_listeners = false;
		return CheatErrorCodes::kSuccess;
	}
	ErrorCode EventManager::Dispatch(const Event *event_obj, Event *event_out) const {
		if (event_obj == nullptr || event_out == nullptr) {
			return CheatErrorCodes::kInvalidParam;
		}
		*event_out = *event_obj;
		while (mod_listeners) {
			internals::FixedSleep(1);
		}
		for (auto &it : listeners) {
			ErrorCode exec_err = it.first(event_out);
			if (exec_err != CheatErrorCodes::kSuccess) {
				*g_err_log << g_err_log->GetPrefix() << "Failed to execute event listener " << it.first << " with error code " << exec_err << "." << std::endl;
			}
		}
		return CheatErrorCodes::kSuccess;
	}

}  // namespace cheat
