#pragma once

#include <Windows.h>
#include <unordered_set>

#include "px_def.h"
#include "shared_mem.h"
#include "hooked_func.h"

namespace projx {

	typedef HWND (__stdcall *CreateWindowExAFn)(
		DWORD     dwExStyle,
		LPCSTR    lpClassName,
		LPCSTR    lpWindowName,
		DWORD     dwStyle,
		int       X,
		int       Y,
		int       nWidth,
		int       nHeight,
		HWND      hWndParent,
		HMENU     hMenu,
		HINSTANCE hInstance,
		LPVOID    lpParam
	);
	typedef HWND (__stdcall *CreateWindowExWFn)(
		DWORD     dwExStyle,
		LPCWSTR   lpClassName,
		LPCWSTR   lpWindowName,
		DWORD     dwStyle,
		int       X,
		int       Y,
		int       nWidth,
		int       nHeight,
		HWND      hWndParent,
		HMENU     hMenu,
		HINSTANCE hInstance,
		LPVOID    lpParam
	);
	typedef BOOL (__stdcall *ShowWindowFn)(
		HWND hWnd,
		int  nCmdShow
	);
	typedef BOOL (__stdcall *ShowWindowAsyncFn)(
		HWND hWnd,
		int  nCmdShow
	);
	typedef BOOL (__stdcall *SetWindowPosFn)(
		HWND hWnd,
		HWND hWndInsertAfter,
		int  X,
		int  Y,
		int  cx,
		int  cy,
		UINT uFlags
	);
	typedef LONG (__stdcall *SetWindowLongAFn)(
		HWND hWnd,
		int  nIndex,
		LONG dwNewLong
	);
	typedef LONG(__stdcall *SetWindowLongWFn)(
		HWND hWnd,
		int  nIndex,
		LONG dwNewLong
	);
	typedef BOOL (__stdcall *AnimateWindowFn)(
		HWND  hWnd,
		DWORD dwTime,
		DWORD dwFlags
	);
	struct PROJECTXSHARED_API HideWindowsHook {
	public:
		HookedFunc<CreateWindowExAFn> create_wnd_exa_hk;
		HookedFunc<CreateWindowExWFn> create_wnd_exw_hk;
		HookedFunc<ShowWindowFn> show_wnd_hk;
		HookedFunc<ShowWindowAsyncFn> show_wnd_async_hk;
		HookedFunc<SetWindowPosFn> set_wnd_pos_hk;
		HookedFunc<SetWindowLongAFn> set_wnd_longa_hk;
		HookedFunc<SetWindowLongWFn> set_wnd_longw_hk;
		HookedFunc<AnimateWindowFn> anim_wnd_hk;
	private:
		SharedMemoryManager target_procs;
		bool added_current_proc;
	private:
		HideWindowsHook();
		~HideWindowsHook();
	public:
		HideWindowsHook(const HideWindowsHook &) = delete;
		HideWindowsHook(HideWindowsHook &&) = delete;
		static HideWindowsHook &GetInstance();
		ProgError AddTargetProc(std::string /*proc_name*/);
		ProgError RemoveTargetProc(std::string /*proc_name*/);
		ProgError GetTargetProcs(std::unordered_set<std::string> * /*targets_out*/) const;
		ProgError Attach();
		ProgError Detach();
		ProgError EnsureRemovedCurrentProc();
		ProgError AddCurrentProc();
		HideWindowsHook &operator=(const HideWindowsHook &) = delete;
		HideWindowsHook &operator=(HideWindowsHook &&) = delete;
	};

}  // namespace projx
