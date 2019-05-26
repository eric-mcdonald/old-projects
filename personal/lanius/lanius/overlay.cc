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

#include <map>

#include <dwmapi.h>

#include "game.h"  // NOLINT

namespace lanius {

	struct D3dColVertex {
		float x, y, z, rhw;
		unsigned int diffuse;
	};
	struct D3dTexVertex {
		float x, y, z, rhw;
		unsigned int diffuse;
		float u, v;
	};

	static constexpr unsigned int kD3dColFvf = D3DFVF_XYZRHW | D3DFVF_DIFFUSE, kD3dTexFvf = D3DFVF_XYZRHW | D3DFVF_DIFFUSE | D3DFVF_TEX1;

	void UpdateOverlay(Overlay *instance) {
		while (instance->IsRunning()) {
			RECT target_rect = { 0 };
			HWND target_handle = instance->target_id().FindHandle();
			if (target_handle && GetWindowRect(target_handle, &target_rect) && target_rect != instance->window_pos()) {
				instance->set_window_pos();
			}
			Sleep(1);
		}
	}
	LRESULT CALLBACK CallWindowOverlay(_In_ HWND hwnd, _In_ UINT uMsg, _In_ WPARAM wParam, _In_ LPARAM lParam) {
		RECT window_rect;
		if (!GetWindowRect(hwnd, &window_rect)) {
			return DefWindowProc(hwnd, uMsg, wParam, lParam);
		}
		MARGINS window_margins = { 0, 0, window_rect.right - window_rect.left, window_rect.bottom - window_rect.top };
		switch (uMsg) {
		case WM_CREATE:
			DwmExtendFrameIntoClientArea(hwnd, &window_margins);
			break;
		case WM_DESTROY:
			PostQuitMessage(0);
			break;
		default:
			return DefWindowProc(hwnd, uMsg, wParam, lParam);
		}
		return 0;
	}
	Overlay::Overlay(const std::string &overlay_title, const WindowId &target_id, unsigned int multi_sample_quality) : frame_started_(false), overlay_title_(overlay_title), target_id_(target_id), window_pos_(), render_data_(), window_handle_(NULL), stop_requested_(false), update_thread_(NULL) {
		set_window_pos();
		USES_CONVERSION;
		if (target_id_.FindHandle()) {
			WNDCLASSEX wnd_class = { 0 };
			wnd_class.cbSize = sizeof(wnd_class);
			wnd_class.style = CS_VREDRAW | CS_HREDRAW;
			wnd_class.lpfnWndProc = CallWindowOverlay;
			wnd_class.hInstance = GetModuleHandle(NULL);
			wnd_class.hIcon = NULL;
			wnd_class.hCursor = NULL;
			wnd_class.hbrBackground = CreateSolidBrush(RGB(0, 0, 0));
			const wchar_t *overlay_title_c = A2CW(overlay_title_.c_str());
			wnd_class.lpszMenuName = overlay_title_c;
			wnd_class.lpszClassName = overlay_title_c;
			wnd_class.hIconSm = NULL;
			if (RegisterClassEx(&wnd_class)) {
				window_handle_ = CreateWindowEx(WS_EX_TOPMOST | WS_EX_TRANSPARENT | WS_EX_LAYERED | WS_EX_TOOLWINDOW, wnd_class.lpszClassName, overlay_title_c, WS_POPUP, window_pos_.x(), window_pos_.y(), window_pos_.width(), window_pos_.height(), NULL, NULL, NULL, NULL);
				SetLayeredWindowAttributes(window_handle_, RGB(0, 0, 0), 255, LWA_ALPHA);
				ShowWindow(window_handle_, SW_SHOW);
			}
			render_data_ = RenderData(window_handle_, multi_sample_quality);
			update_thread_ = CreateThread(NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(UpdateOverlay), this, 0, NULL);
		}
	}
	Overlay::Overlay(const Overlay &source) : Overlay(source.overlay_title_, source.target_id_, source.render_data_.multi_sample_quality()) {}
	Overlay::Overlay() : frame_started_(false), overlay_title_(""), target_id_(), window_pos_(), render_data_(), window_handle_(NULL), stop_requested_(false), update_thread_(NULL) {}
	const std::string &Overlay::overlay_title() const {
		return overlay_title_;
	}
	Overlay::~Overlay() {
		if (frame_started_) {
			EndFrame();
		}
		stop_requested_ = true;
		if (update_thread_) {
			MsgWaitForMultipleObjects(1, &update_thread_, FALSE, INFINITE, QS_ALLINPUT);
			CloseNativeHandle(update_thread_);
		}
		DestroyWindow(window_handle_);
		USES_CONVERSION;
		const wchar_t *overlay_title_c = A2CW(overlay_title_.c_str());
		if (overlay_title_c) {
			UnregisterClass(overlay_title_c, GetModuleHandle(NULL));
		}
	}
	ErrorCode Overlay::StartFrame() {
		if (!frame_started_) {
			frame_started_ = true;
			HRESULT d3d_error = render_data_.d3d_device()->Clear(0, NULL, D3DCLEAR_TARGET, 0, 1.0, 0);
			if (d3d_error != D3D_OK) {
				return d3d_error;
			}
			d3d_error = render_data_.d3d_device()->BeginScene();
			if (d3d_error != D3D_OK) {
				return d3d_error;
			}
		}
		return ErrorCodes::kSuccess;
	}
	bool Overlay::frame_started() const {
		return frame_started_;
	}
	ErrorCode Overlay::EndFrame() {
		if (frame_started_) {
			frame_started_ = false;
			HRESULT d3d_error = render_data_.d3d_device()->EndScene();
			if (d3d_error != D3D_OK) {
				return d3d_error;
			}
			d3d_error = render_data_.d3d_device()->Present(NULL, NULL, NULL, NULL);
			if (d3d_error != D3D_OK) {
				return d3d_error;
			}
		}
		return ErrorCodes::kSuccess;
	}
	void Overlay::UpdateWindow() {
		MSG msg = { 0 };
		if (PeekMessage(&msg, window_handle_, 0, 0, PM_REMOVE)) {
			DispatchMessage(&msg);
			TranslateMessage(&msg);
		}
	}
	const WindowRect &Overlay::window_pos() const {
		return window_pos_;
	}
	void Overlay::set_window_pos() {
		HWND target_handle = target_id_.FindHandle();
		if (!target_handle) {
			return;
		}
		RECT native_rect = { 0 };
		GetWindowRect(target_handle, &native_rect);
		window_pos_ = WindowRect(native_rect);
	}
	bool Overlay::IsRunning() const {
		return !stop_requested_ && target_id_.FindHandle();
	}
	ErrorCode Overlay::DrawFilledRect(float x, float y, float width, float height, Color color) {
		D3dColVertex rect_data[6] = { 0 };
		ErrorCode error = ErrorCodes::kSuccess;
		if (render_data_.vertex_bufs().find("DrawFilledRect") == render_data_.vertex_bufs().end()) {
			error = render_data_.AddVertexBuf("DrawFilledRect", sizeof(rect_data), kD3dColFvf);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
		}
		IDirect3DVertexBuffer9 *vertex_buf = render_data_.vertex_bufs().find("DrawFilledRect")->second;
		rect_data[0].x = x;
		rect_data[0].y = y + height;
		rect_data[0].rhw = 1.0;
		rect_data[0].diffuse = color;

		rect_data[1].x = x;
		rect_data[1].y = y;
		rect_data[1].rhw = 1.0;
		rect_data[1].diffuse = color;

		rect_data[2].x = x + width;
		rect_data[2].y = y;
		rect_data[2].rhw = 1.0;
		rect_data[2].diffuse = color;

		rect_data[3].x = x + width;
		rect_data[3].y = y + height;
		rect_data[3].rhw = 1.0;
		rect_data[3].diffuse = color;

		rect_data[4].x = x;
		rect_data[4].y = y + height;
		rect_data[4].rhw = 1.0;
		rect_data[4].diffuse = color;

		rect_data[5].x = x + width;
		rect_data[5].y = y;
		rect_data[5].rhw = 1.0;
		rect_data[5].diffuse = color;
		void *vertex_data = NULL;
		HRESULT d3d_error = vertex_buf->Lock(0, sizeof(rect_data), &vertex_data, 0);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		memcpy(vertex_data, rect_data, sizeof(rect_data));
		d3d_error = vertex_buf->Unlock();
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetStreamSource(0, vertex_buf, 0, sizeof(*rect_data));
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetFVF(kD3dColFvf);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_LIGHTING, FALSE);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->DrawPrimitive(D3DPT_TRIANGLELIST, 0, 2);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		return error;
	}
	ErrorCode Overlay::DrawTexture(const std::string &texture, float x, float y, float width, float height) {
		if (render_data_.textures().find(texture) == render_data_.textures().end()) {
			ErrorCode error = render_data_.AddTexture(texture);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
		}
		D3dTexVertex rect_data[6] = { 0 };
		ErrorCode error = ErrorCodes::kSuccess;
		if (render_data_.vertex_bufs().find("DrawTexture") == render_data_.vertex_bufs().end()) {
			error = render_data_.AddVertexBuf("DrawTexture", sizeof(rect_data), kD3dTexFvf);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
		}
		IDirect3DVertexBuffer9 *vertex_buf = render_data_.vertex_bufs().find("DrawTexture")->second;
		rect_data[0].x = x;
		rect_data[0].y = y + height;
		rect_data[0].rhw = 1.0;
		rect_data[0].diffuse = 0xFFFFFFFF;
		rect_data[0].u = 0.0F;
		rect_data[0].v = 1.0F;

		rect_data[1].x = x;
		rect_data[1].y = y;
		rect_data[1].rhw = 1.0;
		rect_data[1].diffuse = 0xFFFFFFFF;
		rect_data[1].u = 0.0F;
		rect_data[1].v = 0.0F;

		rect_data[2].x = x + width;
		rect_data[2].y = y;
		rect_data[2].rhw = 1.0;
		rect_data[2].diffuse = 0xFFFFFFFF;
		rect_data[2].u = 1.0F;
		rect_data[2].v = 0.0F;

		rect_data[3].x = x + width;
		rect_data[3].y = y + height;
		rect_data[3].rhw = 1.0;
		rect_data[3].diffuse = 0xFFFFFFFF;
		rect_data[3].u = 1.0F;
		rect_data[3].v = 1.0F;

		rect_data[4].x = x;
		rect_data[4].y = y + height;
		rect_data[4].rhw = 1.0;
		rect_data[4].diffuse = 0xFFFFFFFF;
		rect_data[4].u = 0.0F;
		rect_data[4].v = 1.0F;

		rect_data[5].x = x + width;
		rect_data[5].y = y;
		rect_data[5].rhw = 1.0;
		rect_data[5].diffuse = 0xFFFFFFFF;
		rect_data[5].u = 1.0F;
		rect_data[5].v = 0.0F;
		void *vertex_data = NULL;
		HRESULT d3d_error = vertex_buf->Lock(0, sizeof(rect_data), &vertex_data, 0);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		memcpy(vertex_data, rect_data, sizeof(rect_data));
		d3d_error = vertex_buf->Unlock();
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetStreamSource(0, vertex_buf, 0, sizeof(*rect_data));
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetFVF(kD3dTexFvf);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_LIGHTING, FALSE);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetTexture(0, render_data_.textures().find(texture)->second);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetTextureStageState(0, D3DTSS_COLOROP, D3DTOP_SELECTARG1);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetTextureStageState(0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->DrawPrimitive(D3DPT_TRIANGLELIST, 0, 2);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetTexture(0, NULL);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		return error;
	}
	ErrorCode Overlay::GetTextSz(const Font &font, const std::string &text, int &width, int &height) const {
		USES_CONVERSION;
		const wchar_t *text_c = A2CW(text.c_str());
		if (!text_c) {
			return ErrorCodes::kAtlFailure;
		}
		RECT rect = { 0 };
		rect.right = rect.left + 1;
		rect.bottom = rect.top + 1;
		INT txt_height = font.d3d_font()->DrawText(NULL, text_c, -1, &rect, DT_CALCRECT, 0xFFFFFFFF);
		if (!txt_height) {
			return ErrorCodes::kD3dFailure;
		}
		width = rect.right - rect.left;
		height = txt_height;
		return ErrorCodes::kSuccess;
	}
	ErrorCode Overlay::DrawStr(const Font &font, const std::string &text, int x, int y, Color color) {
		USES_CONVERSION;
		const wchar_t *text_c = A2CW(text.c_str());
		if (!text_c) {
			return ErrorCodes::kAtlFailure;
		}
		RECT rect = { 0 };
		rect.left = x;
		rect.top = y;
		rect.right = rect.left + 1;
		rect.bottom = rect.top + 1;
		INT txt_height = font.d3d_font()->DrawText(/*render_data_.d3d_sprite()*/NULL, text_c, -1, &rect, DT_CALCRECT, color);
		if (!txt_height) {
			return ErrorCodes::kD3dFailure;
		}
		rect.bottom = rect.top + txt_height;
		if (!font.d3d_font()->DrawText(/*render_data_.d3d_sprite()*/NULL, text_c, -1, &rect, DT_NOCLIP, color)) {
			return ErrorCodes::kD3dFailure;
		}
		return ErrorCodes::kSuccess;
	}
	const WindowId &Overlay::target_id() const {
		return target_id_;
	}
	bool Overlay::IsActive() const {
		HWND target_handle = target_id_.FindHandle();
		return target_handle && GetForegroundWindow() == target_handle;
	}
	ErrorCode Overlay::DrawLine(float x1, float y1, float x2, float y2, float width, Color color, bool antialias) {
		HRESULT d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_LIGHTING, FALSE);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_device()->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		D3DXVECTOR2 line_vecs[2];
		line_vecs[0].x = x1;
		line_vecs[0].y = y1;

		line_vecs[1].x = x2;
		line_vecs[1].y = y2;
		d3d_error = render_data_.d3d_line()->SetWidth(width);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_line()->SetAntialias(antialias);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_line()->Begin();
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_line()->Draw(line_vecs, sizeof(line_vecs) / sizeof(*line_vecs), color);
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		d3d_error = render_data_.d3d_line()->End();
		if (d3d_error != D3D_OK) {
			return d3d_error;
		}
		return ErrorCodes::kSuccess;
	}
	ErrorCode Overlay::DrawRect(float x, float y, float width, float height, float line_width, Color color, bool antialias) {
		ErrorCode error = DrawLine(x, y, x + width, y, line_width, color, antialias);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		error = DrawLine(x, y, x, y + height, line_width, color, antialias);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		error = DrawLine(x + width, y + height, x + width, y, line_width, color, antialias);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		error = DrawLine(x + width, y + height, x, y + height, line_width, color, antialias);
		if (error != ErrorCodes::kSuccess) {
			return error;
		}
		return error;
	}
	bool Overlay::operator==(const Overlay &operand) const {
		return overlay_title_ == operand.overlay_title_ && target_id_ == operand.target_id_;
	}
	bool Overlay::operator!=(const Overlay &operand) const {
		return !(*this == operand);
	}
	HWND Overlay::window_handle() const {
		return window_handle_;
	}
	void Overlay::set_stop_requested() {
		stop_requested_ = true;
	}
	RenderData &Overlay::render_data() {
		return render_data_;
	}

}  // namespace lanius
