#include "LineManager.h"

namespace sict {

	LineManager::LineManager(std::vector<Station*> &stations_, std::vector<size_t> &station_indexes, std::vector<CustomerOrder> &orders, size_t first_station, std::ostream &out) : orders_sz(0) {
		for (auto &it : orders) {
			if (!it.isFilled()) {
				unfilled_orders.push_back(std::move(it));
				++orders_sz;
			}
		}
		size_t last_station = first_station;
		for (size_t i = 0; i < station_indexes.size(); ++i) {
			stations.push_back(stations_[last_station]);
			last_station = station_indexes[last_station];
		}
	}
	void LineManager::display(std::ostream &out) const {
		std::cout << "COMPLETED ORDERS" << std::endl;
		for (auto &it : filled_orders) {
			it.display(out, true);
		}
		std::cout << "INCOMPLETE ORDERS" << std::endl;
		for (auto &it : unfilled_orders) {
			it.display(out, true);
		}
	}
	bool LineManager::run(std::ostream &out) {
		size_t order_count = 0;
		while (order_count != orders_sz) {
			if (!unfilled_orders.empty()) {
				CustomerOrder o = unfilled_orders.front();
				*stations.front() += std::move(o);
			}
			for (size_t i = 0; i < stations.size(); i++) {
				Station &station = *stations[i];
				station.fill(out);
				CustomerOrder ready_order;
				if (!station.pop(ready_order)) {
					continue;
				}
				if (i != stations.size() - 1) {
					out << "--> " << ready_order.getNameProduct() << " moved from " << station.getName() << " to " << stations[i + 1]->getName() << std::endl;
					*stations[i + 1] += std::move(ready_order);
				}
				else {
					unfilled_orders.erase(unfilled_orders.begin());
					if (!ready_order.isFilled()) {
						out << "--> " << ready_order.getNameProduct() << " moved from " << station.getName() << " to Incomplete Set" << std::endl;
						unfilled_orders.push_back(std::move(ready_order));
					}
					else {
						out << "--> " << ready_order.getNameProduct() << " moved from " << station.getName() << " to Completed Set" << std::endl;
						filled_orders.push_back(std::move(ready_order));
					}
					++order_count;
				}
			}
		}
		return order_count == orders_sz;
	}

}  // namespace sict
