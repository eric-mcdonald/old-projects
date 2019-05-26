/*
Author: Eric McDonald
Date created: 2017-09-11
Purpose: Provides typedefs, constants, structures, and etc for the program. This header should not have an implementation file.
*/

#pragma once

#include <string>

// Eric McDonald: Do not include any project files in this header.

/*
Author: Eric McDonald
Date created: 2017-09-11
Purpose: Checks if the X12Obj is initialized. If it is not, then it will initialize the object.
*/
#define INIT_X12_OBJ(object, id_, desc_) if (object != nullptr && object->id.empty()) {object->id = id_;} if (object != nullptr && object->desc.empty()) {object->desc = desc_;}

namespace x12 {

	/*
	Author: Eric McDonald
	Date created: 2017-09-11
	Purpose: Provides a superclass for all Ex One Two objects. This structure allows the user to see objects through a GUI or command-line.
	*/
	struct X12Obj {
		std::string id;
		std::string desc;
	};
	namespace cli {
		class Command;
	}
	struct Parameters : X12Obj {
		// TODO(Eric McDonald) Retrieve command-line arguments and set the following variables accordingly.

		cli::Command *gui;
		std::string log_filename;
	};
	namespace log {
		class Logger;
	}
	struct Instance : X12Obj {
		/*
		TODO(Eric McDonald) Add an audio stream object, configuration object(s), overlay, etc here. Perhaps a parser for text-to-voice(?)
		Put the objects before the shutdown flag.
		*/

		Parameters creation_params;
		log::Logger *file_logger, *out_logger;
		bool shutdown = false;
	};
	static const char *kName = "Ex One Two";
	static const float kVersion = 1.0F;
	static const int kErrorBase = 0;
	enum ErrorCodes {
		kSuccess,
		kFatal = kErrorBase - 1,
		kError = kErrorBase - 2,
		kWarn = kErrorBase - 3
	};

}