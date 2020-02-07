#pragma once

#include <Windows.h>
#include <detours.h>

#include "px_def.h"

namespace projx {

	template<typename FuncDef>
	class PROJECTXSHARED_API HookedFunc {
		FuncDef real_func, new_func;
		bool hooked;
	public:
		HookedFunc(FuncDef real_func_, FuncDef new_func_) : real_func(real_func_), new_func(new_func_), hooked(false) {}
		~HookedFunc() {
			Detach();
		}
		bool is_hooked() const {
			return hooked;
		}
		ProgError Attach() {
			if (hooked || real_func == nullptr || new_func == nullptr) {
				return ErrorCodes::kInvalidState;
			}
			if (DetourIsHelperProcess()) {
				return ErrorCodes::kInvalidOp;
			}
			ProgError error = DetourTransactionBegin();
			if (error != NO_ERROR) {
				return error;
			}
			error = DetourUpdateThread(GetCurrentThread());
			if (error != NO_ERROR) {
				DetourTransactionAbort();
				return error;
			}
			error = DetourAttach(&(PVOID&)real_func, new_func);
			if (error != NO_ERROR) {
				DetourTransactionAbort();
				return error;
			}
			error = DetourTransactionCommit();
			if (error != NO_ERROR) {
				return error;
			}
			hooked = true;
			return ErrorCodes::kSuccess;
		}
		ProgError Detach() {
			if (!hooked || real_func == nullptr || new_func == nullptr) {
				return ErrorCodes::kInvalidState;
			}
			if (DetourIsHelperProcess()) {
				return ErrorCodes::kInvalidOp;
			}
			ProgError error = DetourTransactionBegin();
			if (error != NO_ERROR) {
				return error;
			}
			error = DetourUpdateThread(GetCurrentThread());
			if (error != NO_ERROR) {
				DetourTransactionAbort();
				return error;
			}
			error = DetourDetach(&(PVOID&)real_func, new_func);
			if (error != NO_ERROR) {
				DetourTransactionAbort();
				return error;
			}
			error = DetourTransactionCommit();
			if (error != NO_ERROR) {
				return error;
			}
			hooked = false;
			return ErrorCodes::kSuccess;
		}
		FuncDef get_real_func() const {
			return real_func;
		}
		bool operator==(const HookedFunc &rhs) const {
			return real_func == rhs.real_func && new_func == rhs.new_func;
		}
		bool operator!=(const HookedFunc &rhs) const {
			return !(*this == rhs);
		}
	};

}  // namespace projx
