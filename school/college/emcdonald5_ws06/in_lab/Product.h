#ifndef PRODUCT_H_
#define PRODUCT_H_

#include "iProduct.h"

namespace sict {

	class Product : public iProduct {
		unsigned int id;
		double prod_price;
	public:
		Product(unsigned int, double);
		double price() const override;
		void display(std::ostream &) const override;
	};

	static constexpr unsigned int kFieldWidth = 10;

}  // namespace sict

#endif  // PRODUCT_H_
