#pragma once

#include <string>

namespace cheat {

	// Eric McDonald: All error constants are a negative integer.
	enum CheatErrorCodes {
		kSuccess = 0,
		kFeatDisabled = -1,
		kElemNotFound = -2,
		kElemAlreadyRegistered = -3,
		kInvalidParam = -4,
		kStartTimeNotSet = -5,
		kInvalidOp = -6,
		kSigNotFound = -7,
		kInvalidAddr = -8,
		kNotEnoughElems = -9,
		kFileNotOpen = -10,
		kValOutOfRange = -11,
		kValInvalidState = -12,
	};
	enum Priorities {
		kHighest,
		kHigher,
		kHigh,
		kNormal,
		kLow,
		kLower,
		kLowest
	};
	enum InitStates {
		kNone,
		kDone,
		kInit,
		kUninit,
	};
	typedef int ErrorCode;  // Eric McDonald: For functions that can throw a Windows error, or a CheatErrorCodes.

	static const std::string kCheatName = "CS:GO Cheat";
	static const std::string kProcName = "Counter-Strike: Global Offensive";
	static const std::string kDataDir = "data", kCfgDir = kDataDir + "\\config", kLogDir = kDataDir + "\\logs";
	static constexpr float kCheatVer = 1.0F;
	extern InitStates g_init_state;

}  // namespace cheat
