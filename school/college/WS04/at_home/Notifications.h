#ifndef NOTIFICATIONS_H_
#define NOTIFICATIONS_H_

#include <iostream>

#include "Message.h"

namespace sict {

	class Notifications {
		Message **messages;
		size_t current_sz, max_sz;
	public:
		Notifications();
		Notifications(size_t);
		Notifications(const Notifications &);
		Notifications(Notifications &&);
		~Notifications();
		Notifications &operator+=(const Message &);
		Notifications &operator-=(const Message &);
		void display(std::ostream &out) const;
		size_t size() const;
	};
	
	std::ostream &operator<<(std::ostream &, const Notifications &);

}  // namespace sict

#endif  // NOTIFICATIONS_H_
