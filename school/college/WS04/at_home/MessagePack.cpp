#include "MessagePack.h"

namespace sict {

	MessagePack::MessagePack() : messages(nullptr), messages_sz(0) {}
	MessagePack::MessagePack(Message **messages_, size_t messages_sz_) : MessagePack() {
		if (messages_ != nullptr && messages_sz_ != 0) {
			messages_sz = messages_sz_;
			messages = new Message*[messages_sz_];
			memcpy(messages, messages_, messages_sz_ * sizeof(Message*));
		}
	}
	MessagePack::MessagePack(const MessagePack &src) {
		if (messages != nullptr) {
			delete[] messages;
			messages = nullptr;
			messages_sz = 0;
		}
		if (src.messages != nullptr && src.messages_sz != 0) {
			messages_sz = src.messages_sz;
			messages = new Message*[src.messages_sz];
			memcpy(messages, src.messages, src.messages_sz * sizeof(Message*));
		}
	}
	MessagePack::MessagePack(MessagePack &&src) {
		messages = src.messages;
		messages_sz = src.messages_sz;
		src.messages = nullptr;
		src.messages_sz = 0;
	}
	MessagePack::~MessagePack() {
		if (messages != nullptr) {
			delete[] messages;
			messages_sz = 0;
		}
	}
	void MessagePack::display(std::ostream &out) const {
		for (size_t i = 0; i < messages_sz; ++i) {
			if (messages[i] == nullptr) {
				continue;
			}
			messages[i]->display(out);
		}
	}
	size_t MessagePack::size() const {
		return messages_sz;
	}
	std::ostream &operator<<(std::ostream &out, const MessagePack &pack) {
		pack.display(out);
		return out;
	}

}  // namespace sict
