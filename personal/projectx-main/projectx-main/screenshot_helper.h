#pragma once

#include <Windows.h>
#include <px_def.h>

namespace projx {

	class ScreenshotHelper {
		HDC screen_dc_cpy;
		HBITMAP screenshot_bmp;
		HANDLE display_thread;
		bool display_running, modifying_screenshot;
	public:
		ScreenshotHelper();
		ScreenshotHelper(const ScreenshotHelper &) = delete;
		ScreenshotHelper(ScreenshotHelper &&);
		~ScreenshotHelper();
		ProgError CaptureImage();
		ProgError DisplayImage() const;
		ScreenshotHelper &operator=(const ScreenshotHelper &) = delete;
		ScreenshotHelper &operator=(ScreenshotHelper &&);
		bool IsValid() const;
		bool is_display_running() const;
		ProgError StartDisplayingImage(int /*thread_priority*/ = THREAD_PRIORITY_TIME_CRITICAL);
		ProgError StopDisplayingImage();
	private:
		void Cleanup();
	};

}  // namespace projx
