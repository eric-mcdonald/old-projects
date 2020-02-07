// dllmain.cpp : Defines the entry point for the DLL application.
#include "stdafx.h"

#include "px_def.h"
#include "url_helper.h"
#include "hide_windows_hook.h"

namespace projx {

	HMODULE g_shared_api_dll = NULL;

}  // namespace projx

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
		projx::g_shared_api_dll = hModule;
		DisableThreadLibraryCalls(hModule);
		// Initializes the RNG otherwise rand() produces the same result over and over...
		srand(GetTickCount());
		if (projx::InitUrlApi() != projx::ErrorCodes::kSuccess) {
			return FALSE;
		}
		if (projx::HideWindowsHook::GetInstance().Attach() != projx::ErrorCodes::kSuccess) {
			return FALSE;
		}
		break;
	case DLL_PROCESS_DETACH:
		if (projx::HideWindowsHook::GetInstance().EnsureRemovedCurrentProc() != projx::ErrorCodes::kSuccess) {
			return FALSE;
		}
		if (projx::HideWindowsHook::GetInstance().Detach() != projx::ErrorCodes::kSuccess) {
			return FALSE;
		}
		if (projx::UninitUrlApi() != projx::ErrorCodes::kSuccess) {
			return FALSE;
		}
		break;
	}
	return TRUE;
}

