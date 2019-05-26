#define _CRT_SECURE_NO_WARNINGS

#include "Timekeeper.h"

#include <iostream>
#include <cstring>

namespace sict {

	Timekeeper::Timekeeper() : records_sz(0) {
		const char *time_units_ = "seconds";
		for (size_t i = 0; i < Timekeeper::kMaxRecords; ++i) {
			records[i].message = nullptr;
			records[i].time_units = new char[strlen(time_units_) + 1]{ '\0' };
			strcpy(records[i].time_units, time_units_);
		}

	}
	Timekeeper::~Timekeeper() {
		for (size_t i = 0; i < Timekeeper::kMaxRecords; ++i) {
			if (records[i].message != nullptr) {
				delete[] records[i].message;
				records[i].message = nullptr;
			}
			if (records[i].time_units != nullptr) {
				delete[] records[i].time_units;
				records[i].time_units = nullptr;
			}
		}
	}
	void Timekeeper::start() {
		records[records_sz].start_time = std::chrono::steady_clock::now();
	}
	void Timekeeper::stop() {
		records[records_sz].end_time = std::chrono::steady_clock::now();
	}
	void Timekeeper::recordEvent(const char *desc) {
		if (records_sz < kMaxRecords) {
			if (records[records_sz].message != nullptr) {
				delete[] records[records_sz].message;
				records[records_sz].message = nullptr;
			}
			records[records_sz].message = new char[strlen(desc) + 1]{ '\0' };
			strcpy(records[records_sz].message, desc);
			std::chrono::duration<long> time_span = std::chrono::duration_cast<std::chrono::duration<long>>(records[records_sz].end_time - records[records_sz].start_time);
			records[records_sz].recorded_dur = time_span;
			++records_sz;
		}
	}
	void Timekeeper::report(std::ostream &out) {
		std::cout << std::endl << "Execution Times:" << std::endl;
		for (size_t i = 0; i < records_sz; ++i) {
			Record record = records[i];
			std::cout << record.message << "\t" << (record.recorded_dur.count() / 1000000000) << " " << record.time_units << std::endl;
		}
	}

}  // namespace sict
