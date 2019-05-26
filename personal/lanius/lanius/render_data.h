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

#include <vector>

#include <Windows.h>
#include <d3d9.h>

#include "lanius.h"  // NOLINT

namespace lanius {

	using TextureMap = std::map<const std::string, IDirect3DTexture9 *>;
	using VertexBufMap = std::map<const std::string, IDirect3DVertexBuffer9 *>;
	class RenderData {
		IDirect3D9 *d3d_obj_;
		IDirect3DDevice9 *d3d_device_;
		unsigned int multi_sample_quality_;
		VertexBufMap vertex_bufs_;
		TextureMap textures_;
		HWND overlay_handle_;
		ID3DXLine *d3d_line_;
	public:
		RenderData(HWND /*overlay_handle*/, unsigned int /*multi_sample_quality*/);
		RenderData();
		IDirect3D9 *d3d_obj() const;
		IDirect3DDevice9 *d3d_device() const;
		unsigned int multi_sample_quality() const;
		ErrorCode set_multi_sample_quality(unsigned int);
		const VertexBufMap &vertex_bufs() const;
		ErrorCode AddVertexBuf(const std::string &/*name*/, unsigned int /*length*/, unsigned int /*fvf*/);
		const TextureMap &textures() const;
		ErrorCode AddTexture(const std::string &/*img_file_path*/);
		ID3DXLine *d3d_line() const;
	};

	void GetQualityLevels(IDirect3D9 *, D3DPRESENT_PARAMETERS &);
	inline ErrorCode GetDisplaySettings(DEVMODE &dev_mode, const std::string &device_name = "") {
		dev_mode.dmSize = sizeof(dev_mode);
		USES_CONVERSION;
		return EnumDisplaySettings(device_name.empty() ? NULL : A2W(device_name.c_str()), ENUM_CURRENT_SETTINGS, &dev_mode) ? ErrorCodes::kSuccess : ErrorCodes::kGetDisplaySettingsFailed;
	}

}  // namespace lanius
