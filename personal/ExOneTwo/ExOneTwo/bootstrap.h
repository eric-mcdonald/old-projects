/*
Author: Eric McDonald
Date created: 2017-09-11
Purpose: Provides startup and shutdown functionality.
*/

#pragma once

#include "stdafx.h"

namespace x12 {

	namespace main {

		// TODO(Eric McDonald) Implement the following functions. Use the INIT_X12_OBJ whilst registering an instance of X12Obj.

		ErrorCodes Create(Parameters *params, Instance *instance);
		ErrorCodes Start(const Instance *instance);
		void Destroy(Instance *instance);
		ErrorCodes Pause(const Instance *instance);
		ErrorCodes Resume(const Instance *instance);
		void Shutdown(const Instance *instance);

	}

}