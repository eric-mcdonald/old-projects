#ifndef STATION_H_
#define STATION_H_

#include <queue>
#include <string>
#include <iostream>

#include "CustomerOrder.h"
#include "ItemSet.h"

namespace sict {

	class Station {
		std::vector<CustomerOrder> orders;
		ItemSet items;
	public:
		Station(const std::string &/*inv_entry*/);
		Station(const Station &) = delete;
		Station(Station &&) = delete;
		void display(std::ostream &) const;
		void fill(std::ostream &);
		const std::string &getName() const;
		bool hasAnOrderToRelease();
		bool pop(CustomerOrder &/*ready_order*/);
		void validate(std::ostream &) const;
		Station &operator--();
		Station &operator+=(CustomerOrder &&);
		Station &operator=(const Station &) = delete;
		Station &operator=(Station &&) = delete;
	};

}  // namespace sict

#endif  // STATION_H_
