#include "stdafx.h"

#include "cheat.h"
#include "se_event_impl.h"
#include "native_utils.h"
#include "vector2d.h"
#include "font.h"

#include "render_routine_test.h"

namespace cheat {

	namespace test {

		RenderTestRoutine::RenderTestRoutine() : Routine(false, "render_test_routine.cfg", "render_test", "Test description."), internals::BaseRenderable(), x(0.0F), y(0.0F) {
			std::string path;
			cheat::ErrorCode error = internals::GetModuleDirectory(internals::g_this_dll, &path);
			if (error == cheat::CheatErrorCodes::kSuccess) {
				GetOverlay().RegisterTexture(test_texture_id = path + "\\" + kDataDir + "\\test\\res\\textures\\test0.png");
			}
		}
		ErrorCode RenderTestRoutine::Render() {
			ErrorCode error = GetOverlay().DrawTexturedQuad(Vector2d<float>(x++, y++), 100.0F, 100.0F, test_texture_id);
			if (x >= 1920.0F) {
				x = 0.0F;
			}
			if (y >= 1080.0F) {
				y = 0.0F;
			}
			std::string path;
			error = internals::GetModuleDirectory(internals::g_this_dll, &path);
			if (error == cheat::CheatErrorCodes::kSuccess) {
				static internals::Font test_font(path + "\\" + kDataDir + "\\res\\fonts\\open_sans\\OpenSans-Regular.ttf", 24, GetOverlay());
				error = test_font.RenderWithShadow("test text wooo weeee", Vector2d<float>(x + 300, y + 300), 0x00FFFFFF);
				if (error != cheat::CheatErrorCodes::kSuccess) {
					return error;
				}
			}
			return cheat::CheatErrorCodes::kSuccess;
		}
		bool RenderTestRoutine::ShouldRender() const {
			return is_enabled();
		}

	}  // namespace test

}  // namespace cheat
