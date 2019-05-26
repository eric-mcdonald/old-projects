#include "stdafx.h"

#include "se_offset_cache.h"

namespace source_engine {

	OffsetCache::OffsetCache() {
		health_offs = g_net_vars->GetOffset("DT_BasePlayer", "m_iHealth");
		life_state_offs = g_net_vars->GetOffset("DT_BasePlayer", "m_lifeState");
		team_offs = g_net_vars->GetOffset("DT_BaseEntity", "m_iTeamNum");
		entity_dormant_offs = 0;
		internals::g_signatures->GetSignatureVal("m_bDormant", &entity_dormant_offs);
		engine_offs = nullptr;
		internals::g_signatures->GetSignatureVal("dwClientState", &engine_offs);
		local_player_idx_offs = 0;
		internals::g_signatures->GetSignatureVal("dwClientState_GetLocalPlayer", &local_player_idx_offs);
		entity_list_offs = nullptr;
		internals::g_signatures->GetSignatureVal("dwEntityList", &entity_list_offs);
		glow_obj_defs_offs = nullptr;
		internals::g_signatures->GetSignatureVal("g_GlowObjectManager", &glow_obj_defs_offs);
	}
	OffsetCache &OffsetCache::GetInstance() {
		static OffsetCache instance;
		return instance;
	}

}  // namespace source_engine
