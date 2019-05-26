/*
 * org_bitbucket_reliant_memory_MemoryStream.cc
 *
 *  Created on: Dec 16, 2016
 *      Author: Eric McDonald
 */




#include "org_bitbucket_reliant_memory_MemoryStream.h"

#include <windows.h>
#include <tlhelp32.h>
#include <psapi.h>
//#include <winternl.h>
#include <map>
//#include <iostream>

typedef std::map<jobject, DWORD> ProcessIdMap;

ProcessIdMap process_id_map;

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    clientCmd
 * Signature: (JJLjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_clientCmd
  (JNIEnv *env, jobject this_obj, jlong process_handle, jlong engine, jstring command) {
	HANDLE process = reinterpret_cast<HANDLE>(process_handle);
	char *cmd_native = const_cast<char*>(env->GetStringUTFChars(command, JNI_FALSE));
	LPVOID cmd_ptr = VirtualAllocEx(process, NULL, strlen(cmd_native) + 1, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
	if (!cmd_ptr) {
		env->ReleaseStringUTFChars(command, cmd_native);
		return;
	}
	if (!WriteProcessMemory(process, cmd_ptr, cmd_native, strlen(cmd_native) + 1, NULL)) {
		VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(command, cmd_native);
		return;
	}
	BYTE engine_bytes[sizeof(DWORD)];
	memcpy(engine_bytes, &engine, sizeof(engine_bytes));
	BYTE cmd_ptr_bytes[sizeof(DWORD)];
	memcpy(cmd_ptr_bytes, &cmd_ptr, sizeof(cmd_ptr_bytes));
	BYTE shellcode[17] = {
			0x8B, 0x0D, engine_bytes[0], engine_bytes[1], engine_bytes[2], engine_bytes[3],
			0x68, cmd_ptr_bytes[0], cmd_ptr_bytes[1], cmd_ptr_bytes[2], cmd_ptr_bytes[3],
			0x8B, 0x01,
			0xFF, 0x50, 0x1C,
			0xC3
	};
	LPVOID shellcode_ptr = VirtualAllocEx(process, NULL, sizeof(shellcode), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
	if (!shellcode_ptr) {
		VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(command, cmd_native);
		return;
	}
	if (!WriteProcessMemory(process, shellcode_ptr, shellcode, sizeof(shellcode), NULL)) {
		VirtualFreeEx(process, shellcode_ptr, 0, MEM_RELEASE);
		VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(command, cmd_native);
		return;
	}
	HANDLE exec_thread = CreateRemoteThread(process, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(shellcode_ptr), NULL, 0, NULL);
	if (!exec_thread) {
		VirtualFreeEx(process, shellcode_ptr, 0, MEM_RELEASE);
		VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(command, cmd_native);
		return;
	}
	if (WaitForSingleObject(exec_thread, INFINITE) != WAIT_OBJECT_0) {
		CloseHandle(exec_thread);
		VirtualFreeEx(process, shellcode_ptr, 0, MEM_RELEASE);
		VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(command, cmd_native);
	}
	CloseHandle(exec_thread);
	VirtualFreeEx(process, shellcode_ptr, 0, MEM_RELEASE);
	VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
	env->ReleaseStringUTFChars(command, cmd_native);
}

JNIEXPORT void JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_clientCmdUnrestricted
  (JNIEnv *env, jobject this_obj, jlong process_handle, jlong engine, jstring command, jbyte param) {
	HANDLE process = reinterpret_cast<HANDLE>(process_handle);
	char *cmd_native = const_cast<char*>(env->GetStringUTFChars(command, JNI_FALSE));
	LPVOID cmd_ptr = VirtualAllocEx(process, NULL, strlen(cmd_native) + 1, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
	if (!cmd_ptr) {
		env->ReleaseStringUTFChars(command, cmd_native);
		return;
	}
	if (!WriteProcessMemory(process, cmd_ptr, cmd_native, strlen(cmd_native) + 1, NULL)) {
		VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(command, cmd_native);
		return;
	}
	BYTE engine_bytes[sizeof(DWORD)];
	memcpy(engine_bytes, &engine, sizeof(engine_bytes));
	BYTE cmd_ptr_bytes[sizeof(DWORD)];
	memcpy(cmd_ptr_bytes, &cmd_ptr, sizeof(cmd_ptr_bytes));
	BYTE ecx_offset_bytes[sizeof(DWORD)];
	DWORD ecx_offset = 0x1C8;
	memcpy(ecx_offset_bytes, &ecx_offset, sizeof(ecx_offset_bytes));
	BYTE shellcode[22] = {
			0x6A, static_cast<BYTE>(param),
			0x8B, 0x0D, engine_bytes[0], engine_bytes[1], engine_bytes[2], engine_bytes[3],
			0x68, cmd_ptr_bytes[0], cmd_ptr_bytes[1], cmd_ptr_bytes[2], cmd_ptr_bytes[3],
			0x8B, 0x01,
			0xFF, 0x90, ecx_offset_bytes[0], ecx_offset_bytes[1], ecx_offset_bytes[2], ecx_offset_bytes[3],
			0xC3
	};
	LPVOID shellcode_ptr = VirtualAllocEx(process, NULL, sizeof(shellcode), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
	if (!shellcode_ptr) {
		VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(command, cmd_native);
		return;
	}
	if (!WriteProcessMemory(process, shellcode_ptr, shellcode, sizeof(shellcode), NULL)) {
		VirtualFreeEx(process, shellcode_ptr, 0, MEM_RELEASE);
		VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(command, cmd_native);
		return;
	}
	HANDLE exec_thread = CreateRemoteThread(process, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(shellcode_ptr), NULL, 0, NULL);
	if (!exec_thread) {
		VirtualFreeEx(process, shellcode_ptr, 0, MEM_RELEASE);
		VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(command, cmd_native);
		return;
	}
	if (WaitForSingleObject(exec_thread, INFINITE) != WAIT_OBJECT_0) {
		CloseHandle(exec_thread);
		VirtualFreeEx(process, shellcode_ptr, 0, MEM_RELEASE);
		VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(command, cmd_native);
	}
	CloseHandle(exec_thread);
	VirtualFreeEx(process, shellcode_ptr, 0, MEM_RELEASE);
	VirtualFreeEx(process, cmd_ptr, 0, MEM_RELEASE);
	env->ReleaseStringUTFChars(command, cmd_native);
}

/*int WINAPI SetClanTag(const char *tag) {
	typedef int (__fastcall *SetClanTagFunc)(const char *, const char *);
	return reinterpret_cast<SetClanTagFunc>(0xFEEDFACE)(tag, "");
}*/

/*DWORD WINAPI CallFuncSafe(LPVOID address) {
	CONST HANDLE thread_snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPTHREAD, 0);
	if (thread_snapshot == INVALID_HANDLE_VALUE) {
		return 0;
	}
	THREADENTRY32 thread_entry;
	thread_entry.dwSize = sizeof(THREADENTRY32);
	if (!Thread32First(thread_snapshot, &thread_entry)) {
		return 0;
	}
	CONST DWORD current_proc_id = GetCurrentProcessId();
	HANDLE main_thread = NULL;
	do {
		if (thread_entry.th32OwnerProcessID == current_proc_id) {
			main_thread = OpenThread(THREAD_QUERY_INFORMATION, FALSE, thread_entry.th32ThreadID);
			break;
		}
	} while (Thread32Next(thread_snapshot, &thread_entry));
	if (!CloseHandle(thread_snapshot) || main_thread == NULL) {
		return 0;
	}
	typedef NTSTATUS (WINAPI *NtQueryInformationThreadFunc)(HANDLE, THREADINFOCLASS, PVOID, ULONG, PULONG);
	typedef struct _THREAD_BASIC_INFORMATION {
		NTSTATUS                ExitStatus;
		PVOID                   TebBaseAddress;
		CLIENT_ID               ClientId;
		KAFFINITY               AffinityMask;
		KPRIORITY               Priority;
		KPRIORITY               BasePriority;
	} THREAD_BASIC_INFORMATION, *PTHREAD_BASIC_INFORMATION;
	THREAD_BASIC_INFORMATION main_thread_info = {0};
	CONST HMODULE ntdll_handle = GetModuleHandle("ntdll.dll");
	if (ntdll_handle == NULL) {
		return 0;
	}
	reinterpret_cast<NtQueryInformationThreadFunc>(GetProcAddress(ntdll_handle, "NtQueryInformationThread"))(main_thread, ThreadBasicInformation, &main_thread_info, sizeof(main_thread_info), NULL);
	if (!CloseHandle(main_thread) || main_thread_info.TebBaseAddress == NULL) {
		return 0;
	}
	typedef struct _TEB {
		NT_TIB                  Tib;
		PVOID                   EnvironmentPointer;
		CLIENT_ID               Cid;
		PVOID                   ActiveRpcInfo;
		PVOID                   ThreadLocalStoragePointer;
		PPEB                    Peb;
		ULONG                   LastErrorValue;
		ULONG                   CountOfOwnedCriticalSections;
		PVOID                   CsrClientThread;
		PVOID                   Win32ThreadInfo;
		ULONG                   Win32ClientInfo[0x1F];
		PVOID                   WOW32Reserved;
		ULONG                   CurrentLocale;
		ULONG                   FpSoftwareStatusRegister;
		PVOID                   SystemReserved1[0x36];
		PVOID                   Spare1;
		ULONG                   ExceptionCode;
		ULONG                   SpareBytes1[0x28];
		PVOID                   SystemReserved2[0xA];
		ULONG                   GdiRgn;
		ULONG                   GdiPen;
		ULONG                   GdiBrush;
		CLIENT_ID               RealClientId;
		PVOID                   GdiCachedProcessHandle;
		ULONG                   GdiClientPID;
		ULONG                   GdiClientTID;
		PVOID                   GdiThreadLocaleInfo;
		PVOID                   UserReserved[5];
		PVOID                   GlDispatchTable[0x118];
		ULONG                   GlReserved1[0x1A];
		PVOID                   GlReserved2;
		PVOID                   GlSectionInfo;
		PVOID                   GlSection;
		PVOID                   GlTable;
		PVOID                   GlCurrentRC;
		PVOID                   GlContext;
		NTSTATUS                LastStatusValue;
		UNICODE_STRING          StaticUnicodeString;
		WCHAR                   StaticUnicodeBuffer[0x105];
		PVOID                   DeallocationStack;
		PVOID                   TlsSlots[0x40];
		LIST_ENTRY              TlsLinks;
		PVOID                   Vdm;
		PVOID                   ReservedForNtRpc;
		PVOID                   DbgSsReserved[0x2];
		ULONG                   HardErrorDisabled;
		PVOID                   Instrumentation[0x10];
		PVOID                   WinSockData;
		ULONG                   GdiBatchCount;
		ULONG                   Spare2;
		ULONG                   Spare3;
		ULONG                   Spare4;
		PVOID                   ReservedForOle;
		ULONG                   WaitingOnLoaderLock;
		PVOID                   StackCommit;
		PVOID                   StackCommitMax;
		PVOID                   StackReserved;
	} TEB, *PTEB;
	TEB *main_teb = reinterpret_cast<TEB*>(main_thread_info.TebBaseAddress), *current_teb = reinterpret_cast<TEB*>(NtCurrentTeb());
	const size_t tls_slots_sz = 0x40;
	PVOID prev_tls_slots[tls_slots_sz] = {0};
	CONST FARPROC acquire_peb_lock = GetProcAddress(ntdll_handle, "RtlAcquirePebLock"), release_peb_lock = GetProcAddress(ntdll_handle, "RtlReleasePebLock");
	for (size_t i = 0; i < tls_slots_sz; ++i) {
		prev_tls_slots[i] = current_teb->TlsSlots[i];
		current_teb->TlsSlots[i] = main_teb->TlsSlots[i];
		acquire_peb_lock();
		int arrIdx = 0, index = i;
		if (index > 31) {
			arrIdx = 1;
			index -= 32;
		}
		typedef void (*PPEBLOCKROUTINE)(
		PVOID PebLock
		);
		typedef struct _PEB_FREE_BLOCK {
		  _PEB_FREE_BLOCK          *Next;
		  ULONG                   Size;
		} PEB_FREE_BLOCK, *PPEB_FREE_BLOCK;
		typedef struct _PEB {
		  BOOLEAN                 InheritedAddressSpace;
		  BOOLEAN                 ReadImageFileExecOptions;
		  BOOLEAN                 BeingDebugged;
		  BOOLEAN                 Spare;
		  HANDLE                  Mutant;
		  PVOID                   ImageBaseAddress;
		  PPEB_LDR_DATA           LoaderData;
		  PRTL_USER_PROCESS_PARAMETERS ProcessParameters;
		  PVOID                   SubSystemData;
		  PVOID                   ProcessHeap;
		  PVOID                   FastPebLock;
		  PPEBLOCKROUTINE         FastPebLockRoutine;
		  PPEBLOCKROUTINE         FastPebUnlockRoutine;
		  ULONG                   EnvironmentUpdateCount;
		  PVOID                  *KernelCallbackTable;
		  PVOID                   EventLogSection;
		  PVOID                   EventLog;
		  PPEB_FREE_BLOCK         FreeList;
		  ULONG                   TlsExpansionCounter;
		  PVOID                   TlsBitmap;
		  ULONG                   TlsBitmapBits[0x2];
		  PVOID                   ReadOnlySharedMemoryBase;
		  PVOID                   ReadOnlySharedMemoryHeap;
		  PVOID                  *ReadOnlyStaticServerData;
		  PVOID                   AnsiCodePageData;
		  PVOID                   OemCodePageData;
		  PVOID                   UnicodeCaseTableData;
		  ULONG                   NumberOfProcessors;
		  ULONG                   NtGlobalFlag;
		  BYTE                    Spare2[0x4];
		  LARGE_INTEGER           CriticalSectionTimeout;
		  ULONG                   HeapSegmentReserve;
		  ULONG                   HeapSegmentCommit;
		  ULONG                   HeapDeCommitTotalFreeThreshold;
		  ULONG                   HeapDeCommitFreeBlockThreshold;
		  ULONG                   NumberOfHeaps;
		  ULONG                   MaximumNumberOfHeaps;
		  PVOID                  **ProcessHeaps;
		  PVOID                   GdiSharedHandleTable;
		  PVOID                   ProcessStarterHelper;
		  PVOID                   GdiDCAttributeList;
		  PVOID                   LoaderLock;
		  ULONG                   OSMajorVersion;
		  ULONG                   OSMinorVersion;
		  ULONG                   OSBuildNumber;
		  ULONG                   OSPlatformId;
		  ULONG                   ImageSubSystem;
		  ULONG                   ImageSubSystemMajorVersion;
		  ULONG                   ImageSubSystemMinorVersion;
		  ULONG                   GdiHandleBuffer[0x22];
		  ULONG                   PostProcessInitRoutine;
		  ULONG                   TlsExpansionBitmap;
		  BYTE                    TlsExpansionBitmapBits[0x80];
		  ULONG                   SessionId;
		} PEB, *PPEB;
		_bittestandset(reinterpret_cast<LONG*>(&reinterpret_cast<PEB*>(current_teb->Peb)->TlsBitmapBits[arrIdx]), index);
		release_peb_lock();
	}
	CONST DWORD exit_code = reinterpret_cast<LPTHREAD_START_ROUTINE>(address)(NULL);
	for (size_t i = 0; i < tls_slots_sz; ++i) {
		current_teb->TlsSlots[i] = prev_tls_slots[i];
	}
	return exit_code;
}*/

ProcessIdMap::iterator ProcessId(JNIEnv *env, jobject obj) {
	for (ProcessIdMap::iterator process_id_it = process_id_map.begin(); process_id_it != process_id_map.end(); ++process_id_it) {
		if (env->IsSameObject(process_id_it->first, obj)) {
			return process_id_it;
		}
	}
	return process_id_map.end();
}

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    setPriorityClass
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_setPriorityClass
  (JNIEnv *env, jobject this_obj, jlong process_handle, jint priority) {
	return SetPriorityClass(reinterpret_cast<HANDLE>(process_handle), priority);
}

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    getPriorityClass
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_getPriorityClass
  (JNIEnv *env, jobject this_obj, jlong process_handle) {
	return GetPriorityClass(reinterpret_cast<HANDLE>(process_handle));
}

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    protect
 * Signature: (JJII)I
 */
JNIEXPORT jint JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_protect
  (JNIEnv *env, jobject this_obj, jlong process_handle, jlong address, jint size, jint protect) {
	DWORD old_protect = 0;
	VirtualProtectEx(reinterpret_cast<HANDLE>(process_handle), reinterpret_cast<LPVOID>(address), size, protect, &old_protect);
	return old_protect;
}

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    open
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_open
(JNIEnv *env, jobject this_obj, jstring process) {
	CONST HANDLE snapshot_handle = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	if (snapshot_handle == INVALID_HANDLE_VALUE) {
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	const char *process_chars = env->GetStringUTFChars(process, JNI_FALSE);
	PROCESSENTRY32 process_entry;
	process_entry.dwSize = sizeof(PROCESSENTRY32);
	if (!Process32First(snapshot_handle, &process_entry)) {
		goto failure;
	}
	do {
		if (strcmp(process_entry.szExeFile, process_chars) == 0) {
			CONST HANDLE process_handle = OpenProcess(PROCESS_VM_OPERATION | PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_QUERY_INFORMATION | PROCESS_CREATE_THREAD | PROCESS_SET_INFORMATION, FALSE, process_entry.th32ProcessID);
			if (process_handle == NULL) {
				goto failure;
			}
			jobject global_this_obj = env->NewGlobalRef(this_obj);
			process_id_map[global_this_obj] = process_entry.th32ProcessID;
			env->ReleaseStringUTFChars(process, process_chars);
			CloseHandle(snapshot_handle);
			return reinterpret_cast<jlong>(process_handle);
		}
	} while (Process32Next(snapshot_handle, &process_entry));
	failure:
	env->ReleaseStringUTFChars(process, process_chars);
	CloseHandle(snapshot_handle);
	return org_bitbucket_reliant_memory_MemoryStream_NULL;
}

/*LRESULT CALLBACK LowLevelMouseProc(_In_ int n_code, _In_ WPARAM w_param, _In_ LPARAM l_param) {
	switch (n_code) {
	case HC_ACTION:
		if (w_param == WM_MOUSEMOVE) {
			return 0;
		}
	default:
		return CallNextHookEx(NULL, n_code, w_param, l_param);
	}
}*/

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    disableMouse
 * Signature: (J)V
 */
/*JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_disableMouse
  (JNIEnv *env, jobject this_obj, jlong process_handle) {
	return reinterpret_cast<jlong>(SetWindowsHookEx(WH_MOUSE_LL, LowLevelMouseProc, reinterpret_cast<HINSTANCE>(process_handle), 0));
}*/

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    enableMouse
 * Signature: (J)Z
 */
/*JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_enableMouse
  (JNIEnv *env, jobject this_obj, jlong mouse_hook) {
	return UnhookWindowsHookEx(reinterpret_cast<HHOOK>(mouse_hook));
}*/

/*JNIEXPORT jint JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_captureInterface
(JNIEnv *env, jobject this_obj, jstring name, jlong module) {
	const char *name_native = env->GetStringUTFChars(name, JNI_FALSE);
	const size_t mem_sz = strlen(name_native) + sizeof(DWORD);
	CONST HANDLE process_handle = ProcessHandle(env, this_obj)->second;
	LPVOID pages_addr = VirtualAllocEx(process_handle, NULL, mem_sz, MEM_COMMIT | MEM_RESERVE, PAGE_READWRITE);
	if (pages_addr == NULL) {
		std::cout << "t2" << std::endl;
		env->ReleaseStringUTFChars(name, name_native);
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	if (!WriteProcessMemory(process_handle, pages_addr, name_native, mem_sz - sizeof(DWORD), NULL) || !WriteProcessMemory(process_handle, reinterpret_cast<LPVOID>(reinterpret_cast<DWORD>(pages_addr) + strlen(name_native)), NULL, sizeof(DWORD), NULL)) {
		std::cout << "t3" << std::endl;
		env->ReleaseStringUTFChars(name, name_native);
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	typedef jint (*CreateInterfaceFunc)(const char *name_sz, int *ret_code);
	CreateInterfaceFunc CreateInterface = reinterpret_cast<CreateInterfaceFunc>(GetProcAddress(reinterpret_cast<HMODULE>(module), "CreateInterface"));
	CONST HANDLE create_interface_thread = CreateRemoteThread(process_handle, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(CreateInterface), pages_addr, 0, NULL);
	if (create_interface_thread == NULL) {
		std::cout << "t" << std::endl;
		env->ReleaseStringUTFChars(name, name_native);
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	if (WaitForSingleObject(create_interface_thread, INFINITE) == WAIT_FAILED) {
		env->ReleaseStringUTFChars(name, name_native);
		std::cout << "t1" << std::endl;
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	DWORD exit_code;
	if (!GetExitCodeThread(create_interface_thread, &exit_code)) {
		env->ReleaseStringUTFChars(name, name_native);
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	env->ReleaseStringUTFChars(name, name_native);
	CloseHandle(create_interface_thread);
	return exit_code;
}*/

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    suspend0
 * Signature: ()V
 */
/*JNIEXPORT void JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_suspend0
  (JNIEnv *env, jobject this_obj) {
	typedef LONG (NTAPI *NtSuspendProcessFunc)(IN HANDLE ProcessHandle);
	reinterpret_cast<NtSuspendProcessFunc>(GetProcAddress(GetModuleHandle("ntdll"), "NtSuspendProcess"))(ProcessHandle(env, this_obj)->second);
}*/

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    resume0
 * Signature: ()V
 */
/*JNIEXPORT void JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_resume0
  (JNIEnv *env, jobject this_obj) {
	typedef LONG (NTAPI *NtResumeProcessFunc)(IN HANDLE ProcessHandle);
	reinterpret_cast<NtResumeProcessFunc>(GetProcAddress(GetModuleHandle("ntdll"), "NtResumeProcess"))(ProcessHandle(env, this_obj)->second);
}*/

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    close
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_close
(JNIEnv *env, jobject this_obj, jlong process_handle) {
	CONST BOOL success = CloseHandle(reinterpret_cast<HANDLE>(process_handle));
	const ProcessIdMap::iterator process_id_it = ProcessId(env, this_obj);
	env->DeleteGlobalRef(process_id_it->first);
	process_id_map.erase(process_id_it);
	return success;
}

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    read
 * Signature: (JI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_read
(JNIEnv *env, jobject this_obj, jlong process_handle, jlong address, jint size) {
	jbyteArray buffer = env->NewByteArray(size);
	jbyte *buff_elements = env->GetByteArrayElements(buffer, JNI_FALSE);
	ReadProcessMemory(reinterpret_cast<HANDLE>(process_handle), reinterpret_cast<LPCVOID>(address), buff_elements, size, NULL);
	env->ReleaseByteArrayElements(buffer, buff_elements, 0);
	return buffer;
}

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    write
 * Signature: (J[B)Z
 */
JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_write
(JNIEnv *env, jobject this_obj, jlong process_handle, jlong address, jbyteArray buffer) {
	jbyte *buff_elements = env->GetByteArrayElements(buffer, JNI_FALSE);
	CONST BOOL success = WriteProcessMemory(reinterpret_cast<HANDLE>(process_handle), reinterpret_cast<LPVOID>(address), buff_elements, env->GetArrayLength(buffer), NULL);
	env->ReleaseByteArrayElements(buffer, buff_elements, JNI_ABORT);
	return success;
}

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    moduleAddress
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_moduleAddress0
(JNIEnv *env, jobject this_obj, jstring module) {
	const char *module_name = env->GetStringUTFChars(module, JNI_FALSE);
	HANDLE snapshot_handle = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE, ProcessId(env, this_obj)->second);
	while (GetLastError() == ERROR_BAD_LENGTH) {
		snapshot_handle = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE, ProcessId(env, this_obj)->second);
	}
	if (snapshot_handle == INVALID_HANDLE_VALUE) {
		env->ReleaseStringUTFChars(module, module_name);
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	MODULEENTRY32 module_entry;
	module_entry.dwSize = sizeof(MODULEENTRY32);
	if (!Module32First(snapshot_handle, &module_entry)) {
		CloseHandle(snapshot_handle);
		env->ReleaseStringUTFChars(module, module_name);
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	do {
		if (strcmp(module_entry.szModule, module_name)) {
			continue;
		}
		CloseHandle(snapshot_handle);
		env->ReleaseStringUTFChars(module, module_name);
		return reinterpret_cast<jlong>(module_entry.modBaseAddr);
	} while (Module32Next(snapshot_handle, &module_entry));
	CloseHandle(snapshot_handle);
	env->ReleaseStringUTFChars(module, module_name);
	return org_bitbucket_reliant_memory_MemoryStream_NULL;
}

/*DWORD InjectText(HANDLE process, const char *text) {
	const size_t text_sz = strlen(text);
	CONST LPVOID text_addr = VirtualAllocEx(process, NULL, text_sz, MEM_COMMIT | MEM_RESERVE, PAGE_READWRITE);
	if (text_addr == NULL || !WriteProcessMemory(process, text_addr, text, text_sz, NULL)) {
		VirtualFreeEx(process, text_addr, 0, MEM_RELEASE);
		return 0;
	}
	return reinterpret_cast<DWORD>(text_addr);
}

JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_callFunc
(JNIEnv *env, jobject this_obj, jlong process_handle, jlong address, jlong param, jint timeout) {
	CONST HANDLE process = reinterpret_cast<HANDLE>(process_handle);
	CONST DWORD ntdll_txt_addr = InjectText(process, "ntdll.dll"), query_txt_addr = InjectText(process, "NtQueryInformationThread");
	if (ntdll_txt_addr == 0 || query_txt_addr == 0) {
		return 0;
	}
	BYTE ntdll_txt_addr_bytes[sizeof(ntdll_txt_addr)], query_txt_addr_bytes[sizeof(query_txt_addr)];
	memcpy(ntdll_txt_addr_bytes, &ntdll_txt_addr, sizeof(ntdll_txt_addr_bytes));
	memcpy(query_txt_addr_bytes, &query_txt_addr, sizeof(query_txt_addr_bytes));
	CONST DWORD kernel_addr = reinterpret_cast<DWORD>(GetModuleHandle("kernel32.dll")), ntdll_addr = reinterpret_cast<DWORD>(GetModuleHandle("ntdll.dll")); // Eric: kernel32 and ntdll are loaded in the same addresses for all x86 processes
	CONST DWORD proc_id_addr = kernel_addr + 0x111F8, open_thread_addr = kernel_addr + 0x21210, close_handle_addr = kernel_addr + 0x113E0, mod_handle_addr = kernel_addr + 0x11245, proc_address_addr = kernel_addr + 0x11222, tls_alloc_addr = kernel_addr + 0x1494D;
	CONST LPVOID proc_id_ptr_addr = VirtualAllocEx(process, NULL, sizeof(proc_id_addr), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE), open_thread_ptr_addr = VirtualAllocEx(process, NULL, sizeof(open_thread_addr), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE), close_handle_ptr_addr = VirtualAllocEx(process, NULL, sizeof(close_handle_addr), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE), mod_handle_ptr_addr = VirtualAllocEx(process, NULL, sizeof(mod_handle_addr), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE), proc_address_ptr_addr = VirtualAllocEx(process, NULL, sizeof(proc_address_addr), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE), tls_alloc_ptr_addr = VirtualAllocEx(process, NULL, sizeof(tls_alloc_addr), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
	if (proc_id_ptr_addr == NULL || open_thread_ptr_addr == NULL || close_handle_ptr_addr == NULL || mod_handle_ptr_addr == NULL || proc_address_ptr_addr == NULL || tls_alloc_ptr_addr == NULL || !WriteProcessMemory(process, proc_id_ptr_addr, &proc_id_addr, sizeof(proc_id_addr), NULL) || !WriteProcessMemory(process, open_thread_ptr_addr, &open_thread_addr, sizeof(open_thread_addr), NULL) || !WriteProcessMemory(process, close_handle_ptr_addr, &close_handle_addr, sizeof(close_handle_addr), NULL) || !WriteProcessMemory(process, mod_handle_ptr_addr, &mod_handle_addr, sizeof(mod_handle_addr), NULL) || !WriteProcessMemory(process, proc_address_ptr_addr, &proc_address_addr, sizeof(proc_address_addr), NULL) || !WriteProcessMemory(process, tls_alloc_ptr_addr, &tls_alloc_addr, sizeof(tls_alloc_addr), NULL)) {
		VirtualFreeEx(process, tls_alloc_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, close_handle_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, proc_address_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, mod_handle_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, proc_id_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, open_thread_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, reinterpret_cast<PVOID>(ntdll_txt_addr), 0, MEM_RELEASE);
		VirtualFreeEx(process, reinterpret_cast<PVOID>(query_txt_addr), 0, MEM_RELEASE);
	}
	BYTE proc_id_addr_bytes[sizeof(proc_id_addr)], open_thread_addr_bytes[sizeof(open_thread_addr)], close_handle_addr_bytes[sizeof(close_handle_addr)], mod_handle_addr_bytes[sizeof(mod_handle_addr)], proc_address_addr_bytes[sizeof(proc_address_addr)], tls_alloc_addr_bytes[sizeof(tls_alloc_addr)];
	memcpy(proc_id_addr_bytes, &proc_id_ptr_addr, sizeof(proc_id_addr_bytes));
	memcpy(open_thread_addr_bytes, &open_thread_ptr_addr, sizeof(open_thread_addr_bytes));
	memcpy(close_handle_addr_bytes, &close_handle_ptr_addr, sizeof(close_handle_addr_bytes));
	memcpy(mod_handle_addr_bytes, &mod_handle_ptr_addr, sizeof(mod_handle_addr_bytes));
	memcpy(proc_address_addr_bytes, &proc_address_ptr_addr, sizeof(proc_address_addr_bytes));
	memcpy(tls_alloc_addr_bytes, &tls_alloc_ptr_addr, sizeof(tls_alloc_addr_bytes));
	CONST BYTE call_func_safe_nop[] = {0x55, 0x89, 0xE5, 0x57, 0x81, 0xEC, 0x84, 0x01, 0x00, 0x00, 0xC7, 0x44, 0x24, 0x04, 0x00, 0x00, 0x00, 0x00, 0xC7, 0x04, 0x24, 0x04, 0x00, 0x00, 0x00, 0xE8, 0x90, 0x90, 0x90, 0x90, 0x83, 0xEC,
			0x08, 0x89, 0x45, 0xE8, 0x83, 0x7D, 0xE8, 0xFF, 0x75, 0x0A, 0xB8, 0x00, 0x00, 0x00, 0x00, 0xE9, 0x26, 0x02, 0x00, 0x00, 0xC7, 0x45, 0xB8, 0x1C, 0x00, 0x00, 0x00, 0x8D, 0x45, 0xB8, 0x89, 0x44,
			0x24, 0x04, 0x8B, 0x45, 0xE8, 0x89, 0x04, 0x24, 0xE8, 0x90, 0x90, 0x90, 0x90, 0x83, 0xEC, 0x08, 0x85, 0xC0, 0x0F, 0x94, 0xC0, 0x84, 0xC0, 0x74, 0x0A, 0xB8, 0x00, 0x00, 0x00, 0x00, 0xE9, 0xF7,
			0x01, 0x00, 0x00, 0xA1, proc_id_addr_bytes[0], proc_id_addr_bytes[1], proc_id_addr_bytes[2], proc_id_addr_bytes[3], 0xFF, 0xD0, 0x89, 0x45, 0xE4, 0xC7, 0x45, 0xF4, 0x00, 0x00, 0x00, 0x00, 0x8B, 0x45, 0xC4, 0x3B, 0x45, 0xE4, 0x75, 0x25, 0x8B, 0x45, 0xC0, 0x89,
			0x44, 0x24, 0x08, 0xC7, 0x44, 0x24, 0x04, 0x00, 0x00, 0x00, 0x00, 0xC7, 0x04, 0x24, 0x40, 0x00, 0x00, 0x00, 0xA1, open_thread_addr_bytes[0], open_thread_addr_bytes[1], open_thread_addr_bytes[2], open_thread_addr_bytes[3], 0xFF, 0xD0, 0x83, 0xEC, 0x0C, 0x89, 0x45, 0xF4, 0xEB,
			0x20, 0x8D, 0x45, 0xB8, 0x89, 0x44, 0x24, 0x04, 0x8B, 0x45, 0xE8, 0x89, 0x04, 0x24, 0xE8, 0x90, 0x90, 0x90, 0x90, 0x83, 0xEC, 0x08, 0x85, 0xC0, 0x0F, 0x95, 0xC0, 0x84, 0xC0, 0x74, 0x02, 0xEB,
			0xB3, 0x8B, 0x45, 0xE8, 0x89, 0x04, 0x24, 0xA1, close_handle_addr_bytes[0], close_handle_addr_bytes[1], close_handle_addr_bytes[2], close_handle_addr_bytes[3], 0xFF, 0xD0, 0x83, 0xEC, 0x04, 0x85, 0xC0, 0x74, 0x06, 0x83, 0x7D, 0xF4, 0x00, 0x75, 0x07, 0xB8, 0x01, 0x00, 0x00, 0x00,
			0xEB, 0x05, 0xB8, 0x00, 0x00, 0x00, 0x00, 0x84, 0xC0, 0x74, 0x0A, 0xB8, 0x00, 0x00, 0x00, 0x00, 0xE9, 0x65, 0x01, 0x00, 0x00, 0xB9, 0x00, 0x00, 0x00, 0x00, 0xB8, 0x1C, 0x00, 0x00, 0x00, 0x83,
			0xE0, 0xFC, 0x89, 0xC2, 0xB8, 0x00, 0x00, 0x00, 0x00, 0x89, 0x4C, 0x05, 0x9C, 0x83, 0xC0, 0x04, 0x39, 0xD0, 0x72, 0xF5, 0xC7, 0x04, 0x24, ntdll_txt_addr_bytes[0], ntdll_txt_addr_bytes[1], ntdll_txt_addr_bytes[2], ntdll_txt_addr_bytes[3], 0xA1, mod_handle_addr_bytes[0], mod_handle_addr_bytes[1], mod_handle_addr_bytes[2], mod_handle_addr_bytes[3],
			0xFF, 0xD0, 0x83, 0xEC, 0x04, 0xC7, 0x44, 0x24, 0x04, query_txt_addr_bytes[0], query_txt_addr_bytes[1], query_txt_addr_bytes[2], query_txt_addr_bytes[3], 0x89, 0x04, 0x24, 0xA1, proc_address_addr_bytes[0], proc_address_addr_bytes[1], proc_address_addr_bytes[2], proc_address_addr_bytes[3], 0xFF, 0xD0, 0x83, 0xEC, 0x08, 0x89, 0xC2, 0xC7, 0x44, 0x24, 0x10,
			0x00, 0x00, 0x00, 0x00, 0xC7, 0x44, 0x24, 0x0C, 0x1C, 0x00, 0x00, 0x00, 0x8D, 0x45, 0x9C, 0x89, 0x44, 0x24, 0x08, 0xC7, 0x44, 0x24, 0x04, 0x00, 0x00, 0x00, 0x00, 0x8B, 0x45, 0xF4, 0x89, 0x04,
			0x24, 0xFF, 0xD2, 0x83, 0xEC, 0x14, 0x8B, 0x45, 0xF4, 0x89, 0x04, 0x24, 0xA1, close_handle_addr_bytes[0], close_handle_addr_bytes[1], close_handle_addr_bytes[2], close_handle_addr_bytes[3], 0xFF, 0xD0, 0x83, 0xEC, 0x04, 0x85, 0xC0, 0x74, 0x07, 0x8B, 0x45, 0xA0, 0x85, 0xC0, 0x75,
			0x07, 0xB8, 0x01, 0x00, 0x00, 0x00, 0xEB, 0x05, 0xB8, 0x00, 0x00, 0x00, 0x00, 0x84, 0xC0, 0x74, 0x0A, 0xB8, 0x00, 0x00, 0x00, 0x00, 0xE9, 0xBF, 0x00, 0x00, 0x00, 0x8B, 0x45, 0xA0, 0x89, 0x45,
			0xE0, 0xE8, 0x90, 0x90, 0x90, 0x90, 0x89, 0x45, 0xDC, 0xC7, 0x45, 0xD8, 0x40, 0x00, 0x00, 0x00, 0x8D, 0x95, 0x9C, 0xFE, 0xFF, 0xFF, 0xB8, 0x00, 0x00, 0x00, 0x00, 0xB9, 0x40, 0x00, 0x00, 0x00,
			0x89, 0xD7, 0xF3, 0xAB, 0xC7, 0x45, 0xF0, 0x00, 0x00, 0x00, 0x00, 0x83, 0x7D, 0xF0, 0x3F, 0x77, 0x47, 0xA1, tls_alloc_addr_bytes[0], tls_alloc_addr_bytes[1], tls_alloc_addr_bytes[2], tls_alloc_addr_bytes[3], 0xFF, 0xD0, 0x8B, 0x45, 0xDC, 0x8B, 0x55, 0xF0, 0x81, 0xC2,
			0x68, 0x02, 0x00, 0x00, 0x8B, 0x54, 0x90, 0x08, 0x8B, 0x45, 0xF0, 0x89, 0x94, 0x85, 0x9C, 0xFE, 0xFF, 0xFF, 0x8B, 0x45, 0xE0, 0x8B, 0x55, 0xF0, 0x81, 0xC2, 0x68, 0x02, 0x00, 0x00, 0x8B, 0x54,
			0x90, 0x08, 0x8B, 0x45, 0xDC, 0x8B, 0x4D, 0xF0, 0x81, 0xC1, 0x68, 0x02, 0x00, 0x00, 0x89, 0x54, 0x88, 0x08, 0x83, 0x45, 0xF0, 0x01, 0xEB, 0xB3, 0x8B, 0x45, 0x08, 0xC7, 0x04, 0x24, 0x00, 0x00,
			0x00, 0x00, 0xFF, 0xD0, 0x83, 0xEC, 0x04, 0x89, 0x45, 0xD4, 0xC7, 0x45, 0xEC, 0x00, 0x00, 0x00, 0x00, 0x83, 0x7D, 0xEC, 0x3F, 0x77, 0x20, 0x8B, 0x45, 0xEC, 0x8B, 0x94, 0x85, 0x9C, 0xFE, 0xFF,
			0xFF, 0x8B, 0x45, 0xDC, 0x8B, 0x4D, 0xEC, 0x81, 0xC1, 0x68, 0x02, 0x00, 0x00, 0x89, 0x54, 0x88, 0x08, 0x83, 0x45, 0xEC, 0x01, 0xEB, 0xDA, 0x8B, 0x45, 0xD4, 0x8B, 0x7D, 0xFC, 0xC9, 0xC2, 0x04,
			0x00};
	CONST LPVOID call_func_safe_addr = VirtualAllocEx(process, NULL, sizeof(call_func_safe_nop), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
	if (call_func_safe_addr == NULL || !WriteProcessMemory(process, call_func_safe_addr, call_func_safe_nop, sizeof(call_func_safe_nop), NULL)) {
		VirtualFreeEx(process, call_func_safe_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, tls_alloc_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, close_handle_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, proc_address_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, mod_handle_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, proc_id_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, open_thread_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, reinterpret_cast<PVOID>(ntdll_txt_addr), 0, MEM_RELEASE);
		VirtualFreeEx(process, reinterpret_cast<PVOID>(query_txt_addr), 0, MEM_RELEASE);
		return 0;
	}
	CONST SIZE_T call_rel_len = 0x5;
	CONST DWORD create_snapshot_dist = kernel_addr + 0x3733F - (reinterpret_cast<DWORD>(call_func_safe_addr) + 25) - call_rel_len, thread_first_dist = kernel_addr + 0x96313 - (reinterpret_cast<DWORD>(call_func_safe_addr) + 72) - call_rel_len, thread_next_dist = kernel_addr + 0x963BF - (reinterpret_cast<DWORD>(call_func_safe_addr) + 174) - call_rel_len, current_teb_dist = ntdll_addr + 0x9E943 - (reinterpret_cast<DWORD>(call_func_safe_addr) + 417) - call_rel_len;
	/*BYTE create_snapshot_addr_bytes[sizeof(create_snapshot_addr)], thread_first_addr_bytes[sizeof(thread_first_addr)], thread_next_addr_bytes[sizeof(thread_next_addr)], current_teb_addr_bytes[sizeof(current_teb_addr)];
	memcpy(create_snapshot_addr_bytes, &create_snapshot_addr, sizeof(create_snapshot_addr_bytes));
	memcpy(thread_first_addr_bytes, &thread_first_addr, sizeof(thread_first_addr_bytes));
	memcpy(thread_next_addr_bytes, &thread_next_addr, sizeof(thread_next_addr_bytes));
	memcpy(current_teb_addr_bytes, &current_teb_addr, sizeof(current_teb_addr_bytes));
	//CONST BYTE code_segment[] = {0x23, 0x00}; // TODO(Eric) Stop using the far call opcode and just calculate the distances; don't need to set CS
	//const size_t cs_size = sizeof(code_segment);
	CONST BYTE call_func_safe[] = {0x55, 0x89, 0xE5, 0x57, 0x81, 0xEC, 0x84, 0x01, 0x00, 0x00, 0xC7, 0x44, 0x24, 0x04, 0x00, 0x00, 0x00, 0x00, 0xC7, 0x04, 0x24, 0x04, 0x00, 0x00, 0x00, 0xE8, create_snapshot_addr_bytes[0], create_snapshot_addr_bytes[1], create_snapshot_addr_bytes[2], create_snapshot_addr_bytes[3], 0x83, 0xEC,
			0x08, 0x89, 0x45, 0xE8, 0x83, 0x7D, 0xE8, 0xFF, 0x75, 0x0A, 0xB8, 0x00, 0x00, 0x00, 0x00, 0xE9, 0x1F, 0x02, 0x00, 0x00, 0xC7, 0x45, 0xB8, 0x1C, 0x00, 0x00, 0x00, 0x8D, 0x45, 0xB8, 0x89, 0x44,
			0x24, 0x04, 0x8B, 0x45, 0xE8, 0x89, 0x04, 0x24, 0xE8, thread_first_addr_bytes[0], thread_first_addr_bytes[1], thread_first_addr_bytes[2], thread_first_addr_bytes[3], 0x83, 0xEC, 0x08, 0x85, 0xC0, 0x0F, 0x94, 0xC0, 0x84, 0xC0, 0x74, 0x0A, 0xB8, 0x00, 0x00, 0x00, 0x00, 0xE9, 0xF0,
			0x01, 0x00, 0x00, 0xA1, proc_id_addr_bytes[0], proc_id_addr_bytes[1], proc_id_addr_bytes[2], proc_id_addr_bytes[3], 0xFF, 0xD0, 0x89, 0x45, 0xE4, 0xC7, 0x45, 0xF4, 0x00, 0x00, 0x00, 0x00, 0x8B, 0x45, 0xC4, 0x3B, 0x45, 0xE4, 0x75, 0x25, 0x8B, 0x45, 0xC0, 0x89,
			0x44, 0x24, 0x08, 0xC7, 0x44, 0x24, 0x04, 0x00, 0x00, 0x00, 0x00, 0xC7, 0x04, 0x24, 0x40, 0x00, 0x00, 0x00, 0xA1, open_thread_addr_bytes[0], open_thread_addr_bytes[1], open_thread_addr_bytes[2], open_thread_addr_bytes[3], 0xFF, 0xD0, 0x83, 0xEC, 0x0C, 0x89, 0x45, 0xF4, 0xEB,
			0x20, 0x8D, 0x45, 0xB8, 0x89, 0x44, 0x24, 0x04, 0x8B, 0x45, 0xE8, 0x89, 0x04, 0x24, 0xE8, thread_next_addr_bytes[0], thread_next_addr_bytes[1], thread_next_addr_bytes[2], thread_next_addr_bytes[3], 0x83, 0xEC, 0x08, 0x85, 0xC0, 0x0F, 0x95, 0xC0, 0x84, 0xC0, 0x74, 0x02, 0xEB,
			0xB3, 0x8B, 0x45, 0xE8, 0x89, 0x04, 0x24, 0xA1, close_handle_addr_bytes[0], close_handle_addr_bytes[1], close_handle_addr_bytes[2], close_handle_addr_bytes[3], 0xFF, 0xD0, 0x83, 0xEC, 0x04, 0x85, 0xC0, 0x74, 0x06, 0x83, 0x7D, 0xF4, 0x00, 0x75, 0x07, 0xB8, 0x01, 0x00, 0x00, 0x00,
			0xEB, 0x05, 0xB8, 0x00, 0x00, 0x00, 0x00, 0x84, 0xC0, 0x74, 0x0A, 0xB8, 0x00, 0x00, 0x00, 0x00, 0xE9, 0x5E, 0x01, 0x00, 0x00, 0xB9, 0x00, 0x00, 0x00, 0x00, 0xB8, 0x1C, 0x00, 0x00, 0x00, 0x83,
			0xE0, 0xFC, 0x89, 0xC2, 0xB8, 0x00, 0x00, 0x00, 0x00, 0x89, 0x4C, 0x05, 0x9C, 0x83, 0xC0, 0x04, 0x39, 0xD0, 0x72, 0xF5, 0xC7, 0x04, 0x24, ntdll_txt_addr_bytes[0], ntdll_txt_addr_bytes[1], ntdll_txt_addr_bytes[2], ntdll_txt_addr_bytes[3], 0xA1, mod_handle_addr_bytes[0], mod_handle_addr_bytes[1], mod_handle_addr_bytes[2], mod_handle_addr_bytes[3],
			0xFF, 0xD0, 0x83, 0xEC, 0x04, 0xC7, 0x44, 0x24, 0x04, query_txt_addr_bytes[0], query_txt_addr_bytes[1], query_txt_addr_bytes[2], query_txt_addr_bytes[3], 0x89, 0x04, 0x24, 0xA1, proc_address_addr_bytes[0], proc_address_addr_bytes[1], proc_address_addr_bytes[2], proc_address_addr_bytes[3], 0xFF, 0xD0, 0x83, 0xEC, 0x08, 0x89, 0xC2, 0xC7, 0x44, 0x24, 0x10,
			0x00, 0x00, 0x00, 0x00, 0xC7, 0x44, 0x24, 0x0C, 0x1C, 0x00, 0x00, 0x00, 0x8D, 0x45, 0x9C, 0x89, 0x44, 0x24, 0x08, 0xC7, 0x44, 0x24, 0x04, 0x00, 0x00, 0x00, 0x00, 0x8B, 0x45, 0xF4, 0x89, 0x04,
			0x24, 0xFF, 0xD2, 0x83, 0xEC, 0x14, 0x8B, 0x45, 0xF4, 0x89, 0x04, 0x24, 0xA1, close_handle_addr_bytes[0], close_handle_addr_bytes[1], close_handle_addr_bytes[2], close_handle_addr_bytes[3], 0xFF, 0xD0, 0x83, 0xEC, 0x04, 0x85, 0xC0, 0x74, 0x07, 0x8B, 0x45, 0xA0, 0x85, 0xC0, 0x75,
			0x07, 0xB8, 0x01, 0x00, 0x00, 0x00, 0xEB, 0x05, 0xB8, 0x00, 0x00, 0x00, 0x00, 0x84, 0xC0, 0x74, 0x0A, 0xB8, 0x00, 0x00, 0x00, 0x00, 0xE9, 0xB8, 0x00, 0x00, 0x00, 0x8B, 0x45, 0xA0, 0x89, 0x45,
			0xE0, 0xE8, current_teb_addr_bytes[0], current_teb_addr_bytes[1], current_teb_addr_bytes[2], current_teb_addr_bytes[3], 0x89, 0x45, 0xDC, 0xC7, 0x45, 0xD8, 0x40, 0x00, 0x00, 0x00, 0x8D, 0x95, 0x9C, 0xFE, 0xFF, 0xFF, 0xB8, 0x00, 0x00, 0x00, 0x00, 0xB9, 0x40, 0x00, 0x00, 0x00,
			0x89, 0xD7, 0xF3, 0xAB, 0xC7, 0x45, 0xF0, 0x00, 0x00, 0x00, 0x00, 0x83, 0x7D, 0xF0, 0x3F, 0x77, 0x40, 0x8B, 0x45, 0xDC, 0x8B, 0x55, 0xF0, 0x81, 0xC2, 0x68, 0x02, 0x00, 0x00, 0x8B, 0x54, 0x90,
			0x08, 0x8B, 0x45, 0xF0, 0x89, 0x94, 0x85, 0x9C, 0xFE, 0xFF, 0xFF, 0x8B, 0x45, 0xE0, 0x8B, 0x55, 0xF0, 0x81, 0xC2, 0x68, 0x02, 0x00, 0x00, 0x8B, 0x54, 0x90, 0x08, 0x8B, 0x45, 0xDC, 0x8B, 0x4D,
			0xF0, 0x81, 0xC1, 0x68, 0x02, 0x00, 0x00, 0x89, 0x54, 0x88, 0x08, 0x83, 0x45, 0xF0, 0x01, 0xEB, 0xBA, 0x8B, 0x45, 0x08, 0xC7, 0x04, 0x24, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xD0, 0x83, 0xEC, 0x04,
			0x89, 0x45, 0xD4, 0xC7, 0x45, 0xEC, 0x00, 0x00, 0x00, 0x00, 0x83, 0x7D, 0xEC, 0x3F, 0x77, 0x20, 0x8B, 0x45, 0xEC, 0x8B, 0x94, 0x85, 0x9C, 0xFE, 0xFF, 0xFF, 0x8B, 0x45, 0xDC, 0x8B, 0x4D, 0xEC,
			0x81, 0xC1, 0x68, 0x02, 0x00, 0x00, 0x89, 0x54, 0x88, 0x08, 0x83, 0x45, 0xEC, 0x01, 0xEB, 0xDA, 0x8B, 0x45, 0xD4, 0x8B, 0x7D, 0xFC, 0xC9, 0xC2, 0x04, 0x00};*/
	/*if (!WriteProcessMemory(process, reinterpret_cast<LPVOID>(reinterpret_cast<DWORD>(call_func_safe_addr) + 25 + 0x1), &create_snapshot_dist, sizeof(create_snapshot_dist), NULL) || !WriteProcessMemory(process, reinterpret_cast<LPVOID>(reinterpret_cast<DWORD>(call_func_safe_addr) + 72 + 0x1), &thread_first_dist, sizeof(thread_first_dist), NULL) || !WriteProcessMemory(process, reinterpret_cast<LPVOID>(reinterpret_cast<DWORD>(call_func_safe_addr) + 174 + 0x1), &thread_next_dist, sizeof(thread_next_dist), NULL) || !WriteProcessMemory(process, reinterpret_cast<LPVOID>(reinterpret_cast<DWORD>(call_func_safe_addr) + 417 + 0x1), &current_teb_dist, sizeof(current_teb_dist), NULL)) {
		VirtualFreeEx(process, call_func_safe_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, tls_alloc_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, close_handle_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, proc_address_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, mod_handle_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, proc_id_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, open_thread_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, reinterpret_cast<PVOID>(ntdll_txt_addr), 0, MEM_RELEASE);
		VirtualFreeEx(process, reinterpret_cast<PVOID>(query_txt_addr), 0, MEM_RELEASE);
		return 0;
	}
	/*if (!WriteProcessMemory(process, call_func_safe_addr, call_func_safe, sizeof(call_func_safe), NULL)) {
			VirtualFreeEx(process, call_func_safe_addr, 0, MEM_RELEASE);
			VirtualFreeEx(process, ntdll_txt_addr_bytes, 0, MEM_RELEASE);
			VirtualFreeEx(process, query_txt_addr_bytes, 0, MEM_RELEASE);
			return 0;
		}*/
	/*CONST HANDLE exec_thread = CreateRemoteThread(process, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(call_func_safe_addr), reinterpret_cast<LPVOID>(address), 0, NULL);
	if (exec_thread == NULL) {
		VirtualFreeEx(process, call_func_safe_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, tls_alloc_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, close_handle_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, proc_address_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, mod_handle_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, proc_id_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, open_thread_ptr_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, reinterpret_cast<PVOID>(ntdll_txt_addr), 0, MEM_RELEASE);
		VirtualFreeEx(process, reinterpret_cast<PVOID>(query_txt_addr), 0, MEM_RELEASE);
		return 0;
	}
	DWORD exit_code;
	DWORD ret_val = WaitForSingleObject(exec_thread, timeout) != WAIT_OBJECT_0 || !GetExitCodeThread(exec_thread, &exit_code) ? 0 : exit_code;
	CloseHandle(exec_thread);
	VirtualFreeEx(process, call_func_safe_addr, 0, MEM_RELEASE);
	VirtualFreeEx(process, tls_alloc_ptr_addr, 0, MEM_RELEASE);
	VirtualFreeEx(process, close_handle_ptr_addr, 0, MEM_RELEASE);
	VirtualFreeEx(process, proc_address_ptr_addr, 0, MEM_RELEASE);
	VirtualFreeEx(process, mod_handle_ptr_addr, 0, MEM_RELEASE);
	VirtualFreeEx(process, proc_id_ptr_addr, 0, MEM_RELEASE);
	VirtualFreeEx(process, open_thread_ptr_addr, 0, MEM_RELEASE);
	VirtualFreeEx(process, reinterpret_cast<PVOID>(ntdll_txt_addr), 0, MEM_RELEASE);
	VirtualFreeEx(process, reinterpret_cast<PVOID>(query_txt_addr), 0, MEM_RELEASE);
	return ret_val;
}*/

/*JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_callFunc
(JNIEnv *env, jobject this_obj, jlong process_handle, jlong address, jlong param, jint timeout) {
	//std::cout << QueryThreadInfo << std::endl;
	CONST HANDLE process = reinterpret_cast<HANDLE>(process_handle);
	DWORD exec_thread_id;
	CONST HANDLE exec_thread = CreateRemoteThread(process, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(address), reinterpret_cast<LPVOID>(param), CREATE_SUSPENDED, &exec_thread_id);
	if (exec_thread == NULL) {
		return 0;
	}
	bool success = true;
	CONST HANDLE thread_snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPTHREAD, 0);
	if (thread_snapshot != INVALID_HANDLE_VALUE) {
		THREADENTRY32 thread_entry;
		thread_entry.dwSize = sizeof(THREADENTRY32);
		DWORD proc_main_thread_id = 0;
		if (Thread32First(thread_snapshot, &thread_entry)) {
			CONST DWORD process_id = ProcessId(env, this_obj)->second;
			do {
				if (thread_entry.th32OwnerProcessID == process_id) {
					proc_main_thread_id = thread_entry.th32ThreadID;
					break;
				}
			} while (Thread32Next(thread_snapshot, &thread_entry));
			if (proc_main_thread_id != 0) {
				//typedef NTSTATUS (WINAPI *NtQueryInformationThreadFunc)(HANDLE, THREADINFOCLASS, PVOID, ULONG, PULONG);
				//THREAD_BASIC_INFORMATION main_thread_info, exec_thread_info;
				//SecureZeroMemory(&main_thread_info, sizeof(THREAD_BASIC_INFORMATION));
				//SecureZeroMemory(&exec_thread_info, sizeof(THREAD_BASIC_INFORMATION));
				//std::cout << "Zero'd TebBaseAddress: " << std::hex << reinterpret_cast<DWORD>(main_thread_info.TebBaseAddress) << std::endl;
				// Eric: ntdll.dll is loaded in the same address for all x86 processes; therefore, we can simply call our GetProcAddress
				//std::cout << "ntdll:" << std::hex << GetModuleHandle("ntdll.dll") << std::endl;
				//const NtQueryInformationThreadFunc query_info_thread_func = reinterpret_cast<NtQueryInformationThreadFunc>(GetProcAddress(GetModuleHandle("ntdll.dll"), "NtQueryInformationThread"));
				//std::cout << "NtQueryInformationThreadFunc: " << std::hex << reinterpret_cast<DWORD>(query_info_thread_func) << ":" << std::dec << GetLastError() << std::endl;
				CONST BYTE query_info_func[] = {0x55, 0x89, 0xE5, 0x83, 0xEC, 0x48, 0x8B, 0x45, 0x08, 0x89, 0x44, 0x24, 0x08, 0xC7, 0x44, 0x24, 0x04, 0x00, 0x00, 0x00, 0x00, 0xC7, 0x04, 0x24, 0x40, 0x00, 0x00, 0x00, 0xB8, 0x10, 0x12, 0xEF,
						0x74, 0xFF, 0xD0, 0x83, 0xEC, 0x0C, 0x89, 0x45, 0xF4, 0xC7, 0x44, 0x24, 0x10, 0x00, 0x00, 0x00, 0x00, 0xC7, 0x44, 0x24, 0x0C, 0x1C, 0x00, 0x00, 0x00, 0x8D, 0x45, 0xD8, 0x89, 0x44, 0x24, 0x08,
						0xC7, 0x44, 0x24, 0x04, 0x00, 0x00, 0x00, 0x00, 0x8B, 0x45, 0xF4, 0x89, 0x04, 0x24, 0xB8, 0x18, 0xFC, 0x52, 0x77, 0xFF, 0xD0, 0x83, 0xEC, 0x14, 0x8B, 0x45, 0xF4, 0x89, 0x04, 0x24, 0xB8, 0xE0,
						0x13, 0xEE, 0x74, 0xFF, 0xD0, 0x83, 0xEC, 0x04, 0x8B, 0x45, 0xDC, 0xC9, 0xC2, 0x04, 0x00};
				CONST LPVOID query_info_func_addr = VirtualAllocEx(process, NULL, sizeof(query_info_func), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
				if (query_info_func_addr != NULL && WriteProcessMemory(process, query_info_func_addr, query_info_func, sizeof(query_info_func), NULL)) {
					CONST HANDLE query_threads[2] = {CreateRemoteThread(process, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(query_info_func_addr), reinterpret_cast<LPVOID>(proc_main_thread_id), 0, NULL), CreateRemoteThread(process, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(query_info_func_addr), reinterpret_cast<LPVOID>(exec_thread_id), 0, NULL)};
					if (query_threads[0] != NULL && query_threads[1] != NULL) {
						DWORD main_teb_addr, exec_teb_addr;
						if (WaitForMultipleObjects(sizeof(query_threads) / sizeof(HANDLE), query_threads, TRUE, INFINITE) != WAIT_FAILED && GetExitCodeThread(query_threads[0], &main_teb_addr) && CloseHandle(query_threads[0]) && GetExitCodeThread(query_threads[1], &exec_teb_addr) && CloseHandle(query_threads[1]) && main_teb_addr != 0 && exec_teb_addr != 0) {
							std::cout << "main_teb: " << std::hex << main_teb_addr << std::endl;
							std::cout << "exec_teb: " << std::hex << exec_teb_addr << std::endl;
							std::cout << "current teb: " << std::hex << reinterpret_cast<DWORD>(NtCurrentTeb()) << std::endl;
							CONST DWORD tls_offset = 0xE10;
							//#ifdef _M_AMD64
							//tls_offset = 0x1480;
							//std::cout << "x64" << std::endl;
							//#else
							//tls_offset = 0xE10;
							//#endif
							std::cout << "tls_offset:" << std::hex << tls_offset << std::endl;
							//PVOID exec_teb, main_teb;
							const size_t process_ptr_sz = sizeof(DWORD);
							//if(ReadProcessMemory(process, reinterpret_cast<LPCVOID>(exec_teb_addr), &exec_teb, process_ptr_sz, NULL) && ReadProcessMemory(process, reinterpret_cast<LPCVOID>(main_teb_addr), &main_teb, process_ptr_sz, NULL)) {
								//std::cout << "breakpoint:" << std::hex << reinterpret_cast<DWORD>(exec_teb) << std::endl;
								const size_t tls_slots_sz = 64;
								PVOID prev_tls_slots[tls_slots_sz];
								// TODO(Eric) Refactor the ReadProcessMemory usage: read the entire array
								for (size_t i = 0; i < tls_slots_sz; ++i) {
									PVOID main_tls_slot;
									if (!ReadProcessMemory(process, reinterpret_cast<LPCVOID>(exec_teb_addr + tls_offset + i * process_ptr_sz), &prev_tls_slots[i], process_ptr_sz, NULL) || !ReadProcessMemory(process, reinterpret_cast<LPCVOID>(main_teb_addr + tls_offset + i * process_ptr_sz), &main_tls_slot, process_ptr_sz, NULL)) {
										std::cout << "RPM: " << std::dec << GetLastError() << std::endl;
										continue;
									}
									std::cout << "breakpoint1:" << std::endl;
									WriteProcessMemory(process, reinterpret_cast<LPVOID>(exec_teb_addr + tls_offset + i * process_ptr_sz), &main_tls_slot, process_ptr_sz, NULL);
								}
								if (ResumeThread(exec_thread) != -1) {
									success = WaitForSingleObject(exec_thread, timeout) == WAIT_OBJECT_0;
									std::cout << "breakpoint2:" << std::endl;
								}
								for (size_t i = 0; i < tls_slots_sz; ++i) {
									WriteProcessMemory(process, reinterpret_cast<LPVOID>(exec_teb_addr + tls_offset + i * process_ptr_sz), &prev_tls_slots[i], process_ptr_sz, NULL);
								}
							//} else {
								//success = false;
							//}
						} else {
							success = false;
						}
					} else {
						success = false;
					}
					if (!VirtualFreeEx(process, query_info_func_addr, 0, MEM_RELEASE)) {
						success = false;
					}
				} else {
					success = false;
				}
			} else {
				success = false;
			}
		} else {
			success = false;
		}
		CloseHandle(thread_snapshot);
	} else {
		success = false;
	}
	DWORD exit_code;
	return !success || !GetExitCodeThread(exec_thread, &exit_code) || !CloseHandle(exec_thread) ? 0 : exit_code;
}*/

JNIEXPORT jint JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_setClanTag
  (JNIEnv *env, jobject this_obj, jlong process_handle, jlong set_clan_tag_addr, jstring tag) {
	const char *name_param = "";
	CONST HANDLE process = reinterpret_cast<HANDLE>(process_handle);
	CONST LPVOID name_param_addr = VirtualAllocEx(process, NULL, sizeof(name_param), MEM_COMMIT | MEM_RESERVE, PAGE_READWRITE);
	if (name_param_addr == NULL) {
		return 0;
	}
	if (!WriteProcessMemory(process, name_param_addr, name_param, sizeof(name_param), NULL)) {
		VirtualFreeEx(process, name_param_addr, 0, MEM_RELEASE);
		return 0;
	}
	BYTE name_param_addr_bytes[sizeof(DWORD)];
	memcpy(name_param_addr_bytes, &name_param_addr, sizeof(name_param_addr_bytes));
	BYTE set_clan_tag_addr_bytes[sizeof(DWORD)];
	memcpy(set_clan_tag_addr_bytes, &set_clan_tag_addr, sizeof(set_clan_tag_addr_bytes));
	CONST BYTE set_clan_tag_func[] = {0x55, 0x89, 0xE5, 0x83, 0xEC, 0x08, 0xBA, name_param_addr_bytes[0], name_param_addr_bytes[1], name_param_addr_bytes[2], name_param_addr_bytes[3], 0x8B, 0x4D, 0x08, 0xB8, set_clan_tag_addr_bytes[0], set_clan_tag_addr_bytes[1], set_clan_tag_addr_bytes[2], set_clan_tag_addr_bytes[3], 0xFF, 0xD0, 0xC9, 0xC2, 0x04, 0x00};
	CONST LPVOID set_clan_tag_func_addr = VirtualAllocEx(process, NULL, sizeof(set_clan_tag_func), MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
	if (set_clan_tag_func_addr == NULL) {
		VirtualFreeEx(process, name_param_addr, 0, MEM_RELEASE);
		return 0;
	}
	if (!WriteProcessMemory(process, set_clan_tag_func_addr, set_clan_tag_func, sizeof(set_clan_tag_func), NULL)) {
		VirtualFreeEx(process, name_param_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, set_clan_tag_func_addr, 0, MEM_RELEASE);
		return 0;
	}
	char *tag_native = const_cast<char*>(env->GetStringUTFChars(tag, JNI_FALSE));
	size_t tag_len = strlen(tag_native);
	if (tag_len == 0) {
		tag_len = 1;
	}
	CONST LPVOID tag_native_addr = VirtualAllocEx(process, NULL, tag_len, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
	if (tag_native_addr == NULL) {
		VirtualFreeEx(process, name_param_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, set_clan_tag_func_addr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(tag, tag_native);
		return 0;
	}
	if (!WriteProcessMemory(process, tag_native_addr, tag_native, tag_len, NULL)) {
		VirtualFreeEx(process, name_param_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, set_clan_tag_func_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, tag_native_addr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(tag, tag_native);
		return 0;
	}
	CONST HANDLE set_clan_tag_thread = CreateRemoteThread(process, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(set_clan_tag_func_addr), tag_native_addr, 0, NULL);
	if (set_clan_tag_thread == NULL) {
		VirtualFreeEx(process, name_param_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, set_clan_tag_func_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, tag_native_addr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(tag, tag_native);
		return 0;
	}
	DWORD exit_code;
	if (WaitForSingleObject(set_clan_tag_thread, INFINITE) != WAIT_OBJECT_0 || !GetExitCodeThread(set_clan_tag_thread, &exit_code)) {
		VirtualFreeEx(process, name_param_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, set_clan_tag_func_addr, 0, MEM_RELEASE);
		VirtualFreeEx(process, tag_native_addr, 0, MEM_RELEASE);
		env->ReleaseStringUTFChars(tag, tag_native);
		CloseHandle(set_clan_tag_thread);
		return 0;
	}
	VirtualFreeEx(process, name_param_addr, 0, MEM_RELEASE);
	VirtualFreeEx(process, set_clan_tag_func_addr, 0, MEM_RELEASE);
	VirtualFreeEx(process, tag_native_addr, 0, MEM_RELEASE);
	env->ReleaseStringUTFChars(tag, tag_native);
	CloseHandle(set_clan_tag_thread);
	return exit_code;
}

JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_moduleSize0
(JNIEnv *env, jobject this_obj, jstring module) {
	const char *module_name = env->GetStringUTFChars(module, JNI_FALSE);
	HANDLE snapshot_handle = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE | TH32CS_SNAPMODULE32, ProcessId(env, this_obj)->second);
	while (GetLastError() == ERROR_BAD_LENGTH) {
		snapshot_handle = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE | TH32CS_SNAPMODULE32, ProcessId(env, this_obj)->second);
	}
	if (snapshot_handle == INVALID_HANDLE_VALUE) {
		env->ReleaseStringUTFChars(module, module_name);
		return 0;
	}
	MODULEENTRY32 module_entry;
	module_entry.dwSize = sizeof(MODULEENTRY32);
	if (!Module32First(snapshot_handle, &module_entry)) {
		CloseHandle(snapshot_handle);
		env->ReleaseStringUTFChars(module, module_name);
		return 0;
	}
	do {
		if (strcmp(module_entry.szModule, module_name)) {
			continue;
		}
		CloseHandle(snapshot_handle);
		env->ReleaseStringUTFChars(module, module_name);
		return module_entry.modBaseSize;
	} while (Module32Next(snapshot_handle, &module_entry));
	CloseHandle(snapshot_handle);
	env->ReleaseStringUTFChars(module, module_name);
	return 0;
}

JNIEXPORT jstring JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_moduleFileName
  (JNIEnv *env, jobject this_obj, jlong process_handle, jlong module) {
	TCHAR filename[MAX_PATH];
	return GetModuleFileNameEx(reinterpret_cast<HANDLE>(process_handle), reinterpret_cast<HMODULE>(module), filename, sizeof(filename) / sizeof(TCHAR)) == 0 ? NULL : env->NewStringUTF(filename);
}
