// dllmain.cpp : Defines the entry point for the DLL application.
#include "stdafx.h"

#include "hooks.h"
#include "native_utils.h"
#include "cheat.h"
#include "overlay.h"
#include "cmd_impl.h"
#include "source_engine.h"
#include "se_signature_scanner.h"
#include "logger.h"
#include "cheat_def.h"

namespace internals {

	HMODULE g_this_dll;

}  // namespace internals
namespace cheat {

	InitStates g_init_state = InitStates::kNone;

}  // namespace cheat

FILE *out_file = NULL, *in_file = NULL, *err_file = NULL;

inline void RegisterSignatures(HMODULE this_dll) {
	*cheat::g_info_log << cheat::g_info_log->GetPrefix() << "Registering signatures for " << cheat::kProcName << "..." << std::endl;
	bool loaded;
	cheat::ErrorCode error = internals::g_signatures->TryLoadingStored(&loaded);
	if (error != cheat::CheatErrorCodes::kSuccess) {
		*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to load stored signatures with error code " << error << "." << std::endl;
	}
	//internals::g_signatures->RegisterSignature("base#CheckExternCheats", GetModuleHandle(NULL), "40554155????????????????48????????????33D28D4A02");
	error = source_engine::RegisterSignatures(*internals::g_signatures);
	if (error != cheat::CheatErrorCodes::kSuccess) {
		*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to register signatures for " << source_engine::kEngineName << " with error code " << error << "." << std::endl;
	}
}
inline BOOL InitSourceEngine(HMODULE this_dll) {
	*cheat::g_info_log << cheat::g_info_log->GetPrefix() << "Initializing the API for " << source_engine::kEngineName << "..." << std::endl;
	source_engine::g_net_vars = new source_engine::NetVarRegistry();
	cheat::ErrorCode error = source_engine::Init(*source_engine::g_net_vars);
	if (error != cheat::CheatErrorCodes::kSuccess) {
		*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to initialize the API for " << source_engine::kEngineName << " with error code " << error << "." << std::endl;
		return FALSE;
	}
	return TRUE;
}
cheat::ErrorCode CreateDirs(HMODULE this_dll) {
	std::string path;
	cheat::ErrorCode error = internals::GetModuleDirectory(this_dll, &path);
	if (error != cheat::CheatErrorCodes::kSuccess) {
		return error;
	}
	USES_CONVERSION;
	CreateDirectory(A2CT((path + "\\" + cheat::kDataDir).c_str()), NULL);
	CreateDirectory(A2CT((path + "\\" + cheat::kCfgDir).c_str()), NULL);
	CreateDirectory(A2CT((path + "\\" + cheat::kLogDir).c_str()), NULL);
	return error;
}
BOOL Init(HMODULE this_dll) {
	if (!AllocConsole()) {
		return FALSE;
	}
	freopen_s(&out_file, "CONOUT$", "w", stdout);
	freopen_s(&in_file, "CONIN$", "r", stdin);
	freopen_s(&err_file, "CONOUT$", "w", stderr);
	cheat::ErrorCode dir_err = CreateDirs(this_dll);
	if (dir_err != cheat::CheatErrorCodes::kSuccess) {
		std::cerr << "Failed to create cheat directories with error code " << dir_err << ". Trying anyway..." << std::endl;
	}
	std::string mod_path;
	dir_err = internals::GetModuleDirectory(this_dll, &mod_path);
	if (dir_err != cheat::CheatErrorCodes::kSuccess) {
		std::cerr << "Failed to get the directory of this module with error code " << dir_err << "." << std::endl;
		return FALSE;
	}
	char time_str[256]{ '\0' };
	time_t time_data;
	time(&time_data);
	tm local_time;
	localtime_s(&local_time, &time_data);
	if (strftime(time_str, sizeof(time_str), "%F_%H-%M-%S", &local_time)) {
		cheat::g_info_log = new cheat::Logger(std::cout, cheat::LogLevels::kInfo, mod_path + "\\" + cheat::kLogDir + "\\" + time_str + "_info.txt");
		cheat::g_err_log = new cheat::Logger(std::cerr, cheat::LogLevels::kError, mod_path + "\\" + cheat::kLogDir + "\\" + time_str + "_error.txt");
	}
	else {
		cheat::g_info_log = new cheat::Logger(std::cout, cheat::LogLevels::kInfo);
		cheat::g_err_log = new cheat::Logger(std::cerr, cheat::LogLevels::kError);
	}
	*cheat::g_info_log << cheat::g_info_log->GetPrefix() << "Started initializing " << cheat::kCheatName << " v" << cheat::kCheatVer << "." << std::endl;
	BOOL init_engine = InitSourceEngine(this_dll);
	if (!init_engine) {
		return init_engine;
	}
	std::string sig_path(mod_path);
	sig_path += "\\" + cheat::kDataDir;
	sig_path += "\\signatures.dat";
	internals::g_signatures = new source_engine::SignatureScanner(sig_path);
	RegisterSignatures(this_dll);
	cheat::Cheat::GetInstance().get_general_cfg()->Read();
	cheat::ErrorCode hook_err = internals::HookFuncs(this_dll);
	if (hook_err != cheat::CheatErrorCodes::kSuccess) {
		*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to hook functions with error code " << hook_err << "." << std::endl;
		return FALSE;
	}
	hook_err = source_engine::HookFuncs();
	if (hook_err != cheat::CheatErrorCodes::kSuccess) {
		*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to hook " << source_engine::kEngineName << " functions with error code " << hook_err << "." << std::endl;
		return FALSE;
	}
	*cheat::g_info_log << cheat::g_info_log->GetPrefix() << "Finished loading the cheat DLL." << std::endl;
	cheat::g_init_state = cheat::InitStates::kInit;
	return TRUE;
}
inline void UninitSourceEngine() {
	source_engine::UnhookFuncs();
	if (source_engine::g_net_vars != nullptr) {
		delete source_engine::g_net_vars;
		source_engine::g_net_vars = nullptr;
	}
}
BOOL Uninit(HMODULE this_dll) {
	*cheat::g_info_log << cheat::g_info_log->GetPrefix() << "Shutting down!" << std::endl;
	cheat::ErrorCode error = cheat::Cheat::GetInstance().get_general_cfg()->Write();
	if (error != cheat::CheatErrorCodes::kSuccess) {
		*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to write to " << cheat::Cheat::GetInstance().get_general_cfg()->get_file_path() << " with error code " << error << "." << std::endl;
	}
	error = internals::g_signatures->StoreSignatures();
	if (error != cheat::CheatErrorCodes::kSuccess) {
		*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to store signatures with error code " << error << "." << std::endl;
	}
	if (cheat::g_init_state != cheat::InitStates::kNone) {
		cheat::g_init_state = cheat::InitStates::kUninit;
		cheat::Timer shutdown_timer;
		shutdown_timer.set_start_time();
		while (cheat::g_init_state != cheat::InitStates::kDone) {
			if (shutdown_timer.HasDelayPassed(5000)) {
				cheat::Cheat::GetInstance().Stop();  // Eric McDonald: Forces the cheat to stop to avoid a deadlock in case the game shut down.
				cheat::g_init_state = cheat::InitStates::kDone;
			}
			internals::FixedSleep(1);  // Eric McDonald: Waits for the hooked function to run and stop the cheat.
		}
	}
	UninitSourceEngine();
	internals::UnhookFuncs(this_dll);
	if (internals::g_signatures != nullptr) {
		delete internals::g_signatures;
		internals::g_signatures = nullptr;
	}
	if (cheat::g_err_log != nullptr) {
		delete cheat::g_err_log;
		cheat::g_err_log = nullptr;
	}
	if (cheat::g_info_log != nullptr) {
		delete cheat::g_info_log;
		cheat::g_info_log = nullptr;
	}
	fclose(err_file);
	fclose(in_file);
	fclose(out_file);
	if (!FreeConsole()) {
		return FALSE;
	}
	return TRUE;
}
BOOL APIENTRY DllMain(HMODULE hModule, DWORD ul_reason_for_call, LPVOID lpReserved) {
	DisableThreadLibraryCalls(hModule);
	switch (ul_reason_for_call) {
	case DLL_PROCESS_ATTACH:
		internals::g_this_dll = hModule;
		return Init(hModule);
	case DLL_PROCESS_DETACH:
		internals::g_this_dll = hModule;
		return Uninit(hModule);
	}
	return TRUE;
}

