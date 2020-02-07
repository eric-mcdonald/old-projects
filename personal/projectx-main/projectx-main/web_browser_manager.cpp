#include "stdafx.h"

#include "web_browser_manager.h"

namespace projx {

	inline File BrowserGetDefaultPath(std::string exe_dir) {
		exe_dir = FileFormatPath(exe_dir, true);
		std::string program_files_path;
		// TODO Use path with OS installed as the root for program files.
		static constexpr char *kProgFiles64 = "C:\\Program Files\\", *kProgFiles32 = "C:\\Program Files (x86)\\";
#ifdef PROJECTX_ENV64
		program_files_path = kProgFiles64;
#else
		program_files_path = kProgFiles32;
#endif
		File exe_path = File(program_files_path + exe_dir);
		if (!exe_path.Exists()) {
			exe_path = File(program_files_path == kProgFiles64 ? kProgFiles32 + exe_dir : kProgFiles64 + exe_dir);
		}
		return exe_path;
	}

	WebBrowserManager::WebBrowserManager() {
		web_browsers["internet_explorer"] = new WebBrowserInfo(BrowserGetDefaultPath("Internet Explorer\\iexplore.exe"));
		web_browsers["google_chrome"] = new WebBrowserInfo(File("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"));  // There is only one Google Chrome per computer; it's always in this directory.
		web_browsers["firefox"] = new WebBrowserInfo(BrowserGetDefaultPath("Mozilla Firefox\\firefox.exe"));
		std::string user_path;
		ProgError error = GetCurrentUserDir(&user_path);
		if (error == ErrorCodes::kSuccess) {
			user_path = FileFormatPath(user_path, true);  // Ensures that the format of the path is consistent.
			web_browsers["opera"] = new WebBrowserInfo(File(user_path + "\\AppData\\Local\\Programs\\Opera\\opera.exe"), "start /b \"\" \"" + user_path + "\\AppData\\Local\\Programs\\Opera\\launcher.exe\"");
		}
		UpdateMainPaths();
	}
	WebBrowserManager::~WebBrowserManager() {
		for (auto &it : web_browsers) {
			delete it.second;
		}
		web_browsers.clear();
	}
	WebBrowserManager &WebBrowserManager::GetInstance() {
		static WebBrowserManager instance;
		return instance;
	}
	const WebBrowserManager::WebBrowserMap &WebBrowserManager::get_web_browsers() const {
		return web_browsers;
	}
	ProgError WebBrowserManager::GetPreferred(WebBrowserInfo *preferred_browser_out) {
		if (preferred_browser_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		ProgError error = UpdateMainPaths();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		// Defaults to IE if the web browser is unknown.
		*preferred_browser_out = WebBrowserInfo(web_browsers["internet_explorer"]->get_main_dir() + "/" + web_browsers["internet_explorer"]->get_exe_name(), "start /b \"\"");
		char value_buf[256]{ '\0' };
		DWORD value_sz = sizeof(value_buf);
		ProgError err_status = RegGetValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\Shell\\Associations\\UrlAssociations\\http\\UserChoice", "ProgId", RRF_RT_REG_SZ, NULL, value_buf, &value_sz);
		if (err_status == ERROR_SUCCESS) {
			if (!strncmp(value_buf, "IE.HTTP", value_sz)) {
				*preferred_browser_out = *web_browsers["internet_explorer"];
			}
			else if (!strncmp(value_buf, "FirefoxURL", value_sz)) {
				*preferred_browser_out = *web_browsers["firefox"];
			}
			else if (!strncmp(value_buf, "ChromeHTML", value_sz)) {
				*preferred_browser_out = *web_browsers["google_chrome"];
			}
			else if (!strncmp(value_buf, "OperaStable", value_sz)) {
				*preferred_browser_out = *web_browsers["opera"];
			}
			else if (!strncmp(value_buf, "AppXq0fevzme2pys62n3e0fbqa7peapykr8v", value_sz)) {
				*preferred_browser_out = *web_browsers["internet_explorer"];  // Edge is unsupported, so defaults to IE
			}
			return ErrorCodes::kSuccess;
		}
		memset(value_buf, 0, sizeof(value_buf));
		value_sz = sizeof(value_buf);
		err_status = RegGetValue(HKEY_CURRENT_USER, "Software\\Clients\\StartMenuInternet", NULL, RRF_RT_REG_SZ, NULL, value_buf, &value_sz);
		if (err_status != ERROR_SUCCESS) {
			memset(value_buf, 0, sizeof(value_buf));
			value_sz = sizeof(value_buf);
			err_status = RegGetValue(HKEY_LOCAL_MACHINE, "Software\\Clients\\StartMenuInternet", NULL, RRF_RT_REG_SZ, NULL, value_buf, &value_sz);
		}
		*preferred_browser_out = WebBrowserInfo(File(err_status == ERROR_SUCCESS ? value_buf : web_browsers["internet_explorer"]->get_main_dir() + "/" + web_browsers["internet_explorer"]->get_exe_name()),
			"start /b \"\"");
		return err_status;
	}
	ProgError WebBrowserManager::UpdateMainPaths() {
		for (auto &it : web_browsers) {
			HANDLE process = NULL;
			if (it.second->OpenProc(PROCESS_QUERY_INFORMATION, FALSE, &process) == ErrorCodes::kSuccess && process != NULL) {
				TCHAR exe_path[MAX_PATH]{ '\0' };
				DWORD exe_path_sz = sizeof(exe_path);
				if (!QueryFullProcessImageName(process, 0, exe_path, &exe_path_sz)) {
					CloseHandle(process);
					continue;  // Skip because it can fail with ERROR_ACCESS_DENIED due to a race condition.
				}
				WebBrowserInfo *new_browser = new WebBrowserInfo(File(exe_path), it.second->get_launch_cmd_prefix());
				delete it.second;
				it.second = new_browser;
				CloseHandle(process);
			}
		}
		return ErrorCodes::kSuccess;
	}

}  // namespace projx
