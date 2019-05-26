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

#include <Windows.h>

namespace lanius {

	class Font {
		ID3DXFont *d3d_font_;
		std::string face_;
		int height_;
		int weight_;
		bool italic_;
		unsigned int quality_;
	public:
		Font(const std::string &, int /*height*/, int /*weight*/, bool /*italic*/, unsigned int /*quality*/, IDirect3DDevice9 *);
		Font();
		ID3DXFont *d3d_font() const;
		const std::string &face() const;
		int height() const;
		int weight() const;
		bool italic() const;
		unsigned int quality() const;
	};

	ErrorCode FindFont(const std::string &);

}  // namespace lanius
