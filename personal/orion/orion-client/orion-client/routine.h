#pragma once

#include <string>
#include <map>

#include "orion_def.h"

namespace orion {

	class Routine {
		bool enabled;
		std::string name;
	public:
		Routine(const std::string &, bool /*enabled_*/ = false);
		~Routine();
		virtual ErrorCode Update() = 0;
		std::string get_name() const;
		bool is_enabled() const;
		virtual void SetEnabled(bool);
	};

	extern std::map<std::string, Routine*> g_routines;

	ErrorCode InitRoutines();
	ErrorCode UninitRoutines();

}  // namespace orion
