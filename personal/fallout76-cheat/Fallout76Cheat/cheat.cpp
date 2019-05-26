#include "stdafx.h"

#include "cheat.h"
#include "native_utils.h"
#include "overlay.h"
#include "cmd_impl.h"
#include "routine_test.h"
#include "render_routine_test.h"

namespace cheat {

	Cheat::Cheat() : speed(1.0F), cmd_handler(nullptr), shutting_down(false), update_thread(NULL), general_cfg(nullptr) {
		std::string cfg_file;
		if (internals::GetModuleDirectory(internals::g_this_dll, &cfg_file) == CheatErrorCodes::kSuccess) {
			general_cfg = new Configuration(cfg_file + "\\" + kCfgDir + "\\general.cfg");
		}
		general_cfg->SetEntry("multisample_type", D3DMULTISAMPLE_NONE);
		general_cfg->SetEntry("multisample_quality", 0);
		RegisterEvtManager("Update", new EventManager());
	};
	Cheat::~Cheat() {
		shutting_down = true;
		if (update_thread != NULL) {
			WaitForSingleObject(update_thread, INFINITE);
			internals::CloseNativeHandle(update_thread);
			update_thread = NULL;
		}
		if (general_cfg != nullptr) {
			delete general_cfg;
			general_cfg = nullptr;
		}
		if (cmd_handler != nullptr) {
			delete cmd_handler;
			cmd_handler = nullptr;
		}
		UnregisterEvtManager("Update");
	}
	void Cheat::UnregisterEvtManager(const std::string &event_id) {
		if (evt_managers.find(event_id) != evt_managers.end()) {
			EventManager *manager = evt_managers[event_id];
			evt_managers.erase(event_id);
			delete manager;
			manager = nullptr;
		}
	}
	ErrorCode Cheat::RegisterEvtManager(const std::string &event_id, EventManager *manager) {
		if (manager == nullptr) {
			return CheatErrorCodes::kInvalidParam;
		}
		UnregisterEvtManager(event_id);
		evt_managers[event_id] = manager;
		return CheatErrorCodes::kSuccess;
	}
	ErrorCode Cheat::GetEvtManager(const std::string &event_id, EventManager **manager_out) {
		if (manager_out == nullptr) {
			return CheatErrorCodes::kInvalidParam;
		}
		if (evt_managers.find(event_id) == evt_managers.end()) {
			return CheatErrorCodes::kElemNotFound;
		}
		*manager_out = evt_managers[event_id];
		return CheatErrorCodes::kSuccess;
	}
	Cheat &Cheat::GetInstance() {
		static Cheat instance;
		return instance;
	}
	ErrorCode Cheat::RegisterCmd(std::string id, Command *cmd) {
		ErrorCode error = CheatErrorCodes::kSuccess;
		error = cmds.Register(id, cmd);
		return error;
	}
	ErrorCode Cheat::RegisterCmd(Command *cmd) {
		ErrorCode error = CheatErrorCodes::kSuccess;
		error = RegisterCmd(cmd->get_id(), cmd);
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		for (const auto &it : cmd->get_aliases()) {
			error = RegisterCmd(it, cmd);
			if (error != CheatErrorCodes::kSuccess) {
				return error;
			}
		}
		return error;
	}
	ErrorCode Cheat::RegisterRoutine(BasicRoutine *routine) {
		ErrorCode error = RegisterRoutine(reinterpret_cast<Routine*>(routine));
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		routine->OnRegistered();
		return CheatErrorCodes::kSuccess;
	}
	ErrorCode Cheat::RegisterRoutine(Routine *routine) {
		ErrorCode error = CheatErrorCodes::kSuccess;
		error = routines.Register(routine->Name(), routine);
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		return error;
	}
	FeatureRegistry &Cheat::get_cmds() {
		return cmds;
	}
	FeatureRegistry &Cheat::get_routines() {
		return routines;
	}
	ErrorCode Cheat::GetCmd(std::string id, Command **cmd_out) const {
		return cmds.Get(id, reinterpret_cast<FeatureInterface**>(cmd_out));
	}
	ErrorCode Cheat::GetRoutine(std::string name, Routine **routine_out) const {
		return routines.Get(name, reinterpret_cast<FeatureInterface**>(routine_out));
	}
	void Cheat::set_speed(float speed_) {
		speed = speed_;
	}
	float Cheat::get_speed() const {
		return speed;
	}
	void Cheat::StartCmdHandler() {
		if (cmd_handler == nullptr) {
			cmd_handler = new CmdHandler(std::cin, *g_info_log, *g_err_log, cmds);
		}
	}
	CmdHandler *Cheat::get_cmd_handler() {
		return cmd_handler;
	}
	HANDLE Cheat::get_update_thread() {
		return update_thread;
	}
	bool Cheat::is_shutting_down() const {
		return shutting_down;
	}
	void Update(Cheat *instance) {
		while (!instance->is_shutting_down()) {
			Event update_evt = { 0 };
			update_evt.source_fn = Update;
			update_evt.data = instance;
			EventManager *manager;
			if (instance->GetEvtManager("Update", &manager) == CheatErrorCodes::kSuccess) {
				manager->Dispatch(&update_evt, &update_evt);
			}
			internals::FixedSleep(1);
		}
	}
	void Cheat::StartUpdateThread() {
		if (update_thread == NULL) {
			update_thread = CreateThread(NULL, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(Update), this, 0, NULL);
		}
	}
	const std::map<std::string, EventManager*> &Cheat::get_evt_managers() const {
		return evt_managers;
	}
	Configuration *Cheat::get_general_cfg() {
		return general_cfg;
	}
	ErrorCode RegisterRoutines(Cheat &instance) {
		ErrorCode error = instance.RegisterRoutine(new test::TestRoutine());
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		auto *render_test = new test::RenderTestRoutine();
		error = instance.RegisterRoutine(render_test);
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		error = instance.get_renderer().RegisterRenderable(render_test);
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		return CheatErrorCodes::kSuccess;
	}
	void UnregisterCmds(Cheat &instance) {
		Command *cmd;
		if (instance.GetCmd("toggle", &cmd) == CheatErrorCodes::kSuccess) {
			instance.get_cmds().Unregister("toggle");
			delete cmd;
			cmd = nullptr;
		}
	}
	void UnregisterRoutines(Cheat &instance) {
		Routine *routine;
		if (instance.GetRoutine("test", &routine) == CheatErrorCodes::kSuccess) {
			instance.get_routines().Unregister("test");
			delete routine;
			routine = nullptr;
		}
		test::RenderTestRoutine *render_test;
		if (instance.GetRoutine("render_test", reinterpret_cast<Routine**>(&render_test)) == CheatErrorCodes::kSuccess) {
			instance.get_routines().Unregister("render_test");
			instance.get_renderer().UnregisterRenderable(render_test);
			delete render_test;
			render_test = nullptr;
		}
	}
	internals::Renderer &Cheat::get_renderer() {
		return renderer;
	}
	ErrorCode Cheat::Start() {
		ErrorCode error = CheatErrorCodes::kSuccess;
		StartUpdateThread();
		internals::CreateOverlay(*get_general_cfg());
		error = internals::InstallRenderHooks();
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		error = RegisterRoutines(*this);
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		error = RegisterCmds(*this);
		if (error != CheatErrorCodes::kSuccess) {
			return error;
		}
		for (auto &it : routines.get_unique_instances()) {
			it.second->get_config()->Read();
			Routine *routine = reinterpret_cast<Routine*>(it.second);
			bool enabled;
			if (routine->get_config()->GetEntry("start_enabled", &enabled) == CheatErrorCodes::kSuccess) {
				routine->set_enabled(enabled);
			}
		}
		return error;
	}
	void Cheat::Stop() {
		for (auto &it : routines.get_unique_instances()) {
			ErrorCode error = it.second->get_config()->Write();
			if (error != CheatErrorCodes::kSuccess) {
				*g_err_log << g_err_log->GetPrefix() << "Failed to write to " << it.second->get_config()->get_file_path() << " with error code " << error << "." << std::endl;
			}
		}
		UnregisterCmds(*this);
		UnregisterRoutines(*this);
		internals::UninstallRenderHooks();
		if (internals::g_overlay != nullptr) {
			delete internals::g_overlay;
			internals::g_overlay = nullptr;
		}
	}

}  // namespace cheat
