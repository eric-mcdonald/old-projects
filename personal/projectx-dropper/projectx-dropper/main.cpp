#include "stdafx.h"

#include <native_util.h>

#include "dropper.h"

int main() {
	projx::HideConsole();
	projx::ProgError error = projx::Dropper::GetInstance().Start();
	if (error != projx::ErrorCodes::kSuccess) {
		return error;
	}
	while (projx::Dropper::GetInstance().IsRunning()) {
		error = projx::Dropper::GetInstance().RunFrame();
		if (error != projx::ErrorCodes::kSuccess) {
			return error;
		}
		Sleep(1);
	}
	error = projx::Dropper::GetInstance().Stop();
	if (error != projx::ErrorCodes::kSuccess) {
		return error;
	}
	return 0;
}
