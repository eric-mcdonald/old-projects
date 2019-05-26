#include "stdafx.h"

#include "hooks.h"

#include "cheat.h"
#include "se_event_impl.h"
#include "native_utils.h"
#include "se_offset_cache.h"
#include "se_render_def.h"

#include "source_engine.h"

namespace source_engine {

	typedef void (__stdcall *CreateMoveFn)(int sequence_number, float input_sample_frametime, bool active);
	typedef void(__fastcall *ApplyEntityGlowEffectsFn)(void *this_ptr, void *edx, const void *pSetup, int nSplitScreenSlot, void *pRenderContext, float flBloomScale, int x, int y, int w, int h);
	typedef void(__fastcall *FrameStageNotifyFn)(void *ecx, void *edx, ClientFrameStage_t curStage);

	IBaseClientDLL *g_client = nullptr;
	IClientEntityList *g_entities = nullptr;
	IVEngineClient013 *g_engine = nullptr;
	std::map<std::string,internals::VtableFuncInfo> old_funcs;
	ApplyEntityGlowEffectsFn real_apply_ent_glow_effs = nullptr;

	void __stdcall HCreateMove(int sequence_number, float input_sample_frametime, bool active) {
		auto func = old_funcs.find("CreateMove");  // Eric McDonald: This function might not be added to old_funcs yet.
		if (func != old_funcs.end()) {
			cheat::EventManager *pre_manager = nullptr, *post_manager = nullptr;
			if (cheat::Cheat::GetInstance().GetEvtManager("HCreateMove#pre", &pre_manager) == cheat::CheatErrorCodes::kSuccess && cheat::Cheat::GetInstance().GetEvtManager("HCreateMove#post", &post_manager) == cheat::CheatErrorCodes::kSuccess) {
				cheat::Event event_obj = { 0 };
				event_obj.source_fn = HCreateMove;
				CreateMoveData data;
				data.sequence_number = sequence_number;
				data.input_sample_frametime = input_sample_frametime;
				data.active = active;
				event_obj.data = reinterpret_cast<void*>(&data);
				if (pre_manager->Dispatch(&event_obj, &event_obj) == cheat::CheatErrorCodes::kSuccess && event_obj.cancel) {
					return;
				}
				reinterpret_cast<CreateMoveFn>(func->second.func_ptr)(sequence_number, input_sample_frametime, active);
				event_obj = { 0 };
				event_obj.source_fn = HCreateMove;
				data = { 0 };
				data.sequence_number = sequence_number;
				data.input_sample_frametime = input_sample_frametime;
				data.active = active;
				event_obj.data = reinterpret_cast<void*>(&data);
				post_manager->Dispatch(&event_obj, &event_obj);
			}
		}
	}
	void __fastcall HApplyEntityGlowEffects(void *this_ptr, void *edx, const void *pSetup, int nSplitScreenSlot, void *pRenderContext, float flBloomScale, int x, int y, int w, int h) {
		cheat::EventManager *evt_manager;
		if (cheat::Cheat::GetInstance().GetEvtManager("HApplyEntityGlowEffects", &evt_manager) == cheat::CheatErrorCodes::kSuccess) {
			cheat::Event event_obj = { 0 };
			event_obj.source_fn = HApplyEntityGlowEffects;
			ApplyEntityGlowEffectsData data;
			data.this_ptr = this_ptr;
			data.edx = edx;
			data.pSetup = pSetup;
			data.nSplitScreenSlot = nSplitScreenSlot;
			data.pRenderContext = pRenderContext;
			data.flBloomScale = flBloomScale;
			data.x = x;
			data.y = y;
			data.w = w;
			data.h = h;
			event_obj.data = reinterpret_cast<void*>(&data);
			evt_manager->Dispatch(&event_obj, &event_obj);
		}
		real_apply_ent_glow_effs(this_ptr, edx, pSetup, nSplitScreenSlot, pRenderContext, flBloomScale, x, y, w, h);
	}
	void __fastcall HFrameStageNotify(void *ecx, void *edx, ClientFrameStage_t curStage) {
		auto func = old_funcs.find("FrameStageNotify");
		if (func != old_funcs.end()) {
			reinterpret_cast<FrameStageNotifyFn>(func->second.func_ptr)(ecx, edx, curStage);
			cheat::EventManager *pre_manager = nullptr, *post_manager = nullptr;
			if (cheat::Cheat::GetInstance().GetEvtManager("HFrameStageNotify#pre", &pre_manager) == cheat::CheatErrorCodes::kSuccess && cheat::Cheat::GetInstance().GetEvtManager("HFrameStageNotify#post", &post_manager) == cheat::CheatErrorCodes::kSuccess) {
				cheat::Event event_obj = { 0 };
				event_obj.source_fn = HFrameStageNotify;
				FrameStageNotifyData data;
				data.ecx = ecx;
				data.edx = edx;
				data.curStage = curStage;
				event_obj.data = reinterpret_cast<void*>(&data);
				if (pre_manager->Dispatch(&event_obj, &event_obj) == cheat::CheatErrorCodes::kSuccess && event_obj.cancel) {
					return;
				}
				reinterpret_cast<FrameStageNotifyFn>(func->second.func_ptr)(ecx, edx, curStage);
				event_obj = { 0 };
				event_obj.source_fn = HFrameStageNotify;
				data = { 0 };
				data.ecx = ecx;
				data.edx = edx;
				data.curStage = curStage;
				event_obj.data = reinterpret_cast<void*>(&data);
				post_manager->Dispatch(&event_obj, &event_obj);
			}
		}
	}
	cheat::ErrorCode Init(NetVarRegistry &net_vars) {
		typedef void *(*CreateInterfaceFn)(const char * /*name*/, int * /*ret_out*/);
		CreateInterfaceFn client_create_interface = reinterpret_cast<CreateInterfaceFn>(GetProcAddress(GetModuleHandle(L"client_panorama.dll"), "CreateInterface")), engine_create_interface = reinterpret_cast<CreateInterfaceFn>(GetProcAddress(GetModuleHandle(L"engine.dll"), "CreateInterface"));
		g_client = reinterpret_cast<IBaseClientDLL*>(client_create_interface(kClientDllVer, nullptr));
		g_entities = reinterpret_cast<IClientEntityList*>(client_create_interface(kClientEntListVer, nullptr));
		g_engine = reinterpret_cast<IVEngineClient013*>(engine_create_interface(kEngineClientVer, nullptr));
		if (g_client == nullptr || g_entities == nullptr || g_engine == nullptr) {
			return cheat::CheatErrorCodes::kInvalidAddr;
		}
		return net_vars.Read(g_client->GetAllClasses());
	}
	cheat::ErrorCode InstallBasicEvtHooks(const internals::VtableFuncInfo &vtable_func, const std::string &event_type, const std::string &old_func_id) {
		cheat::ErrorCode error = cheat::Cheat::GetInstance().RegisterEvtManager(event_type + "#pre", new cheat::EventManager());
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		error = cheat::Cheat::GetInstance().RegisterEvtManager(event_type + "#post", new cheat::EventManager());
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		internals::VtableFuncInfo old_func_inf;
		error = internals::SwapVirtualFunc(vtable_func, &old_func_inf);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		old_funcs[old_func_id] = old_func_inf;
		return cheat::CheatErrorCodes::kSuccess;
	}
	inline void UnregisterBasicEvts(const std::string &event_type) {
		cheat::Cheat::GetInstance().UnregisterEvtManager(event_type + "#pre");
		cheat::Cheat::GetInstance().UnregisterEvtManager(event_type + "#post");
	}
	cheat::ErrorCode HookFuncs() {
		internals::VtableFuncInfo vtable_func;
		vtable_func.vtable = *reinterpret_cast<void***>(g_client);
		vtable_func.func_idx = 22;
		vtable_func.func_ptr = HCreateMove;
		cheat::ErrorCode error = InstallBasicEvtHooks(vtable_func, "HCreateMove", "CreateMove");
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		vtable_func.vtable = *reinterpret_cast<void***>(g_client);
		vtable_func.func_idx = 37;
		vtable_func.func_ptr = HFrameStageNotify;
		error = InstallBasicEvtHooks(vtable_func, "HFrameStageNotify", "FrameStageNotify");
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		error = cheat::Cheat::GetInstance().RegisterEvtManager("HApplyEntityGlowEffects", new cheat::EventManager());
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		error = internals::g_signatures->GetSignatureVal("ApplyEntityGlowEffects", &real_apply_ent_glow_effs);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		internals::RedirectFuncErr detours_err = internals::AttachFunc(&reinterpret_cast<void*&>(real_apply_ent_glow_effs), HApplyEntityGlowEffects);
		if (detours_err != NO_ERROR) {
			return detours_err;
		}
		return error;
	}
	cheat::ErrorCode UnhookFuncs() {
		internals::RedirectFuncErr detours_err = internals::DetachFunc(&reinterpret_cast<void*&>(real_apply_ent_glow_effs), HApplyEntityGlowEffects);
		if (detours_err != NO_ERROR) {
			return detours_err;
		}
		for (auto &it : old_funcs) {
			internals::VtableFuncInfo old_func_inf;
			internals::SwapVirtualFunc(it.second, &old_func_inf);
		}
		old_funcs.clear();
		cheat::Cheat::GetInstance().UnregisterEvtManager("HApplyEntityGlowEffects");
		UnregisterBasicEvts("HFrameStageNotify");
		UnregisterBasicEvts("HCreateMove");
		return cheat::CheatErrorCodes::kSuccess;
	}
	inline cheat::ErrorCode RegisterSignatureRel(const std::string &signature_name, const std::string &net_var_table, const std::string &net_var_name, int offset, internals::SignatureScanner &signatures) {
		int var_offset;
		cheat::ErrorCode error = g_net_vars->Get(net_var_table, net_var_name, &var_offset);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		internals::SignatureInfo info;
		info.module = GetModuleHandle(L"client_panorama.dll");
		info.start_addr = reinterpret_cast<void*>(var_offset + offset);
		info.rel_base = false;
		return signatures.RegisterSignature(signature_name, info);
	}
	cheat::ErrorCode RegisterHazeSignature(const std::string &signature_name, HMODULE module, const std::string &pattern, bool rel_base, internals::SignatureScanner &signatures, int sig_offset = 0, int read_offset = 0) {
		cheat::ErrorCode error = signatures.RegisterSignature(signature_name + "#internal0", module, pattern, sig_offset);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		internals::SignatureInfo sig_info;
		error = signatures.GetSignature(signature_name + "#internal0", &sig_info);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		sig_info.rel_base = rel_base;
		sig_info.start_addr = reinterpret_cast<void*>(reinterpret_cast<DWORD_PTR>(*reinterpret_cast<void**>(sig_info.start_addr)) + read_offset);
		return signatures.RegisterSignature(signature_name, sig_info);
	}
	inline cheat::ErrorCode RegisterSignatureRel(const std::string &signature_name, const std::string &rel_signature, internals::SignatureScanner &signatures, int offset) {
		internals::SignatureInfo info;
		cheat::ErrorCode error = signatures.GetSignature(rel_signature, &info);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		info.start_addr = reinterpret_cast<void*>(reinterpret_cast<DWORD_PTR>(info.start_addr) + offset);
		error = signatures.RegisterSignature(signature_name, info);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		return error;
	}
	cheat::ErrorCode RegisterSignatures(internals::SignatureScanner &signatures) {
		cheat::ErrorCode error = RegisterHazeSignature("g_GlowObjectManager", GetModuleHandle(L"client_panorama.dll"), "A1????????A801754B", true, signatures, +1, +4);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		error = RegisterSignatureRel("entity_glow_idx", "DT_CSPlayer", "m_flFlashDuration", +24, signatures);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		error = RegisterHazeSignature("dwEntityList", GetModuleHandle(L"client_panorama.dll"), "BB????????83FF010F8C????????3BF8", true, signatures, +1);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		error = signatures.RegisterSignature("ApplyEntityGlowEffects", GetModuleHandle(L"client_panorama.dll"), "558BEC83E4F081EC580100005633C033F6578BF989442458");
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		error = RegisterHazeSignature("m_bDormant", GetModuleHandle(L"client_panorama.dll"), "8A81????????C332C0", false, signatures, +2, +8);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		error = signatures.RegisterSignature("m_GlowObjectDefinitions#loop", GetModuleHandle(L"client_panorama.dll"), "8B44242883C6??40894424283B47??0F8CC6FEFFFF");  // Eric McDonald: Registers this signature for efficiency on subsequent startups.
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		error = RegisterSignatureRel("m_GlowObjectDefinitions#size", "m_GlowObjectDefinitions#loop", signatures, +6);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		error = RegisterSignatureRel("m_GlowObjectDefinitions#Count", "m_GlowObjectDefinitions#loop", signatures, +14);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		error = RegisterHazeSignature("dwClientState", GetModuleHandle(L"engine.dll"), "A1????????33D26A006A0033C989B0", true, signatures, +1);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		error = RegisterHazeSignature("dwClientState_GetLocalPlayer", GetModuleHandle(L"engine.dll"), "8B80????????40C3", false, signatures, +2);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		error = RegisterHazeSignature("dwClientState_State", GetModuleHandle(L"engine.dll"), "83B8??????????0F94C0C3", false, signatures, +2);
		if (error != cheat::CheatErrorCodes::kSuccess && error != cheat::CheatErrorCodes::kElemAlreadyRegistered) {
			return error;
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode OnFrameStageNotifyPost(cheat::Event *event_obj) {
		FrameStageNotifyData *event_data = reinterpret_cast<FrameStageNotifyData*>(event_obj->data);
		cheat::ErrorCode error = cheat::CheatErrorCodes::kSuccess;
		switch (event_data->curStage) {
		case ClientFrameStage_t::FRAME_RENDER_END:
			error = cheat::Cheat::GetInstance().get_renderer().RenderAll(internals::g_overlay);
			break;
		}
		return error;
	}
	cheat::ErrorCode InstallRenderHook() {
		cheat::EventManager *frame_stage_manager;
		cheat::ErrorCode error = cheat::Cheat::GetInstance().GetEvtManager("HFrameStageNotify#post", &frame_stage_manager);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		return frame_stage_manager->RegisterListener(OnFrameStageNotifyPost);
	}
	cheat::ErrorCode UninstallRenderHook() {
		cheat::EventManager *frame_stage_manager;
		cheat::ErrorCode error = cheat::Cheat::GetInstance().GetEvtManager("HFrameStageNotify#post", &frame_stage_manager);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		return frame_stage_manager->UnregisterListener(OnFrameStageNotifyPost);
	}

}  // namespace source_engine
