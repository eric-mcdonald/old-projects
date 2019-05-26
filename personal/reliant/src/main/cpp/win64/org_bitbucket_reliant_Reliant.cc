/*
 * org_bitbucket_reliant_Reliant.cc
 *
 *  Created on: Dec 29, 2016
 *      Author: Eric McDonald
 */




#include "org_bitbucket_reliant_Reliant.h"

#include <windows.h>
#include <windowsx.h>
#include <d3d9.h>
#include <dwmapi.h>
#include <vector>

#include "org_bitbucket_reliant_memory_MemoryStream.h"

JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_Reliant_runAdmin
(JNIEnv *env, jobject this_obj, jstring file, jstring directory, jboolean hide, jboolean wait) {
	SHELLEXECUTEINFO shell_exec_info;
	shell_exec_info.cbSize = sizeof(SHELLEXECUTEINFO);
	shell_exec_info.fMask = SEE_MASK_NOASYNC | SEE_MASK_NOCLOSEPROCESS;
	shell_exec_info.hwnd = NULL;
	shell_exec_info.lpVerb = TEXT("runas");
	const char *file_chars = env->GetStringUTFChars(file, JNI_FALSE);
	shell_exec_info.lpFile = file_chars;
	shell_exec_info.lpParameters = NULL;
	const char *directory_chars = env->GetStringUTFChars(directory, JNI_FALSE);
	shell_exec_info.lpDirectory = directory_chars;
	shell_exec_info.nShow = hide ? SW_HIDE : SW_SHOWDEFAULT;
	CONST BOOL success = ShellExecuteEx(&shell_exec_info);
	env->ReleaseStringUTFChars(file, file_chars);
	env->ReleaseStringUTFChars(directory, directory_chars);
	if (wait) {
		WaitForSingleObject(shell_exec_info.hProcess, INFINITE);
	}
	return CloseHandle(shell_exec_info.hProcess) && success;
}

JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_Reliant_setPriorityClass
(JNIEnv *env, jobject this_obj, jint priority_class) {
	return SetPriorityClass(GetCurrentProcess(), priority_class);
}

struct OverlayData {
	HWND overlayed_window, target_window;
};

void OverlayWindow(OverlayData *data) {
	while (true) {
		RECT overlayed_rect;
		GetWindowRect(data->overlayed_window, &overlayed_rect);
		RECT target_rect;
		GetWindowRect(data->target_window, &target_rect);
		if (overlayed_rect.left != target_rect.left || overlayed_rect.top != target_rect.top || overlayed_rect.right != target_rect.right || overlayed_rect.bottom != target_rect.bottom) {
			SetWindowPos(data->overlayed_window, HWND_TOPMOST, target_rect.left, target_rect.top, target_rect.right - target_rect.left, target_rect.bottom - target_rect.top, SWP_NOSIZE | SWP_NOZORDER | SWP_NOREDRAW);
		}
	}
}

LRESULT CALLBACK WindowProc(_In_ HWND hwnd, _In_ UINT msg, _In_ WPARAM w_param, _In_ LPARAM l_param) {
	RECT window_rect;
	GetWindowRect(hwnd, &window_rect);
	MARGINS overlayed_margins = {0, 0, window_rect.right - window_rect.left, window_rect.bottom - window_rect.top};
	switch (msg) {
	case WM_CREATE:
		DwmExtendFrameIntoClientArea(hwnd, &overlayed_margins);
		break;
	case WM_DESTROY:
		PostQuitMessage(0);
		break;
	default:
		return DefWindowProc(hwnd, msg, w_param, l_param);
	}
	return 0;
}

JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_Reliant_createOverlayedWindow
(JNIEnv *env, jobject this_obj, jstring target_window, jstring window_title) {
	WNDCLASSEX window_class;
	window_class.cbClsExtra = NULL;
	window_class.cbSize = sizeof(WNDCLASSEX);
	window_class.cbWndExtra = NULL;
	window_class.hbrBackground = reinterpret_cast<HBRUSH>(CreateSolidBrush(RGB(0, 0, 0)));
	window_class.hCursor = LoadCursor(NULL, IDC_ARROW);
	window_class.hIcon = LoadIcon(NULL, IDI_APPLICATION);
	window_class.hIconSm = LoadIcon(NULL, IDI_APPLICATION);
	window_class.hInstance = GetModuleHandle(NULL);
	window_class.lpfnWndProc = WindowProc;
	const char *target_window_title = env->GetStringUTFChars(target_window, JNI_FALSE), *new_window_title = env->GetStringUTFChars(window_title, JNI_FALSE);
	window_class.lpszClassName = new_window_title;
	window_class.lpszMenuName = new_window_title;
	window_class.style = CS_VREDRAW | CS_HREDRAW;
	if(!RegisterClassEx(&window_class)) {
		env->ReleaseStringUTFChars(target_window, target_window_title);
		env->ReleaseStringUTFChars(window_title, new_window_title);
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	CONST HWND target_handle = FindWindow(NULL, target_window_title);
	HWND overlayed_window = org_bitbucket_reliant_memory_MemoryStream_NULL;
	if (target_handle != NULL) {
		RECT target_rect;
		GetWindowRect(target_handle, &target_rect);
		overlayed_window = CreateWindowEx(WS_EX_TOPMOST | WS_EX_TRANSPARENT | WS_EX_LAYERED | WS_EX_TOOLWINDOW, new_window_title, new_window_title, WS_POPUP, target_rect.left, target_rect.top, target_rect.right - target_rect.left, target_rect.bottom - target_rect.top, NULL, NULL, NULL, NULL);
		SetLayeredWindowAttributes(overlayed_window, RGB(0, 0, 0), 255, LWA_ALPHA);
		ShowWindow(overlayed_window, SW_SHOW);
		OverlayData *data = new OverlayData();
		data->overlayed_window = overlayed_window;
		data->target_window = target_handle;
		CreateThread(NULL, NULL, reinterpret_cast<LPTHREAD_START_ROUTINE>(OverlayWindow), data, 0, NULL);
	}
	env->ReleaseStringUTFChars(target_window, target_window_title);
	env->ReleaseStringUTFChars(window_title, new_window_title);
	return reinterpret_cast<jlong>(overlayed_window);
}

JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_Reliant_createD3dDevice
(JNIEnv *env, jobject this_obj, jlong window, jboolean anti_aliasing) {
	IDirect3D9 *d3d9 = Direct3DCreate9(D3D_SDK_VERSION);
	if (d3d9 == NULL) {
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	D3DPRESENT_PARAMETERS present_params;
	ZeroMemory(&present_params, sizeof(present_params));
	RECT window_rect;
	CONST HWND window_handle = reinterpret_cast<HWND>(window);
	if (!GetWindowRect(window_handle, &window_rect)) {
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	present_params.BackBufferWidth = window_rect.right - window_rect.left;
	present_params.BackBufferHeight = window_rect.bottom - window_rect.top;
	present_params.BackBufferFormat = D3DFMT_A8R8G8B8;
	present_params.BackBufferCount = 0;
	DWORD quality_levels = 1;
	present_params.MultiSampleType = D3DMULTISAMPLE_NONE;
	if (anti_aliasing) {
		for (int multi_sample_type = D3DMULTISAMPLE_16_SAMPLES; multi_sample_type >= D3DMULTISAMPLE_2_SAMPLES; --multi_sample_type) {
			CONST D3DMULTISAMPLE_TYPE multi_sample = (D3DMULTISAMPLE_TYPE) multi_sample_type;
			if (SUCCEEDED(d3d9->CheckDeviceMultiSampleType(D3DADAPTER_DEFAULT, D3DDEVTYPE_HAL, present_params.BackBufferFormat, TRUE, multi_sample, &quality_levels))) {
				present_params.MultiSampleType = multi_sample;
				break;
			}
		}
	}
	present_params.MultiSampleQuality = quality_levels - 1;
	present_params.SwapEffect = D3DSWAPEFFECT_DISCARD;
	present_params.hDeviceWindow = window_handle;
	present_params.Windowed = TRUE;
	present_params.EnableAutoDepthStencil = TRUE;
	present_params.AutoDepthStencilFormat = D3DFMT_D16;
	present_params.Flags = D3DPRESENTFLAG_DISCARD_DEPTHSTENCIL;
	present_params.FullScreen_RefreshRateInHz = 0;
	present_params.PresentationInterval = D3DPRESENT_INTERVAL_DEFAULT;
	IDirect3DDevice9 *d3d_device;
	if (FAILED(d3d9->CreateDevice(D3DADAPTER_DEFAULT, D3DDEVTYPE_HAL, window_handle, D3DCREATE_SOFTWARE_VERTEXPROCESSING, &present_params, &d3d_device))) {
		return org_bitbucket_reliant_memory_MemoryStream_NULL;
	}
	return reinterpret_cast<jlong>(d3d_device);
}

bool mouse_enabled = true, keyboard_enabled = true;
std::vector<DWORD> keys_down;
HHOOK mouse_hook = NULL;
char dll_name_[MAX_PATH] = {0};
int mouse_x = 0, mouse_y = 0;

/*
 * Class:     org_bitbucket_reliant_Reliant
 * Method:    getMouseX
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_bitbucket_reliant_Reliant_getMouseX
  (JNIEnv *env, jobject this_obj) {
	return mouse_x;
}

/*
 * Class:     org_bitbucket_reliant_Reliant
 * Method:    getMouseY
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_bitbucket_reliant_Reliant_getMouseY
  (JNIEnv *env, jobject this_obj) {
	return mouse_y;
}

void DeleteKey(DWORD key) {
	for (std::vector<DWORD>::iterator it = keys_down.begin(); it != keys_down.end();) {
		if (*it == key) {
			it = keys_down.erase(it);
		} else {
			++it;
		}
	}
}

LRESULT CALLBACK LowLevelMouseProc(
  _In_ int    nCode,
  _In_ WPARAM wParam,
  _In_ LPARAM lParam
) {
	if (nCode >= 0 && wParam == WM_MOUSEMOVE) {
		mouse_x = GET_X_LPARAM(lParam);
		mouse_y = GET_Y_LPARAM(lParam);
	}
	if (GetForegroundWindow() == FindWindow("Valve001", NULL) && nCode >= 0 && (wParam == WM_LBUTTONDOWN || wParam == WM_RBUTTONDOWN)/* && !mouse_enabled*/) {
		RECT foreground_rect;
		if (GetWindowRect(GetForegroundWindow(), &foreground_rect)) {
			POINT pt = reinterpret_cast<MSLLHOOKSTRUCT*>(lParam)->pt;
			if (pt.x >= foreground_rect.left && pt.x <= foreground_rect.right && pt.y >= foreground_rect.top && pt.y <= foreground_rect.bottom) {
				keys_down.push_back(wParam == WM_LBUTTONDOWN ? VK_LBUTTON : VK_RBUTTON);
				return 1;
			}
		}
	}
	if (wParam == WM_LBUTTONUP || wParam == WM_RBUTTONUP) {
		DeleteKey(wParam == WM_LBUTTONUP ? VK_LBUTTON : VK_RBUTTON);
	}
	return CallNextHookEx(NULL, nCode, wParam, lParam);
}

/*
 * Class:     org_bitbucket_reliant_Reliant
 * Method:    installMouseHook
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_Reliant_installMouseHook
  (JNIEnv *env, jobject this_obj, jstring dll_name) {
	const char *dll_name_native = env->GetStringUTFChars(dll_name, JNI_FALSE);
	const jlong result = /*reinterpret_cast<jlong>(mouse_hook = SetWindowsHookEx(WH_MOUSE_LL, LowLevelMouseProc, GetModuleHandle(dll_name_native), 0))*/0;
	strcpy(dll_name_, dll_name_native);
	env->ReleaseStringUTFChars(dll_name, dll_name_native);
	return result;
}

