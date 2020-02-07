#include "stdafx.h"

#include "app_volume_ctrl.h"

#include "audio_session_data.h"

namespace projx {

	AppVolumeControl::AppVolumeControl(const std::string &process_) : process(process_) {}
	ProgError AppVolumeControl::SetMuted(BOOL muted) {
		AudioSessionsData sessions;
		if (!sessions.is_valid()) {
			return ErrorCodes::kFailedApiCall;
		}
		bool has_muted = false;
		for (int i = 0; i < sessions.get_session_count(); ++i) {
			IAudioSessionControl *session_ctrl;
			if (FAILED(sessions.get_session_enum()->GetSession(i, &session_ctrl))) {
				continue;
			}
			IAudioSessionControl2 *session_ctrl2;
			if (FAILED(session_ctrl->QueryInterface(kIdIAudioSessionControl2, (void**)&session_ctrl2))) {
				continue;
			}
			DWORD ctrl_proc_id;
			if (FAILED(session_ctrl2->GetProcessId(&ctrl_proc_id))) {
				continue;
			}
			std::string proc_path;
			if (GetProcPathById(ctrl_proc_id, &proc_path) != ErrorCodes::kSuccess) {
				continue;
			}
			std::string proc_name;
			FileSplitDir(proc_path, &proc_name);
			if (StrEqIgnoreCase(proc_name, process)) {
				ISimpleAudioVolume *audio_vol;
				if (FAILED(session_ctrl2->QueryInterface(kIdISimpleAudioVolume, (void**)&audio_vol))) {
					continue;
				}
				audio_vol->SetMute(muted, NULL);
				has_muted = true;
			}
		}
		return has_muted ? ErrorCodes::kSuccess : ErrorCodes::kFileNotFound;
	}
	ProgError AppVolumeControl::IsMuted(BOOL *muted_out) {
		if (muted_out == nullptr) {
			return ErrorCodes::kInvalidParam;
		}
		AudioSessionsData sessions;
		if (!sessions.is_valid()) {
			return ErrorCodes::kFailedApiCall;
		}
		for (int i = 0; i < sessions.get_session_count(); ++i) {
			IAudioSessionControl *session_ctrl;
			if (FAILED(sessions.get_session_enum()->GetSession(i, &session_ctrl))) {
				continue;
			}
			IAudioSessionControl2 *session_ctrl2;
			if (FAILED(session_ctrl->QueryInterface(kIdIAudioSessionControl2, (void**)&session_ctrl2))) {
				continue;
			}
			DWORD ctrl_proc_id;
			if (FAILED(session_ctrl2->GetProcessId(&ctrl_proc_id))) {
				continue;
			}
			std::string proc_path;
			if (GetProcPathById(ctrl_proc_id, &proc_path) != ErrorCodes::kSuccess) {
				continue;
			}
			std::string proc_name;
			FileSplitDir(proc_path, &proc_name);
			if (StrEqIgnoreCase(proc_name, process)) {
				ISimpleAudioVolume *audio_vol;
				if (FAILED(session_ctrl2->QueryInterface(kIdISimpleAudioVolume, (void**)&audio_vol))) {
					continue;
				}
				return audio_vol->GetMute(muted_out);
			}
		}
		return ErrorCodes::kFileNotFound;
	}

}  // namespace projx
