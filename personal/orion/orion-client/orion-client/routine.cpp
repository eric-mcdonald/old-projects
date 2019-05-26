#include "stdafx.h"

#include "routine.h"
#include "routine_impl.h"

namespace orion {

	Routine::Routine(const std::string &name_, bool enabled_) : name(name_), enabled(enabled_) {}
	Routine::~Routine() {
		SetEnabled(false);
	}
	std::string Routine::get_name() const {
		return name;
	}
	bool Routine::is_enabled() const {
		return enabled;
	}
	void Routine::SetEnabled(bool enabled_) {
		enabled = enabled_;
	}

	std::map<std::string, Routine*> g_routines;

	ErrorCode InitRoutines() {
		JumpScareRoutine *jump_scare = new JumpScareRoutine(std::string("jump_scare"), false);
		g_routines["jump_scare"] = jump_scare;
		RandMouseMoveRoutine *rand_mouse = new RandMouseMoveRoutine(std::string("rand_mouse"), false);
		g_routines["rand_mouse"] = rand_mouse;
		return ErrorCodes::kSuccess;
	}
	ErrorCode UninitRoutines() {
		return ErrorCodes::kSuccess;
	}

}  // namespace orion
