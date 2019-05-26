#pragma once

#include "cheat_def.h"

namespace source_engine {

	cheat::ErrorCode GetEntityIndex(const void *, size_t *);
	cheat::ErrorCode GetLocalPlayer(void **);
	cheat::ErrorCode IsEntityAlive(const void *, bool *);

}  // namespace source_engine
