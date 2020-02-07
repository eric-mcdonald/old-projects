#include "stdafx.h"

#include "shared_mem.h"

namespace projx {

	SharedMemoryManager::SharedMemoryManager(const std::string &map_subkey, DWORD map_size_) : map_key(kSharedMappingPrefix + map_subkey), mem_mapping(NULL), map_size(0) {
		if (GetOrCreateMapping(map_size_, &mem_mapping) == ErrorCodes::kSuccess) {
			map_size = map_size_;
		}
	}
	SharedMemoryManager::SharedMemoryManager(SharedMemoryManager &&src) {
		map_key = src.map_key;
		mem_mapping = src.mem_mapping;
		map_size = src.map_size;
		src.mem_mapping = NULL;
	}
	SharedMemoryManager::~SharedMemoryManager() {
		if (mem_mapping != NULL) {
			CloseHandle(mem_mapping);
			mem_mapping = NULL;
		}
	}
	ProgError SharedMemoryManager::Write(const void *data) const {
		if (data == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		if (map_size == 0) {
			return ErrorCodes::kInvalidState;
		}
		LPVOID map_buf = MapViewOfFile(mem_mapping, FILE_MAP_WRITE, 0, 0, map_size);
		if (map_buf == NULL) {
			return GetLastError();
		}
		memcpy(map_buf, data, map_size);
		if (!UnmapViewOfFile(map_buf)) {
			return GetLastError();
		}
		return ErrorCodes::kSuccess;
	}
	ProgError SharedMemoryManager::Read(void *buffer) const {
		if (buffer == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		if (map_size == 0) {
			return ErrorCodes::kInvalidState;
		}
		LPVOID map_buf = MapViewOfFile(mem_mapping, FILE_MAP_READ, 0, 0, map_size);
		if (map_buf == NULL) {
			return GetLastError();
		}
		memcpy(buffer, map_buf, map_size);
		if (!UnmapViewOfFile(map_buf)) {
			return GetLastError();
		}
		return ErrorCodes::kSuccess;
	}
	ProgError SharedMemoryManager::GetOrCreateMapping(DWORD max_sz, HANDLE *mapping_out) const {
		if (map_key.empty() || mapping_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		*mapping_out = OpenFileMapping(FILE_MAP_ALL_ACCESS, FALSE, map_key.c_str());
		if (*mapping_out == NULL) {
			*mapping_out = CreateFileMapping(INVALID_HANDLE_VALUE, NULL, PAGE_READWRITE, 0, max_sz, map_key.c_str());
			if (*mapping_out == NULL) {
				return GetLastError();
			}
			if (GetLastError() != ERROR_ALREADY_EXISTS) {
				LPVOID map_buf = MapViewOfFile(*mapping_out, FILE_MAP_WRITE, 0, 0, max_sz);
				if (map_buf == NULL) {
					return GetLastError();
				}
				memset(map_buf, 0, max_sz);
				if (!UnmapViewOfFile(map_buf)) {
					return GetLastError();
				}
			}
		}
		return ErrorCodes::kSuccess;
	}
	size_t SharedMemoryManager::get_map_size() const {
		return map_size;
	}
	std::string SharedMemoryManager::get_map_key() const {
		return map_key;
	}
	bool SharedMemoryManager::operator==(const SharedMemoryManager &rhs) const {
		return map_key == rhs.map_key;
	}
	bool SharedMemoryManager::operator!=(const SharedMemoryManager &rhs) const {
		return !(*this == rhs);
	}
	SharedMemoryManager &SharedMemoryManager::operator=(SharedMemoryManager &&rhs) {
		if (this != &rhs) {
			if (mem_mapping != NULL) {
				CloseHandle(mem_mapping);
				mem_mapping = NULL;
			}
			map_key = rhs.map_key;
			mem_mapping = rhs.mem_mapping;
			map_size = rhs.map_size;
			rhs.mem_mapping = NULL;
		}
		return *this;
	}

}  // namespace projx
