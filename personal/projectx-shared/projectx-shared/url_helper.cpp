#include "stdafx.h"

#include "url_helper.h"

#include "px_file.h"

namespace projx {

	size_t WriteUrlToFile(void *data_buf, size_t elem_size, size_t data_buf_sz, void *out_stream) {
		return fwrite(data_buf, elem_size, data_buf_sz, (FILE*)out_stream);
	}
	size_t WriteToBuf(void *data_buf, size_t elem_sz, size_t data_buf_sz, void *out_stream) {
		UrlDataBuf *url_data = (UrlDataBuf*)out_stream;
		if (url_data->get_data()) {
			// Extends the existing buffer in case this function is called more than once in a row by cURL
			std::vector<Byte> new_data;
			for (size_t i = 0; i < url_data->get_data_sz(); ++i) {
				new_data.push_back(url_data->get_data()[i]);
			}
			for (size_t i = 0; i < data_buf_sz; ++i) {
				new_data.push_back(((Byte*)data_buf)[i]);
			}
			*url_data = UrlDataBuf(new_data.data(), new_data.size());
		}
		else {
			*url_data = UrlDataBuf((Byte*)data_buf, data_buf_sz);
		}
		return data_buf_sz;
	}

	UrlDataBuf::UrlDataBuf() : data(nullptr), data_sz(0) {}
	UrlDataBuf::UrlDataBuf(Byte *data_, size_t data_sz_) : UrlDataBuf() {
		if (data_ != nullptr && data_sz_ != 0) {
			data = new Byte[data_sz = data_sz_];
			memcpy(data, data_, data_sz);
		}
	}
	UrlDataBuf::UrlDataBuf(const UrlDataBuf &src) : UrlDataBuf() {
		data_sz = src.data_sz;
		if (src.data != nullptr) {
			data = new Byte[data_sz];
			memcpy(data, src.data, data_sz);
		}
	}
	UrlDataBuf::~UrlDataBuf() {
		if (data != nullptr) {
			delete[] data;
			data = nullptr;
		}
		data_sz = 0;
	}
	Byte *UrlDataBuf::get_data() const {
		return data;
	}
	size_t UrlDataBuf::get_data_sz() const {
		return data_sz;
	}
	UrlDataBuf &UrlDataBuf::operator=(const UrlDataBuf &src) {
		if (this != &src) {
			if (data != nullptr) {
				delete[] data;
			}
			data_sz = src.data_sz;
			if (src.data == nullptr) {
				data = nullptr;
			}
			else {
				data = new Byte[data_sz];
				memcpy(data, src.data, data_sz);
			}
		}
		return *this;
	}
	UrlHelper::UrlHelper() : curl(curl_easy_init()) {}
	UrlHelper::~UrlHelper() {
		if (curl != nullptr) {
			curl_easy_cleanup(curl);
			curl = nullptr;
		}
	}
	CURL *UrlHelper::get_curl() const {
		return curl;
	}
	ProgError UrlHelper::SetupApiForDl(const std::string &url) {
		ProgError error = curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
		if (error) {
			return error;
		}
		error = curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, TRUE);
		if (error) {
			return error;
		}
		return ErrorCodes::kSuccess;
	}
	ProgError UrlHelper::DownloadUrl(const std::string &url, std::string file_path) {
		ProgError error = SetupApiForDl(url);
		if (error) {
			return error;
		}
		error = curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteUrlToFile);
		if (error) {
			return error;
		}
		file_path = FileFormatPath(file_path, false);
		FILE *file = nullptr;
		errno_t std_err = fopen_s(&file, file_path.c_str(), "wb+");
		if (std_err || file == nullptr) {
			return std_err;
		}
		error = curl_easy_setopt(curl, CURLOPT_WRITEDATA, file);
		if (error) {
			return error;
		}
		error = curl_easy_perform(curl);
		if (error) {
			fclose(file);
			return error;
		}
		fclose(file);
		return ErrorCodes::kSuccess;
	}
	ProgError UrlHelper::DownloadUrl(const std::string &url, UrlDataBuf *data_buf) {
		if (data_buf == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		ProgError error = SetupApiForDl(url);
		if (error) {
			return error;
		}
		error = curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteToBuf);
		if (error) {
			return error;
		}
		*data_buf = UrlDataBuf();
		error = curl_easy_setopt(curl, CURLOPT_WRITEDATA, data_buf);
		if (error) {
			return error;
		}
		error = curl_easy_perform(curl);
		if (error) {
			return error;
		}
		return ErrorCodes::kSuccess;
	}
	ProgError InitUrlApi() {
		CURLcode error = curl_global_init(CURL_GLOBAL_ALL);
		if (error) {
			return error;
		}
		return ErrorCodes::kSuccess;
	}
	ProgError UninitUrlApi() {
		curl_global_cleanup();
		return ErrorCodes::kSuccess;
	}

}  // namespace projx
