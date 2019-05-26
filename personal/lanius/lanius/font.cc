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

#include "font.h"  // NOLINT

namespace lanius {

	int CALLBACK EnumFontFamEquals(const LOGFONT *lpelfe, const TEXTMETRIC *lpntme, DWORD FontType, LPARAM lParam) {
		return lParam && _tcscmp(lpelfe->lfFaceName, reinterpret_cast<TCHAR *>(lParam));
	}
	Font::Font(const std::string &face, int height, int weight, bool italic, unsigned int quality, IDirect3DDevice9 *d3d_device) : face_(face), height_(height), weight_(weight), italic_(italic), quality_(quality), d3d_font_(NULL) {
		USES_CONVERSION;
		D3DXCreateFont(d3d_device, height_, 0, weight_, 0, italic_, DEFAULT_CHARSET, OUT_DEFAULT_PRECIS, quality_, DEFAULT_PITCH | FF_DONTCARE, A2CW(face_.c_str()), &d3d_font_);
	}
	Font::Font() : face_(""), height_(0), weight_(0), italic_(false), quality_(0), d3d_font_(NULL) {}
	ID3DXFont *Font::d3d_font() const {
		return d3d_font_;
	}
	const std::string &Font::face() const {
		return face_;
	}
	int Font::height() const {
		return height_;
	}
	int Font::weight() const {
		return weight_;
	}
	bool Font::italic() const {
		return italic_;
	}
	unsigned int Font::quality() const {
		return quality_;
	}
	ErrorCode FindFont(const std::string &font_face) {
		HDC device_ctx = GetDC(NULL);
		if (!device_ctx) {
			return ErrorCodes::kInvalidDeviceCtx;
		}
		LOGFONT font_search_info = { 0 };
		font_search_info.lfCharSet = DEFAULT_CHARSET;
		font_search_info.lfFaceName[0] = '\0';
		font_search_info.lfPitchAndFamily = 0;
		USES_CONVERSION;
		bool exists = !EnumFontFamiliesEx(device_ctx, &font_search_info, EnumFontFamEquals, reinterpret_cast<LPARAM>(A2CT(font_face.c_str())), 0);
		ReleaseDC(NULL, device_ctx);
		return exists ? ErrorCodes::kSuccess : ErrorCodes::kFontNotFound;
	}

}  // namespace lanius
