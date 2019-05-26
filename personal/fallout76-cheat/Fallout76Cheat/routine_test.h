#pragma once

#include "cheat_def.h"

#include "basic_routine.h"

namespace cheat {

	namespace test {

		class TestRoutine : public BasicRoutine {
		public:
			TestRoutine();
		protected:
			virtual void AddListeners(std::vector<RoutineListener> &) override;
		};

	}  // namespace test

}  // namespace cheat
