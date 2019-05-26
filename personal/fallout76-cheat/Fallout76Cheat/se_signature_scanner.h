/*
Eric McDonald: Checks the TimeDateStamp of all modules that the game owns in order to load the stored signatures, as 
some games (such as CS:GO) seems to be updated on a per-module basis.
*/

#pragma once

#include "signatures.h"

namespace source_engine {

	class SignatureScanner : public internals::SignatureScanner {
		std::map<std::string, DWORD> mod_timestamps;
	public:
		SignatureScanner(const std::string &);
		cheat::ErrorCode TryLoadingStored(bool * /*loaded_out*/) override;
		cheat::ErrorCode StoreSignatures() override;
	};

}  // namespace source_engine
