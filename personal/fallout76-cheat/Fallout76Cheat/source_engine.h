#pragma once

#include "cheat_def.h"
#include "net_var_registry.h"
#include "se_client_def.h"
#include "se_iclient_def.h"
#include "signatures.h"

namespace source_engine {

	extern IBaseClientDLL *g_client;
	extern IClientEntityList *g_entities;
	extern IVEngineClient013 *g_engine;
	static constexpr char *kEngineName = "Source Engine";
	static constexpr char *kGameId = "csgo";

	cheat::ErrorCode Init(NetVarRegistry &);
	cheat::ErrorCode HookFuncs();
	cheat::ErrorCode UnhookFuncs();
	cheat::ErrorCode RegisterSignatures(internals::SignatureScanner &);
	cheat::ErrorCode InstallRenderHook();
	cheat::ErrorCode UninstallRenderHook();

}  // namespace source_engine
