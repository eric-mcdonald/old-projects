#pragma once

#include "routine.h"
#include "base_renderable.h"

namespace cheat {

	namespace test {

		class RenderTestRoutine : public Routine, public internals::BaseRenderable {
			std::string test_texture_id;
			float x, y;
		public:
			RenderTestRoutine();
			ErrorCode Render() override;
			bool ShouldRender() const override;
		};

	}  // namespace test

}  // namespace cheat
