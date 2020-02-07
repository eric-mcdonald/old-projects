#pragma once

#if _WIN32 || _WIN64
#if _WIN64
#define PROJECTX_ENV64
#else
#define PROJECTX_ENV32
#endif
#endif

#include <unordered_map>

#include "projectx-shared.h"

namespace projx {

	enum PROJECTXSHARED_API ErrorCodes {
		kSuccess,
		kInvalidParam = -1,
		kIrcErr = -2,
		kInvalidState = -3,
		kFileNotFound = -4,
		kInvalidApiCall = -5,
		kOutOfMem = -6,
		kInvalidOp = -7,
		kFailedApiCall = -8,
		kFileInaccessible = -9,
	};
	PROJECTXSHARED_API typedef int ProgError;
	PROJECTXSHARED_API typedef unsigned char Byte;

	PROJECTXSHARED_API extern const char *kIrcController;
	PROJECTXSHARED_API extern const char *kFakeProgInfFile;
	extern HMODULE g_shared_api_dll;

}  // namespace projx
