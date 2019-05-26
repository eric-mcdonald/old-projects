/*
Author: Eric McDonald
Date created: 2017-09-14
File version: 1.3
Program: IPC144 Labs - Main Function Bridger
Purpose: To bridge different "main" functions so one can put all of the files in one project (LNK2005 is thrown if this is not done).
*/

// Eric McDonald: These files were made for use throughout different school assignments.

#pragma once

#include <stdlib.h>

// Eric McDonald: All errors caused by the bridge start with this value and move in the direction of MAIN_ERROR_BASE - 1.
#define MAIN_ERROR_BASE -999

typedef int (*MainFn)(int argc, char *argv[]);
typedef int Boolean;

static const int kNoError = 0, kInvalidFunc = MAIN_ERROR_BASE, kIdOutOfRange = MAIN_ERROR_BASE - 1, kRegisterLimitReached = MAIN_ERROR_BASE - 2, kAlreadyRegisteredFunc = MAIN_ERROR_BASE - 3, kPromptFailed = MAIN_ERROR_BASE - 4;
static const size_t kMaxRegisteredMains = 32;
static const Boolean kFalse = 0, kTrue = 1;
static MainFn *registered_mains = NULL;
static size_t registered_mains_sz = 0;

Boolean ContainsMain(MainFn func);
int RegisterMain(MainFn func);
int ExecMain(size_t func_id, int argc, char *argv[]);
int PromptMain(const char *prompt_msg, int argc, char *argv[]);