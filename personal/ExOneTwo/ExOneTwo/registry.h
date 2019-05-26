#pragma once

#include <string>
#include <vector>

#include "main_def.h"

namespace x12 {

	namespace util {

		namespace registry {

			// TODO(Eric) Impl
			class Registry {
				typedef std::vector<X12Obj> ObjList;
				ObjList objects;
			public:
				void Register(size_t object_id, const X12Obj value);
				void Unregister(size_t object_id);
				bool Contains(const X12Obj value);
				bool Get(size_t object_id, X12Obj *object);
				ObjList::iterator ObjectsBegin();
				ObjList::iterator ObjectsEnd();
			};
			class NamedObjRegistry : Registry {
				static const size_t kNoObjId = -1;
			public:
				bool Get(std::string obj_name, size_t object_id = kNoObjId);
				void Register(const X12Obj value, size_t object_id = kNoObjId);
				void Unregister(std::string obj_name, size_t object_id = kNoObjId);
			};

		}

	}

}