#include "stdafx.h"

#include "audio_session_data.h"

namespace projx {

	inline void ComSafeRelease(IUnknown *com_obj) {
		if (com_obj != nullptr) {
			com_obj->Release();
		}
	}
	AudioSessionsData::AudioSessionsData() : device_enum(NULL), out_device(NULL), session_manager(NULL), session_enum(NULL), session_count(0), valid(true) {
		if (FAILED(CoCreateInstance(kCldIdMMDeviceEnumerator, NULL, CLSCTX_ALL, kIdIMMDeviceEnumerator, (LPVOID*)&device_enum))) {
			valid = false;
		}
		if (FAILED(device_enum->GetDefaultAudioEndpoint(EDataFlow::eRender, ERole::eMultimedia, &out_device))) {
			valid = false;
		}
		if (FAILED(out_device->Activate(kIdIAudioSessionManager2, CLSCTX_ALL, NULL, (void**)&session_manager))) {
			valid = false;
		}
		if (FAILED(session_manager->GetSessionEnumerator(&session_enum))) {
			valid = false;
		}
		if (FAILED(session_enum->GetCount(&session_count))) {
			valid = false;
		}
	}
	AudioSessionsData::AudioSessionsData(AudioSessionsData &&src) {
		device_enum = src.device_enum;
		out_device = src.out_device;
		session_manager = src.session_manager;
		session_enum = src.session_enum;
		session_count = src.session_count;
		valid = src.valid;
		src.device_enum = NULL;
		src.out_device = NULL;
		src.session_manager = NULL;
		src.session_enum = NULL;
		src.session_count = 0;
		src.valid = false;
	}
	AudioSessionsData::~AudioSessionsData() {
		ComSafeRelease(session_enum);
		ComSafeRelease(session_manager);
		ComSafeRelease(out_device);
		ComSafeRelease(device_enum);
	}
	AudioSessionsData &AudioSessionsData::operator=(AudioSessionsData &&src) {
		if (this != &src) {
			ComSafeRelease(session_enum);
			ComSafeRelease(session_manager);
			ComSafeRelease(out_device);
			ComSafeRelease(device_enum);
			device_enum = src.device_enum;
			out_device = src.out_device;
			session_manager = src.session_manager;
			session_enum = src.session_enum;
			session_count = src.session_count;
			valid = src.valid;
			src.device_enum = NULL;
			src.out_device = NULL;
			src.session_manager = NULL;
			src.session_enum = NULL;
			src.session_count = 0;
			src.valid = false;
		}
		return *this;
	}
	IMMDeviceEnumerator *AudioSessionsData::get_device_enum() {
		return device_enum;
	}
	IMMDevice *AudioSessionsData::get_out_device() {
		return out_device;
	}
	IAudioSessionManager2 *AudioSessionsData::get_session_manager() {
		return session_manager;
	}
	IAudioSessionEnumerator *AudioSessionsData::get_session_enum() {
		return session_enum;
	}
	int AudioSessionsData::get_session_count() const {
		return session_count;
	}
	bool AudioSessionsData::is_valid() const {
		return valid;
	}

}  // namespace projx
