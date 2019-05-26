#include "Product.h"

#include <iomanip>
#include <fstream>
#include <string>

namespace sict {

	Product::Product(unsigned int id_, double prod_price_) : id(id_), prod_price(prod_price_) {}
	double Product::price() const {
		return prod_price;
	}
	void Product::display(std::ostream &out) const {
		out << std::setfill(' ') << std::setw(kFieldWidth) << id << std::setw(0) << " " << std::setw(kFieldWidth) << std::fixed << std::setprecision(2) << prod_price << std::endl;
	}
		std::ostream &operator<<(std::ostream &out, const iProduct &rhs) {
		rhs.display(out);
		return out;
	}
	iProduct *readRecord(std::ifstream& file) {
		while (file.good()) {
			std::string entry;
			std::getline(file, entry);
			if (entry.empty()) {
				continue;
			}
			size_t separator = entry.find_first_of(" ");
			return new Product(stoul(entry.substr(0, separator - 0)), stod(entry.substr(separator + 1)));
		}
		return nullptr;
	}

}  // namespace sict
