#include "stdafx.h"

#include "rcs_routine.h"

namespace cheat {

	RcsRoutine::RcsRoutine() : BasicRoutine(false, "rcs_routine.cfg", "rcs", "Controls your weapon's recoil.") {

	}
	void RcsRoutine::AddListeners(std::vector<RoutineListener> &listeners) {

	}

}  // namespace cheat
