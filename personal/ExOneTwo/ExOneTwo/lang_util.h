/*
Author: Eric McDonald
Date created: 2017-09-11
Purpose: Provides miscellaneous utilities for the program.
*/

#pragma once

namespace x12 {

	namespace util {

		namespace lang {

			/*
			Author: Eric McDonald
			Date created: 2017-09-11
			Purpose: Returns a non-zero value if obj1 is an instance of Superclass.
			*/
			template<class Superclass>
			bool IsInstance(const void *obj1);

		}

	}

}