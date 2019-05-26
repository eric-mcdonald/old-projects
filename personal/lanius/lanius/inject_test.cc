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

#include "stdafx.h"  // NOLINT

#include "inject_test.h"  // NOLINT
#include "native_util.h"

namespace lanius {

	namespace test {

		ErrorCode DoApcInjectTest() {
			ErrorCode error = ErrorCodes::kSuccess;
			NativeHandle cheat_device = INVALID_HANDLE_VALUE;
			error = OpenCheatDevice(&cheat_device);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			error = QueueInjectLib("D:\\lanius\\lanius-test-injected-dll\\x64\\Debug\\lanius-test-injected-dll\\lanius-test-injected-dll.dll", kTargetProc, cheat_device);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			CloseNativeHandle(cheat_device);
			return error;
		}

	}  // namespace test

}  // namespace lanius
