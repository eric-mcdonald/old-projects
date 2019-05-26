#include "stdafx.h"

#include "signatures.h"
#include "source_engine.h"
#include "se_offset_cache.h"
#include "se_utils.h"

#include "se_player_wrapper.h"

namespace source_engine {

	std::map<void*, size_t> PlayerWrapper::list_idx_cache;
	std::map<void*, ClassIds> PlayerWrapper::class_id_cache;

	PlayerWrapper::PlayerWrapper(void *entity_ptr_) : EntityWrapper(entity_ptr_) {}
	cheat::ErrorCode PlayerWrapper::GetListIndex(size_t *index_out) const {
		if (index_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		if (get_entity_ptr() == nullptr) {
			return cheat::CheatErrorCodes::kInvalidAddr;
		}
		auto index_it = list_idx_cache.find(get_entity_ptr());
		if (index_it != list_idx_cache.end()) {
			*index_out = index_it->second;
		}
		cheat::ErrorCode error = GetEntityIndex(get_entity_ptr(), index_out);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		list_idx_cache[get_entity_ptr()] = *index_out;
		return cheat::CheatErrorCodes::kSuccess;
	}
	bool PlayerWrapper::IsDormant() const {
		if (get_entity_ptr() == nullptr) {
			return false;
		}
		return *reinterpret_cast<bool*>(reinterpret_cast<DWORD_PTR>(get_entity_ptr()) + OffsetCache::GetInstance().entity_dormant_offs);
	}
	int PlayerWrapper::GetHealth() const {
		if (!IsValid()) {
			return 0;
		}
		return *reinterpret_cast<short*>(reinterpret_cast<DWORD_PTR>(get_entity_ptr()) + OffsetCache::GetInstance().health_offs);
	}
	int PlayerWrapper::GetTeam() const {
		if (!IsValid()) {
			return 0;
		}
		return *reinterpret_cast<short*>(reinterpret_cast<DWORD_PTR>(get_entity_ptr()) + OffsetCache::GetInstance().team_offs);
	}
	bool PlayerWrapper::IsAlive() const {
		if (!IsValid()) {
			return false;
		}
		bool alive;
		if (IsEntityAlive(get_entity_ptr(), &alive) != cheat::CheatErrorCodes::kSuccess) {
			return false;
		}
		return alive;
	}
	bool PlayerWrapper::IsValid() const {
		return get_entity_ptr() != nullptr && !IsDormant();
	}
	cheat::ErrorCode PlayerWrapper::GetClassId(ClassIds *class_id_out) const {
		if (class_id_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		if (get_entity_ptr() == nullptr) {
			return cheat::CheatErrorCodes::kInvalidAddr;
		}
		auto class_id_it = class_id_cache.find(get_entity_ptr());
		if (class_id_it != class_id_cache.end()) {
			*class_id_out = class_id_it->second;
		}
		size_t entity_idx;
		cheat::ErrorCode error = GetListIndex(&entity_idx);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		IClientNetworkable *networkable = g_entities->GetClientNetworkable(entity_idx);
		if (networkable == nullptr) {
			return cheat::CheatErrorCodes::kInvalidAddr;
		}
		ClientClass *client_cls = networkable->GetClientClass();
		if (client_cls == nullptr) {
			return cheat::CheatErrorCodes::kInvalidAddr;
		}
		*class_id_out = static_cast<ClassIds>(client_cls->m_ClassID);
		class_id_cache[get_entity_ptr()] = *class_id_out;
		return cheat::CheatErrorCodes::kSuccess;
	}

}  // namespace source_engine
