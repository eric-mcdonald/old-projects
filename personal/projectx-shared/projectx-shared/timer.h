#pragma once

#include <chrono>
#include <Windows.h>

namespace projx {

	PROJECTXSHARED_API typedef unsigned long long TimeDiff;
	class PROJECTXSHARED_API Timer {
		TimeDiff start_time;
	public:
		Timer();
		void set_start_time();
		TimeDiff get_start_time() const;
		TimeDiff CalcTimePassed() const;
		bool HasTimePassed(const TimeDiff &/*milliseconds*/) const;
	};

}  // namespace projx
