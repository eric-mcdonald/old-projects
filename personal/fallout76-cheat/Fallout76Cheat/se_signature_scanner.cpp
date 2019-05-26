#include "stdafx.h"

#include "native_utils.h"
#include "source_engine.h"

#include "se_signature_scanner.h"

namespace source_engine {

	cheat::ErrorCode GetGameModules(std::vector<HMODULE> *game_modules) {
		if (game_modules == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		std::string modules_dir;
		cheat::ErrorCode error = internals::GetModuleDirectory(NULL, &modules_dir);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		const std::string exe_path(modules_dir);
		modules_dir += "\\bin";
		error = internals::GetModulesInDir(modules_dir, game_modules);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		modules_dir = exe_path + "\\" + kGameId + "\\bin";
		error = internals::GetModulesInDir(modules_dir, game_modules);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	SignatureScanner::SignatureScanner(const std::string &cfg_file) : internals::SignatureScanner(cfg_file) {}
	cheat::ErrorCode SignatureScanner::TryLoadingStored(bool *loaded_out) {
		if (loaded_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		std::vector<HMODULE> game_modules;
		cheat::ErrorCode error = GetGameModules(&game_modules);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		error = get_stored_sigs().Read();
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		for (auto &it : game_modules) {
			TCHAR mod_path[MAX_PATH];
			if (!GetModuleFileName(it, mod_path, MAX_PATH)) {
				continue;
			}
			IMAGE_NT_HEADERS nt_headers;
			USES_CONVERSION;
			if (internals::GetNtHeadersFromFile(T2CA(mod_path), &nt_headers) != cheat::CheatErrorCodes::kSuccess) {
				continue;
			}
			std::string mod_filename;
			if (internals::GetModuleFilename(it, &mod_filename) != cheat::CheatErrorCodes::kSuccess) {
				continue;
			}
			mod_timestamps[mod_filename] = nt_headers.FileHeader.TimeDateStamp;
		}
		for (auto &it : mod_timestamps) {
			DWORD stored_time = 0;
			error = get_stored_sigs().GetEntry(it.first + "#time_stamp", &stored_time);
			if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemNotFound) {
				return error;
			}
			if (it.second != stored_time) {
				*loaded_out = false;
				return cheat::CheatErrorCodes::kSuccess;
			}
		}
		return internals::SignatureScanner::TryLoadingStored(loaded_out);
	}
	cheat::ErrorCode SignatureScanner::StoreSignatures() {
		cheat::ErrorCode error = internals::SignatureScanner::StoreSignatures();
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		for (auto &it : mod_timestamps) {
			get_stored_sigs().SetEntry(it.first + "#time_stamp", it.second);
		}
		return get_stored_sigs().Write();
	}

}  // namespace source_engine
