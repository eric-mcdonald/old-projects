#include "stdafx.h"

#include "se_entity_wrapper.h"

namespace source_engine {

	EntityWrapper::EntityWrapper(void *entity_ptr_) : entity_ptr(entity_ptr_) {}
	bool EntityWrapper::IsValid() const {
		return entity_ptr != nullptr;
	}
	void *EntityWrapper::get_entity_ptr() const {
		return entity_ptr;
	}
	bool EntityWrapper::operator==(const EntityWrapper *rhs) const {
		return rhs != nullptr && entity_ptr == rhs->entity_ptr;
	}
	bool EntityWrapper::operator!=(const EntityWrapper *rhs) const {
		return !(*this == rhs);
	}

}  // namespace source_engine
