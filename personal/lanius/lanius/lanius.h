/*
Copyright 2018 Eric McDonald

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

#pragma once

#include <string>

#define SIOCTL_TYPE 40000

namespace lanius {

	typedef int ErrorCode;  // Error data type that includes native/STL errors and our error codes.

	static const std::string &kCheatName = "Lanius";
	static constexpr float kCheatVer = 0.1F;
	static constexpr ErrorCode kErrorBase = -999;  // All errors start at this value and subsequent values are decremented from it.
	static bool dse_patched = false;
	// For holding testing-phase functionality.
	namespace test {

		static constexpr bool kRestartOnShutdown = false, kRestartForceHung = true,  // For automatically restarting the user's PC to avoid a BSOD error (see WillBsod function).
			kRestartGame = true,  // For cleanly stopping BEService.exe to help avoid detection.
			kWillAssertVolType = false;

	}  // namespace test

	enum ErrorCodes {
		kSuccess = 0,
		kInvalidVolType = kErrorBase - 0,
		kInvalidNativeHandle = kErrorBase - 1,
		kUnsupportedOs = kErrorBase - 2,
		kIllegalParam = kErrorBase - 3,
		kProcNotFound = kErrorBase - 4,
		kInvalidProc = kErrorBase - 5,
		kVolNotFound = kErrorBase - 6,
		kInvalidVolPath = kErrorBase - 7,
		kNoModulesFound = kErrorBase - 8,
		kSigNotFound = kErrorBase - 9,
		kSigFoundButSkipped = kErrorBase - 10,
		kInvalidDeviceCtx = kErrorBase - 11,
		kFontNotFound = kErrorBase - 12,
		kD3dFailure = kErrorBase - 13,
		kGdiFailure = kErrorBase - 14,
		kGetDisplaySettingsFailed = kErrorBase - 15,
		kAtlFailure = kErrorBase - 16,
		kInvalidCaps = kErrorBase - 17,
		kWindowNotFound = kErrorBase - 18,
		kUser32Failure = kErrorBase - 19,
		kFileNotFound = kErrorBase - 20,
		kThreadAlreadySuspended = kErrorBase - 21,
		kInvalidThread = kErrorBase - 22,
		kInvalidCtx = kErrorBase - 23,
	};

	ErrorCode ClearArtifacts();
	inline bool WillBsod() {
		// The vulnerability used to load the unsigned driver will cause a delayed BSOD on Windows 8.1+
		return dse_patched && IsWindows8Point1OrGreater();
	}
	inline bool IsCheatError(const ErrorCode error) {
		return error <= kErrorBase;
	}

}  // namespace lanius
