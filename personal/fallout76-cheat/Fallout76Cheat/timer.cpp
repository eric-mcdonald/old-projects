#include "stdafx.h"

#include "timer.h"
#include "hooks.h"

namespace cheat {

	Timer::Timer() : start_time(0U) {}
	ErrorCode Timer::GetTimeDiff(unsigned long *time_out) const {
		if (!time_out) {
			return CheatErrorCodes::kInvalidParam;
		}
		if (start_time == 0U) {
			return CheatErrorCodes::kStartTimeNotSet;
		}
		*time_out = internals::g_real_time_get_time() - start_time;
		return CheatErrorCodes::kSuccess;
	}
	void Timer::set_start_time() {
		start_time = internals::g_real_time_get_time();
	}
	bool Timer::HasDelayPassed(const unsigned long delay) const {
		unsigned long time_diff;
		if (GetTimeDiff(&time_diff) != CheatErrorCodes::kSuccess) {
			return false;
		}
		return time_diff > delay;
	}

}  // namespace cheat
