#include "stdafx.h"

#include "string_utils.h"

#include "net_var_registry.h"

namespace source_engine {

	NetVarRegistry *g_net_vars = nullptr;

	NetVarRegistry::~NetVarRegistry() {
		for (const auto &it : net_vars) {
			delete it.second;
		}
		net_vars.clear();
	}
	auto &NetVarRegistry::get_net_vars() {
		return net_vars;
	}
	cheat::ErrorCode NetVarRegistry::Get(const std::string &table_name, const std::string &var_name, int *var_offs_out) const {
		if (var_offs_out == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		for (const auto &table_it : net_vars) {
			if (table_it.first == table_name) {
				for (const auto &var_it : *table_it.second) {
					if (var_it.first == var_name) {
						*var_offs_out = var_it.second;
						return cheat::CheatErrorCodes::kSuccess;
					}
				}
				break;
			}
		}
		return cheat::CheatErrorCodes::kElemNotFound;
	}
	cheat::ErrorCode NetVarRegistry::RegisterTable(RecvTable *table) {
		if (table == nullptr || table->m_pNetTableName == nullptr || strlen(table->m_pNetTableName) == 0) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		std::map<std::string, int> *class_vars = nullptr;
		for (int i = 0; i < table->m_nProps; ++i) {
			RecvProp prop = table->m_pProps[i];
			if (prop.m_pVarName == nullptr || strlen(prop.m_pVarName) == 0 || !strcmp(prop.m_pVarName, "baseclass") || isdigit(prop.m_pVarName[0])) {
				continue;
			}
			if (prop.m_RecvType == SendPropType::DPT_DataTable) {
				RegisterTable(prop.m_pDataTable);
			}
			if (class_vars == nullptr) {
				class_vars = new std::map<std::string, int>();
			}
			//std::cout << table->m_pNetTableName << "->" << prop.m_pVarName << " = " << prop.m_Offset << std::endl;
			(*class_vars)[prop.m_pVarName] = prop.m_Offset;
		}
		if (class_vars != nullptr && !class_vars->empty()) {
			net_vars[table->m_pNetTableName] = class_vars;
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode NetVarRegistry::Read(ClientClass *class_head) {
		if (class_head == nullptr) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		do {
			RegisterTable(class_head->m_pRecvTable);
			class_head = class_head->m_pNext;
		} while (class_head != nullptr);
		return cheat::CheatErrorCodes::kSuccess;
	}

}  // namespace source_engine
