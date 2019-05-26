#pragma once

#include <string>
#include <map>

#include "se_client_def.h"
#include "cheat_def.h"

namespace source_engine {

	class NetVarRegistry {
		std::map<std::string,std::map<std::string,int>*> net_vars;
		cheat::ErrorCode RegisterTable(RecvTable *);
	public:
		~NetVarRegistry();
		auto &get_net_vars();
		cheat::ErrorCode Get(const std::string &/*table_name*/, const std::string &/*var_name*/, int *) const;
		cheat::ErrorCode Read(ClientClass *);
		inline int GetOffset(const std::string &table_name, const std::string &var_name) {
			int var_offset;
			cheat::ErrorCode error = Get(table_name, var_name, &var_offset);
			if (error != cheat::CheatErrorCodes::kSuccess) {
				*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to retrieve network variable \"" << table_name << "->" << var_name << "\" with error code " << error << "." << std::endl;
				return 0;
			}
			return var_offset;
		}
	};

	extern NetVarRegistry *g_net_vars;

}  // namespace source_engine