LRESULT CALLBACK LowLevelKeyboardProc(
  _In_ int    nCode,
  _In_ WPARAM wParam,
  _In_ LPARAM lParam
) {
	if (GetForegroundWindow() == FindWindow("Valve001", NULL) && nCode >= 0 && (wParam == WM_KEYDOWN || wParam == WM_SYSKEYDOWN) && !keyboard_enabled) {
		keys_down.push_back(reinterpret_cast<KBDLLHOOKSTRUCT*>(lParam)->vkCode);
		return 1;
	}
	if (wParam == WM_KEYUP || wParam == WM_SYSKEYUP) {
		DeleteKey(reinterpret_cast<KBDLLHOOKSTRUCT*>(lParam)->vkCode);
	}
	return CallNextHookEx(NULL, nCode, wParam, lParam);
}

/*
 * Class:     org_bitbucket_reliant_Reliant
 * Method:    installKeyboardHook
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_Reliant_installKeyboardHook
  (JNIEnv *env, jobject this_obj, jstring dll_name) {
	const char *dll_name_native = env->GetStringUTFChars(dll_name, JNI_FALSE);
	const jlong result = reinterpret_cast<jlong>(SetWindowsHookEx(WH_KEYBOARD_LL, LowLevelKeyboardProc, GetModuleHandle(dll_name_native), 0));
	env->ReleaseStringUTFChars(dll_name, dll_name_native);
	return result;
}

/*
 * Class:     org_bitbucket_reliant_Reliant
 * Method:    uninstallHook
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_Reliant_uninstallHook
  (JNIEnv *env, jobject this_obj, jlong hook_proc) {
	return hook_proc == 0 ? 0 : UnhookWindowsHookEx(reinterpret_cast<HHOOK>(hook_proc));
}

/*
 * Class:     org_bitbucket_reliant_Reliant
 * Method:    isMouseEnabled
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_Reliant_isMouseEnabled
  (JNIEnv *env, jobject this_obj) {
	return mouse_enabled;
}

/*
 * Class:     org_bitbucket_reliant_Reliant
 * Method:    isKeyboardEnabled
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_Reliant_isKeyboardEnabled
  (JNIEnv *env, jobject this_obj) {
	return keyboard_enabled;
}

/*
 * Class:     org_bitbucket_reliant_Reliant
 * Method:    setMouseEnabled
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_bitbucket_reliant_Reliant_setMouseEnabled
  (JNIEnv *env, jobject this_obj) {
	mouse_enabled = !mouse_enabled;
	if (mouse_enabled) {
		std::vector<INPUT> inputs;
		for (std::vector<DWORD>::iterator it = keys_down.begin(); it != keys_down.end();) {
			if (*it < 0x1 || *it > 0x6) {
				++it;
				continue;
			}
			INPUT in;
			in.type = INPUT_MOUSE;
			MOUSEINPUT mouse_in;
			mouse_in.dx = mouse_in.dy = 0;
			mouse_in.mouseData = 0;
			mouse_in.dwFlags = *it == VK_LBUTTON ? MOUSEEVENTF_LEFTDOWN : MOUSEEVENTF_RIGHTDOWN;
			mouse_in.time = 0;
			mouse_in.dwExtraInfo = GetMessageExtraInfo();
			in.mi = mouse_in;
			inputs.push_back(in);
			it = keys_down.erase(it);
		}
		SendInput(inputs.size(), inputs.data(), sizeof(INPUT));
		if (mouse_hook != NULL) {
			UnhookWindowsHookEx(mouse_hook);
			mouse_hook = NULL;
		}
	} else {
		mouse_hook = SetWindowsHookEx(WH_MOUSE_LL, LowLevelMouseProc, GetModuleHandle(dll_name_), 0);
	}
}

/*
 * Class:     org_bitbucket_reliant_Reliant
 * Method:    setKeyboardEnabled
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_bitbucket_reliant_Reliant_setKeyboardEnabled
  (JNIEnv *env, jobject this_obj) {
	keyboard_enabled = !keyboard_enabled;
	if (keyboard_enabled) {
		std::vector<INPUT> inputs;
		for (std::vector<DWORD>::iterator it = keys_down.begin(); it != keys_down.end();) {
			if (*it >= 0x1 && *it <= 0x6) {
				++it;
				continue;
			}
			INPUT in;
			in.type = INPUT_KEYBOARD;
			KEYBDINPUT kb_in;
			kb_in.wVk = *it;
			kb_in.wScan = 0;
			kb_in.dwFlags = 0;
			kb_in.time = 0;
			kb_in.dwExtraInfo = GetMessageExtraInfo();
			in.ki = kb_in;
			inputs.push_back(in);
			it = keys_down.erase(it);
		}
		SendInput(inputs.size(), inputs.data(), sizeof(INPUT));
	}
}
