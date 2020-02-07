#pragma once

#include <Windows.h>
#include <px_def.h>

namespace projx {

	// TODO Move this to projectx-shared
	class AppVolumeControl {
		std::string process;
	public:
		AppVolumeControl(const std::string &);
		ProgError SetMuted(BOOL);
		ProgError IsMuted(BOOL *);
	};

}   // namespace projx
