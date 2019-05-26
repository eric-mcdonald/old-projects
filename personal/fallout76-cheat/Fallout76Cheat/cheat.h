#pragma once

#include "feature_registry.h"
#include "command.h"
#include "routine.h"
#include "timer.h"
#include "cmd_handler.h"
#include "configuration.h"
#include "event_manager.h"
#include "basic_routine.h"
#include "renderer.h"

namespace cheat {

	class Cheat {
		float speed;
		FeatureRegistry cmds, routines;
		CmdHandler *cmd_handler;
		Configuration *general_cfg;
		HANDLE update_thread;
		std::map<std::string, EventManager*> evt_managers;
		internals::Renderer renderer;
		bool shutting_down;
		Cheat();
		ErrorCode RegisterCmd(std::string, Command *);
		void StartUpdateThread();
	public:
		Cheat(const Cheat &) = delete;
		Cheat &operator=(const Cheat &) = delete;
		~Cheat();
		static Cheat &GetInstance();
		void StartCmdHandler();
		ErrorCode RegisterCmd(Command *);
		ErrorCode RegisterRoutine(Routine *);
		ErrorCode RegisterRoutine(BasicRoutine *);
		FeatureRegistry &get_cmds();
		FeatureRegistry &get_routines();
		const std::map<std::string, EventManager*> &get_evt_managers() const;
		ErrorCode GetCmd(std::string, Command **) const;
		ErrorCode GetRoutine(std::string, Routine **) const;
		void set_speed(float);
		float get_speed() const;
		CmdHandler *get_cmd_handler();
		HANDLE get_update_thread();
		bool is_shutting_down() const;
		Configuration *get_general_cfg();
		ErrorCode Start();
		void Stop();
		void UnregisterEvtManager(const std::string &);
		ErrorCode RegisterEvtManager(const std::string &, EventManager *);
		ErrorCode GetEvtManager(const std::string &, EventManager **);
		internals::Renderer &get_renderer();
	};

}  // namespace cheat
