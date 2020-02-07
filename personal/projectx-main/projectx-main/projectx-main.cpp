// projectx-main.cpp : Defines the entry point for the console application.

#include "stdafx.h"

#include "web_browser_info.h"
#include "main_prog.h"
#include "app_volume_ctrl.h"

int main() {
	projx::HideConsole();
	HRESULT com_err = CoInitializeEx(NULL, COINIT_SPEED_OVER_MEMORY | COINIT_MULTITHREADED);
	if (FAILED(com_err)) {
		return com_err;
	}
	projx::ProgError error = projx::MainProg::GetInstance().Start();
	if (error != projx::ErrorCodes::kSuccess) {
		CoUninitialize();
		return error;
	}
	while (projx::MainProg::GetInstance().IsRunning()) {
		error = projx::MainProg::GetInstance().RunFrame();
		if (error != projx::ErrorCodes::kSuccess) {
			CoUninitialize();
			return error;
		}
		Sleep(1);
	}
	error = projx::MainProg::GetInstance().Stop();
	CoUninitialize();
	if (error != projx::ErrorCodes::kSuccess) {
		return error;
	}
    return 0;
}
