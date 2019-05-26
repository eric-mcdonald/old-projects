#include "ItemSet.h"

#include <sstream>
#include <iomanip>

#include "Utilities.h"

namespace sict {

	static Utilities utils;

	ItemSet::ItemSet(const std::string &inventory_entry) {
		utils.setDelimiter('|');
		size_t next_pos = 0;
		name = utils.extractToken(inventory_entry, next_pos);
		std::istringstream serial_stream(utils.extractToken(inventory_entry, next_pos));
		serial_stream >> serial_num;
		std::istringstream quantity_stream(utils.extractToken(inventory_entry, next_pos));
		quantity_stream >> quantity;
		desc = utils.extractToken(inventory_entry, next_pos);
		if (name.length() > utils.getFieldWidth()) {
			utils.setFieldWidth(name.length());
		}
	}
	const std::string &ItemSet::getName() const {
		return name;
	}
	const unsigned int ItemSet::getQuantity() const {
		return quantity;
	}
	const unsigned int ItemSet::getSerialNumber() const {
		return serial_num;
	}
	ItemSet &ItemSet::operator--() {
		if (quantity != 0) {
			--quantity;
			++serial_num;
		}
		return *this;
	}
	void ItemSet::display(std::ostream &out, bool ext_data) const {
		out << std::left << std::setw(utils.getFieldWidth() + 1) << std::setfill(' ') << name << std::setw(0) << "[" << std::setfill('0') << std::setw(5) << serial_num << std::setw(0) << "]";
		if (ext_data) {
			out << " Quantity " << std::setfill(' ') << std::setw(4) << quantity << std::setw(0) << "Description: " << desc << std::endl;
		}

	}

}  // namespace sict
