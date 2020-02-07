#include "stdafx.h"

#include "timer.h"

namespace projx {

	Timer::Timer() : start_time(0) {}
	void Timer::set_start_time() {
		start_time = GetTickCount64();
	}
	TimeDiff Timer::get_start_time() const {
		return start_time;
	}
	TimeDiff Timer::CalcTimePassed() const {
		ULONGLONG current_tick = GetTickCount64();
		return start_time == 0 ? 0 : start_time > current_tick ? 0 : current_tick - start_time;
	}
	bool Timer::HasTimePassed(const TimeDiff &milliseconds) const {
		return CalcTimePassed() >= milliseconds;
	}

}  // namespace projx
