#ifndef PRODUCT_H_
#define PRODUCT_H_

#include "iProduct.h"

namespace sict {

	class Product : public iProduct {
		double prod_price;
	protected:
		unsigned int id;
	public:
		Product(unsigned int, double);
		virtual double price() const override;
		virtual void display(std::ostream &) const override;
	};
	enum class TaxTypes {
		kHst,
		kPst,
	};
	class TaxableProduct : public Product {
		TaxTypes tax_type;
	public:
		TaxableProduct(unsigned int, double, TaxTypes);
		double price() const override;
		void display(std::ostream &) const override;
	};

	static constexpr unsigned int kFieldWidth = 10;

}  // namespace sict

#endif  // PRODUCT_H_
