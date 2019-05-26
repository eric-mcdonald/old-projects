#pragma once

#include <string>
#include <iostream>
#include <map>

#include "se_iclient_def.h"

#include "se_entity_wrapper.h"

namespace source_engine {

	class PlayerWrapper : public EntityWrapper {
		// Eric McDonald: Caches for values that take too long to retrieve:
		static std::map<void*, size_t> list_idx_cache;
		static std::map<void*, ClassIds> class_id_cache;
	public:
		PlayerWrapper(void *);
		cheat::ErrorCode GetListIndex(size_t *) const;
		bool IsDormant() const;
		int GetHealth() const;
		int GetTeam() const;
		bool IsAlive() const;
		virtual bool IsValid() const override;
		cheat::ErrorCode GetClassId(ClassIds *) const;
	};

}  // namespace source_engine
