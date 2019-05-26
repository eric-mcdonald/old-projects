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

#include "memory_stream.h"  // NOLINT

#include "lanius.h"  // NOLINT

#define IOCTL_READ_PROC_MEM CTL_CODE(SIOCTL_TYPE, 2048, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_WRITE_PROC_MEM CTL_CODE(SIOCTL_TYPE, 2049, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_HIJACK_THREAD CTL_CODE(SIOCTL_TYPE, 2051, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_ALLOC_VIRTUAL_MEM CTL_CODE(SIOCTL_TYPE, 2052, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_FREE_VIRTUAL_MEM CTL_CODE(SIOCTL_TYPE, 2053, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_SUSPEND_THREAD CTL_CODE(SIOCTL_TYPE, 2054, METHOD_BUFFERED, FILE_ANY_ACCESS)  // TODO(Eric McDonald): Implement this IOCTL code.
#define IOCTL_RESUME_THREAD CTL_CODE(SIOCTL_TYPE, 2055, METHOD_BUFFERED, FILE_ANY_ACCESS)  // TODO(Eric McDonald): Implement this IOCTL code.

namespace lanius {

	struct MemoryIoRequest {
		DWORD proc_id;
		void *base_addr;
		void *buffer;
		size_t buffer_sz;
	};
	struct Image {
		wchar_t full_img_name[MAX_PATH];
		HANDLE proc_id;
		ULONGLONG base_addr;
		SIZE_T size;
	};
	struct GetModulesRequest {
		DWORD proc_id;
		struct Image loaded_images_buf[256];
		size_t loaded_images_ret;
	};
	struct HijackThreadRequest {
		DWORD proc_id;
		DWORD thread_id;
		void *new_rip;
	};
	struct AllocVirtualMemoryRequest {
		DWORD proc_id;
		void *base_addr;
		ULONG_PTR zero_bits;
		SIZE_T region_sz;
		DWORD alloc_type;
		DWORD protect;
	};
	struct FreeVirtualMemoryRequest {
		DWORD proc_id;
		void *base_addr;
		SIZE_T region_sz;
		DWORD free_type;
	};
	struct SuspendThreadRequest {
		DWORD thread_id;
		LONG prev_suspend_count;
		void *zw_suspend_thread_ptr;
	};
	struct ResumeThreadRequest {
		DWORD thread_id;
		LONG suspend_count;
		void *zw_resume_thread_ptr;
	};

