#pragma once

#include <curl.h>

#include <string>

#include "px_file.h"
#include "projectx-shared.h"

namespace projx {

	class PROJECTXSHARED_API UrlDataBuf {
		Byte *data;
		size_t data_sz;
	public:
		UrlDataBuf();
		UrlDataBuf(Byte *, size_t);
		UrlDataBuf(const UrlDataBuf &);
		~UrlDataBuf();
		Byte *get_data() const;
		size_t get_data_sz() const;
		UrlDataBuf &operator=(const UrlDataBuf &);
	};
	class PROJECTXSHARED_API UrlHelper {
		CURL *curl;
	public:
		UrlHelper();
		~UrlHelper();
		CURL *get_curl() const;
		ProgError DownloadUrl(const std::string &/*url*/, std::string /*file_path*/);
		inline ProgError DownloadUrl(const std::string &url, const File &file_path) {
			return DownloadUrl(url, file_path.get_path());
		}
		ProgError DownloadUrl(const std::string &/*url*/, UrlDataBuf *);
	private:
		ProgError SetupApiForDl(const std::string &/*url*/);
	};
	typedef size_t (*UrlOutputFn)(void * /*data_buf*/, size_t /*elem_size*/, size_t /*data_buf_sz*/, void * /*out_stream*/);

	ProgError InitUrlApi();
	ProgError UninitUrlApi();

}  // namespace projx
