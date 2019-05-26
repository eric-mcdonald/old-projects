#pragma once

#include <string>

#include "orion_def.h"

namespace orion {

	class Email {
		std::string host;
		std::string username;
		std::string password;
	public:
		Email(const std::string &/*host_*/, const std::string &/*username_*/, const std::string &/*password_*/);
		~Email();
		ErrorCode Begin(const std::string &/*subject*/);
		ErrorCode PushReceipent(const std::string &);
		ErrorCode AddBody(const std::string &);
		ErrorCode PushAttachment(const std::string &/*file*/);
		ErrorCode Send();
		std::string get_host() const;
		std::string get_username() const;
	};

}  // namespace orion
