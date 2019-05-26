#include "stdafx.h"

#include "timer.h"
#include "native_utils.h"
#include "cheat.h"

#include "overlay.h"

namespace internals {

	IDirect3D9 *g_d3d = nullptr;
	Overlay *g_overlay = nullptr;
	static std::vector<Overlay*> overlay_pool;

	void CreateOverlay(cheat::Configuration &config) {
		if (g_d3d != nullptr) {
			g_d3d->Release();
			g_d3d = nullptr;
		}
		g_d3d = Direct3DCreate9(D3D_SDK_VERSION);
		DWORD multisample_type;
		cheat::ErrorCode error = config.GetEntry("multisample_type", &multisample_type);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			multisample_type = D3DMULTISAMPLE_NONE;
		}
		DWORD multisample_quality;
		error = config.GetEntry("multisample_quality", &multisample_quality);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			multisample_quality = 0;
		}
		DWORD max_quality;
		if (g_d3d->CheckDeviceMultiSampleType(D3DADAPTER_DEFAULT, D3DDEVTYPE_HAL, D3DFMT_A8R8G8B8, TRUE, static_cast<D3DMULTISAMPLE_TYPE>(multisample_type), &max_quality) == D3D_OK) {
			--max_quality;
			if (multisample_quality > max_quality) {
				multisample_quality = max_quality;
			}
		}
		else {
			multisample_type = D3DMULTISAMPLE_NONE;
			multisample_quality = 0;
		}
		std::vector<std::string> texture_ids;
		if (g_overlay != nullptr) {
			for (const auto &it : g_overlay->textures) {
				texture_ids.push_back(it.first);
			}
			delete g_overlay;
			g_overlay = nullptr;
		}
		g_overlay = new Overlay(cheat::kCheatName, cheat::kProcName, static_cast<D3DMULTISAMPLE_TYPE>(multisample_type), multisample_quality);
		for (const auto &it : texture_ids) {
			cheat::ErrorCode tex_err = g_overlay->RegisterTexture(it);
			if (tex_err != cheat::CheatErrorCodes::kSuccess) {
				*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to re-register texture \"" << it << "\" with error code " << tex_err << "." << std::endl;
			}
		}
	}
	LRESULT CALLBACK OverlayProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
		RECT window_rect;
		GetWindowRect(hwnd, &window_rect);
		MARGINS overlay_margins = {0, 0, window_rect.right - window_rect.left, window_rect.bottom - window_rect.top};
		switch (uMsg) {
		case WM_CREATE:
			DwmExtendFrameIntoClientArea(hwnd, &overlay_margins);
			return 0;
		}
		return DefWindowProc(hwnd, uMsg, wParam, lParam);
	}
	D3dNormVertex::D3dNormVertex() : z(0.0F), rhw(1.0F) {}
	D3dTexVertex::D3dTexVertex() : z(0.0F), rhw(1.0F) {}
	cheat::ErrorCode OnUpdate(cheat::Event *event_obj) {
		for (auto &it : overlay_pool) {
			cheat::ErrorCode error = it->EnsureInitialized();
			if (error != cheat::CheatErrorCodes::kSuccess) {
				*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to ensure the initialization of overlay " << it << " with error code " << error << "." << std::endl;
			}
			error = it->Update();
			if (error != cheat::CheatErrorCodes::kSuccess) {
				*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to update overlay " << it << " with error code " << error << "." << std::endl;
			}
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	Overlay::Overlay(const std::string &overlay_title, const std::string &target_title, D3DMULTISAMPLE_TYPE multisample_type_, DWORD multisample_quality_) : initialized(false), multisample_type(multisample_type_), multisample_quality(multisample_quality_), d3d_device(NULL), overlay_window(NULL), proc_window(NULL), normal_buffer(NULL), textured_buffer(NULL) {
		WNDCLASSEX wnd_cls = { 0 };
		wnd_cls.cbSize = sizeof(WNDCLASSEX);
		wnd_cls.style = CS_HREDRAW | CS_NOCLOSE | CS_VREDRAW;
		wnd_cls.lpfnWndProc = OverlayProc;
		wnd_cls.hInstance = g_this_dll;
		wnd_cls.hbrBackground = CreateSolidBrush(RGB(0, 0, 0));
		USES_CONVERSION;
		wnd_cls.lpszClassName = wnd_cls.lpszMenuName = A2CW(overlay_title.c_str());
		if (RegisterClassEx(&wnd_cls) || GetLastError() == ERROR_CLASS_ALREADY_EXISTS) {
			class_name = overlay_title;
			proc_window = FindWindow(NULL, A2CW(target_title.c_str()));
			cheat::EventManager *manager;
			if (cheat::Cheat::GetInstance().GetEvtManager("Update", &manager) == cheat::CheatErrorCodes::kSuccess) {
				if (overlay_pool.empty()) {
					manager->RegisterListener(OnUpdate);
				}
				overlay_pool.push_back(this);
				static constexpr DWORD kWaitTime = 5000;
				cheat::Timer init_timer;
				init_timer.set_start_time();
				while (!initialized && !init_timer.HasDelayPassed(kWaitTime)) {
					FixedSleep(1);
				}
				if (!initialized) {
					*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Overlay " << this << " is taking longer than " << kWaitTime << " milliseconds to initialize. Continuing anyway..." << std::endl;
				}
			}
		}
	}
	Overlay::Overlay(Overlay &&src) {
		overlay_window = src.overlay_window;
		proc_window = src.proc_window;
		d3d_device = src.d3d_device;
		class_name = src.class_name;
		normal_buffer = src.normal_buffer;
		textured_buffer = src.textured_buffer;
		textures = src.textures;
		multisample_type = src.multisample_type;
		multisample_quality = src.multisample_quality;
		initialized = src.initialized;
		src.overlay_window = src.proc_window = NULL;
		src.d3d_device = NULL;
		src.class_name = "";
		src.normal_buffer = src.textured_buffer = NULL;
		src.textures.clear();
		src.multisample_type = D3DMULTISAMPLE_NONE;
		src.multisample_quality = 0;
		src.initialized = false;
	}
	Overlay::~Overlay() {
		for (auto &it : textures) {
			it.second->Release();
		}
		textures.clear();
		if (normal_buffer != NULL) {
			normal_buffer->Release();
			normal_buffer = NULL;
		}
		if (textured_buffer != NULL) {
			textured_buffer->Release();
			textured_buffer = NULL;
		}
		SendMessage(overlay_window, WM_DESTROY, 0, 0);
		cheat::EventManager *manager;
		if (cheat::Cheat::GetInstance().GetEvtManager("Update", &manager) == cheat::CheatErrorCodes::kSuccess) {
			for (size_t i = 0; i < overlay_pool.size(); ++i) {
				if (overlay_pool[i] == this) {
					overlay_pool.erase(overlay_pool.begin() + i);
					break;
				}
			}
			if (overlay_pool.empty()) {
				manager->UnregisterListener(OnUpdate);
			}
		}
		if (!class_name.empty() && overlay_pool.empty()) {
			USES_CONVERSION;
			UnregisterClass(A2CT(class_name.c_str()), g_this_dll);
		}
	}
	cheat::ErrorCode Overlay::Update() {
		if (!initialized) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}
		RECT target_pos = GetWinRectAdjusted(proc_window);
		SetWindowPos(overlay_window, HWND_TOPMOST, target_pos.left, target_pos.top, target_pos.right - target_pos.left, target_pos.bottom - target_pos.top, SWP_SHOWWINDOW | SWP_NOREDRAW);
		MSG message;
		if (PeekMessage(&message, reinterpret_cast<HWND>(overlay_window), 0, 0, PM_REMOVE)) {
			TranslateMessage(&message);
			DispatchMessage(&message);
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode Overlay::EnsureInitialized() {
		if (initialized) {
			return cheat::CheatErrorCodes::kSuccess;
		}
		RECT target_pos = GetWinRectAdjusted(proc_window);
		USES_CONVERSION;
		const TCHAR *class_name_c = A2CT(class_name.c_str());
		overlay_window = CreateWindowEx(WS_EX_TOOLWINDOW | WS_EX_TRANSPARENT | WS_EX_TOPMOST | WS_EX_LAYERED, class_name_c, class_name_c, WS_POPUP, target_pos.left, target_pos.top, target_pos.right - target_pos.left, target_pos.bottom - target_pos.top, NULL, NULL, g_this_dll, NULL);
		SetLayeredWindowAttributes(overlay_window, RGB(0, 0, 0), 255, LWA_ALPHA);
		ShowWindow(overlay_window, SW_SHOW);
		UpdateWindow(overlay_window);
		D3DPRESENT_PARAMETERS d3d_params = { 0 };
		d3d_params.BackBufferWidth = target_pos.right - target_pos.left;
		d3d_params.BackBufferHeight = target_pos.bottom - target_pos.top;
		d3d_params.BackBufferFormat = D3DFMT_A8R8G8B8;
		d3d_params.MultiSampleType = multisample_type;
		d3d_params.MultiSampleQuality = multisample_quality;
		d3d_params.SwapEffect = D3DSWAPEFFECT_DISCARD;
		d3d_params.hDeviceWindow = overlay_window;
		d3d_params.Windowed = TRUE;
		d3d_params.EnableAutoDepthStencil = TRUE;
		d3d_params.AutoDepthStencilFormat = D3DFMT_D16;
		d3d_params.Flags = D3DPRESENTFLAG_DISCARD_DEPTHSTENCIL;
		d3d_params.PresentationInterval = D3DPRESENT_INTERVAL_DEFAULT;
		HRESULT d3d_err = g_d3d->CreateDevice(D3DADAPTER_DEFAULT, D3DDEVTYPE_HAL, overlay_window, D3DCREATE_SOFTWARE_VERTEXPROCESSING | D3DCREATE_MULTITHREADED, &d3d_params, &d3d_device);
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		d3d_device->CreateVertexBuffer(6 * sizeof(D3dNormVertex), 0, D3DFVF_XYZRHW | D3DFVF_DIFFUSE, D3DPOOL_DEFAULT, &normal_buffer, NULL);
		d3d_device->CreateVertexBuffer(6 * sizeof(D3dTexVertex), 0, D3DFVF_XYZRHW | D3DFVF_DIFFUSE | D3DFVF_TEX1, D3DPOOL_DEFAULT, &textured_buffer, NULL);
		initialized = true;
		return cheat::CheatErrorCodes::kSuccess;
	}
	bool Overlay::is_initialized() const {
		return initialized;
	}
	HWND Overlay::get_overlay_window() const {
		return overlay_window;
	}
	HWND Overlay::get_proc_window() const {
		return proc_window;
	}
	IDirect3DDevice9 *Overlay::get_d3d_device() {
		return d3d_device;
	}
	IDirect3DVertexBuffer9 *Overlay::get_normal_buffer() {
		return normal_buffer;
	}
	IDirect3DVertexBuffer9 *Overlay::get_textured_buffer() {
		return textured_buffer;
	}
	template<class Vertex>
	cheat::ErrorCode Overlay::DrawVertices(const Vertex *vertices, size_t vertices_sz, IDirect3DVertexBuffer9 *vertex_buffer, DWORD fvf, D3DPRIMITIVETYPE primitive_type, UINT primitive_count, IDirect3DBaseTexture9 *texture) {
		if (!initialized) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}
		if (vertices == nullptr || vertices_sz == 0 || vertex_buffer == nullptr || primitive_count == 0) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		void *vertices_buf;
		HRESULT d3d_err = vertex_buffer->Lock(0, sizeof(*vertices) * vertices_sz, &vertices_buf, 0);
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		memcpy(vertices_buf, vertices, sizeof(*vertices) * vertices_sz);
		d3d_err = vertex_buffer->Unlock();
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		d3d_err = d3d_device->SetStreamSource(0, vertex_buffer, 0, sizeof(Vertex));
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		d3d_err = d3d_device->SetFVF(fvf);
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		d3d_device->SetRenderState(D3DRS_LIGHTING, FALSE);
		d3d_device->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
		d3d_device->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
		d3d_device->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);
		if (texture != nullptr) {
			d3d_device->SetTexture(0, texture);
			d3d_device->SetTextureStageState(0, D3DTSS_COLOROP, D3DTOP_SELECTARG1);
			d3d_device->SetTextureStageState(0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
		}
		d3d_err = d3d_device->DrawPrimitive(primitive_type, 0, primitive_count);
		if (texture != nullptr) {
			d3d_device->SetTexture(0, NULL);
		}
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode Overlay::DrawLine(const cheat::Vector2d<float> &pos1, const cheat::Vector2d<float> &pos2, unsigned int color) {
		if (!initialized) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}
		D3dNormVertex vertices[2];
		for (size_t i = 0; i < sizeof(vertices) / sizeof(D3dNormVertex); ++i) {
			vertices[i].color = color;
		}
		vertices[0].x = pos1.get_x();
		vertices[0].y = pos1.get_y();
		vertices[1].x = pos2.get_x();
		vertices[1].y = pos2.get_y();
		return DrawVertices(vertices, sizeof(vertices) / sizeof(*vertices), normal_buffer, D3DFVF_XYZRHW | D3DFVF_DIFFUSE, D3DPT_LINELIST, 1, nullptr);
	}
	cheat::ErrorCode Overlay::DrawQuad(const cheat::Vector2d<float> &position, float width, float height, unsigned int color) {
		if (!initialized) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}
		D3dNormVertex vertices[6];
		for (size_t i = 0; i < sizeof(vertices) / sizeof(D3dNormVertex); ++i) {
			vertices[i].color = color;
		}
		vertices[0].x = position.get_x();
		vertices[0].y = position.get_y() + height;

		vertices[1].x = position.get_x();
		vertices[1].y = position.get_y();

		vertices[2].x = position.get_x() + width;
		vertices[2].y = position.get_y();

		vertices[3].x = position.get_x() + width;
		vertices[3].y = position.get_y() + height;

		vertices[4].x = position.get_x();
		vertices[4].y = position.get_y() + height;

		vertices[5].x = position.get_x() + width;
		vertices[5].y = position.get_y();
		return DrawVertices(vertices, sizeof(vertices) / sizeof(*vertices), normal_buffer, D3DFVF_XYZRHW | D3DFVF_DIFFUSE, D3DPT_TRIANGLELIST, 2, nullptr);
	}
	cheat::ErrorCode Overlay::DrawTexturedQuad(const cheat::Vector2d<float> &position, float width, float height, const std::string &texture_id) {
		if (!initialized) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}
		IDirect3DTexture9 *texture;
		cheat::ErrorCode error = GetTexture(texture_id, &texture);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		return DrawTexturedQuad(position, width, height, texture);
	}
	cheat::ErrorCode Overlay::DrawTexturedQuad(const cheat::Vector2d<float> &position, float width, float height, IDirect3DTexture9 *texture) {
		if (!initialized) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}
		D3dTexVertex vertices[6];
		for (size_t i = 0; i < sizeof(vertices) / sizeof(D3dTexVertex); ++i) {
			vertices[i].color = 0xFFFFFFFF;
		}
		vertices[0].x = position.get_x();
		vertices[0].y = position.get_y() + height;
		vertices[0].u = 0.0F;
		vertices[0].v = 1.0F;

		vertices[1].x = position.get_x();
		vertices[1].y = position.get_y();
		vertices[1].u = 0.0F;
		vertices[1].v = 0.0F;

		vertices[2].x = position.get_x() + width;
		vertices[2].y = position.get_y();
		vertices[2].u = 1.0F;
		vertices[2].v = 0.0F;

		vertices[3].x = position.get_x() + width;
		vertices[3].y = position.get_y() + height;
		vertices[3].u = 1.0F;
		vertices[3].v = 1.0F;

		vertices[4].x = position.get_x();
		vertices[4].y = position.get_y() + height;
		vertices[4].u = 0.0F;
		vertices[4].v = 1.0F;

		vertices[5].x = position.get_x() + width;
		vertices[5].y = position.get_y();
		vertices[5].u = 1.0F;
		vertices[5].v = 0.0F;
		return DrawVertices(vertices, sizeof(vertices) / sizeof(*vertices), textured_buffer, D3DFVF_XYZRHW | D3DFVF_DIFFUSE | D3DFVF_TEX1, D3DPT_TRIANGLELIST, 2, texture);
	}
	inline cheat::ErrorCode DecodePng(const std::string &file_path, unsigned int *img_width_out, unsigned int *img_height_out, std::vector<Byte> *img_data_out) {
		if (file_path.empty() || img_width_out == nullptr || img_height_out == nullptr || img_data_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		unsigned int lodepng_err = lodepng::decode(*img_data_out, *img_width_out, *img_height_out, file_path);
		if (lodepng_err) {
			*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to decode file \"" << file_path << "\" into raw PNG data with error code " << lodepng_err << "." << std::endl;
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode Overlay::RegisterTexture(const std::string &file_path) {
		if (!initialized) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}
		if (textures.find(file_path) != textures.end()) {
			return cheat::CheatErrorCodes::kElemAlreadyRegistered;
		}
		std::vector<Byte> png_data;
		unsigned int png_width, png_height;
		cheat::ErrorCode error = DecodePng(file_path, &png_width, &png_height, &png_data);  // Eric McDonald: png_data is in BGRA format.
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		// Eric McDonald: Converts the format of the data from BGRA to RGBA for Direct 3D.
		for (size_t i = 0; i < png_data.size() - 4; i += 4) {
			Byte r, g, b, a;
			r = png_data[i + 2];
			g = png_data[i + 1];
			b = png_data[i];
			a = png_data[i + 3];
			png_data[i] = r;
			png_data[i + 1] = g;
			png_data[i + 2] = b;
			png_data[i + 3] = a;
		}
		IDirect3DTexture9 *d3d_texture;
		error = CreateTextureBuf(png_data, png_width, png_height, d3d_device, &d3d_texture);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		textures[file_path] = d3d_texture;
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode Overlay::GetTexture(const std::string &texture_id, IDirect3DTexture9 **texture_out) const {
		if (!initialized) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}
		if (texture_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		const auto &found_texture = textures.find(texture_id);
		if (found_texture == textures.end()) {
			return cheat::CheatErrorCodes::kElemNotFound;
		}
		*texture_out = found_texture->second;
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode Overlay::UnregisterTexture(const std::string &texture_id) {
		if (!initialized) {
			return cheat::CheatErrorCodes::kInvalidOp;
		}
		const auto &found_texture = textures.find(texture_id);
		if (found_texture == textures.end()) {
			return cheat::CheatErrorCodes::kElemNotFound;
		}
		found_texture->second->Release();
		textures.erase(found_texture);
		return cheat::CheatErrorCodes::kSuccess;
	}
	Overlay &Overlay::operator=(Overlay &&src) {
		if (this != &src) {
			Overlay(std::move(src));
		}
		return *this;
	}

	cheat::ErrorCode UpdateTextureBuf(const std::vector<Byte> &texture_data, IDirect3DTexture9 *texture) {
		if (texture_data.empty() || texture == NULL) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		D3DLOCKED_RECT locked_rect;
		HRESULT d3d_err = texture->LockRect(0, &locked_rect, NULL, 0);
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		memcpy(locked_rect.pBits, texture_data.data(), texture_data.size());
		d3d_err = texture->UnlockRect(0);
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode CreateTextureBuf(const std::vector<Byte> &texture_data, unsigned int width, unsigned int height, IDirect3DDevice9 *d3d_device, IDirect3DTexture9 **texture_out) {
		if (texture_data.empty() || width == 0 || height == 0 || d3d_device == NULL || texture_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		HRESULT d3d_err = d3d_device->CreateTexture(width, height, 0, D3DUSAGE_SOFTWAREPROCESSING, D3DFMT_A8R8G8B8, D3DPOOL_MANAGED, texture_out, NULL);
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		return UpdateTextureBuf(texture_data, *texture_out);
	}

}  // namespace internals
