/*
Author: Eric McDonald
Date created: 2017-09-14
File version: 1.5
Program: IPC144 Labs - Main Function Bridger
Purpose: Defines implementations of main_bridge.h.
*/

// Eric McDonald: Surpresses C4996 so the compiler does not complain about not using MSVC's 'secure' versions of standard library functions.
#pragma warning(disable : 4996)

#include "main_bridge.h"

#include <stdio.h>
#include <string.h>
//#include <windows.h>

#include "main_impl_defs.h"

Boolean ContainsMain(MainFn func) {
	if (registered_mains == NULL) {
		return kFalse;
	}
	for (size_t i = 0; i < registered_mains_sz; ++i) {
		if (registered_mains[i] == func) {
			return kTrue;
		}
	}
	return kFalse;
}
int RegisterMain(MainFn func) {
	if (func == NULL) {
		return kInvalidFunc;
	}
	if (ContainsMain(func) != kFalse) {
		return kAlreadyRegisteredFunc;
	}
	if (registered_mains == NULL)
		registered_mains = (MainFn*) malloc(sizeof(MainFn) * kMaxRegisteredMains);
	if (registered_mains_sz >= kMaxRegisteredMains) {
		return kRegisterLimitReached;
	}
	registered_mains[registered_mains_sz++] = func;
	return kNoError;
}
int ExecMain(size_t func_id, int argc, char *argv[]) {
	if (func_id >= registered_mains_sz) {
		return kIdOutOfRange;
	}
	if (registered_mains[func_id] == NULL) {
		return kInvalidFunc;
	}
	return registered_mains[func_id](argc, argv);
}
void PrintBridgeMsg(const char *message) {
	const char *bridge_prefix = "[Main Function Bridger]: ";
	size_t message_len = strlen(message), prefix_len = strlen(bridge_prefix);
	size_t prefixed_msg_len = prefix_len + message_len + 1; // Eric McDonald: +1 char for the string terminator '\0'.
	char *prefixed_msg = (char*) malloc(prefixed_msg_len);
	if (prefixed_msg == NULL) {
		exit(kPromptFailed);
	}
	prefixed_msg[prefixed_msg_len - 1] = '\0';
	strcpy(prefixed_msg, bridge_prefix);
	strcat(prefixed_msg, message);
	printf(prefixed_msg);
	free(prefixed_msg);
}
int PromptMain(const char *prompt_msg, int argc, char *argv[]) {
	PrintBridgeMsg("Published by Eric McDonald.\n");
	PrintBridgeMsg(prompt_msg);
	size_t func_id;
	int chars_read = scanf("%ui", &func_id);
	if (chars_read < 0) {
		return chars_read;
	}
	return ExecMain(func_id, argc, argv);
}
int main(int argc, char *argv[]) {
	RegisterMain(CardValIfMain);
	RegisterMain(CardValSwitchMain);
	RegisterMain(SeriesWhileMain);
	RegisterMain(SeriesForMain);
	// TODO: Register your main function pointers here:

	int exit_code = PromptMain("Please enter a main function ID: ", argc, argv);
	//Sleep(INFINITE);
	return exit_code;
}