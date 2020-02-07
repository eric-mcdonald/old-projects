#include "stdafx.h"

#include "px_file.h"

#include "native_util.h"

namespace projx {

	ProgError FileAllocModFile(std::string mod_path, File **file_out) {
		if (mod_path.empty() || file_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		mod_path = FileFormatPath(mod_path, true);
		HMODULE library = LoadLibraryEx(mod_path.c_str(), NULL, DONT_RESOLVE_DLL_REFERENCES);
		if (library == NULL) {
			return GetLastError();
		}
		*file_out = new File(library);
		if (!FreeLibrary(library)) {
			return GetLastError();
		}
		return ErrorCodes::kSuccess;
	}

	File::File(std::string path_) : path(FileFormatPath(path_, false)) {}
	File::File(HMODULE library) {
		if (GetModuleFilename(library, &path) == ErrorCodes::kSuccess) {
			path = FileFormatPath(path, false);
		}
	}
	ProgError File::Delete() const {
		bool dir_exists;
		ProgError error = FileDirExists(path, &dir_exists);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		if (dir_exists) {
			return ErrorCodes::kInvalidOp;  // TODO Deleting a directory is currently unsupported.
		}
		if (Exists()) {
			if (remove(path.c_str())) {
				return ErrorCodes::kFileInaccessible;
			}
			return ErrorCodes::kSuccess;
		}
		return ErrorCodes::kFileNotFound;
	}
	std::string File::get_path() const {
		return path;
	}
	bool File::Exists() const {
		std::ifstream f(FileFormatPath(path, false));
		bool exists = f.is_open();
		f.close();
		if (!exists) {
			FileDirExists(path, &exists);
		}
		return exists;
	}
	bool File::CanModify() const {
		std::ofstream f(FileFormatPath(path, false), std::ofstream::out | std::ofstream::binary);
		bool modifiable = f.is_open() && f.good();
		f.close();
		return modifiable;
	}
	ProgError File::GetRoot(std::string *root_path_out) const {
		if (root_path_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		int drive_num = PathGetDriveNumber(FileFormatPath(path, true).c_str());
		if (drive_num == -1) {
			return ErrorCodes::kFileNotFound;
		}
		TCHAR root_buf[4]{ '\0' };
		std::string root = PathBuildRoot(root_buf, drive_num);
		if (root.empty()) {
			return ErrorCodes::kInvalidApiCall;
		}
		*root_path_out = root;
		return ErrorCodes::kSuccess;
	}
	ProgError File::CopyTo(std::string dest, bool overwrite) const {
		if (dest.empty() || FileArePathsEqual(path, dest)) {
			return ErrorCodes::kInvalidParam;
		}
		if (!Exists()) {
			return ErrorCodes::kFileNotFound;
		}
		if (File(dest).Exists() && !overwrite) {
			return ErrorCodes::kInvalidOp;
		}
		std::ofstream dest_file(dest, std::ofstream::out | std::ofstream::trunc | std::ofstream::binary);
		std::ifstream src_file(path, std::ifstream::in | std::ifstream::binary);
		dest_file << src_file.rdbuf();
		src_file.close();
		dest_file.close();
		return ErrorCodes::kSuccess;
	}

}  // namespace projx
