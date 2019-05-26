#pragma once

#include <Windows.h>

namespace internals {

	typedef DWORD(WINAPI *GetTickCountFn)();
	typedef DWORD(WINAPI *timeGetTimeFn)();
	typedef ULONGLONG(WINAPI *GetTickCount64Fn)();
	typedef BOOL(WINAPI *QueryPerformanceCounterFn)(_Out_ LARGE_INTEGER *);
	typedef HANDLE(WINAPI *CreateToolhelp32SnapshotFn)(DWORD, DWORD);
	typedef BOOL(WINAPI *Process32FirstWFn)(HANDLE, LPPROCESSENTRY32W);
	typedef LONG RedirectFuncErr;
	struct VtableFuncInfo {
		void **vtable;
		size_t func_idx;
		void *func_ptr;
	};

	extern GetTickCountFn g_get_real_tick_count;
	extern GetTickCount64Fn g_get_real_tick_count64;
	extern QueryPerformanceCounterFn g_query_real_perf_count;
	extern timeGetTimeFn g_real_time_get_time;
	//extern CreateToolhelp32SnapshotFn g_real_create_toolhelp32snapshot;
	//extern Process32FirstWFn g_real_proc_first;

	cheat::ErrorCode HookFuncs(HMODULE /*this_dll*/);
	cheat::ErrorCode UnhookFuncs(HMODULE /*this_dll*/);
	cheat::ErrorCode SwapVirtualFunc(const VtableFuncInfo &, VtableFuncInfo *);
	RedirectFuncErr AttachFunc(void ** /*original_func*/, void * /*new_func*/);
	RedirectFuncErr DetachFunc(void ** /*original_func*/, void * /*new_func*/);

}  // namespace internals