	MemoryStream::MemoryStream(const std::string &proc_name) {
		Open();
		if (FindProcess(proc_name, proc_info_, device_file_) != ErrorCodes::kSuccess) {
			memset(&proc_info_, 0, sizeof(proc_info_));
			device_file_ = INVALID_HANDLE_VALUE;
		}
	}
	MemoryStream::MemoryStream(const MemoryStream &source) : proc_info_(source.proc_info_) {
		Open();
	}
	MemoryStream::MemoryStream() : device_file_(INVALID_HANDLE_VALUE) {}
	MemoryStream::~MemoryStream() {
		erase_from_cache_ = false;
		for (const auto &it : allocated_mem_) {
			Free(it);
		}
		allocated_mem_.clear();
		erase_from_cache_ = true;
		Close();
	}
	const ProcessInfo &MemoryStream::proc_info() const {
		return proc_info_;
	}
	bool MemoryStream::operator==(const MemoryStream &operand) const {
		return proc_info_.pid == operand.proc_info_.pid;
	}
	bool MemoryStream::operator!=(const MemoryStream &operand) const {
		return !(*this == operand);
	}
	bool MemoryStream::IsOpen() const {
		ProcessInfo proc_info_tmp = { 0 };
		return device_file_ != INVALID_HANDLE_VALUE && FindProcess(proc_info_.exe_filename, proc_info_tmp, device_file_) == ErrorCodes::kSuccess;
	}
	ErrorCode MemoryStream::Open() {
		return OpenCheatDevice(&device_file_);
	}
	ErrorCode MemoryStream::Close() {
		ErrorCode error = ErrorCodes::kSuccess;
		if (device_file_ != INVALID_HANDLE_VALUE) {
			error = CloseNativeHandle(device_file_);
			device_file_ = INVALID_HANDLE_VALUE;
		}
		return error;
	}
	ErrorCode MemoryStream::Read(const ExternalAddr base_addr, void *buffer, const size_t buffer_sz) const {
		MemoryIoRequest request = { 0 };
		request.proc_id = proc_info_.pid;
		request.base_addr = reinterpret_cast<void *>(base_addr);
		request.buffer = buffer;
		request.buffer_sz = buffer_sz;
		DWORD bytes_ret;
		if (!DeviceIoControl(device_file_, IOCTL_READ_PROC_MEM, &request, sizeof(request), NULL, 0, &bytes_ret, NULL)) {
			return GetLastError();
		}
		return ErrorCodes::kSuccess;
	}
	ErrorCode MemoryStream::Write(const ExternalAddr base_addr, const void *buffer, const size_t buffer_sz) const {
		MemoryIoRequest request = { 0 };
		request.proc_id = proc_info_.pid;
		request.base_addr = reinterpret_cast<void *>(base_addr);
		request.buffer = const_cast<void *>(buffer);
		request.buffer_sz = buffer_sz;
		DWORD bytes_ret;
		if (!DeviceIoControl(device_file_, IOCTL_WRITE_PROC_MEM, &request, sizeof(request), NULL, 0, &bytes_ret, NULL)) {
			return GetLastError();
		}
		return ErrorCodes::kSuccess;
	}
	ErrorCode MemoryStream::FindSig(std::string signature, const ExternalAddr start_addr, const size_t trace_len, ExternalAddr &found_addr, const size_t skip_amount) const {
		signature = RemoveChar(signature, ' ');
		if (signature.length() % 2 != 0 || trace_len == 0U) {
			return ErrorCodes::kIllegalParam;
		}
		const size_t read_buf_sz = signature.length() / 2;
		size_t skips_remaining = skip_amount;
		for (ExternalAddr address = start_addr; address < start_addr + trace_len; ++address) {
			Byte *read_buf = new Byte[read_buf_sz]{ 0 };
			if (Read(address, read_buf, read_buf_sz) == ErrorCodes::kSuccess) {
				bool matches = true;
				for (size_t char_count = 0, read_buf_count = 0; char_count < signature.length() - 1 && read_buf_count < read_buf_sz; char_count += 2, ++read_buf_count) {
					std::string byte_str = signature.substr(char_count, 2);
					if (byte_str.find("?") != std::string::npos) {
						continue;
					}
					byte_str = "0x" + byte_str;
					if (strtoul(byte_str.c_str(), NULL, 16) != read_buf[read_buf_count]) {
						matches = false;
						break;
					}
				}
				if (matches) {
					if (skips_remaining == 0U) {
						delete[] read_buf;
						read_buf = nullptr;
						found_addr = address;
						return ErrorCodes::kSuccess;
					}
					else {
						--skips_remaining;
					}
				}
			}
			delete[] read_buf;
			read_buf = nullptr;
		}
		return skips_remaining != skip_amount ? ErrorCodes::kSigFoundButSkipped : ErrorCodes::kSigNotFound;
	}
	ErrorCode MemoryStream::Allocate(ExternalAddr &base_addr, size_t size) {
		//if (base_addr == kNull) {  // Currently unsupported; the callee must specify a non-null base address.
			//return ErrorCodes::kIllegalParam;
		//}
		//for (const auto &it : allocated_mem_) {
			//if (it == base_addr) {
				//return ErrorCodes::kIllegalParam;
			//}
		//}
		AllocVirtualMemoryRequest request = { 0 };
		request.proc_id = proc_info_.pid;
		request.base_addr = reinterpret_cast<void *>(base_addr);
		request.region_sz = size;
		request.alloc_type = MEM_COMMIT | MEM_RESERVE;
		request.protect = PAGE_EXECUTE_READWRITE;
		DWORD bytes_ret;
		if (!DeviceIoControl(device_file_, IOCTL_ALLOC_VIRTUAL_MEM, &request, sizeof(request), &request, sizeof(request), &bytes_ret, NULL)) {
			return GetLastError();
		}
		base_addr = reinterpret_cast<ExternalAddr>(request.base_addr);
		allocated_mem_.push_back(base_addr);
		return ErrorCodes::kSuccess;
	}
	ErrorCode MemoryStream::Free(const ExternalAddr base_addr) {
		FreeVirtualMemoryRequest request = { 0 };
		request.proc_id = proc_info_.pid;
		request.base_addr = reinterpret_cast<void *>(base_addr);
		request.region_sz = 0;
		request.free_type = MEM_RELEASE;
		DWORD bytes_ret;
		if (!DeviceIoControl(device_file_, IOCTL_FREE_VIRTUAL_MEM, &request, sizeof(request), &request, sizeof(request), &bytes_ret, NULL)) {
			return GetLastError();
		}
		if (erase_from_cache_) {
			for (AllocatedMemCache::iterator it = allocated_mem_.begin(); it != allocated_mem_.end();) {
				if (*it == base_addr) {
					it = allocated_mem_.erase(it);
				}
				else {
					++it;
				}
			}
		}
		return ErrorCodes::kSuccess;
	}

}  // namespace lanius
