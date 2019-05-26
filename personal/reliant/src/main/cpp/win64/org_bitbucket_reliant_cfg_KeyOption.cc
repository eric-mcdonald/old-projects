/*
 * org_bitbucket_reliant_cfg_KeyOption.cc
 *
 *  Created on: Dec 18, 2016
 *      Author: Eric McDonald
 */




#include "org_bitbucket_reliant_cfg_KeyOption.h"

#include <windows.h>

JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_cfg_KeyOption_keyDown
  (JNIEnv *env, jobject this_obj, jint key) {
	CONST SHORT key_state = GetAsyncKeyState(key);
	return key_state != 0 && key_state & 0x8000;
}

JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_cfg_KeyOption_keyPressed
  (JNIEnv *env, jobject this_obj, jint key) {
	CONST SHORT key_state = GetAsyncKeyState(key);
	return key_state != 0 && key_state & 0x0001;
}

JNIEXPORT jint JNICALL Java_org_bitbucket_reliant_cfg_KeyOption_pressKey
  (JNIEnv *env, jobject this_obj, jint key, jboolean release) {
	INPUT input;
	input.type = INPUT_KEYBOARD;
	KEYBDINPUT key_in;
	key_in.wVk = key;
	key_in.wScan = 0;
	key_in.dwFlags = release ? KEYEVENTF_KEYUP : 0;
	key_in.time = 0;
	key_in.dwExtraInfo = GetMessageExtraInfo();
	input.ki = key_in;
	return SendInput(1, &input, sizeof(input));
}
