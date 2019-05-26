#ifndef CUSTOMER_ORDER_H_
#define CUSTOMER_ORDER_H_

#include <iostream>

#include "ItemSet.h"

namespace sict {

	class ItemOrder {
	public:
		std::string name;
		unsigned int serial_num;
		bool fulfilled;
		bool operator==(const ItemOrder &);
		bool operator!=(const ItemOrder &);
		bool operator==(const ItemSet &);
		bool operator!=(const ItemSet &);
	};
	class CustomerOrder {
		std::string customer_name;
		std::string prod_name;
		ItemOrder *item_orders;
		size_t item_orders_sz;
	public:
		CustomerOrder();
		CustomerOrder(const std::string &);
		CustomerOrder(const CustomerOrder &);
		CustomerOrder(CustomerOrder &&);
		~CustomerOrder();
		void fillItem(ItemSet &, std::ostream &);
		bool isFilled() const;
		bool isItemFilled(const std::string &) const;
		std::string getNameProduct();
		void display(std::ostream &, bool /*in_detail*/ = false) const;
		CustomerOrder &operator=(const CustomerOrder &);
		CustomerOrder &operator=(CustomerOrder &&);
	};

}  // namespace sict

#endif  // CUSTOMER_ORDER_H_
