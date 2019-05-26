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
#include <vector>

#include "native_util.h"  // NOLINT

namespace lanius {

	using Byte = unsigned char;
	using AllocatedMemCache = std::vector<ExternalAddr>;
	class MemoryStream {
		ProcessInfo proc_info_;
		NativeHandle device_file_;
		AllocatedMemCache allocated_mem_;
		bool erase_from_cache_ = true;
		ErrorCode Open();
		ErrorCode Close();
	public:
		explicit MemoryStream(const std::string &);
		MemoryStream(const MemoryStream &);
		MemoryStream();
		~MemoryStream();
		const ProcessInfo &proc_info() const;
		bool operator==(const MemoryStream &) const;
		bool operator!=(const MemoryStream &) const;
		bool IsOpen() const;
		ErrorCode Read(const ExternalAddr, void * /*buffer*/, const size_t) const;
		ErrorCode Write(const ExternalAddr, const void * /*buffer*/, const size_t) const;
		ErrorCode Allocate(ExternalAddr &, size_t);
		ErrorCode Free(const ExternalAddr);

		// TODO(Eric McDonald): Store found signature read buffer + build version of the game (from NT header) in a data file.
		// TODO(Eric McDonald): Store signatures in a configuration file.

		ErrorCode FindSig(std::string, const ExternalAddr /*start_addr*/, const size_t /*trace_len*/, ExternalAddr &/*found_addr*/, const size_t /*skip_amount*/ = 0U) const;
	};

	namespace test {

		static const std::string kTargetProc = "FortniteClient-Win64-Shipping.exe";

	}  // namespace test

}  // namespace lanius
