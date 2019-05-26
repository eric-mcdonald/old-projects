#include "stdafx.h"

#include "base_renderable.h"

namespace internals {

	BaseRenderable::BaseRenderable(Overlay *overlay_, cheat::Priorities priorities_) : overlay(overlay_), priorities(priorities_) {}
	BaseRenderable::BaseRenderable(cheat::Priorities priorities_) : BaseRenderable(g_overlay, priorities_) {}
	Overlay &BaseRenderable::GetOverlay() {
		return *overlay;
	}
	void BaseRenderable::SetOverlay(Overlay *overlay_) {
		overlay = overlay_;
	}
	cheat::Priorities BaseRenderable::GetPriority() const {
		return priorities;
	}

}  // namespace internals
