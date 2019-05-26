#include "Message.h"

#include <iomanip>

namespace sict {

	Message::Message() : tweet_usr(""), reply_usr(""), tweet_txt("") {}
	Message::Message(const std::string &message) : Message() {
		size_t space_idx = message.find_first_of(" ");
		if (space_idx != std::string::npos) {
			tweet_usr = message.substr(0, space_idx - 0);
		}
		size_t at_symbol_idx = message.find_first_of("@");
		if (at_symbol_idx != std::string::npos) {
			size_t i = message.substr(at_symbol_idx + 1).find_first_of(" ");
			if (i != std::string::npos) {
				size_t next_space_idx = i + (at_symbol_idx + 1);
				reply_usr = message.substr(at_symbol_idx + 1, next_space_idx - (at_symbol_idx + 1));
				space_idx = next_space_idx;
			} else {
				space_idx = i + (at_symbol_idx + 1);
			}
		}
		if (space_idx != std::string::npos) {
			tweet_txt = message.substr(space_idx + 1);
		}
	}
	bool Message::empty() const {
		return tweet_usr.empty() || tweet_txt.empty();
	}
	void Message::display(std::ostream &out) const {
		if (empty()) {
			return;
		}
		std::cout << std::setfill(' ') << std::setw(7) << std::left << ">User" << std::setw(0) << ": " << tweet_usr << std::endl;
		if (!reply_usr.empty()) {
			std::cout << std::setfill(' ') << std::setw(7) << " Reply" << std::setw(0) << ": " << reply_usr << std::endl;
		}
		std::cout << std::setfill(' ') << std::setw(7) << " Tweet" << std::setw(0) << ": " << tweet_txt << '\r' << std::endl;  // Eric McDonald: The assignment requires a carriage return to be here for some reason...
	}

}  // namespace sict
