#pragma once

#include <unordered_map>

#include <Windows.h>

#include "IRCClient.h"
#include "px_def.h"
#include "px_file.h"
#include "url_helper.h"

namespace projx {

	struct PROJECTXSHARED_API IrcConnectParams {
		std::string channel;
		std::string host;
		unsigned int port;
	};
	struct PROJECTXSHARED_API DownloadedLib {
		HMODULE loaded_lib;
		bool temporary;
	};
	class PROJECTXSHARED_API Client {
		IRCClient *irc;
		UrlHelper *file_downloader;
		bool running;
		std::unordered_map<std::string, DownloadedLib> downloaded_libs;
	protected:
		Client();
		~Client();
	public:
		virtual ProgError Start();
		virtual ProgError Stop();
		IRCClient *get_irc();
		virtual void RequestShutdown();
		virtual bool IsRunning() const;
		UrlHelper *get_file_downloader() const;
		ProgError UpdateLibs();
		const std::unordered_map<std::string, DownloadedLib> &get_downloaded_libs() const;
		virtual IrcConnectParams GetIrcParams() const = 0;
		virtual std::string GetLibListUrl() const = 0;
	private:
		void UnloadLibs();
	};

}  // namespace projx
