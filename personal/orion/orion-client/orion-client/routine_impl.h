#pragma once

#include "routine.h"

#include <vector>

#include <Windows.h>

namespace orion {

	class JumpScareRoutine : public Routine {
		std::vector<unsigned char> img_data;
		ULONGLONG last_time;
		unsigned int width, height;
		HBITMAP bitmap;
		bool black_screen;
	public:
		JumpScareRoutine(std::string, bool /*enabled_*/);
		~JumpScareRoutine();
		virtual ErrorCode Update() override;
		virtual void SetEnabled(bool) override;
	};
	class RandMouseMoveRoutine : public Routine {
		ULONGLONG last_time;
	public:
		RandMouseMoveRoutine(std::string, bool /*enabled_*/);
		virtual ErrorCode Update() override;
		virtual void SetEnabled(bool) override;
	};

}  // namespace orion
