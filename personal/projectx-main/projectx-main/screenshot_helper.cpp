#include "stdafx.h"

#include "screenshot_helper.h"

namespace projx {

	void ScreenshotHelper_DisplayImageLoop(ScreenshotHelper *instance) {
		while (instance->is_display_running()) {
			instance->DisplayImage();
		}
	}

	ScreenshotHelper::ScreenshotHelper() : screen_dc_cpy(NULL), screenshot_bmp(NULL), modifying_screenshot(false), display_running(false), display_thread(NULL) {
		HDC screen_dc = GetDC(NULL);
		if (screen_dc != NULL) {
			screen_dc_cpy = CreateCompatibleDC(screen_dc);
			if (screen_dc != NULL) {
				screenshot_bmp = CreateCompatibleBitmap(screen_dc, GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN));
			}
			ReleaseDC(NULL, screen_dc);
		}
	}
	ScreenshotHelper::ScreenshotHelper(ScreenshotHelper &&src) {
		display_thread = src.display_thread;
		display_running = src.display_running;
		modifying_screenshot = src.modifying_screenshot;
		screen_dc_cpy = src.screen_dc_cpy;
		screenshot_bmp = src.screenshot_bmp;
		src.screen_dc_cpy = NULL;
		src.screenshot_bmp = NULL;
		src.display_thread = NULL;
		src.display_running = modifying_screenshot = false;
	}
	ScreenshotHelper::~ScreenshotHelper() {
		Cleanup();
	}
	ProgError ScreenshotHelper::CaptureImage() {
		if (!IsValid() || modifying_screenshot) {
			return ErrorCodes::kInvalidState;
		}
		HDC screen_dc = GetDC(NULL);
		if (screen_dc == NULL) {
			return ErrorCodes::kFailedApiCall;
		}
		modifying_screenshot = true;
		HGDIOBJ prev_obj = SelectObject(screen_dc_cpy, screenshot_bmp);
		if (prev_obj == NULL || prev_obj == HGDI_ERROR) {
			modifying_screenshot = false;
			ReleaseDC(NULL, screen_dc);
			return ErrorCodes::kFailedApiCall;
		}
		if (!BitBlt(screen_dc_cpy, 0, 0, GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN), screen_dc, 0, 0, SRCCOPY)) {
			SelectObject(screen_dc_cpy, prev_obj);
			modifying_screenshot = false;
			ReleaseDC(NULL, screen_dc);
			return GetLastError();
		}
		SelectObject(screen_dc_cpy, prev_obj);
		modifying_screenshot = false;
		ReleaseDC(NULL, screen_dc);
		return ErrorCodes::kSuccess;
	}
	ProgError ScreenshotHelper::DisplayImage() const {
		if (!IsValid() || modifying_screenshot) {
			return ErrorCodes::kInvalidState;
		}
		HDC screen_dc = GetDC(NULL);
		if (screen_dc == NULL) {
			return ErrorCodes::kFailedApiCall;
		}
		HGDIOBJ prev_obj = SelectObject(screen_dc_cpy, screenshot_bmp);
		if (prev_obj == NULL || prev_obj == HGDI_ERROR) {
			ReleaseDC(NULL, screen_dc);
			return ErrorCodes::kFailedApiCall;
		}
		if (!BitBlt(screen_dc, 0, 0, GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN), screen_dc_cpy, 0, 0, SRCCOPY)) {
			SelectObject(screen_dc_cpy, prev_obj);
			ReleaseDC(NULL, screen_dc);
			return GetLastError();
		}
		SelectObject(screen_dc_cpy, prev_obj);
		ReleaseDC(NULL, screen_dc);
		return ErrorCodes::kSuccess;
	}
	ScreenshotHelper &ScreenshotHelper::operator=(ScreenshotHelper &&src) {
		if (this != &src) {
			Cleanup();
			display_thread = src.display_thread;
			display_running = src.display_running;
			modifying_screenshot = src.modifying_screenshot;
			screen_dc_cpy = src.screen_dc_cpy;
			screenshot_bmp = src.screenshot_bmp;
			src.screen_dc_cpy = NULL;
			src.screenshot_bmp = NULL;
			src.display_thread = NULL;
			src.display_running = modifying_screenshot = false;
		}
		return *this;
	}
	bool ScreenshotHelper::IsValid() const {
		return screen_dc_cpy != NULL && screenshot_bmp != NULL;
	}
	void ScreenshotHelper::Cleanup() {
		StopDisplayingImage();
		if (screenshot_bmp != NULL) {
			DeleteObject(screenshot_bmp);
			screenshot_bmp = NULL;
		}
		if (screen_dc_cpy != NULL) {
			DeleteDC(screen_dc_cpy);
			screen_dc_cpy = NULL;
		}
	}
	bool ScreenshotHelper::is_display_running() const {
		return display_running;
	}
	ProgError ScreenshotHelper::StartDisplayingImage(int thread_priority) {
		if (display_running) {
			return ErrorCodes::kInvalidState;
		}
		display_running = true;
		display_thread = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)ScreenshotHelper_DisplayImageLoop, this, 0, NULL);
		if (display_thread == NULL) {
			display_running = false;
			return GetLastError();
		}
		return !SetThreadPriority(display_thread, thread_priority) ? GetLastError() : ErrorCodes::kSuccess;
	}
	ProgError ScreenshotHelper::StopDisplayingImage() {
		if (!display_running) {
			return ErrorCodes::kInvalidState;
		}
		display_running = false;
		if (display_thread != NULL) {
			if (WaitForSingleObject(display_thread, INFINITE) == WAIT_FAILED) {
				display_thread = NULL;
				return GetLastError();
			}
			display_thread = NULL;
		}
		return ErrorCodes::kSuccess;
	}

}  // namespace projx
