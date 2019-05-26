#include "Notifications.h"

namespace sict {

	Notifications::Notifications() : messages(nullptr), current_sz(0), max_sz(0) {}
	Notifications::Notifications(size_t max_sz_) : Notifications() {
		if (max_sz_ > 0) {
			max_sz = max_sz_;
			messages = new Message*[max_sz_];
		}
	}
	Notifications::Notifications(const Notifications &src) {
		if (messages != nullptr) {
			messages = { 0 };
			delete[] messages;
			messages = nullptr;
			current_sz = max_sz = 0;
		}
		if (src.max_sz > 0) {
			max_sz = src.max_sz;
			current_sz = src.current_sz;
			messages = new Message*[src.max_sz];
			memcpy(messages, src.messages, src.max_sz * sizeof(Message*));
		}
	}
	Notifications::Notifications(Notifications &&src) {
		messages = src.messages;
		max_sz = src.max_sz;
		current_sz = src.current_sz;
		src.messages = nullptr;
		src.max_sz = src.current_sz = 0;
	}
	Notifications::~Notifications() {
		if (messages != nullptr) {
			messages = { 0 };
			delete[] messages;
			messages = nullptr;
			current_sz = max_sz = 0;
		}
	}
	Notifications &Notifications::operator+=(const Message &msg) {
		if (msg.empty() || current_sz >= max_sz) {
			return *this;
		}
		messages[current_sz++] = &const_cast<Message&>(msg);
		return *this;
	}
	Notifications &Notifications::operator-=(const Message &msg) {
		if (msg.empty()) {
			return *this;
		}
		for (size_t i = 0; i < current_sz; ++i) {
			if (messages[i] == &msg) {
				messages[i] = nullptr;
				--current_sz;
				break;
			}
		}
		return *this;
	}
	void Notifications::display(std::ostream &out) const {
		for (size_t i = 0; i < current_sz; ++i) {
			if (messages[i] == nullptr) {
				continue;
			}
			messages[i]->display(out);
		}
	}
	size_t Notifications::size() const {
		return current_sz;
	}
	std::ostream &operator<<(std::ostream &out, const Notifications &notifications) {
		notifications.display(out);
		return out;
	}

}  // namespace sict
