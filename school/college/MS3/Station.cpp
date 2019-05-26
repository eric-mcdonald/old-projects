#include "Station.h"

namespace sict {

	Station::Station(const std::string &inv_entry) : items(inv_entry) {}
	void Station::display(std::ostream &out) const {
		items.display(out, true);
	}
	void Station::fill(std::ostream &out) {
		if (orders.empty()) {
			return;
		}
		CustomerOrder &last_order = orders.back();
		last_order.fillItem(items, out);
	}
	const std::string &Station::getName() const {
		return items.getName();
	}
	bool Station::hasAnOrderToRelease() {
		return !orders.empty() && (orders.front().isItemFilled(items.getName()) || items.getQuantity() == 0);
	}
	bool Station::pop(CustomerOrder &ready_order) {
		if (orders.empty()) {
			return false;
		}
		ready_order = std::move(orders.front());
		orders.erase(orders.begin());
		return true;
	}
	void Station::validate(std::ostream &out) const {
		out << "getName(): " << getName() << std::endl;
		out << "getSerialNumber(): " << items.getSerialNumber() << std::endl;
		out << "getQuantity(): " << items.getQuantity() << std::endl;
	}
	Station &Station::operator--() {
		--items;
		return *this;
	}
	Station &Station::operator+=(CustomerOrder &&order) {
		orders.push_back(std::move(order));
		return *this;
	}

}  // namespace sict
