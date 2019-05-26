#pragma once

#include <vector>

#include "cheat_def.h"
#include "overlay.h"
#include "renderable.h"

namespace internals {

	class Renderer {
		std::vector<RenderableInterface*> renderables;
		bool modifying_container;
	public:
		Renderer();
		cheat::ErrorCode RenderAll(Overlay *);
		cheat::ErrorCode RegisterRenderable(const RenderableInterface *);
		cheat::ErrorCode UnregisterRenderable(const RenderableInterface *);
	};

	cheat::ErrorCode InstallRenderHooks();
	cheat::ErrorCode UninstallRenderHooks();

}  // namespace internals
