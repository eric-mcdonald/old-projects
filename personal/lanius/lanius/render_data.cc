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

#include "render_data.h"  // NOLINT

namespace lanius {

	RenderData::RenderData(HWND overlay_handle, unsigned int multi_sample_quality) : multi_sample_quality_(0), d3d_obj_(NULL), d3d_device_(NULL), overlay_handle_(overlay_handle), d3d_line_(NULL) {
			set_multi_sample_quality(multi_sample_quality);
			D3DXCreateLine(d3d_device_, &d3d_line_);
	}
	RenderData::RenderData() : multi_sample_quality_(0), d3d_obj_(NULL), d3d_device_(NULL), d3d_line_(NULL) {}
	IDirect3D9 *RenderData::d3d_obj() const {
		return d3d_obj_;
	}
	IDirect3DDevice9 *RenderData::d3d_device() const {
		return d3d_device_;
	}
	ID3DXLine *RenderData::d3d_line() const {
		return d3d_line_;
	}
	ErrorCode RenderData::set_multi_sample_quality(unsigned int multi_sample_quality) {
		d3d_obj_ = Direct3DCreate9(D3D_SDK_VERSION);
		if (!d3d_obj_) {
			return ErrorCodes::kD3dFailure;
		}
		D3DPRESENT_PARAMETERS d3d_params = { 0 };
		GetQualityLevels(d3d_obj_, d3d_params);
		unsigned int max_quality = d3d_params.MultiSampleQuality;
		if (multi_sample_quality >= max_quality) {
			multi_sample_quality = max_quality - 1;
		}
		RECT window_rect = { 0 };
		if (!GetWindowRect(overlay_handle_, &window_rect)) {
			return GetLastError();
		}
		d3d_params.BackBufferWidth = window_rect.right - window_rect.left;
		d3d_params.BackBufferHeight = window_rect.bottom - window_rect.top;
		d3d_params.BackBufferFormat = D3DFMT_A8R8G8B8;
		d3d_params.BackBufferCount = 1;
		d3d_params.MultiSampleQuality = multi_sample_quality;
		d3d_params.SwapEffect = D3DSWAPEFFECT_DISCARD;
		d3d_params.hDeviceWindow = overlay_handle_;
		d3d_params.Windowed = TRUE;
		d3d_params.EnableAutoDepthStencil = TRUE;
		d3d_params.AutoDepthStencilFormat = D3DFMT_D16;
		d3d_params.Flags = D3DPRESENTFLAG_DISCARD_DEPTHSTENCIL;
		d3d_params.FullScreen_RefreshRateInHz = 0;
		d3d_params.PresentationInterval = D3DPRESENT_INTERVAL_DEFAULT;
		ErrorCode error = d3d_obj_->CreateDevice(D3DADAPTER_DEFAULT, D3DDEVTYPE_HAL, overlay_handle_, D3DCREATE_SOFTWARE_VERTEXPROCESSING, &d3d_params, &d3d_device_);
		if (error == D3D_OK) {
			multi_sample_quality_ = multi_sample_quality;
			return ErrorCodes::kSuccess;
		}
		return error;
	}
	unsigned int RenderData::multi_sample_quality() const {
		return multi_sample_quality_;
	}
	const VertexBufMap &RenderData::vertex_bufs() const {
		return vertex_bufs_;
	}
	ErrorCode RenderData::AddVertexBuf(const std::string &name, unsigned int length, unsigned int fvf) {
		if (vertex_bufs_.find(name) != vertex_bufs_.end()) {
			return ErrorCodes::kIllegalParam;
		}
		IDirect3DVertexBuffer9 *vertex_buf = NULL;
		ErrorCode error = d3d_device_->CreateVertexBuffer(length, 0, fvf, D3DPOOL_DEFAULT, &vertex_buf, NULL);
		if (error == D3D_OK) {
			vertex_bufs_[name] = vertex_buf;
			return ErrorCodes::kSuccess;
		}
		return error;
	}
	const TextureMap &RenderData::textures() const {
		return textures_;
	}
	ErrorCode RenderData::AddTexture(const std::string &img_file_path) {
		if (textures_.find(img_file_path) != textures_.end()) {
			return ErrorCodes::kIllegalParam;
		}
		USES_CONVERSION;
		HRESULT d3d_error = D3DXCreateTextureFromFile(d3d_device_, A2CW(img_file_path.c_str()), &textures_[img_file_path]);
		return d3d_error == D3D_OK ? ErrorCodes::kSuccess : d3d_error;
	}
	void GetQualityLevels(IDirect3D9 *d3d_obj, D3DPRESENT_PARAMETERS &d3d_params) {
		d3d_params.MultiSampleType = D3DMULTISAMPLE_NONE;
		d3d_params.MultiSampleQuality = 0;
		DWORD quality_levels = 0;
		for (int multi_sample_type = D3DMULTISAMPLE_16_SAMPLES; multi_sample_type >= D3DMULTISAMPLE_NONE; --multi_sample_type) {
			if (d3d_obj->CheckDeviceMultiSampleType(D3DADAPTER_DEFAULT, D3DDEVTYPE_HAL, D3DFMT_A8R8G8B8, TRUE, static_cast<D3DMULTISAMPLE_TYPE>(multi_sample_type), &quality_levels) == D3D_OK) {
				d3d_params.MultiSampleType = static_cast<D3DMULTISAMPLE_TYPE>(multi_sample_type);
				d3d_params.MultiSampleQuality = quality_levels;
			}
		}
	}

}  // namespace lanius
