/*
Eric McDonald: Retrieving network variables and signatures seems to take too long for some functions (i.e. ApplyEntityGlowEffects).
So, this module contains a constant for each frequently-used offset to avoid this issue.
*/

#pragma once

#include "net_var_registry.h"
#include "signatures.h"

namespace source_engine {

	class OffsetCache {
	public:
		int health_offs;
		int life_state_offs;
		int team_offs;
		int entity_dormant_offs;
		IVEngineClient013 **engine_offs;
		int local_player_idx_offs;
		void **entity_list_offs;
		GlowObjectDefinition_t **glow_obj_defs_offs;
	private:
		OffsetCache();
	public:
		static OffsetCache &GetInstance();
	};

}  // namespace source_engine
