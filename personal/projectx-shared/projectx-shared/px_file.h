#pragma once
#pragma warning(disable : 4251)

#include <string>
#include <fstream>

#include "string_util.h"
#include "projectx-shared.h"

namespace projx {

	class PROJECTXSHARED_API File {
		std::string path;
	public:
		File(std::string /*path_*/);
		File(HMODULE /*library*/);
		std::string get_path() const;
		bool Exists() const;
		ProgError GetRoot(std::string *) const;
		ProgError CopyTo(std::string /*dest*/, bool /*overwrite*/ = true) const;
		bool CanModify() const;
		ProgError Delete() const;
	};

	PROJECTXSHARED_API inline std::string FileFormatPath(const std::string &path, bool use_win_fmt) {
		return use_win_fmt ? StrReplaceAll(path, "/", "\\") : StrReplaceAll(path, "\\", "/");
	}
	PROJECTXSHARED_API ProgError FileAllocModFile(std::string mod_path, File **file_out);
	PROJECTXSHARED_API inline bool FileArePathsEqual(std::string path1, std::string path2) {
		return StrEqIgnoreCase(FileFormatPath(path1, false), FileFormatPath(path2, false));
	}
	PROJECTXSHARED_API inline std::string FileSplitDir(std::string path, std::string *filename_out) {
		path = FileFormatPath(path, false);
		size_t found_sep = path.find_last_of("/");
		if (found_sep == std::string::npos) {
			if (filename_out != nullptr) {
				*filename_out = path;
			}
			return path;
		}
		if (filename_out != nullptr) {
			*filename_out = path.substr(found_sep + 1);
		}
		return path.substr(0, found_sep - 0);
	}
	PROJECTXSHARED_API inline ProgError FileDirExists(std::string directory, bool *exists_out) {
		if (exists_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		directory = FileFormatPath(directory, true);
		DWORD file_attrs = GetFileAttributes(directory.c_str());
		if (file_attrs == INVALID_FILE_ATTRIBUTES) {
			return GetLastError();
		}
		*exists_out = (file_attrs & FILE_ATTRIBUTE_DIRECTORY) != 0;
		return ErrorCodes::kSuccess;
	}
}  // namespace projx
