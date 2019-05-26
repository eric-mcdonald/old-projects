#pragma once

#include <map>
#include <string>

#include <Windows.h>

#include "cheat_def.h"
#include "configuration.h"

namespace internals {

	struct SignatureInfo {
		HMODULE module;
		void *start_addr;
		bool rel_base;
		std::string mod_name;  // Eric McDonald: Caches the module name for incase the game has shutdown.
	};
	class SignatureScanner {
		std::map<std::string, SignatureInfo> signatures;
		void *FindSignature(HMODULE, const std::string &, int /*search_offset*/ = 0) const;
		cheat::Configuration stored_sigs;
		DWORD exe_time_stamp;
	public:
		SignatureScanner(const std::string &);
		cheat::ErrorCode RegisterSignature(const std::string &, HMODULE, const std::string &, int /*offset*/ = 0, int /*search_offset*/ = 0);
		cheat::ErrorCode GetSignature(const std::string &, SignatureInfo *) const;
		cheat::Configuration &get_stored_sigs();
		virtual cheat::ErrorCode TryLoadingStored(bool * /*loaded_out*/);
		virtual cheat::ErrorCode StoreSignatures();
		cheat::ErrorCode RegisterSignature(const std::string &, SignatureInfo &);
		template<class ValType>
		cheat::ErrorCode GetSignatureVal(const std::string &key, ValType *val_out) const {
			if (val_out == nullptr) {
				return cheat::CheatErrorCodes::kInvalidParam;
			}
			SignatureInfo info;
			cheat::ErrorCode error = GetSignature(key, &info);
			if (error != cheat::CheatErrorCodes::kSuccess) {
				return error;
			}
			*val_out = reinterpret_cast<ValType>(info.start_addr);
			return cheat::CheatErrorCodes::kSuccess;
		}
	};

	extern SignatureScanner *g_signatures;

}  // namespace internals
