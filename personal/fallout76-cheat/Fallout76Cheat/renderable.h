#pragma once

#include "cheat_def.h"
#include "overlay.h"

namespace internals {

	class RenderableInterface {
	public:
		virtual cheat::ErrorCode Render() = 0;
		virtual cheat::Priorities GetPriority() const = 0;
		virtual Overlay &GetOverlay() = 0;
		virtual void SetOverlay(Overlay *) = 0;
		virtual bool ShouldRender() const = 0;
	};

}  // namespace internals
