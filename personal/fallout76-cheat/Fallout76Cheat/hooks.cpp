#include "stdafx.h"

#include "hooks.h"
#include "cheat.h"
#include "cheat_def.h"
#include "native_utils.h"

#pragma comment(lib, "Winmm.lib")

namespace internals {

	GetTickCountFn g_get_real_tick_count = GetTickCount;
	GetTickCount64Fn g_get_real_tick_count64 = GetTickCount64;
	QueryPerformanceCounterFn g_query_real_perf_count = QueryPerformanceCounter;
	timeGetTimeFn g_real_time_get_time = timeGetTime;
	//CreateToolhelp32SnapshotFn g_real_create_toolhelp32snapshot = CreateToolhelp32Snapshot;
	//Process32FirstWFn g_real_proc_first = Process32FirstW;
	static DWORD base_tick_count;
	static DWORD base_time;
	static ULONGLONG base_tick_count64;
	static LARGE_INTEGER base_perf_count;

	void ProcessDllAction() {
		if (cheat::g_init_state != cheat::InitStates::kNone && cheat::g_init_state != cheat::InitStates::kDone) {
			cheat::ErrorCode error = cheat::CheatErrorCodes::kSuccess;
			switch (cheat::g_init_state) {
			case cheat::InitStates::kInit:
				cheat::g_init_state = cheat::InitStates::kDone;  // Eric McDonald: Prevents the start function from being called twice.
				error = cheat::Cheat::GetInstance().Start();
				if (error != cheat::CheatErrorCodes::kSuccess) {
					*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to start the cheat with error code " << error << "." << std::endl;
				}
				else {
					*cheat::g_info_log << cheat::g_info_log->GetPrefix() << "Cheat successfully initialized." << std::endl;
					cheat::Cheat::GetInstance().StartCmdHandler();
					//internals::g_signatures->StoreSignatures();  // TODO(Eric McDonald): Comment this line out when you're done testing.
				}
				break;
			case cheat::InitStates::kUninit:
				cheat::g_init_state = cheat::InitStates::kDone;
				cheat::Cheat::GetInstance().Stop();
				break;
			}
			cheat::g_init_state = cheat::InitStates::kDone;
		}
	}
	DWORD WINAPI HGetTickCount() {
		ProcessDllAction();
		if (cheat::Cheat::GetInstance().get_speed() == 1.0F) {
			return base_tick_count = g_get_real_tick_count();
		}
		return static_cast<DWORD>(base_tick_count + (g_get_real_tick_count() - base_tick_count) * cheat::Cheat::GetInstance().get_speed());
	}
	DWORD WINAPI HtimeGetTime() {
		ProcessDllAction();
		if (cheat::Cheat::GetInstance().get_speed() == 1.0F) {
			return base_time = g_real_time_get_time();
		}
		return static_cast<DWORD>(base_time + (g_real_time_get_time() - base_time) * cheat::Cheat::GetInstance().get_speed());
	}
	ULONGLONG WINAPI HGetTickCount64() {
		ProcessDllAction();
		if (cheat::Cheat::GetInstance().get_speed() == 1.0F) {
			return base_tick_count64 = g_get_real_tick_count64();
		}
		return static_cast<ULONGLONG>(base_tick_count64 + (g_get_real_tick_count64() - base_tick_count64) * cheat::Cheat::GetInstance().get_speed());
	}
	BOOL WINAPI HQueryPerformanceCounter(_Out_ LARGE_INTEGER *lpPerformanceCount) {
		ProcessDllAction();
		LARGE_INTEGER perf_count;
		BOOL success = g_query_real_perf_count(&perf_count);
		if (!success) {
			*lpPerformanceCount = base_perf_count = perf_count;
			return success;
		}
		if (cheat::Cheat::GetInstance().get_speed() == 1.0F) {
			*lpPerformanceCount = base_perf_count = perf_count;
			return success;
		}
		LARGE_INTEGER res_count;
		res_count.QuadPart = static_cast<LONGLONG>(base_perf_count.QuadPart + (perf_count.QuadPart - base_perf_count.QuadPart) * cheat::Cheat::GetInstance().get_speed());
		*lpPerformanceCount = res_count;
		return success;
	}
	/*HANDLE WINAPI HCreateToolhelp32Snapshot(DWORD dwFlags, DWORD th32ProcessID) {
		return dwFlags & TH32CS_SNAPPROCESS ? INVALID_HANDLE_VALUE : g_real_create_toolhelp32snapshot(dwFlags, th32ProcessID);
	}*/
	/*BOOL WINAPI HProcess32FirstW(HANDLE hSnapshot, LPPROCESSENTRY32 lppe) {
		return FALSE;
	}*/
	RedirectFuncErr AttachFunc(void **original_func, void *new_func) {
		/*size_t func_sz;
		if (GetFuncSize(original_func, NULL, &func_sz) != cheat::CheatErrorCodes::kSuccess) {
			return ERROR_INVALID_OPERATION;
		}
		DWORD old_protect;
		if (!VirtualProtect(original_func, func_sz, PAGE_EXECUTE_READWRITE, &old_protect)) {
			return GetLastError();
		}*/
		DetourTransactionBegin();
		DetourUpdateThread(GetCurrentThread());
		DetourAttach(original_func, new_func);
		/*DWORD dw;
		VirtualProtect(original_func, func_sz, old_protect, &dw);*/
		return DetourTransactionCommit();
	}
	RedirectFuncErr DetachFunc(void **original_func, void *new_func) {
		DetourTransactionBegin();
		DetourUpdateThread(GetCurrentThread());
		DetourDetach(&reinterpret_cast<PVOID&>(original_func), new_func);
		return DetourTransactionCommit();
	}
	cheat::ErrorCode HookFuncs(HMODULE this_dll) {
		base_tick_count = g_get_real_tick_count();
		base_time = g_real_time_get_time();
		base_tick_count64 = g_get_real_tick_count64();
		g_query_real_perf_count(&base_perf_count);
		RedirectFuncErr detours_err = AttachFunc(&reinterpret_cast<void*&>(g_get_real_tick_count), HGetTickCount);
		if (detours_err != NO_ERROR) {
			return detours_err;
		}
		detours_err = AttachFunc(&reinterpret_cast<void*&>(g_real_time_get_time), HtimeGetTime);
		if (detours_err != NO_ERROR) {
			return detours_err;
		}
		detours_err = AttachFunc(&reinterpret_cast<void*&>(g_get_real_tick_count64), HGetTickCount64);
		if (detours_err != NO_ERROR) {
			return detours_err;
		}
		detours_err = AttachFunc(&reinterpret_cast<void*&>(g_query_real_perf_count), HQueryPerformanceCounter);
		if (detours_err != NO_ERROR) {
			return detours_err;
		}

		/*DetourTransactionBegin();
		DetourUpdateThread(GetCurrentThread());
		DetourAttach(&reinterpret_cast<PVOID&>(g_real_create_toolhelp32snapshot), HCreateToolhelp32Snapshot);
		detours_err = DetourTransactionCommit();
		if (detours_err != NO_ERROR) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}*/

		/*DetourTransactionBegin();
		DetourUpdateThread(GetCurrentThread());
		DetourAttach(&reinterpret_cast<PVOID&>(g_real_proc_first), HProcess32FirstW);
		detours_err = DetourTransactionCommit();
		if (detours_err != NO_ERROR) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}*/
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode UnhookFuncs(HMODULE this_dll) {
		RedirectFuncErr detours_err = DetachFunc(&reinterpret_cast<void*&>(g_get_real_tick_count), HGetTickCount);
		if (detours_err != NO_ERROR) {
			return detours_err;
		}
		detours_err = DetachFunc(&reinterpret_cast<void*&>(g_real_time_get_time), HtimeGetTime);
		if (detours_err != NO_ERROR) {
			return detours_err;
		}
		detours_err = DetachFunc(&reinterpret_cast<void*&>(g_get_real_tick_count64), HGetTickCount64);
		if (detours_err != NO_ERROR) {
			return detours_err;
		}
		detours_err = DetachFunc(&reinterpret_cast<void*&>(g_query_real_perf_count), HQueryPerformanceCounter);
		if (detours_err != NO_ERROR) {
			return detours_err;
		}

		/*DetourTransactionBegin();
		DetourUpdateThread(GetCurrentThread());
		DetourDetach(&reinterpret_cast<PVOID&>(g_real_create_toolhelp32snapshot), HCreateToolhelp32Snapshot);
		detours_err = DetourTransactionCommit();
		if (detours_err != NO_ERROR) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}*/

		/*DetourTransactionBegin();
		DetourUpdateThread(GetCurrentThread());
		DetourDetach(&reinterpret_cast<PVOID&>(g_real_proc_first), HProcess32FirstW);
		detours_err = DetourTransactionCommit();
		if (detours_err != NO_ERROR) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}*/
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode SwapVirtualFunc(const VtableFuncInfo &func_info, VtableFuncInfo *func_inf_out) {
		if (func_info.vtable == nullptr || func_info.func_ptr == nullptr || func_inf_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		DWORD old_protect;
		if (!VirtualProtect(func_info.vtable, (func_info.func_idx + 1) * sizeof(func_info.func_ptr), PAGE_EXECUTE_READWRITE, &old_protect)) {
			return GetLastError();
		}
		func_inf_out->vtable = func_info.vtable;
		func_inf_out->func_idx = func_info.func_idx;
		func_inf_out->func_ptr = func_info.vtable[func_info.func_idx];
		func_info.vtable[func_info.func_idx] = func_info.func_ptr;
		DWORD dw;
		VirtualProtect(func_info.vtable, (func_info.func_idx + 1) * sizeof(func_info.func_ptr), old_protect, &dw);
		return cheat::CheatErrorCodes::kSuccess;
	}

}  // namespace internals
