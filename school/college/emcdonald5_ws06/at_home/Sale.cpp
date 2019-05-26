#include "Sale.h"

#include <fstream>
#include <sstream>
#include <string>
#include <iomanip>

#include "Product.h"

namespace sict {

	Sale::Sale(const char *filename) {
		std::ifstream file(filename);
		if (!file.is_open()) {
			std::ostringstream err_stream;
			err_stream << "Failed to open file: " << filename;
			throw err_stream.str();
		}
		while (file.good()) {
			iProduct *product = readRecord(file);
			if (product != nullptr) {
				products.push_back(product);
			}
		}
	}
	Sale::~Sale() {
		for (auto &it : products) {
			delete it;
		}
		products.clear();
	}
	void Sale::display(std::ostream &out) const {
		out << std::setw(0) << "Product No       Cost Taxable" << std::endl;
		double total = 0.00;
		for (auto &it : products) {
			it->display(out);
			total += it->price();
		}
		out << std::setfill(' ') << std::setw(kFieldWidth) << "Total" << std::setw(0) << " " << std::setw(kFieldWidth) << std::fixed << std::setprecision(2) << total << std::endl;
	}

}  // namespace sict
