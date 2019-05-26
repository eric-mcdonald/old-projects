#ifndef MESSAGE_H_
#define MESSAGE_H_

#include <string>
#include <iostream>

namespace sict {

	class Message {
		std::string tweet_usr, reply_usr, tweet_txt;
	public:
		Message();
		Message(const std::string &);
		bool empty() const;
		void display(std::ostream &) const;
	};

}  // namespace sict

#endif  // MESSAGE_H_
