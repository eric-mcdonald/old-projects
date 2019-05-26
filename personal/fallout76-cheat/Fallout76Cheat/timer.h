#pragma once

namespace cheat {

	class Timer {
		unsigned long start_time;
	public:
		Timer();
		ErrorCode GetTimeDiff(unsigned long *) const;
		void set_start_time();
		bool HasDelayPassed(const unsigned long) const;
	};

}  // namespace cheat
