#pragma once

#include "basic_routine.h"

namespace cheat {

	class RcsRoutine : public BasicRoutine {
	public:
		RcsRoutine();
	protected:
		virtual void AddListeners(std::vector<RoutineListener> &) override;
	};

}  // namespace cheat
