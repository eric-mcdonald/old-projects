#pragma once

#include <string>
#include <unordered_map>

#include <Windows.h>
#include <px_def.h>
#include <px_file.h>
#include <timer.h>
#include <url_helper.h>

namespace projx {

	class WebBrowserInfo {
		std::string main_dir, exe_name;
		std::string launch_cmd_prefix;
		HANDLE hide_wnds_thread;
		DWORD prev_priority_class;
	public:
		WebBrowserInfo();
		WebBrowserInfo(const File &/*exe_path*/, const std::string &/*launch_cmd_prefix_*/ = "");
		WebBrowserInfo(const WebBrowserInfo &);
		WebBrowserInfo(WebBrowserInfo &&);
		~WebBrowserInfo();
		bool operator==(const WebBrowserInfo &) const;
		bool operator!=(const WebBrowserInfo &) const;
		std::string get_main_dir() const;
		std::string get_exe_name() const;
		std::string get_launch_cmd_prefix() const;
		ProgError IsRunning(bool *) const;
		ProgError OpenProc(DWORD /*desired_acc*/, BOOL /*inherit*/, HANDLE *) const;
		ProgError TerminateSync();
		ProgError StartSilent(const std::string &/*arguments*/);
		ProgError Is64Bit(bool *) const;
		void set_hide_wnds_thread();
		WebBrowserInfo &operator=(const WebBrowserInfo &);
		WebBrowserInfo &operator=(WebBrowserInfo &&);
	private:
		ProgError HideWindows(const TimeDiff /*exec_time*/, HWND /*topmost_wnd*/);
		void FreeHideThread();
		ProgError ResetPriorityClass();
		ProgError ChangePriorityClass(DWORD /*new_priority*/);
	};

}  // namespace projx
