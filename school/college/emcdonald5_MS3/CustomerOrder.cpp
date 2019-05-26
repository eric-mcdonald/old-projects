#include "CustomerOrder.h"

#include <vector>
#include <iomanip>

#include "Utilities.h"

namespace sict {

	static Utilities utils;

	bool ItemOrder::operator==(const ItemOrder &rhs) {
		return name == rhs.name;
	}
	bool ItemOrder::operator!=(const ItemOrder &rhs) {
		return !(*this == rhs);
	}
	bool ItemOrder::operator==(const ItemSet &rhs) {
		return name == rhs.getName();
	}
	bool ItemOrder::operator!=(const ItemSet &rhs) {
		return !(*this == rhs);
	}
	CustomerOrder::CustomerOrder() : item_orders(nullptr), item_orders_sz(0) {}
	CustomerOrder::CustomerOrder(const std::string &order_entry) : CustomerOrder() {
		utils.setDelimiter('|');
		size_t next_pos = 0;
		customer_name = utils.extractToken(order_entry, next_pos);
		// Eric McDonald: The professor wants extractToken to return the string parameter on error, and throw the string on a different error?
		if (customer_name == order_entry) {
			throw order_entry;
		}
		prod_name = utils.extractToken(order_entry, next_pos);
		if (prod_name == order_entry) {
			throw order_entry;
		}
		std::string first_item_name = utils.extractToken(order_entry, next_pos);
		if (first_item_name == order_entry) {
			throw order_entry;
		}
		std::vector<std::string> entries;
		entries.push_back(first_item_name);
		std::string token;
		while (next_pos < order_entry.length() - 1) {
			token = utils.extractToken(order_entry, next_pos);
			entries.push_back(token);
		}
		item_orders = new ItemOrder[item_orders_sz = entries.size()];
		for (size_t i = 0; i < item_orders_sz; ++i) {
			item_orders[i].name = entries[i];  // Eric McDonald: Copies entries to a dynamically-allocated array, as according to the assignment specifications.
			item_orders[i].serial_num = 0;
			item_orders[i].fulfilled = false;
		}
	}
	CustomerOrder::CustomerOrder(const CustomerOrder &src) {
		customer_name = src.customer_name;
		prod_name = src.prod_name;
		item_orders = new ItemOrder[item_orders_sz = src.item_orders_sz];
		for (size_t i = 0; i < item_orders_sz; ++i) {
			item_orders[i] = src.item_orders[i];
		}
	}
	CustomerOrder::CustomerOrder(CustomerOrder &&src) {
		item_orders = src.item_orders;
		item_orders_sz = src.item_orders_sz;
		customer_name = src.customer_name;
		prod_name = src.prod_name;
		src.item_orders = nullptr;
		src.item_orders_sz = 0;
		src.customer_name = src.prod_name = "";
	}
	CustomerOrder::~CustomerOrder() {
		if (item_orders != nullptr) {
			delete[] item_orders;
			item_orders = nullptr;
		}
		item_orders_sz = 0;
	}
	void CustomerOrder::fillItem(ItemSet &item, std::ostream &out) {
		ItemOrder *order = nullptr;
		for (size_t i = 0; i < item_orders_sz; ++i) {
			ItemOrder &item_order = item_orders[i];
			if (item_order == item) {
				order = &item_orders[i];
				break;
			}
		}
		if (order == nullptr) {
			return;
		}
		auto order_serial = order->serial_num == 0 ? item.getSerialNumber() : order->serial_num;  // Eric McDonald: order->serial_num may not have been set yet.
		if (isItemFilled(item.getName())) {
			out << "Unable to fill " << customer_name << " [" << prod_name << "][" << item.getName() << "][" << order_serial << "] already filled" << std::endl;
		}
		else if (item.getQuantity() == 0) {
			out << "Unable to fill " << customer_name << " [" << prod_name << "][" << item.getName() << "][" << order_serial << "] out of stock" << std::endl;
		}
		else if (*order == item && !order->fulfilled) {
			order->fulfilled = true;
			order->serial_num = item.getSerialNumber();
			--item;
			out << "Filled " << customer_name << " [" << prod_name << "][" << item.getName() << "][" << order->serial_num << "]" << std::endl;
		}
	}
	bool CustomerOrder::isFilled() const {
		for (size_t i = 0; i < item_orders_sz; ++i) {
			if (!item_orders[i].fulfilled) {
				return false;
			}
		}
		return true;
	}
	bool CustomerOrder::isItemFilled(const std::string &item_name) const {
		for (size_t i = 0; i < item_orders_sz; ++i) {
			if (item_orders[i].name == item_name) {
				return item_orders[i].fulfilled;
			}
		}
		return false;
	}
	std::string CustomerOrder::getNameProduct() {
		return customer_name + " [" + prod_name + "]";
	}
	void CustomerOrder::display(std::ostream &out, bool in_detail) const {
		out << std::left << std::setw(utils.getFieldWidth() - 4) << std::setfill(' ');
		out << customer_name << std::setw(0) << " [" << prod_name << "]" << std::endl;
		for (size_t i = 0; i < item_orders_sz; ++i) {
			out << std::left << std::setw(utils.getFieldWidth() - 3) << std::setfill(' ') << " " << std::setw(0);
			if (in_detail) {
				out << "[" << item_orders[i].serial_num << "] " << item_orders[i].name << " - " << (item_orders[i].fulfilled? "FILLED" : "MISSING") << std::endl;
			}
			else {
				out << item_orders[i].name << std::endl;
			}
		}
	}
	CustomerOrder &CustomerOrder::operator=(CustomerOrder &&src) {
		if (this != &src) {
			item_orders = src.item_orders;
			item_orders_sz = src.item_orders_sz;
			customer_name = src.customer_name;
			prod_name = src.prod_name;
			src.item_orders = nullptr;
			src.item_orders_sz = 0;
			src.customer_name = src.prod_name = "";
		}
		return *this;
	}
	CustomerOrder &CustomerOrder::operator=(const CustomerOrder &src) {
		if (this != &src) {
			customer_name = src.customer_name;
			prod_name = src.prod_name;
			if (item_orders != nullptr) {
				delete item_orders;
			}
			item_orders = new ItemOrder[item_orders_sz = src.item_orders_sz];
			for (size_t i = 0; i < item_orders_sz; ++i) {
				item_orders[i] = src.item_orders[i];
			}
		}
		return *this;
	}

}  // namespace sict
