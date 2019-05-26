#ifndef ITEM_SET_H_
#define ITEM_SET_H_

#include <string>
#include <iostream>

namespace sict {

	class ItemSet {
		std::string name, desc;
		unsigned int serial_num;
		unsigned int quantity;
	public:
		ItemSet(const std::string &);
		const std::string &getName() const;
		const unsigned int getQuantity() const;
		const unsigned int getSerialNumber() const;
		ItemSet &operator--();
		void display(std::ostream &, bool /*ext_data*/) const;
	};

}  // namespace sict

#endif  // ITEM_SET_H_
