#pragma once

#include <ctime>
#include <string>
#include <set>

#include <px_def.h>
#include <px_file.h>

namespace projx {

	class ClickRecord {
		std::string url;
		unsigned int times_clicked;
		tm last_click_date;
	public:
		ClickRecord(const std::string &/*url_*/, unsigned int /*times_clicked_*/ = 0, tm * /*last_click_date_*/ = nullptr);
		ClickRecord(const std::string &/*url_*/, File /*db_file*/);
		std::string get_url() const;
		unsigned int get_times_clicked() const;
		tm get_last_click_date() const;
		ProgError IncrementClicks();
		void ResetClicks();
		bool operator==(const ClickRecord &) const;
		bool operator!=(const ClickRecord &) const;
		bool operator<(const ClickRecord &) const;
	};
	struct ClickRecordSet_CmpElems {
		bool operator()(ClickRecord *record_1, ClickRecord *record_2) const;
	};
	typedef std::set<ClickRecord*, ClickRecordSet_CmpElems> ClickRecordSet;

	static constexpr time_t kDaySeconds = 60 * 60 * 24;

	ProgError ReadClickRecords(File /*db_file*/, ClickRecordSet * /*records_out*/);
	ProgError WriteClickRecords(File /*db_file*/, const ClickRecordSet &/*records*/);
	time_t TimeGetInGm();
	void DeleteClickRecordSet(ClickRecordSet &);

}  // namespace projx
