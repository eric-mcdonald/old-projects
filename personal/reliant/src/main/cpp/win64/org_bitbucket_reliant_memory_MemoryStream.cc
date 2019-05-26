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
/*JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_disableMouse0
  (JNIEnv *env, jobject this_obj) {
	return reinterpret_cast<jlong>(SetWindowsHookEx(WH_MOUSE_LL, LowLevelMouseProc, NULL, 0));
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

/*
 * Class:     org_bitbucket_reliant_memory_MemoryStream
 * Method:    captureInterface
 * Signature: (Ljava/lang/String;J)I
 */
/*JNIEXPORT jint JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_captureInterface
(JNIEnv *env, jobject this_obj, jstring name, jlong module) {
	const char *name_native = env->GetStringUTFChars(name, JNI_FALSE);
	const size_t name_len = strlen(name_native);
	CONST HANDLE process_handle = ProcessHandle(env, this_obj)->second;
	LPVOID pages_addr = VirtualAllocEx(process_handle, NULL, name_len, MEM_COMMIT | MEM_RESERVE, PAGE_READWRITE);
	if (pages_addr == NULL) {
		std::cout << "t3" << std::endl;
		env->ReleaseStringUTFChars(name, name_native);
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	if (!WriteProcessMemory(process_handle, pages_addr, name_native, name_len, NULL)) {
		std::cout << "t4" << std::endl;
		env->ReleaseStringUTFChars(name, name_native);
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	typedef jint (*CreateInterfaceFunc)(const char *name_sz, int *ret_code);
	CreateInterfaceFunc CreateInterface = reinterpret_cast<CreateInterfaceFunc>(GetProcAddress(reinterpret_cast<HMODULE>(module), "CreateInterface"));
	CONST HANDLE create_interface_thread = CreateRemoteThread(process_handle, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(CreateInterface), pages_addr, 0, NULL);
	if (create_interface_thread == NULL) {
		std::cout << GetLastError() << std::endl;
		env->ReleaseStringUTFChars(name, name_native);
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	if (WaitForSingleObject(create_interface_thread, INFINITE) == WAIT_FAILED) {
		std::cout << "t6" << std::endl;
		env->ReleaseStringUTFChars(name, name_native);
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
	HANDLE snapshot_handle = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE | TH32CS_SNAPMODULE32, ProcessId(env, this_obj)->second);
	while (GetLastError() == ERROR_BAD_LENGTH) {
		snapshot_handle = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE | TH32CS_SNAPMODULE32, ProcessId(env, this_obj)->second);
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

/*typedef struct _THREAD_BASIC_INFORMATION {
	NTSTATUS                ExitStatus;
	PVOID                   TebBaseAddress;
	CLIENT_ID               ClientId;
	KAFFINITY               AffinityMask;
	KPRIORITY               Priority;
	KPRIORITY               BasePriority;
} THREAD_BASIC_INFORMATION, *PTHREAD_BASIC_INFORMATION;

JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_memory_MemoryStream_callFunc
(JNIEnv *env, jobject this_obj, jlong process_handle, jlong address, jlong param, jint timeout) {
	CONST HANDLE process = reinterpret_cast<HANDLE>(process_handle);
	CONST HANDLE exec_thread = CreateRemoteThread(process, NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(address), reinterpret_cast<LPVOID>(param), CREATE_SUSPENDED, NULL);
	if (exec_thread == NULL) {
		return 0;
	}
	bool success = true;
	CONST HANDLE thread_snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPTHREAD, 0);
	if (thread_snapshot != INVALID_HANDLE_VALUE) {
		THREADENTRY32 thread_entry;
		thread_entry.dwSize = sizeof(THREADENTRY32);
		HANDLE proc_main_thread = NULL;
		if (Thread32First(thread_snapshot, &thread_entry)) {
			CONST DWORD process_id = ProcessId(env, this_obj)->second;
			do {
				if (thread_entry.th32OwnerProcessID == process_id) {
					proc_main_thread = OpenThread(THREAD_QUERY_INFORMATION, FALSE, thread_entry.th32ThreadID);
					break;
				}
			} while (Thread32Next(thread_snapshot, &thread_entry));
			if (proc_main_thread != NULL) {
				typedef NTSTATUS (WINAPI *NtQueryInformationThreadFunc)(HANDLE, THREADINFOCLASS, PVOID, ULONG, PULONG);
				THREAD_BASIC_INFORMATION main_thread_info, exec_thread_info;
				SecureZeroMemory(&main_thread_info, sizeof(main_thread_info));
				SecureZeroMemory(&exec_thread_info, sizeof(exec_thread_info));
				std::cout << sizeof(main_thread_info) << std::endl;
				// Eric: ntdll.dll is loaded in the same address for all processes; therefore, we can simply call our GetProcAddress
				std::cout << "ntdll:" << std::hex << GetModuleHandle("ntdll.dll") << std::endl;
				const NtQueryInformationThreadFunc query_info_thread_func = reinterpret_cast<NtQueryInformationThreadFunc>(GetProcAddress(GetModuleHandle("ntdll.dll"), "NtQueryInformationThread"));
				std::cout << "NtQueryInformationThreadFunc: " << std::hex << GetProcAddress(GetModuleHandle("ntdll.dll"), "NtQueryInformationThread") << ":" << std::dec << GetLastError() << std::endl;
				if (NT_SUCCESS(query_info_thread_func(proc_main_thread, ThreadBasicInformation, &main_thread_info, sizeof(main_thread_info), NULL)) && NT_SUCCESS(query_info_thread_func(exec_thread, ThreadBasicInformation, &exec_thread_info, sizeof(exec_thread_info), NULL))) {
					std::cout << "main_teb: " << std::hex << main_thread_info.TebBaseAddress << std::endl;
					std::cout << "exec_teb: " << std::hex << exec_thread_info.TebBaseAddress << std::endl;
					DWORD tls_offset;
#ifdef _M_AMD64
					tls_offset = 0x1480;
					std::cout << "x64" << std::endl;
#else
					tls_offset = 0xE10;
#endif
					std::cout << "tls_offset:" << std::hex << tls_offset << std::endl;
					PVOID exec_tls_buffer, main_tls_buffer;
					const size_t process_ptr_sz = sizeof(DWORD);
					if(ReadProcessMemory(process, reinterpret_cast<LPCVOID>(exec_thread_info.TebBaseAddress + tls_offset), &exec_tls_buffer, process_ptr_sz, NULL) && ReadProcessMemory(process, reinterpret_cast<LPCVOID>(main_thread_info.TebBaseAddress + tls_offset), &main_tls_buffer, process_ptr_sz, NULL)) {
						const size_t tls_slots_sz = 64;
						PVOID prev_tls_slots[tls_slots_sz];
						// TODO(Eric) Refactor the ReadProcessMemory usage: read the entire array
						for (size_t i = 0; i < tls_slots_sz; ++i) {
							PVOID main_tls_slot;
							if (!ReadProcessMemory(process, reinterpret_cast<LPCVOID>(exec_tls_buffer + i * process_ptr_sz), &prev_tls_slots[i], process_ptr_sz, NULL) || !ReadProcessMemory(process, reinterpret_cast<LPCVOID>(main_tls_buffer + i * process_ptr_sz), &main_tls_slot, process_ptr_sz, NULL)) {
								continue;
							}
							WriteProcessMemory(process, reinterpret_cast<LPVOID>(exec_tls_buffer + i * process_ptr_sz), &main_tls_slot, process_ptr_sz, NULL);
						}
						if (ResumeThread(exec_thread) != -1) {
							success = WaitForSingleObject(exec_thread, timeout) != WAIT_OBJECT_0;
						}
						for (size_t i = 0; i < tls_slots_sz; ++i) {
							WriteProcessMemory(process, reinterpret_cast<LPVOID>(exec_tls_buffer + i * process_ptr_sz), &prev_tls_slots[i], process_ptr_sz, NULL);
						}
					} else {
						success = false;
					}
				} else {
					success = false;
				}
				CloseHandle(proc_main_thread);
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
