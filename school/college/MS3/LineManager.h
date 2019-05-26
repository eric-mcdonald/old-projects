#ifndef LINE_MANAGER_H_
#define LINE_MANAGER_H_

#include <iostream>
#include <vector>

#include "Station.h"
#include "CustomerOrder.h"

namespace sict {

	class LineManager {
		std::vector<Station*> stations;
		std::vector<CustomerOrder> unfilled_orders, filled_orders;
		size_t orders_sz;
	public:
		LineManager(std::vector<Station*> &, std::vector<size_t> &, std::vector<CustomerOrder> &, size_t, std::ostream &/*out*/);
		void display(std::ostream &) const;
		bool run(std::ostream &);
	};

}  // namespace sict

#endif  // LINE_MANAGER_H_
