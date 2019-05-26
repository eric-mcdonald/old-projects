#pragma once

namespace source_engine {

	class EntityWrapper {
		void *entity_ptr;
	public:
		EntityWrapper(void *);
		virtual bool IsValid() const;
		void *get_entity_ptr() const;
		virtual bool operator==(const EntityWrapper *) const;
		virtual bool operator!=(const EntityWrapper *) const;
	};

}  // namespace source_engine
