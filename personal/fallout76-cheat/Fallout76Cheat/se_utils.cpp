#include "stdafx.h"

#include "se_utils.h"
#include "se_iclient_def.h"
#include "source_engine.h"
#include "se_offset_cache.h"

#include "se_utils.h"

namespace source_engine {

	cheat::ErrorCode GetEntityIndex(const void *entity, size_t *index_out) {
		if (entity == nullptr || index_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		if (g_entities->GetHighestEntityIndex() <= 0) {
			return cheat::CheatErrorCodes::kNotEnoughElems;
		}
		size_t highest_idx = static_cast<size_t>(g_entities->GetHighestEntityIndex());
		for (size_t i = 0; i <= highest_idx; ++i) {
			IClientNetworkable *client_entity = g_entities->GetClientNetworkable(i);
			if (client_entity == nullptr || client_entity->GetIClientUnknown() == nullptr) {
				continue;
			}
			if (client_entity->GetIClientUnknown()->GetBaseEntity() == entity) {
				*index_out = i;
				return cheat::CheatErrorCodes::kSuccess;
			}
		}
		return cheat::CheatErrorCodes::kElemNotFound;
	}
	cheat::ErrorCode GetLocalPlayer(void **local_player_out) {
		if (local_player_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		if (OffsetCache::GetInstance().engine_offs == nullptr || *OffsetCache::GetInstance().engine_offs == nullptr) {
			return cheat::CheatErrorCodes::kInvalidAddr;
		}
		int highest_idx = g_entities->GetHighestEntityIndex();
		if (highest_idx <= 0) {
			return cheat::CheatErrorCodes::kNotEnoughElems;
		}
		int local_player_idx = *reinterpret_cast<int*>(reinterpret_cast<DWORD_PTR>(*OffsetCache::GetInstance().engine_offs) + OffsetCache::GetInstance().local_player_idx_offs);
		if (local_player_idx < 0 || local_player_idx > highest_idx) {
			return cheat::CheatErrorCodes::kValOutOfRange;
		}
		*local_player_out = *(OffsetCache::GetInstance().entity_list_offs + local_player_idx * kEntListElemSz);
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode IsEntityAlive(const void *entity, bool *alive_out) {
		if (entity == nullptr || alive_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		if (*reinterpret_cast<bool*>(reinterpret_cast<DWORD_PTR>(entity) + OffsetCache::GetInstance().entity_dormant_offs)) {
			return cheat::CheatErrorCodes::kValInvalidState;
		}
		*alive_out = *reinterpret_cast<short*>(reinterpret_cast<DWORD_PTR>(entity) + OffsetCache::GetInstance().health_offs) > 0 && *reinterpret_cast<char*>(reinterpret_cast<DWORD_PTR>(entity) + OffsetCache::GetInstance().life_state_offs) == LifeStates::kAlive;
		return cheat::CheatErrorCodes::kSuccess;
	}

}  // namespace source_engine
