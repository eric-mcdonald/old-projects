/*
 * org_bitbucket_reliant_Main.cc
 *
 *  Created on: Dec 15, 2016
 *      Author: Eric McDonald
 */




#include "org_bitbucket_reliant_Main.h"

#include <windows.h>
#include <tlhelp32.h>

JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_Main_processActive
  (JNIEnv *env, jclass this_class, jstring process) {
	CONST HANDLE snapshot_handle = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	if (snapshot_handle == INVALID_HANDLE_VALUE) {
		return false;
	}
	const char *process_chars = env->GetStringUTFChars(process, JNI_FALSE);
	PROCESSENTRY32 process_entry;
	process_entry.dwSize = sizeof(PROCESSENTRY32);
	if (!Process32First(snapshot_handle, &process_entry)) {
		goto failure;
	}
	do {
		if (strcmp(process_entry.szExeFile, process_chars) == 0) {
			env->ReleaseStringUTFChars(process, process_chars);
			CloseHandle(snapshot_handle);
			return true;
		}
	} while (Process32Next(snapshot_handle, &process_entry));
	failure:
	env->ReleaseStringUTFChars(process, process_chars);
	CloseHandle(snapshot_handle);
	return false;
}

JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_Main_cursorShowing
  (JNIEnv *env, jclass this_class) {
	CURSORINFO cursor_info;
	cursor_info.cbSize = sizeof(CURSORINFO);
	return GetCursorInfo(&cursor_info) && cursor_info.flags & CURSOR_SHOWING;
}

/*
 * Class:     org_bitbucket_reliant_Main
 * Method:    updateWindow
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_bitbucket_reliant_Main_updateWindow
  (JNIEnv *env, jclass this_class, jlong window) {
	MSG msg;
	if (PeekMessage(&msg, reinterpret_cast<HWND>(window), 0, 0, PM_REMOVE)) {
		DispatchMessage(&msg);
		TranslateMessage(&msg);
	}
}

JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_Main_windowIsForeground
  (JNIEnv *env, jclass this_class, jstring window) {
	const char *window_title = env->GetStringUTFChars(window, JNI_FALSE);
	CONST BOOL window_foreground = GetForegroundWindow() == FindWindow(NULL, window_title);
	env->ReleaseStringUTFChars(window, window_title);
	return window_foreground;
}
