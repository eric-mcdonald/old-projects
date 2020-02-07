#pragma once
#pragma warning(disable : 4251)

#include <string>
#include <Windows.h>

#include "px_def.h"

namespace projx {

	class PROJECTXSHARED_API SharedMemoryManager {
		static constexpr char *kSharedMappingPrefix = "Global\\PXSHARED_";
		std::string map_key;
		size_t map_size;
		HANDLE mem_mapping;
	public:
		SharedMemoryManager(const std::string &/*map_subkey*/, DWORD /*map_size_*/);
		SharedMemoryManager(const SharedMemoryManager &) = delete;
		SharedMemoryManager(SharedMemoryManager &&);
		~SharedMemoryManager();
		ProgError Write(const void * /*data*/) const;
		ProgError Read(void * /*buffer*/) const;
		std::string get_map_key() const;
		size_t get_map_size() const;
		bool operator==(const SharedMemoryManager &) const;
		bool operator!=(const SharedMemoryManager &) const;
	private:
		ProgError GetOrCreateMapping(DWORD /*max_sz*/, HANDLE *) const;
	public:
		SharedMemoryManager &operator=(const SharedMemoryManager &) = delete;
		SharedMemoryManager &operator=(SharedMemoryManager &&);
	};

}  // namespace projx
