#pragma once

#include <string>
#include <thread>
#include <map>

#include <Windows.h>
#include <d3d9.h>

#include "configuration.h"
#include "event_manager.h"
#include "native_utils.h"

namespace internals {

	struct D3dNormVertex {
		FLOAT x, y, z, rhw;
		DWORD color;
		D3dNormVertex();
	};
	struct D3dTexVertex {
		FLOAT x, y, z, rhw;
		DWORD color;
		FLOAT u, v;
		D3dTexVertex();
	};
	class Overlay {
		HWND overlay_window, proc_window;
		IDirect3DDevice9 *d3d_device;
		std::string class_name;
		IDirect3DVertexBuffer9 *normal_buffer, *textured_buffer;
		std::map<std::string, IDirect3DTexture9*> textures;
		D3DMULTISAMPLE_TYPE multisample_type;
		DWORD multisample_quality;
		bool initialized;
	public:
		Overlay(const std::string &/*overlay_title*/, const std::string &/*target_title*/, D3DMULTISAMPLE_TYPE, DWORD /*multisample_quality_*/);
		Overlay(Overlay &&);
		Overlay(const Overlay &) = delete;
		~Overlay();
		HWND get_overlay_window() const;
		HWND get_proc_window() const;
		IDirect3DDevice9 *get_d3d_device();
		IDirect3DVertexBuffer9 *get_normal_buffer();
		IDirect3DVertexBuffer9 *get_textured_buffer();
		cheat::ErrorCode DrawLine(const cheat::Vector2d<float> &/*pos1*/, const cheat::Vector2d<float> &/*pos2*/, unsigned int /*color*/);
		cheat::ErrorCode DrawQuad(const cheat::Vector2d<float> &, float /*width*/, float /*height*/, unsigned int /*color*/);
		cheat::ErrorCode DrawTexturedQuad(const cheat::Vector2d<float> &, float /*width*/, float /*height*/, const std::string &/*texture_id*/);
		cheat::ErrorCode DrawTexturedQuad(const cheat::Vector2d<float> &, float /*width*/, float /*height*/, IDirect3DTexture9 *);
		cheat::ErrorCode RegisterTexture(const std::string &/*file_path*/);  // Eric McDonald: Currently only supports PNG files.
		cheat::ErrorCode GetTexture(const std::string &/*texture_id*/, IDirect3DTexture9 ** /*texture_out*/) const;
		cheat::ErrorCode UnregisterTexture(const std::string &);
		bool is_initialized() const;
		inline cheat::Vector2d<unsigned int> GetResolution() const {
			cheat::Vector2d<unsigned int> resolution;
			RECT overlay_sz = GetWinRectAdjusted(overlay_window);
			resolution.set_x(overlay_sz.right - overlay_sz.left);
			resolution.set_y(overlay_sz.bottom - overlay_sz.top);
			return resolution;
		}
		extern friend void CreateOverlay(cheat::Configuration &);
		Overlay &operator=(const Overlay &) = delete;
		Overlay &operator=(Overlay &&);
	private:
		template<class Vertex>
		cheat::ErrorCode DrawVertices(const Vertex * /*vertices*/, size_t, IDirect3DVertexBuffer9 *, DWORD /*fvf*/, D3DPRIMITIVETYPE, UINT /*primitive_count*/, IDirect3DBaseTexture9 *);
		cheat::ErrorCode EnsureInitialized();  // Eric McDonald: The window needs to be created in the same thread as the message handling to send WM_DESTROY.
		cheat::ErrorCode Update();
		friend cheat::ErrorCode OnUpdate(cheat::Event *);
	};

	extern IDirect3D9 *g_d3d;
	extern Overlay *g_overlay;

	cheat::ErrorCode UpdateTextureBuf(const std::vector<Byte> &/*texture_data*/, IDirect3DTexture9 *);
	cheat::ErrorCode CreateTextureBuf(const std::vector<Byte> &/*texture_data*/, unsigned int /*width*/, unsigned int /*height*/, IDirect3DDevice9 * /*d3d_device*/, IDirect3DTexture9 ** /*texture_out*/);
	

}  // namespace internals
