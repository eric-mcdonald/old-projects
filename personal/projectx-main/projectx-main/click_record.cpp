#include "stdafx.h"

#include "click_record.h"

namespace projx {

	static constexpr char *kClickFieldSep = "!", *kTimeFieldSep = "_";

	void DeleteClickRecordSet(ClickRecordSet &records) {
		for (auto &it : records) {
			delete it;
		}
		records.clear();
	}
	ProgError ReadClickRecords(File db_file, ClickRecordSet *records_out) {
		if (records_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		if (!db_file.Exists()) {
			return ErrorCodes::kFileNotFound;
		}
		std::ifstream db_stream(db_file.get_path());
		while (db_stream.good()) {
			char entry[1024]{ '\0' };
			db_stream.getline(entry, sizeof(entry));
			const std::string entry_str = StrReplaceAll(entry, "\r", "");
			if (entry_str.empty()) {
				continue;
			}
			std::vector<std::string> entry_fields;
			if (StrSplt(entry_str, kClickFieldSep, &entry_fields) != ErrorCodes::kSuccess) {
				continue;
			}
			std::vector<std::string> time_fields;
			if (StrSplt(entry_fields[1], kTimeFieldSep, &time_fields) != ErrorCodes::kSuccess) {
				continue;
			}
			Byte last_date_bytes[sizeof(tm)]{ 0 };
			for (size_t i = 0; i < time_fields.size(); ++i) {
				last_date_bytes[i] = stoi(time_fields[i], nullptr, 10);
			}
			tm last_click_date;
			memcpy(&last_click_date, last_date_bytes, sizeof(last_date_bytes));
			records_out->insert(new ClickRecord(entry_fields[0], stoi(entry_fields[2], nullptr, 10), &last_click_date));
		}
		db_stream.close();
		return ErrorCodes::kSuccess;
	}
	ProgError WriteClickRecords(File db_file, const ClickRecordSet &records) {
		if (records.empty()) {
			return ErrorCodes::kSuccess;
		}
		std::ofstream db_stream(db_file.get_path(), std::ofstream::out | std::ofstream::trunc);
		if (!db_stream.is_open()) {
			return ErrorCodes::kFailedApiCall;
		}
		for (const auto &it : records) {
			Byte last_date_bytes[sizeof(tm)];
			memcpy(last_date_bytes, &it->get_last_click_date(), sizeof(last_date_bytes));
			db_stream << it->get_url() << kClickFieldSep;
			for (size_t i = 0; i < sizeof(last_date_bytes); ++i) {
				db_stream << (int)last_date_bytes[i];
				if (i != sizeof(last_date_bytes) - 1) {
					db_stream << kTimeFieldSep;
				}
			}
			db_stream << kClickFieldSep << it->get_times_clicked() << std::endl;
		}
		db_stream.close();
		return ErrorCodes::kSuccess;
	}
	inline ProgError TimeGetCurrentGm(tm *result) {
		if (result == nullptr) {
			ErrorCodes::kInvalidParam;
		}
		time_t current_time = time(nullptr);
		if (current_time == -1) {
			return ErrorCodes::kFailedApiCall;
		}
		ProgError error = gmtime_s(result, &current_time);
		if (error) {
			return error;
		}
		return ErrorCodes::kSuccess;
	}
	time_t TimeGetInGm() {
		tm t;
		if (TimeGetCurrentGm(&t) != ErrorCodes::kSuccess) {
			return -1;
		}
		return mktime(&t);
	}

	ClickRecord::ClickRecord(const std::string &url_, unsigned int times_clicked_, tm *last_click_date_) : url(url_), times_clicked(times_clicked_) {
		tm current_gmt;
		if (last_click_date_ == nullptr) {
			if (TimeGetCurrentGm(&current_gmt) == ErrorCodes::kSuccess) {
				last_click_date = current_gmt;
			}
		}
		else {
			last_click_date = *last_click_date_;
		}
	}
	ClickRecord::ClickRecord(const std::string &url_, File db_file) : url(url_) {
		TimeGetCurrentGm(&last_click_date);
		times_clicked = 0;
		ClickRecordSet records;
		if (ReadClickRecords(db_file, &records) == ErrorCodes::kSuccess) {
			for (const auto &it : records) {
				if (it->url == url) {
					*this = *it;
					break;
				}
			}
		}
		DeleteClickRecordSet(records);
	}
	bool ClickRecordSet_CmpElems::operator()(ClickRecord *record_1, ClickRecord *record_2) const {
		return *record_1 < *record_2;
	}
	void ClickRecord::ResetClicks() {
		times_clicked = 0;
	}
	ProgError ClickRecord::IncrementClicks() {
		tm current_gmt;
		ProgError error = TimeGetCurrentGm(&current_gmt);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		++times_clicked;
		last_click_date = current_gmt;
		return ErrorCodes::kSuccess;
	}
	std::string ClickRecord::get_url() const {
		return url;
	}
	unsigned int ClickRecord::get_times_clicked() const {
		return times_clicked;
	}
	tm ClickRecord::get_last_click_date() const {
		return last_click_date;
	}
	bool ClickRecord::operator==(const ClickRecord &rhs) const {
		return url == rhs.url;
	}
	bool ClickRecord::operator!=(const ClickRecord &rhs) const {
		return !(*this == rhs);
	}
	bool ClickRecord::operator<(const ClickRecord &rhs) const {
		return url < rhs.url;
	}

}  // namespace projx
