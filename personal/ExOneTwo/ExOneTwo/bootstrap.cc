#include "stdafx.h"

#include "bootstrap.h"

namespace x12 {

	namespace main {

		ErrorCodes Create(Parameters *params, Instance *instance);
		ErrorCodes Start(const Instance *instance);
		void Destroy(Instance *instance);
		ErrorCodes Pause(const Instance *instance);
		ErrorCodes Resume(const Instance *instance);
		void Shutdown(const Instance *instance);

	}

}