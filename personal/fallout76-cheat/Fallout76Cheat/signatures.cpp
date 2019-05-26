#include "stdafx.h"

#include "signatures.h"
#include "native_utils.h"
#include "string_utils.h"

namespace internals {

	static constexpr char *kSigInfSep = ",";
	SignatureScanner *g_signatures = nullptr;

	cheat::ErrorCode GetExeTimeStamp(DWORD *time_stamp_out) {
		if (time_stamp_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		TCHAR exe_path[MAX_PATH];
		if (!GetModuleFileName(NULL, exe_path, MAX_PATH)) {
			return GetLastError();
		}
		IMAGE_NT_HEADERS nt_headers;
		USES_CONVERSION;
		cheat::ErrorCode error = GetNtHeadersFromFile(T2CA(exe_path), &nt_headers);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		*time_stamp_out = nt_headers.FileHeader.TimeDateStamp;
		return cheat::CheatErrorCodes::kSuccess;
	}
	void *SignatureScanner::FindSignature(HMODULE module, const std::string &signature, int search_offset) const {
		if (signature.length() % 2 != 0) {
			return nullptr;
		}
		MODULEINFO mod_info = { 0 };
		if (!GetModuleInformation(GetCurrentProcess(), module, &mod_info, sizeof(MODULEINFO))) {
			return nullptr;
		}
		for (DWORD_PTR current_addr = reinterpret_cast<DWORD_PTR>(mod_info.lpBaseOfDll) + search_offset; current_addr < reinterpret_cast<DWORD_PTR>(mod_info.lpBaseOfDll) + mod_info.SizeOfImage; ++current_addr) {
			bool matches = true;
			for (size_t i = 0; i < signature.length() - 1; i += 2) {
				std::string byte_str = signature.substr(i, 2);
				if (cheat::StrContains(byte_str, "?")) {
					continue;
				}
				DWORD old_protect;
				if (!VirtualProtect(reinterpret_cast<LPVOID>(current_addr), 1, PAGE_EXECUTE_READWRITE, &old_protect)) {
					continue;
				}
				BYTE read_byte = *reinterpret_cast<BYTE*>(current_addr + i / 2);
				DWORD dw;
				VirtualProtect(reinterpret_cast<LPVOID>(current_addr), 1, old_protect, &dw);
				unsigned long sig_byte = stoul("0x" + byte_str, nullptr, 16);
				if (read_byte != sig_byte) {
					matches = false;
					break;
				}
			}
			if (matches) {
				return reinterpret_cast<void*>(current_addr);
			}
		}
		return nullptr;
	}
	SignatureScanner::SignatureScanner(const std::string &cfg_file) : stored_sigs(cfg_file), exe_time_stamp(0) {
		GetExeTimeStamp(&exe_time_stamp);
	}
	cheat::ErrorCode SignatureScanner::RegisterSignature(const std::string &key, HMODULE module, const std::string &signature, int offset, int search_offset) {
		const auto &found_signature = signatures.find(key);
		if (found_signature != signatures.end()) {
			return cheat::CheatErrorCodes::kElemAlreadyRegistered;
		}
		void *sig_addr = FindSignature(module, signature, search_offset);
		if (sig_addr == nullptr) {
			return cheat::CheatErrorCodes::kSigNotFound;
		}
		SignatureInfo info;
		info.module = module;
		std::string mod_name;
		if (GetModuleFilename(module, &mod_name) == cheat::CheatErrorCodes::kSuccess) {
			info.mod_name = mod_name;
		}
		info.start_addr = reinterpret_cast<void*>(reinterpret_cast<DWORD_PTR>(sig_addr) + offset);
		info.rel_base = true;
		signatures[key] = info;
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode SignatureScanner::GetSignature(const std::string &key, SignatureInfo *signature_info) const {
		const auto &signature = signatures.find(key);
		if (signature == signatures.end()) {
			return cheat::CheatErrorCodes::kSigNotFound;
		}
		*signature_info = signature->second;
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::Configuration &SignatureScanner::get_stored_sigs() {
		return stored_sigs;
	}
	cheat::ErrorCode SignatureScanner::TryLoadingStored(bool *loaded_out) {
		if (loaded_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		cheat::ErrorCode error = GetExeTimeStamp(&exe_time_stamp);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		error = stored_sigs.Read();
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		DWORD stored_time = 0;
		cheat::ErrorCode time_err = stored_sigs.GetEntry("exe_time_stamp", &stored_time);
		if (time_err != cheat::CheatErrorCodes::kSuccess && time_err != cheat::CheatErrorCodes::kElemNotFound) {
			return error = time_err;
		}
		if (exe_time_stamp == stored_time) {
			for (const auto &it : stored_sigs.get_loaded_entries()) {
				if (it.first == "exe_time_stamp") {
					continue;
				}
				size_t sep_idx = it.second.find_first_of(kSigInfSep);
				if (sep_idx == std::string::npos || sep_idx == it.second.length() - 1) {
					continue;
				}
				std::vector<std::string> split_vals;
				cheat::ErrorCode split_err = cheat::StrSplit(it.second, kSigInfSep, &split_vals);
				if (split_err != cheat::CheatErrorCodes::kSuccess) {
					*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to parse entry: " << it.first << " in file " << stored_sigs.get_file_path() << " with error code " << split_err << ". Skipping..." << std::endl;
					continue;
				}
				bool rel_base = split_vals[0] != "0";
				USES_CONVERSION;
				HMODULE mod_handle = GetModuleHandle(A2CW(split_vals[1].c_str()));
				if (!mod_handle) {
					*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to parse entry: " << it.first << " in file " << stored_sigs.get_file_path() << ". Skipping..." << std::endl;
					continue;
				}
				SignatureInfo info;
				info.rel_base = rel_base;
				info.module = mod_handle;
				std::string mod_name;
				if (GetModuleFilename(mod_handle, &mod_name) == cheat::CheatErrorCodes::kSuccess) {
					info.mod_name = mod_name;
				}
				std::istringstream sig_addr_stream(split_vals[2]);
				sig_addr_stream >> info.start_addr;
				if (rel_base) {
					info.start_addr = reinterpret_cast<void*>(reinterpret_cast<DWORD_PTR>(info.start_addr) + reinterpret_cast<DWORD_PTR>(mod_handle));
				}
				signatures[it.first] = info;
			}
			*loaded_out = true;
		}
		else {
			*loaded_out = false;
		}
		return error;
	}
	cheat::ErrorCode SignatureScanner::StoreSignatures() {
		stored_sigs.SetEntry("exe_time_stamp", exe_time_stamp);
		for (const auto &it : signatures) {
			if (it.first == "exe_time_stamp") {
				continue;
			}
			std::ostringstream mod_inf_stream;
			mod_inf_stream << it.second.rel_base << kSigInfSep << it.second.mod_name << kSigInfSep << std::hex << (it.second.rel_base ? reinterpret_cast<DWORD_PTR>(it.second.start_addr) - reinterpret_cast<DWORD_PTR>(it.second.module) : reinterpret_cast<DWORD_PTR>(it.second.start_addr));
			stored_sigs.SetEntry(it.first, mod_inf_stream.str());
		}
		return stored_sigs.Write();
	}
	cheat::ErrorCode SignatureScanner::RegisterSignature(const std::string &key, SignatureInfo &info) {
		const auto &found_signature = signatures.find(key);
		if (found_signature != signatures.end()) {
			return cheat::CheatErrorCodes::kElemAlreadyRegistered;
		}
		std::string mod_name;
		if (GetModuleFilename(info.module, &mod_name) == cheat::CheatErrorCodes::kSuccess) {
			info.mod_name = mod_name;
		}
		signatures[key] = info;
		return cheat::CheatErrorCodes::kSuccess;
	}

}  // namespace internals
