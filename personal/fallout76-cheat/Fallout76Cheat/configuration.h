#pragma once

#include <string>
#include <map>

#include "cheat_def.h"
#include "math_helper.h"
#include "string_utils.h"
#include "vector3d.h"

namespace cheat {

	class Configuration {
		const unsigned int version;
		std::map<std::string, std::string> loaded_entries;
		static constexpr char *kEntryDelim = ":", *kCommentPrefix = "#";
		std::string file_path;
		static constexpr char *kVecValDelim = ",";
	public:
		Configuration(const std::string &);
		template<class ValType>
		void SetEntry(const std::string &key, ValType value) {
			std::ostringstream val_str;
			val_str << value;
			loaded_entries[key] = val_str.str();
		}
		template<>
		void SetEntry(const std::string &key, Vector3d<float> value) {
			std::ostringstream val_str;
			val_str << value.get_x() << kVecValDelim << value.get_y() << kVecValDelim << value.get_z();
			loaded_entries[key] = val_str.str();
		}
		template<class ValType>
		ErrorCode GetEntry(const std::string &key, ValType *value_out) const {
			if (value_out == nullptr) {
				return CheatErrorCodes::kInvalidParam;
			}
			auto value = loaded_entries.find(key);
			if (value == loaded_entries.end()) {
				return CheatErrorCodes::kElemNotFound;
			}
			std::istringstream val_stream(value->second);
			val_stream >> *value_out;
			return CheatErrorCodes::kSuccess;
		}
		template<>
		ErrorCode Configuration::GetEntry(const std::string &key, Vector3d<float> *value_out) const {
			if (value_out == nullptr) {
				return CheatErrorCodes::kInvalidParam;
			}
			auto value = loaded_entries.find(key);
			if (value == loaded_entries.end()) {
				return CheatErrorCodes::kElemNotFound;
			}
			std::vector<std::string> vec_vals;
			ErrorCode error = StrSplit(value->second, kVecValDelim, &vec_vals);
			if (error != CheatErrorCodes::kSuccess) {
				return error;
			}
			float vec_coords[3]{ 0.0F };
			if (vec_vals.size() < sizeof(vec_coords) / sizeof(float)) {
				return CheatErrorCodes::kNotEnoughElems;
			}
			for (size_t i = 0; i < sizeof(vec_coords) / sizeof(float); ++i) {
				std::istringstream val_stream(vec_vals[i]);
				val_stream >> vec_coords[i];
			}
			*value_out = Vector3d<float>(vec_coords[0], vec_coords[1], vec_coords[2]);
			return CheatErrorCodes::kSuccess;
		}
		template<class ValType>
		ValType GetEntry(const std::string &key) {
			ValType value;
			ErrorCode error = GetEntry(key, &value);
			if (error != CheatErrorCodes::kSuccess) {
				*g_err_log << g_err_log->GetPrefix() << "Failed to retrieve entry " << key << " from file " << file_path << " with error code " << error << "." << std::endl;
				return ValType();
			}
			return value;
		}
		const std::map<std::string, std::string> &get_loaded_entries() const;
		unsigned int get_version() const;
		ErrorCode Read();
		ErrorCode Write() const;
		std::string get_file_path() const;
	};

}  // namespace cheat
