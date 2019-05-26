#include "Product.h"

#include <iomanip>
#include <fstream>
#include <sstream>

namespace sict {

	static constexpr double kHstMult = 1.13, kPstMult = 1.08;

	Product::Product(unsigned int id_, double prod_price_) : id(id_), prod_price(prod_price_) {}
	double Product::price() const {
		return prod_price;
	}
	void Product::display(std::ostream &out) const {
		out << std::setfill(' ') << std::setw(kFieldWidth) << id << std::setw(0) << " " << std::setw(kFieldWidth) << std::fixed << std::setprecision(2) << prod_price << std::endl;
	}
	TaxableProduct::TaxableProduct(unsigned int id_, double prod_price_, TaxTypes tax_type_) : Product(id_, prod_price_), tax_type(tax_type_) {}
	double TaxableProduct::price() const {
		switch (tax_type) {
		case TaxTypes::kHst:
			return Product::price() * kHstMult;
		case TaxTypes::kPst:
			return Product::price() * kPstMult;
		}
		return Product::price();
	}
	void TaxableProduct::display(std::ostream &out) const {
		out << std::setfill(' ') << std::setw(kFieldWidth) << id << std::setw(0) << " " << std::setw(kFieldWidth) << std::fixed << std::setprecision(2);
		switch (tax_type) {
		case TaxTypes::kHst:
			out << price() / kHstMult;
			break;
		case TaxTypes::kPst:
			out << price() / kPstMult;
			break;
		}
		out << std::setw(0) << " ";
		switch (tax_type) {
		case TaxTypes::kHst:
			out << "HST";
			break;
		case TaxTypes::kPst:
			out << "PST";
			break;
		}
		out << std::endl;
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
			if (entry[entry.length() - 1] == 'H') {
				return new TaxableProduct(stoul(entry.substr(0, separator - 0)), stod(entry.substr(separator + 1)), TaxTypes::kHst);
			}
			if (entry[entry.length() - 1] == 'P') {
				return new TaxableProduct(stoul(entry.substr(0, separator - 0)), stod(entry.substr(separator + 1)), TaxTypes::kPst);
			}
			return new Product(stoul(entry.substr(0, separator - 0)), stod(entry.substr(separator + 1)));
		}
		return nullptr;
	}

}  // namespace sict
