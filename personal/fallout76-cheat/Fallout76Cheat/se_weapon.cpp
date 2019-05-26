#include "stdafx.h"

#include "native_utils.h"

#include "se_weapon.h"

namespace source_engine {

	typedef CCSWeaponInfo*(__fastcall *GetCSWeaponDataFn)(void * /*this_ptr*/);

	WeaponWrapper::WeaponWrapper(void *entity_ptr_) : EntityWrapper(entity_ptr_) {}
	CCSWeaponInfo *WeaponWrapper::GetInfo() const {
		if (!IsValid()) {
			return nullptr;
		}
		return internals::GetVirtualFunc<GetCSWeaponDataFn>(get_entity_ptr(), 454)(get_entity_ptr());
	}
	WeaponIds WeaponWrapper::GetId() const {
		if (!IsValid()) {
			return WeaponIds::WEAPON_NONE;
		}
		return WeaponIds::WEAPON_NONE;  // TODO(Eric McDonald): Finish implementing this function.
	}

}  // namespace source_engine
