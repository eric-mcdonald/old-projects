#ifndef MESSAGE_PACK_H_
#define MESSAGE_PACK_H_

#include <iostream>

#include "Message.h"

namespace sict {

	class MessagePack {
		Message **messages;
		size_t messages_sz;
	public:
		MessagePack();
		MessagePack(Message **, size_t);
		MessagePack(const MessagePack &);
		MessagePack(MessagePack &&);
		~MessagePack();
		void display(std::ostream &out) const;
		size_t size() const;
	};

	std::ostream &operator<<(std::ostream &, const MessagePack &);

}  // namespace sict

#endif  // MESSAGE_PACK_H_
