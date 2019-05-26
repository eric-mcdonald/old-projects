#include "stdafx.h"

#include "email.h"

namespace orion {

	Email::Email(const std::string &host_, const std::string &username_, const std::string &password_) : host(host_), username(username_), password(password_) {}
	// TODO Implement these
	Email::~Email() {

	}
	ErrorCode Email::Begin(const std::string &subject) {
		return ErrorCodes::kInvalidOp;
	}
	ErrorCode Email::PushReceipent(const std::string &receipent) {
		return ErrorCodes::kInvalidOp;
	}
	ErrorCode Email::AddBody(const std::string &body) {
		return ErrorCodes::kInvalidOp;
	}
	ErrorCode Email::PushAttachment(const std::string &file) {
		return ErrorCodes::kInvalidOp;
	}
	ErrorCode Email::Send() {
		return ErrorCodes::kInvalidOp;
	}
	std::string Email::get_host() const {
		return host;
	}
	std::string Email::get_username() const {
		return username;
	}

}  // namespace orion
