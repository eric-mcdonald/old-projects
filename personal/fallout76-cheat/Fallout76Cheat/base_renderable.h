#pragma once

#include "renderable.h"

namespace internals {

	class BaseRenderable : public RenderableInterface {
		Overlay *overlay;
		cheat::Priorities priorities;
	public:
		BaseRenderable(Overlay *, cheat::Priorities = cheat::Priorities::kNormal);
		BaseRenderable(cheat::Priorities = cheat::Priorities::kNormal);
		Overlay &GetOverlay() override;
		void SetOverlay(Overlay *) override;
		cheat::Priorities GetPriority() const override;
	};

}  // namespace internals
