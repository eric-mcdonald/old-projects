#include "stdafx.h"

#include "lang_util.h"

namespace x12 {

	namespace util {

		namespace lang {

			template<class Superclass>
			bool IsInstance(const void *obj1) {
				return obj1 != nullptr ? dynamic_cast<Superclass*>(obj1) != nullptr : false;
			}

		}

	}

}