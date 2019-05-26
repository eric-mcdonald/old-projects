#include "stdafx.h"

#include "source_engine.h"
#include "native_utils.h"
#include "cheat.h"

#include "renderer.h"

namespace internals {

	bool SortByPriority(RenderableInterface *renderable1, RenderableInterface *renderable2) {
		return renderable1->GetPriority() < renderable2->GetPriority();
	}
	Renderer::Renderer() : modifying_container(false) {}
	cheat::ErrorCode Renderer::RenderAll(Overlay *overlay) {
		if (overlay == nullptr || !overlay->is_initialized()) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		if (modifying_container) {
			return cheat::CheatErrorCodes::kInvalidOp;  // Eric McDonald: Cancels the render operation to prevent concurrent modification errors.
		}
		RECT target_pos = GetWinRectAdjusted(overlay->get_proc_window()), current_pos = { 0 };
		if (GetWindowRect(overlay->get_overlay_window(), &current_pos)) {
			if (current_pos.left != target_pos.left || current_pos.top != target_pos.top || current_pos.right != target_pos.right || current_pos.bottom != target_pos.bottom) {
				HRESULT d3d_err = overlay->get_d3d_device()->Present(NULL, NULL, NULL, NULL);
				if (d3d_err != D3D_OK) {
					return d3d_err;
				}
				d3d_err = overlay->get_d3d_device()->Clear(0, NULL, D3DCLEAR_TARGET, 0, 1.0F, 0);
				if (d3d_err != D3D_OK) {
					return d3d_err;
				}
				CreateOverlay(*cheat::Cheat::GetInstance().get_general_cfg());  // Eric McDonald: Re-creates the overlay to resize Direct 3D's back buffer.
				overlay = g_overlay;
			}
		}
		HRESULT d3d_err = overlay->get_d3d_device()->Clear(0, NULL, D3DCLEAR_TARGET, 0, 1.0F, 0);
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		d3d_err = overlay->get_d3d_device()->BeginScene();
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		for (auto &it : renderables) {
			it->SetOverlay(overlay);
			if (!it->ShouldRender()) {
				continue;
			}
			if (GetForegroundWindow() != overlay->get_proc_window()) {
				continue;  // Eric McDonald: This is inside the loop to clear the screen.
			}
			cheat::ErrorCode error = it->Render();
			if (error != cheat::CheatErrorCodes::kSuccess) {
				*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to render an object with error code " << error << "." << std::endl;
			}
		}
		d3d_err = overlay->get_d3d_device()->EndScene();
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		d3d_err = overlay->get_d3d_device()->Present(NULL, NULL, NULL, NULL);
		if (d3d_err != D3D_OK) {
			return d3d_err;
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode Renderer::RegisterRenderable(const RenderableInterface *renderable) {
		if (renderable == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		for (auto &it : renderables) {
			if (it == renderable) {
				return cheat::CheatErrorCodes::kElemAlreadyRegistered;
			}
		}
		modifying_container = true;
		renderables.push_back(const_cast<RenderableInterface*>(renderable));
		std::sort(renderables.begin(), renderables.end(), SortByPriority);
		modifying_container = false;
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode Renderer::UnregisterRenderable(const RenderableInterface *renderable) {
		if (renderable == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		for (size_t i = 0; i < renderables.size(); ++i) {
			if (renderables[i] == renderable) {
				modifying_container = true;
				renderables.erase(renderables.begin() + i);
				std::sort(renderables.begin(), renderables.end(), SortByPriority);
				modifying_container = false;
				return cheat::CheatErrorCodes::kSuccess;
			}
		}
		return cheat::CheatErrorCodes::kElemNotFound;
	}
	cheat::ErrorCode InstallRenderHooks() {
		return source_engine::InstallRenderHook();
	}
	cheat::ErrorCode UninstallRenderHooks() {
		return source_engine::UninstallRenderHook();
	}

}  // namespace internals
