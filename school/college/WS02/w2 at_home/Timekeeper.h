#include <chrono>
#include <iostream>

namespace sict {

	struct Record {
		char *message;
		char *time_units;
		std::chrono::steady_clock::time_point start_time, end_time;
		std::chrono::steady_clock::duration recorded_dur;
	};
	class Timekeeper {
		static constexpr size_t kMaxRecords = 10;
		Record records[kMaxRecords];
		size_t records_sz;
	public:
		Timekeeper();
		~Timekeeper();
		void start();
		void stop();
		void recordEvent(const char *);
		void report(std::ostream &);
	};

}  // namespace sict
