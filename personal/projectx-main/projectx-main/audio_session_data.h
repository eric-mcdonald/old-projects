#pragma once

#include <px_def.h>

namespace projx {

	static const CLSID kCldIdMMDeviceEnumerator = __uuidof(MMDeviceEnumerator);
	static const IID kIdIMMDeviceEnumerator = __uuidof(IMMDeviceEnumerator), kIdIAudioSessionManager2 = __uuidof(IAudioSessionManager2),
		kIdIAudioSessionControl2 = __uuidof(IAudioSessionControl2), kIdISimpleAudioVolume = __uuidof(ISimpleAudioVolume);

	class AudioSessionsData {
		IMMDeviceEnumerator *device_enum;
		IMMDevice *out_device;
		IAudioSessionManager2 *session_manager;
		IAudioSessionEnumerator *session_enum;
		int session_count;
		bool valid;
	public:
		AudioSessionsData();
		AudioSessionsData(const AudioSessionsData &) = delete;
		AudioSessionsData(AudioSessionsData &&);
		~AudioSessionsData();
		AudioSessionsData &operator=(const AudioSessionsData &) = delete;
		AudioSessionsData &operator=(AudioSessionsData &&);
		IMMDeviceEnumerator *get_device_enum();
		IMMDevice *get_out_device();
		IAudioSessionManager2 *get_session_manager();
		IAudioSessionEnumerator *get_session_enum();
		int get_session_count() const;
		bool is_valid() const;
	};

}  // namespace projx
