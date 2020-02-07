#pragma once

#include "web_browser_info.h"

namespace projx {

	class WebBrowserManager {
	public:
		typedef std::unordered_map<std::string, WebBrowserInfo*> WebBrowserMap;
	private:
		WebBrowserMap web_browsers;
		WebBrowserManager();
		~WebBrowserManager();
	public:
		WebBrowserManager(const WebBrowserManager &) = delete;
		WebBrowserManager(WebBrowserManager &&) = delete;
		static WebBrowserManager &GetInstance();
		const WebBrowserMap &get_web_browsers() const;
		ProgError GetPreferred(WebBrowserInfo *);
		WebBrowserManager &operator=(const WebBrowserManager &) = delete;
		WebBrowserManager &operator=(WebBrowserManager &&) = delete;
	private:
		ProgError UpdateMainPaths();
	};

}  // namespace projx
